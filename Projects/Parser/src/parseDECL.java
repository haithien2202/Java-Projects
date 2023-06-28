import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class parseDECL {
	static RobotProgramNode parse(Scanner s) {
        if (Check.checkFor("vars", s)) {
            List<String> variables = new ArrayList<>();
            do {
                String var = Check.require(Parser.VARPAT, "Malformed variable!", s);
                if (var == null) return null;
                variables.add(var.substring(1));
            }
            while (Check.checkFor(",", s));

            Check.require(";", "Missing semicolon ';'", s);

            return new RobotProgramNode() {
                public void execute(Robot robot) {
                    for (String var : variables) if (robot.getVariable(var) != null) robot.setVariable(var, 0);
                }

                public String toString() {
                    return variables.stream().collect(Collectors.joining(", ", "vars ", ";\n"));
                }
            };
        }

        return null;
    }
}
