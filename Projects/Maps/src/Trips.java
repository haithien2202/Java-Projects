import java.util.ArrayList;
import java.util.List;

public class Trips {
		//List<Connections>
		private List<Stop> stops = new ArrayList<Stop>();
		private List<Connections> cons = new ArrayList<Connections>();
		private String tripID;	
		private boolean isSelected;
		public Trips(String ID, List<Stop> listOfStops) {
			tripID = ID;
			stops = listOfStops;
		}
		
		public void addCons(Connections con) {
			cons.add(con);
		}
		
		public String getID() {
			return tripID;
		}
		
		public List<Connections> getCons() {
			return cons;
		}
		
		public void setSelected(boolean n) {
			this.isSelected = n;
		}
		
		public boolean isSelected() {
			return isSelected;
		}
		
		public List<Stop> getStops(){
		 return stops;
		}
	}
