import ecs100.*;
import java.awt.Color;
import java.awt.Shape;
/**
 * Write a description of class drawFlag here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class drawFlag
{
    // instance variables - replace the example below with your own
    private final static double LEFT = 100;
    private final static double TOP = 100;
    private double ratio = 0.5;
    /**
     * Constructor for objects of class drawFlag
     */
    public drawFlag()
    {
    }
    public void draw(){
        double size = UI.askDouble("Insert size: ");
        double height = size * ratio;
        double singleUnit =  height/144;
        UI.setColor(Color.red);
        UI.fillRect(LEFT,TOP,size,height);
        UI.setColor(Color.blue);
        UI.fillRect(LEFT,TOP,size/2,height/2);
        UI.setColor(Color.white);
        //UI.drawRect(LEFT+singleUnit*64,TOP+singleUnit*3,singleUnit*18,singleUnit*18);
        //UI.drawRect(LEFT+singleUnit*42,TOP+singleUnit*21,singleUnit*16,singleUnit*16);
        //UI.drawRect(LEFT+singleUnit*87,TOP+singleUnit*18,singleUnit*15,singleUnit*15);
        //UI.drawRect(LEFT+singleUnit*62,TOP+singleUnit*49,singleUnit*20,singleUnit*20);
        fillStar(LEFT+singleUnit*64 + singleUnit*9, TOP+singleUnit*3+singleUnit*10, singleUnit*10 , 5, 0.35);
        fillStar(LEFT+singleUnit*42 + singleUnit*8, TOP+singleUnit*21+singleUnit*9, singleUnit*9 , 5, 0.35);
        fillStar(LEFT+singleUnit*87 + singleUnit*7.5, TOP+singleUnit*18+singleUnit*8.5, singleUnit*8.5 , 5, 0.35);
        fillStar(LEFT+singleUnit*62 + singleUnit*10, TOP+singleUnit*49+singleUnit*11, singleUnit*11 , 5, 0.35);
        fillStar(LEFT+singleUnit*77 + singleUnit*5, TOP+singleUnit*36+singleUnit*6, singleUnit*6 , 5, 0.35);
    }    
    public void fillStar( double ctrX, double ctrY, double radius, int  nSpikes, double spikiness)
       {    
        int nPoints = nSpikes * 2;

        double xPoint[] = new double[nPoints];
        double yPoint[] = new double[nPoints];

           for (int i = 0; i < nPoints; i++)
           {
             double iRadius = (i % 2 == 0) ? radius : (radius * spikiness);
             double angle = (i * 360.0) / (nPoints);

             xPoint[i] = ctrX + iRadius * Math.cos(Math.toRadians(angle - 90));
             yPoint[i] = ctrY + iRadius * Math.sin(Math.toRadians(angle - 90));
           }
           UI.fillPolygon(xPoint, yPoint, nPoints); // Creates polygon
       }
    public void setupGUI(){
        UI.initialise();
        UI.addButton("Draw", this::draw);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(900,400);
        UI.setDivider(1.0);  //text pane only 
    }
    public static void main(String[] arguments){
        drawFlag obj = new drawFlag();
        obj.setupGUI();
    }   
}
