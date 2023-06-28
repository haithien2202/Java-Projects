import java.util.Scanner;

public class parseSTMT {
	static RobotProgramNode parse(Scanner s) {
        //Try to parse any of the possible things it could be
		        RobotProgramNode stmt = ParseACT.parse(s);
		        if (stmt != null) {
		        	Check.require(";", "Missing semicolon ';'", s);
		            return stmt;
		        }

		        stmt = parseLOOP.parse(s);
		        if (stmt != null) return stmt;

		        stmt = parseIF.parse(s);
		        if (stmt != null) return stmt;

		        stmt = parseWHILE.parse(s);
		        if (stmt != null) return stmt;

		        stmt = parseASSGN.parse(s);
		        if (stmt != null) return stmt;

		        //If it doesn't match anything
		        return null;
		    }
}
