import java.util.Scanner;

public class parseEXP {
	 static RobotNumberNode parse(Scanner s) {
	        RobotNumberNode exp = buildEXP.build(s);
	        if (exp != null) return exp;

	        exp = parseSEN.parse(s);
	        if (exp != null) return exp;

	        exp = parseVAR.parse(s);
	        if (exp != null) return exp;

	        //Last case is parsing it as a number, which can fail if it isn't one
	        return parseNUM.parse(s);
	    }
}
