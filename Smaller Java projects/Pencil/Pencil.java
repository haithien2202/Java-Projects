// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3, Assignment 1
 * Name: Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

import ecs100.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
    /** Pencil */
    public class Pencil{
    private double lastX;
    private double lastY;
    DrawLine currentLine;
    Double lineSize = 3.0; 
    Color defaultColor = Color.black;
    public Stack<DrawLine> undoList = new Stack<>();
    public Stack<DrawLine> redoList = new Stack<>();
    JButton button;
    /**
       * Setup the GUI
         */
        public void setupGUI(){
            UI.setMouseMotionListener(this::doMouse);
            UI.addButton("Undo", this::undo);
            UI.addButton("Redo", this::redo);
            button = UI.addButton("Change Color", this::colorPicker);
        setColor(Color.black);
        UI.addSlider("Size", 1, 15, 3, this::slide);
        UI.addButton("Quit", UI::quit);
        UI.setLineWidth(3);
        UI.setDivider(0.0);
    }

    public void undo() {
        if (!undoList.isEmpty()) {
            DrawLine item = undoList.pop();
            redoList.push(item);
            if (undoList.size() == 0) {
                setColor(Color.black);
                slide(3.0);
            } else {
                setColor(item.c);
                slide(item.l);
            }
        }
        redraw();
  }
    public void redo() {
       if (!redoList.isEmpty()) {
            DrawLine item = redoList.pop();
        item.draw();
        undoList.push(item);
        // put back the previous color
        setColor(item.c);
        slide(item.l);
        redraw();
     }
  } 
    public void clear() {
        redoList.removeAll(redoList);
  }
    public void redraw(){
    UI.clearGraphics();
    for (DrawLine items: undoList){
        items.draw();
    }
  }

    private static final int threshold = 140;
    /**
    * let user choose a color
     */
    public void colorPicker() {
        Color color = JColorChooser.showDialog(null, "Select a color", defaultColor);
        setColor(color);
  }
   /**
   * input a color and change the button to that
   * @param color
   */
  public void setColor(Color color) {
      if (color != null) {
          defaultColor = color;
          button.setBackground(defaultColor);
          // set font color
          if (defaultColor.getRed() < threshold && defaultColor.getBlue() < threshold && defaultColor.getGreen() < threshold | (defaultColor.getRed() + defaultColor.getBlue() + defaultColor.getGreen() < 300)) {
              button.setForeground(Color.white);
            } else {
                button.setForeground(Color.black);
            }
        }
   }
    public void slide(double slideSize) {
    lineSize = slideSize;
    UI.setLineWidth(lineSize);
  }

  /**
  * Respond to mouse events
  */
   public void doMouse(String action, double x, double y) {
        UI.setColor(defaultColor);
       if (action.equals("pressed")){
           lastX = x;
           lastY = y;
           // initialize drawline object for current object
           currentLine = new DrawLine(defaultColor, lineSize, x,y);
    }
    else if (action.equals("dragged")){
        UI.drawLine(lastX, lastY, x, y);
        lastX = x;
        lastY = y;
        currentLine.add(x,y);
    }
    else if (action.equals("released")){
        UI.drawLine(lastX, lastY, x, y);
        // finish drawing line and store it
        undoList.push(currentLine);
        clear();
    }
  }
    public static void main(String[] arguments){
    new Pencil().setupGUI();
  }
    private class DrawLine {
    private ArrayList<Double> listX = new ArrayList<>();
    private ArrayList<Double> listY = new ArrayList<>();
    public Color c;
    public double l;
    // initialize the draw line
    public DrawLine(Color c, double lineSize, double x, double y) {
        add(x,y);
        this.c = c;
        this.l = lineSize;
  }
    // add point to the list
    public void add(double x, double y) {
    listX.add(x);
    listY.add(y);
  }
    // draw each line one at a time
    public void draw() {
        if (listX.size() == 1) {
        listX.add(listX.get(0));
        listY.add(listY.get(0));
    }
    for (int i = 1; i < listX.size(); i++) {
        UI.setColor(c);
        UI.setLineWidth(l);
        UI.drawLine( listX.get(i-1), listY.get(i-1), listX.get(i), listY.get(i));
    }
  }
}
}