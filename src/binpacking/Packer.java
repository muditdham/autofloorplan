package binpacking;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import pack.Convex;
import pack.FixPolygon;
import pack.M;
import pack.Pack;
import pack.Property;
import pack.Strip;
import pack.Tuple;
import processing.core.PApplet;


public class Packer extends PApplet {


	// Set Properties related to the working environment like heights, widths, polygon types, their counts and their scaling factors 
	void setEnvironmentProperties() {
		
		setBase_WIDTH(400); 					// Define Width of the first rectangular region 					
		setBase_HEIGHT(200);					// Define Height of the first rectangular region
		
		setSubsequent_PACK_WIDTHs(new double [] {300, 200, 200, 150, 250}); 	// Define Widths of other rectangular regions
		setSubsequent_PACK_HEIGHTs(new double [] {250, 200, 200, 250, 250});	// Define Heights of other rectangular regions
		
		setMargin(0.0); // Change if boundary required around every component (polygon)
		
		
		// Define coordinates for different types of components (polygons) - proceed either clockwise or anti clockwise
		ArrayList<double[][]> prototypes = new ArrayList<>();
		
		
		// Polygon Type 1
		prototypes.add(new double [][] {{3.5,0.5}, {4.0,0.5}, {4,1}, {9,1}, {9,0}, {10,0}, {10,5}, {9,5}, {9,4}, {4,4}, {4,4.5}, {3.5,4.5}});
		colorPolygonType.add(new double[] {0,0,255}); //blue
		
		// Polygon Type 2
		prototypes.add(new double [][] {{4,0}, {9,0}, {9,10}, {8,10}, {8,14.5}, {5,14.5}, {5,10}, {4,10}, {4,5}, {5,5}, {5,1}, {4,1}});
		colorPolygonType.add(new double[] {255,0,0}); //red
		
		// Polygon Type 3
		prototypes.add(new double [][] {{10,0}, {10,1}, {9,1}, {9,17}, {8.5,17}, {8.5,19.5}, {5,19.5}, {5,17}, {4.5,17}, {4.5,1}, {3.5,1}, {3.5,0}});		
		colorPolygonType.add(new double[] {0,255,0}); //green
		
		// Polygon Type 4
		prototypes.add(new double [][] {{10,0}, {10,1}, {9,1}, {9,3}, {10,3}, {10,7.5}, {9,7.5}, {9,13}, {6.5,13}, {6.5,9}, {5.5,9}, {5.5,7}, {4,7}, {4,2.5}, {5,2.5}, {5,1}, {4,1}, {4,0}});
		colorPolygonType.add(new double[] {255,255,51}); //yellow 
		
		// Polygon Type 5
		prototypes.add(new double [][] {{10,0}, {10,0.5}, {12,0.5}, {12,4.5}, {5,4.5}, {5,3}, {1,3}, {1,2}, {5,2}, {5,0}});
		colorPolygonType.add(new double[] {160,160,160}); //grey
		
		
		// Similarly you can add more polygons below using the following syntax / format
		// prototypes.add(new double [][] { {x1,y1}, {x2, y2}, {x3,y3} ....... {x_n,y_n} });
		
		
		// Define number of different types of polygons
		// {Count for Polygon of Type 1, followed by count for Polygon of Type 2, followed by Count for Polygon of Type 3, .... and so on } 
		
		int[] prototype_counts = {1, 1, 2, 2, 1};
		
		// Define Scaling factor for different types of polygons
		// {Scaling factor for Polygon of Type 1, followed by scaling factor for Polygon of Type 2, followed by scaling factor for Polygon of Type 3, .... and so on }
		
		double[] prototype_scales = {10.0, 10.0, 10.0, 10.0, 10.0};
		
		
		
		
		
		// No Change required - this is command used to set the above defined properties
		setPolygons(prototypes, prototype_counts, prototype_scales);
			
	}
	
