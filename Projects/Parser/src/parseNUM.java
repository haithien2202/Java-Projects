import java.util.Scanner;

public class parseNUM {
	static RobotNumberNode parse(Scanner s) {
        int i = Check.requireInt(Parser.NUMPAT, "Malformed number or variable!", s);
        return new RobotNumberNode() {
            public int execute(Robot robot) {
                return i;
            }

            public String toString() {
                return Integer.toString(i);
            }
        };
    }
}
