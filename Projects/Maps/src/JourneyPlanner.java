import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This is a small example class to demonstrate extending the GUI class and
 * implementing the abstract methods. Instead of doing anything maps-related, it
 * draws some squares to the drawing area which are removed when clicked. Some
 * information is given in the text area, and pressing any of the navigation
 * buttons makes a new set of squares.
 * 
 * @author thien
 */
public class JourneyPlanner extends GUI {
	private final int moveUnit = 30;
	private final List<Stop> stops = new ArrayList<Stop>();
	private Map<String,Stop> stopMap = new HashMap<String,Stop>();
	private Map<String,Stop> stopMapName = new HashMap<String,Stop>();
	private Map<String,Trips> tripMap = new HashMap<String,Trips>();
	private final List<Connections> connections = new ArrayList<Connections>();
	private final List<Trips> trips = new ArrayList<Trips>();
	private List<Character> arrayChar = new ArrayList<Character>();
	private List<String> listOfNames = new ArrayList<String>();
	private TrieNode root = new TrieNode();
	private boolean isDisplayed = true;
	private final boolean EndsOfWord = true;
	private static final double x = 0;
	private static final double y = 0; 
	private Location middle;
	private static final double width = Stop.size.getWidth(); 
	private static final double height = Stop.size.getWidth(); 
	private QuadTree rootQuad = new QuadTree (x, y , width, height);
	private List<Stop> neighStops = new ArrayList<Stop>();
	
	private void updateWindow() {
		Dimension x = getDrawingAreaDimension();
		double HEIGHT = x.height;
		double WIDTH = x.width;
		middle = new Location(WIDTH/2,HEIGHT/2);
	}
	
	@Override
	protected void redraw(Graphics g) {
		if (stops.size() > 0) {
			for (Stop s : stops) {
				s.scaling();
			}
		}
		if (check == false)loadQuad();
		if (isDisplayed == true) {
			QuadTree newQuad = rootQuad;
			drawQuadTree(newQuad,g);
			}
		if (connections.size() > 0) {
		for (Connections n : connections) {
			if (n.isSelected() == false) {
				n.draw(g);
				}
			}
		for (Connections n : connections) {
				if (n.isSelected() == true) {
				n.draw(g);
				}
			}
		}
		if (stops.size() > 0) {
			for (Stop s : stops) {
				if(s.isSelected() == false) {
				s.draw(g);
				}
			}
			for (Stop s : stops) {
				if(s.isSelected() == true) {
				s.draw(g);
				}
			}
			
		}						
	}
	
	
	public void drawQuadTree(QuadTree current, Graphics g) {
		current.drawQuadTree(g);
		if (current.isDivided() == true) {
			drawQuadTree(current.getSouthEast(),g);
			drawQuadTree(current.getNorthEast(),g);
			drawQuadTree(current.getSouthWest(),g);
			drawQuadTree(current.getNorthWest(),g);
		}
	}

	public void setAllUnselected(){
		for (Stop s : stops) {
			s.unSuggest();
			s.setUnSelected();
		}
		for (Connections n : connections) {
			n.setSelected(false);
		}
		for (Trips t : trips) {
			t.setSelected(false);
		}
	}
	
	@Override
	protected void onClick(MouseEvent e) {
		unselectQuad();
		StopList.setSelectedIndex(0);
		neighStops.clear();
		setAllUnselected();
		textOutputArea.setText("");
		int mouseX = e.getX();
		int mouseY = e.getY();
		Location m = new Location(mouseX,mouseY);
		searchClosest(m);
	}
	
	public void unselectQuad() {
		QuadTree newQuad = rootQuad;
		unselectQuadNode (newQuad);
	}
	public void unselectQuadNode(QuadTree current) {
		current.isIntersect=false;
		if (current.isDivided() == true) {
			unselectQuadNode(current.getNorthEast());	
			unselectQuadNode(current.getNorthWest());	
			unselectQuadNode(current.getSouthEast());	
			unselectQuadNode(current.getSouthWest());	
		}
	}
	
	public void searchClosest(Location mouse) {
		double range = 0;
		range = findRange(mouse.x,mouse.y,range);
		double xx = mouse.x - range/2;
		double yy = mouse.y - range/2;
		double wx = mouse.x + range/2;
		double hy = mouse.y + range/2;
		
		QuadTree newQuad = rootQuad;
		searchQuad(xx, yy, wx, hy, newQuad);
		
		double distance = Double.MAX_VALUE;
		Stop n = null;
		for (Stop s : neighStops) {
			Location ofStop = s.getLocation();
			double currentDist = ofStop.distance(mouse);
			if (currentDist < distance) {
				distance = currentDist;
				n = s;
			}
		}
		highlightStop(n);	
	}
	
