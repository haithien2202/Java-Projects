import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Articulation {
	private static int subTrees;
    private static Set<Node> AP = new HashSet<>();
    
	public static Set<Node> search(Node firstNode){
		firstNode.setCount(0);
		firstNode.setCheck(true);
        subTrees = 0;	
        for (Node neighbor : firstNode.getNeighborNodes()) {
            if (neighbor.getCount() != Integer.MAX_VALUE) continue;
            	iterAP(neighbor, 1, firstNode);
            subTrees++;
        }
        if (subTrees > 1)
            AP.add(firstNode);
        return AP;
	}
	
	 public static void iterAP(Node node, int count, Node root) {
		//create fringe and add the first node into fringe
		 	Stack<Tuple> fringe = new Stack<Tuple>(); 	
	        fringe.push(new Tuple(node, count, root));
	        while (!fringe.isEmpty()) {
	            Tuple currentNode = fringe.peek();
			 	currentNode.parent.setCheck(true);
			 	// if this node haven't been visited, do this
	            if (currentNode.node.getCount() == Integer.MAX_VALUE) {
	            	currentNode.node.setReachBack(currentNode.count);
	                currentNode.node.setCount(currentNode.count);   
	                // add children into current tuple
	                currentNode.children = (ArrayList<Node>) currentNode.node.getNeighborNodes().stream().filter(thisNode -> (thisNode != currentNode.parent && thisNode != null)).collect(Collectors.toList());
	            // if this node had been visited b4, and children is not null, do this
	            } else if (!currentNode.children.isEmpty()) {
	                Node child = currentNode.children.remove(0);
	                // if child had been visited, set reach back.
	                if (child.getCount() < Integer.MAX_VALUE) currentNode.node.setReachBack(Math.min(child.getCount(), currentNode.node.getReachBack()));
	                // if child had not been visited, add child into fringe
	                else fringe.push(new Tuple(child, currentNode.count + 1, currentNode.node));
	             // if this node had been visited and children is null, do this
	            } else {
	            	//if node is not equal first node, do this
	                if (currentNode.node != node) {
	                    currentNode.parent.setReachBack(Math.min(currentNode.node.getReachBack(), currentNode.parent.getReachBack()));
	                    // if current node reach back > or = parent depth, it is an articulation point
	                    if (currentNode.node.getReachBack() >= currentNode.parent.getCount()) AP.add(currentNode.parent);
	                }
	                // remove fringe when finished
	                fringe.pop();
	            }
	        }
	    }
	 
	 private static class Tuple {
			Node node;
			Node parent;
			int count;
			ArrayList<Node> children;
			
			public Tuple(Node node, int count, Node parent) {
		        this.node = node;
		        this.parent = parent;
		        this.count = count;
		    }
		}
}