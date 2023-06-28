import java.util.*;
import java.io.*;

public class LempelZivDecompress {

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

                System.out.println(decompress(fileText.toString()));
            } catch (FileNotFoundException e) {
                System.out.println("Unable to find file called " + args[0]);
            }
        }
    }
    
    private static String decompressLempelZiv(String input) {
    	StringBuilder output = new StringBuilder();
		int cursor = 0;
		String [] compressArray = input.split("]");
		for(int i = 0; i < compressArray.length - 1; i++){
			String tuple  = compressArray[i].substring(1);
			String[] tupleArr = tuple.split("\\|");
			int off = Integer.valueOf(tupleArr[0]);
			int length = Integer.valueOf(tupleArr[1]);
			if(off == 0 || length == 0) {
				cursor++;
			} else {
				String reachBack = output.substring(cursor - off, cursor - off + length);
				output.append(reachBack);
				cursor+=length + 1;
			}
			output.append(tupleArr[2]);
		}
		return output.toString();
    }
    
    /**
     * Take compressed input as a text string, decompress it, and return it as a
     * text string.
     */
    public static String decompress(String compressed) {
    	return decompressLempelZiv(compressed);
    }
}
