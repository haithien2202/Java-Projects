
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
import java.awt.Color;

/**
 * Simple Simulation of a Hospital ER
 * 
 * The Emergency room has a waiting room and a treatment room that has a fixed
 *  set of beds for examining and treating patients.
 * 
 * When a patient arrives at the emergency room, they are immediately assessed by the
 *  triage team who determines the priority of the patient.
 *
 * They then wait in the waiting room until a bed becomes free, at which point
 * they go from the waiting room to the treatment room.
 *
 * When a patient has finished their treatment, they leave the treatment room and are discharged,
 *  at which point information about the patient is added to the statistics. 
 *
 *  READ THE ASSIGNMENT PAGE!
 */

public class HospitalERCompl{

    // Fields for recording the patients waiting in the waiting room and being treated in the treatment room
    private Queue<Patient> waitingRoom = new ArrayDeque<Patient>();
    private static final int MAX_PATIENTS = 5;   // max number of patients currently being treated
    private Set<Patient> treatmentRoom = new HashSet<Patient>();

    // fields for the statistics
    /*# YOUR CODE HERE */

    // Fields for the simulation
    private boolean running = false;
    private boolean checkSort = false;
    private int time = 0; // The simulated time - the current "tick"
    private int delay = 300;  // milliseconds of real time for each tick

    // fields controlling the probabilities.
    private int arrivalInterval = 5;   // new patient every 5 ticks, on average
    private double probPri1 = 0.1; // 10% priority 1 patients
    private double probPri2 = 0.2; // 20% priority 2 patients
    private Random random = new Random();  // The random number generator.
    private Map<String,Department> department = new HashMap<String,Department>();
    // number of patients count
    private int numberOfPatient = 0;
    // number of priority patients count
    private int numberOfPrioPatient = 0;
    // waiting time of priority patients and other patients
    private int waitingTime = 0;
    private int waitingPrioTime = 0;
    // max patient for emergency room
    private int maxEmergency = 0;
    //List,Set for emergency rooms
    private Set<Patient> emergencyRoom = new HashSet<Patient>();
    private Queue<Patient> waitingERoom = new ArrayDeque<Patient>();
    private List<Integer> waitingTimeList = new ArrayList<Integer>();
    //Lists contain removed patients and their waiting time
    private List<Integer> priorityList = new ArrayList<Integer>();
    private List<Patient> removedPatient = new ArrayList<Patient>();
    // initial X scrolling to graph
    private int initialX = 0;
    private boolean check = false;
    //check if it is overloaded or not
    private boolean isOverloaded = false;
    private boolean isOverloaded1 = false;
    