	public void highlightStop(Stop n) {
		if (n != null) {
			textOutputArea.append("Selected stop: ");
			textOutputArea.append("\n");
			textOutputArea.append( "   " + n.getName());		
			textOutputArea.append("\n");
			textOutputArea.append("-------------------------------");
			textOutputArea.append("\n");
			textOutputArea.append("Trip IDs:  ");
			n.setSelected();	
			for (Trips current : findTrips(n)) {
					textOutputArea.append(current.getID()+"       ");
					findConnections(current);
			}
		}
	}
	
	public double findRange(double xx, double yy, double range) {
		int noOfStop = 0;
		double wx = xx + range /2 ;
		double hy = yy + range / 2;
		double x = xx - range /2 ;
		double y = yy - range / 2;
		for (Stop s : stops) {
			double n = s.getLocX();
			double m = s.getLocY();
			if ( n > x && n < wx && m > y && m < hy) {
				noOfStop ++;
			}
		}
		while ( noOfStop == 0) {
			range += 30;
			wx = xx + range /2 ;
			hy = yy + range / 2;
			x = xx - range /2 ;
			y = yy - range / 2;
			for (Stop s : stops) {
				double n = s.getLocX();
				double m = s.getLocY();
				if ( n > x && n < wx && m > y && m < hy) {
					noOfStop ++;
				}
			}
		}
		return range;
	}
	
	public void searchQuad(double xx, double yy, double wx, double hy, QuadTree newQuad) {
		if (newQuad.isInterfered(xx, yy, wx, hy) == true ) {
			redraw();
			if (newQuad.isDivided()) {
				searchQuad(xx,yy,wx,hy,newQuad.getNorthEast());	
				searchQuad(xx,yy,wx,hy,newQuad.getNorthWest());	
				searchQuad(xx,yy,wx,hy,newQuad.getSouthEast());	
				searchQuad(xx,yy,wx,hy,newQuad.getSouthWest());	
			}else {
				if (!newQuad.getNull()) {
					newQuad.setIntersect(true);
					Stop current = newQuad.getPoint();
					if(current != null) {
						neighStops.add(current);
					}
				}
			}
		}
	}
	
	
	@Override
	protected void onDisplay() {
		if (isDisplayed == false) isDisplayed = true;
		else if (isDisplayed == true) isDisplayed = false;
	}
	
	@Override
	protected void onScroll(boolean positive) {
		unselectQuad();
		if (positive == true) {
			onMove(Move.ZOOM_IN);
		}else onMove(Move.ZOOM_OUT);
	}
	
	@Override
	protected Point onDrag(MouseEvent e, int x , int y ) {
		unselectQuad();
		int mouseX = e.getX();
		int mouseY = e.getY();
		double difX = mouseX - x;
		double difY = mouseY - y;
		Stop.addOrigin(difX, difY);
		Point m = new Point(mouseX,mouseY);
		return m;
	}
	
	@Override
	protected void onPressed() {
		unselectQuad();
		textOutputArea.setText("");
		setAllUnselected();
		String name = (String) StopList.getSelectedItem();
		Stop currentStop = stopMapName.get(name);	
		if (currentStop != null) {
			currentStop.setSelected();
			highlightStop(currentStop);	
		}
	}
	
	
	public List<Trips> findTrips(Stop m) {
		List<Trips> allTrips = m.getTrips();
			for (Trips x : allTrips) {
				x.setSelected(true);
			}
		return allTrips;
	}
	
	public void findConnections(Trips current) {
		List<Connections> cons = current.getCons();
		for(Connections con : cons) {
				con.setSelected(true);
		}
	}
	
	public void loadQuad() {
		rootQuad = new QuadTree (x, y , width, height);
		makeQuad(rootQuad);
	}
	
	public void makeQuad (QuadTree current) {
		double boundaryX = current.getX();
		double boundaryY = current.getY();
		double Width = current.getW();
		double Height = current.getH();
		double boundaryW = boundaryX + Width;
		double boundaryH = boundaryY + Height;
		int noPoint = 0;
		List<Stop> listQuadStop = new ArrayList<Stop>();
		for (Stop s : stops) {
			Location ofStop = s.getLocation();
			double stopX = ofStop.x;
			double stopY = ofStop.y;
			if ((stopX < boundaryW) && (stopX > boundaryX) && (stopY < boundaryH) && (stopY > boundaryY)) {
				noPoint++;
				listQuadStop.add(s);
			}		
		}
		if (noPoint > 1) {
			current.subdivide();
		}else if (noPoint == 1) {
			current.isDivided=false;
			current.addStop(listQuadStop.get(0));
			current.isNull = false;
		}
		else if (noPoint == 0) {
			current.isDivided = false;
			current.isNull = true;
		}
		if (current.isDivided() == true) {
			makeQuad(current.getSouthEast());
			makeQuad(current.getNorthEast());
			makeQuad(current.getSouthWest());
			makeQuad(current.getNorthWest());
		}
		
	}

