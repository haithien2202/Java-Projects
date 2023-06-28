// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP102/112 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP102
 * Name: Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

import ecs100.*;
import java.util.*;
import java.nio.file.*;
import java.io.*;
import java.awt.Color;

/**
 * WeatherReporter
 * Analyses weather data from files of weather-station measurements.
 *
 * The weather data files consist of a set of measurements from weather stations around
 * New Zealand at a series of date/time stamps.
 * For each date/time, the file has:
 *  A line with the date and time (four integers for day, month, year, and time)
 *   eg "24 01 2021 1900"  for 24 Jan 2021 at 19:00 
 *  A line with the number of weather-stations for that date/time 
 *  Followed by a line of data for each weather station:
 *   - name: one token, eg "Cape-Reinga"
 *   - (x, y) coordinates on the map: two numbers, eg   186 38
 *   - four numbers for temperature, dew-point, suface-pressure, and sea-level-pressure
 * Some of the data files (eg hot-weather.txt, and cold-weather.txt) have data for just one date/time.
 * The weather-all.txt has data for lots of times. The date/times are all in order.
 * You should look at the files before trying to complete the methods below.
 *
 * Note, the data files were extracted from MetOffice weather data from 24-26 January 2021
 */

public class WeatherReporter{

    public static final double DIAM = 10;       // The diameter of the temperature circles.    
    public static final double LEFT_TEXT = 10;  // The left of the date text
    public static final double TOP_TEXT = 50;   // The top of the date text
    
    private double maxtemplat = 0;
    private double maxtemplon = 0;
    private double maxtemp = 0;
    
    private double mintemplat = 0;
    private double mintemplon = 0;
    private double mintemp = 999;
    
    private String filename = "";
    private boolean found = false;
    /**   COMPLETION
     * Plots the temperatures for one date/time from a file on a map of NZ
     * Asks for the name of the file and opens a Scanner
     * It is good design to call plotSnapshot, passing the Scanner as an argument.
     */
    public void plotTemperatures(){
        try {
            filename = UIFileChooser.open("Choose file");
            File newFile = new File(filename);
            Scanner myReader = new Scanner(newFile);
            plotSnapshot(myReader);
           
        } catch(IOException e){UI.println("File reading failed");}    
    }


    /**
     * COMPLETION:
     *  Plot the temperatures for the next snapshot in the file by drawing
     *   a filled coloured circle (size DIAM) at each weather-station location.
     *  The colour of the circle should indicate the temperature.
     *
     *  The method should
     *   - read the date/time and draw the date/time at the top-left of the map.
     *   - read the number of stations, then
     *   - for each station,
     *     - read the name, coordinates, and data, and
     *     - plot the temperature for that station. 
     *   (Hint: You will find the getTemperatureColor(...) method useful.)
     *
     *  CHALLENGE:
     *  Also finds the highest and lowest temperatures at that time, and
     *  plots them with a larger circle.
     *  (Hint: If more than one station has the highest (or coolest) temperature,
     *         you only need to draw a larger circle for one of them.
     */     
    public void plotSnapshot(Scanner sc){
        UI.drawImage("map-new-zealand.gif", 0, 0);
        int day = sc.nextInt();
        int month = sc.nextInt();
        int year = sc.nextInt();
        String time = String.valueOf(sc.nextInt());
        int lines = sc.nextInt();
        UI.setColor(Color.BLACK);
        if (time.length() == 3) UI.drawString(day+"/"+month+"/"+year + " at " + time.charAt(0) + ":" + time.charAt(1) + time.charAt(2) , LEFT_TEXT,TOP_TEXT);
        else UI.drawString(day+"/"+month+"/"+year + " at " + time.charAt(0) + time.charAt(1)+":" + time.charAt(2) + time.charAt(3) , LEFT_TEXT,TOP_TEXT);
        for (int i = 0; i < lines; i++){
                String name = sc.next();
                double lat = sc.nextDouble();
                double lon = sc.nextDouble();
                double temp = sc.nextDouble();
                double dew = sc.nextDouble();
                double sur_pressure = sc.nextDouble();
                double seal_pressure = sc.nextDouble();
                UI.setColor(getTemperatureColor(temp));
                UI.fillOval(lat-DIAM/2,lon-DIAM/2,DIAM,DIAM);
                if (temp > maxtemp){
                    maxtemp = temp;
                    maxtemplat = lat;
                    maxtemplon = lon;
                } 
                if (temp < mintemp){
                    mintemp = temp;
                    mintemplat = lat;
                    mintemplon = lon;
                }    
        }
        UI.setColor(getTemperatureColor(maxtemp));
        UI.fillOval(maxtemplat-DIAM,maxtemplon-DIAM,DIAM*2,DIAM*2);
        UI.setColor(getTemperatureColor(mintemp));
        UI.fillOval(mintemplat-DIAM,mintemplon-DIAM,DIAM*2,DIAM*2);
    }

