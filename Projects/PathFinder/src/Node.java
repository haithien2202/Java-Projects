import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Node represents an intersection in the road graph. It stores its ID and its
 * location, as well as all the segments that it connects to. It knows how to
 * draw itself, and has an informative toString method.
 * 
 * @author tony
 */
public class Node {

	public final int nodeID;
	public final Location location;
	public final Collection<Segment> segments;
	private ArrayList<Node> neighborNodes = new ArrayList<>();
	private boolean visited = false;
	private int count = Integer.MAX_VALUE;
	private int reachBack;
	private boolean atCheck;
	private int trafficLight = 1;

	public Node(int nodeID, double lat, double lon) {
		this.nodeID = nodeID;
		this.location = Location.newFromLatLon(lat, lon);
		this.segments = new HashSet<Segment>();
	}

	public void addSegment(Segment seg) {
		segments.add(seg);
	}
	
	public void addNeighbours() {
		for (Segment current : this.segments) {
			Node n = current.end == this ? current.start : current.end;
			neighborNodes.add(n);
		}
	}

	public void draw(Graphics g, Dimension area, Location origin, double scale) {
		Point p = location.asPoint(origin, scale);

		// for efficiency, don't render nodes that are off-screen.
		if (p.x < 0 || p.x > area.width || p.y < 0 || p.y > area.height)
			return;

		int size = (int) (Mapper.NODE_GRADIENT * Math.log(scale) + Mapper.NODE_INTERCEPT);
		g.fillRect(p.x - size / 2, p.y - size / 2, size, size);
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setVisited(boolean b) {
		this.visited = b;
	}


	public String toString() {
		Set<String> edges = new HashSet<String>();
		for (Segment s : segments) {
			if (!edges.contains(s.road.name))
				edges.add(s.road.name);
		}

		String str = "ID: " + nodeID + "  loc: " + location + "\nroads: ";
		for (String e : edges) {
			str += e + ", ";
		}
		return str.substring(0, str.length() - 2);
	}

	public void setCount(int i) {
		count = i;
	}

	public int getCount() {
		return count;
	}

	public void setReachBack(int i) {
		reachBack = i;
	}
	
	public int getReachBack() {
		return reachBack;
	}
	
	public boolean getCheck() {
		return atCheck;
	}
	
	public void setCheck(boolean m) {
		atCheck = m;
	}
	
	public ArrayList<Node> getNeighborNodes(){
		return this.neighborNodes;
	}
	
	public void setTrafficLight() {
		trafficLight = 5;
	}
	
	public int getTrafficLight() {
		return trafficLight;
	}
}

// code for COMP261 assignments