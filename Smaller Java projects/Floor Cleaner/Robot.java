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

/** The robot is a circular vacuum cleaner than runs around
 * a floor, erasing any "dirt".
 * The parameters of the constructor should include the initial position,
 * and possibly its size and color.
 * It has methods to make it step and change direction:
 *  step() makes it go forward one step in its current direction.
 *  changeDirection() makes it go backward one step, and then turn to a new
 *     (random) direction.
 * It has methods to report its current position (x and y) with the
 *     getX() and getY() methods.
 * It has methods to erase and draw itself
 *  draw() will make it draw itself,
 *  erase() will make it erase itself (cleaning the floor under it also!)
 *
 * Hint: if the the current direction of the robot is d (expressed in
 *  degrees clockwise from East), then it should step
 *     cos(d * pi/180) in the horizontal direction, and
 *     sin(d * pi/180) in the vertical direction.
 * Hint: see the Math class documentation!
 */

public class Robot{
    public static final double DIAM = 60;  //diameter of the robot.
    public static final double LEFT = 50;  // borders of the floor area.
    public static final double TOP = 50;
    public static final double RIGHT = 550;
    public static final double BOT = 420;
    public static final double left = 200;
    public static final double top = 300;
    public static final double wd = 400;
    public static final double ht = 200;
    public static enum wallHit{
            LEFT,
            RIGHT,
            TOP,
            BOT,
            BALL,
            NO
    }

    // Fields to store the state of the robot.
    private double stepx = 0.6;
    private double stepy = 0.6 ;
    private double xpos;
    private double ypos;
    private String direction = "right";
    private double diam;
    private double hyp;
    private double angle;
    private wallHit wallcheck = wallHit.NO;
    private Color color;
    /** Construct a new Robot object.
     *  set its direction to a random direction, 0..360
     */
    public Robot(double diam, double xpos, double ypos, Color color){
        this.xpos = xpos;
        this.ypos = ypos;
        this.diam = diam;
        this.hyp = Math.hypot(stepx,stepy);
        this.color = color;
    }
    // Methods to return the x and y coordinates of the current position 
    public double getX(){
    return xpos;
    }
    public double getY(){
    return ypos;
    }
    /** Step one unit in the current direction (but don't redraw) */
    public void step(){
       this.xpos += 1.5*stepx;
       this.ypos += 1.5*stepy;
       this.hyp = Math.hypot(stepx,stepy);
    }
    public void getXspeed(double Xspeed){
    this.stepx = Xspeed;
    }
    public void getYspeed(double Yspeed){
    this.stepy = Yspeed;
    }
    /** changeDirection: move backwards one unit and change direction randomly */
    public void changeDir(boolean isHitWall){
        if (isHitWall){
        if(this.xpos + this.stepx <=LEFT){
            double value = getRandomDouble(-Math.PI+Math.PI/6,Math.PI - Math.PI/6);
            this.stepx = Math.sin(value)*this.hyp;
            this.stepy = Math.cos(value)*this.hyp;
        }
        if(this.xpos + this.stepx + diam/15 >= RIGHT){
            double value = getRandomDouble(Math.PI/2+Math.PI/6,Math.PI/2*3 - Math.PI/6);
            this.stepx = Math.sin(value)*this.hyp;
            this.stepy = Math.cos(value)*this.hyp;
        }
        if(this.ypos +this.stepy <= TOP){
            double value = getRandomDouble(-Math.PI+Math.PI/6,- Math.PI/6);
            this.stepx = Math.sin(value)*this.hyp;
            this.stepy = Math.cos(value)*this.hyp;
        }
        if(this.ypos +this.stepy + diam/15 >= BOT){
            double value = getRandomDouble(0+Math.PI/6,Math.PI - Math.PI/6);
            this.stepx = Math.sin(value)*this.hyp;
            this.stepy = Math.cos(value)*this.hyp;

        }
        }else {
               this.stepx = - this.stepx;
               this.stepy = - this.stepy;
        }
    }
    public boolean checkHit(){
        if (this.wallcheck == wallHit.NO || this.wallcheck == wallHit.BALL){
            if(this.getX()<=LEFT) this.wallcheck = wallHit.LEFT;
            else if (this.getX()-DIAM/2 >= 620) this.wallcheck = wallHit.RIGHT;
            else if (this.getY() <= TOP) this.wallcheck = wallHit.TOP;
            else if (this.getY()-DIAM/2 >= BOT) this.wallcheck = wallHit.BOT;
            return true;
        }
        else if (this.wallcheck == wallHit.RIGHT){
            if(this.getX() <= LEFT) this.wallcheck = wallHit.LEFT;
            else if (this.getY() <= TOP) this.wallcheck = wallHit.TOP;
            else if (this.getY()-DIAM/2 >= BOT) this.wallcheck = wallHit.BOT;
            return true;
        }
        else if (this.wallcheck == wallHit.LEFT){
            if (this.getX()-DIAM/2 >= (RIGHT+100*2)) this.wallcheck = wallHit.RIGHT;
            else if (this.getY() <= TOP) this.wallcheck = wallHit.TOP;
            else if (this.getY()-DIAM/2 >= BOT) this.wallcheck = wallHit.BOT;
            return true;
        }   
        else if (this.wallcheck == wallHit.BOT){
            if(this.getX() <= LEFT) this.wallcheck = wallHit.LEFT;
            else if (this.getX()-DIAM/2 >= RIGHT) this.wallcheck = wallHit.RIGHT;
            else if (this.getY() <= TOP) this.wallcheck = wallHit.TOP;
            return true;
        }
        else if (this.wallcheck == wallHit.TOP){
            if(this.getX() <= LEFT) this.wallcheck = wallHit.LEFT;
            else if (this.getX()-DIAM/2 >= RIGHT) this.wallcheck = wallHit.RIGHT;
            else if (this.getY()-DIAM/2 >= BOT) this.wallcheck = wallHit.BOT;
            return true;
        }
        return false;
    }
    public boolean checkHitEachOther(Robot other){
        double dist = Math.hypot(this.getX()- other.getX(), this.getY()-other.getY());
        if(dist <= DIAM && wallcheck != wallHit.BALL){
            wallcheck = wallHit.BALL;
            return true;
        }
        return false;
    }
    public void move (){
        if(FloorCleaner.dist < DIAM){
            this.stepx = -this.stepx;
            this.stepy = -this.stepy;
        }
    }
    public void tele(){
        if(FloorCleaner.dist1 < DIAM || FloorCleaner.dist2 < DIAM){
            this.stepx = -this.stepx;
            this.stepy = -this.stepx;
        }
    }
    public double getRandomDouble(double min, double max){
        return((Math.random()*(max - min)))+min;
    }
    /** Erase the robot */
    public void erase(){
        UI.setColor(Color.white);
        UI.eraseOval(this.xpos,this.ypos,diam,diam);
    }

    /** Draw the robot */
    public void draw(){
        UI.setColor(color);
        UI.fillOval(this.xpos,this.ypos,diam,diam);
        UI.setColor(Color.black);
        UI.drawOval(this.xpos,this.ypos,diam,diam);
        UI.fillOval(this.xpos+DIAM/2+15*this.stepx,this.ypos+DIAM/2+15*this.stepy,5,5);
    }
    
    private double getAngle(){
        double a;
        a = Math.atan(stepy/stepx);
        return a;
    }
}
