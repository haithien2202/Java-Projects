package nz.ac.wgtn.swen301.assignment2;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import nz.ac.wgtn.swen301.assignment2.JsonLayout;
import nz.ac.wgtn.swen301.assignment2.MemAppender;

import static org.junit.jupiter.api.Assertions.*;
public class MemAppenderTest {

  @Test
  public void test_MemAppender1(){
    Logger l = Logger.getLogger(JsonLayout.class.toString());
    MemAppender m = new MemAppender();
    l.addAppender(m);

    l.info("This is a test");
    assertEquals(m.getCurrentLogs().size(), 1);
  }

  @Test
  public void test_MemAppender2(){
    Logger l = Logger.getLogger(JsonLayout.class.toString());
    MemAppender m = new MemAppender();
    l.addAppender(m);

    for (int i = 0; i < 1050; i++){
      l.warn("test2");
    }
    long x = m.getDiscardedLogCount();
    assertEquals(x, 50);
  }

}