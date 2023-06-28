// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3
 * Name:    Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import ecs100.*;

public class Town {

    private String name;
    private Set<Town> neighbours = new HashSet<Town>();
    private boolean visited = false;
    private boolean selected = false;
    private double ladt = Double.NaN;
    private double longt = Double.NaN;

    public Town(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    
    public void setVisited(boolean v) {
        visited = v;
    }
    
    public void setLong(double x){
        longt = x;
    }
    
    public void setLad(double x){
        ladt = x;
    }
    
    public double getLad(){
        return ladt;
    }
    
    public double getLong(){
        return longt;
    }
    
    public boolean isSelected(){
        return selected;
    }
    
    public void setSelected(boolean v){
        selected = v;
    }
    
    public boolean on(double x,double y, double longi , double lati){
        return (Math.abs(longi-x)<=10/2 && (y >= lati) && (y <= lati + 10) );
    }
    
    public boolean isVisited() {
        return visited;
    }

    public Set<Town> getNeighbours() {
        return Collections.unmodifiableSet(neighbours);
    }

    public void addNeighbour(Town node) {
        neighbours.add(node);
    }

    public String toString(){
        return name+" ("+neighbours.size()+" connections)";
    }


}
