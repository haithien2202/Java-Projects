import java.util.*;
import java.io.*;

public class HuffmanCoding {
	private static HashMap<Character, String> encodingTable = new HashMap<>();
    private static HashMap<String, Character> decodingTable = new HashMap<>();

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please call this program with two arguments, which are " +
                               "the input file name and either 0 for constructing tree and printing it, or " +
                               "1 for constructing the tree and encoding the file and printing it, or " +
                               "2 for constructing the tree, encoding the file, and then decoding it and " +
                               "printing the result which should be the same as the input file.");
        } else {
            try {
                Scanner s = new Scanner(new File(args[0]));

                // Read the entire file into one String.
                StringBuilder fileText = new StringBuilder();
                while (s.hasNextLine()) {
                    fileText.append(s.nextLine() + "\n");
                }
                
                if (args[1].equals("0")) {
                    System.out.println(constructTree(fileText.toString()));
                } else if (args[1].equals("1")) {
                    constructTree(fileText.toString()); // initialises the tree field.
                    System.out.println(encode(fileText.toString()));
                } else if (args[1].equals("2")) {
                    constructTree(fileText.toString()); // initialises the tree field.
                    String codedText = encode(fileText.toString());
                     // DO NOT just change this code to simply print fileText.toString() back. ;-)
                    System.out.println(decode(codedText));
                } else {
                    System.out.println("Unknown second argument: should be 0 or 1 or 2");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Unable to find file called " + args[0]);
            }
        }
    }

    
    public static HashMap<Character, Node> findFreq (String text){
    	HashMap<Character, Node> counts = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            char chars = text.charAt(i);
            if (counts.containsKey(chars)) {
                counts.get(chars).freq = counts.get(chars).freq + 1;
            }
            else {
                counts.put(chars, new Node(chars, 1));
            }
        }
        return counts;
    }

    /**
     * This would be a good place to compute and store the tree.
     */
    public static Map<Character, String> constructTree(String text) {
         PriorityQueue<Node> queue = new PriorityQueue<>();
         queue.addAll(findFreq(text).values());
         while (queue.size() > 1) { 
        	 Node left = queue.poll();
        	 char temporaryHighest;
        	 Node right = queue.poll();
             if ((left.higherChar - right.higherChar) < 0) {
            	 temporaryHighest = left.higherChar;
             }else {
            	 temporaryHighest = right.higherChar;
             }
             Node parent = new Node('\0', left.freq + right.freq);
             parent.higherChar = temporaryHighest; 
             parent.left = left;
             parent.right = right;

             queue.offer(parent);
         }

         Node root = queue.poll();

         //Assign the codes to each node in the tree
         Deque<Node> nodes = new ArrayDeque<>();
         nodes.add(root);
         while (!nodes.isEmpty()) {
        	 Node currentNode = nodes.poll();

             if (currentNode.character != '\0') {
                 StringBuilder sb = new StringBuilder();
                 currentNode.code.forEach(sb::append);
                 String code = sb.toString();

                 encodingTable.put(currentNode.character, code);
                 decodingTable.put(code, currentNode.character);
             }
             else {
            	 currentNode.left.code.addAll(currentNode.code);
            	 currentNode.left.code.add('0');
            	 currentNode.right.code.addAll(currentNode.code);
            	 currentNode.right.code.add('1');
             }

             if (currentNode.left != null) {
            	 nodes.offer(currentNode.left);
             }
             if (currentNode.right != null) {
            	 nodes.offer(currentNode.right);
             }
         }
         return encodingTable;
     }
    
    /**
     * Take an input string, text, and encode it with the tree computed from the text. Should
     * return the encoded text as a binary string, that is, a string containing
     * only 1 and 0.nodeStack
     */
    public static String encode(String text) {
    	 constructTree(text);
    	 StringBuilder sb = new StringBuilder();
         for (int i = 0; i < text.length(); i++) {
        	 sb.append(encodingTable.get(text.charAt(i)));
         }

         return sb.toString();
    }
    
    /**
     * Take encoded input as a binary string, decode it using the stored tree,
     * and return the decoded text as a text string.
     */
    public static String decode(String encoded) {
    	  StringBuilder output = new StringBuilder();

          int i = 0;
          while (i < encoded.length()) {
              StringBuilder charBuilder = new StringBuilder();
              while (!decodingTable.containsKey(charBuilder.toString()))  charBuilder.append(encoded.charAt(i++));

              output.append(decodingTable.get(charBuilder.toString()));
          }

          return output.toString();
    }
    
    private static class Node implements Comparable<Node> {
    	public Node	right = null;
        public Node left = null;
        public ArrayList<Character> code = new ArrayList<>();
        public char character;
        public char higherChar;
        public int freq;
        public Node(char character, int frequency) {
            this.character = character;
            this.higherChar = character;
            this.freq = frequency;
        }

        public int compareTo(Node o) {
        	if (this.freq == o.freq) {
        		return Character.compare(this.higherChar, o.higherChar);
        	}
        	else {
        		return this.freq - o.freq;
        	}
        }
    }
}