import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;


public class Stop {
		public static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		private static Location origin = new Location(350,350);
		public static double scale = 0.06;
		private final double x, y;	
		private final double sizeRect = 5;
		private double dx, dy;
		private boolean isSuggested = false;
		String stopID;
		String name;
		private List<Trips> listOfTrips = new ArrayList<Trips>();
		private boolean selected = false;
		public Stop(String ID, String Name, Location loc) {
			x =  loc.x;
			y =  loc.y;
			name = Name;
			stopID = ID;
		}
		
		public void setSelected() {
			selected = true;
		}
		
		public void setSuggest() {
			isSuggested = true;
		}
		
		public void unSuggest() {
			isSuggested = false;
		}
		
		public void setUnSelected() {
			selected = false;
		}
		
		public String getName() {
			return name;
		}
		
		public void addTrip(Trips trip) {
			listOfTrips.add(trip);
		}
		
		public List<Trips> getTrips() {
			return listOfTrips;
		}
		
		public Location getLocation() {
			return new Location(dx,dy);
		}
		
		public boolean isSelected() {
			return selected;
		}
		
		public static double getOriginLocX() {
			return origin.x;
		}
		
		public static double getOriginLocY() {
			return origin.y;
		}
		
		public double getLocX() {
			return dx;
		}
		
		public double getLocY() {
			return dy;
		}
		
		public static void setOrigin(double moveX, double moveY) {
			origin = new Location(moveX,moveY);
		}
		
		public static void addOrigin(double moveX, double moveY) {
			double X = origin.x;
			double Y = origin.y;
			origin = new Location(X+moveX,Y+moveY);
		}
		
		public static void addScale(double s) {
			scale = scale * s;
		}
		
		public static void setScale(double s) {
			scale = s;
		}
		
		public void scaling () {
			Point2D.Double p = new Point2D.Double(x, y);
			Location newLoc = Location.newFromPoint(p,origin,scale);
			dx = newLoc.x;
			dy = newLoc.y;
		}

		public void draw(Graphics g) {
			Graphics2D graphics = (Graphics2D)g;
			graphics.setStroke(new BasicStroke(1));
			if (selected == true) {	
				g.setColor(Color.blue);
				graphics.fill(new Rectangle2D.Double(dx-sizeRect/2, dy-sizeRect/2, sizeRect, sizeRect));
			}else if(isSuggested == true){
				g.setColor(Color.red.brighter());
				graphics.fill(new Rectangle2D.Double(dx-sizeRect/2, dy-sizeRect/2, sizeRect, sizeRect));
			}
			else {
			g.setColor(Color.red);
			graphics.draw(new Rectangle2D.Double(dx-sizeRect/2, dy-sizeRect/2, sizeRect, sizeRect));
			}
		}

		public boolean contains(int x, int y) {
			return x > this.x && y > this.y && x < this.x + 10
					&& y < this.y + 10;
		}
	}
