


// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T3, Assignment 3
 * Name: Hai Thien Tran
 * Username: tranhai1
 * ID: 300503987
 */

import ecs100.*;
import java.util.*;
import java.io.*;

/**
 * Simulation of a Hospital ER
 * 
 * The hospital has a collection of Departments, including the ER department, each of which has
 *  and a treatment room.
 * 
 * When patients arrive at the hospital, they are immediately assessed by the
 *  triage team who determine the priority of the patient and (unrealistically) a sequence of treatments 
 *  that the patient will need.
 *
 * The simulation should move patients through the departments for each of the required treatments,
 * finally discharging patients when they have completed their final treatment.
 *
 *  READ THE ASSIGNMENT PAGE!
 */

public class HospitalERCore{

    // Copy the code from HospitalERCore and then modify/extend to handle multiple departments

    // Fields for recording the patients waiting in the waiting room and being treated in the treatment room
    private Queue<Patient> waitingRoom = new ArrayDeque<Patient>();
    private static final int MAX_PATIENTS = 5;   // max number of patients currently being treated
    private Set<Patient> treatmentRoom = new HashSet<Patient>();

    // fields for the statistics
    /*# YOUR CODE HERE */

    // Fields for the simulation
    private boolean running = false;
    private int time = 0; // The simulated time - the current "tick"
    private int delay = 300;  // milliseconds of real time for each tick
    private boolean checkSort = false;

    // fields controlling the probabilities.
    private int arrivalInterval = 5;   // new patient every 5 ticks, on average
    private double probPri1 = 0.1; // 10% priority 1 patients
    private double probPri2 = 0.2; // 20% priority 2 patients
    private Random random = new Random();  // The random number generator.
    private int numberOfPatient = 0;
    private int numberOfPrioPatient = 0;
    private int waitingTime = 0;
    private int waitingPrioTime = 0;
    private List<Patient> PrioWaitingRoom = new ArrayList<Patient>();

    /**
     * Construct a new HospitalERCore object, setting up the GUI, and resetting
     */
    public static void main(String[] arguments){
        HospitalERCore er = new HospitalERCore();
        er.setupGUI();
        er.reset(false);   // initialise with an ordinary queue.
    }        

