import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * This is the main class for the mapping program. It extends the GUI abstract
 * class and implements all the methods necessary, as well as having a main
 * function.
 * 
 * @author tony
 */
public class Mapper extends GUI {
	public static final Color NODE_COLOUR = new Color(77, 113, 255);
	public static final Color SEGMENT_COLOUR = new Color(130, 130, 130);
	public static final Color HIGHLIGHT_COLOUR = new Color(255, 219, 77);
	private static final DecimalFormat df = new DecimalFormat("0.00");

	// these two constants define the size of the node squares at different zoom
	// levels; the equation used is node size = NODE_INTERCEPT + NODE_GRADIENT *
	// log(scale)
	public static final int NODE_INTERCEPT = 1;
	public static final double NODE_GRADIENT = 0.8;

	// defines how much you move per button press, and is dependent on scale.
	public static final double MOVE_AMOUNT = 100;
	// defines how much you zoom in/out per button press, and the maximum and
	// minimum zoom levels.
	public static final double ZOOM_FACTOR = 1.3;
	public static final double MIN_ZOOM = 1, MAX_ZOOM = 200;

	// how far away from a node you can click before it isn't counted.
	public static final double MAX_CLICKED_DISTANCE = 0.15;

	// these two define the 'view' of the program, ie. where you're looking and
	// how zoomed in you are.
	private Location origin;
	private double scale;

	// our data structures.
	private Graph graph;
	private Trie trie;
	private boolean isLoaded = false;
	public static double distance = 0;

	@Override
	protected void redraw(Graphics g) {
		if (graph != null)
			graph.draw(g, getDrawingAreaDimension(), origin, scale);
	}

	@Override
	protected Point onDrag(MouseEvent e, double x , double y ) {
		if (isLoaded != true) return null;
		//unselectQuad();
		int mouseX = e.getX();
		int mouseY = e.getY();
		double difX = mouseX - x;
		double difY = mouseY - y;
		origin = origin.moveBy(-difX / scale, difY / scale);
		Point m = new Point(mouseX,mouseY);
		return m;
	}
	
	@Override
	protected void onScroll(boolean positive) {
		if (isLoaded) {
		//unselectQuad();
		if (positive == true) {
			onMove(Move.ZOOM_IN);
		}else onMove(Move.ZOOM_OUT);
		}
	}
	
	@Override
	protected void searchArt() {	
		if(!isLoaded) return;
		if (graph.APON) {	
			resetAP();
			getTextOutputArea().setText("");
		}
		else {
			graph.APON = true;
			if (graph.ORIGIN == null) {
				for(int no : graph.nodes.keySet()) {
					Node current = graph.nodes.get(no);
					if (current.getCheck()) continue;
					graph.artPoints = Articulation.search(current);
				}
					getTextOutputArea().setText("Number of Articulation Points: " + graph.artPoints.size());
			}
			else {
				graph.artPoints = Articulation.search(graph.ORIGIN);
				getTextOutputArea().setText("Number of Articulation Points: " + graph.artPoints.size());
			}
		}
			
	}
	
	public void resetAP() {
		if (!isLoaded) return;
		graph.artPoints.clear();
		graph.APON = false;
		for(int no : graph.nodes.keySet()) {
			graph.nodes.get(no).setCheck(false);
			graph.nodes.get(no).setCount(Integer.MAX_VALUE);
		}
	}
	

	@Override
	protected void onClick(MouseEvent e) {
		graph.isValid = false;
		distance = 0;
		if(isLoaded != true) return;
		Location clicked = Location.newFromPoint(e.getPoint(), origin, scale);
		// find the closest node.
		double bestDist = Double.MAX_VALUE;
		Node closest = null;

		for (Node node : graph.nodes.values()) {
			double distance = clicked.distance(node.location);
			if (distance < bestDist) {
				bestDist = distance;
				closest = node;
			}
		}

		// if it's close enough, highlight it and show some information.
		if (clicked.distance(closest.location) < MAX_CLICKED_DISTANCE) {
			if (graph.ORIGIN == closest) {
				getTextOutputArea().setText("");
				graph.ORIGIN = null;
			}
			else {
				getTextOutputArea().setText(closest.toString());
				graph.setOriginNode(closest);
			}
		}
	}
	
