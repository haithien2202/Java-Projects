import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * This represents the data structure storing all the roads, nodes, and
 * segments, as well as some information on which nodes and segments should be
 * highlighted.
 * 
 * @author tony
 */
public class Graph {
	// map node IDs to Nodes.
	Map<Integer, Node> nodes = new HashMap<>();
	// map road IDs to Roads.
	Map<Integer, Road> roads;
	// just some collection of Segments.
	Collection<Segment> segments;
	Set<Node> trafficLights;

	Node highlightedNode;
	Collection<Road> highlightedRoads = new HashSet<>();
	ASearch.SEARCH_FILTER filter = ASearch.SEARCH_FILTER.DISTANCE; 
	ArrayList<ASearch.RESTRICTIONS> restrictions = new ArrayList<>();
	
	Node ORIGIN;
	Node DESTINATION;
	Set<Node> artPoints = new HashSet<Node>();
	ArrayList<Segment> hSegment = new ArrayList<Segment>();
	boolean isValid = false;
	boolean APON = false;
	boolean lightCheck = false;

	public Graph(File nodes, File roads, File segments, File polygons, File TrafficLight) {
		this.nodes = Parser.parseNodes(nodes, this);
		this.roads = Parser.parseRoads(roads, this);
		this.segments = Parser.parseSegments(segments, this);
		this.trafficLights = Parser.parseLight(TrafficLight, this);
	}

	public void draw(Graphics g, Dimension screen, Location origin, double scale) {
		// a compatibility wart on swing is that it has to give out Graphics
		// objects, but Graphics2D objects are nicer to work with. Luckily
		// they're a subclass, and swing always gives them out anyway, so we can
		// just do this.
		Graphics2D g2 = (Graphics2D) g;

		// draw all the segments.
		g2.setColor(Mapper.SEGMENT_COLOUR);
		for (Segment s : segments)
			s.draw(g2, origin, scale);

		// draw the segments of all highlighted roads. 	
		g2.setStroke(new BasicStroke(1));
		for (int n : roads.keySet()) {
			Road road = roads.get(n);
			for (Segment seg : road.components) {
				g2.setColor(Mapper.SEGMENT_COLOUR);
				if (road.noPede == 1)g2.setColor(Color.MAGENTA);
				if (road.noBike == 1)g2.setColor(Color.CYAN);
				if (road.noCar == 1)g2.setColor(Color.YELLOW.darker());
				if (road.oneWay == 1)g2.setColor(Color.RED.brighter());
				seg.draw(g2, origin, scale);
			}
		}
		g2.setStroke(new BasicStroke(3));
		g2.setColor(Mapper.HIGHLIGHT_COLOUR);
		for (Segment segment : hSegment) {
			segment.draw(g2, origin, scale);
		}

		// draw all the nodes.
		g2.setColor(Mapper.NODE_COLOUR);
		for (Node n : nodes.values())
			n.draw(g2, screen, origin, scale);

		// draw the highlighted node, if it exists.
		if (highlightedNode != null) {
			g2.setColor(Mapper.HIGHLIGHT_COLOUR);
			highlightedNode.draw(g2, screen, origin, scale);
		}
		if (lightCheck) {
			for (Node c : trafficLights) {
				g2.setColor(Color.RED);
				c.draw(g2, screen, origin, scale);
			}
		}
		if (ORIGIN != null) {
			g2.setColor(Color.GREEN);
			ORIGIN.draw(g2, screen, origin, scale);
		}
		if (DESTINATION != null) {
			g2.setColor(Color.YELLOW.darker());
			DESTINATION.draw(g2, screen, origin, scale);
		}
		if (APON == true)
			if (!artPoints.isEmpty()) {
				for (Node current : artPoints) {
					g2.setColor(Color.RED);
					current.draw(g2, screen, origin, scale);
				}
			}
	}
	
	private void search() {
		ArrayList<Node> nodes = ASearch.search(this, ORIGIN, DESTINATION, filter, restrictions);
		// ArrayList<Node> nodes = dijkstra.search(this, originNode,HEURESTIC_FUNCTION destinationNode);
		hSegment.clear();
		if (nodes == null) {
			isValid = false;
			return;
		}else isValid = true;
		for (int i = 0; i < nodes.size() - 1; i++) {
			highlightSegment(nodes.get(i), nodes.get(i + 1));
		}
	}

	private void highlightSegment(Node start, Node end) {
		for (Segment segment : segments) {
			if ((segment.start == start && segment.end == end) || (segment.start == end && segment.end == start)) {
				hSegment.add(segment);
				return;
			}
		}
	}

	public void setHighlight(Node node) {
		this.highlightedNode = node;
	}

	public void setHighlight(Collection<Road> roads) {
		this.highlightedRoads = roads;
	}
	
	public Collection<Segment> getSegments() {
		return segments;
	}
	
	public void setFilter(ASearch.SEARCH_FILTER f) {
		filter = f;
	}
	
	public void addRestriction(ASearch.RESTRICTIONS r) {
		restrictions.add(r);
	}
	
	public void removeRestriction(ASearch.RESTRICTIONS r) {
		restrictions.remove(r);
	}
	
	public void setOriginNode(Node thisNode) {
		clearHighlighted();
		this.ORIGIN = thisNode;
		setHighlight(new ArrayList<>());
	}
	
	public void setDestinationNode(Node thisNode) {
		this.DESTINATION = thisNode;
		search();
	}
	
	private void clearHighlighted() {
		hSegment.clear();
		ORIGIN = null;
		DESTINATION = null;
	}
}

// code for COMP261 assignments