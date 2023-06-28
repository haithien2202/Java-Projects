package main.java.nz.ac.wgtn.swen301.assignment1.cli;

import java.sql.SQLException;

import main.java.nz.ac.wgtn.swen301.assignment1.StudentManager;
import nz.ac.wgtn.swen301.studentdb.NoSuchRecordException;
import nz.ac.wgtn.swen301.studentdb.Student;

public class FindStudentDetails {

    // THE FOLLOWING METHOD MUST IMPLEMENTED
    /**
     * Executable: the user will provide a student id as single argument, and if the details are found,
     * the respective details are printed to the console.
     * E.g. a user could invoke this by running "java -cp <someclasspath> nz.ac.wgtn.swen301.assignment1.cli.FindStudentDetails id42"
     * @param arg
     * @throws SQLException 
     * @throws NoSuchRecordException 
     */
    public static void main (String[] arg) throws NoSuchRecordException, SQLException {
    	 Student student = null;
         if(!arg[0].isEmpty()){
             student = StudentManager.readStudent(arg[0]);
             System.out.println("ID: " + student.getId());
             System.out.println("First Name: " + student.getFirstName());
             System.out.println("Last Name: " + student.getName());
             System.out.println("Degree: " + student.getDegree().getName());
         }
    }
}
