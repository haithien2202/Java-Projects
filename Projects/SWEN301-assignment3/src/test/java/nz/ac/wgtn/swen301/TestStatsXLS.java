package nz.ac.wgtn.swen301;

import com.google.gson.JsonObject;
import nz.ac.wgtn.swen301.server.LogsServlet;
import nz.ac.wgtn.swen301.server.Persistency;
import nz.ac.wgtn.swen301.server.StatsXLSServlet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.Assert.assertEquals;

public class TestStatsXLS {

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
  public void testContentTypeXLS() throws ServletException, IOException {
    StatsXLSServlet service = new StatsXLSServlet();
    service.doGet(request, response);
    assertEquals(response.getContentType(), "application/vnd.ms-excel");
  }

  @Test
  public void testStatusXLS() throws ServletException, IOException {
    StatsXLSServlet service = new StatsXLSServlet();
    service.doGet(request, response);
    assertEquals(response.getStatus(), 200);
  }

  @SuppressWarnings("resource")
  @Test
  public void testValidXLS() throws IOException, ServletException {
    StatsXLSServlet service = new StatsXLSServlet();
    service.doGet(request, response);
    byte[] content = response.getContentAsByteArray();
    InputStream stream = new ByteArrayInputStream(content);

    XSSFWorkbook wk = new XSSFWorkbook(stream);
    Sheet sheet = wk.getSheet("XLS Log Stats");
    Row header = sheet.getRow(0);

    assertEquals(header.getCell(header.getFirstCellNum()).toString(), "Logger");

    int count = 0;
    for(int i = 1; i < header.getLastCellNum(); i++){
      assertEquals(header.getCell(i).getStringCellValue(), levels.get(count));
      count++;
    }

    int logs = 0;
    int rowNum;
    if(sheet.getLastRowNum() == 1) {
      rowNum = 2;
    }
    else{
      rowNum = sheet.getLastRowNum() + 1;
    }


    for (int i = 1; i < rowNum; i++) {       //for each line;
      Row nextRow = sheet.getRow(i);
      for (int j = 1; j < nextRow.getLastCellNum(); j++) {//for each element in the line
        Cell cell = nextRow.getCell(j);
        assert cell.getNumericCellValue() >= 0;   //make sure the number of logs of that type is not a negative
        logs += cell.getNumericCellValue();
      }
    }
    assert p.getDB().size() == logs;
  }

  public void generateLogs() throws ServletException, IOException {
    p.clearDB();
    Random r = new Random();
    for (int i = 0; i < 10; i++) {
      JsonObject json = buildJSON("test" + i, levels.get(r.nextInt(8)), "example.logger");
      request.setContent(json.toString().getBytes(StandardCharsets.UTF_8));
      service.doPost(request, response);
    }
    for (int i = 0; i < 10; i++) {
      JsonObject json = buildJSON("test" + i, levels.get(r.nextInt(8)), "example.logger" + i);
      request.setContent(json.toString().getBytes(StandardCharsets.UTF_8));
      service.doPost(request, response);
    }
  }

  public JsonObject buildJSON(String message, String level, String logger) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", String.valueOf(UUID.randomUUID()));
    jsonObject.addProperty("message", message);
    jsonObject.addProperty("timestamp", ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()));
    jsonObject.addProperty("thread", "main");
    jsonObject.addProperty("logger", logger);
    jsonObject.addProperty("level", level);
    jsonObject.addProperty("errorDetails", "string");
    return jsonObject;
  }

}