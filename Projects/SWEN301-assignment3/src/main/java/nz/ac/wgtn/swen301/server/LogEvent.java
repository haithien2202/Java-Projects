package nz.ac.wgtn.swen301.server;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEvent implements Comparable<Object> {

  private final String id;
  private final String message;
  private final String timestamp;
  private final String thread;
  private final String logger;
  private final String level;
  private final String errorDetails;

  public LogEvent(String id, String message, String timestamp, String thread, String logger, String level, String errorDetails){

    this.id = id;
    this.message = message;
    this.timestamp = timestamp;
    this.thread = thread;
    this.logger = logger;
    this.level = level;
    this.errorDetails = errorDetails;

  }

  public Timestamp getTimestamp(){
	    Date date = null;
	    try {
	      date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
	      return new Timestamp(date.getTime());
	    } catch (ParseException e) {
	      e.printStackTrace();
	    }
	    return null;
	  }
  
  public String getId(){
    return id;
  }

  public String getMessage(){
    return message;
  }

  public String getThread(){
    return thread;
  }

  public String getLogger(){
    return logger;
  }

  public String getErrorDetails(){
	    return errorDetails;
	  }
  
  public String getLevel(){
    return level;
  }

  @Override
  public int compareTo(Object o) {
    int compare = 0;
    if(o instanceof LogEvent) {
      compare = getTimestamp().compareTo(((LogEvent) o).getTimestamp());
    }
    return compare;
  }

}