	// Both POLYGON_ID AND SHEET_ID numbering starts from 0 
	// SHEET_ID = 0 represents the first rectangular sheet
	// SHEET_ID = 1 represents the second rectangular sheet
	// SHEET_ID = 2 represents the third rectangular sheet
	
	
	// Set Properties related to the specific polygons / components
	void setPolygonProperties() { 
		
		// fixPolygon.put(POLYGON_ID, new FixPolygon(POLYGON_ID, SHEET_ID, ROTATION, X_COORDINATE, Y_COORDINATE));
		
		// fixPolygon.put(0, new FixPolygon(0, 0, -1, 300, 250)); // Fixes polygon 3 to first sheet at coordinate (300, 250) by trying all possible rotations
		// fixPolygon.put(4, new FixPolygon(4, 1, -1, 100, 100)); // Fixes polygon 4 to second sheet at coordinate (100,100) by trying all possible rotations
		 
		
		
		// ##############################################################################################################################
		// ##############################################################################################################################
		// ####################################                      FIX POLYGON                     ####################################
		// ####################################                 SAMPLES FOR REFERENCE                ####################################  
		// ##############################################################################################################################
		// ##############################################################################################################################
		//
		// fixPolygon is used to fix the location of a polygon permanently
		// this can also be used to move polygons across different sheets
		// also make sure the both the POLYGON_IDs are defined to be same
		//
		//
		// fixPolygon.put(POLYGON_ID, new FixPolygon(POLYGON_ID, SHEET_ID, ROTATION, X_COORDINATE, Y_COORDINATE)); // Format / Syntax
		//
		//
		// // Fixes polygon 4 to second sheet (SHEET_ID = 1) at coordinate (50,100) 
		//
		// fixPolygon.put(4, new FixPolygon(4, 1, -1, 50, 100));  	// tries all possible rotations
		// fixPolygon.put(4, new FixPolygon(4, 1, 1, 50, 100));  	// with a 0 degrees clockwise rotation
		// fixPolygon.put(4, new FixPolygon(4, 1, 1, 50, 100)); 	// with a 90 degrees clockwise rotation
		// fixPolygon.put(4, new FixPolygon(4, 1, 2, 50, 100)); 	// with a 180 degrees clockwise rotation
		// fixPolygon.put(4, new FixPolygon(4, 1, 3, 50, 100)); 	// with a 270 degrees clockwise rotation
		// fixPolygon.put(4, new FixPolygon(4, 1, 4, 50, 100)); 	// with a 360 degrees clockwise rotation
		
		
		
		// ##############################################################################################################################
		// ##############################################################################################################################
		// ##############################################################################################################################

		
		
		
		// polygonProperty.put(1, new Property(1, 1)); 			// 90 degrees rotation 
		// polygonProperty.put(1, new Property(1, 2)); 			// 180 degrees rotation 
		// polygonProperty.put(1, new Property(1, 3)); 			// 270 degrees rotation 
		
		// polygonProperty.put(1, new Property(1, "right")); 	// RIGHT EDGE alignment
		// polygonProperty.put(1, new Property(1, "left")); 	// LEFT EDGE alignment
		// polygonProperty.put(1, new Property(1, "top")); 		// TOP EDGE alignment
		// polygonProperty.put(1, new Property(1, "bottom")); 	// BOTTOM EDGE alignment
		
	
		// polygonProperty.put(1, new Property(1, 1, "right")); // rotate 90 degrees and align to RIGHT edge
		// polygonProperty.put(1, new Property(1, 2, "bottom")); 	// rotate 180 degrees and align to BOTTOM edge
		
		
	
	
		// ##############################################################################################################################
		// ##############################################################################################################################
		// ####################################                    POLYGON PROPERTY                  ####################################
		// ####################################                 SAMPLES FOR REFERENCE                ####################################  
		// ##############################################################################################################################
		// ##############################################################################################################################
		//
		// polygonProperty tries to stick to user defined properties while packing the polygon / component
		
		// Possible combinations of setting properties
		
		// Makes sure the polygon is oriented to a specific angle while its being placed 
		// polygonProperty.put(POLYGON_ID, new Property(POLYGON_ID, ROATION)); 
		// rotation can be 1, 2, or 3
		
		// Makes sure the polygon is aligns to a specific edge of the rectangular sheet while its being placed
		// polygonProperty.put(POLYGON_ID, new Property(POLYGON_ID, EDGE));
		// edge can be left, right, top or bottom 
		
		// Makes sure the polygon is moved to a specific location (x,y) coordinate while its being placed
		// polygonProperty.put(POLYGON_ID, new Property(POLYGON_ID, X_COORDINATE, Y_COORDINATE));
		
		// Makes sure the polygon is rotated to a specific angle while being aligned to a specific edge when its being placed
		// polygonProperty.put(POLYGON_ID, new Property(POLYGON_ID, ROTATION, EDGE));
		//
		// polygonProperty.put(1, new Property(1, 1, "right")); // rotate 90 degrees and align to RIGHT edge
		// polygonProperty.put(1, new Property(1, 2, "right")); // rotate degrees and align to RIGHT edge
		// polygonProperty.put(1, new Property(1, 3, "right")); // rotate 270 degrees and align to RIGHT edge
		// polygonProperty.put(1, new Property(1, 4, "right")); // rotate 360 degrees and align to RIGHT edge
		
		// Makes sure the polygon is rotated to a specific angle while being moved to a specific location (x,y) coordinate when its being place
		// polygonProperty.put(POLYGON_ID, new Property(POLYGON_ID, ROTATION, X_COORDINATE, Y_COORDINATE));
		
		
		// ##############################################################################################################################
		// ##############################################################################################################################
		// ##############################################################################################################################
		
		// Change display names of specific polygons
		
		// namePolygon.put(1, "Component 1");
		// namePolygon.put(2, "Component 2");
		// namePolygon.put(3, "C3");
		// namePolygon.put(4, "C4");
		
		// Change colors of specific polygons
		
		// colorPolygon.put(0, new double[] {0,204,204});  // teal
		// colorPolygon.put(1, new double[] {255,153,204}); // pink
		// colorPolygon.put(3, new double[] {204,229,255}); // sky blue


	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// ##############################################################################################################################
	// ##############################################################################################################################
	// ##############################################################################################################################
	// ####################################                                                      ####################################
	// ####################################          DO NOT CHANGE ANYTHING BELOW THIS           ####################################
	// ####################################                                                      ####################################
	// ##############################################################################################################################
	// ##############################################################################################################################
	// ##############################################################################################################################
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int base_WIDTH = 1800; 								// Width of the base Rectangular Sheet 
	private int base_HEIGHT = 1500; 							// Height of the base Rectangular Sheet
	private double subsequent_PACK_WIDTHs[] = {base_WIDTH}; 	// Subsequent Widths 
	private double subsequent_PACK_HEIGHTs[] = {base_HEIGHT}; 	// Subsequent Heights
	private double margin = 2.50; 								// Margin between polygons
	
	
	public int getBase_WIDTH() {
		return base_WIDTH;
	}

	public void setBase_WIDTH(int base_WIDTH) {
		this.base_WIDTH = base_WIDTH;
	}

	public int getBase_HEIGHT() {
		return base_HEIGHT;
	}

	public void setBase_HEIGHT(int base_HEIGHT) {
		this.base_HEIGHT = base_HEIGHT;
	}

	public double getMargin() {
		return margin;
	}

	public void setMargin(double margin) {
		this.margin = 0.00001 + margin;
	}

	public double[] getSubsequent_PACK_WIDTHs() {
		return subsequent_PACK_WIDTHs;
	}

	public void setSubsequent_PACK_WIDTHs(double[] subsequent_PACK_WIDTHs) {
		this.subsequent_PACK_WIDTHs = subsequent_PACK_WIDTHs;
	}

	public double[] getSubsequent_PACK_HEIGHTs() {
		return subsequent_PACK_HEIGHTs;
	}

	public void setSubsequent_PACK_HEIGHTs(double[] subsequent_PACK_HEIGHTs) {
		this.subsequent_PACK_HEIGHTs = subsequent_PACK_HEIGHTs;
	}
	
	
	/* Parameters used by algorithm internally */ 
	private ArrayList<Pack> packs = new ArrayList<Pack>(); 	// Container for different Rectangular Sheets
	private ArrayList<double[][]> inppolys = new ArrayList<>();   						// Container for input polygons
	private HashMap<Integer, Property> polygonProperty = new HashMap<Integer, Property>();		// Container for properties	
	private HashMap<Integer, FixPolygon> fixPolygon = new HashMap<Integer, FixPolygon>();		// Container for fixing polygons
	private HashMap<Integer, Strip> transferPolygon = new HashMap<Integer, Strip>();				// Container for transferring polygons
	private HashMap<Integer, String> namePolygon = new HashMap<Integer, String>();				// Container for naming polygons
	private HashMap<Integer, double[]> colorPolygon = new HashMap<Integer, double[]>();				// Container for coloring polygons
	private ArrayList<double[]> colorPolygonType = new ArrayList<>(); 							// Container for coloring polygon types
	private ArrayList<ArrayList<Integer>> polycolors = new ArrayList<>(); //Container for Polygon Colors
	private final double preferX = 0.499; // 0.501 or 1
	private final double segment_max_length = 250.0; //250,400,800, use to break long edges if necessary, relative to the scale of the polygons
	private final int rotSteps = 4 ; 	// Determine the possible rotations of all the polygons
	private int[] result_pack_id;  		// result_pack_id[9]=2 means the 9th polygon is on the 2nd sheet.
	private double[][] result_cos_sin; 	// result_cos_sin[9]={0.5, 0.866} means the 9th polygon is rotated 60 degree w.r.t its reference point
	private double[][] result_position; // result_cos_sin[9] denotes the x,y-coordinate of the 9th polygon w.r.t its reference point	
	private boolean useAbey = true; 	
										
	
	private void report() {
		result_pack_id = new int[inppolys.size()];
		result_cos_sin = new double[inppolys.size()][];
		result_position = new double[inppolys.size()][];
		for (int i = 0; i < packs.size(); i++) {
			Pack pack = packs.get(i);
			for (Strip strip : pack.fixs) {
				result_pack_id[strip.id] = i;
				result_cos_sin[strip.id] = strip.trigo;
				result_position[strip.id] = strip.position;
			}
		}
	}
	
	
	public int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}

