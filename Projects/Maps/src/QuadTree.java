import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

 
public class QuadTree {
 double boundaryX;
 double boundaryY;
 double boundaryW;
 double boundaryH;
 QuadTree southEast;
 QuadTree southWest;
 QuadTree northEast;
 QuadTree northWest;
 boolean isIntersect = false;
 boolean isDivided = false;
 boolean isNull = false;
 Stop singleStop = null;
 
 		public QuadTree(double x, double y, double w, double h) {
 			boundaryX = x;
 			boundaryY = y;
 			boundaryW = w;
 			boundaryH = h;
 		}
 		
 		public void addStop(Stop current) {
 			singleStop = current;
 		}
 	
 		public QuadTree getSouthEast() {
 	 		return southEast;
 	 	}
 	 	public QuadTree getSouthWest() {
 	 		return southWest;
 	 	}
 	 	public QuadTree getNorthEast() {
 	 		return northEast;
 	 	}
 	 	public QuadTree getNorthWest() {
 	 		return northWest;
 	 	}	
 	 	
 	 	public void setIntersect(boolean m) {
 	 		isIntersect = m;
 	 	 }	
 	 	
 	 public boolean isDivided() {
 		 return isDivided;
 	 }
 	 
 	public Stop getPoint() {
		 return singleStop;
	 }
 		
 	public double getX() {
 		return boundaryX;
 	}
 	public double getY() {
 		return boundaryY;
 	}
 	public double getW() {
 		return boundaryW;
 	}
 	public double getH() {
 		return boundaryH;
 	}
 	
 	public boolean getNull() {
 		return isNull;
 	}
 	
 	public boolean isInterfered(double xx, double yy, double ww, double hh) {
 		boolean isInterfering = false;
 		double boundaryR = this.boundaryX + this.boundaryW;
 		double boundaryB = this.boundaryY + this.boundaryH;
 		boolean finalCheck = true;
 		boolean checkContains = false;
 		if (ww < this.boundaryX ||  xx > boundaryR || hh < boundaryY || yy > boundaryB) finalCheck = false;
 		if ((yy > this.boundaryY && yy < boundaryB) && (hh > boundaryY && hh < boundaryB) && 
 				(xx > boundaryX && xx < boundaryR) && (ww > boundaryX && ww < boundaryR)) checkContains = true;
 				
 		if ( finalCheck == true || checkContains == true) {
 			isInterfering = true;
 		}
 		return isInterfering;
 	}

 	public void subdivide() {
 		southEast = new QuadTree(this.boundaryX,this.boundaryY+this.boundaryH/2,this.boundaryW/2,this.boundaryH/2);
 		southWest = new QuadTree(this.boundaryX+this.boundaryW/2,this.boundaryY+this.boundaryH/2,this.boundaryW/2,this.boundaryH/2);
 		northEast = new QuadTree(this.boundaryX,this.boundaryY,this.boundaryH/2,this.boundaryH/2);
 		northWest = new QuadTree(this.boundaryX+this.boundaryW/2,this.boundaryY,this.boundaryW/2,this.boundaryH/2);
 		isDivided = true;
 	}
 
 	public void drawQuadTree(Graphics g) {
 		Graphics2D graphics = (Graphics2D)g;
 		graphics.setColor(Color.black);
 		if (isIntersect == false) {
 		graphics.draw(new Rectangle2D.Double(boundaryX, boundaryY, boundaryW, boundaryH));
 		} else {
 			graphics.setColor(Color.green.brighter());
 			graphics.fill(new Rectangle2D.Double(boundaryX, boundaryY, boundaryW, boundaryH));
 			graphics.setColor(Color.black);
 			graphics.draw(new Rectangle2D.Double(boundaryX, boundaryY, boundaryW, boundaryH));
 		}
 	}
}
