// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2022T2, Assignment 2
 * Name:    Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */
 

import ecs100.*;
import java.util.*;
import java.util.Map.Entry;
import java.io.*;
import java.nio.file.*;

/**
 * WellingtonTrains
 * A program to answer queries about Wellington train lines and timetables for
 *  the train services on those train lines.
 *
 * See the assignment page for a description of the program and what you have to do.
 */

public class WellingtonTrains{
    //Fields to store the collections of Stations and Lines
    Map<String,Station> stations = new HashMap<String, Station>();
    Map<String,TrainLine> trainline = new HashMap<String, TrainLine>();
    List<Station> stationsList = new ArrayList<Station>();

    // Fields for the suggested GUI.
    private String stationName;        // station to get info about, or to start journey from
    private String lineName;           // train line to get info about.
    private String destinationName;
    private int startTime = 0;         // time for enquiring about

    private static boolean loadedData = false;  // used to ensure that the program is called from main.

    /**
     * main method:  load the data and set up the user interface
     */
    public static void main(String[] args){
        WellingtonTrains wel = new WellingtonTrains();
        wel.loadData();   // load all the data
        wel.setupGUI();   // set up the interface
    }

    /**
     * Load data files
     */
    public void loadData(){
        loadStationData();
        UI.println("Loaded Stations");
        loadTrainLineData();
        UI.println("Loaded Train Lines");
        // The following is only needed for the Completion and Challenge
        loadTrainServicesData();
        UI.println("Loaded Train Services");
        loadedData = true;
    }

    /**
     * User interface has buttons for the queries and text fields to enter stations and train line
     * You will need to implement the methods here.
     */
    public void setupGUI(){
        UI.addButton("All Stations",        this::listAllStations);
        UI.addButton("Stations by name",    this::listStationsByName);
        UI.addButton("All Lines",           this::listAllTrainLines);
        UI.addTextField("Station",          (String name) -> {this.stationName=name;});
        UI.addTextField("Train Line",       (String name) -> {this.lineName=name;});
        UI.addTextField("Destination",      (String name) -> {this.destinationName=name;});
        UI.addTextField("Time (24hr)",      (String time) ->
            {try{this.startTime=Integer.parseInt(time);}catch(Exception e){UI.println("Enter four digits");}});
        UI.addButton("Lines of Station",    () -> {listLinesOfStation(this.stationName);});
        UI.addButton("Stations on Line",    () -> {listStationsOnLine(this.lineName);});
        UI.addButton("Stations connected?", () -> {checkConnected(this.stationName, this.destinationName);});
        UI.addButton("Next Services",       () -> {findNextServices(this.stationName, this.startTime);});
        UI.addButton("Find Trip",           () -> {findTrip(this.stationName, this.destinationName, this.startTime);});

        UI.addButton("Quit", UI::quit);
        UI.setMouseListener(this::doMouse);

        UI.setWindowSize(900, 400);
        UI.setDivider(0.2);
        // this is just to remind you to start the program using main!
        if (! loadedData){
            UI.setFontSize(36);
            UI.drawString("Start the program from main", 2, 36);
            UI.drawString("in order to load the data", 2, 80);
            UI.sleep(2000);
            UI.quit();
        }
        else {
            UI.drawImage("data/geographic-map.png", 0, 0);
            UI.drawString("Click to list closest stations", 2, 12);
        }
    }

    public void doMouse(String action, double x, double y){
        if (action.equals("released")){
            UI.clearText();
            Collections.sort(stationsList, ( Station a, Station b) ->{
                    double distance = Math.sqrt(Math.pow((a.getXCoord() - x),2) + Math.pow((a.getYCoord() - y),2));
                    double distanceb = Math.sqrt(Math.pow((b.getXCoord() - x),2) + Math.pow((b.getYCoord() - y),2));
                    if (distance > distanceb) return 1;
                    if (distance < distanceb) return -1;
                    return 0;
                });
            int l = 0;
            for (Station n : stationsList){
                if (l < 7){
                    double distance = Math.sqrt(Math.pow((n.getXCoord() - x),2) + Math.pow((n.getYCoord() - y),2));   
                    UI.println(n.getName() +"  " + String.format("%.2f",distance)+" km."); 
                    l += 1;
                }
            }
        }
    }

    // Methods for loading data and answering queries