    /**
     * Construct a new HospitalERCore object, setting up the GUI, and resetting
     */
    public static void main(String[] arguments){
        HospitalERCompl er = new HospitalERCompl();
        er.loadDepartment();
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
        UI.addButton("Pause & Report",   this::reportStatistics);
        UI.addButton(">",   this::goRight);
        UI.addButton("<",   this::goLeft);
        UI.addSlider("Speed", 1, 400, (401-delay),
            (double val)-> {delay = (int)(401-val);});
        UI.addSlider("Av arrival interval", 1, 50, arrivalInterval,
            (double val)-> {arrivalInterval = (int)val;});
        UI.addSlider("Prob of Pri 1", 1, 100, probPri1*100,
            (double val)-> {probPri1 = val/100;});
        UI.addSlider("Prob of Pri 2", 1, 100, probPri2*100,
            (double val)-> {probPri2 = Math.min(val/100,1-probPri1);});
            //able to close or open rooms for emergency
        UI.addSlider("Emergency room max patients", 0, 5, 0, this::emergencyRoom);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1000,600);
        UI.setDivider(0.5);
    }
    
    //declare departments
    public void loadDepartment(){
    department.put("ER", new Department("ER", 8));
    department.put("MRI", new Department("MRI", 2));
    department.put("Surgery", new Department("Surgery", 2));
    department.put("X-ray", new Department("X-ray", 1));
    department.put("Ultrasound", new Department("Ultrasound", 3));
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

        if (usePriorityQueue == true){
            checkSort = true;
        }
        if (usePriorityQueue == false){
            checkSort = false;
        }
        for (String name : department.keySet()){
                 Department dept = department.get(name);
                 dept.clearAll();
            }

        waitingTimeList.clear();
        removedPatient.clear();
        numberOfPatient = 0;
        numberOfPrioPatient = 0;
        waitingPrioTime = 0;
        waitingTime = 0;

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
            check = false;
            // Hint: if you are stepping through a set, you can't remove
            //   items from the set inside the loop!
            //   If you need to remove items, you can add the items to a
            //   temporary list, and after the loop is done, remove all 
            //   the items on the temporary list from the set.
            for (String name : department.keySet()){
                 Department dept = department.get(name);
                 Queue<Patient> waitingRoom1 = new ArrayDeque<Patient>(dept.getPatientList());
                for (Patient x : waitingRoom1 ){
                   x.waitForATick(); 
                }
            }
            addPatient();
            if ( checkSort == true){
                sortPatient();
            }
            treatPatient();
            addEPatient();
            treatEPatient();
            checkPatient();
            if (isOverloaded == true){
                reportOverload();
            }
            if (isOverloaded1 == true){
                reportOverload();
            }
            UI.sleep(2*delay);
            
            // Gets any new patient that has arrived and adds them to the waiting room
            if (time==1 || Math.random()<1.0/arrivalInterval){
                Patient newPatient = new Patient(time, randomPriority());
                UI.println(time+ ": Arrived: "+newPatient);
                String currentTreatment = newPatient.getCurrentTreatment();
                Department dept = department.get(currentTreatment);
                if (maxEmergency > 0){
                    if (newPatient.getPriority() == 1 && emergencyRoom.size() <5){
                        waitingERoom.offer(newPatient);
                    }
                    else dept.addPatient(newPatient); 
                }
                else
                dept.addPatient(newPatient);  
            }
            redraw();
            UI.sleep(delay);
        }
    }
 
    // Additional methods used by run() (You can define more of your own)
    // Check if patient is waiting for too long or not
    public void checkPatient(){
           double currentW = 0;
           double currentW1 = 0;
           int noOfPatients = 0;
            int noOfPatients1 = 0;
           for ( String name : department.keySet()){
                           Department dept = department.get(name);
                           List<Patient> waitingR = new ArrayList<Patient>(dept.getPatientList());
                 for (Patient x : waitingR){
                        if (x.getPriority() > 1){
                          currentW += x.getWaitingTime();
                          noOfPatients ++;
                            }
                          if (x.getPriority() == 1){
                              currentW1 += x.getWaitingTime();
                             noOfPatients1 ++;
                          }
                    }
           }
           if (waitingERoom.size() > 0){
               for (Patient x : waitingERoom){
                         currentW1 += x.getWaitingTime();
                         noOfPatients1 ++;
                    }
            }
           currentW = currentW / noOfPatients;
           currentW1 = currentW1 / noOfPatients1;
           if ( currentW > 120 ){
               isOverloaded = true;
            }
            else isOverloaded = false;
           if ( currentW > 30 ){
               isOverloaded1 = true;
            }
            else isOverloaded1 = false;
           
    }
    
    //adding patient into treatment room
    public void addPatient(){
           for ( String name : department.keySet()){
                           Department dept = department.get(name);
                           dept.removePatientWatingRoom();
           }
    }
    
    //adding patient into emergency room
    public void addEPatient(){
               if (waitingERoom.size() > 0){
                   if (emergencyRoom.size() < maxEmergency){
                       Patient finishWaiting = waitingERoom.poll();
                       emergencyRoom.add(finishWaiting);
                    }
                }
    }
    
    //treat patient in emergency room
    public void treatEPatient(){
        if (emergencyRoom.size() > 0){
            for (Patient x : emergencyRoom ){
            x.advanceTreatmentByTick();
            if ( x.completedCurrentTreatment() == true){
                x.incrementTreatmentNumber1();   
                if (x.noMoreTreatments()== true){    
                    waitingPrioTime += x.getWaitingTime();
                    numberOfPrioPatient += 1;
                    waitingTimeList.add(x.getWaitingTime());
                    priorityList.add(x.getPriority());
                    removedPatient.add(x);
                    UI.println(time+ ": Released: "+x);
                    emergencyRoom.remove(x);
                    UI.sleep(2*delay);
                    break;
                }
            }
           }
        }
    }
    
    //sorting patient room
     public void sortPatient(){
         for (String name : department.keySet()){
             Department dept = department.get(name);
             dept.sortWaitingPatient();
            }
    }
    
    //treat patient in other departments
    public void treatPatient(){
        for (String name : department.keySet()){
            Department dept = department.get(name);
             Set<Patient> treatmentRoom1 = new  HashSet<Patient>(dept.getTreatmentRoomList());
            for (Patient x : treatmentRoom1){
                 x.advanceTreatmentByTick();
                if ( x.completedCurrentTreatment()){
                    if(x.noMoreTreatments1() == false && x.noMoreTreatments()== false){
                        String next = x.getNextTreatment();
                        Department nextDept = department.get(next);
                        nextDept.addPatient(x);
                        x.incrementTreatmentNumber();
                    }
                    else {
                        int prio = x.getPriority();
                        waitingTimeList.add(x.getWaitingTime());
                        priorityList.add(x.getPriority());
                        removedPatient.add(x);
                        UI.println(time+ ": Released: "+x);
                        if (prio == 1){
                            waitingPrioTime += x.getWaitingTime();
                            numberOfPrioPatient += 1;
                        }
                        if (prio > 1){
                            waitingTime += x.getWaitingTime();
                            numberOfPatient += 1;
                        }
                    }
                        dept.removeTreatmentPatient(x);     
                }
             }
      }
    }
    
    //drawing graph
    public void drawGraph(){
        UI.clearGraphics();
        check = true;
        int x = 20;
        int x1 = 22;
        int y = 400;
        for (int i = 0; i < waitingTimeList.size(); i++){
            int timewaited =  waitingTimeList.get(i);
            if ( priorityList.get(i) == 1) UI.setColor(Color.red);
            if ( priorityList.get(i) == 2) UI.setColor(Color.yellow);
            if ( priorityList.get(i) == 3) UI.setColor(Color.green);
            UI.fillRect(x+10*i - initialX, y - waitingTimeList.get(i), 6,waitingTimeList.get(i));
            UI.setColor(Color.black);
            UI.drawRect(x+10*i - initialX, y - waitingTimeList.get(i),6, waitingTimeList.get(i));
            UI.drawString(String.valueOf(waitingTimeList.get(i)), x+10*i - initialX, y - waitingTimeList.get(i) - 10);
        }
        int n = 0;
        for (Patient patient : removedPatient){
            patient.redraw(x1+10*n - initialX , y + 30);
            n+=1;
        }
    }
    
    //scroling through drawed graph
    public void goLeft(){
        if (check == true){
        initialX += 30;
        drawGraph();}
    }
    
    public void goRight(){
        if (check == true){
        initialX -= 30;
        drawGraph();}
    }
    
    //report stats
    /**
     * Report summary statistics about all the patients that have been discharged.
     * (Doesn't include information about the patients currently waiting or being treated)
     * The run method should have been recording various statistics during the simulation.
     */
    public void reportStatistics(){
        running = false;
            UI.sleep(2*delay);
            drawGraph();
            if (numberOfPatient > 0){
                UI.println("Processed " + numberOfPatient + " patients with average waiting time of " + waitingTime/numberOfPatient + " seconds.");
            }
            else if (numberOfPatient == 0)  UI.println("No patient processed.");
            if (numberOfPrioPatient > 0){
                UI.println("Processed " + numberOfPrioPatient + " priority patients with average waiting time of " + waitingPrioTime/numberOfPrioPatient + " seconds.");
           }    
           else if (numberOfPrioPatient == 0)  UI.println("No patient processed.");
    }
       
    
    //report overloaded
        public void reportOverload(){
                     UI.drawString("It is overloaded! Increase receiving patients inverval!", 20, 500);
      }
    
      public void reportOverload1(){
                     UI.drawString("High risk patients have been waiting for too long, open more emergency rooms for high risk patients!",20, 520);
                     UI.drawString("Stop receiving patients!" ,20, 540);
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
        UI.drawString("MRI", 0, y+20);
        UI.drawString("Surgery", 0, y+75);
        UI.drawString("X-ray", 0, y+130);
        UI.drawString("Ultrasound", 0, y+185);
        double x = 10;
        UI.drawRect(x-5, y-30 + (0*55), 8*10, 30); 
        UI.drawRect(x-5, y-30 + (1*55), 2*10, 30); 
        UI.drawRect(x-5, y-30 + (2*55), 2*10, 30); 
        UI.drawRect(x-5, y-30 + (3*55), 1*10, 30); 
        UI.drawRect(x-5, y-30 + (4*55), 3*10, 30); 
        if (maxEmergency > 0){
           UI.drawString("Emergency Room", 0, y+(240));
           UI.drawRect(x-5, y-30 + (5*55) , maxEmergency*10, 30); 
           UI.drawLine(0,y+2+(5*55),400, y+2+5*55);
        }
        for (String nameDep : department.keySet()){
             Department dept = department.get(nameDep);
             int yDraw = 0;
             if (nameDep.equals("ER")) yDraw = 80;
             if (nameDep.equals("MRI")) yDraw = 135;
             if (nameDep.equals("Surgery")) yDraw = 190;
             if (nameDep.equals("X-ray")) yDraw = 245;
             if (nameDep.equals("Ultrasound")) yDraw = 300;
        for(Patient p : dept.getTreatmentRoomList()){
            dept.redraw(yDraw);
        }
        x = 200;
        for(Patient p : dept.getPatientList()){
            dept.redraw(yDraw);
        }
    }   
        int z = 10;
       for(Patient p : emergencyRoom){
            p.redraw(z, y+(5*55));
            z += 10;
        }
        x = 200;
        for(Patient p : waitingERoom){
            p.redraw(x, y+(5*55));
            x += 10;
        }
        for( int i =0; i <5 ; i++){
        UI.drawLine(0,y+2+i*55,400, y+2+i*55);
    }
    }
    
    //input for maxEmergency room
    public void emergencyRoom(double maxPatients){
        maxEmergency = (int) maxPatients;
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