    /**   CHALLENGE
     * Displays an animated view of the temperatures over all
     * the times in a weather data files, plotting the temperatures
     * for the first date/time, as in the completion, pausing for half a second,
     * then plotting the temperatures for the second date/time, and
     * repeating until all the data in the file has been shown.
     * 
     * (Hint, use the plotSnapshot(...) method that you used in the completion)
     */
    public void animateTemperatures(){
        try {
            filename = "weather-all.txt";
            File newFile = new File(filename);
            Scanner myReader = new Scanner(newFile);
            while (myReader.hasNext()){
                plotSnapshot(myReader);
                UI.sleep(1000);
            }
        } catch(IOException e){UI.println("File reading failed");}    
    }

    /**   CHALLENGE
     * Prints a table of all the weather data from a single station, one line for each day/time.
     * Asks for the name of the station.
     * Prints a header line
     * Then for each line of data for that station in the weather-all.txt file, it prints 
     * a line with the date/time, temperature, dew-point, surface-pressure, and  sealevel-pressure
     * If there are no entries for that station, it will print a message saying "Station not found".
     * Hint, the \t in a String is the tab character, which helps to make the table line up.
     */
    public void reportStation(){
        String stationName = UI.askString("Name of a station: ");
        UI.printf("Report for %s: \n", stationName);
        UI.println("Date       \tTime \ttemp \tdew \tkPa \t\tsea kPa");   
        if (filename == "") return;
        found = false;
        try {
            File newFile = new File(filename);
            Scanner sc = new Scanner(newFile);
            while (sc.hasNext()) {
                printText(sc, stationName);   
            }
            if (found == false){
                UI.println("Station not found");
            }
        } catch(IOException e){UI.println("File reading failed");}    
    }
    
    public void printText(Scanner sc, String name){
         int day = sc.nextInt();
            int month = sc.nextInt();
            int year = sc.nextInt();
            String time = String.valueOf(sc.nextInt());
            String formatTime = "";
            int lines = sc.nextInt();
            if (time.length() == 3){
               formatTime = time.charAt(0) + ":" + time.charAt(1) + "" + time.charAt(2); 
            }
            else {
                formatTime = time.charAt(0) + "" + time.charAt(1)+":" + time.charAt(2) +""+ time.charAt(3);
            }
            for (int i = 0; i < lines; i++){
                String n = sc.next();
                double lat = sc.nextDouble();
                double lon = sc.nextDouble();
                double temp = sc.nextDouble();
                double dew = sc.nextDouble();
                double sur_pressure = sc.nextDouble();
                double seal_pressure = sc.nextDouble();
                if (n.equals(name)) {
                    UI.println(day+"/"+month+"/"+year+ "   \t"+ formatTime+" \t"+ temp + " \t"+ dew +" \t"+ sur_pressure +" \t\t"+seal_pressure);
                    found = true;
                }     
        }
    }

    /** Returns a color representing that temperature
     *  The colors are increasingly blue below 15 degrees, and
     *  increasingly red above 15 degrees.
     */
    public Color getTemperatureColor(double temp){
        double max = 37, min = -5, mid = (max+min)/2;
        if (temp < min || temp > max){
            return Color.white;
        }
        else if (temp <= mid){ //blue range: hues from .7 to .5
            double tempFracOfRange = (temp-min)/(mid-min);
            double hue = 0.7 -  tempFracOfRange*(0.7-0.5); 
            return Color.getHSBColor((float)hue, 1.0F, 1.0F);
        }
        else { //red range: .15 to 0.0
            double tempFracOfRange = (temp-mid)/(max-mid);
            double hue = 0.15 -  tempFracOfRange*(0.15-0.0); 
            return Color.getHSBColor((float)hue, 1.0F, 1.0F);
        }
    }

    public void setupGUI(){
        UI.initialise();
        UI.addButton("Plot temperature", this::plotTemperatures);
        UI.addButton("Animate temperature", this::animateTemperatures);
        UI.addButton("Report",  this::reportStation);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(800,750);
        UI.setFontSize(18);
    }

    public static void main(String[] arguments){
        WeatherReporter obj = new WeatherReporter();
        obj.setupGUI();
    }    

}
