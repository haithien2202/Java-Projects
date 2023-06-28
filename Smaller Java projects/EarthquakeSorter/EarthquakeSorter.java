// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3
 * Name:    Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.lang.Math;

/**
 * EarthquakeSorter
 * Sorts data about a collection of 4335 NZ earthquakes from May 2016 to May 2017
 * Each line of the file "earthquake-data.txt" has a description of one earthquake:
 *   ID time longitude latitude magnitude depth region
 * Data is from http://quakesearch.geonet.org.nz/
 *  Note the earthquakes' ID have been modified to suit this assignment.
 *  Note bigearthquake-data.txt has just the 421 earthquakes of magnitude 4.0 and above
 *   which may be useful for testing, since it is not as big as the full file.
 * 
 * Core:  three methods:
 *   loadData
 *      Loads the data from a file into a field containing an
 *      ArrayList of Earthquake objects.
 *      Hint : to make an Earthquake object, read all the lines from the file
 *              and then take each line apart into the values to pass to the
 *              Earthquake constructor
 *   sortByID
 *      Sorts the list of earthquakes using their natural ordering (based on their ID number).
 *   sortByTime
 *      Sorts the list of earthquakes according to the date and time that they occurred.
 *   
 * Completion: two methods:
 *   sortByRegion
 *      Sorts the list of earthquakes according to region. If two earthquakes have the same
 *      region, they should be sorted by magnitude and then depth.
 *   sortByProximity
 *      Sorts the list of earthquakes according to their proximity (distance) to a given location.
 *   
 * Challenge: one method
 *   sortByProximityChallenge
 *      Sorts the list of earthquakes according to their distance to a given location first,
 *      but sorts the earthquakes with a 1 km radius of each other according to magnitude and depth.
 */

public class EarthquakeSorter{

    private List<Earthquake> earthquakes = new ArrayList<Earthquake>();
    /*
     * Load data from the data file into the earthquakes field:
     * Clear the earthquakes field.
     * Read lines from file
     * For each line, use Scanner to break up each line and make an Earthquake
     *  adding it to the earthquakes field.
     */
    public void loadData(String filename){
        try {
            String FileName = UIFileChooser.open("Choose file");
            File newFile = new File(FileName);
            Scanner myReader = new Scanner(newFile);
            while (myReader.hasNext()){
                String ID = myReader.next();
                String date = myReader.next();
                String time = myReader.next();
                double longt = myReader.nextDouble();
                double  ladt = myReader.nextDouble();
                double  magn = myReader.nextDouble();
                double  depth = myReader.nextDouble(); 
                String  region = myReader.next();
                Earthquake eq = new Earthquake(ID,date,time,longt,ladt,magn,depth,region); 
                earthquakes.add(eq);
            }
            UI.printf("Loaded %d earthquakes into list\n", this.earthquakes.size());
            UI.println("----------------------------");
        } catch(IOException e){UI.println("File reading failed");}
    }

    /**
     * Sorts the earthquakes by ID
     * The file "output-ID.txt" lists the output for the "bigearthquake-data.txt"
     */
    public void sortByID(){
        UI.clearText();
        UI.println("Earthquakes sorted by ID");
        Collections.sort(earthquakes);

        for (Earthquake e : this.earthquakes){
            UI.println(e.toString());
        }
        UI.println("------------------------");
    }

