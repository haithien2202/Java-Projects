package nz.ac.wgtn.swen301.assignment2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**MemAppender class.
*
*/
public class MemAppender extends AppenderSkeleton implements MemAppenderMBean {

  List<LoggingEvent> logEvents = new ArrayList<>();
  private long discardCount = 0;
  private long maxSize = 1000;

  @Override
  protected void append(LoggingEvent loggingEvent) {
    logEvents.add(loggingEvent);

    while (logEvents.size() > maxSize) {
      logEvents.remove(0);
      discardCount++;
    }

  }

  public String getMemName() {
    return name;
  }

  public void setMemName(String name) {
    this.name = name;
  }
  
  /**Method to export to JSON.
   *
   */
  public void exportToJSON(String fileName) {
    JsonLayout l = new JsonLayout();
    StringBuilder s = new StringBuilder();
    for (LoggingEvent i : logEvents) {
      s.append(l.format(i));
    }
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
      writer.write(s.toString());
      writer.close();
    } catch (Exception ignored) {
    }
  }

  public List<LoggingEvent> getCurrentLogs() {
    return Collections.unmodifiableList(logEvents);
  }

  public long getDiscardedLogCount() {
    return discardCount;
  }
  
  @Override
  public void close() {
    return;
  }
  
  @Override
  public boolean requiresLayout() {
    return false;
  }

  @Override
  public String[] getLogs() {
    String[] logs = new String[logEvents.size()];
    Layout currentLayout = new PatternLayout();
    for (int i = 0; i < this.logEvents.size(); i++) {
      logs[i] = currentLayout.format(this.logEvents.get(i));
    }
    return logs;
  }

  @Override
  public long getLogCount() {
    return this.logEvents.size();
  }

}