	@Override
	protected void onSearch() {
		unselectQuad();
		listOfNames.clear();
		setAllUnselected();
		String search = getSearchBox().getText();
		if (search.equals("")) {
			addToComboBox();
			getTextOutputArea().setText("");	
		}else {
			arrayChar.clear();
			boolean Check = find(search);
			if (Check == true) {
				searchName();
				StopList.removeAllItems();
				Collections.sort(listOfNames);
				for (String name : listOfNames) {
					name = fixName(name);
					Stop current = stopMapName.get(name);
					current.setSuggest();
					StopList.addItem(name);
				}
			}
			else getTextOutputArea().setText("No stop found!");
		}
	}
	
	protected void onSearchButton() {
		unselectQuad();
		setAllUnselected();
		String search = getSearchBox().getText();
		findStop(search);	
			
	}
	
	public String fixName( String name ) {
		String newName ="";
		for (int i = 1; i < name.length(); i++) {
			newName += name.charAt(i);
		}
		return newName;
	}
	
	
	public void findStop(String n) {
		StopList.setSelectedIndex(-1);
		getTextOutputArea().setText("");	
		boolean isValid = false;
		Stop searchStop = null;
		for (String stopName : stopMapName.keySet()) {
			if ( stopName.equals(n)) {
				isValid = true;
				searchStop = stopMapName.get(stopName);
				searchStop.setSelected();
			}
		}
		
		if (isValid == false) {
			getTextOutputArea().setText("No stop found!");	
		}
		else if (n.equals("")) {
			getTextOutputArea().setText("No stop found!");
		}
		else {
			highlightStop(searchStop);
		}
	}
	
	public void searchName() {
		TrieNode current = root;
		String tempName ="";
		if (current != null) {
			for (int i = 0 ; i < arrayChar.size(); i++) {	
				Map<Character, TrieNode> childrenMap = current.getChildrenMap();
				tempName += current.getChar();
				current = childrenMap.get(arrayChar.get(i));
			}
		}
		if (current == null) return;
			findTheRest(current,tempName);	
			setAllUnselected();
	}
	
	public void findTheRest(TrieNode current,String name) {
		if (current != null)name = name + current.getChar();
		if ( current != null && current.isLeaf() == false) {
			for (Character  o : current.getChildrenMap().keySet()) {
				TrieNode y = current.getChildrenMap().get(o);
				findTheRest(y,name);
			}
		}else if ( current != null && current.isLeaf() == true && current.getChildrenMap()  != null) {
			listOfNames.add(name);
			for (Character  o : current.getChildrenMap().keySet()) {
				TrieNode y = current.getChildrenMap().get(o);
				findTheRest(y,name);
			}
		}
		else listOfNames.add(name);
	}
	
	
	public boolean find(String name) {
		TrieNode current = root;
		for (int i = 0; i<name.length(); i++) {
			Character c = name.charAt(i);
			if (current != null){
			Map<Character, TrieNode> childrenMap = current.getChildrenMap();
			if (childrenMap != null) {
				boolean isValid = false;
				for (Character children : childrenMap.keySet()) {
					char k = childrenMap.get(children).getChar();
					if ( k == c ) {
						current = childrenMap.get(c);
						arrayChar.add(c);
						isValid = true;
					} 
				}
					if (isValid == false) {
						getTextOutputArea().setText("Invadlid name!");
						return false;
					}  
				}
			}
		}
			return EndsOfWord;
		}
						

	@Override
	protected void onMove(Move m) {
		check = false;
		if (m == Move.WEST) {
			Stop.addOrigin(moveUnit, 0);
		}
		if (m == Move.EAST) {
			Stop.addOrigin(-moveUnit, 0);
		}
		if (m == Move.SOUTH) {
			Stop.addOrigin(0, -moveUnit);
		}
		if (m == Move.NORTH) {
			Stop.addOrigin(0, moveUnit);
		}
		if (m == Move.ZOOM_IN) {
			if (Stop.scale < 0.001) return;			
			double disX = Stop.getOriginLocX() - (Stop.size.getWidth()/2-150);
			double disY = Stop.getOriginLocY() - (Stop.size.getHeight()/2-150);
			Stop.addOrigin(disX*0.2, disY*0.2);
			Stop.addScale(0.8);
		}
		if (m == Move.ZOOM_OUT) {		
			double disX = Stop.getOriginLocX() - (Stop.size.getWidth()/2-150);
			double disY = Stop.getOriginLocY() - (Stop.size.getHeight()/2-150);
			Stop.addOrigin(-disX*0.2, -disY*0.2);
			Stop.addScale(1.2);
		}
	}
	
