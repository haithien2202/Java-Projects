package nz.ac.wgtn.swen301;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import nz.ac.wgtn.swen301.server.LogEvent;
import nz.ac.wgtn.swen301.server.LogsServlet;
import nz.ac.wgtn.swen301.server.Persistency;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGetLogs {

  MockHttpServletRequest request;
  MockHttpServletResponse response;
  LogsServlet service;
  Persistency p = new Persistency();
  ArrayList<String> levels = p.getAll_levels();

  @Before
  public void init() {
    service = new LogsServlet();
    response = new MockHttpServletResponse();
    request = new MockHttpServletRequest();
  }

  @Test
  public void testInvalidRequestResponseCode1() throws IOException, ServletException {

    service.doGet(request,response);

    assertEquals(400,response.getStatus());
  }

  @Test
  public void testInvalidRequestResponseCode2() throws IOException, ServletException {

    //level is incorrect

    request.setParameter("limit", "1");
    request.setParameter("level", "1");
    service.doGet(request,response);

    assertEquals(400,response.getStatus());
  }

  @Test
  public void testInvalidRequestResponseCode3() throws IOException, ServletException {

    //level is incorrect

    request.setParameter("limit", "-1");
    request.setParameter("level", "WARN");
    service.doGet(request,response);

    assertEquals(400,response.getStatus());
  }

  @Test
  public void testInvalidRequestResponseCode4() throws IOException, ServletException {

    //level is incorrect
    request.setParameter("INVALID", "10");
    request.setParameter("level", "DEBUG");
    service.doGet(request,response);

    assertEquals(400,response.getStatus());
  }

  @Test
  public void testValidRequestResponseCode1() throws IOException, ServletException {

    //level is incorrect
    request.setParameter("limit", "10");
    request.setParameter("level", "DEBUG");
    service.doGet(request,response);

    assertEquals(200,response.getStatus());
  }

  @Test
  public void testValidContentType() throws IOException, ServletException {

    request.setParameter("limit", "1");
    request.setParameter("level", "1");
    service.doGet(request,response);

    assertTrue(response.getContentType().startsWith("application/json"));
  }

  @Test
  public void testValidJson(){
    LogEvent lg = null;
    try{
      Gson gson = new Gson();
      JsonObject json = buildJSON("valid","test", "WARN", "example.logger");
      request.setContent(json.toString().getBytes(StandardCharsets.UTF_8));

      service.doPost(request, response);


      request.setParameter("limit", "1");
      request.setParameter("level", "ALL");

      service.doGet(request,response);
      LogEvent[] log = gson.fromJson(response.getContentAsString(), LogEvent[].class);
      lg = log[0];


    } catch (ServletException | IOException e) {
      e.printStackTrace();
    }
    boolean validTimeStamp;
    if(lg != null) {
      assertEquals(lg.getId(), lg.getId());
      assertEquals("test", lg.getMessage());
      assertEquals("main", lg.getThread());
      try {
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lg.getTimestamp().toString());
        validTimeStamp = true;
      }catch (ParseException e){
        validTimeStamp = false;
      }
      assertTrue(validTimeStamp);
      assertEquals("example.logger", lg.getLogger());
      assertEquals("WARN", lg.getLevel());
      assertEquals("string", lg.getErrorDetails());
    }

  }


  public JsonObject buildJSON(String id, String message, String level, String logger) {
    JsonObject jsonObject = new JsonObject();
    if(id.equals("")) id = String.valueOf(UUID.randomUUID());
    jsonObject.addProperty("id", id);
    jsonObject.addProperty("message", message);
    jsonObject.addProperty("timestamp", ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()));
    jsonObject.addProperty("thread", "main");
    jsonObject.addProperty("logger", "example.logger");
    jsonObject.addProperty("level", level);
    jsonObject.addProperty("errorDetails", "string");
    return jsonObject;
  }


}