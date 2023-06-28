// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3
 * Name:    Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

/**
 * Implements a decision tree that asks a user yes/no questions to determine a decision.
 * Eg, asks about properties of an animal to determine the type of animal.
 * 
 * A decision tree is a tree in which all the internal nodes have a question, 
 * The answer to the question determines which way the program will
 *  proceed down the tree.  
 * All the leaf nodes have the decision (the kind of animal in the example tree).
 *
 * The decision tree may be a predermined decision tree, or it can be a "growing"
 * decision tree, where the user can add questions and decisions to the tree whenever
 * the tree gives a wrong answer.
 *
 * In the growing version, when the program guesses wrong, it asks the player
 * for another question that would help it in the future, and adds it (with the
 * correct answers) to the decision tree. 
 *
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.awt.Color;

public class DecisionTree {

    public DTNode theTree;    // root of the decision tree;

    /**
     * Setup the GUI and make a sample tree
     */
    public static void main(String[] args){
        DecisionTree dt = new DecisionTree();
        dt.setupGUI();
        dt.loadTree("sample-animal-tree.txt");
    }

    /**
     * Set up the interface
     */
    public void setupGUI(){
        UI.addButton("Load Tree", ()->{loadTree(UIFileChooser.open("File with a Decision Tree"));});
        UI.addButton("Print Tree", this::printTree);
        UI.addButton("Run Tree", this::runTree);
        UI.addButton("Grow Tree", this::growTree);
        UI.addButton("Save Tree", this::saveTree);  // for completion
        UI.addButton("Draw Tree", this::drawTree);  // for challenge
        UI.addButton("Reset", ()->{loadTree("sample-animal-tree.txt");});
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.5);
    }

    /**  
     * Print out the contents of the decision tree in the text pane.
     * The root node should be at the top, followed by its "yes" subtree,
     * and then its "no" subtree.
     * Needs a recursive "helper method" which is passed a node.
     * 
     * COMPLETION:
     * Each node should be indented by how deep it is in the tree.
     * The recursive "helper method" is passed a node and an indentation string.
     *  (The indentation string will be a string of space characters)
     */
    public void printTree(){
        UI.clearText();
        UI.println(theTree.getText()+"?");
        if (theTree.getYes() != null) printRecursive(theTree.getYes(), "y","    ");
        if (theTree.getNo() != null) printRecursive(theTree.getNo(), "n","    ");
    }
    
    public void printRecursive(DTNode current, String yn, String format){
        if (!current.isAnswer()) UI.println(format+yn + ": " + current.getText()+"?");
        else UI.println(format+yn + ": " + current.getText());
        if (current.getYes() != null) printRecursive(current.getYes(), "y", format+"    ");
        if (current.getNo() != null) printRecursive(current.getNo(), "n", format+"    ");
    }

    /**
     * Run the tree by starting at the top (of theTree), and working
     * down the tree until it gets to a leaf node (a node with no children)
     * If the node is a leaf it prints the answer in the node
     * If the node is not a leaf node, then it asks the question in the node,
     * and depending on the answer, goes to the "yes" child or the "no" child.
     */
    public void runTree() {
        UI.clearText();
        String answ = UI.askString(theTree.getText()+"?(yes/no): ");
        if (theTree.getYes() != null && answ.equalsIgnoreCase("yes")) runRecursive(theTree.getYes());
        if (theTree.getNo() != null && answ.equalsIgnoreCase("no")) runRecursive(theTree.getNo());

    }
    public void runRecursive(DTNode current){
        String answ = "";
        while (true){
            if (!current.isAnswer()) answ = UI.askString(current.getText()+"?(yes/no): ");
            else {
                UI.println("The answer is: " + current.getText());
                break;
            }
            if (current.getYes() != null && answ.equalsIgnoreCase("yes")) {
                runRecursive(current.getYes());
                break;
            }
            else if (current.getNo() != null && answ.equalsIgnoreCase("no")){
                runRecursive(current.getNo());
                break;
            }
        }
    }

    /**
     * Grow the tree by allowing the user to extend the tree.
     * Like runTree, it starts at the top (of theTree), and works its way down the tree
     *  until it finally gets to a leaf node. 
     * If the current node has a question, then it asks the question in the node,
     * and depending on the answer, goes to the "yes" child or the "no" child.
     * If the current node is a leaf it prints the decision, and asks if it is right.
     * If it was wrong, it
     *  - asks the user what the decision should have been,
     *  - asks for a question to distinguish the right decision from the wrong one
     *  - changes the text in the node to be the question
     *  - adds two new children (leaf nodes) to the node with the two decisions.
     */
    public void growTree () {
        UI.clearText();
        String answ = UI.askString("Is it true: " + theTree.getText()+" (yes/no): ");
        if (theTree.getYes() != null && answ.equalsIgnoreCase("yes")) growRecursive(theTree.getYes());
        if (theTree.getNo() != null && answ.equalsIgnoreCase("no")) growRecursive(theTree.getNo());

    }
    public void growRecursive(DTNode current){
        String answ = "";
        while (true){
            if (!current.isAnswer()) answ = UI.askString("Is it true: " + current.getText()+" (yes/no): ");
            else {
                String answer = UI.askString("I think I know. Is it a " + current.getText() + "?");
                if (answer == "yes") {
                    break;
                }else {
                    String addNew = UI.askString(" OK, what should the answer be?");
                    String oldText = current.getText();
                    UI.println("Oh. I can't distinguish a " + oldText + " from a " + addNew);
                    UI.println("Tell me something that's true for a " + addNew + " from a " + oldText + "?");
                    String prob = UI.askString("Property: ");
                    UI.println("Thank you! I've updated my decision tree.");
                    current.setText(prob);
                    current.setChildren(new DTNode(addNew,null,null),new DTNode(oldText,null,null));
                    break;
                }
            }
            if (current.getYes() != null && answ.equalsIgnoreCase("yes")) {
                growRecursive(current.getYes());
                break;
            }
            else if (current.getNo() != null && answ.equalsIgnoreCase("no")){
                runRecursive(current.getNo());
                break;
            }
        }
    }
    
    public void drawTree () {
        drawRecursive(theTree, 50, 400, 150);
        
    }
    public void drawRecursive (DTNode current, double x, double y, double ydiff){
        if (current.getYes() != null) {
            drawRecursive (current.getYes(), x+100, y-ydiff, ydiff/2);
            UI.drawLine(x , y - 15/2, x+100, y-ydiff + 15/2);
        }
        if (current.getNo() != null) {
            drawRecursive (current.getNo(), x+100, y+ydiff, ydiff/2);
            UI.drawLine(x, y + 14/2, x+100, y+ydiff - 14/2);
        }
        current.draw(x, y);
    }
    
    public void saveTree () {
        String filename = UIFileChooser.open("Save Decision Tree to File"); 
        try{
            File file = new File(filename);
            if (!Files.exists(Path.of(filename))){
                file.createNewFile();
            }
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writeTree (filename, theTree);
        }
        catch(IOException e){UI.println("File reading failed: " + e);}
    }
    
    public void writeTree (String filename, DTNode current) {
        try{
            if (!current.isAnswer()){
                String add = "Question: " + current.getText() + "\n";
                Files.write(Paths.get(filename), add.getBytes(), StandardOpenOption.APPEND);
            }
            else {
                String add = "Answer: " + current.getText() + "\n";
                Files.write(Paths.get(filename), add.getBytes(), StandardOpenOption.APPEND);
            }
        }
        catch(IOException e){UI.println("File reading failed: " + e);}
        if (current.getYes() != null) writeTree(filename, current.getYes());
        if (current.getNo() != null) writeTree(filename, current.getNo());
    }
    
    

    // You will need to define methods for the Completion and Challenge parts.

    // Written for you

    /** 
     * Loads a decision tree from a file.
     * Each line starts with either "Question:" or "Answer:" and is followed by the text
     * Calls a recursive method to load the tree and return the root node,
     *  and assigns this node to theTree.
     */
    public void loadTree (String filename) { 
        if (!Files.exists(Path.of(filename))){
            UI.println("No such file: "+filename);
            return;
        }
        try{theTree = loadSubTree(new ArrayDeque<String>(Files.readAllLines(Path.of(filename))));}
        catch(IOException e){UI.println("File reading failed: " + e);}
    }

    /**
     * Loads a tree (or subtree) from a Scanner and returns the root.
     * The first line has the text for the root node of the tree (or subtree)
     * It should make the node, and 
     *   if the first line starts with "Question:", it loads two subtrees (yes, and no)
     *    from the scanner and add them as the  children of the node,
     * Finally, it should return the  node.
     */
    public DTNode loadSubTree(Queue<String> lines){
        Scanner line = new Scanner(lines.poll());
        String type = line.next();
        String text = line.nextLine().trim();
        DTNode node = new DTNode(text);
        if (type.equals("Question:")){
            DTNode yesCh = loadSubTree(lines);
            DTNode noCh = loadSubTree(lines);
            node.setChildren(yesCh, noCh);
        }
        return node;

    }



}
