import java.util.Scanner;

public class parseWHILE {
	   static RobotProgramNode parse(Scanner s) {
	        if (Check.checkFor("while", s)) {
	        	Check.require(Parser.OPENPAREN, "Missing opening bracket '('", s);

	            RobotConditionNode condition = parseCOND.parse(s);
	            if (condition == null) return null;

	            Check.require(Parser.CLOSEPAREN, "Missing ending bracket ')'", s);

	            RobotBlockNode block = parseBLOCK.parse(s);
	            if (block == null)  return null;

	            return new RobotProgramNode() {
	                public void execute(Robot r) {
	                    block.preExec(r);
	                    while (condition.execute(r)) block.execute(r);
	                    block.postExec(r);
	                }

	                public String toString() {
	                    return "while (" + condition + ") " + block;
	                }
	            };
	        }

	        return null;
	    }

}
