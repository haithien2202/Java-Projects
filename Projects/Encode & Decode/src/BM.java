import java.util.*;
import java.io.*;

public class BM {
	
	static int NO_OF_CHARS = 256;
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please call this program with " +
                               "two arguments which is the input file name " +
                               "and the string to search.");
        } else {
            try {
                Scanner s = new Scanner(new File(args[0]));

                // Read the entire file into one String.
                StringBuilder fileText = new StringBuilder();
                while (s.hasNextLine()) {
                    fileText.append(s.nextLine() + "\n");
                }

                System.out.println(search(fileText.toString(), args[1]));
            } catch (FileNotFoundException e) {
                System.out.println("Unable to find file called " + args[0]);
            }
        }
    }
    
    public static int[] buildCharTable(String pattern) {
        final int pLen = pattern.length();
        int[] table = new int[NO_OF_CHARS];

        for (int i = 0; i < table.length; i++) table[i] = pLen;

        for (int i = 0; i < pLen - 1; i++) table[pattern.charAt(i)] = pLen - 1 - i;

        return table;
    }


    private static boolean findPrefix(String pattern, int pos) {
        for (int i = pos, j = 0; i < pattern.length(); i++, j++) if (pattern.charAt(i) != pattern.charAt(j)) return false;
        return true;
    }


    private static int findSuffix(String pattern, int pos) {
        int sLen = 0;
        for (int i = pos, j = pattern.length() - 1; i >= 0; i--, j--) {
        	if (pattern.charAt(i) != pattern.charAt(j)) break;
            sLen++;
        }
        return sLen;
    }

    
    private static int[] buildJumpTable(String pattern) {
        final int patternLength = pattern.length();
        int[] table = new int[patternLength];

        int lastPos = patternLength;

        //Backwards prefix search
        for (int i = patternLength; i > 0; i--) {
            if (findPrefix(pattern, i)) lastPos = i;
            table[patternLength - i] = lastPos - i + patternLength;
        }

        for (int i = 0; i < patternLength - 1; i++) {
            table[findSuffix(pattern, i)] = patternLength - 1 - i + findSuffix(pattern, i);
        }

        return table;
    }

    
    private static int search(String text, String pattern) {
    	int pLen = pattern.length();
        int tLen = text.length();
        
        int[] jumpTable = buildJumpTable(pattern);
        int[] charTable =  buildCharTable(pattern);
        
        //Empty pattern
        if (pLen == 0) return 0;

        int tPos = pLen - 1;
        int pPos;

        while (tPos < tLen) {
            for (pPos = pLen - 1; pattern.charAt(pPos) == text.charAt(tPos); pPos--, tPos--) if (pPos == 0) return tPos;
            tPos += Math.max(jumpTable[pLen - 1 - pPos], charTable[text.charAt(tPos)]);
        }

        return -1;
    }  
}
