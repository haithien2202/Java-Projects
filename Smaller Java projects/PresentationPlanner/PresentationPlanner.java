// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3, Assignment 1
 * Name: Hai Thien Tran 
 * Username: tranhai1   
 * ID: 300503987
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.awt.MouseInfo;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.awt.Point;
import java.awt.event.MouseAdapter;


/**
 * PresentationPlanner allows a user to create and view (as a slide presentation) a presentation consisting of images with descriptive text.
 *
 * This class contains the main method of the program, the GUI setup, and
 * all the methods that respond to buttons, mouse, and keys.
 * 
 * @author pondy and monique
 */
public class PresentationPlanner {

    public static final double SMALL_HEIGHT = 221;  // size of images when editing list and for the library
    public static final double SMALL_WIDTH = 40.8;  // size of images when editing list and for the library
    public static final double GAP = 5;             // gap between images when editing
    public static final double TOP_LIBRARY = 500;
    
    // Fields to store the ImageInfo objects in the presentation and the library 
    // n is a variable to store name of the pannels temporarily
    //decs is a variable to store description of the pannels temporarily
    private String n;
    private String desc;
    // largeSize is variable to control the size of presentation
    private double largeSize=1;
    // currentImage is variable for current selected image in thumbList, newImage is the new location it is going to be
    private int currentImage, newImage= -1;    // index of currently selected image in thumbnails 
    private double initialX;
    
    private int candidate;        // index of currently selected image in library of candidates 
    // saveFile is for the name of the text file which the programe is saving the thumblist to
    private String saveFile = "saveFile";
    // other variables to check the conditions
    private boolean presentationRunning, isloaded,randomOrder, isSelected, isSelected1= false;      // flag signalling whether the slidepresentation is running or not
    private int i ,order, noOfImage = 0; 
    //GAPf is the gap between coordinate x = 0 to the right side of the image
    private double GAPf = 0;
    //indicator shows the delay time in seconds
    private double indicator = 1;
    //speed to control the speed of presentation
    private long speed = 1;
    //arrays storing infomation of the lists
    List<String> P = new ArrayList<String>();
    List<String> description = new ArrayList<String>();
    List<String> thumbnails = new ArrayList<String>();
    List<String> thumbnailsDesc = new ArrayList<String>();
    
    /**
     * Load the library of images
     */
    public void loadLibrary() {
        if (isloaded == false){
        String filename = "list-of-images.txt";
        try {
            File planner = new File(filename);
            Scanner myReader = new Scanner(planner);
            UI.setColor(Color.BLACK);
            UI.drawString("Library", GAP , TOP_LIBRARY - 10);
            UI.drawString("Delay: " + String.format("%.2f", indicator)+"s",GAP,TOP_LIBRARY + SMALL_HEIGHT+20);
            while (myReader.hasNextLine()) {
                n = myReader.next();
                desc = myReader.nextLine();   
                P.add(n);
                description.add(desc);
                GAPf = GAP*(i+1) + SMALL_WIDTH*i;
                i += 1;
                displayLibrary();
          }
        } catch(IOException e){UI.println("File reading failed");}    }
        isloaded = true;
    }
    
    
    // this function is to load a list from a saved text file
    public void loadFromFile(){
        saveFile = UI.askString("Input save file name");
        String filename = saveFile ;
        try {
            File planner = new File(filename);
            Scanner myReader = new Scanner(planner);
            thumbnails.clear();
            while (myReader.hasNextLine()) {
                thumbnails.add(myReader.nextLine());
          }
        } catch(IOException e){UI.println("File reading failed");}  
        reset();
    }
    
    // save a list to a text file
    public void saveToFile(){
        saveFile = UI.askString("Input save file name");
        try {
        save(saveFile);
        } catch(IOException e){UI.println("File reading failed");}
    }
    
    //saving continue
    public void save(String fileName) throws FileNotFoundException {
    PrintWriter saveFile = new PrintWriter(new FileOutputStream(fileName));
    for (String n : thumbnails)
        saveFile.println(n);
    saveFile.close();
   }
    
    
    /**
     * Display the thumbnails in the presentation and the library
     * highlighting the selected images
     * Will be called after
     *  - each change to the list of images in the presentation or
     *  - after selecting a thumbnail 
     */
    public void display(){ 
        if (presentationRunning == true)return;
        if (isSelected1 == false) return;
        UI.setColor(Color.RED);
        UI.setLineWidth(3);
        UI.drawRect(GAP*currentImage+SMALL_WIDTH*currentImage-initialX,GAP-2,SMALL_WIDTH,SMALL_HEIGHT);
    }
    
    public void display1(){ 
        if (presentationRunning == true)return;
        if (isSelected == false) return;
        UI.setColor(Color.RED);
        UI.setLineWidth(3);
        UI.drawRect(GAP+GAP*(candidate)+SMALL_WIDTH*(candidate),TOP_LIBRARY-2,SMALL_WIDTH,SMALL_HEIGHT);
    }

