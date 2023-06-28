// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3, Assignment 3
 * Name:    Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

import ecs100.*;
import java.util.*;
//import java.io.*;

/**
 * A treatment Department (Surgery, X-ray room,  ER, Ultrasound, etc)
 * Each department will need
 * - A name,
 * - A maximum number of patients that can be treated at the same time
 * - A Set of Patients that are currently being treated
 * - A Queue of Patients waiting to be treated.
 *    (ordinary queue, or priority queue, depending on argument to constructor)
 */

public class Department{

    private String name;
    private int maxPatients;

    /*# YOUR CODE HERE */
    private Set<Patient> treatmentRoom = new HashSet<Patient>();
    private Queue<Patient> waitingRoom = new ArrayDeque<Patient>();
    private List<Patient> PrioWaitingRoom = new ArrayList<Patient>();
    
    /**
     * Constructor requires
     */
    public Department(String name, int maxPatients){
        this.name = name;
        this.maxPatients = maxPatients;
    }
    
    //add patient into waiting room
    public void addPatient(Patient x){
        waitingRoom.offer(x);
    }
    
    //add patient into treatment room
    public void addTreatmentPatient(Patient x){
        treatmentRoom.add(x);
    }
    
    //remove patient from treatment room when treatment is completed
    public void removeTreatmentPatient(Patient x){
        treatmentRoom.remove(x);
    }
    
    //get queue of patients in waiting room
    public Queue<Patient> getPatientList(){
        return waitingRoom;
    }
    
    //remove patients from waiting room and add them to treatment room
    public void removePatientWatingRoom(){
        if(waitingRoom.size() >0){
        if (treatmentRoom.size() < maxPatients){
        Patient x = waitingRoom.poll();
         treatmentRoom.add(x);
        }}
    }
    
    // get set of patients in treatment room
    public Set<Patient> getTreatmentRoomList(){
        return  treatmentRoom;
    }
    
    //clear all patients from boths collections
    public void clearAll(){
        treatmentRoom.clear();
        waitingRoom.clear();
    }
    
    //remove patient from treatment room
    public void removePatient(Patient x){
        treatmentRoom.remove(x);
    }
    
    //sort patient by priority
    public void sortWaitingPatient(){
    if (waitingRoom.size() > 0){
        PrioWaitingRoom.clear();
        PrioWaitingRoom.addAll(waitingRoom);
        waitingRoom.clear();
        Collections.sort(PrioWaitingRoom);
        waitingRoom.addAll(PrioWaitingRoom);
       } 
    }
    
    //draw patient
    /**
     * Draw the department: the patients being treated and the patients waiting
     * You may need to change the names if your fields had different names
     */
    public void redraw(double y){
        UI.setFontSize(14);
        UI.drawString(name, 0, y-35);
        double x = 10;
        UI.drawRect(x-5, y-30, maxPatients*10, 30);  // box to show max number of patients
        for(Patient p : treatmentRoom){
            p.redraw(x, y);
            x += 10;
        }
        x = 200;
        for(Patient p : waitingRoom){
            p.redraw(x, y);
            x += 10;
        }
    }

}
