package nz.ac.wgtn.swen301.assignment2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**JsonLayout class.
  *
  */
public class JsonLayout extends Layout {
  @Override
  public String format(LoggingEvent loggingEvent) {
    String format = ""; 
    ObjectMapper objM = new ObjectMapper();
    ObjectNode objN = objM.createObjectNode();
    objN.put("logger", loggingEvent.getLoggerName());
    objN.put("level", loggingEvent.getLevel().toString());
    objN.put("starttime", Long.toString(loggingEvent.getTimeStamp()));
    objN.put("thread", loggingEvent.getThreadName());
    objN.put("message", loggingEvent.getMessage().toString());
    try {
      format = objM.writerWithDefaultPrettyPrinter().writeValueAsString(objN);
    } catch (IOException e) {
      return null;
    }
    return format;
  }
  
  @Override
  public boolean ignoresThrowable() {
    return false;
  }

  @Override
  public void activateOptions() {
  }
}