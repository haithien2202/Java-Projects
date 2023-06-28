import java.util.Scanner;

public class parseASSGN {
	static RobotProgramNode parse(Scanner s) {
        if (s.hasNext(Parser.VARPAT)) {
            String n = s.next().substring(1);

            Check.require("=", "Missing equal sign '='", s);

            RobotNumberNode v = parseEXP.parse(s);

            Check.require(";", "Missing semicolon ';'", s);

            return new RobotProgramNode() {
                public void execute(Robot robot) {
                    robot.setVariable(n, v.execute(robot));
                }

                public String toString() {
                    return "$" + n + " = " + v + ";\n";
                }
            };
        }

        return null;
    }
}
