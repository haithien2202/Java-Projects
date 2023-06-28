    // This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3, Assignment 4
 * Name: Hai Thien Tran 
 * Username: tranhai1
 * ID: 300503987
 */

/**
 * Implements a binary tree that represents the Morse code symbols, named after its inventor
 *   Samuel Morse.
 * Each Morse code symbol is formed by a sequence of dots and dashes.
 *
 * To increase the efficiency of encoding, Morse code was designed so that the length of each symbol
 * is approximately inverse to the frequency of the character in English text.
 * Thus the most common letter in English, the letter "E", has the shortest code: a single dot.
 *
 * The International Morse Code encodes the 26 English letters A through Z, some non-English letters,
 * the Arabic numerals and a small set of punctuation and procedural signals (prosigns).
 *
 * A Morse code chart has been provided with this assignment. This chart only contains the 26 letters
 * and 10 numerals. These are given in alphanumerical order. 
 * Some nodes will not be matched with any symbols (text).
 *
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.awt.Color;

public class MorseCode {

    public Character root;    // root of the morse code binary tree;
    private String mCode;

    /**
     * Setup the GUI and creates the morse code with characters up to 2 symbols
     */
    public static void main(String[] args){
        MorseCode mc = new MorseCode();
        mc.setupGUI();
        mc.makeBasicTree();
    }

    /**
     * Set up the interface
     */
    public void setupGUI(){
        UI.addButton("Decode", this::decode);
        UI.addButton("Extend Tree Core", this::extendTreeCore);
        UI.addButton("Print Tree", this::printTree);
        UI.addButton("Extend Tree Comp", this::extendTreeComp);
        UI.addButton("Load Alphabet", this::loadAlphabet);
        UI.addButton("Draw Graph", this::printGraph);
        UI.addButton("Encode", this::encode);
        UI.addButton("Reset", this::makeBasicTree);
        UI.addButton("Quit", UI::quit);
        UI.setMouseMotionListener(this::doMouse);
        UI.setWindowSize(1500,450);
        UI.setDivider(0.25);
    }

    /**
     * Makes an initial tree with the 6 characters that have at most 2 symbols.
     */
    public void makeBasicTree(){
        root = new Character(null,
            new Character("T",                             
                new Character("M", null, null),
                new Character("N", null, null)),
            new Character("E",                             
                new Character("A", null, null),
                new Character("I", null, null)));
    }

    /**
     * Run the tree by starting at the top (root), and working
     * down the tree following the dash or dot nodes according to the
     * given sequence.
     * FOR CORE: you may assume that the code entered exists in the tree
     * FOR COMPLETION: your program needs to be more robust
     */
    public void decode() {
        UI.clearText();
        String code;
        do {
            code = UI.askString("Morse code:");
        }
        while ( ! isValidCode(code));
        String signal = "";
        StringBuffer result = new StringBuffer("");
        Character current = root;

        for (int i = 0; i < code.length(); i++) {
            signal = code.substring(i, i + 1);
                if (signal.equals("-")) {
                    if (current.getDash() != null) {
                        current = current.getDash();
                    }  else if (current.getDash() == null && i+1 < code.length()){
                        current.setDash(new Character(null));
                        current = current.getDash();
                    } else if (current.getDash() == null && i+1 == code.length()) {
                        String newCode = askForChar();
                        current.setDash(new Character(newCode));
                        current = current.getDash();
                    }
                } else if (signal.equals(".")) {
                    if (current.getDot() != null) {
                        current = current.getDot();
                    } else if (current.getDot() == null && i+1 < code.length()){
                        current.setDot(new Character(null));
                        current = current.getDot();      
                    } else if (current.getDot() == null && i+1 == code.length()) {
                        String newCode = askForChar();
                        current.setDot(new Character(newCode));
                        current = current.getDot();
                } 
            }else {
                    result = result.append(current.getText());
                    current = root;
                }
        }
        result = result.append(current.getText());
        UI.println(result);
    }
    
    //Asking for letter to add if it is not exist
    public String askForChar() {
        String letter = UI.askString("Invalid code, enter a letter to add:");
        return letter;
    }
    
    // is the code valid or not
    public boolean isValidCode (String code) {
        if (code.length() <= 0){
            UI.println("Must be at least 1 character long");
            return false;
        }
        for (int index=0; index<code.length(); index++){
            char c = code.charAt(index);
            if (c != '-' && c != '.'){
                UI.println(c + "is invalid; Must only contain . (dot) or - (dash)");
                return false;
            }
        }
        return true;
    }

    // You will need to define at least headers for the other methods to make the GUI work.
    public void extendTreeCore(){
        UI.clearText();
        String code;
        String letter;
        do {
            letter = UI.askString("Letter code:");
            code = UI.askString("Morse code:");
        }
        while ( ! isValidCode(code));
        add1(letter,code);
    }
        
    // add letter
      private void add(String letter, String code) {
        Character current = root;
        for (int i = 0; i < code.length(); i++) {
            String signal = code.substring(i, i + 1);
            boolean checkDash = false;
            boolean checkDot = false;
            if (current.getDash() != null) {
                if(current.getDash().getText()== null)checkDash = true;
            }
            if (current.getDot() != null) {
                 if(current.getDot().getText() == null)checkDot = true;
            }
                if (signal.equals("-")) {
                    if ((current.getDash() != null && i+1 < code.length()) || (checkDash == true && i+1 < code.length())) {
                        current = current.getDash();
                    }  else if (current.getDash() == null && i+1 < code.length()){
                        current.setDash(new Character(null));
                        current = current.getDash();
                    } else if (current.getDash() == null && i+1 == code.length()) {
                        current.setDash(new Character(letter));
                        current = current.getDash();
                    } else if (checkDash == true && i+1 == code.length()) {
                        Character child = current.getDash();
                        child.setText(letter);
                        current = current.getDash();
                    }
                } else if (signal.equals(".")) {
                    if ((current.getDot() != null  && i+1 < code.length()) || (checkDot == true && i+1 < code.length())) {
                        current = current.getDot();
                    } else if (current.getDot() == null && i+1 < code.length()){
                        current.setDot(new Character(null));
                        current = current.getDot();
                    } else if (current.getDot() == null && i+1 == code.length()) {
                        current.setDot(new Character(letter));
                        current = current.getDot();
                    } else if (checkDot == true && i+1 == code.length()) {
                        Character child = current.getDot();
                        child.setText(letter);
                        current = current.getDot();
                    }
            }
        }
    }
    
    //add letter core
    private void add1(String letter, String code) {
        Character current = root;
        for (int i = 0; i < code.length(); i++) {
            String signal = code.substring(i, i + 1);
                if (signal.equals(".")) {
                    if (current.getDash() != null) {
                        current = current.getDash();
                    } else 
                     UI.println("No valid code");
                } else if (signal.equals("-")) {
                    if (current.getDot() != null) {
                        current = current.getDot();
                    } else
                    UI.println("No valid code");
        }
    }
   }
   
    //printing tree
    public void printTree(){
        UI.clearText();
        Character current = root; 
        String x = "";
        String y = "";
        drawTree(current,x,y);
    }
    
    public void drawTree(Character current, String x,String y){      
        if(current == null) return;
        if( current.getDash() != null ) {
            drawLeft(current,x,y);
        } 
        if( current.getDot() != null ) {
            drawRight(current,x,y);
        } 
    }
    
    public void drawLeft(Character current, String x, String y){
            Character left = current.getDash();
            y = y+"-";
            UI.println(x+y+": "+left.getText());   
            drawTree(left,x+" ",y); 
    }
    
    public void drawRight(Character current, String x,  String y){
            Character right = current.getDot();
            y = y+".";
            UI.println(x+y+": "+right.getText());       
            drawTree(right,x+" ",y); 
    }

    public void extendTreeComp(){
        String code;
        String letter;
        do {
            letter = UI.askString("Letter code:");
            code = UI.askString("Morse code:");
        }
        while ( ! isValidCode(code));
        add(letter,code);
    }
    
    public void loadAlphabet(){
        Character current = root;
        try {
            File planner = new File("morse-code.txt");
            Scanner myReader = new Scanner(planner);
            while (myReader.hasNextLine()) {
            String letter = myReader.next();
            String mCode = myReader.next();
            add(letter,mCode);
            myReader.nextLine();
          }   
        }catch(IOException e){UI.println("File reading failed");}         
    }
    
    //draw graph
    public void printGraph(){
        calculateSpaceNeeded();
        UI.eraseRect(0,0,1026,40*maxLevel-2);
        Character current = root;
        calculateSpaceNeeded();
        double iniX = calculateSpaceNeeded() - (sliderX-400)*(calculateSpaceNeeded()*2-1025)/100;
        double iniY = 30;
        double iniDiff = calculateSpaceNeeded();
        if (iniDiff > 400){
            drawSlider();
        }
        drawLine(current,iniX ,iniY,iniDiff);
        drawGraph(current,iniX ,iniY,iniDiff);           
    }
    
    boolean isDragged = false;
    int sliderX = 400;
    double X;
    public void doMouse(String action, double x, double y){
        if (action.equals("pressed")){
            if (x > sliderX && x < sliderX + 100 && y > 40*maxLevel && y < 40*maxLevel + 20){
               isDragged = true; 
               X = sliderX - x; 
            }
        }
        else if (action.equals("dragged")){
            if (isDragged == true && sliderX >= 400 && sliderX <= 500){
                UI.eraseRect(400,40*maxLevel,220,20);
                UI.drawRect(400,40*maxLevel,200,20);
                UI.setColor(Color.LIGHT_GRAY);
                sliderX = (int) (x+X);
                if (sliderX < 400) sliderX = 400;
                if (sliderX > 500) sliderX = 500;
                UI.fillRect(sliderX,40*maxLevel,100,20);
                UI.setColor(Color.BLACK);
                if (Math.abs(x - X) > 2)printGraph();
            }
        }
        else if (action.equals("released")){
                isDragged = false;             
        }
    }
    
    private void drawSlider(){
        UI.drawRect(400,40*maxLevel,200,20);
        UI.setColor(Color.LIGHT_GRAY);
        UI.fillRect(sliderX,40*maxLevel,100,20);
        UI.setColor(Color.BLACK);
    }
    int maxLevel = -1;
    private double calculateSpaceNeeded(){
        double n = 12.5;
        maxLevel = -1;
        Character current = root;
        find(current, 0);
        for (int i = 0; i < maxLevel; i++){
            n=n*2;
        }
        return n;
    }
    
    private void find(Character current, int level)
    {
        if (current != null)
        {
            if (current.getDash() != null)find(current.getDash(), ++level);
 
            // Update level and rescue
            if (level > maxLevel)
            {
                maxLevel = level;
            }
            if (current.getDash() != null && current.getDot() != null )find(current.getDot(), level);
            else if(current.getDash() == null && current.getDot() != null)find(current.getDot(), ++level);
        }
    }
    
    public void drawLine(Character current,double x, double y,double diff){ 
        diff = diff/2;
        if (current.getDash() != null)drawLineL(current.getDash(),x,y,diff);     
        if (current.getDot() != null)drawLineR(current.getDot(),x,y,diff);
    }
    
    //draw line
    public void drawLineL(Character current,double x, double y, double diff){
        double x1 = x;
        double y1 = y;
        x=x-diff;
        y=y+30;
        diff = diff/2;
        UI.drawLine(x1,y1,x,y);  
        if (current.getDash() != null)drawLineL(current.getDash(),x,y,diff); 
        if (current.getDot() != null)drawLineR(current.getDot(),x,y,diff);
    }
    
    public void drawLineR(Character current,double x, double y, double diff){
        double x1 = x;
        double y1 = y;
        x=x+diff;
        y = y+30;
        diff = diff/2;
        UI.drawLine(x1,y1,x,y);      
        if (current.getDash() != null)drawLineL(current.getDash(),x,y,diff);
        if (current.getDot() != null)drawLineR(current.getDot(),x,y,diff);
    }
    
    public void drawGraph(Character current,double x, double y,double diff){ 
        current.draw(x,y);
        diff = diff/2;
        if (current.getDash() != null)drawLeft(current.getDash(),x,y,diff);     
        if (current.getDot() != null)drawRight(current.getDot(),x,y,diff);
    }
    
    public void drawLeft(Character current,double x, double y, double diff){
        double x1 = x;
        double y1 = y;
        x=x-diff;
        y=y+30;
        diff = diff/2;
        current.draw(x,y);   
        if (current.getDash() != null)drawLeft(current.getDash(),x,y,diff); 
        if (current.getDot() != null)drawRight(current.getDot(),x,y,diff);
    }
    
    public void drawRight(Character current,double x, double y, double diff){
        double x1 = x;
        double y1 = y;
        x=x+diff;
        y = y+30;
        diff = diff/2;
        current.draw(x,y);     
        if (current.getDash() != null)drawLeft(current.getDash(),x,y,diff);
        if (current.getDot() != null)drawRight(current.getDot(),x,y,diff);
    }
    
    public void encode(){
        UI.clearText();
        String letter = UI.askString("Letter:");
        Character current = root;
        mCode = null;
        String code = "";
        letter = letter.toUpperCase();  
        findLetter(current , letter , code);
        if (mCode != null)
        UI.println("Morse code for letter "+ letter +" is   " + mCode+"   !");
        else UI.println("No letter found!");
    }
    
    public void findLetter(Character current , String letter , String code){
        if (current.getText() != null ){
            String letterChar = current.getText();
            if (letterChar.equals(letter) == true) {
                mCode = code;
            }
        }
        if (current.getDash() != null)searchLeft(current.getDash(),letter,code);
        if (current.getDot() != null)searchRight(current.getDot(),letter,code);
    }
    
    public void searchLeft(Character current, String letter, String code){
         code = code + "-";
         findLetter(current ,letter,code);
    }
    
    public void searchRight(Character current, String letter, String code){
         code = code + ".";
         findLetter(current ,letter,code);
    }
}
