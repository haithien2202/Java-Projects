// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3, Assignment 3
 * Name: Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

import ecs100.*;
import java.awt.Color;
import javax.swing.JButton;

/**
 *  Simple 'Minesweeper' program.
 *  There is a grid of squares, some of which contain a mine.
 *  The user can click on a square to either expose it or to
 *  mark/unmark it.
 *  
 *  If the user exposes a square with a mine, they lose.
 *  Otherwise, it is uncovered, and shows a number which represents the
 *  number of mines in the eight squares surrounding that one.
 *  If there are no mines adjacent to it, then all the unexposed squares
 *  immediately adjacent to it are exposed (and and so on)
 *
 *  If the user marks a square, then they cannot expose the square,
 *  (unless they unmark it first)
 *  When all the squares without mines are exposed, the user has won.
 */
public class MineSweeper {

    public static final int ROWS = 15;
    public static final int COLS = 15;

    public static final double LEFT = 10; 
    public static final double TOP = 10;
    public static final double SQUARE_SIZE = 20;

    // Fields
    private boolean marking;
    private boolean hasLost = false;

    private Square[][] squares;

    private JButton mrkButton;
    private JButton expButton;
    private boolean won = false;
    
    Color defaultColor;

    /** 
     * Construct a new MineSweeper object
     * and set up the GUI
     */
    public static void main(String[] arguments){
        MineSweeper ms = new MineSweeper();
        ms.setupGUI();
        ms.setMarking(false);
        ms.makeGrid();
    }

