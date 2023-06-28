package test.java.test.nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.studentdb.Degree;
import nz.ac.wgtn.swen301.studentdb.NoSuchRecordException;
import nz.ac.wgtn.swen301.studentdb.Student;
import nz.ac.wgtn.swen301.studentdb.StudentDB;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

import main.java.nz.ac.wgtn.swen301.assignment1.StudentManager;

import static org.junit.Assert.*;


public class TestStudentManager {

    // DO NOT REMOVE THE FOLLOWING -- THIS WILL ENSURE THAT THE DATABASE IS AVAILABLE
    // AND IN ITS INITIAL STATE BEFORE EACH TEST RUNS
    @Before
    public  void init () {
        StudentDB.init();
    }
    // DO NOT REMOVE BLOCK ENDS HERE

    @Test
    public void dummyTest() throws Exception {
        new StudentManager();
		Student student = StudentManager.readStudent("id42");
        // THIS WILL INITIALLY FAIL !!
        assertNotNull(student);
    }
    
   //Test read first name of student has id of "id42"
    @Test
    public void test_readStudent1() throws Exception {
        new StudentManager();
        Student student = StudentManager.readStudent("id42");
        assertEquals("Sue", student.getFirstName());
    }
    
  //Test read last name of student has id of "id0"
    @Test
    public void test_readStudent2() throws Exception {
        new StudentManager();
        Student student = StudentManager.readStudent("id0");
        assertEquals("Smith", student.getName());
    }
    
    //Test read first name of student has id of "id9"
    @Test
    public void test_readStudent3() throws Exception {
        new StudentManager();
        Student student = StudentManager.readStudent("id9");
        assertEquals("Alex", student.getFirstName());
    }
    
    //Test read degree from degree id of "deg3"
    @Test
    public void test_readDegree1() throws Exception {
    	new StudentManager();
		Degree degree = StudentManager.readDegree("deg3");
        assertEquals("BE Software Engineering", degree.getName());
    }
    
  //Test read degree from degree id of "deg5"
    @Test
    public void test_readDegree2() throws Exception {
    	new StudentManager();
		Degree degree = StudentManager.readDegree("deg5");
        assertEquals("BSc Chemistry", degree.getName());
    }
    
    //Test read degree from student with id of "id4"
    @Test
    public void test_readDegree3() throws Exception {
    	new StudentManager();
		Student student = StudentManager.readStudent("id4");
        Degree degree = student.getDegree();
        assertEquals("BSc Mathematics", degree.getName());
    }
    
    //Test deleting student
    @Test
    public void test_deleteStudent1() throws Exception {
    	 new StudentManager();
         Student student = StudentManager.readStudent("id6");
         StudentManager.delete(student);

         try {
             StudentManager.readStudent("id6");
         } catch (NoSuchRecordException e) {
             System.out.println("no such record");
         }
    }
    
  	//Test 1000 queries
    @Test
    public void testPerformance() throws  Exception {
        new StudentManager();
        long start = System.nanoTime();

        Random r = new Random();
        for(int i = 0; i < 1000; i++) StudentManager.readStudent("id" + r.nextInt(10000));

        double timeTaken = ((double)System.nanoTime() - (double)start)/1000000000;
        assertTrue(timeTaken <= 1.00);
    }

    //Test update student
    @Test
    public void test_updateStudent1() throws Exception {
    	new StudentManager();
  
            Student student = StudentManager.readStudent("id2");

            Student newStudent = new Student("id2", "Hai Thien", "Tran", StudentManager.readDegree("deg3"));
            StudentManager.update(newStudent);

            Student getUpdatedStudent = StudentManager.readStudent("id2");
            if(student.getId().equals(getUpdatedStudent.getId())) 
            	assertNotEquals(getUpdatedStudent.getFirstName(), student.getFirstName());
    }
    
  //Test size of student array, must be 10000
    @Test
    public void test_getAllStudent() throws Exception {
    	new StudentManager();
        ArrayList<String> ids = (ArrayList<String>) StudentManager.getAllStudentIds();
        assertEquals(10000, ids.size());
    }
    
  //Test size of degree array, must be 10
    @Test
    public void test_getAllDegree() throws Exception {
    	new StudentManager();
        ArrayList<String> ids = (ArrayList<String>) StudentManager.getAllDegreeIds();
        assertEquals(10, ids.size());
    }
    
    //Test create student 1
    @Test
    public void wtest_writeStudent() throws Exception {
    	new StudentManager();

        Degree degree = StudentManager.readDegree("deg4");
        StudentManager.createStudent("Nguyen", "Wendy", degree);
        String name = StudentManager.readStudent("id10000").getFirstName();

        assertTrue(Objects.equals(name, "Wendy"));
    }
    
  //Test create student 2
    @Test
    public void wtest_writeStudent1() throws Exception {
    	new StudentManager();
    	
        Degree degree = StudentManager.readDegree("deg9");
        StudentManager.createStudent("Hojin", "Han", degree);
        String name = StudentManager.readStudent("id10001").getFirstName();

        assertTrue(Objects.equals(name, "Han"));
    } 
}