	@Override
	protected void onRClick(MouseEvent e) {
		graph.isValid = false;
		setUnvisited();
		if (graph.ORIGIN == null) {
			return;
		}
		Location clicked = Location.newFromPoint(e.getPoint(), origin, scale);
		// find the closest node.
		double bestDist = Double.MAX_VALUE;
		Node closest = null;
		for (Node node : graph.nodes.values()) {
			double distance = clicked.distance(node.location);
			if (distance < bestDist) {
				bestDist = distance;
				closest = node;
			}
		}

		// if it's close enough, highlight it and show some information.
		if (clicked.distance(closest.location) < MAX_CLICKED_DISTANCE) {
			getTextOutputArea().setText("");
			if (graph.DESTINATION == closest) {
				graph.hSegment.clear();
				graph.DESTINATION = null;
			}else {
			resetAP();
			graph.setDestinationNode(closest);
			if (graph.isValid == true)printPath();
			else getTextOutputArea().setText("Cannot find path!");
			}
		}
	}
	
	public void printPath() {
		getTextOutputArea().append("Route info \n");
		getTextOutputArea().append("----------------------------------- \n");
		String currentRoad = "";
		double length = 0;
		ArrayList<String> printInfo = new ArrayList<>();
		for (Segment thisSeg : graph.hSegment) {
			if (currentRoad.equals(thisSeg.road.name)) {
				length += thisSeg.length;
				int lastIndex =  printInfo.size() - 1;
				String txt = currentRoad + ": " + df.format(length) + "km";
				 printInfo.set(lastIndex, txt);
			} else {
				currentRoad = thisSeg.road.name;
				length = thisSeg.length;
				String txt = currentRoad + ": " + df.format(length) + "km";
				printInfo.add(txt);
			}
		}
		for (String s : printInfo) {
			getTextOutputArea().append(s);
			getTextOutputArea().append("\n");
		}
		getTextOutputArea().append("-----------------------------------");
		getTextOutputArea().append("\n");
		getTextOutputArea().append("Total distance: " + df.format(distance) + "km");
	}
	
	@Override
	protected void onPressed() {
		if (isLoaded != true) return;
		String filter = (String) StopList.getSelectedItem();
		if(filter.equals("Distance")) {
			filterByDistance();
		}else if(filter.equals("Time")) {
			filterByTime();
		}
	}
	
	public void setUnvisited() {
		for (int key : graph.nodes.keySet()) {
			Node current = graph.nodes.get(key);
			current.setVisited(false);
		}
	}
	
	@Override
	protected void filterByDistance() {
		graph.setFilter(ASearch.SEARCH_FILTER.DISTANCE);
	}

	@Override
	protected void filterByTime() {
		graph.setFilter(ASearch.SEARCH_FILTER.TIME);
	}

	@Override
	protected void noCar() {
		graph.addRestriction(ASearch.RESTRICTIONS.NO_CAR);
	}

	@Override
	protected void forCar() {
		graph.removeRestriction(ASearch.RESTRICTIONS.NO_CAR);
	}

	@Override
	protected void noPede() {
		graph.addRestriction(ASearch.RESTRICTIONS.NO_PEDE);
	}

	@Override
	protected void forPede() {
		graph.removeRestriction(ASearch.RESTRICTIONS.NO_PEDE);
	}

	@Override
	protected void noBike() {
		graph.addRestriction(ASearch.RESTRICTIONS.NO_BIKE);
	}

	@Override
	protected void forBike() {
		graph.removeRestriction(ASearch.RESTRICTIONS.NO_BIKE);
	}
	