    public void loadStationData(){
        try {
            File station = new File("data/stations.data");
            Scanner myReader = new Scanner(station);
            while (myReader.hasNextLine()) {
                String stationName = myReader.next();
                stations.put(stationName, new Station(stationName,myReader.nextInt(),myReader.nextInt(),myReader.nextInt()));
                myReader.nextLine();
            } 
            for (String key : stations.keySet()) {
                Station m = stations.get(key);    
                stationsList.add(m);
            }
        } catch(IOException e){UI.println("File reading failed");}    
    }
    
    public void loadTrainLineData(){
        try {
            File line = new File("data/train-lines.data");
            Scanner myReader = new Scanner(line);
            while (myReader.hasNextLine()) {
                String linename = myReader.nextLine();
                trainline.put(linename,new TrainLine(linename));
            }
            for (String key : trainline.keySet()){
                String filename = "data/"+key+"-stations.data";
                File linesStation = new File(filename);
                Scanner reader = new Scanner(linesStation);
                TrainLine m = trainline.get(key);
                while (reader.hasNextLine()) {
                    Station n = stations.get(reader.nextLine());
                    m.addStation(n);
                } 
            }  
        } catch(IOException e){UI.println("File reading failed");} 
        for (String key : trainline.keySet()){
            TrainLine m = trainline.get(key);
            List<Station> n = m.getStations();
            for ( Station b : n){
                b.addTrainLine(m);
            }
        }
    }
    
    // check if the name of the station is valid
    public boolean checkStation(String station){
        if (station == null) return false;
        for (String key : stations.keySet()){
            if (station.equals(key)){
                return true;
            }
        }
        return false;
    }
    
    // check if the name of the trainline is valid
    public boolean checkTrainLine(String trainlines){
        if (trainlines == null) return false;
        for (String key : trainline.keySet()){
            if (trainlines.equals(key)){
                return true;
            }
        }
        return false;
    }

    public void loadTrainServicesData(){
        try {
            for (String key : trainline.keySet()){
                String filename = "data/"+key+"-services.data";
                TrainLine m = trainline.get(key);  
                List<String> lines = Files.readAllLines(Path.of(filename));
                for (String line : lines){
                    TrainService newservice = new TrainService(m);
                    Scanner scan = new Scanner(line);
                    while (scan.hasNextInt()){
                        newservice.addTime(scan.nextInt());
                    }
                    m.addTrainService(newservice);
                }
            }
        } catch(IOException e){UI.println("File reading failed");} 
    }
    
    public void listAllStations(){
        UI.clearText();
        UI.println("All train stations in region");
        UI.println("-----------------------------");
        for (String key : stations.keySet()) {
            Station m = stations.get(key);
            UI.println(m.toString());
        }
    }
    
    public void listStationsByName(){
        java.util.Collections.sort(stationsList);
        UI.clearText();
        UI.println("All train stations in region");
        UI.println("-----------------------------");
        for (Station x : stationsList){
            UI.println(x.toString()); 
        }
    }
    
    public void listAllTrainLines(){
        UI.clearText();
        UI.println("All train lines in region");
        UI.println("-----------------------------");
        for (String key : trainline.keySet()){
            TrainLine m = trainline.get(key);
            UI.println(m.toString());
        }
    }

    public void listLinesOfStation(String name){
        UI.clearText();
        if (name == null) {
            UI.println("Enter station name.");
            return;
        }
        if (checkStation(name) == false){
            UI.println("No station found.");
            return;
        }
        Station m = stations.get(name);
        List<TrainLine> n = new ArrayList<TrainLine>(m.getTrainLines());
        UI.println("List of train lines on "+ name+" station.");
        UI.println("-----------------------------");
        for (int i = 0; i < n.size()/2 ; i++){
            UI.println(n.get(i));
        }
    }

    public void listStationsOnLine(String name){
        UI.clearText();
        if (name == null) {
            UI.println("Enter station name.");
            return;
        }
        if (checkTrainLine(name) == false){
            UI.println("No train line found.");
            return;
        }
        TrainLine m = trainline.get(name);
        List<Station> n = new ArrayList<Station>(m.getStations());
        UI.println("List of stations on "+ name+" line.");
        UI.println("-----------------------------");
        for (int i = 0; i < n.size() ; i++){
            UI.println(n.get(i));
        }
    } 