    /**
     * Display the thumbnails of all the images in the presentation
     * across the top of the graphics pane.
     * Hint: use the displaySmall(...) method in ImageInfo
     */
    public void displayThumbnails(){
             for (String i : thumbnails) {       
                 ImageInfo obj = new ImageInfo(thumbnails.get(noOfImage),desc);
                 obj.displaySmall(GAP*noOfImage + SMALL_WIDTH * (noOfImage)-initialX,5,SMALL_WIDTH,SMALL_HEIGHT);
                 noOfImage += 1;      
                }         
        }
        
          // reset graphics 
    public void reset(){
        isloaded = false;
        UI.clearGraphics();
        GAPf= 0;i=0;
        noOfImage = 0;
        loadLibrary();
        display1();
        displayThumbnails();
        display();
    }
    
    //adding image before an selected image in thumblist
    //if thumblist is not selected, it adds to the front of the thumblist
    public void addBefore(){
       if (isSelected == true){
        if(isSelected1 == true){
        thumbnails.add(currentImage,P.get(candidate));
        thumbnailsDesc.add(currentImage,description.get(candidate));
        currentImage += 1;
       }
        if(isSelected1 == false){
        thumbnails.add(0,P.get(candidate));
        thumbnailsDesc.add(0,description.get(candidate));
       }
        reset();
       }
    }
    
    //adding image after an selected image in thumblist
    //if thumblist is not selected, it adds to the end of the thumblist
    public void addAfter(){
        if (isSelected == true){
        if (isSelected1 == true){
        thumbnails.add(currentImage+1,P.get(candidate));
        thumbnailsDesc.add(currentImage+1,description.get(candidate));
        }
        if(isSelected1 == false){
        thumbnails.add(P.get(candidate));
        thumbnailsDesc.add(description.get(candidate));
        }
        reset();
       }
    }
    
    /**
     * Display the library of images across the bottom of the graphics pane.
     * Hint: use the displaySmall(...) method in ImageInfo
     */
    public void displayLibrary() { 
        ImageInfo obj=new ImageInfo(n,desc);
        obj.displaySmall(GAPf,TOP_LIBRARY,SMALL_WIDTH,SMALL_HEIGHT);
    }

    // RUNNING

    /**
     * As long as the presentation isn't already running, and there are some
     * images to show, start the presentation running from the currently selected image.
     * The presentation should keep running indefinitely, as long as the
     * presentationRunning field is still true.
     * Cycles through the images, going back to the start when it gets to the end.
     * The currentImage field should always contain the index of the current image.
     * Hint: use the displayLarge() method in ImageInfo
     */
    public void runPresentation(){
        if (thumbnails.size() > 0){
        String shufflemode;
        UI.setColor(Color.BLACK);
        UI.clearGraphics();
        presentationRunning = true;
        while(presentationRunning == true) {       
                 ImageInfo obj = new ImageInfo(thumbnails.get(order),thumbnailsDesc.get(order));
                 Random rand = new Random();
                 UI.drawString("Delay: " + String.format("%.2f", indicator)+"s",GAP,TOP_LIBRARY + SMALL_HEIGHT+20);
                 if (randomOrder == true)
                 shufflemode = "On";
                 else
                 shufflemode = "Off";
                 UI.drawString("Shuffle: " +shufflemode,GAP+100,TOP_LIBRARY + SMALL_HEIGHT+20);
                 if (randomOrder == false){
                 order += 1;
                }
                 else if (randomOrder == true){
                     order =    rand.nextInt(thumbnails.size());
                    }
                 obj.displayLarge(largeSize); 
                 try        
                    {
                      Thread.sleep(speed*1000);
                     } 
                     catch(InterruptedException ex) 
                    {
                     Thread.currentThread().interrupt();
                    }
                 if (presentationRunning == true){
                 UI.clearGraphics();
                 }
                 if (order == thumbnails.size()) {
                     order = 0;
                 }
     }  
    }   
    }

    /**
     * Stop the presentation by changing presentationRunning to false.
     * Redisplay the thumbnails in the slide presentation and the library
     */
    public void editPresentation(){
        if (isloaded == false)return;
        presentationRunning = false;
        initialX=0;
        try        
                    {
                      Thread.sleep(100);
                     } 
                     catch(InterruptedException ex) 
                {
                     Thread.currentThread().interrupt();
                    }
        reset();
    }

    // Other Methods (you will need quite a lot of additional methods).
    public void drawDraging(double x, double y, int inx){
        double xloc = x;
        double yloc = y;
        int index = inx;
    }
    

