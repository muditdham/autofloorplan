# AutoFloorPlan
Implmentation of a packing algorithm which is space efficient while placing various circuit components together inaccordance to certain user defined constraints.

# Features
- Define multiple circuits with simple polygon definitions
- Scale and create different polygon types
- Align polygons to different edges – top, left, right, bottom
- Define specific location for individual polygons
- Define various orientations for individual polygons - 90,180,270 degrees
- Define multiple rectangular sheets for varying sizes
- Move polygons across different sheets
- Define margins across different polygons
- Change color and display names for individual polygons 
- Pack large number of polygons quickly and efficiently


# Running Application
1. Open the Eclipse IDE and select eclipse workspace directory as the one created in Step 4 of "Environment Setup and Installation" process
2. Import project by following these Steps
    > File -> Import.. -> General -> Projects from Folder or Archive
    > Select the AutoFloorPlan code directory and Click Open
3. Navigate to Packer.java file at AutoFloorPlan > src > binpacking > Packer.java and follow these steps
    > Right Click Packer.java file > Build Path > Configure Build Path > Libraries > Add External Jars > "AutoFloorPlan > binpacking > core.jar"
    > **core.jar is located inside the src folder of the code repository.**

4. Make sure before running that **Java compilation is set to 1.8**
    > Right click the project AutoFloorPlan and select properties. Select Java Compiler on the left and set the compliance execution evironment to Java 8 (1.8) from the drop down. 
    Uncheck "Use compliance from execution environment" if the drop down is grayed out.
    Click Apply and Close
5. Additionally, Make sure **Java JDK 8 is checked/selected in the Installed JREs**
    > Window -> Preferences -> Java -> From Drop Down select "Installed JREs"
    > Click Add -> Standard VM -> Select Directory as "C:/Program Files/Java/ JDK 8 Folder"
    > Click Finish and Apply.

6. Run using the run button 
 
# Environment Setup and Installation 
1. Download and install JAVA JDK 8 for the appropriate platform using the following link  
    > https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
2. Download and install Eclipse IDE for the appropraite platform using the following link   
    > https://www.eclipse.org/downloads/
3. Download and install GIT Desktop for the appropraite platform using the following link  
    > https://desktop.github.com
4. Create a folder / directory on your which will server both as your eclipse workspace and local repository for your AutoFloorPlan code.
5. Open Git Desktop and Clone the AutoFloorPlan repository. 

 


