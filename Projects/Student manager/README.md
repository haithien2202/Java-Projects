# SWEN301 Assignment 1<br>
Name: Thien Tran<br>
ID: 300503987<br>

b) Plugins were added into pom.xml file. Spring-boot-maven plugin allows packaging executable jar file.<br>
To generate standalone CLI application, we use 'mvn package' in the project base directory of assignment-1.<br>
To use standalone CLI application, we:<br>
*Redirect the directory into target folder.<br>
*Then we use 'java -jar studentfinder.jar ' + with the id we want to find.<br>
For instance, 'java -jar studentfinder.jar id24' where id24 is the student id.<br>
*It should compile and give the result on terminal.<br>
Student details will be listed: ID, First Name, Last Name, Degree.<br>
<br>
c) Discuss (less than 100 words) whether your design is prone to memory leaks by interfering with Garbage Collection and how this has been or could be addressed.
Static methods increase the risk of memory leaks. In my application, StudentManager.java is a static class. Therefore, it all relies on static members, resulting 
in increasing the likelihood of memory leaks. This can be problematic since static members are kept in memory throughout the application. The fields in the class 
must also be static and cannot be addressed. My solution to the memory leak problem was to close the connections to the databases that were opened for every method 
call since those connections were also kept in memory when running the program.