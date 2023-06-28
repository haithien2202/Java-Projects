
// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3
 * Name:    Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
import java.nio.file.*;

/** <description of class OrganisationChart>
 */

public class OrganisationChart {

    // Fields
    private Position organisation;             // the root of the current organisational chart 
    private Position pressedPosition = null;   // the position on which the mouse was pressed
    private Position selectedPosition = null;  // the selected position on which we can modify
                                               //  the attributes.
    private boolean newPosition = false;       // adding a new Position to tree
                                               //  the user entered
    private boolean checkSub = false;                                            
    private String newRole = null;
    private double initialX = 0;
    private Position current = null;
    private List<Position> loadFile = new ArrayList<Position>();

    // constants for the layout
    public static final double NEW_LEFT = 10;  // left of the new position Icon
    public static final double NEW_TOP = 10;   // top of the new position Icon

    public static final double ICON_X = 40;    // location and size of the remove icon
    public static final double ICON_Y = 100;   
    public static final double ICON_RAD = 20; 


    /**
     * Set up the GUI (buttons and mouse)
     */
    public void setupGUI(){
        UI.setMouseMotionListener( this::doMouse );
        UI.addTextField("Change Role", this::setRole);
        UI.addButton("Load test tree",  this::makeTestTree); 
        UI.addButton("Save to file",  this::saveFile); 
        UI.addButton("Load from file",  this::loadFile);
        UI.addButton("Fix position",  this::fixPos);
        UI.addButton("Reset",  this::reset); 
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1100,500);
        UI.setDivider(0);
    }

    /**
     * initialise the root of the organisation
     */
    public void initialiseChart() {
        organisation = new Position("CEO");   // Set the root node of the organisation
        redraw();
    }
    
    /** If a Position has been selected, update the name of the role of this position */
    public void setRole (String v){
        if (selectedPosition != null) {
            selectedPosition.setRole(v);
        }
        redraw();
    }
    
    
    //save file
    public void saveFile(){  
    try
    {
        String saveFile = UI.askString("File name:") +".txt";
        FileOutputStream outputStream = new FileOutputStream(saveFile);
        PrintWriter printWriter = new PrintWriter(outputStream);
        boolean checkend = false;
        boolean checkManager = false;
        makeString(organisation,checkend,checkManager,printWriter);
        printWriter.close();
    }catch(IOException e){UI.println("File reading failed");}
    }
    
    public void makeString(Position pos, boolean checkend,boolean checkManager ,PrintWriter printWriter){
        String name = pos.getRole(); 
        double offset = pos.getOffset();
        printWriter.println(name + " " + offset);    
        if (checkManager == true)printWriter.println("/");
        List<Position> team = new ArrayList<Position>(pos.getTeam());
        for ( int i = 0; i < team.size() ; i++ ){
            if ( i+1 == team.size()) checkend = true;
            else checkend = false;
            if ( team.get(i).isManager() == false) checkManager = true;
            else checkManager = false;
            makeString(team.get(i),checkend,checkManager,printWriter);
        }
        if (checkend == true )printWriter.println("/");
        
    }
    
    
    //load from file
    public void loadFile(){
        initialiseChart();
        try {
            String FileName = UIFileChooser.open("Choose file");      
            File newFile = new File(FileName);
            Scanner myReader = new Scanner(newFile);
            Position Target = null;
            Stack<Position> previous = new Stack<Position>();
            while (myReader.hasNext()){           
                String pos = myReader.next(); 
                double off = 0;
                if (pos != "/" && pos.equals("CEO")){
                    off = myReader.nextDouble();
                    Target = organisation;
                }
                else if (!pos.equals("CEO")){
                    if (!pos.equals("/") )   {   
                    off = myReader.nextDouble();
                    Position Pos = new Position(pos); 
                    addPos(Pos,Target,off);
                    previous.push(Target);
                    Target = Pos;
                  }else if (pos.equals("/")){
                    if ( previous.size() > 0 )
                    Target = previous.pop();            
                  }
              }
                myReader.nextLine();
            }
        } catch(IOException e){UI.println("File reading failed");}
        redraw();
    }
    
    public void addPos(Position pos, Position target, double offset){
            //UI.println(pos+" " +target);
            target.addToTeam(pos);
            pos.setOffset(offset);
    }
    
    /**
     * Most of the work is initiated by the mouse.
     * 
     * The action depends on where the mouse is pressed:
     *   on the new icon,
     *   a Position in the tree, or
     * and where it is released:
     *   on the same Position,
     *   another Position in the tree,
     *   on the delete Icon, or
     *   empty space
     * 
     * See the table in the assignment description.
     * The method follows the structure of the table.
     */
    public void doMouse(String action, double x, double y){
        boolean drag = false;
        if (action.equals("pressed")){
            //initialise
            newPosition = false;
            pressedPosition = null; 
            if (onNewIcon(x, y)) {// adding a new vacant Position to tree
                newPosition = true;
            }
            else { // acting on an existing Position
                pressedPosition = findPosition(x, y, organisation);
                if (pressedPosition != null) {
                    pressedPosition.draw(false, true);            
                }
            }
             //UI.println(pressedPosition);
        }
        else if (action.equals("dragged")){
             if (newPosition == true){ 
                 redrawDragging(x,y,newPosition);
                }
             if (pressedPosition != null  && drag == false){
                 redrawDragging(x,y,newPosition);
                }
        }
        else if (action.equals("released")){
            Position targetPosition = findPosition(x, y, organisation);
            // pressed on "new" icon, released on a target
            if (newPosition && targetPosition != null){
                Position newP = new Position();
                addNewPosition(newP, targetPosition);     // Method to complete!
            }
            // pressed and released on a Position 
            else if (pressedPosition != null && targetPosition == pressedPosition) {
                // selecting a position
                selectedPosition = targetPosition;
            }
            // pressed on a Position and released on empty space
            else if (pressedPosition != null && targetPosition == null && ! onRemoveIcon(x, y)){
                // move the Position to left or right
                pressedPosition.moveOffset(x);
                }
            // pressed on a Position 
            else if (pressedPosition != null){
                if (targetPosition != null ) {
                    // moving pressed position to target
                    movePosition(pressedPosition, targetPosition);  // Method to complete!
                }
                else if (onRemoveIcon(x, y) ){
                    // removing Position from tree   
                    removePosition(pressedPosition);                // Method to complete!
                }  
            }
            this.redraw();
        }
    }
    

    //  METHODS FOR YOU TO COMPLETE ===============================
    
    // Drawing the tree  =========================================
    /** [STEP 1:]
     *  Recursive method to draw all nodes in a subtree, given the root node.
     *  (The provided code just draws the root node;
     *  you need to make it draw all the nodes.)
     */
    private void drawTree(Position pos) {
        if (pos==selectedPosition){
            pos.drawHighlighted();
        }
        else {
            pos.draw();
        }
        //draw the nodes under pos
        Set<Position> team = new HashSet<Position>(pos.getTeam());
        for ( Position x : team ) {
            drawTree(x);
        }
    }
    
    /** 
     * Find and return a Position that is currently placed over the point (x,y). 
     * Must do a recursive search of the subtree whose root is the given Position.
     * [STEP 2:] 
     *    Returns a Position if it finds one,
     *    Returns null if it doesn't.
     * [Completion:] If (x,y) is under two Positions, it should return the top one.
     */
    private Position findPosition(double x, double y, Position pos){
        current = null;
        findingPosition(x,y,pos);    
        if (current != null) return current;
        return null;  // it wasn't found;
    }
    
    private void findingPosition(double x, double y, Position pos){
        if ( pos.on(x,y)) current = pos;
        Set<Position> team = new HashSet<Position>(pos.getTeam());  
        for ( Position n : team ) {
            findingPosition(x,y,n);  
        }
    }
    
    /** [STEP 2:] 
     * Add the new position to the target's team.
     * Check the arguments are valid first.
     */
    public void addNewPosition(Position newPos, Position target){
            target.addToTeam(newPos);
    }

    /** [STEP 2:] 
     *    Move a current position (pos) to another position (target)
     *    by adding the position to the team of the target,
     *    (and bringing the whole subtree of the position with them)
     *    Check the arguments are valid first.
     *
     * [COMPLETION:]
     *   Moving any position to a target that is in the
     *   position's subtree is a problem and should not be allowed. (Why?)
     *   (one consequence is that the CEO position can't be moved at all)
     */
    private void movePosition(Position pos, Position target) {
        if (inSubtree(pos,target) == true) return;
        if ( isExisted(pos,target) == false){
        Position manager = pos.getManager();
        target.addToTeam(pos);
        manager.removeFromTeam(pos);
       }
    }
    
    public boolean isExisted(Position pos, Position target){
        Set<Position> team = new HashSet<Position>(target.getTeam());  
        for (Position n : team){
            if (n == pos){
                return true;
            }
        }
        return false;
    }

    /** [STEP 2:]
     * Remove a position by removing it from the tree completely.
     * The position cannot be a manager of another position.
     * If this removes the current selected position, then there
     *  should now be no selected position
     */
    public void removePosition(Position pos){
        if (pos == null || pos.isManager() ){return;}   //invalid arguments.
        Position manager = pos.getManager();
        manager.removeFromTeam(pos);
    }

    /** [COMPLETION:]
     * Return true if position is in the subtree, and false otherwise
     * Uses == to determine node equality
     *  Check if positition is the same as the root of subTree
     *  if not, check if in any of the subtrees of the team members of the root
     *  (recursive call, which must return true if it finds the position)
     */
    private boolean inSubtree(Position pos, Position subtree) {
        checkSub = false;
        isSubtree(pos,subtree);
        return checkSub;
    }
    
    private void isSubtree(Position pos , Position subtree){
        if ( pos == subtree )checkSub = true;
        Set<Position> team = new HashSet<Position>(pos.getTeam());  
        for ( Position n : team ) {
            isSubtree(n,subtree);
        }
    }

    /**
     * Redraw the entire organisation chart.
     */
    private void redraw() {
        UI.clearGraphics();
        drawTree(organisation);
        drawNewIcon();
        drawRetireIcon();
    }
    
    public void fixPos(){
        List<Position> team = new ArrayList<Position>();
        fixPosition(organisation,team ,0);
        redraw();
    }
    
    
    //fix the position, make it spacing out equally
    public void fixPosition(Position pos,List<Position> team1, int index){
        double teamSize = 0;
        if (pos != organisation)teamSize = pos.getManager().getSpace();
        double mid = -(teamSize-66)/2;
        double currentSpace = pos.getSpace()/2;
        double space = 0;
        if (team1.size() > 0){
            for (int i = 0 ; i < index ; i ++){
            if (i == 0) space = space + team1.get(i).getSpace();
            else
            space = space + team1.get(i).getSpace();
        }
        if ( pos.getManager().getTeam().size() == 1)pos.setOffset(0);
              else pos.setOffset(mid + space + currentSpace - 33);
        }     
        List<Position> team = new ArrayList<Position>(pos.getTeam());
         Collections.sort(team, ( Position a, Position b) ->{
            if (a.getOffset() > b.getOffset()) return 1;
            if (a.getOffset() < b.getOffset()) return -1;
        return 0;
        });
        for (int i =0 ; i < team.size() ; i++){ 
              Position n = team.get(i);
              fixPosition(n,team,i);
        }
    }
    
    private void redrawDragging(double x,double y, boolean drag) {
        if ( drag == false ) {
        UI.clearGraphics();
        drawTree(organisation);
        drawNewIcon();
        drawRetireIcon();
        pressedPosition.drawDragging(x-55/2,y-40/2, false, true, onRemoveIcon(x,y));
    } else {
        UI.clearGraphics();
        drawTree(organisation);
        drawNewIcon();
        drawRetireIcon();
        UI.setColor(Position.BACKGROUND_COL);
        UI.fillRect(x-25,y-25,Position.WIDTH, Position.HEIGHT);
        UI.setColor(Color.black);
        UI.drawRect(x-25,y-25,Position.WIDTH, Position.HEIGHT);
        UI.drawString("NEW", x+8-25, y-25+Position.HEIGHT/2-5);
        UI.drawString("POSN", x+5-25, y-25+Position.HEIGHT/2+10);
    }
    }
    
   

    // OTHER DRAWING METHODS =======================================
    /**
     * Redraw the new Person box
     */
    private void drawNewIcon(){
        UI.setColor(Position.BACKGROUND_COL);
        UI.fillRect(NEW_LEFT,NEW_TOP,Position.WIDTH, Position.HEIGHT);
        UI.setColor(Color.black);
        UI.drawRect(NEW_LEFT,NEW_TOP,Position.WIDTH, Position.HEIGHT);
        UI.drawString("NEW", NEW_LEFT+8, NEW_TOP+Position.HEIGHT/2-5);
        UI.drawString("POSN", NEW_LEFT+5, NEW_TOP+Position.HEIGHT/2+10);
    }

    /**
     * Redraw the remove Icon
     */
    private void drawRetireIcon(){
        UI.setColor(Color.red);
        UI.setLineWidth(5);
        UI.drawOval(ICON_X-ICON_RAD, ICON_Y-ICON_RAD, ICON_RAD*2, ICON_RAD*2);
        double off = ICON_RAD*0.68;
        UI.drawLine((ICON_X - off), (ICON_Y - off), (ICON_X + off), (ICON_Y + off));
        UI.setLineWidth(1);
        UI.setColor(Color.black);
    }

    /** is the mouse position on the New Position box */
    private boolean onNewIcon(double x, double y){
        return ((x >= NEW_LEFT) && (x <= NEW_LEFT + Position.WIDTH) &&
            (y >= NEW_TOP) && (y <= NEW_TOP + Position.HEIGHT));
    }

    /** is the mouse position on the remove icon */
    private boolean onRemoveIcon(double x, double y){
        return (Math.abs(x - ICON_X) < ICON_RAD) && (Math.abs(y - ICON_Y) < ICON_RAD);
    }


    // Testing ==============================================
    /**
     * Makes an initial tree so you can test your program
     */
    private void makeTestTree(){
        organisation = new Position("CEO");
        Position aa = new Position("VP1");
        Position bb = new Position("VP2");
        Position cc = new Position("VP3");
        Position dd = new Position("VP4");
        Position a1 = new Position("AL1");
        Position a2 = new Position("AL2");
        Position b1 = new Position("AS");
        Position b2 = new Position("DPA");
        Position d1 = new Position("DBP");
        Position d2 = new Position("SEP");
        Position d3 = new Position("MSP");

        organisation.addToTeam(aa); aa.setOffset(-160);
        organisation.addToTeam(bb); bb.setOffset(-50);
        organisation.addToTeam(cc); cc.setOffset(15);
        organisation.addToTeam(dd); dd.setOffset(150);

        aa.addToTeam(a1); a1.setOffset(-35);
        aa.addToTeam(a2); a2.setOffset(25);
        bb.addToTeam(b1); b1.setOffset(-25);
        bb.addToTeam(b2); b2.setOffset(35);
        dd.addToTeam(d1); d2.setOffset(-60);
        dd.addToTeam(d2); 
        dd.addToTeam(d3); d3.setOffset(60);
        organisation.addToTeam(aa); aa.setOffset(-160);

        selectedPosition = null;
        this.redraw();
    }

    //* Test for printing out the tree structure, indented text */
    private void printTree(Position posn, String indent){
        UI.println(indent+posn+ " " +
            (posn.getManager()==null?"noM":"hasM") + " " +
            posn.getTeam().size()+" reports");
        String subIndent = indent+"  ";
        for (Position tm : posn.getTeam()){
            printTree(tm, subIndent);
        }
    }

    // Main
    public static void main(String[] arguments) {
        OrganisationChart oc = new OrganisationChart();
        oc.setupGUI();
        oc.initialiseChart();
    }
    
    public void reset(){
        initialiseChart();
    }
        

}
