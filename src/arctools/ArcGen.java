package arctools;

import java.io.IOException;

public class ArcGen extends ArcGenSuper{
    
    double tempo; // Tempo (BPM) of the chart
    int start, end; // Start and end timings of the visual effect
    final double Y_TO_X = 0.47; // Convert Y coordinates to X coordinates for better visuals :)
    final double PI = 3.14159265; // pi
    
    public ArcGen() throws IOException {
        super(); // Create all the filewriters and stuff.
        tempo = Math.round(super.doublePrompt("BPM")*100)/100; // Prompts the user to input the tempo of the song, prompt function in super class.
        start = super.intPrompt("Start Point"); // Input the start point of the effect.
        end = super.intPrompt("End Point"); // Input the end point of the effect.
        
        menu(); // opens up the menu
        pw.close();
    }
    
    /*
     * menu
     */
    private void menu() {
        switch(intPrompt("a number (0 for help)")) {
            case 1: 
                createLightspeedVoidArcs(); // Lightspeed styled void arc generator
                break;
            case 2: 
                createPolygonResizeAnim(false); // Creates animated polygon
                break;
            case 3: 
                createPolygonResizeAnim(true); // Creates animated polygon
                break;
            case 4: break;
            case 5: break;
            case 6: break;
            case 7: break;
            case 8: break;
            case 9: break;
            case 0: 
                System.out.println("Help menu: \n"
                        + "<1> Create lightspeed void arcs \n"
                        + "<2> Create animated/static polygon (No TimingGroup frames) \n"
                        + "<3> Create animated/static polygon (TimingGroup frames) \n"
                        + "<4> Create 'Moving line' animation (No TimingGroup frames) \n"
                        + "<5> Create 'Moving line' animation (TimingGroup frames) \n");
                menu();
                break;
        }
    }
    
    /*
     * Functional methods to generate AFF file
     */
    
    /* Creates that star wars highspeed arc effect thingie idk what it's called shut up */
    public void createLightspeedVoidArcs() {
        double timingDensity = doublePrompt("Timing Density (0 - 1)"); // How frequent the lines will appear in the timing axis
        int maxLinePerTiming = intPrompt("Max line per timing"); // max number of lines per timing
        double measure = doublePrompt("Measure no."); // FUCK I FORGOT WHAT THIS IS FOR
        
        pw.println("timinggroup(){");
        pw.println("timing(0,"+tempo+",4.00);");
        
        while(start < end) { // Prints the lines
            if(rng.nextDouble() < timingDensity) { // Using RNG to determine whether the line should appear at a certain point
                int numLines = rng.nextInt(maxLinePerTiming); // Using RNG to generate number of line in that timing
                if(numLines == 0) numLines = 1; // If RNG generated a 0, change that to 1
                for(int j = 0; j < numLines; j++) {
                    double x = twoDP(rng.nextDouble()); // Generates X position between 0 and 1
                    double y = twoDP(rng.nextDouble()); // Generates Y position between 0 and 1
                    pw.println("arc("+start+","+(int)(start + tempo/(2*measure))+","+x+","+x+",s,"+y+","+y+",0,none,true);"); // Creates void arcs
                }
            }
            start = start + (int)(tempo/measure); // Increment time to next position
        }
        pw.println("};");
    }
    
    /* Creates polygon with resize and/or rotation animation. */
    public void createPolygonResizeAnim(boolean timingGroupFrames) { 
        double x = doublePrompt("Input x value"); // X coordinate
        double y = doublePrompt("Input y value"); // Y coordinate
        double ySizeInit = doublePrompt("Input initial size of the polygon"); // initial size of polygon.
        int sides = intPrompt("Input number of sides"); // Number of polygon side (eg. 3 = equi-triangle, 4 = square, etc.)
        int frameCount = intPrompt("Input number of frames"); // Number of frames, higher number recommended for more timing.
        double numRotation = doublePrompt("Input number of rotation/s");
        double ySizeFinal = doublePrompt("Input Final Size"); // final size of polygon
        
        int timingInterval = (end - start) / frameCount;
        double angleInterval = numRotation * 2 * PI / frameCount;
        double resizeInterval = (ySizeFinal - ySizeInit) / frameCount;
        
        double points[][] = new double [sides][2]; // List of points of the polygon
        
        if(!timingGroupFrames) {
            pw.println("timinggroup(){");
            pw.println("timing(0,"+tempo+",4.00);"); // Initial timing (Timing based)
            pw.println("timing(1,6969420,4.00);");
        } 
        
        for(int j = 1; j <= frameCount; j++) {
            
            if(timingGroupFrames) {
                pw.println("timinggroup(){");
                pw.println("timing(0,"+tempo+",4.00);"); // Initial timing (Timing group frame based)
                pw.println("timing(1,6969420,4.00);");
            } 
            
            for(int i = 0; i < sides; i++) {
                points[i][0] = - (ySizeInit + resizeInterval * j) * Math.sin(2 * i * PI / sides + angleInterval * j); // X axis
                points[i][1] = (ySizeInit + resizeInterval * j) * Math.cos(2 * i * PI / sides + angleInterval * j); // Y axis
            }
            
            /*
             * Minimum value for start is 2, 0 is reserved for bpm assignment and 1 is reserved for 6969420 speed, goddammit schwarzer
             * whose idea is it to use 0 timing as the song's initial tempo?
             */
            if(start > 1) {
                pw.println("timing("+start+",0,4.00);"); // 0 timing on start if current frame timing is more than 1
            }
            else {
                pw.println("timing(2,0,4.00);"); // 0 timing on timing 2 if timing is at most 1
            }
            
            for(int k = 0; k < sides-1; k++) { // Print out one frame
                pw.println("arc("+start+","+(start + timingInterval - 1)+","+twoDP(points[k][0] * Y_TO_X + x)+","+twoDP(points[k+1][0] * Y_TO_X + x)+",s,"+twoDP(points[k][1] + y)+","+twoDP(points[k+1][1] + y)+",0,none,true);");
            }
            pw.println("arc("+start+","+(start + timingInterval - 1)+","+twoDP(points[0][0] * Y_TO_X + x)+","+twoDP(points[sides-1][0] * Y_TO_X + x)+",s,"+twoDP(points[0][1] + y)+","+twoDP(points[sides-1][1] + y)+",0,none,true);");
                       
            start = start + timingInterval; // Change start point
            
            if(timingGroupFrames) {
                pw.println("timing("+(int)(start + 1)+",6969420,4.00);");
                pw.println("};");
            } 
            else {
                pw.println("timing("+(int)(start - 1)+",6969420,4.00);");
            }
            
        }
        if(!timingGroupFrames) pw.println("};"); 
    }
}
