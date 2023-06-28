 // This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP102 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP102 - 2021T3, Assignment 3
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.awt.Color;

/** Runs a simulation of a robot vacuum cleaner that moves around a floor area,
 *      changing to a new random direction every time it hits the edge of the
 *      floor area.
 */
public class FloorCleaner{

    // Constants for drawing the robot and the floor.
    public static final double DIAM = 60;  //diameter of the robot.
    public static final double LEFT = 50;  // borders of the floor area.
    public static final double TOP = 50;
    public static final double RIGHT = 550;
    public static final double BOT = 420;
    public static final double left = 200;
    public static final double top = 100;
    public static final double wd = 100;
    public static final double ht = 200;
    public static double dist = 0;
    //field for furniture
    public static double dist1 = 0;
    public static double dist2 = 0;
    public static final double x =  300;
    public static final double y = 200;
    /* Simulation loop.
     * The method should draw a dirty floor (a gray rectangle), and then
     * create one robot (core) or two robots (completion) and make them run around for ever.
     * Each time step, each robot will erase the "dirt" under it, and then
     *  move forward a small amount.
     * After it has moved, the program should ask for the robot's
     *  position and check the position against the edges of the floor area.
     * If it has gone over the edge, it will make the robot step back onto the floor
     *  and change its direction.
     * For the completion, it will also check if the robots have hit each other, and
     *  if so, make them both back off and change direction
     * 
     * Hint: A robot should start in a "safe" initial position (not over the edge):
     *  its x position should be between  LEFT+DIAM/2 and RIGHT-DIAM/2
     *  its y position should be between  TOP+DIAM/2 and BOT-DIAM/2
     * Hint: For the completion, you have to make sure that starting positions of
     *  the robots are not on top of each other (otherwise they get "stuck" to each other!)
     */
    public void cleanFloor(){
        UI.setColor(Color.gray);
        UI.fillRect(LEFT,TOP,RIGHT,BOT);
        double randX = LEFT+ Math.random()*500;
        double randY = TOP + Math.random()*200;
        Robot robot1 = new Robot(DIAM,randX,randY,Color.red);
        while(true){
        robot1.step();
        robot1.draw(); 
        if(robot1.checkHit()) robot1.changeDir(true);
        UI.sleep(20);
        robot1.erase();
        }
    }
    public void cleanFloor2(){
        UI.setColor(Color.gray);
        UI.fillRect(LEFT,TOP,RIGHT,BOT);
        double randX1 = LEFT+ Math.random()*500;
        double randY1 = TOP + Math.random()*200;
        double randX2 = LEFT+ Math.random()*500;
        double randY2 = TOP + Math.random()*200;
        while (Math.abs(randX1 - randX2) < DIAM
        && Math.abs(randY1 - randY2) < DIAM){
            randX1 = 100 + Math.random()*300;
            randY1 = 100 + Math.random()*300;
            randX2 = 100 + Math.random()*300;
            randY2 = 100 + Math.random()*300;
        }
        Robot robot1 = new Robot(DIAM,randX1,randY1,Color.red);
        Robot robot2 = new Robot(DIAM,randX2,randY2,Color.red);
        double centre1 = randX1 - DIAM/2;
        double centre2 = randX2 - DIAM/2;
        while(true){
        robot1.step();
        if(robot1.checkHit())robot1.changeDir(true);
        robot2.step();
        if(robot2.checkHit())robot2.changeDir(true);
        if(robot1.checkHitEachOther(robot2)){
            robot1.changeDir(false);
            robot2.changeDir(false);
        }
        robot1.draw();
        robot2.draw();
        UI.sleep(20);
        robot1.erase();
        robot2.erase();
        }
        }
    public void cleanFloor3(){
        UI.setColor(Color.gray);
        UI.fillRect(LEFT,TOP,RIGHT,BOT);
        UI.setColor(Color.yellow);
        UI.fillOval(x,y,DIAM,DIAM);
        double randX1 = 100 + Math.random()*300;
        double randY1 = 100 + Math.random()*300;
        double randX2 = 100 + Math.random()*300;
        double randY2 = 100 + Math.random()*300;
        while (Math.abs(randX1 - randX2) < DIAM
        && Math.abs(randY1 - randY2) < DIAM){
            randX1 = 100 + Math.random()*300;
            randY1 = 100 + Math.random()*300;
            randX2 = 100 + Math.random()*300;
            randY2 = 100 + Math.random()*300;
        }
        while (Math.abs(randX1 - x) < DIAM
        && Math.abs(randY1 - y) < DIAM){
            randX1 = 100 + Math.random()*300;
            randY1 = 100 + Math.random()*300;
        }
        while (Math.abs(randX2 - x) < DIAM
        && Math.abs(randY2 - y) < DIAM){
            randX2 = 100 + Math.random()*300;
            randY2 = 100 + Math.random()*300;
        }
        Robot robot1 = new Robot(DIAM,randX1,randY1,Color.red);
        Robot robot2 = new Robot(DIAM,randX2,randY2,Color.red);
        double centre1 = randX1 - DIAM/2;
        double centre2 = randX2 - DIAM/2;
        while(true){
        robot1.step();
        robot1.changeDir(true);
        robot2.step();
        robot2.changeDir(true);
        double dist = Math.hypot(robot1.getX()- robot2.getX(), robot1.getY()-robot2.getY());
        double dist1 = Math.hypot(robot1.getX() -x ,robot1.getY() -y );
        double dist2 = Math.hypot(robot2.getX() - x, robot2.getY() - y);
        if(dist < DIAM){
        robot1.move();
        robot2.move();
        }
        if(dist1 < DIAM){
        robot1.tele();
        }
        if(dist2 < DIAM){
        robot2.tele();
        }
        robot1.draw();
        robot2.draw();
        UI.sleep(20);
        robot1.erase();
        robot2.erase();
        }
        }


    //------------------ Set up the GUI (buttons) ------------------------
    /** Make buttons to let the user run the methods */
    public void setupGUI(){
        UI.addButton("start", this::cleanFloor);
        UI.addButton("start completion", this::cleanFloor2);
        UI.addButton("start challenger", this::cleanFloor3);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(650,500);
        UI.setDivider(0);
    }    

    // Main
    public static void main(String[] arguments){
        FloorCleaner fc = new FloorCleaner();
        fc.setupGUI();
    }    

}