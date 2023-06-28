
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
import java.awt.Color;
import java.lang.Number;

/** 
 * Calculator for Cambridge-Polish Notation expressions
 * (see the description in the assignment page)
 * User can type in an expression (in CPN) and the program
 * will compute and print out the value of the expression.
 * The template provides the method to read an expression and turn it into a tree.
 * You have to write the method to evaluate an expression tree.
 *  and also check and report certain kinds of invalid expressions
 */

public class CPNCalculator{

    /**
     * Setup GUI then run the calculator
     */
public static void main(String[] args){
        CPNCalculator calc = new CPNCalculator();
        calc.setupGUI();
        calc.runCalculator();
    }

    /** Setup the gui */
    public void setupGUI(){
        UI.addButton("Clear", UI::clearText); 
        UI.addButton("Quit", UI::quit); 
        UI.setDivider(1.0);
    }

    /**
     * Run the calculator:
     * loop forever:  (a REPL - Read Eval Print Loop)
     *  - read an expression,
     *  - evaluate the expression,
     *  - print out the value
     * Invalid expressions could cause errors when reading or evaluating
     * The try-catch prevents these errors from crashing the program - 
     *  the error is caught, and a message printed, then the loop continues.
     */
    public void runCalculator(){
        UI.println("Enter expressions in pre-order format with spaces");
        UI.println("eg   ( * ( + 4 5 8 3 -10 ) 7 ( / 6 4 ) 18 )");
        while (true){
            UI.println();
           try {
                GTNode<ExpElem> expr = readExpr(); 
                double value = evaluate(expr);
                if (value != Double.NaN){
                List<String> items =  new ArrayList<String>();
                printExpr(expr, items, 0);
                String exprs = "";
                for (String x : items){
                    exprs = exprs + x;
                }
                UI.println (exprs);
            }
                UI.println(" -> " + value);
            }catch(Exception e){UI.println("Something went wrong! "+e);}
        }
    }
    
    
    //evaluate result
    /**
     * Evaluate an expression and return the value
     * Returns Double.NaN if the expression is invalid in some way.
     * If the node is a number
     *  => just return the value of the number
     * or it is a named constant
     *  => return the appropriate value
     * or it is an operator node with children
     *  => evaluate all the children and then apply the operator.
     */
    public double evaluate(GTNode<ExpElem> expr){
        if (expr==null){
            return Double.NaN;
        } 
        double result = 0;
         int number = expr.numberOfChildren();
         if (expr.getItem().toString().equals("+") ){ 
             if ( number < 1 ) {
                 UI.println("Incorrect expression for addition!");
                 return Double.NaN;
            }
            for ( int i = 0 ; i < number ; i++ ){
              GTNode children = expr.getChild(i);
              result = result + evaluate(children);
            }    
            return result;
        }else if ( expr.getItem().toString().equals("-")){
              if ( number != 2 ) {
                 UI.println("Incorrect expression for subtraction!");
                 return Double.NaN;
            }
              for ( int i = 0 ; i < number ; i++ ){
                GTNode children = expr.getChild(i);
                if ( i == 0 ) result =  evaluate(children);
                else
                result = result - evaluate(children);
              }
              return result;
            }    
            else if ( expr.getItem().toString().equals("*")){
                if ( number  <  1 ) {
                 UI.println("Incorrect expression for multiplication!");
                 return Double.NaN;
              }
                result = 1;
            for ( int i = 0 ; i < number ; i++ ){
                GTNode children = expr.getChild(i);double no = 0;
                result = result * evaluate(children);
                }    
            return result;
        }else if ( expr.getItem().toString().equals("/")){
                if ( number < 1 ) {
                 UI.println("Incorrect expression division!");
                 return Double.NaN;
              }
              for ( int i = 0 ; i < number ; i++ ){
                GTNode children = expr.getChild(i);
                if ( i == 0 ) result =  evaluate(children);
                else
                result = result / evaluate(children);
              }
              return result;
            }else if ( expr.getItem().toString().equals("avg")){
                if ( number < 1 ) {
                 UI.println("Incorrect expression average!");
                 return Double.NaN;
              }
              for ( int i = 0 ; i < number ; i++ ){
                GTNode children = expr.getChild(i);
                result = result + evaluate(children);
              }
              result = result / number;
              return result;
            }   
        else if (expr.getItem().toString().toUpperCase().equals("PI")){
            return Math.PI;
        }
        else if (expr.getItem().toString().toUpperCase().equals("E")){
            return Math.E;
        }
        else if (expr.getItem().toString().toUpperCase().equals("SIN")){
            if ( number != 1 ) {
                UI.println("Incorrect expression for sine!");
             return Double.NaN;
            }
            GTNode child = expr.getChild(0);
            result = Math.sin(evaluate(child));
            return result;
        }
        else if (expr.getItem().toString().toUpperCase().equals("COS")){
            if ( number != 1 ) {
                 UI.println("Incorrect expression for cosine!");
                 return Double.NaN;
            }
            GTNode child = expr.getChild(0);
            result = Math.cos(evaluate(child));
            return result;
        }
        else if (expr.getItem().toString().toUpperCase().equals("TAN")){
            if ( number != 1 ) {
                 UI.println("Incorrect expression for tangent!");
                 return Double.NaN;
              }
            GTNode child = expr.getChild(0);
            result = Math.tan(evaluate(child));
            return result;
        }else if ( expr.getItem().toString().equals("^")){
                if ( number != 2 ) {
                 UI.println("Incorrect expression for power!");
                 return Double.NaN;
              }
              GTNode children = expr.getChild(0);
              GTNode children1 = expr.getChild(1);
              result = Math.pow(evaluate(children),evaluate(children1));
              return result;
        }  
        else if ( expr.getItem().toString().equals("ln")){
              if ( number != 1 ) {
                 UI.println("Incorrect expression for ln!");
                 return Double.NaN;
              }
              GTNode children = expr.getChild(0);
              return Math.log(evaluate(children));
        }   
        else if ( expr.getItem().toString().equals("log")){
              if ( number != 1 && number != 2 ) {
                 UI.println("Incorrect expression for log!");
                 return Double.NaN;
              }
              if (number == 1){
              GTNode children = expr.getChild(0);
              return Math.log10(evaluate(children));}
              else {
                  GTNode children = expr.getChild(0);
                  GTNode children1 = expr.getChild(1);
              return (Math.log10(evaluate(children1))/Math.log10(evaluate(children)));
                }
        }   
        
        else if ( expr.getItem().toString().equals("dist")){
              if ( number != 4 && number != 6 ) {
                 UI.println("Incorrect expression for distance");
                 return Double.NaN;
              }
              if (number == 4 ){
                double a = expr.getChild(0).getItem().value;
                double b = expr.getChild(1).getItem().value;
                double c = expr.getChild(2).getItem().value;
                double d = expr.getChild(3).getItem().value;
                result =  Math.sqrt(Math.pow(c-a,2) + Math.pow(d-b,2));
                }  
              if (number == 6 ){
                double a = expr.getChild(0).getItem().value;
                double b = expr.getChild(1).getItem().value;
                double c = expr.getChild(2).getItem().value;
                double d = expr.getChild(3).getItem().value;
                double e = expr.getChild(3).getItem().value;
                double f = expr.getChild(3).getItem().value;
                result = Math.sqrt(Math.pow(d - a, 2) + Math.pow(e - b, 2) + Math.pow(f - c, 2));
                } 
              return result;
        }   
        else if (!isOperator(expr)) {
            return (Double) expr.getItem().value; 
        }
        return result;
    }
    
    
    //if it is an operator or not
    public boolean isOperator(GTNode<ExpElem> expr){
        if (expr.getItem().toString().equals("+") ||  expr.getItem().toString().equals("-") || expr.getItem().toString().equals("*") || expr.getItem().toString().equals("/") 
        || expr.getItem().toString().equals("^") || expr.getItem().toString().toUpperCase().equals("PI") || expr.getItem().toString().toUpperCase().equals("E") ||
        expr.getItem().toString().toUpperCase().equals("SIN") ||
        expr.getItem().toString().toUpperCase().equals("COS") ||
        expr.getItem().toString().toUpperCase().equals("TAN") ||
        expr.getItem().toString().equals("dist") ||
        expr.getItem().toString().equals("log") ||
        expr.getItem().toString().equals("avg") ||
        expr.getItem().toString().equals("ln")) return true;
        return false ;
    }
    
