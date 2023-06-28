package nz.ac.wgtn.swen301.assignment2.example;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import nz.ac.wgtn.swen301.assignment2.JsonLayout;
import nz.ac.wgtn.swen301.assignment2.MemAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**Log runner.
*
*/
public class LogRunner {

  private static int count = 0;

  /**
   * Generate a random log event.
   */
  public static void generateLogEvent() {
    //Exits after 2 minutes
    if (count >= 120) {
      System.exit(0);
    }
    Logger logger = Logger.getLogger("Log");
    MemAppender appender = new MemAppender();
    appender.setLayout(new JsonLayout());
    logger.addAppender(appender);
    //Set logger to lowest level
    logger.setLevel(Level.DEBUG);
    //Make Random log event
    Random r = new Random();
    int random = r.nextInt(5);
    switch (random) {
      case 0:
        logger.fatal("FATAL");
        break;
      case 1: 
        logger.warn("WARN");
        break;
      case 2: 
        logger.debug("DEBUG");
        break;
      case 3: 
        logger.warn("WARN");
        break;
      case 4: 
        logger.error("ERROR");
        break;
      default: 
    }
    count++;
    System.out.println(appender.getCurrentLogs().toString());
  }
  
  /**Main method.
  *
  */
  public static void main(String[] args) {
    Runnable logRunnable = LogRunner::generateLogEvent;
    ScheduledExecutorService logExec = Executors.newScheduledThreadPool(1);
    //Generate
    logExec.scheduleAtFixedRate(logRunnable, 0, 1, TimeUnit.SECONDS);
  }
}