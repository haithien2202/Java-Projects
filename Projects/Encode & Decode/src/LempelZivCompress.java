import java.util.*;
import java.io.*;

public class LempelZivCompress {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please call this program with one argument which is the input file name.");
        } else {
            try {
                Scanner s = new Scanner(new File(args[0]));

                // Read the entire file into one String.
                StringBuilder fileText = new StringBuilder();
                while (s.hasNextLine()) {
                    fileText.append(s.nextLine() + "\n");
                }

                System.out.println(compress(fileText.toString()));
            } catch (FileNotFoundException e) {
                System.out.println("Unable to find file called " + args[0]);
            }
        }
    }
    
     private static String compressLempelZiv(String input) {
    	int inputSize = input.length();
        int cursor = 0;
        
        StringBuilder output = new StringBuilder();

        while (cursor < inputSize) {
            int length = 0;
            int position = 0;
            int start = Math.max(cursor - 200, 0);

            String str = input.substring(start, cursor);

            int currentPos;
            while (cursor + length < inputSize - 1 && (currentPos = str.indexOf(input.substring(cursor, cursor + length + 1))) >= 0) {
                position = currentPos;
                length++;
            }

            if (length > 0) {
                output.append('[');
                output.append(str.length() - position);
                output.append('|');
                output.append(length);
                output.append('|');
                output.append(input.charAt(cursor + length));
                output.append(']');
            }
            else {
                output.append("[0|0|");
                output.append(input.charAt(cursor));
                output.append(']');
            }

            cursor += length + 1;
        }

        return output.toString();
    }
     
     /**
      * Take uncompressed input as a text string, compress it, and return it as a
      * text string.
      */
     public static String compress(String input) {
          return compressLempelZiv(input);
     }
}