	@Override
	protected void onCentral() {
		check = false;
		updateWindow();
		Stop.setOrigin(middle.x,middle.y);
	}

	@Override
	protected void onLoad(File stopFile, File tripFile) {	
	updateWindow();
	stops.clear();
	stopMap.clear();
	tripMap.clear();
	connections.clear();
	trips.clear();
	Stop.setScale(0.06);
	Stop.setOrigin(middle.x,middle.y);
	 try(BufferedReader stopFileReader = new BufferedReader(new FileReader(stopFile))) {
		 stopFileReader.readLine();
		        for(String line; (line = stopFileReader.readLine()) != null; ) {
		            // process the line
		        	String ID;
		        	String name;
		        	double lon;
		        	double lat;
		        	String[] parts = line.split("\t");
		        	ID = parts[0]; // 004
		        	name = parts[1];
		        	lat =  Double.parseDouble(parts[2]);
		        	lon = Double.parseDouble(parts[3]);
		        	Location loc = Location.newFromLatLon(lat,lon);
		        	Stop stop = new Stop(ID,name,loc);
		        	stopMap.put(ID,stop);
		        	stopMapName.put(name,stop);
		        	stops.add(stop);
		        }
		        // line is not visible here.
            stopFileReader.close(); 	
        } catch(IOException e){getTextOutputArea().setText("example doesn't load any files.");}	 
	 loadTrip(tripFile); 
	 makeConnection();
	 addAllTriptoStop();
	 addStopToTrie();
	 addToComboBox();
	 check = false;
	}
	
	public void addStopToTrie() {	
		for (String name : stopMapName.keySet()) {
			insert(name);
		}
	}
	
	public void addToComboBox() {
		StopList.removeAllItems();
		StopList.addItem("");
		List<String> nameUnsorted = new ArrayList<String>();
		for (String name : stopMapName.keySet()) {
			nameUnsorted.add(name);
		}
		Collections.sort(nameUnsorted);
		for (String name : nameUnsorted) {
			StopList.addItem(name);
		}
		StopList.setSelectedIndex(-1);
	}
	
	public void insert(String name) {
        HashMap<Character, TrieNode> children = (HashMap<Character, TrieNode>) root.children;
 
        for(int i=0; i<name.length(); i++){
            char c = name.charAt(i);
 
            TrieNode t;
            if(children.containsKey(c)){
                    t = children.get(c);
            }else{
                t = new TrieNode(c);
                children.put(c, t);
            }
 
            children = (HashMap<Character, TrieNode>) t.children;
 
            //set leaf node
            if(i==name.length()-1)
                t.isLeaf = true;    
        }
    }
	  
	  public TrieNode searchNode(String str){
	        Map<Character, TrieNode> children = root.children; 
	        TrieNode t = null;
	        for(int i=0; i<str.length(); i++){
	            char c = str.charAt(i);
	            if(children.containsKey(c)){
	                t = children.get(c);
	                children = t.children;
	            }else{
	                return null;
	            }
	        }
	 
	        return t;
	    }
	
	
	public void loadTrip(File tripFile) {
		try(BufferedReader stopFileReader = new BufferedReader(new FileReader(tripFile))) {
			 stopFileReader.readLine();
			        for(String line; (line = stopFileReader.readLine()) != null; ) {
			            // process the line
			        	String ID;
			        	String[] parts = line.split("\t");
			        	ID = parts[0]; // 004
			        	List<Stop> listOfStops = new ArrayList<Stop>();
			        	for (int i = 1; i < parts.length; i++) {
			        		String name = parts[i];
			        		Stop x = stopMap.get(name);
			        		listOfStops.add(x);
			        	}
			        	Trips newTrip = new Trips(ID,listOfStops);
			        	tripMap.put(ID,newTrip);
			        	trips.add(newTrip);
			        }
			        // line is not visible here.
	            stopFileReader.close(); 	
	        } catch(IOException e){getTextOutputArea().setText("example doesn't load any files.");}  
	}
	
	public void addAllTriptoStop() {
		if(trips.size()>0) {
			for (Trips m : trips) {
				List<Stop> y = m.getStops();
				for (Stop x : y) {
					x.addTrip(m);
				}
			}
		}
	}
	
	public void makeConnection() {
		for (String tripID : tripMap.keySet()) {
			Trips trip = tripMap.get(tripID);
			List<Stop> stopsOfTrip = trip.getStops();
			for(int i = 0 ; i < stopsOfTrip.size()-1; i++) {
				Stop current = stopsOfTrip.get(i);
				Stop next = stopsOfTrip.get(i+1);
				Connections newCon = new Connections(current,next,trip);
				connections.add(newCon);
				trip.addCons(newCon);
			}
		}
	}
	
	public static void main(String[] args) {
		new JourneyPlanner();
	}
}


// code for COMP261 assignments