    //check if the 2 stations are on the same trainline
    public void checkConnected(String name, String destination){
        UI.clearText();
        if (name == null){
            UI.println("Enter departure station name.");
            return;
        }
        if (name == null){
            UI.println("Enter destination station name.");
            return;
        }
        if  (name == destination){
            UI.println("Please select 2 different stations.");
            return;
        }    
        if ( checkStation(name) == false){
            UI.println("No station found.");
            if ( checkStation(destination) == true){
                UI.println("No destination station found.");
            }
            return;
        }
        Station m = stations.get(name);
        Station n = stations.get(destination);
        List<TrainLine> common = new ArrayList<TrainLine>(m.getTrainLines());
        common.retainAll(n.getTrainLines());
        int check = 0;
        Collections.sort(common, ( TrainLine a, TrainLine b) ->
            { for (int i = 0 ; i < a.getName().length() ; i++){
                    if (a.getName().charAt(i) == b.getName().charAt(i)) continue;
                    if (a.getName().charAt(i) > b.getName().charAt(i)) return 1;
                    if (a.getName().charAt(i) < b.getName().charAt(i)) return -1;
                    if ((a.getName().charAt(i) == b.getName().charAt(i)) && i == a.getName().length()) return 0; 
                }
                return 0;
            });
        if (!common.isEmpty()){
            check = getIndex(m,common.get(common.size()-1)) -getIndex(n,common.get(common.size()-1));
        }
        if (!common.isEmpty()){
            if (check < 0 ){
                for ( int i=0; i < common.size()/2; i++){
                    UI.println("-----------------------------");
                    TrainLine current = common.get(i);    
                    int zone = m.getZone() - n.getZone()+1;
                    UI.println("The " + current.getName() + " go through " + m.getName() + " and " + n.getName()+".");
                    UI.println("The trip go through "+ Math.abs(zone) + " fare zones.");
                }
            }
            if (check > 0 ){
                for ( int i=common.size()/2 ; i < common.size(); i++){
                    UI.println("-----------------------------");
                    TrainLine current = common.get(i);    
                    int zone = m.getZone() - n.getZone()+1;
                    UI.println("The " + current.getName() + " go through " + m.getName() + " and " + n.getName()+".");
                    UI.println("The trip goes through "+ Math.abs(zone) + " fare zones.");
                }
            }
        } 
        else {
            UI.println("No train line found from "+m.getName() +" to " + n.getName()+".");
        }
    }  

    //find next service using given time
    public void findNextServices(String name,int time){
        UI.clearText();
        if (name == null){
            UI.println("Enter station name.");
            return;
        }
        if ( checkStation(name) == false){
            UI.println("No station found.");
            return;
        }
        Station m = stations.get(name);   
        List<TrainLine> listOfTrainLines = new ArrayList<TrainLine>(m.getTrainLines());
        for ( TrainLine trainlIne : listOfTrainLines){
            int indexOfStation = getIndex(m,trainlIne);
            List<TrainService> listOfService = new ArrayList<TrainService>(trainlIne.getTrainServices());
            String nameOfTrainLine = trainlIne.getName();
            UI.println(trainlIne);
            for (TrainService y : listOfService){
                List<Integer> listOfTime = new ArrayList<Integer>(y.getTimes());

                if (listOfTime.get(indexOfStation) > time){
                    UI.println("The next service on line " + nameOfTrainLine +" at station " + name + " is "+listOfTime.get(indexOfStation));
                    UI.println("-----------------------------");   
                    break;                      
                }
            }
        }
    }

    // a function that I created to help getting the time for the trip finder
    public List<Integer> findNextServices1(String name,String destination,TrainLine v ,int time){
        UI.clearText();
        Station m = stations.get(name);
        Station n = stations.get(destination);
        int indexOfStation = getIndex(m,v);
        int indexOfDestination = getIndex(n,v);

        List<TrainService> listOfService = new ArrayList<TrainService>(v.getTrainServices());
        for (TrainService y : listOfService){
            List<Integer> listOfTime = new ArrayList<Integer>(y.getTimes());
            if (listOfTime.get(indexOfStation) >= time && listOfTime.get(indexOfDestination) != -1){  
                List<Integer> times = new ArrayList<Integer>();
                times.add(listOfTime.get(indexOfStation)) ;
                times.add(listOfTime.get(indexOfDestination)) ;
                return times;
            }
        }
        return null;
    }

