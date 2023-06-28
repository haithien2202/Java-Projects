import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class parseIF {
	static RobotProgramNode parse(Scanner s) {
        if (Check.checkFor("if", s)) {
            List<RobotConditionNode> conditions = new ArrayList<>();
            List<RobotBlockNode> blocks = new ArrayList<>();

            //Parse if and elifs
            do {
            	Check.require(Parser.OPENPAREN, "Missing opening bracket '('", s);

                conditions.add(parseCOND.parse(s));

                Check.require(Parser.CLOSEPAREN, "Missing ending bracket ')'", s);

                blocks.add(parseBLOCK.parse(s));
            } while (Check.checkFor("elif", s));

            //Check for optional else block
            RobotBlockNode elseBlock = RobotBlockNode.NULL;
            if (Check.checkFor("else", s)) {
                elseBlock = parseBLOCK.parse(s);
                if (elseBlock == null) elseBlock = RobotBlockNode.NULL;
            }

            if (s.hasNext("elif"))
            	Check.fail("elif must come before else", s);

            //Copy the else block so it can be passed into the anonymous class
            final RobotBlockNode finalElseBlock = elseBlock;
            return new RobotProgramNode() {
                public void execute(Robot r) {
                    for (int i = 0; i < conditions.size(); i++) {
                        if (conditions.get(i).execute(r)) {
                            RobotBlockNode block = blocks.get(i);
                            block.preExec(r);
                            block.execute(r);
                            block.postExec(r);
                            return;
                        }
                    }

                    finalElseBlock.preExec(r);
                    finalElseBlock.execute(r);
                    finalElseBlock.postExec(r);
                }

                public String toString() {
                    StringBuilder sb = new StringBuilder();
                    sb.append("if (");
                    sb.append(conditions.get(0));
                    sb.append(") ");
                    sb.append(blocks.get(0));

                    for (int i = 1; i < conditions.size(); i++) {
                        sb.append("if (");
                        sb.append(conditions.get(i));
                        sb.append(") ");
                        sb.append(blocks.get(i));
                    }

                    if (finalElseBlock != RobotBlockNode.NULL) {
                        sb.append("else ");
                        sb.append(finalElseBlock);
                    }

                    return sb.toString();
                }
            };
        }

        return null;
    }
}
