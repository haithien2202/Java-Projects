// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3
 * Name:    Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

import ecs100.UI;
import java.awt.*;
import java.util.*;

///button to make, slider for size, slider for speed, delay -> field.

/**
 * Search for a path to the goal in a maze.
 * The maze consists of a graph of MazeCells:
 *  Each cell has a collection of neighbouring cells.
 *  Each cell can be "visited" and it will remember that it has been visited
 *  A MazeCell is Iterable, so that you can iterate through its neighbour cells with
 *    for(MazeCell neighbour : cell){....
 *
 * The maze has a goal cell (shown in green, two thirds towards the bottom right corner)
 * The maze.getGoal() method returns the goal cell of the maze.
 * The user can click on a cell, and the program will search for a path
 * from that cell to the goal.
 * 
 * Every cell that is looked at during the search is coloured  yellow, and then,
 * if the cell turns out to be on a dead end, it is coloured red.
 */

public class MazeSearch {


    private Maze maze;
    private String search = "first";   // "first", "all", or "shortest"
    private int pathCount = 0;
    private boolean stopNow = false;
    private ArrayList<ArrayList<MazeCell>>  listOfLists = new ArrayList<ArrayList<MazeCell>>();
    private Stack<MazeCell> cellStack = new Stack<MazeCell>();
    private ArrayList<MazeCell> smallest = new ArrayList<MazeCell>();
    private int min = 9999;

    /** CORE
     * Search for a path from a cell to the goal.
     * Return true if we got to the goal via this cell (and don't
     *  search for any more paths).
     * Return false if there is not a path via this cell.
     * 
     * If the cell is the goal, then we have found a path - return true.
     * If the cell is already visited, then abandon this path - return false.
     * Otherwise,
     *  Mark the cell as visited, and colour it yellow [and pause: UI.sleep(delay);]
     *  Recursively try exploring from the cell's neighbouring cells, returning true
     *   if a neighbour leads to the goal
     *  If no neighbour leads to a goal,
     *    colour the cell red (to signal failure)
     *    abandon the path - return false.
     */
    public boolean exploreFromCell(MazeCell cell) {
        cell.visit();
        try {
            Thread.sleep(70);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        if (cell == maze.getGoal()) {
            cell.draw(Color.blue);   // to indicate finding the goal
            return true;
        }
        boolean v = false;
        for (MazeCell neigh : cell){
            if (!neigh.isVisited()){
            cell.draw(Color.yellow);
            v = exploreFromCell(neigh);         
          }
          if (v == false){
                cell.draw(Color.red);
            }else return true;
        }
        return false;
    }
    
    //searching for all paths, please press stop before changing to other searching modes
    /** COMPLETION
     * Search for all paths from a cell,
     * If we reach the goal, then we have found a complete path,
     *  so pause for 1000 milliseconds 
     * Otherwise,
     *  visit the cell, and colour it yellow [and pause: UI.sleep(delay);]
     *  Recursively explore from the cell's neighbours, 
     *  unvisit the cell and colour it white.
     * 
     */
    public void exploreFromCellAll(MazeCell cell) {
        if (stopNow) { return; }    // exit if user clicked the stop now button
        cell.visit();
        cell.draw(Color.yellow); 
        try {
            Thread.sleep(70);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        if (cell == maze.getGoal()) {
            cell.draw(Color.blue);   // to indicate finding the goal
            try {
            Thread.sleep(1000);
          } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
          }
           cell.draw(Color.green);
        }else{
            for (MazeCell neigh : cell){
            if (!neigh.isVisited()){ 
            exploreFromCellAll(neigh);         
           }
         }
        }
            if(cell != maze.getGoal()){
                cell.draw(Color.white);
        }
        cell.unvisit();
    }
    
    
    //Find the sortest path, please press stop before changing to other searching modes as it still looking for the shorter paths
    /** CHALLENGE
     * Search for shortest path from a cell,
     * Use Breadth first search.
     */
    public void exploreFromCellShortest(MazeCell cell) {
        if (stopNow) { return; }    // exit if user clicked the stop now button
        cellStack.push(cell);
        cell.visit();
        if (cell == maze.getGoal()) {
            ArrayList<MazeCell> list = new ArrayList<MazeCell>(cellStack);
           listOfLists.add(list);
           for (ArrayList<MazeCell> lists : listOfLists){
            if (lists.size() < min){
                min = lists.size();
                smallest = lists;
            }
          }
           drawPath(smallest);
        }else{
            for (MazeCell neigh : cell){
            if (!neigh.isVisited()){
            exploreFromCellShortest(neigh);         
           }
         }
        }
        cellStack.pop();
        cell.unvisit();     
    }
    
    public void drawPath(ArrayList<MazeCell> list){
        maze.draw();
        for (MazeCell m : list){
            if (m == maze.getGoal())m.draw(Color.blue);
            else
            m.draw(Color.yellow);
        }
    }

    //=================================================

    // fields for gui.
    private int delay = 20;
    private int size = 10;
    /**
     * Set up the interface
     */
    public void setupGui(){
        UI.addButton("New Maze", this::makeMaze);
        UI.addSlider("Maze Size", 4, 40,10, (double v)->{size=(int)v;});
        UI.setMouseListener(this::doMouse);
        UI.addButton("First path",    ()->{search="first";});
        UI.addButton("All paths",     ()->{search="all";});
        UI.addButton("Shortest path", ()->{search="shortest";});
        UI.addButton("Stop",          ()->{stopNow=true;});
        UI.addSlider("Speed", 1, 101,80, (double v)->{delay=(int)(100-v);});
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.);
    }

    /**
     * Creates a new maze and draws it .
     */
    public void makeMaze(){
        maze = new Maze(size);
        maze.draw();
    }

    /**
     * Clicking the mouse on a cell should make the program
     * search for a path from the clicked cell to the goal.
     */
    public void doMouse(String action, double x, double y){
        if (action.equals("released")){
            listOfLists.clear();
            cellStack.clear();
            smallest.clear();
            maze.reset();
            maze.draw();
            pathCount = 0;
            min = 9999;
            MazeCell start = maze.getCellAt(x, y);
            if (search=="first"){
                exploreFromCell(start);
            }
            else if (search=="all"){
                stopNow=false;
                exploreFromCellAll(start);
            }
            else if (search=="shortest"){
                stopNow=false;
                exploreFromCellShortest(start);
            }
        }
    }

    public static void main(String[] args) {
        MazeSearch ms = new MazeSearch();
        ms.setupGui();
        ms.makeMaze();
    }
}