    // Methods for the user interface: keys, mouse (for selecting), buttons
    /**
     * Simple Mouse actions to select thumbnails on the presentation or on the library
     */
    public void doMouse(String action, double x, double y){
        if (isloaded == false) return;
        if (presentationRunning) return;
        if (action.equals("pressed")){            
            if (onPresentation(y) && onPresentationX(x) ){
                currentImage = getIndexFromPosThumbnails(x); // should check if over the end!!!!
                newImage = getIndexFromPosThumbnails(x);
                isSelected1 = true;
                reset();   
            }
            else if (onLibrary(y) && getIndexFromPos(x) < 22){
                candidate = getIndexFromPos(x); // should check if over the end!!!!
                isSelected = true;
                reset();
            } 
        }
        if (action.equals("released")){
            if ( onPresentation(y) && onPresentationX(x)){
               newImage = getIndexFromPosThumbnails(x);
                if (newImage != currentImage){
                dragging();
                isSelected = true;
               }
                reset();
            }
            else if (onLibrary(y) && getIndexFromPos(x) < 22){
                candidate = getIndexFromPos(x); // should check if over the end!!!!
                isSelected = true;
                reset();
            } 
        }
    }

    /**
     * Is mouse over the presentation 
     */
    public boolean onPresentation(double y){
        return (y >= GAP && y<= GAP+SMALL_HEIGHT);
    }
    
    public boolean onPresentationX(double x){
        int  inx = getIndexFromPosThumbnails(x);
        return (inx >= 0 && inx < thumbnails.size());
    }

    /**
     * Is mouse is over the library
     */
    public boolean onLibrary(double y){
        return (y >= TOP_LIBRARY && y<= TOP_LIBRARY+SMALL_HEIGHT*2);
    }
    
    /**
     * Return the index in the list of the image that the mouse is on.
     * Note, it might return an invalid index!!!!
     */
    public int getIndexFromPos(double x){
        return (int) (x/(SMALL_WIDTH + GAP));
    }
    
    public int getIndexFromPosThumbnails(double x){
        return (int) ((x+initialX)/(SMALL_WIDTH + GAP));
    }
    
    public boolean getLast(){
        boolean last = false;
        if (thumbnails.size()*(SMALL_WIDTH + GAP)-initialX < 22* (SMALL_WIDTH + GAP)) last = true;
        return last;
    }
    
    // moving image in thumbnails
    public void goLeft(){
        if(currentImage > 0 && presentationRunning == false && isSelected1 == true){
            thumbnails.add(currentImage-1,thumbnails.get(currentImage));
            thumbnailsDesc.add(currentImage-1,thumbnailsDesc.get(currentImage));
            thumbnails.remove(currentImage+1);
            thumbnailsDesc.remove(currentImage+1);
            currentImage -= 1;
            reset();
         }
        if (presentationRunning == true){
            if (order > 0){
            order -= 1;
        }
        else if (order == 0) order = thumbnails.size()-1;
        }
    }
    
    public void goRight(){
        if(currentImage < thumbnails.size()-1 && presentationRunning == false && isSelected1 == true){
            thumbnails.add(currentImage+2,thumbnails.get(currentImage));
            thumbnailsDesc.add(currentImage+2,thumbnailsDesc.get(currentImage));
            thumbnails.remove(currentImage);
            thumbnailsDesc.remove(currentImage);
            currentImage += 1;
            reset();
       }
        if (presentationRunning == true){
            if (order < thumbnails.size()-1){
            order += 1;
        }
        else if (order == thumbnails.size()-1) order = 0;
        }
    }
    
    public void goStart(){
        if (presentationRunning == false && isSelected1 == true){
            thumbnails.add(0,thumbnails.get(currentImage));
            thumbnailsDesc.add(0,thumbnailsDesc.get(currentImage));
            thumbnails.remove(currentImage+1);
            thumbnailsDesc.remove(currentImage+1);
            currentImage = 0;
            reset();
        }
        if (presentationRunning == true){
            order = 0;
        }
    }
    
    public void goEnd(){
        if (presentationRunning == false && isSelected1 == true){
            thumbnails.add(thumbnails.get(currentImage));
            thumbnailsDesc.add(thumbnailsDesc.get(currentImage));
            thumbnails.remove(currentImage);
            thumbnailsDesc.remove(currentImage);
            currentImage = thumbnails.size()-1;
            reset();
        }
        if (presentationRunning == true){
            order = thumbnails.size();
        }
    }
    
    //using mouse to drag image in thumbList
    public void dragging(){
        if (presentationRunning == false && isSelected1 == true){
            if (newImage < currentImage){
            thumbnails.add(newImage,thumbnails.get(currentImage));
            thumbnailsDesc.add(newImage,thumbnailsDesc.get(currentImage));
            thumbnails.remove(currentImage+1);
            thumbnailsDesc.remove(currentImage+1);
            currentImage = newImage;
            reset();
           }
           if (newImage > currentImage){
            thumbnails.add(newImage+1,thumbnails.get(currentImage));
            thumbnailsDesc.add(newImage+1,thumbnailsDesc.get(currentImage));
            thumbnails.remove(currentImage);
            thumbnailsDesc.remove(currentImage);
            currentImage = newImage;
            reset();
           }
        }
        if (presentationRunning == true){
            order = thumbnails.size();
        }
    }
    