    /** Set up the GUI: buttons and mouse to play the game */
    public void setupGUI(){
        UI.setMouseListener(this::doMouse);
        UI.addButton("New Game", this::makeGrid);
        this.expButton = UI.addButton("Expose", ()->setMarking(false));
        this.mrkButton = UI.addButton("Mark", ()->setMarking(true));

        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.0);
    }

    /** Respond to mouse events */
    public void doMouse(String action, double x, double y) {
        if (hasLost == true) return;
        if (action.equals("released")){
            int row = (int)((y-TOP)/SQUARE_SIZE);
            int col = (int)((x-LEFT)/SQUARE_SIZE);
            if (row>=0 && row < ROWS && col >= 0 && col < COLS){
                if (marking) { mark(row, col);}
                else         { tryExpose(row, col); }
            }
        }
    }

    /**
     * Remember whether it is "Mark" or "Expose"
     * Change the colour of the "Mark", "Expose" buttons
     */
    public void setMarking(boolean v){
        marking=v;
        if (marking) {
            mrkButton.setBackground(Color.red);
            expButton.setBackground(null);
        }
        else {
            expButton.setBackground(Color.red);
            mrkButton.setBackground(null);
        }
    }

    // Other Methods

    /** 
     * The player has clicked on a square to expose it
     * - if it is already exposed or marked, do nothing.
     * - if it's a mine: lose (call drawLose()) 
     * - otherwise expose it (call exposeSquareAt)
     * then check to see if the player has won and call drawWon() if they have.
     * (This method is not recursive)
     */
    public void tryExpose(int row, int col){
        if( squares[row][col].isExposed() == false &&  squares[row][col].hasMine() == false ){
            if (won == true) return;
            exposeSquareAt(row,col);
            redraw();
        }
        if ( squares[row][col].hasMine()){
            drawLose();
            hasLost = true;
        }
        if (hasWon()){
            won = true;
            drawWin();
        }
    }

    /** 
     *  Expose a square, and spread to its neighbours if safe to do so.
     *  It is guaranteed that this square is safe to expose (ie, does not have a mine).
     *  If it is already exposed, we are done.
     *  Otherwise expose it, and redraw it.
     *  If the number of adjacent mines of this square is 0, then
     *     expose all its neighbours (which are safe to expose)
     *     (and if they have no adjacent mine, expose their neighbours, and ....)
     */
    public void exposeSquareAt(int row, int col){  
        if (row ==-1 || row == ROWS || col == -1 || col== COLS  || squares[row][col].isExposed() == true) return;
        squares[row][col].setExposed();
        if (squares[row][col].getAdjacentMines() == 0){
         for (int p=-1;p<=1;p++) {
            for (int q=-1;q<=1;q++) {
                int Row = row+p;
                int Col = col +q;
                if ( Row > -1 && Row < ROWS && Col > -1 && Col < COLS && squares[Row][Col].isExposed() == false){ 
                    if (squares[Row][Col].hasMine() == false )exposeSquareAt(Row, Col);
                }
            }
        }
        }   
    }
    
    public void redraw(){
        UI.clearGraphics();
        for (int row=0; row < ROWS; row++){
            double y = TOP+row*SQUARE_SIZE;
            for (int col=0; col<COLS; col++){
                double x =LEFT+col*SQUARE_SIZE;
                squares[row][col].draw(x, y, SQUARE_SIZE);
            }
        }
    }

    /**
     * Mark/unmark the square.
     * If the square is exposed, don't do anything,
     * If it is marked, unmark it.
     * otherwise mark it and redraw.
     * (Marking cannot make the player win or lose)
     */
    public void mark(int row, int col){
         if (won == false){
             squares[row][col].toggleMark();
             redraw();
         }
    }

    /** 
     * Returns true if the player has won:
     * If all the squares without a mine have been exposed, then the player has won.
     */
    public boolean hasWon(){
        if (hasLost == true) return false;
           for (int row=0; row < ROWS; row++){
            for (int col=0; col<COLS; col++){
                if ((squares[row][col].isExposed() == false && squares[row][col].hasMine() == false)) return false;
            }
        }
        return true;
    }

    /**
     * Construct a grid with random mines.
     */
    public void makeGrid(){
        UI.clearGraphics();
        won = false;
        hasLost = false;
        this.squares = new Square[ROWS][COLS];
        for (int row=0; row < ROWS; row++){
            double y = TOP+row*SQUARE_SIZE;
            for (int col=0; col<COLS; col++){
                double x =LEFT+col*SQUARE_SIZE;
                boolean isMine = Math.random()<0.1;     // approx 1 in 10 squares is a mine 
                this.squares[row][col] = new Square(isMine);
                this.squares[row][col].draw(x, y, SQUARE_SIZE);
            }
        }
        // now compute the number of adjacent mines for each square
        for (int row=0; row<ROWS; row++){
            for (int col=0; col<COLS; col++){
                int count = 0;
                //look at each square in the neighbourhood.
                for (int r=Math.max(row-1,0); r<Math.min(row+2, ROWS); r++){
                    for (int c=Math.max(col-1,0); c<Math.min(col+2, COLS); c++){
                        if (squares[r][c].hasMine())
                            count++;
                    }
                }
                if (this.squares[row][col].hasMine())
                    count--;  // we weren't suppose to count this square, just the adjacent ones.

                this.squares[row][col].setAdjacentMines(count);
            }
        }
    }

    /** Draw a message telling the player they have won */
    public void drawWin(){
        UI.setFontSize(28);
        UI.drawString("You Win!", LEFT + COLS*SQUARE_SIZE + 20, TOP + ROWS*SQUARE_SIZE/2);
        UI.setFontSize(12);
    }

    /**
     * Draw a message telling the player they have lost
     * and expose all the squares and redraw them
     */
    public void drawLose(){
        for (int row=0; row<ROWS; row++){
            for (int col=0; col<COLS; col++){
                squares[row][col].setExposed();
                squares[row][col].draw(LEFT+col*SQUARE_SIZE, TOP+row*SQUARE_SIZE, SQUARE_SIZE);
            }
        }
        UI.setFontSize(28);
        UI.drawString("You Lose!", LEFT + COLS*SQUARE_SIZE+20, TOP + ROWS*SQUARE_SIZE/2);
        UI.setFontSize(12);
        hasLost = true;
    }

}