	void setPolygons(ArrayList<double[][]>prototypes, int[] prototype_counts, double[] prototype_scales) {

		for (int i = 0; i < prototypes.size(); i++) {
			int j = 0;
			
			ArrayList<Integer> c = new ArrayList<Integer>();
			
			if (i >= colorPolygonType.size()) {
				c.add(getRandomNumber(0,255));
				c.add(getRandomNumber(0,255));
				c.add(getRandomNumber(0,255));
			} else {
				double[] pct = colorPolygonType.get(i);
				c.add((int) pct[0]);
				c.add((int) pct[1]);
				c.add((int) pct[2]);
			}
			
			while (j<prototype_counts[i]) {
				double[][] e = M.scale(prototypes.get(i), prototype_scales[i]);
				inppolys.add(e);
				polycolors.add(c);
				j+=1;
			}
		}
	}
	
	public void fixPolygons(int sheet_id, Pack pack) {
		boolean first_polygon = true;
		Convex currCon = null;
		
		for (Map.Entry<Integer,FixPolygon> mapElement : fixPolygon.entrySet()) { 
			
            int polygon_id = mapElement.getKey();  
            FixPolygon value = mapElement.getValue();
            Strip polygon = null;
            int movs_idx = -99;
            if (transferPolygon.containsKey(polygon_id)){
				polygon = transferPolygon.get(polygon_id);
			} else if (!pack.movs.isEmpty()) {
				for (int i = 0; i < pack.movs.size(); i++) {
					if (pack.movs.get(i) != null) {
						if (pack.movs.get(i).id == polygon_id) {
							polygon = pack.movs.get(i);
							movs_idx = i;
							break;
						}
					}
				}
			} 
            
			if (polygon != null) {
				if (value.getSheet_id() == sheet_id) {
					
					Property prop = new Property(polygon.id, value.getRotation(), value.getX_location(), value.getY_location());
					Tuple currCon_tuple = pack.translatePolygon(prop, polygon, first_polygon, true);
					 
					if (currCon_tuple.isStatus()) {
						currCon = currCon_tuple.getConvex();
						
						if (transferPolygon.containsKey(polygon.id)){
							transferPolygon.remove(polygon.id);
						} else {
							pack.movs.set(movs_idx, null);
						}
						pack.fixs.add(polygon);
						first_polygon = false;
						pack.cntConvex = currCon;
					} else {
						System.out.println("Was not placed and will be treated normally");
						pack.movs.add(polygon);
						if (transferPolygon.containsKey(polygon.id)){
							transferPolygon.remove(polygon.id);
							
						}
					}
					 
				} else {
					if (movs_idx >= 0) {
						pack.movs.set(movs_idx, null);
					}
					else {
						System.out.println("Skipping Sheet");
					
					}
					transferPolygon.put(polygon.id, polygon);
				}
				
			} else {
				System.out.printf("polygon %d not found when on sheet %d \n", polygon_id, sheet_id);
			}
		}
	}
	
