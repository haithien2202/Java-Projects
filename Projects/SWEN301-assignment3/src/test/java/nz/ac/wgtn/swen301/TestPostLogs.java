package nz.ac.wgtn.swen301;

import com.google.gson.JsonObject;
import nz.ac.wgtn.swen301.server.LogsServlet;
import nz.ac.wgtn.swen301.server.Persistency;
import nz.ac.wgtn.swen301.server.StatsXLSServlet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestPostLogs {

  MockHttpServletRequest request;
  MockHttpServletResponse response;
  LogsServlet service;
  StatsXLSServlet statsService;

  Persistency p = new Persistency();
  ArrayList<String> levels = p.getAll_levels();

  @Before
  public void init() throws ServletException, IOException {
    service = new LogsServlet();
    statsService = new StatsXLSServlet();
    response = new MockHttpServletResponse();
    request = new MockHttpServletRequest();
    generateLogs();
  }

  @Test
  public void testNullLogEvent() {
    try {
      service.doPost(request, response);
    } catch (ServletException | IOException e) {
      e.printStackTrace();
    }
    assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
  }

  @Test
  public void testInvalidJson() {

    JsonObject json = new JsonObject();
    json.addProperty("id", "");
    json.addProperty("message", "");
    json.addProperty("timestamp", ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()));
    json.addProperty("thread", "");
    json.addProperty("logger", "");
    json.addProperty("level", "");
    json.addProperty("errorDetails", "");

    request.setContent(json.toString().getBytes(StandardCharsets.UTF_8));

    try {
      service.doPost(request, response);
    } catch (ServletException | IOException e) {
      e.printStackTrace();
    }
    assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
  }

  @Test
  public void testValidJson(){
    Random r = new Random();
    JsonObject json = buildJSON("", "test", levels.get(r.nextInt(8)), "example.logger");

    request.setContent(json.toString().getBytes(StandardCharsets.UTF_8));

    try {
      service.doPost(request, response);
    } catch (ServletException | IOException e) {
      e.printStackTrace();
    }
    assertEquals(HttpServletResponse.SC_OK, response.getStatus());
  }

  @Test
  public void testDuplicateID(){
    Random r = new Random();
    JsonObject first = buildJSON("this is the same", "test", levels.get(r.nextInt(8)), "example.logger");
    JsonObject second = buildJSON("this is the same", "test", levels.get(r.nextInt(8)), "example.logger");
    try {
      request.setContent(first.toString().getBytes(StandardCharsets.UTF_8));
      service.doPost(request, response);
      request.setContent(second.toString().getBytes(StandardCharsets.UTF_8));
      service.doPost(request, response);

    } catch (ServletException | IOException e) {
      e.printStackTrace();
    }
    assertEquals(HttpServletResponse.SC_CONFLICT, response.getStatus());
  }

  public void generateLogs() throws ServletException, IOException {
    p.clearDB();
    Random r = new Random();
    for (int i = 0; i < 10; i++) {
      JsonObject json = buildJSON("","test" + i, levels.get(r.nextInt(8)), "example.logger");
      request.setContent(json.toString().getBytes(StandardCharsets.UTF_8));
      service.doPost(request, response);
    }
  }

  @Test
  public void testValidContentType() throws IOException, ServletException {

    request.setParameter("limit", "1");
    request.setParameter("level", "1");
    service.doGet(request,response);

    assertTrue(response.getContentType().startsWith("application/json"));
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