import java.util.Scanner;

class ParseACT {
static RobotProgramNode parse(Scanner s) {
        //A whole bunch of if statements switching through different actions the robot can do

        if (Check.checkFor("move", s)) {
            //Has argument
            if (Check.checkFor(Parser.OPENPAREN, s)) {
                RobotNumberNode exp = parseEXP.parse(s);
                Check.require(Parser.CLOSEPAREN, "Missing ending bracket ')'", s);

                return new RobotProgramNode() {
                    public void execute(Robot robot) {
                        int dist = exp.execute(robot);
                        for (int i = 0; i < dist; i++) robot.move();
                    }

                    public String toString() {
                        return "move(" + exp + ");\n";
                    }
                };
            }

            return new RobotProgramNode() {
                public void execute(Robot robot) {
                    robot.move();
                }

                public String toString() {
                    return "move;\n";
                }
            };
        }
        if (Check.checkFor("wait", s)) {
            //Has argument
            if (Check.checkFor(Parser.OPENPAREN, s)) {
                RobotNumberNode exp = parseEXP.parse(s);
                Check.require(Parser.CLOSEPAREN, "Missing ending bracket ')'", s);

                return new RobotProgramNode() {
                    public void execute(Robot robot) {
                        int dist = exp.execute(robot);
                        for (int i = 0; i < dist; i++) robot.idleWait();
                    }
                    public String toString() {
                        return "wait(" + exp + ");\n";
                    }
                };
            }

            return new RobotProgramNode() {
                public void execute(Robot robot) {
                    robot.idleWait();
                }

                public String toString() {
                    return "wait;\n";
                }
            };
        }
        
        if (Check.checkFor("turnR", s))
            return new RobotProgramNode() {
                public void execute(Robot robot) {
                    robot.turnRight();
                }

                public String toString() {
                    return "turnR;\n";
                }
            };
            
        if (Check.checkFor("turnL", s))
            return new RobotProgramNode() {
                public void execute(Robot robot) {
                    robot.turnLeft();
                }

                public String toString() {
                    return "turnL;\n";
                }
            };
 
         if (Check.checkFor("takeFuel", s))
               return new RobotProgramNode() {
                   public void execute(Robot robot) {
                       robot.takeFuel();
                   }
                   public String toString() {
                       return "takeFuel;\n";
                   }
          };    
        if (Check.checkFor("turnAround", s))
            return new RobotProgramNode() {
                public void execute(Robot robot) {
                    robot.turnAround();
                }

                public String toString() {
                    return "turnAround;\n";
                }
            };
        if (Check.checkFor("shieldOn", s))
            return new RobotProgramNode() {
                public void execute(Robot robot) {
                    robot.setShield(true);
                }

                public String toString() {
                    return "shieldOn;\n";
                }
            };
        if (Check.checkFor("shieldOff", s))
            return new RobotProgramNode() {
                public void execute(Robot robot) {
                    robot.setShield(false);
                }

                public String toString() {
                    return "shieldOff;\n";
                }
            };
       

        return null;
    }
}