	public void setup() {
		setEnvironmentProperties();
		setPolygonProperties();
		

		size(1300, 800);
		
		/* Setting random colors to the polygons */ 
		// int seed = 4346;
//		Random cran = new Random(seed);
		
//		for(int i=0; i<inppolys.size(); i++) {
//			ArrayList<Integer> c = new ArrayList<Integer>(); 
//			c.add(cran.nextInt(225));
//			c.add(cran.nextInt(225));
//			c.add(cran.nextInt(225));
//			polycolors.add(c);
//		}
		
		Double segment_len = useAbey ? null : segment_max_length;
		Pack pack = new Pack(inppolys, margin, segment_len, rotSteps, base_WIDTH, base_HEIGHT, preferX);
		fixPolygons(0, pack);
		pack.packOneSheet(useAbey, polygonProperty);
		packs.add(pack);

		for (int i = 0; i < 100; i++) { // packing one sheet after another, 100 is estimated
			int size = packs.size();
			if (packs.get(size - 1).isEmpty() & transferPolygon.isEmpty()) {
				println(size + " sheets");
				break;
			}
			
			if (i>=subsequent_PACK_WIDTHs.length)
				pack = packs.get(size - 1).clone(subsequent_PACK_WIDTHs[subsequent_PACK_WIDTHs.length - 1], subsequent_PACK_HEIGHTs[subsequent_PACK_WIDTHs.length-1]);
			else
				pack = packs.get(size - 1).clone(subsequent_PACK_WIDTHs[i], subsequent_PACK_HEIGHTs[i]);
			
			fixPolygons(i+1, pack);
			pack.packOneSheet(useAbey, polygonProperty);

			packs.add(pack);
		}
		report();
	}
	
