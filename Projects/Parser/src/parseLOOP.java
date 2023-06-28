import java.util.Scanner;

public class parseLOOP {
	 static RobotProgramNode parse(Scanner s) {
	        if (Check.checkFor("loop", s)) {
	            RobotBlockNode block = parseBLOCK.parse(s);
	            if (block == null) return null;

	            return new RobotProgramNode() {
	                public void execute(Robot r) {
	                    block.preExec(r);
	                    while (r.getFuel() > 0) block.execute(r);
	                    block.postExec(r);
	                }

	                public String toString() {
	                    return "loop " + block;
	                }
	            };
	        }

	        return null;
	    }
}
