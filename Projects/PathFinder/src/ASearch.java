import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;


public class ASearch {
	private static boolean trafficLightCheck = false;
	public enum RESTRICTIONS {
		NO_CAR, NO_PEDE, NO_BIKE
	}

	public enum SEARCH_FILTER {
		DISTANCE ,  TIME
	}
	
	public static ArrayList<Node> search(Graph graph, Node start, Node end, SEARCH_FILTER filter, List<RESTRICTIONS> restrictions) {
		trafficLightCheck = graph.lightCheck;
		PriorityQueue<Tuple> fringe = new PriorityQueue<>();
		double dist = getDistance(start,end);
		Tuple origin = new Tuple(start,null,0,0,dist);
		
		fringe.add(origin);
		while(!fringe.isEmpty()) {
			Tuple current = fringe.poll();
			current.node.setVisited(true);
				if (current.node == end) {
					Tuple thisTup = current;
					ArrayList<Node> path = new ArrayList<Node>();
					Mapper.distance = current.cost;
					while (thisTup != null) {
						path.add(0, thisTup.node);
						thisTup = thisTup.previous;
					}
					return path;
				}
		List<Tuple> neighs = current.getNeighs();
		for (Tuple neigh : neighs)	{
			double cost;
			Segment thisSeg = getSegment(current.node, neigh.node);
			if (filter == SEARCH_FILTER.DISTANCE) {
				if (trafficLightCheck == true)cost =  neigh.g-neigh.currentLength + neigh.currentLength*neigh.node.getTrafficLight() + getDistance(neigh.node, end);
				else cost = neigh.g + getDistance(neigh.node, end); 
			}else{
				Road currentRoad = thisSeg.road;
				if (trafficLightCheck == true) cost = ((neigh.g - neigh.currentLength) + getDistance(neigh.node, end)/150+ (neigh.currentLength / currentRoad.speed / (currentRoad.Rclass+1))*neigh.node.getTrafficLight() );
				else cost = ((neigh.g - neigh.currentLength) + getDistance(neigh.node, end)/50/3 + (neigh.currentLength / currentRoad.speed / (currentRoad.Rclass+1)));
			}
			if (neigh.node.isVisited()) continue;
			if (!isValid(thisSeg, current.node, neigh.node, restrictions,graph)) continue;
			neigh.cost = cost;
			fringe.add(neigh);
		 }
		}
		return null;
	}
	
	private static boolean isValid(Segment s, Node start,Node end, List<RESTRICTIONS> rest,Graph g) {
				if (s.road.oneWay == 1) if (s.start != start) return false;
				
				if (!rest.isEmpty()) {
					if (rest.contains(RESTRICTIONS.NO_CAR)) {
						if (s.road.noCar == 1) return false;	
					}
					if (rest.contains(RESTRICTIONS.NO_PEDE)) {
						if (s.road.noPede == 1) return false;
					}
					if (rest.contains(RESTRICTIONS.NO_BIKE)) {
						if (s.road.noBike == 1) return false;
					}
					return true;
				}
		return true;
	}
		
		private static Segment getSegment(Node start, Node end) {
			Collection<Segment> segments = start.segments;
			for (Segment s : segments) {
				if ((s.start == start && s.end == end) || (s.start == end && s.end == start)) {
					return s;
				}
			}
			return null;
		}
	
	private static double getDistance(Node start, Node end) {
		return start.location.distance(end.location);
	}
	
	private static class Tuple implements Comparable<Tuple> {
		Node node;
		double g;
		double currentLength;
		double cost;
		Tuple previous;

		public Tuple(Node n, Tuple prev, double g, double l, double h) {
			this.node = n;
			this.previous = prev;
			this.currentLength = l;
			this.g = g + currentLength;
			this.cost = h;
		}

		List<Tuple> getNeighs() {
			List<Tuple> neighs = new ArrayList<>();
			List<Segment> seg = new ArrayList<>(node.segments);

			for (Segment current : seg) {
				Node n = current.start == this.node ? current.end : current.start;
				Tuple neighborTuple = new Tuple(n, this, this.g, current.length, Double.POSITIVE_INFINITY);
				neighs.add(neighborTuple);
			}
			return neighs;
		}
		
		@Override
		public int compareTo(Tuple o) {
				if (this.cost > o.cost) return 1;
				else if (this.cost < o.cost) return -1;
				return 0;
		}
	}
}