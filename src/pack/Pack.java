package pack;

//Hao Hua, Southeast University, whitegreen@163.com

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class Pack {
	public ArrayList<Strip> fixs = new ArrayList<Strip>();
	public ArrayList<Strip> movs = new ArrayList<Strip>();
	public Convex cntConvex;
	private final double[][] trigos;
	private final int rotSteps;
	private static final double PI = Math.PI;
	public final double WID, HEI;
	private static final double areasc = 1E-6; //
	private final double preferX; // 0.501 or 1
	private final double offset;
	

	public Pack clone(double WID, double HEI) {
		Pack np = new Pack(trigos, rotSteps, WID, HEI, preferX, offset);
		np.movs = movs;
		np.cntConvex = null;
		return np;
	}

	public Pack(double[][] trigos, int rotSteps, double WID, double HEI, double preferX, double offset) {
		this.trigos = trigos;
		this.rotSteps = rotSteps;
		this.WID = WID;
		this.HEI = HEI;
		this.preferX = preferX;
		this.offset = offset;

		
		float[][] arr = { { 1, 2 }, { 2, 0 }, { 3, -2 } };
		Arrays.sort(arr, new Comparator<float[]>() {
			public int compare(float[] a, float[] b) {
				if (a[0] < b[0])
					return -1;
				else if (a[0] > b[0])
					return 1;
				else
					return 0;
			}
		});

	}

	public boolean isEmpty() {
		return movs.isEmpty();
	}

	public Pack(ArrayList<double[][]> polys, double offset, Double segment_max_length, int rotSteps, double WID, double HEI, double preferX) {
		for (int i = 0; i < polys.size(); i++) {
			double[][] poly = polys.get(i);
			Strip strip = new Strip(i, poly, offset, segment_max_length);
			movs.add(strip);
		}
		Collections.sort(movs); // ascend, remove form the end (largest)
		if (0 > preferX || 1 < preferX)
			throw new RuntimeException();
		this.preferX = preferX;
		

		this.rotSteps = rotSteps;
		trigos = new double[rotSteps][];
		for (int i = 0; i < rotSteps; i++) {
			double theta = i * 2 * PI / rotSteps;
			trigos[i] = new double[] { Math.cos(theta), Math.sin(theta) };
		}
		this.WID = WID;
		this.HEI = HEI;
		this.offset = offset;
		System.out.println(WID + "*" + HEI);
	}
	
	private void place1stStrip(HashMap<Integer, Property> properties) {
		
		int rotid = -1;
		double minArea= 1000000000;
		if (!movs.isEmpty()) {
			int sid = movs.size() - 1; // ***************************************** last one
			while(sid >0 & movs.get(sid) == null) {
				sid -=1;
			}
			
			if (movs.get(sid) != null) {
				
				if (cntConvex != null) {
					placeAnotherStrip_Abey(sid, properties);
				} else {
					
					Strip first = movs.get(sid);
					for (int i = 0; i < rotSteps; i++) {
						double[][] tp = M.rotate(trigos[i], first.outps);
						double[] bd = M.boundBox(tp); // minx, maxx, miny, maxy
						if (bd[1] - bd[0] > WID || bd[3] - bd[2] > HEI)
							continue;
						double area;
						
						area = areasc * (bd[1] - bd[0]) * (bd[3] - bd[2]);
						double[] center = M.mean(tp);
						double len = preferX * (center[0] - bd[0]) + (1 - preferX) * (center[1] - bd[2]);
						area *= len;	
						if (minArea > area) {
							minArea = area;
							rotid = i;
						}
					}
					if (rotid==-1) {
						System.out.println("Not rotation feasible");
						rotid = 0;
					}
					double[][] tp = M.rotate(trigos[rotid], first.outps);
					double[] bd = M.boundBox(tp);
					first.fix_rotate_move(trigos[rotid], new double[] { -bd[0], -bd[2] });
					
					Convex new_con = null;
					if (properties.containsKey(first.id)){
						new_con = setProperty((Property) properties.get(first.id), first, true);

					} else {
						new_con = new Convex(first.outps);
					}
					
					movs.set(sid, null);
					fixs.add(first);
					cntConvex = new_con;
					
				}
			}
		}
		
		
		
		
	}

	public void packOneSheet(boolean Abey, HashMap<Integer, Property> properties) {
		place1stStrip(properties);
		int size = movs.size();
		for (int i = 0; i < size; i++) {
			if (Abey)
				placeAnotherStrip_Abey(size - 1 - i, properties);
			else
				placeAnotherStrip_Dalalah(size - 1 - i);
		}
		ArrayList<Strip> list = new ArrayList<Strip>();
		for (Strip stp : movs) {
			if (null == stp)
				continue;
			list.add(stp);
		}
		movs = list;
		System.out.println(fixs.size() + "->" + movs.size());
	}

	public double leftOverArea() {
		double sum = 0;
		for (Strip strip : movs)
			sum += strip.inarea;
		return sum;
	}
	
	
	private boolean placeAnotherStrip_Abey(int sid, HashMap<Integer, Property> properties) {
		Strip stp = movs.get(sid);
		if (stp != null) {
			double min = 1000000000;
			double[] min_cossin = null;
			double[] min_trans = null;
			Convex min_con = null;
			double[][] opl = stp.outps;
			for (int i = 0; i < opl.length; i++) { // each vertex of new strip
				double[] p = opl[i];
				double[] d0 = M.sub(opl[(i - 1 + opl.length) % opl.length], p);
				double[] d2 = M.sub(opl[(i + 1) % opl.length], p);
				double mag0 = M.mag(d0);
				double mag2 = M.mag(d2);
				double cb0 = d0[0] / mag0;
				double sb0 = d0[1] / mag0;
				double cb2 = d2[0] / mag2;
				double sb2 = d2[1] / mag2;

				for (Strip fixed : fixs) {
					double[][] fopl = fixed.outps;
					for (int j = 0; j < fopl.length; j++) { // each vertex of each fixed strip
						double[] v = fopl[j];
						double[] t0 = M.sub(fopl[(j - 1 + fopl.length) % fopl.length], v);
						double[] t2 = M.sub(fopl[(j + 1) % fopl.length], v);
						double m0 = M.mag(t0);
						double m2 = M.mag(t2);
						double ca0 = t0[0] / m0;
						double sa0 = t0[1] / m0;
						double ca2 = t2[0] / m2;
						double sa2 = t2[1] / m2;

						for (int h = 0; h < 2; h++) { // two angles
							double[] cossin;
							if (0 == h)
								cossin = new double[] { ca0 * cb2 + sa0 * sb2, sa0 * cb2 - ca0 * sb2 }; // a0 - b2
							else
								cossin = new double[] { ca2 * cb0 + sa2 * sb0, sa2 * cb0 - ca2 * sb0 };// a2 - b0

							double[][] rot_opl = M.rotate(cossin, opl);
							double[] trans = M.sub(v, rot_opl[i]); // *****************
							double[][] trans_rot_outpoly = M.move(trans, rot_opl);
							if (feasible(trans_rot_outpoly)) {
								double[][] trans_rot_inpoly = M.move(trans, M.rotate(cossin, stp.inps));
								Convex tmpcon = cntConvex.clone();
								for (double[] trp : trans_rot_inpoly)
									tmpcon.increment_hull(trp);

								double conarea = areasc * M.areaAbs(tmpcon.convex);
								double[] center = M.mean(trans_rot_inpoly);
								double area = conarea * (preferX * Math.abs(center[0]) + (1 - preferX) * Math.abs(center[1]));
								
								if (min > area) {
									min = area;
									min_cossin = cossin;
									min_trans = trans;
									min_con = tmpcon;
								}
							}
						}// for h
					}
				}
			} // for each vertex of new strip
			if (null == min_cossin) // no solution, stop
				return false;
			stp.fix_rotate_move(min_cossin, min_trans);
			
			
			Convex new_con = min_con;
			if (properties.containsKey(stp.id)){
				new_con = setProperty((Property) properties.get(stp.id), stp, false);
			}
	
			movs.set(sid, null);
			fixs.add(stp);
			cntConvex = new_con; // updating the convex hull if property set
//			cntConvex = min_con;
			return true;
		} else {
			return false;
		}
		
	}
	
	private Convex setProperty(Property prop, Strip polygon, boolean first_polygon) {
		System.out.printf("Setting properties for :: %d \n",prop.getId());
		
		Tuple tmpcon_tuple = new Tuple(null, true);
		
		if (prop.getX_location() >= 0 && prop.getY_location() >= 0) {
			tmpcon_tuple = translatePolygon(prop, polygon, first_polygon, false);

		} else {
			tmpcon_tuple.setConvex(rotatePolygon(prop, polygon, first_polygon)); 
			
		}
		return tmpcon_tuple.getConvex();
	}
	
	public Tuple translatePolygon(Property prop, Strip polygon, boolean first_polygon, boolean from_user_fix) {
		
		ArrayList<double[]> rot_cossin = new ArrayList<double[]>();
		
		if (prop.getRotation() >= 0) {
			rot_cossin.add(new double[] {1,0});
			if ((prop.getRotation() % 4) == 1) {
				rot_cossin.get(0)[0] = 0;
				rot_cossin.get(0)[1] = 1;
			} else if ((prop.getRotation() % 4) == 2) {
				rot_cossin.get(0)[0] = -1;
				rot_cossin.get(0)[1] = 0;
			} else if ((prop.getRotation() % 4) == 3) {
				rot_cossin.get(0)[0] = 0;
				rot_cossin.get(0)[1] = -1;
			} 
			
		} else {
			rot_cossin.add(new double[] {0,1});
			rot_cossin.add(new double[] {-1,0});
			rot_cossin.add(new double[] {0,-1});
			
		}
		
		double min_area = 1000000000;
		double[] final_mv = null;
		double[] final_rot = null;
		Convex final_con = null;
		
		ArrayList<double[]> possible_trans = new ArrayList<double[]>();
		ArrayList<double[]> possible_rots = new ArrayList<double[]>();
		for (double[] cs : rot_cossin) {
			double[][] rot_poly = M.rotate(cs, polygon.outps);
			double[] v = {prop.getX_location(), prop.getY_location()};
			for (int i = 0; i < rot_poly.length; i++) {
				double[] trans = M.sub(v, rot_poly[i]);
				double[][] trans_rot_poly = M.move(trans, rot_poly);
				if (feasible(trans_rot_poly)) {
					possible_trans.add(trans);
					possible_rots.add(cs);
				}	
			}		
		}
		
		for (int i = 0; i < possible_trans.size(); i++) {
			double[] t = possible_trans.get(i);
			double[] r = possible_rots.get(i);
			double[][] rp = M.rotate(r, polygon.outps);
			double[][] trp = M.move(t, rp);
			Convex trp_con = null;
			if (first_polygon) {
				trp_con = new Convex(trp);
			} else {
				trp_con = cntConvex.clone();
				for (double[] v : trp)
					trp_con.increment_hull(v);
			}
			
			
			double conarea = areasc * M.areaAbs(trp_con.convex);
			double[] center = M.mean(trp);
			double area = conarea * (preferX * Math.abs(center[0]) + (1 - preferX) * Math.abs(center[1]));
	
			if (min_area >= area) {
				min_area = area;
				final_mv = t;
				final_rot = r;
				final_con = trp_con;
			}	
		}
		Tuple result = new Tuple(final_con, true);
		
		if (final_con != null) {
			System.out.println("Location is possible");
			polygon.inps = M.move(final_mv, M.rotate(final_rot, polygon.inps));
			polygon.outps = M.move(final_mv, M.rotate(final_rot, polygon.outps));
			
		} else {
			System.out.println("Location is not possible from any vertex with rotation");
			if (from_user_fix) {
				result.setStatus(false);
			} else {
				if (first_polygon) {
					final_con = new Convex(polygon.inps);
				} else {
					final_con = cntConvex.clone();
				}
				result.setConvex(final_con);
			}		
		}
	
		
		return result;
	}
	
	
	private Convex rotatePolygon(Property prop, Strip polygon, boolean first_polygon) {
		Convex tmpcon = null; 
		if (first_polygon) {
			tmpcon = new Convex(polygon.inps);
		} else {
			tmpcon = cntConvex.clone();
		}
		 
		if (prop.getRotation() > 0) {
			System.out.println("Some Rotation is required");
			double[] cossin = new double[] {1,0};
			if (prop.getRotation() % 4 == 1) {
				cossin[0] = 0;
				cossin[1] = 1;
			} else if (prop.getRotation() % 4 == 2) {
				cossin[0] = -1;
				cossin[1] = 0;
			} else if (prop.getRotation() % 4 == 3) {
				cossin[0] = 0;
				cossin[1] = -1;
			} 
			
			double min_area = 1000000000;
			double[] final_mv = null;
			Convex final_con = null;
	
			double[][] rot_poly = M.rotate(cossin, polygon.outps);
			ArrayList<double[]> possible_trans = new ArrayList<double[]>();
			
			for (int i=0; i<rot_poly.length; i++) {
				for (Strip fixed : fixs) {
					double[][] fopl = fixed.outps;
					for(int j=0;j<fopl.length;j++) {
						double[] v = fopl[j];			
						double[] trans = M.sub(v, rot_poly[i]);
						double[][] trans_rot_poly = M.move(trans, rot_poly);
						if (feasible(trans_rot_poly)) {
//							System.out.printf("\n possible using polygon vertex (%f,%f) at location (%f, %f)", rot_poly[i][0], rot_poly[i][1], v[0], v[1]);
							possible_trans.add(trans);
						}
					}
				}
			}

			for (double[] t : possible_trans) {
				double[][] t_rot_poly = M.move(t, rot_poly);
				Convex trot_con = null;
				if (first_polygon) {
					trot_con = new Convex(t_rot_poly);
				} else {
					trot_con = cntConvex.clone();
					for (double[] trp : t_rot_poly)
						trot_con.increment_hull(trp);
				}
				 
									
				double conarea = areasc * M.areaAbs(trot_con.convex);
				double[] center = M.mean(t_rot_poly);
				double area = conarea * (preferX * Math.abs(center[0]) + (1 - preferX) * Math.abs(center[1]));
				
				if (min_area >= area) {
					min_area = area;
					final_mv = t;
					final_con = trot_con;
				}
			}
			if (final_con != null) {
				polygon.inps = M.move(final_mv, M.rotate(cossin, polygon.inps));
				polygon.outps = M.move(final_mv, M.rotate(cossin, polygon.outps));
				tmpcon = final_con;
			}	
			
		}
		
		double[] mv = alignPolygon(prop, polygon);
		double[][] new_inpoly = M.move(mv, polygon.inps);
		double[][] new_outpoly = M.move(mv, polygon.outps);
		for (double[] lap : new_inpoly)
			tmpcon.increment_hull(lap);
		System.out.println("\t Convex hull updated");
		polygon.inps = new_inpoly;
		polygon.outps = new_outpoly;
		System.out.println("\t in and out pts updated");
		
		
		return tmpcon;
	}
	
	
	
	private double[] alignPolygon(Property prop, Strip polygon) {
		double[] mv = new double[2]; 
		double[] bb = M.boundBox(polygon.outps);
		if (prop.getEdge() == null)
			return mv;
		
		switch (prop.getEdge()) {
		case "left":
			System.out.println("\t move to left");
			double[] left_mv = new double[]{-bb[0], 0};
			double[][] left_align_poly = M.move(left_mv, polygon.outps);
			System.out.println("\t aligned to left axis");
			if (!feasible(left_align_poly)) {
				System.out.println("\t need to move up or down");
				
				System.out.println("\t Trying to move the polygon up");
				double[][] left_up_align_poly = left_align_poly;
				double[] lubb = M.boundBox(left_up_align_poly);
				while (lubb[2] > 0 && !feasible(left_up_align_poly)) {
					double[] left_up_mv = new double[]{0,-0.1};
					left_up_align_poly = M.move(left_up_mv, left_up_align_poly);
					lubb = M.boundBox(left_up_align_poly);
				}
				if (feasible(left_up_align_poly)) {
					System.out.println("\t Left up postion is possible");
					double[] lbb = M.boundBox(left_align_poly);
					lubb = M.boundBox(left_up_align_poly);
					left_mv[1] = - (lbb[2] - lubb[2]);
					System.out.println("\t Left Up postion is updated");
				} else {
					System.out.println("\t Trying to move the polygon down");
					double[][] left_down_align_poly = left_align_poly;
					double[] ldbb = M.boundBox(left_down_align_poly);
					while (ldbb[3] < HEI && !feasible(left_down_align_poly)) {
						double[] left_down_mv = new double[]{0,0.1};
						left_down_align_poly = M.move(left_down_mv, left_down_align_poly);
						ldbb = M.boundBox(left_down_align_poly);
					}
					if (feasible(left_down_align_poly)) {
						System.out.println("\t Left down postion is possible");
						double[] lbb = M.boundBox(left_align_poly);
						ldbb = M.boundBox(left_down_align_poly);
						left_mv[1] = ldbb[2] - lbb[2];
						System.out.println("\t Left Down postion is updated");
					} else {
						System.out.println("\t Left up/down movement not possible, will not be left align");
						left_mv[0] = 0;
						left_mv[1] = 0;
					}
				}
				
			}
			mv = left_mv;
			break;
		case "right":
			System.out.println("\t move to right");
			double[] right_mv = new double[]{WID-bb[1], 0};
			double[][] right_align_poly = M.move(right_mv, polygon.outps);
			System.out.println("\t aligned to right axis");
			if (!feasible(right_align_poly)) {
				System.out.println("\t need to move up or down");
				
				System.out.println("\t Trying to move the polygon up");
				double[][] right_up_align_poly = right_align_poly;
				double[] rubb = M.boundBox(right_up_align_poly);
				while (rubb[2] > 0 && !feasible(right_up_align_poly)) {
					double[] right_up_mv = new double[]{0,-0.1};
					right_up_align_poly = M.move(right_up_mv, right_up_align_poly);
					rubb = M.boundBox(right_up_align_poly);
				}
				if (feasible(right_up_align_poly)) {
					System.out.println("\t Right up postion is possible");
					double[] rbb = M.boundBox(right_align_poly);
					rubb = M.boundBox(right_up_align_poly);
					right_mv[1] = - (rbb[2] - rubb[2]);
					System.out.println("\t Right Up postion is updated");
				} else {
					System.out.println("\t Trying to move the polygon down");
					double[][] right_down_align_poly = right_align_poly;
					double[] rdbb = M.boundBox(right_down_align_poly);
					while (rdbb[3] < HEI && !feasible(right_down_align_poly)) {
						double[] right_down_mv = new double[]{0,0.1};
						right_down_align_poly = M.move(right_down_mv, right_down_align_poly);
						rdbb = M.boundBox(right_down_align_poly);
					}
					if (feasible(right_down_align_poly)) {
						System.out.println("\t Right down postion is possible");
						double[] rbb = M.boundBox(right_align_poly);
						rdbb = M.boundBox(right_down_align_poly);
						right_mv[1] = rdbb[2] - rbb[2];
						System.out.println("\t Right Down postion is updated");
					}else {
						System.out.println("\t Right up/down movement not possible, will not be right align");
						right_mv[0] = 0;
						right_mv[1] = 1;
					}
				}
			}
			mv = right_mv;
			break;
		case "top":
			System.out.println("\t move to top");
			double[] top_mv = new double[]{0,-bb[2]};
			double[][] top_align_poly = M.move(top_mv, polygon.outps);
			System.out.println("\t aligned to top axis");
			if (!feasible(top_align_poly)) {
				System.out.println("\t need to move left or right");
				
				System.out.println("\t Trying to move the polygon to left");
				double[][] top_left_align_poly = top_align_poly;
				double[] tlbb = M.boundBox(top_left_align_poly);
				
				while (tlbb[0] > 0 && !feasible(top_left_align_poly)) {
					double[] top_left_mv = new double[]{-0.1,0};
					top_left_align_poly = M.move(top_left_mv, top_left_align_poly);
					tlbb = M.boundBox(top_left_align_poly);
				}
				if (feasible(top_left_align_poly)) {
					System.out.println("\t Top left postion is possible");
					double[] tbb = M.boundBox(top_align_poly);
					tlbb = M.boundBox(top_left_align_poly);
					top_mv[0] = - (tbb[0] - tlbb[0]);
					System.out.println("\t Top left postion is updated");
				}else {
					System.out.println("\t Try moving the polygon to right");
					double[][] top_right_align_poly = top_align_poly;
					double[] trbb = M.boundBox(top_right_align_poly);
					
					while (trbb[1] < WID && !feasible(top_right_align_poly)) {
						double[] top_right_mv = new double[]{0.1,0};
						top_right_align_poly = M.move(top_right_mv, top_right_align_poly);
						trbb = M.boundBox(top_right_align_poly);
					}
					if (feasible(top_right_align_poly)) {
						System.out.println("\t Top right postion is possible");
						double[] tbb = M.boundBox(top_align_poly);
						trbb = M.boundBox(top_right_align_poly);
						top_mv[0] = trbb[0] - tbb[0];
						System.out.println("\t Top right postion is updated");
					} else {
						System.out.println("\t Top left/right movement not possible, will not be top align");
						top_mv[0] = 0;
						top_mv[1] = 0;
					}
				}
			}
			mv = top_mv;
			break;
		case "bottom":
			System.out.println("\t move to bottom");
			double[] bottom_mv = new double[]{0, HEI-bb[3]};
			double[][] bottom_align_poly = M.move(bottom_mv, polygon.outps);
			System.out.println("\t aligned to bottom axis");
			if (!feasible(bottom_align_poly)) {
				System.out.println("\t need to move left or right");
				
				System.out.println("\t Trying to move the polygon to left");
				double[][] bottom_left_align_poly = bottom_align_poly;
				double[] blbb = M.boundBox(bottom_left_align_poly);
				while (blbb[0] > 0 && !feasible(bottom_left_align_poly)) {
					double[] bottom_left_mv = new double[]{-0.1,0};
					bottom_left_align_poly = M.move(bottom_left_mv, bottom_left_align_poly);
					blbb = M.boundBox(bottom_left_align_poly);
				}
				if (feasible(bottom_left_align_poly)) {
					System.out.println("\t Bottom left postion is possible");
					double[] bbb = M.boundBox(bottom_align_poly);
					blbb = M.boundBox(bottom_left_align_poly);
					bottom_mv[0] = - (bbb[0] - blbb[0]);
					System.out.println("\t Bottom left postion is updated");
				} else {
					System.out.println("\t Try moving the polygon to right");
					double[][] bottom_right_align_poly = bottom_align_poly;
					double[] brbb = M.boundBox(bottom_right_align_poly);
					while (brbb[1] < WID && !feasible(bottom_right_align_poly)) {
						double[] bottom_right_mv = new double[]{0.1,0};
						bottom_right_align_poly = M.move(bottom_right_mv, bottom_right_align_poly);
						brbb = M.boundBox(bottom_right_align_poly);
					}
					if (feasible(bottom_right_align_poly)) {
						System.out.println("\t Bottom right postion is possible");
						double[] bbb = M.boundBox(bottom_align_poly);
						brbb = M.boundBox(bottom_right_align_poly);
						bottom_mv[0] = brbb[0] - bbb[0];
						System.out.println("\t Bottom right postion is updated");
					} else {
						System.out.println("\t Bottom left/right movement not possible, will not be bottom align");
						bottom_mv[0] = 0;
						bottom_mv[1] = 0;
					}
				}
			}
			mv = bottom_mv;
			break;
			default: 
				System.out.println("No alignment Specified");
		}
		return mv;
		
	}
		

	private boolean placeAnotherStrip_Dalalah(int sid) {
		Strip stp = movs.get(sid);
		double min = 1000000000;
		int min_rotid = -1;
		double[] min_trans = null;
		Convex min_con = null;
		for (int i = 0; i < rotSteps; i++) {// each angle of new strip
			double[][] rotated_outpoly = M.rotate(trigos[i], stp.outps);
			double[][] rotated_inpoly = M.rotate(trigos[i], stp.inps);

			for (double[] p : rotated_outpoly) { // each vertex of new strip
				for (Strip fixed : fixs) {
					for (double[] v : fixed.outps) { // each vertex of each fixed strip
						double[] trans = M.sub(v, p);
						double[][] trans_rot_outpoly = M.move(trans, rotated_outpoly);
						if (feasible(trans_rot_outpoly)) {
							double[][] trans_rot_inpoly = M.move(trans, rotated_inpoly);
							Convex tmpcon = cntConvex.clone();
							for (double[] trp : trans_rot_inpoly)
								tmpcon.increment_hull(trp);

							double conarea = areasc * M.areaAbs(tmpcon.convex);
							double[] center = M.mean(trans_rot_outpoly); // trans_rot_outpoly
							double area = conarea * (preferX * Math.abs(center[0]) + Math.abs(center[1]));
							if (min >= area) {
								min = area;
								min_rotid = i;
								min_trans = trans;
								min_con = tmpcon;
							}
						}
					}
				}
			} // for each vertex of new strip
		} // for each angle of new strip
		if (0 > min_rotid) // no solution, stop
			return false;
		stp.fix_rotate_move(trigos[min_rotid], min_trans);
		// movs.remove(sid);
		movs.set(sid, null);
		fixs.add(stp);
		cntConvex = min_con;
		return true;
	}

	private boolean feasible(double[][] poly) {
		
		for (double[] p : poly) {
			if (p[0] < (0) || p[0] > (WID))
				return false;
			if (p[1] < (0) || p[1] > (HEI))
				return false;
		}

		for (Strip fixed : fixs) {
			if (overlap(poly, fixed.inps))
				return false;
		}
		return true;
	}
	

	public int[] getStripIds() {
		int size = fixs.size();
		int[] ids = new int[size];
		for (int i = 0; i < size; i++)
			ids[i] = fixs.get(i).id;
		return ids;
	}

	public double[][] getStripRotations() {
		int size = fixs.size();
		double[][] rots = new double[size][];
		for (int i = 0; i < size; i++)
			rots[i] = fixs.get(i).trigo;
		return rots;
	}

	public double[][] getStripPositions() {
		int size = fixs.size();
		double[][] ps = new double[size][];
		for (int i = 0; i < size; i++)
			ps[i] = fixs.get(i).position;
		return ps;
	}

	private static boolean overlap(double[][] poly1, double[][] poly2) {
		if (!M.intersect_boundBox(poly1, poly2))
			return false;
		double[][] sml, big;
		if (M.areaAbs(poly1) < M.areaAbs(poly2)) {
			sml = poly1;
			big = poly2;
		} else {
			sml = poly2;
			big = poly1;
		}
		for (double[] p : sml) {
			if (M.inside(p, big))
				return true;
		}
		for (int i = 0; i < sml.length; i++) {
			for (int j = 0; j < big.length; j++) {
				if (M.intersection(sml[i], sml[(i + 1) % sml.length], big[j], big[(j + 1) % big.length]))
					return true;
			}
		}
		return false;
	}

}
