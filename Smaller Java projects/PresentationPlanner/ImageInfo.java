// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3, Assignment 1
 * Name:    Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

/** ImageInfo represents the information of an Image.
 */

public class ImageInfo{

    public static final double HEIGHT_LARGE = 650;  // size of images for the large display
    public static final double WIDTH_LARGE = 120; 
    public static final double TOP_LARGE = 5; 
    public static final double LEFT_LARGE = 5;
    public static final double TOP_TEXT = 50; 
    public static final double LEFT_TEXT = WIDTH_LARGE+70; 

    // Fields to store
    //   the name of the image
    //   the description of the image
    private String name;          // name of the image
    private String description;    // description of the image

    // Constructor
    /** Construct a new ImageInfo object.
     *  Parameters are the name of the image and its description
     *  Stores the parameters into fields 
     */
    public ImageInfo(String n, String desc){
        this.name = n;
        this.description = desc;
    }

    /**
     * Display the image (no text) at the specified coordinates
     */
    public void displaySmall(double left, double top, double width, double height){
        UI.drawImage(this.name+".jpg", left, top, width, height);
    }

    /**
     * Display the image and the text, large size.
     * Position given by the constants defined above
     */
    public void displayLarge(double size){
        UI.drawImage(this.name+".jpg", LEFT_LARGE, TOP_LARGE,  WIDTH_LARGE*size, HEIGHT_LARGE*size);
        // Draw the text description to the right of the image
        UI.drawString(name,LEFT_TEXT+2,TOP_TEXT);
        UI.drawString(description,LEFT_TEXT,TOP_TEXT+20);
    }
}
