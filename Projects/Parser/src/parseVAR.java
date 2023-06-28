import java.util.Scanner;

public class parseVAR {
	 static RobotNumberNode parse(Scanner s) {
	        //If it looks like a variable
	        if (s.hasNext(Parser.VARPAT)) {
	            String name = s.next().substring(1);

	            return new RobotNumberNode() {
	                public int execute(Robot robot) {
	                    Integer value = robot.getVariable(name);
	                    if (value != null)  return value;
	                    else {
	                        robot.cancel();
	                        throw new RuntimeException("$" + name + " has not yet been initialised or is out of scope!");
	                    }
	                }

	                public String toString() {
	                    return "$" + name;
	                }
	            };
	        }

	        return null;
	    }
}
