import java.util.Scanner;

public class parseSEN {
	 static RobotNumberNode parse(Scanner s) {
	        if (Check.checkFor("barrelLR", s)) {
	            //Optional argument
	            if (Check.checkFor(Parser.OPENPAREN, s)) {
	                RobotNumberNode exp = parseEXP.parse(s);
	                Check.require(Parser.CLOSEPAREN, "Missing ending bracket ')'", s);

	                return new RobotNumberNode() {
	                    public int execute(Robot robot) {
	                        return robot.getBarrelLR(exp.execute(robot));
	                    }

	                    public String toString() {
	                        return "barrelLR(" + exp + ");\n";
	                    }
	                };
	            }

	            return RobotNumberNode.BARREL_LR;
	        }
	        if (Check.checkFor("barrelFB", s)) {
	            //Optional argument
	            if (Check.checkFor(Parser.OPENPAREN, s)) {
	                RobotNumberNode exp = parseEXP.parse(s);
	                Check.require(Parser.CLOSEPAREN, "Missing opening bracket ')'", s);

	                return new RobotNumberNode() {
	                    public int execute(Robot robot) {
	                        return robot.getBarrelFB(exp.execute(robot));
	                    }

	                    public String toString() {
	                        return "barrelFB(" + exp + ");\n";
	                    }
	                };
	            }

	            return RobotNumberNode.BARREL_FB;
	        }
	        if (Check.checkFor("fuelLeft", s)) return RobotNumberNode.FUEL_LEFT;
	        else if (Check.checkFor("oppLR", s)) return RobotNumberNode.OPP_LR;
	        else if (Check.checkFor("oppFB", s)) return RobotNumberNode.OPP_FB;
	        else if (Check.checkFor("numBarrels", s)) return RobotNumberNode.NUM_BARRELS;
	        else if (Check.checkFor("wallDist", s)) return RobotNumberNode.WALL_DIST;
	        else return null;
	    }
}
