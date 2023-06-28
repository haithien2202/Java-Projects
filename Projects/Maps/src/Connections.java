import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Connections {
		private Stop From;
		private Stop To;
		private Trips tripID;
		private boolean isSelected = false;
		public Connections(Stop Stopform, Stop Stopto, Trips trip) {
			From = Stopform;
			To = Stopto;
			tripID  = trip;
		}
		
		public Trips getTrip() {
			return tripID;
		}
		
		public void setSelected ( boolean select) {
			isSelected = select;
		}
		
		public boolean isSelected () {
			return isSelected;
		}
		
		
		public void draw(Graphics g) {
			double xFrom = From.getLocX();
			double yFrom = From.getLocY();
			double xTo = To.getLocX();
			double yTo =To.getLocY();
			Graphics2D graphics = (Graphics2D)g;
			if (isSelected == true) {
				
				graphics.setStroke(new BasicStroke(2));
				graphics.setColor(Color.yellow);	
				graphics.draw(new Line2D.Double(xFrom, yFrom, xTo, yTo));
			}
			else {
			graphics.setStroke(new BasicStroke(1));
			graphics.setColor(Color.black);	
			graphics.draw(new Line2D.Double(xFrom, yFrom, xTo, yTo));
			}
		}
	}
