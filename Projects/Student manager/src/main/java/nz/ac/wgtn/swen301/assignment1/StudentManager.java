package main.java.nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.studentdb.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * A student managers providing basic CRUD operations for instances of Student, and a read operation for instances of Degree.
 * @author jens dietrich
 */
public class StudentManager {
	//URL
	private static String url = "jdbc:derby:memory:studentdb;create=true";
	
	private static PreparedStatement STATEMENT;
	
	//Maps store read Student and Degree objects
	private static HashMap<String, Student> studentMap;
	private static HashMap<String, Degree> degreeMap;
	private static int lastID = 9999;  
    
    // DO NOT REMOVE THE FOLLOWING -- THIS WILL ENSURE THAT THE DATABASE IS AVAILABLE
    // AND THE APPLICATION CAN CONNECT TO IT WITH JDBC
    static {
    	degreeMap = new HashMap<String, Degree>();
        studentMap = new HashMap<String, Student>();
        StudentDB.init();
    }
    // DO NOT REMOVE BLOCK ENDS HERE

    // THE FOLLOWING METHODS MUST IMPLEMENTED :

    /**
     * Return a student instance with values from the row with the respective id in the database.
     * If an instance with this id already exists, return the existing instance and do not create a second one.
     * @param id
     * @return
     * @throws NoSuchRecordException if no record with such an id exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_readStudent (followed by optional numbers if multiple tests are used)
     * @throws SQLException 
     */
    public static Student readStudent(String id) throws NoSuchRecordException, SQLException {  		
        if(studentMap.containsKey(id)){
            return studentMap.get(id);
        }
    	
        String ID = null;
        String firstName = null;
        String lastName = null;
        String degree = null;

        
        Connection conn = DriverManager.getConnection(url);
        STATEMENT = conn.prepareStatement("SELECT * FROM students WHERE id = ?");
        STATEMENT.setString(1, id);
        STATEMENT.executeQuery();
        ResultSet results = STATEMENT.getResultSet();
        if (results.next()) {
            ID = results.getString(1);
            firstName = results.getString(2);
            lastName = results.getString(3);
            degree = results.getString(4);
        }
        results.close();
        conn.close();

        if(ID != null){
            Student student = new Student(id, lastName, firstName, readDegree(degree));
            studentMap.put(id, student);
            return student;
        }
        
        throw new NoSuchRecordException();
    }

    /**
     * Return a degree instance with values from the row with the respective id in the database.
     * If an instance with this id already exists, return the existing instance and do not create a second one.
     * @param id
     * @return
     * @throws NoSuchRecordException if no record with such an id exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_readDegree (followed by optional numbers if multiple tests are used)
     * @throws SQLException 
     */
    public static Degree readDegree(String id) throws NoSuchRecordException, SQLException {
         if(degreeMap.containsKey(id))  return degreeMap.get(id);
         
         String degree = null;
         Connection conn = DriverManager.getConnection(url);
         STATEMENT = conn.prepareStatement("SELECT * FROM degrees WHERE id = ?");
         STATEMENT.setString(1, id);
         STATEMENT.executeQuery();
         ResultSet degreeResults = STATEMENT.getResultSet();
         
         if (degreeResults.next()) degree = degreeResults.getString(2);
         degreeResults.close();
         conn.close();
         if(degree != null){
        	 Degree d = new Degree(id, degree);
        	 degreeMap.put(id, d);
        	 return d;
         }
         
         throw new NoSuchRecordException();
    }

    /**
     * Delete a student instance from the database.
     * I.e., after this, trying to read a student with this id will result in a NoSuchRecordException.
     * @param student
     * @throws NoSuchRecordException if no record corresponding to this student instance exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_delete
     * @throws SQLException 
     */
    public static void delete(Student student) throws NoSuchRecordException, SQLException {
    	 Connection conn = DriverManager.getConnection(url);
    	 STATEMENT = conn.prepareStatement("DELETE FROM students WHERE id = ?");

    	 STATEMENT.setString(1, student.getId());
         int row = STATEMENT.executeUpdate();
         studentMap.remove(student.getId());
         System.out.println("deleted row number " + row);

         conn.close();
    }