    //printing expression
    public void printExpr(GTNode<ExpElem> expr,List<String> items, int priority){
        boolean hasBracket = false;
        String item = expr.getItem().toString();
        if (isOperator( expr ) == false) {
            if ( Double.valueOf(item) < 0){
                item = "("+ item + ")";
            }
            items.add(item);
        }
        int number = expr.numberOfChildren();
        if (item.equals("+")){
            if (priority == 2 || priority == 3){
                hasBracket = true;
            }
            priority = 0;
            if (hasBracket == true) items.add("(");
            for (int i = 0 ; i< number ; i++){
                printExpr(expr.getChild(i),items ,priority);
                if (i+1 < number)
                items.add("+");
            }
            if (hasBracket == true) items.add(")");
        }else if (item.equals("-")){
            if (priority == 2 || priority == 3){
                hasBracket = true;
            }
            priority = 1;
              GTNode children = expr.getChild(0);
              GTNode children1 = expr.getChild(1);
              if (hasBracket = true) items.add("(");
              printExpr(children,items, priority);
              items.add("-");
              printExpr(children1,items, priority);
              if (hasBracket = true) items.add(")");
        }else if (item.equals("*")){
            if (priority == 3) hasBracket = true;
            priority = 2;
            if (hasBracket == true) items.add("(");
            for (int i = 0 ; i< number ; i++){
                printExpr(expr.getChild(i),items, priority);
                if (i+1 < number)
                items.add("*");
            }
            if (hasBracket == true) items.add(")");
        }else if (item.equals("/")){
              priority = 3;
              GTNode children = expr.getChild(0);
              GTNode children1 = expr.getChild(1);
              printExpr(children,items,priority);
              items.add("/");
              printExpr(children1,items,priority);
              hasBracket = false;
        }else if ( expr.getItem().toString().equals("^")){
              GTNode children = expr.getChild(0);
              GTNode children1 = expr.getChild(1);
              printExpr(children,items,priority);
              items.add("^");
              printExpr(children1,items,priority);
        }  
        else if ( expr.getItem().toString().toUpperCase().equals("SIN")){
              GTNode children = expr.getChild(0);
              items.add("sin");
              items.add("(");
              printExpr(children,items,priority);
              items.add(")");
        }   
        else if ( expr.getItem().toString().toUpperCase().equals("COS")){
              GTNode children = expr.getChild(0);
              items.add("cos");
              items.add("(");
              printExpr(children,items,priority);
              items.add(")");
        }   
        else if ( expr.getItem().toString().toUpperCase().equals("TAN")){
              GTNode children = expr.getChild(0);
              items.add("tan");
              items.add("(");
              printExpr(children,items,priority);
              items.add(")");
        }   
        else if ( expr.getItem().toString().equals("ln")){
              GTNode children = expr.getChild(0);
              items.add("ln");
              items.add("(");
              printExpr(children,items,priority);
              items.add(")");
        }
        else if ( expr.getItem().toString().equals("log")){
            if ( number == 1 ) {
              GTNode children = expr.getChild(0);
              items.add("log");
              items.add("(");
              printExpr(children,items,priority);
              items.add(")");
            }
            else {
                GTNode children = expr.getChild(0);
                GTNode children1 = expr.getChild(1);
              items.add("log");
              items.add("(");
              printExpr(children,items,priority);
              items.add(" ");
              printExpr(children1,items,priority);
              items.add(")");
            }
        }
        else if ( expr.getItem().toString().equals("avg")){
            items.add("average");
            items.add("(");
              for (int i = 0 ; i< number ; i++){
                printExpr(expr.getChild(i),items, priority);
                items.add(",");
            }
              items.add(")");
        }
        else if ( expr.getItem().toString().toUpperCase().equals("PI")){
              items.add("PI");
        }  
        
        else if ( expr.getItem().toString().toUpperCase().equals("E")){
              items.add("E");
        }  
        else if ( expr.getItem().toString().toUpperCase().equals("DIST")){
              items.add("distance");
              items.add("(");
              for (int i = 0 ; i< number ; i++){
                printExpr(expr.getChild(i),items, priority);
                if (i+1 < number)
                items.add(",");
            }   
              items.add(")");
        }  
    }
    
    
    //reading expr
    /** 
     * Reads an expression from the user and constructs the tree.
     */ 
    public GTNode<ExpElem> readExpr(){
        String expr = UI.askString("expr:");
        int check = 0;
        if (checkBracket(expr,check) == -1){
            UI.println("Missing an opening bracket");   
            return null;
        }else if (checkBracket(expr,check) == 1){
            UI.println("Missing a closing bracket");   
            return null;
        }else if (checkBracket(expr,check) < -1){
            UI.println("Missing " + checkBracket(expr,check) + " opening brackets");   
            return null;
        }else if (checkBracket(expr,check) > 1){
            UI.println("Missing " + checkBracket(expr,check) + " closing brackets"); 
            return null;
        } else 
        return readExpr(new Scanner(expr));   // the recursive reading method
    }
    
    
    //check if there is anything wrong with the brackets
    /**
     * Recursive helper method.
     * Uses the hasNext(String pattern) method for the Scanner to peek at next token
     */
    public GTNode<ExpElem> readExpr(Scanner sc){
        if (sc.hasNextDouble()) {                     // next token is a number: return a new node
            return new GTNode<ExpElem>(new ExpElem(sc.nextDouble()));
        }
    else if (sc.hasNext("\\(")) {                 // next token is an opening bracket
            sc.next();                                // read and throw away the opening '('
            if(sc.hasNext("\\)")){
                UI.println("Invalid tree!");
                return null;
            }
            ExpElem opElem = new ExpElem(sc.next());  // read the operator
            GTNode<ExpElem> node = new GTNode<ExpElem>(opElem);  // make the node, with the operator in it.
            while (! sc.hasNext("\\)")){              // loop until the closing ')'
                GTNode<ExpElem> child = readExpr(sc); // read each operand/argument
                node.addChild(child);                 // and add as a child of the node      
            }
            sc.next();                                // read and throw away the closing ')'
            return node;
        }
        else {    
                                                            // next token must be a named constant (PI or E)
                                                            // make a token with the name as the "operator"   
           return new GTNode<ExpElem>(new ExpElem(sc.next()));
        }
    }
    
    public int checkBracket(String expr,int check){
        Scanner sc = new Scanner(expr);
        while (sc.hasNext()){
        if (sc.hasNext("\\(")) {                 // next token is an opening bracket
            sc.next();                                // read and throw away the opening '('
            check ++;   
        }else if  (sc.hasNext("\\)")) {
            sc.next(); 
            check --; 
        }
        else {
            sc.next();
        }
      }
        return check;
    }
}