    /**
     * Sorts the list of earthquakes according to the date and time that they occurred.
     * The file "output-Time.txt" lists the output for the "bigearthquake-data.txt"
     */
    public void sortByTime(){
        UI.clearText();
        UI.println("Earthquakes sorted by time");
        Collections.sort(earthquakes, ( Earthquake a, Earthquake b) ->
        {   if(a.getYear() < b.getYear())return -1;
            if(a.getYear() > b.getYear())return 1;
            if(a.getMonth() < b.getMonth())return -1;
            if(a.getMonth() > b.getMonth()) return 1;
            if(a.getDay() < b.getDay())return -1;
            if(a.getDay() > b.getDay()) return 1;  
            for (int i = 0 ; i < a.getTime().length() ; i++){
            if (a.getTime().charAt(i) == b.getTime().charAt(i)) continue;
            if (a.getTime().charAt(i) > b.getTime().charAt(i)) return 1;
            if (a.getTime().charAt(i) < b.getTime().charAt(i)) return -1;
            if (a.getTime().charAt(i) == b.getTime().charAt(i) && i == a.getTime().length()) return 0; 
        }   
            return 0;
        });

        for (Earthquake e : this.earthquakes){
            UI.println(e.toString());
        }
        UI.println("------------------------");
    }

    /**
     * Sorts the list of earthquakes according to region. If two earthquakes have the same
     *   region, they should be sorted by magnitude (highest first) and then depth (more shallow first)
     * The file "output-Region.txt" lists the output for the "bigearthquake-data.txt"
     */
    public void sortByRegion(){
        UI.clearText();
        UI.println("Earthquakes sorted by region, then by magnitude and depth");
        Collections.sort(earthquakes, ( Earthquake a, Earthquake b) ->{
             for (int i = 0 ; i < a.getRegion().length() ; i++){
            if (a.getRegion().charAt(i) == b.getRegion().charAt(i)) continue;
            if (a.getRegion().charAt(i) > b.getRegion().charAt(i)) return 1;
            if (a.getRegion().charAt(i) < b.getRegion().charAt(i)) return -1;
            if (a.getRegion().charAt(i) == b.getRegion().charAt(i) && i == a.getRegion().length()) return 0; 
        }   
        return 0;
        });

        for (Earthquake e : this.earthquakes){
            UI.println(e.toString());
        }
        UI.println("------------------------");
    }

    /**
     * Sorts the list of earthquakes according to their proximity (distance) to a given location.
     *   Auckland: -36.8485, 174.7633
     *   Wellington: -41.2865, 174.7762
     *   Christchurch: -43.5321, 172.6362
     *   Queenstown: -45.0312, 168.6626
     *   Dunedin -45.8788, 170.5028
     *   Invercargill: -46.4132, 168.3538
     * The file "output-Proximity-Completion-ChCh.txt" lists the output for the 
     *     "bigearthquake-data.txt" for Christchurch
     * The latitude and longitude of Christchurch are stored in chch.txt
     */
    public void sortByProximity(double latitude, double longitude){
        UI.clearText();
        UI.println("Earthquakes sorted by proximity");
        UI.println("Latitude: " + latitude + " Longitude: " + longitude);
        Collections.sort(earthquakes, ( Earthquake a, Earthquake b) ->{
        if (a.distanceTo(latitude,longitude) == b.distanceTo(latitude,longitude)) return 0;
        if (a.distanceTo(latitude,longitude) > b.distanceTo(latitude,longitude)) return 1;
        if (a.distanceTo(latitude,longitude) < b.distanceTo(latitude,longitude)) return -1;
         return 0;
        });
        UI.println("------------------------");
        for (Earthquake e : this.earthquakes){
            UI.println(e.toString()+ " " + String.format("%.2f",e.distanceTo(latitude,longitude))+" km");
        }
    }

