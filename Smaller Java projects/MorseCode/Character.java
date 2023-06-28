// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3, Assignment 4
 * Name:    Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

/**
 * A Binary Tree is a data structure in which each node has at most two
 * children.
 * Each node has a field to hold the text representing the character, if known,
 * or null otherwise.
 * The combination of dashes and dots leading to this character represents its
 * morse code.
 * The two possible nodes are dash and dot.
 */

public class Character{

    private String text;
    private Character dash;
    private Character dot;

    /**
     * Construct a new node with some text.
     */
    public Character(String txt){
        if (txt != null)
            text = txt.toUpperCase();
        else
            txt = null;
    }

    /**
     * Construct a new node with text and two children
     */
    public Character(String txt, Character dashChild, Character dotChild){
        text = txt;
        dash = dashChild;
        dot = dotChild;
    }

    /** Getters */
    public String getText(){return text;}

    public Character getDash(){return dash;}

    public Character getDot(){return dot;}

    /** Setters */
    public void setText(String t ){text=t;}

    public void setDash(Character dashChild){
        if (dashChild==null){
            throw new RuntimeException("Not allowed to have one null child");
        }
        dash = dashChild;
    }

    public void setDot(Character dotChild){
        if (dotChild==null){
            throw new RuntimeException("Not allowed to have one null child");
        }
        dot = dotChild;
    }

    public static final int WIDTH = 18;
    public static final int HEIGHT = 18;

    /**
     * Draw the node (as a box with the text) on the graphics pane
     * (x,y) is the center of the box
     * The box should be WIDTH wide, and HEIGHT high.
     */
    public void draw(double x, double y){
        double left = x-WIDTH/2;
        String toDraw;
        if (text == null) toDraw = "";
        else              toDraw = text;

        UI.eraseRect(left, y-HEIGHT/2, WIDTH, HEIGHT);  // to clear anything behind it
        UI.drawRect(left, y-HEIGHT/2, WIDTH, HEIGHT);
        UI.drawString(toDraw, left+4, y+6);             // assume height of characters = 12
    }

}