    //get index of the station in the trainline to determine the direction
    public int getIndex(Station station, TrainLine trainline){
        List<Station> stationList = new ArrayList<Station>(trainline.getStations());
        return stationList.indexOf(station);
    }

    //findtrip function, if on the same line call findTripOnSameLine(), if not on the same line call findTripOnDifLine()
    public void findTrip(String name, String destination, int time){
        UI.clearText();
        if (name == null){
            UI.println("Enter departure station name.");
            return;
        }
        if (name == null){
            UI.println("Enter destination station name.");
            return;
        }
        if ( checkStation(name) == false){
            UI.println("No station found");
            if ( checkStation(destination) == false){
                UI.println("No destination station found");
                return;
            }
        }
        Station m = stations.get(name);
        Station n = stations.get(destination);
        if  (m == n){
            UI.println("Please select 2 different stations.");
            return;
        }
        List<TrainLine> common = new ArrayList<TrainLine>(m.getTrainLines());
        common.retainAll(n.getTrainLines());
        if (!common.isEmpty()){  
            findTripOnSameLine(m, n, common,time);
        }
        else{
            findTripOnDiffLine(m,n,time);
        }
    }

    //if 2 stations are on the same line, do this
    public void findTripOnSameLine(Station station, Station destination, List<TrainLine> common, int time){
        Collections.sort(common, ( TrainLine aTrainLine, TrainLine bTrainLine) ->
            { for (int i = 0 ; i < aTrainLine.getName().length() ; i++){
                    if (aTrainLine.getName().charAt(i) == bTrainLine.getName().charAt(i)) continue;
                    if (aTrainLine.getName().charAt(i) > bTrainLine.getName().charAt(i)) return 1;
                    if (aTrainLine.getName().charAt(i) < bTrainLine.getName().charAt(i)) return -1;
                    if ((aTrainLine.getName().charAt(i) == bTrainLine.getName().charAt(i)) && i == aTrainLine.getName().length()) return 0; 
                }
                return 0;
            });
        int indexCheck = getIndex(station,common.get(0)) - getIndex(destination,common.get(common.size()-1));
        if ( indexCheck < 0){
            for (int i = 0 ; i < common.size()/2 ; i++){
                TrainLine current = common.get(i);
                int zone = station.getZone() - destination.getZone()+1;
                List<Integer> Times = findNextServices1(station.getName(),destination.getName(),current, time);
                UI.println("The next service (" + current.getName() + ") go from " + station.getName()+" ("+ Times.get(0) + ") to " + destination.getName()+" ("+Times.get(1)+").");
                UI.println("The trip goes through "+ Math.abs(zone) + " fare zones.");
            }
        }
        if ( indexCheck > 0){
            for (int i = common.size()/2 ; i < common.size() ; i++){
                TrainLine current = common.get(i);
                int zone = station.getZone() - destination.getZone()+1;
                List<Integer> Times = findNextServices1(station.getName(),destination.getName(),current, time);
                UI.println("The next service (" + current.getName() + ") go from " + station.getName()+" ("+ Times.get(0) + ") to " + destination.getName()+" ("+Times.get(1)+").");
                UI.println("The trip goes through "+ Math.abs(zone) + " fare zones.");
            }
        }
    }