	public void draw() {
		background(255);
		smooth();
		translate(40, 20);

		float sc = 1.0f;
		double x_gap = 30;
		double y_gap = 30;

		
		double max_width = base_WIDTH;
		double max_height = base_HEIGHT;
		
		for(int i=0; i<subsequent_PACK_WIDTHs.length;i++)
			if (subsequent_PACK_WIDTHs[i] > max_width)
				max_width = subsequent_PACK_WIDTHs[i];
		
		for(int i=0; i<subsequent_PACK_HEIGHTs.length;i++) 
			if (subsequent_PACK_HEIGHTs[i] > max_height)
				max_height = subsequent_PACK_HEIGHTs[i];
			
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4; j++) {
				int id = i * 4 + j;
				
				if (id >= packs.size())
					return;

				Pack pack = packs.get(id);
				pushMatrix();
				translate((float) (j*(max_width+x_gap)*sc), (float) (i*(max_height+y_gap)*sc));
				noFill();
				rect(0, 0, sc * (float) pack.WID, sc * (float) pack.HEI);
				textSize(10);
				text("0", 0, -5);
				text("0", -10, 10);
				text("TOP", sc * (float) pack.WID/2-5, -5);
				text("BOTTOM", sc * (float) pack.WID/2-15, sc * (float) pack.HEI + 15);
				text(Integer.toString((int) (pack.WID)),  sc * (float) pack.WID - 25 , sc * (float) pack.HEI + 15);
				rotate(PI/2*3);
				text(Integer.toString((int) (pack.HEI)),  -(sc * (float) pack.HEI), sc * (float) pack.WID + 14);
				text("LEFT",  -(sc * (float) pack.HEI/2) , -5);
				text("RIGHT",  -(sc * (float) pack.HEI/2)-5 , sc * (float) pack.WID + 14);
				rotate(-PI/2*3);
	
				for (Strip strip : pack.fixs) {
					
					fill(polycolors.get(strip.id).get(0), 
							polycolors.get(strip.id).get(1), 
							polycolors.get(strip.id).get(2));
					
					if (colorPolygon.containsKey(strip.id)) 
						if (colorPolygon.get(strip.id).length >=3)
							fill((float) colorPolygon.get(strip.id)[0], 
									(float) colorPolygon.get(strip.id)[1], 
									(float) colorPolygon.get(strip.id)[2]); 
					
					
					
					float[] center = draw(strip.inps, sc);
					fill(50);
					
					if (namePolygon.containsKey(strip.id)) {
						double[] bb = M.boundBox(strip.inps);
						text(namePolygon.get(strip.id), (float) (bb[0]+10), (float) (bb[2] +(bb[3]*sc - bb[2]*sc)/2));
					} else {
						text(Integer.toString(strip.id), (float) (center[0]*sc), (float) (center[1]*sc));
					}
				}

				popMatrix();
			}
		}
	}

	private float[] draw(double[][] ps, float sc) {
		float xmin = (float) ps[0][0];
		float xmax = (float) ps[0][0];
		float ymin = (float) ps[0][1];
		float ymax = (float) ps[0][1];

		beginShape();
		for (double[] p : ps) {
			xmin = min(xmin, (float) p[0]);
			xmax = max(xmax, (float) p[0]);
			ymin = min(ymin, (float) p[1]);
			ymax = max(ymax, (float) p[1]);
			vertex((float) p[0] * sc, (float) p[1] * sc);
		}
		endShape(CLOSE);
		
		float[] coordinates = new float[2];
		coordinates[0] = xmin + (xmax-xmin)/2;
		coordinates[1] = ymin + (ymax-ymin)/2;
		return coordinates;
	}
	
	
}
