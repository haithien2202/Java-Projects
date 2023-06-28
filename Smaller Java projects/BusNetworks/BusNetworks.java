// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3
 * Name:    Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

import ecs100.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.awt.Color;

public class BusNetworks {

    /** Map of towns, indexed by their names */
    private Map<String,Town> busNetwork = new HashMap<String,Town>();
    private boolean correctFile = false;
    private Town current;
    private double scaleX = 60;
    private double scaleY = 90;
    private double initialX = 0;
    private double initialY = 0;
    private double lastX;
    private double lastY;

    /** CORE
     * Loads a network of towns from a file.
     * Constructs a Set of Town objects in the busNetwork field
     * Each town has a name and a set of neighbouring towns
     * First line of file contains the names of all the towns.
     * Remaining lines have pairs of names of towns that are connected.
     */
    public void loadNetwork(String filename) {
        try {
            busNetwork.clear();
            UI.clearText();
            UI.clearGraphics();
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
                if ( scanner.hasNextInt()){
                    correctFile = true;
                    loadLadLongNetwork(filename);      
                }else {
                    correctFile = false;
                    List<String> lines = Files.readAllLines(Path.of(filename));
                    String firstLine = lines.remove(0);
                    Scanner scan = new Scanner(firstLine);
                    while (scan.hasNext()){
                        String name = scan.next();
                        Town newTown = new Town(name);
                        busNetwork.put(name,newTown);
                    }
                    for (String line : lines ) {
                        Scanner sc = new Scanner(line);
                        String name = sc.next();
                        Town current = busNetwork.get(name);
                        while(sc.hasNext()){
                            String neighbour = sc.next();
                            Town neigh = busNetwork.get(neighbour);
                            current.addNeighbour(neigh);
                            neigh.addNeighbour(current);
                        }
                    }
                    UI.println("Loaded " + busNetwork.size() + " towns:");
            }
        } catch (IOException e) {throw new RuntimeException("Loading data.txt failed" + e);}
    }
    
    public void loadLadLongNetwork(String filename){
        try {
            busNetwork.clear();
            UI.clearText();
            List<String> lines = Files.readAllLines(Path.of(filename));
            int firstLine = Integer.valueOf(lines.remove(0));
            for ( int i = 0 ; i < firstLine ; i++){
                Scanner scan = new Scanner(lines.remove(0));
                String name = scan.next();
                double ladt = Double.valueOf(scan.next()) * -1;
                double longt = Double.valueOf(scan.next()); 
                Town newTown = new Town(name);
                newTown.setLong(longt);
                newTown.setLad(ladt);
                busNetwork.put(name,newTown);  
            }
            for (String line : lines) {
                Scanner sc = new Scanner(line);
                String name = sc.next();
                Town current = busNetwork.get(name);
                while(sc.hasNext()){
                    String neighbour = sc.next();
                    Town neigh = busNetwork.get(neighbour);
                    current.addNeighbour(neigh);
                    neigh.addNeighbour(current);
                }
            }
            UI.println("Loaded " + busNetwork.size() + " towns:");

        } catch (IOException e) {throw new RuntimeException("Loading data.txt failed" + e);}
    }

    /**  CORE
     * Print all the towns and their neighbours:
     * Each line starts with the name of the town, followed by
     *  the names of all its immediate neighbours,
     */
    public void printNetwork() {
        UI.println("The current network: \n====================");
        for ( String name : busNetwork.keySet()){
            Set<Town> neighbours = busNetwork.get(name).getNeighbours();
            String neigh = "";
            for ( Town neighbour : neighbours){
                neigh += " ";
                neigh += neighbour.getName();
            }
            UI.println(name + " ->" + neigh);
        }
    }

    /** COMPLETION
     * Return a set of all the nodes that are connected to the given node.
     * Traverse the network from this node in the standard way, using a
     * visited set, and then return the visited set
     */
    public Set<Town> findAllConnected(Town town) {
        Set<Town> newSet = new HashSet<Town>(town.getNeighbours());
        town.setVisited(true);
        Set<Town> neigh = new HashSet<Town>();
        for (Town towns : newSet){
            if (!towns.isVisited()){
                neigh.addAll(findAllConnected(towns));
            }
        }
        newSet.addAll(neigh);
        return newSet;
    }

    /**  COMPLETION
     * Print all the towns that are reachable through the network from
     * the town with the given name.
     * Note, do not include the town itself in the list.
     */
    public void printReachable(String name){
        Town town = busNetwork.get(name);      
        if (town==null){
            UI.println(name+" is not a recognised town");
        }
        else {
            Set<Town> reachable = new HashSet<Town>(findAllConnected(town));
            reachable.remove(town);
            UI.println("\nFrom "+town.getName()+" you can get to:");
            for (Town towns : reachable){
                UI.println(towns);
            }
        }
        setUnvisited();
    }
    
    //set unvisited all
    public void setUnvisited(){
        for ( String name : busNetwork.keySet()){
            Town town = busNetwork.get(name);
            town.setVisited(false);
        }
    }
    
    public void setUnselected(){
        for ( String name : busNetwork.keySet()){
            Town town = busNetwork.get(name);
            town.setSelected(false);
        }
    }