    /**
     * Set up the GUI: buttons to control simulation and sliders for setting parameters
     */
    public void setupGUI(){
        UI.addButton("Reset (Queue)", () -> {this.reset(false); });
        UI.addButton("Reset (Pri Queue)", () -> {this.reset(true);});
        UI.addButton("Start", ()->{if (!running){ run(); }});   //don't start if already running!
        UI.addButton("Pause & Report", this::reportStatistics);
        UI.addSlider("Speed", 1, 400, (401-delay),
            (double val)-> {delay = (int)(401-val);});
        UI.addSlider("Av arrival interval", 1, 50, arrivalInterval,
            (double val)-> {arrivalInterval = (int)val;});
        UI.addSlider("Prob of Pri 1", 1, 100, probPri1*100,
            (double val)-> {probPri1 = val/100;});
        UI.addSlider("Prob of Pri 2", 1, 100, probPri2*100,
            (double val)-> {probPri2 = Math.min(val/100,1-probPri1);});
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1000,600);
        UI.setDivider(0.5);
    }

    /**
     * Reset the simulation:
     *  stop any running simulation,
     *  reset the waiting and treatment rooms
     *  reset the statistics.
     */
    public void reset(boolean usePriorityQueue){
        running=false;
        UI.sleep(2*delay);  // to make sure that any running simulation has stopped
        time = 0;           // set the "tick" to zero.

        // reset the waiting room, the treatment room, and the statistics.
        if (usePriorityQueue == true){
            checkSort = true;
        }
        if (usePriorityQueue == false){
            checkSort = false;
        }

        UI.clearGraphics();
        UI.clearText();
    }

    /**
     * Main loop of the simulation
     */
    public void run(){
        if (running) { return; } // don't start simulation if already running one!
        running = true;
        while (running){         // each time step, check whether the simulation should pause.

            // Hint: if you are stepping through a set, you can't remove
            //   items from the set inside the loop!
            //   If you need to remove items, you can add the items to a
            //   temporary list, and after the loop is done, remove all 
            //   the items on the temporary list from the set.
            for (Patient x : waitingRoom ){
                   x.waitForATick();  
            }
            addPatient();
            if ( checkSort == true){
                sortPatient();
            }
            treatPatient();
            
            // Gets any new patient that has arrived and adds them to the waiting room
            if (time==1 || Math.random()<1.0/arrivalInterval){
                Patient newPatient = new Patient(time, randomPriority());
                UI.println(time+ ": Arrived: "+newPatient);
                waitingRoom.offer(newPatient);
            }
            redraw();
            UI.sleep(delay);
        }
        // paused, so report current statistics
    }
 
    // Additional methods used by run() (You can define more of your own)
    
    public void addPatient(){
        if(waitingRoom.size() >0){
            if (treatmentRoom.size() < MAX_PATIENTS){
                       Patient finishWaiting = waitingRoom.poll();
                       treatmentRoom.add(finishWaiting);
                    }
                }
    }
    
    public void sortPatient(){
        if (waitingRoom.size() > 0){
            PrioWaitingRoom.clear();
            PrioWaitingRoom.addAll(waitingRoom);
            waitingRoom.clear();
            Collections.sort(PrioWaitingRoom);
            waitingRoom.addAll(PrioWaitingRoom);
       } 
    }
    
    public void treatPatient(){
        if (treatmentRoom.size() > 0){
        for (Patient x : treatmentRoom ){
                    x.advanceTreatmentByTick();
                    if ( x.completedCurrentTreatment() == true){
                        x.incrementTreatmentNumber1();   
                    if (x.noMoreTreatments() == true){
                        if (x.getPriority() == 1){
                        waitingPrioTime += x.getWaitingTime();
                        numberOfPrioPatient += 1;
                      }
                      if (x.getPriority() > 1){
                        waitingTime += x.getWaitingTime();
                        numberOfPatient += 1;
                      }
                        treatmentRoom.remove(x);
                        UI.sleep(2*delay);
                        break;
                    }
                }
        }
    }
    }
    
    /**
     * Report summary statistics about all the patients that have been discharged.
     * (Doesn't include information about the patients currently waiting or being treated)
     * The run method should have been recording various statistics during the simulation.
     */
    public void reportStatistics(){
         running = false;
            if (numberOfPatient > 0){
                UI.println("Processed " + numberOfPatient + " patients with average waiting time of " + waitingTime/numberOfPatient + " seconds.");
            }
            else if (numberOfPatient == 0)  UI.println("No patient processed.");
            if (numberOfPrioPatient > 0){
                UI.println("Processed " + numberOfPrioPatient + " priority patients with average waiting time of " + waitingPrioTime/numberOfPrioPatient + " seconds.");
           }    
           else if (numberOfPrioPatient == 0)  UI.println("No patient processed.");
            numberOfPatient = 0;
            numberOfPrioPatient = 0;
            waitingPrioTime = 0;
            waitingTime = 0;

    }


    // HELPER METHODS FOR THE SIMULATION AND VISUALISATION
    /**
     * Redraws all the departments
     */
    public void redraw(){
        UI.clearGraphics();
        UI.setFontSize(14);
        UI.drawString("Treating Patients", 5, 15);
        UI.drawString("Waiting Queues", 200, 15);
        UI.drawLine(0,32,400, 32);

        // Draw the treatment room and the waiting room:
        double y = 80;
        UI.setFontSize(14);
        UI.drawString("ER", 0, y-35);
        double x = 10;
        UI.drawRect(x-5, y-30 , MAX_PATIENTS*10, 30); 
        for(Patient p : treatmentRoom){
            p.redraw(x, y);
            x += 10;
        }
        x = 200;
        for(Patient p : waitingRoom){
            p.redraw(x, y);
            x += 10;
        }
        UI.drawLine(0,y+2,400, y+2);
    }

    /** 
     * Returns a random priority 1 - 3
     * Probability of a priority 1 patient should be probPri1
     * Probability of a priority 2 patient should be probPri2
     * Probability of a priority 3 patient should be (1-probPri1-probPri2)
     */
    private int randomPriority(){
        double rnd = random.nextDouble();
        if (rnd < probPri1) {return 1;}
        if (rnd < (probPri1 + probPri2) ) {return 2;}
        return 3;
    }

}