    /**
     * Update (synchronize) a student instance with the database.
     * The id will not be changed, but the values for first names or degree in the database might be changed by this operation.
     * After executing this command, the attribute values of the object and the respective database value are consistent.
     * Note that names and first names can only be max 1o characters long.
     * There is no special handling required to enforce this, just ensure that tests only use values with < 10 characters.
     * @param student
     * @throws NoSuchRecordException if no record corresponding to this student instance exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_update (followed by optional numbers if multiple tests are used)
     * @throws SQLException 
     */
    public static void update(Student student) throws NoSuchRecordException, SQLException {
    	 if (!studentMap.containsKey(student.getId())) throw new NoSuchRecordException();
    	 studentMap.remove(readStudent(student.getId()).getId());
    	
    	 Connection conn = DriverManager.getConnection(url);
    	 STATEMENT = conn.prepareStatement("UPDATE students SET first_name = ?, name = ?, degree = ? where id = ?");
    	 STATEMENT.setString(1, student.getName());
    	 STATEMENT.setString(2, student.getFirstName());
    	 STATEMENT.setString(3, student.getDegree().getId());
    	 STATEMENT.setString(4, student.getId());
    	 STATEMENT.executeUpdate();
    	 
    	 studentMap.put(student.getId(),student);
    }


    /**
     * Create a new student with the values provided, and save it to the database.
     * The student must have a new id that is not been used by any other Student instance or STUDENTS record (row).
     * Note that names and first names can only be max 1o characters long.
     * There is no special handling required to enforce this, just ensure that tests only use values with < 10 characters.
     * @param name
     * @param firstName
     * @param degree
     * @return a freshly created student instance
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_createStudent (followed by optional numbers if multiple tests are used)
     * @throws SQLException 
     */
    public static Student createStudent(String name,String firstName,Degree degree) throws SQLException {
    		lastID++;
            Connection conn = DriverManager.getConnection(url);
            STATEMENT = conn.prepareStatement("INSERT into students(id, first_name, name, degree) values (?, ?, ?, ?)");
            String id = "id" + lastID;
            Student student = new Student(id, name, firstName, degree);
            STATEMENT.setString(1, id);
            STATEMENT.setString(2, firstName);
            STATEMENT.setString(3, name);
            STATEMENT.setString(4, degree.getId());
            STATEMENT.executeUpdate();
            conn.close();
            
            return student;
    }

    /**
     * Get all student ids currently being used in the database.
     * @return
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_getAllStudentIds (followed by optional numbers if multiple tests are used)
     * @throws SQLException 
     */
    public static Collection<String> getAllStudentIds() throws SQLException {
        Collection<String> allIDs = new ArrayList<>();

        Connection conn = DriverManager.getConnection(url);
        STATEMENT = conn.prepareStatement("SELECT id FROM students");
        STATEMENT.executeQuery();
        ResultSet s = STATEMENT.getResultSet();
        
        while(s.next()) allIDs.add(s.getString(1));
        
        s.close();
        conn.close();
            
        return allIDs;
    }

    /**
     * Get all degree ids currently being used in the database.
     * @return
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_getAllDegreeIds (followed by optional numbers if multiple tests are used)
     * @throws SQLException 
     */
    public static Iterable<String> getAllDegreeIds() throws SQLException {
    	Collection<String> allIDs = new ArrayList<>();

        Connection conn = DriverManager.getConnection(url);
        STATEMENT = conn.prepareStatement("SELECT id FROM degrees");
        STATEMENT.executeQuery();
        ResultSet s = STATEMENT.getResultSet();
        
        while(s.next()) allIDs.add(s.getString(1));
        
        s.close();
        conn.close();
            
        return allIDs;
    }


}
