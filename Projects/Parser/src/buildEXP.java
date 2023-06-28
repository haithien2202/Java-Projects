import java.util.Scanner;

public class buildEXP {
	 static RobotNumberNode build(Scanner s) {
	        String t = null;
	        if (Check.checkFor("add", s))      t = "add";
	        else if (Check.checkFor("sub", s)) t = "sub";
	        else if (Check.checkFor("mul", s)) t = "mul";
	        else if (Check.checkFor("div", s)) t = "div";

	        if (t != null) {
	        	Check.require(Parser.OPENPAREN, "Missing opening bracket '('", s);

	            RobotNumberNode var1 = parseEXP.parse(s);
	            if (var1 == null) return null;

	            Check.require(",", "Missing colon ','", s);

	            RobotNumberNode var2 = parseEXP.parse(s);
	            if (var2 == null) return null;

	            Check.require(Parser.CLOSEPAREN, "Missing ending bracket ')'", s);

	            switch (t) {
	                case "add":
	                    return new RobotNumberNode() {
	                        public int execute(Robot r) {
	                            return var1.execute(r) + var2.execute(r);
	                        }

	                        public String toString() {
	                            return "(" + var1 + " + " + var2 + ")";
	                        }
	                    };
	                case "sub":
	                    return new RobotNumberNode() {
	                        public int execute(Robot r) {
	                            return var1.execute(r) - var2.execute(r);
	                        }

	                        public String toString() {
	                            return "(" + var1 + " - " + var2 + ")";
	                        }
	                    };
	                case "mul":
	                    return new RobotNumberNode() {
	                        public int execute(Robot r) {
	                            return var1.execute(r) * var2.execute(r);
	                        }

	                        public String toString() {
	                            return "(" + var1 + " * " + var2 + ")";
	                        }
	                    };
	                case "div":
	                    return new RobotNumberNode() {
	                        public int execute(Robot r) {
	                            return var1.execute(r) / var2.execute(r);
	                        }

	                        public String toString() {
	                            return "(" + var1 + " / " + var2 + ")";
	                        }
	                    };
	            }
	        }

	        return null;
	    }
}
