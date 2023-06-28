public interface RobotRelopNode {
    boolean execute(int a, int b);

    RobotRelopNode LT = new RobotRelopNode() {
        public boolean execute(int a, int b) {
            return a < b;
        }

        public String toString() {
            return "lt";
        }
    };

    RobotRelopNode EQ = new RobotRelopNode() {
        public boolean execute(int a, int b) {
            return a == b;
        }

        public String toString() {
            return "eq";
        }
    };
    
    RobotRelopNode GT = new RobotRelopNode() {
        public boolean execute(int a, int b) {
            return a > b;
        }

        public String toString() {
            return "gt";
        }
    };
}