    //if 2 stations are not on the same line, do this
    public void findTripOnDiffLine(Station station, Station destination,int time){   
        UI.println("Best trip:");
        UI.println("-----------------------------");
        List<TrainLine> stationLineList = new ArrayList<TrainLine>(station.getTrainLines());
        List<TrainLine> destinationLineList = new ArrayList<TrainLine>(destination.getTrainLines());
        List<Station> commonStation = findCommonStation(stationLineList,destinationLineList);
        for (Station x : commonStation){ 
            List<TrainLine> common = new ArrayList<TrainLine>(x.getTrainLines());
            Collections.sort(common, ( TrainLine a, TrainLine b) ->
                { for (int i = 0 ; i < a.getName().length() ; i++){
                        if (a.getName().charAt(i) == b.getName().charAt(i)) continue;
                        if (a.getName().charAt(i) > b.getName().charAt(i)) return 1;
                        if (a.getName().charAt(i) < b.getName().charAt(i)) return -1;
                        if ((a.getName().charAt(i) == b.getName().charAt(i)) && i == a.getName().length()) return 0; 
                    }
                    return 0;
                });
            common.retainAll(station.getTrainLines());
            int size = common.size();
            List<Integer> indexStation = new ArrayList<Integer>();
            int checkIndex = getIndex(station, common.get(size/2)) - getIndex(x, common.get(size/2));
            TrainLine current = null;
            int zone = 0;
            int zone1 = 0;
            List<Integer> Times = new ArrayList<Integer>();
            int min = 999999;
            int secondMin = 99999999; 
            if (checkIndex < 0){
                for (int i = size/2 ; i < size; i++){
                    current = common.get(i);
                    zone = station.getZone() - x.getZone()+1;
                    Times.addAll(findNextServices1(station.getName(),x.getName(),current, time));
                    for ( int a : Times)
                        UI.println(a);
                }
            }
            else if (checkIndex > 0){
                for (int i = 0 ; i < size/2; i++){
                    current = common.get(i);
                    zone = station.getZone() - x.getZone()+1;
                    Times.addAll(findNextServices1(station.getName(),x.getName(),current, time));

                    for (int m = 0; m < Times.size() ; m++) {
                        if (Times.get(m)< min) {
                            secondMin = min;
                            min = Times.get(m); 
                        } else if (Times.get(m)< secondMin) {
                            secondMin = Times.get(m); 
                        }
                    }
                }
            }

            List<TrainLine> commonDestination = new ArrayList<TrainLine>(x.getTrainLines());
            commonDestination.retainAll(destination.getTrainLines());
            Collections.sort(commonDestination, ( TrainLine a, TrainLine b) ->
                { for (int i = 0 ; i < a.getName().length() ; i++){
                        if (a.getName().charAt(i) == b.getName().charAt(i)) continue;
                        if (a.getName().charAt(i) > b.getName().charAt(i)) return 1;
                        if (a.getName().charAt(i) < b.getName().charAt(i)) return -1;
                        if ((a.getName().charAt(i) == b.getName().charAt(i)) && i == a.getName().length()) return 0; 
                    }
                    return 0;
                });
            int indexCheckDestination = getIndex(x,commonDestination.get(0)) - getIndex(destination,commonDestination.get(0));
            List<Integer> TimesDestination = new ArrayList<Integer>();
            int destinationTime = secondMin;
            TrainLine currentDest = null;
            if ( indexCheckDestination < 0 ){
                for (int i = 0 ; i < commonDestination.size()/2 ; i++){
                    currentDest = commonDestination.get(i);
                    zone1 = x.getZone() - destination.getZone()+1;
                    TimesDestination.addAll(findNextServices1( x.getName(), destination.getName(), currentDest, destinationTime));
                }
            }
            else if (indexCheckDestination > 0) {
                for (int i = commonDestination.size()/2 ; i < commonDestination.size(); i++){
                    currentDest = commonDestination.get(i);
                    zone1 = x.getZone() - destination.getZone()+1;
                    TimesDestination.addAll(findNextServices1( x.getName(), destination.getName(),currentDest, destinationTime));
                }
            }   
            UI.println("Best trip:");
            UI.println("-----------------------------");
            UI.println("The next service (" + current.getName() + ") goes from " + station.getName()+" ("+ min + ") to " + x.getName()+" ("+ secondMin +").");
            UI.println("The trip goes through "+ Math.abs(zone) + " fare zones.");
            UI.println("Change to train line "+ currentDest.getName()+ " at " +x.getName()+ ".");
            UI.println("The next service (" + currentDest.getName() + ") goes from " + x.getName()+" ("+ TimesDestination.get(0) + ") to " + destination.getName()+" ("+TimesDestination.get(1)+").");
            UI.println("The trip goes through "+ Math.abs(zone1) + " fare zones.");
        }
    }

    // find the common stations for 2 different trainlines
    public List<Station> findCommonStation(List<TrainLine> station, List<TrainLine> destination){
        List<Station> stationList = new ArrayList<Station>();
        List<Station> destinationStationList = new ArrayList<Station>();
        for ( int i = 0; i < station.size()/2 ; i++ ){
            stationList.addAll(station.get(i).getStations());
        }
        for ( int i = 0; i < destination.size()/2 ; i++ ){
            destinationStationList.addAll(destination.get(i).getStations());
        }
        destinationStationList.retainAll(stationList);
        return  destinationStationList;
    }
}
