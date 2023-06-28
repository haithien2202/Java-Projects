import java.util.Scanner;

public class parseCOND {
	 static RobotConditionNode parse(Scanner s) {
	        RobotRelopNode comp = parseCOMPARE.parse(s);
	        if (comp == null) {
	            RobotConditionNode cond = buildCOND.build(s);
	            if (cond == null)
	            	Check.fail("Unknown condition", s);

	            return cond;
	        }

	        Check.require(Parser.OPENPAREN, "Missing opening bracket '('", s);

	        RobotNumberNode argA = parseEXP.parse(s);
	        if (argA == null)
	            return null;

	        Check.require(",", "Missing colon ','", s);

	        RobotNumberNode argB = parseEXP.parse(s);
	        if (argB == null)
	            return null;

	        Check.require(Parser.CLOSEPAREN, "Missing ending bracket ')'", s);

	        final RobotNumberNode a = argA, b = argB;
	        return new RobotConditionNode() {
	            public boolean execute(Robot r) {
	                return comp.execute(a.execute(r), b.execute(r));
	            }

	            public String toString() {
	                return comp.toString() + "(" + a + ", " + b + ")";
	            }
	        };
	    }
}
