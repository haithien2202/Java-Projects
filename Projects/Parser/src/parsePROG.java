import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class parsePROG {
	static RobotProgramNode parse(Scanner s) {
	        RobotProgramNode decl = parseDECL.parse(s);

	        //Build the list of child nodes
	        List<RobotProgramNode> children = new ArrayList<>();

	        if (decl != null) children.add(decl);

	        while (s.hasNext()) {
	            RobotProgramNode n = parseSTMT.parse(s);
	            if (n != null) children.add(n);
	            else Check.fail("Malformed STMT!", s);
	        }

	        return new RobotProgramNode() {
	            public void execute(Robot robot) {
	                for (RobotProgramNode child : children) child.execute(robot);
	            }

	            public String toString() {
	                StringBuilder sb = new StringBuilder();
	                for (RobotProgramNode child : children)  sb.append(child.toString());
	                return sb.toString();
	            }
	        };
	    }
}