    /**
     * Sorts the list of earthquakes according to their distance to a given location first,
     * but sorts the earthquakes with a 1 km radius of each other according to magnitude
     * (highest first) and depth (more shaloow first).
     *   Auckland: -36.8485, 174.7633
     *   Wellington: -41.2865, 174.7762
     *   Christchurch: -43.5321, 172.6362
     *   Queenstown: -45.0312, 168.6626
     *   Dunedin -45.8788, 170.5028
     *   Invercargill: -46.4132, 168.3538
     *
     * The file "output-Proximity-Challenge-ChCh.txt" lists the output for the 
     *     "bigearthquake-data.txt" for Christchurch
     * The latitude and longitude of Christchurch are stored in chch.txt
     */
    public void sortByProximityChallenge(double latitude, double longitude){
        UI.clearText();
        UI.println("Earthquakes sorted according to their distance to a given location first,\n but earthquakes with a 1 km radius of each other are sorted according to magnitude and depth.");
        UI.println("Latitude: " + latitude + " Longitude: " + longitude);
        Collections.sort(earthquakes, ( Earthquake a, Earthquake b) ->{
            double first = Math.round(a.distanceTo(latitude,longitude));
            double second = Math.round(b.distanceTo(latitude,longitude));
            if (first > second) return 1;
            if (first < second) return -1;
        if (a.getMagnitude() < b.getMagnitude()) return 1;
        if (a.getMagnitude() > b.getMagnitude()) return -1;
        if (a.getDepth() < b.getDepth()) return -1;
        if (a.getDepth() > b.getDepth()) return 1;
        if (a.distanceTo(latitude, longitude) < b.distanceTo(latitude,longitude)) return 1;
        if (a.distanceTo(latitude, longitude) > b.distanceTo(latitude,longitude)) return -1;     
         return 0;
        });
        UI.println("------------------------");
        for (Earthquake e : this.earthquakes){
            UI.println(e.toString()+ " " + String.format("%.2f",e.distanceTo(latitude,longitude))+" km ("+String.format("%.0f",e.distanceTo(latitude,longitude))+")");
        }
    }

    /**
     * Add the buttons
     */
    public void setupGUI(){
        UI.initialise();
        UI.addButton("Load", this::loadData);
        UI.addButton("sort by ID",  this::sortByID);
        UI.addButton("sort by Time",  this::sortByTime);
        UI.addButton("sort by Region", this::sortByRegion);
        UI.addButton("sort by Proximity", this::sortByProximity);
        UI.addButton("sort by Proximity CHALLENGE", this::sortByProximityChallenge);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(900,400);
        UI.setDivider(1.0);  //text pane only 
    }

    public static void main(String[] arguments){
        EarthquakeSorter obj = new EarthquakeSorter();
        obj.setupGUI();
    }   

    //================================================
    // DO NOT CHANGE ANYTHING BELOW THIS LINE!
    // THESE METHODS ARE REQUIRED FOR MARKING
    //================================================
    public void loadData(){
        this.loadData(UIFileChooser.open("Choose data file"));
    }

    public void sortByProximity(){
        this.sortByProximity(UI.askDouble("Give latitude: "), UI.askDouble("Give longitude: "));
    }

    public void sortByProximityChallenge(){
        this.sortByProximityChallenge(UI.askDouble("Give latitude: "), UI.askDouble("Give longitude: "));
    }
    
    public List<Earthquake> getEarthquakes(String filename){
        this.earthquakes = new ArrayList<Earthquake>();
        this.loadData(filename);
        return this.earthquakes;
    }
    public List<Earthquake> checkSortByID(List<Earthquake> eq){
        this.earthquakes = eq;
        this.sortByID();
        return this.earthquakes;
    }
    public List<Earthquake> checkSortByTime(List<Earthquake> eq){
        this.earthquakes = eq;
        this.sortByTime();
        return this.earthquakes;
    }
    public List<Earthquake> checkSortByRegion(List<Earthquake> eq){
        this.earthquakes = eq;
        this.sortByRegion();
        return this.earthquakes;
    }
    public List<Earthquake> checkSortByProximity(List<Earthquake> eq, double lat, double longi){
        this.earthquakes = eq;
        this.sortByProximity(lat,longi);
        return this.earthquakes;
    }
    public List<Earthquake> checkSortByProximityChallenge(List<Earthquake> eq, double lat, double longi){
        this.earthquakes = eq;
        this.sortByProximityChallenge(lat, longi);
        return this.earthquakes;
    }
}
