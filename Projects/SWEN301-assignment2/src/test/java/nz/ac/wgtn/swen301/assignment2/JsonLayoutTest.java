package nz.ac.wgtn.swen301.assignment2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class JsonLayoutTest {

  @Test
  public void test_JsonLayout1(){
	String message = "A random message";
    Logger logger = Logger.getLogger(JsonLayout.class.toString());
    long startTime = System.currentTimeMillis();
    
    LoggingEvent event = new LoggingEvent(this.getClass().toString(),
        logger,
        startTime,
        Level.WARN,
        message,
        Thread.currentThread().getName(),
        null,null,null,null);

    JsonLayout l = new JsonLayout();
    ObjectMapper m = new ObjectMapper();
    JsonNode node = null;
    try {
      node = m.readTree(l.format(event));
    } catch (Exception e){
      e.printStackTrace();
      fail("JSON node not valid");
    }

    assertEquals(node.get("logger").toString(), "\"class nz.ac.wgtn.swen301.assignment2.JsonLayout\"");
    assertEquals(node.get("level").toString(), "\"WARN\"");
    assertEquals(node.get("starttime").toString(), "\""+startTime+"\"");
    assertEquals(node.get("thread").toString(), "\"main\"");
    assertEquals(node.get("message").toString(), "\"A random message\"");


  }

  @Test
  public void test_JsonLayout2(){
	String message = "Pass if this is equal to the result";
    Logger logger = Logger.getLogger(JsonLayout.class.toString()+"foo");
    long startTime = System.currentTimeMillis();
    
    LoggingEvent event = new LoggingEvent(this.getClass().toString(),
        logger,
        startTime,
        Level.WARN,
        message,
        Thread.currentThread().getName(),
        null,null,null,null);


    JsonLayout l = new JsonLayout();
    ObjectMapper m = new ObjectMapper();
    JsonNode node = null;
    try {
      node = m.readTree(l.format(event));
    } catch (Exception e){
      e.printStackTrace();
      fail("Test failed");
    }

    assertNotEquals(node.get("logger").toString(), "\"class nz.ac.wgtn.swen301.assignment2.JsonLayout\"");
    assertEquals(node.get("level").toString(), "\"WARN\"");
    assertEquals(node.get("starttime").toString(), "\""+startTime+"\"");
    assertEquals(node.get("thread").toString(), "\"main\"");
    assertEquals(node.get("message").toString(), "\"Pass if this is equal to the result\"");


  }

  @Test
  public void test_JSONLayout3(){
    Logger logger = Logger.getLogger(JsonLayout.class.toString());
    long startTime = System.currentTimeMillis();
    String message = "JsonLayout test three";

    LoggingEvent event = new LoggingEvent(this.getClass().toString(),
        logger,
        startTime,
        Level.ALL,
        message,
        Thread.currentThread().getName(),
        null,null,null,null);


    JsonLayout l = new JsonLayout();
    ObjectMapper m = new ObjectMapper();
    JsonNode node = null;
    try {
      node = m.readTree(l.format(event));
    } catch (Exception e){
      e.printStackTrace();
      fail("Test failed");
    }

    assertEquals(node.get("logger").toString(), "\"class nz.ac.wgtn.swen301.assignment2.JsonLayout\"");
    assertNotEquals(node.get("level").toString(), "\"WARN\"");
    assertEquals(node.get("starttime").toString(), "\""+startTime+"\"");
    assertEquals(node.get("thread").toString(), "\"main\"");
    assertEquals(node.get("message").toString(), "\"JsonLayout test three\"");


  }

  @Test
  public void test_JsonLayout4(){
	  JsonLayout l = new JsonLayout();
    assertFalse(l.ignoresThrowable());
    l.activateOptions();
  }
  
  @Test
  public void test_JsonLayout5(){
	  JsonLayout layout = new JsonLayout();

    try {
    	layout.format(null);
      fail("Test failed");
    } catch (NullPointerException ignored){ }

  }

}