import java.util.Scanner;

public class parseCOMPARE {
    static RobotRelopNode parse(Scanner s) {
        if (Check.checkFor("lt", s)) return RobotRelopNode.LT;
        else if (Check.checkFor("eq", s)) return RobotRelopNode.EQ;
        else if (Check.checkFor("gt", s)) return RobotRelopNode.GT;   
        else return null;
    }

}
