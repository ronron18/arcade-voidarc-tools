package arctools;

/*
 * This is just a code which functions as a helper to the generator, idk why i did this in the first place anyways lmaooo.
 * tbh this may be useful because i can just rip the code off this file for other projects i guess.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ArcGenSuper {
    
    public static Scanner in = new Scanner(System.in); // Creates a scanner for input (in)
    public static Random rng = new Random(); // Creates a random number generator (rng)
    public static File file; // Creates a variable to store the file (file)
    public static FileWriter fw; // A filewriter to write the aff file (fw)
    public static PrintWriter pw; // A printwriter to write to aff file (pw)
    
    public ArcGenSuper() throws IOException { // Initializes file writing stuffs
        file = new File("Output_AFF.txt"); 
        fw = new FileWriter(file);
        pw = new PrintWriter(fw);
    }
    
    /*
     * Input prompt methods.
     */
    public int intPrompt(String prompt) { // create int prompts
        System.out.println("Enter " + prompt + ": ");
        int ret = in.nextInt();
        in.nextLine();
        return ret;
    }
    
    public double doublePrompt(String prompt) { // create double prompts
        System.out.println("Enter " + prompt + ": ");
        double ret = in.nextDouble();
        in.nextLine();
        return ret;
    }
    
    /*
     * Useful methods >w<
     */
    public double twoDP(double x) { // Round off a double to two decimal points
        return Math.round(x * 100.00)/100.00;
    }
}
