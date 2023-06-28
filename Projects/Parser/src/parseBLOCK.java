import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class parseBLOCK {
	static RobotBlockNode parse(Scanner s) {
        Check.require(Parser.OPENBRACE, "Missing '{'", s);

        List<RobotProgramNode> childrenList = new ArrayList<>();

        RobotProgramNode decl = parseDECL.parse(s);
        if (decl != null)  childrenList.add(decl);

        while (!s.hasNext(Parser.CLOSEBRACE)) {
            RobotProgramNode n = parseSTMT.parse(s);
            if (n != null) childrenList.add(n);
            else if (!s.hasNext(Parser.CLOSEBRACE)) Check.fail("Missing '}'", s);
            else Check.fail("Invalid stmt", s);
        }

        Check.require(Parser.CLOSEBRACE, "Missing '}'", s);

        if (childrenList.isEmpty()) {
            Check.fail("Block must have at least one child", s);
            return null;
        }

        return new RobotBlockNode() {
            Set<String> declaredVars = null;

            public void preExec(Robot robot) {
                declaredVars = robot.getDeclaredVariables();
            }

            public void execute(Robot robot) {
                for (RobotProgramNode child : childrenList) child.execute(robot);
            }

            public void postExec(Robot robot) {
                if (declaredVars != null) {
                    Set<String> newVariables = robot.getDeclaredVariables();
                    if (newVariables.removeAll(declaredVars))  for (String variable : newVariables)  robot.deleteVariable(variable);
                }
            }
                public String toString() {
                    StringBuilder sb = new StringBuilder();
                    sb.append("{\n");
                    for (RobotProgramNode child : childrenList) sb.append(child.toString());
                    sb.append("}\n");
                    return sb.toString();
                }
            };
	}
}
        