	@Override
	protected void trafficLightOn() {
		graph.lightCheck = true;
	}
	
	@Override
	protected void trafficLightOff() {
		graph.lightCheck = false;
	}

	@Override
	protected void onSearch() {
		if (trie == null)
			return;

		// get the search query and run it through the trie.
		String query = getSearchBox().getText();
		Collection<Road> selected = trie.get(query);

		// figure out if any of our selected roads exactly matches the search
		// query. if so, as per the specification, we should only highlight
		// exact matches. there may be (and are) many exact matches, however, so
		// we have to do this carefully.
		boolean exactMatch = false;
		for (Road road : selected)
			if (road.name.equals(query))
				exactMatch = true;

		// make a set of all the roads that match exactly, and make this our new
		// selected set.
		if (exactMatch) {
			Collection<Road> exactMatches = new HashSet<>();
			for (Road road : selected)
				if (road.name.equals(query))
					exactMatches.add(road);
			selected = exactMatches;
		}

		// set the highlighted roads.
		graph.setHighlight(selected);

		// now build the string for display. we filter out duplicates by putting
		// it through a set first, and then combine it.
		Collection<String> names = new HashSet<>();
		for (Road road : selected)
			names.add(road.name);
		String str = "";
		for (String name : names)
			str += name + "; ";

		if (str.length() != 0)
			str = str.substring(0, str.length() - 2);
		getTextOutputArea().setText(str);
	}

	@Override
	protected void onMove(Move m) {
		if (m == GUI.Move.NORTH) {
			origin = origin.moveBy(0, MOVE_AMOUNT / scale);
		} else if (m == GUI.Move.SOUTH) {
			origin = origin.moveBy(0, -MOVE_AMOUNT / scale);
		} else if (m == GUI.Move.EAST) {
			origin = origin.moveBy(MOVE_AMOUNT / scale, 0);
		} else if (m == GUI.Move.WEST) {
			origin = origin.moveBy(-MOVE_AMOUNT / scale, 0);
		} else if (m == GUI.Move.ZOOM_IN) {
			if (scale < MAX_ZOOM) {
				// yes, this does allow you to go slightly over/under the
				// max/min scale, but it means that we always zoom exactly to
				// the centre.
				scaleOrigin(true);
				scale *= ZOOM_FACTOR;
			}
		} else if (m == GUI.Move.ZOOM_OUT) {
			if (scale > MIN_ZOOM) {
				scaleOrigin(false);
				scale /= ZOOM_FACTOR;
			}
		}
	}

	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons, File trafficLight) {
		distance = 0;
		if (isLoaded == true) resetAP();
		isLoaded = true;
		graph = new Graph(nodes, roads, segments, polygons, trafficLight);
		trie = new Trie(graph.roads.values());
		origin = new Location(-250, 250); // close enough
		scale = 1;
		for (int n : graph.nodes.keySet()) {
			graph.nodes.get(n).addNeighbours();
		}
	}

	/**
	 * This method does the nasty logic of making sure we always zoom into/out
	 * of the centre of the screen. It assumes that scale has just been updated
	 * to be either scale * ZOOM_FACTOR (zooming in) or scale / ZOOM_FACTOR
	 * (zooming out). The passed boolean should correspond to this, ie. be true
	 * if the scale was just increased.
	 */
	private void scaleOrigin(boolean zoomIn) {
		Dimension area = getDrawingAreaDimension();
		double zoom = zoomIn ? 1 / ZOOM_FACTOR : ZOOM_FACTOR;

		int dx = (int) ((area.width - (area.width * zoom)) / 2);
		int dy = (int) ((area.height - (area.height * zoom)) / 2);

		origin = Location.newFromPoint(new Point(dx, dy), origin, scale);
	}

	public static void main(String[] args) {
		new Mapper();
	}
}

// code for COMP261 assignments