    //Removing functions
     public void remove(){
        if (presentationRunning == false && isSelected1 == true){
            thumbnails.remove(currentImage);
            thumbnailsDesc.remove(currentImage);
            currentImage = -1;
            isSelected1 = false;
            reset();
        }
    }
    
    public void removeAll(){
        if (presentationRunning == true) return;
        if (isloaded == false) return;
        thumbnails.clear();
        isSelected1 = false;
        initialX = 0;
        reset();
    }
    
    //key
    public void moveLeft(){
        if (presentationRunning == false){
              goLeft();
        }
    }
    
    public void moveRight(){
        if (presentationRunning == false){
              goRight();
        }
    }
    
    public void moveStart(){
        if (presentationRunning == false){
              goStart();
        }
    }
    
    public void moveEnd(){
        if (presentationRunning == false){
              goEnd();
        }
    }
    
    //reverse thumblist
     public void reverse(){
        if (isloaded == false) return;
        if (presentationRunning == false){
        Collections.reverse(thumbnails);
        reset();
    }
    }
    
    //shuffle thumblist
    public void shuffle(){
        if (isloaded == false) return;
        if (presentationRunning == true) {
            if (randomOrder == false){
            randomOrder = true;
        }
        else if (randomOrder == true){
            randomOrder = false;
        }
    }
        else if (presentationRunning == false){
        Collections.shuffle(thumbnails);
        reset();
    }
    }
    
    
    //controling speed of the presentation
    public void slide(double tspeed){
        if (isloaded == false)return;
        speed = (new Double(tspeed)).longValue();
        indicator = tspeed;
        UI.drawString("Delay: " + String.format("%.2f", indicator)+"s",GAP,TOP_LIBRARY + SMALL_HEIGHT+20);
        if (presentationRunning == false)reset();
    }
    
    // size of presentation
    public void slideSize(double size){
        largeSize = size/5;
        if (presentationRunning == false)reset();
    }
    
    /**
     * Interprets key presses.
     * works in both editing the list and in the slide presentation.
     */  
    public void doKey(String key) {
       if (key.equals("Left"))         goLeft();
       else if (key.equals("Right"))   goRight();
       else if (key.equals("Home"))    goStart();
       else if (key.equals("End"))     goEnd();
       else if (key.equals("Delete"))     remove();
       else if (key.equals("Up")) addAfter();
       else if (key.equals("Down")) remove();
    }

    //this 2 functions will enalbe user to scroll through a very large thumblist
    public void scrollLeft(){
        if(thumbnails.size() <23)return;
        if( !getLast()){
        initialX += 30;
        reset();
    }
    }
    
    public void scrollRight(){
        if(thumbnails.size() <23)return;
        if(initialX<0) return;
        initialX -= 30;
        reset();
    }
    
    /**
     * Initialises the UI window, and sets up the buttons. 
     */
    public void setupGUI() {
        UI.addButton("Load library",     this::loadLibrary);
        UI.addButton("Run presentation", this::runPresentation);
        UI.addButton("Edit presentation",this::editPresentation);
        UI.addButton("add before",       this::addBefore);
        UI.addButton("add after",        this::addAfter);
        UI.addButton("move left",        this:: moveLeft);
        UI.addButton("move right",       this:: moveRight);
        UI.addButton("move to start",    this:: moveStart);
        UI.addButton("move to end",      this:: moveEnd);
        UI.addButton("remove",           this::remove);
        UI.addButton("remove all",       this::removeAll);
        UI.addButton("reverse",          this::reverse);
        UI.addButton("shuffle",          this::shuffle);
        UI.addSlider("speed(second)", 1, 7, 1, this::slide);
        UI.addSlider("presentation size", 1,5,5, this::slideSize);
        //scroll through the thumblist
        UI.addButton("<",          this::scrollLeft);
        UI.addButton(">",          this::scrollRight);
        UI.addButton("save",          this::saveToFile);
        UI.addButton("load",          this::loadFromFile);
        UI.addButton("Quit",             UI::quit);
        UI.setKeyListener(this::doKey);
        UI.setMouseListener(this::doMouse);
        UI.setWindowSize(1200,800);
        UI.setDivider(0);
        UI.printMessage("Mouse must be over graphics pane to use the keys");    
    }
    
    public static void main(String[] args) {
        PresentationPlanner pp = new PresentationPlanner();
        pp.setupGUI();
    }

}
