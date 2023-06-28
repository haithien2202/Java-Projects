import java.util.HashMap;
import java.util.Map;

class TrieNode {
    char c;
    Map<Character, TrieNode> children = new HashMap<Character, TrieNode>();
    boolean isLeaf;
 
    public TrieNode() {}
 
    public TrieNode(char character){
        this.c = character;
    }
    
    public char getChar(){
        return c;
    }
    
    public boolean isLeaf() {
    	return isLeaf;
    }
    
    public Map<Character, TrieNode> getChildrenMap(){
    	return children;
    }
}