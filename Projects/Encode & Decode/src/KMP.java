import java.util.*;
import java.io.*;

public class KMP {
	
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

    
    private static int searchKMP(String text, String pattern, int[] table) {
        int pLen = pattern.length();
        int tLen = text.length();
        if (table.length == 0) return 0;

        int pPos = 0; 
        int tPos = 0;
        while (tPos + pPos < tLen) {
            if (pattern.charAt(pPos) == text.charAt(pPos + tPos)) { 
                pPos++;
                if (pPos == pLen) {
                	return tPos;
                }
            }
            else if (table[pPos] == -1) { 
                tPos += pPos + 1;
                pPos = 0;
            }
            else { 
                tPos += pPos - table[pPos];
                pPos = table[pPos];
            }
        }

        //No match
        return -1;
    }
    
    private static int search(String text, String pattern) {
    	 int[] table = new int[pattern.length()];
         
         if (pattern.length() != 0) {
         table[0] = -1;

         for (int pos = 1, cursor = 0; pos < table.length; pos++, cursor++) {
             if (pattern.charAt(pos) == pattern.charAt(cursor)) {
                 table[pos] = table[cursor];
             }
             else {
                 table[pos] = cursor;
                 cursor = table[cursor];

                 while (cursor >= 0 && pattern.charAt(pos) != pattern.charAt(cursor))
                     cursor = table[cursor];
             }
         	}
         }
         return searchKMP(text,pattern,table);
    }
}