    /**  COMPLETION
     * Print all the connected sets of towns in the busNetwork
     * Each line of the output should be the names of the towns in a connected set
     * Works through busNetwork, using findAllConnected on each town that hasn't
     * yet been printed out.
     */
    public void printConnectedGroups() {
        UI.println("Groups of Connected Towns: \n================");
        int groupNum = 1;
        for ( String name : busNetwork.keySet() ){
            Town town = busNetwork.get(name);
            if  (!town.isVisited()){
            String listTown = "" ;
            Set<Town> reachable = new HashSet<Town>();
                 reachable.addAll(findAllConnected(town));
            for (Town towns : reachable){
                listTown += " " + towns.getName();
            }
            UI.println("Group " + groupNum + ":" + listTown);
            groupNum += 1;
        }
       }
        setUnvisited();
    }
    
    public void display(){
        if (!correctFile){
            UI.println("Please insert the file with longtitude and ladtidude!");
        }
        drawGraphs();
    }
    
    
    //get min X
    public double getMinX(){
        double min = 9999999;
        for (String name : busNetwork.keySet()){
            Town town = busNetwork.get(name);
            if ( town.getLong() < min ) {
                min = town.getLong();
            }
        }
        return min;
    }
    
   //get min Y
    public double getMinY(){
        double min = 9999999;
        for (String name : busNetwork.keySet()){
            Town town = busNetwork.get(name);
            if ( town.getLad() < min ) {
                min = town.getLad();
            }
        }
        return min;
    }

    //draw graphs
    public void drawGraphs(){
        double minY = getMinY();
        double minX = getMinX();
        if (current == null){
            Town town1 = busNetwork.get("Kaitaia");
            Town town2 = busNetwork.get("Invercargill");
            drawLines(town1,false);
            drawLines(town2,false);
        }
        for (String name : busNetwork.keySet()){
            Town town = busNetwork.get(name);
            double longt = town.getLong();
            double ladt = town.getLad();
            if (town.isSelected()){
               UI.setColor(Color.green);
            }else  UI.setColor(Color.red);
            UI.fillRect((longt-minX)*scaleY+30+initialX-3.5,(ladt-minY)*scaleX+30-3.5,7,7+initialY);
            UI.setColor(Color.black);
            UI.drawString(name,(longt-minX)*scaleY+10+initialX-3.5,(ladt-minY)*scaleX+30+initialY-3.5);
        }    
    }
    
    public void drawLines(Town town, boolean check){
            Set<Town> newSet = new HashSet<Town>(findAllConnected(town));
            town.setVisited(true);
            for (Town towns : newSet){
                Set<Town> neighs = new HashSet<Town>(towns.getNeighbours());
                for(Town neigh : neighs){
                    drawLine(towns,neigh,check);
                }
            }
            setUnvisited();
    }
    
    //draw connections between towns
    public void drawLine(Town town, Town neigh, boolean check){
         double minY = getMinY();
         double minX = getMinX();
         double longt = town.getLong();
         double ladt = town.getLad();
         double longt1 = neigh.getLong();
         double ladt1 = neigh.getLad();
         if (check == true)UI.setColor(Color.yellow);
         else UI.setColor(Color.gray);
         UI.setLineWidth(2);
         UI.drawLine((longt-minX)*scaleY+30+initialX,(ladt-minY)*scaleX+30+initialY,(longt1-minX)*scaleY+30+initialX,(ladt1-minY)*scaleX+30+initialY);
    }
    
    
    //mouse listener
    public void doMouse(String action, double x, double y){   
           if (action.equals("pressed")){   
            UI.clearText();
            setUnselected();
            lastX = x+3.5;
            lastY = y+3.5;
            findPosition(x+3.5,y+3.5);
            if  ( current != null ){
            UI.println("Current selected: " + current + ".");
            printReachable1(current);
           }     
           reset();
        }
    }
    
    //reset
    private void reset(){
        UI.clearGraphics();
        Town town1 = busNetwork.get("Kaitaia");
        Town town2 = busNetwork.get("Invercargill");
        drawLines(town1,false);
        drawLines(town2,false);
        if(current!= null){ 
            drawLines(current,true);
        }
        drawGraphs();
    }
    
    private void findPosition(double x, double y){ 
        double minY = getMinY();
        double minX = getMinX();
        for ( String n : busNetwork.keySet()) {
            Town m = busNetwork.get(n);
            double longt = m.getLong();
            double ladt = m.getLad();
            if ( m.on(x+initialX,y+initialY,((longt-minX)*scaleY+30),(ladt-minY)*scaleX+30)){
                m.setSelected(true);
                current = m;
                return;
            } 
        }
        current = null;
    }  
    
    public void printReachable1(Town town){
            Set<Town> reachable = new HashSet<Town>(findAllConnected(town));
            reachable.remove(town);
            UI.println("\nFrom "+town.getName()+" you can get to:");
            for (Town towns : reachable){
                UI.println(towns);
            }
            setUnvisited();
    }

    /**
     * Set up the GUI (buttons and mouse)
     */
    public void setupGUI() {
        UI.setMouseMotionListener( this::doMouse );
        UI.addButton("Load", ()->{loadNetwork(UIFileChooser.open());});
        UI.addButton("Print Network", this::printNetwork);
        UI.addTextField("Reachable from", this::printReachable);
        UI.addButton("All Connected Groups", this::printConnectedGroups);
        UI.addButton("Display", this::display);
        UI.addButton("Clear", UI::clearText);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1100, 500);
        UI.setDivider(1.0);
        loadNetwork("data-small.txt");
    }

    // Main
    public static void main(String[] arguments) {
        BusNetworks bnw = new BusNetworks();
        bnw.setupGUI();
    }

}