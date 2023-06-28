package nz.ac.wgtn.swen301.server;

import java.util.*;

/**
 * Simulates a Database using a static list
 */
public class Persistency {

  private static final String[] all_levels = {"ALL", "TRACE", "DEBUG","INFO", "WARN", "ERROR", "FATAL", "OFF"};

  static PriorityQueue<LogEvent> DB = new PriorityQueue<>();

  public void postLog(LogEvent lg){
    DB.offer(lg);
  }

  public ArrayList<LogEvent> getDB(){
    return new ArrayList<>(DB);
  }

  public void clearDB(){
    DB.clear();
  }

  public LinkedHashMap<String, int[]> getLogLevels(){

    /**
     * for each log, add to a map of string and a list. The list contains a number of all levels
     */

    LinkedHashMap<String, int[]> test1 = new LinkedHashMap<>();

    for(int i = 0; i < DB.size(); i++){
      test1.put(getDB().get(i).getLogger(), new int[8]);
    }


    for(LogEvent lg : DB){
      for(int i = 0; i < 8; i++){
        if(lg.getLevel().equals(all_levels[i])) {
          test1.get(lg.getLogger())[i]++;
        }
      }
    }

    return test1;
  }

  public ArrayList<String> getAll_levels(){
    return new ArrayList<>(Arrays.asList(all_levels));
  }

}