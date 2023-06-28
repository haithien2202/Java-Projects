package nz.ac.wgtn.swen301;

import com.google.gson.JsonObject;
import nz.ac.wgtn.swen301.server.LogsServlet;
import nz.ac.wgtn.swen301.server.Persistency;
import nz.ac.wgtn.swen301.server.StatsCSVServlet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.Assert.assertEquals;

public class TestStatsCSV {
    MockHttpServletRequest request;
    MockHttpServletResponse response;
    LogsServlet service;

    Persistency p = new Persistency();
    ArrayList<String> levels = p.getAll_levels();

    @Before
    public void init() throws ServletException, IOException {
        service = new LogsServlet();
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        generateLogs();
    }

    @Test
    public void parseCSVTest_1() {
        StatsCSVServlet csvServlet = new StatsCSVServlet();
        csvServlet.processCSV(response);

        String header = "attachment; filename = statscsv.csv";
        assertEquals(header, response.getHeaderValue("content-disposition"));
    }

    @Test
    public void testValidCSVresponse() throws IOException, ServletException {
        StatsCSVServlet csvServlet = new StatsCSVServlet();
        csvServlet.doGet(request, response);
        assert response.getStatus() == 200;
    }

    @Test
    public void testValidCSV() throws IOException, ServletException {
        StatsCSVServlet service = new StatsCSVServlet();
        service.doGet(request, response);
        assert response.getContentAsString().startsWith("Logger\tALL\tTRACE\tDEBUG\tINFO\tWARN\tERROR\tFATAL\tOFF\n");
        String[] content = response.getContentAsString().split("\n");

        int logs = 0;
        for (int i = 1; i < content.length; i++) {       //for each line
            String[] line = content[i].split("\t");
            for (int j = 1; j < line.length; j++) {      //for each element in the line
                assert Integer.parseInt(line[j]) >= 0;   //make sure the number of logs of that type is not a negative
                logs += Integer.parseInt(line[j]);
            }
        }
        assert p.getDB().size() == logs;
    }

    public void generateLogs() throws ServletException, IOException {
        for(int i = 0; i < 5; i++){
            JsonObject json = buildJSON("test" + i, levels.get(i));
            request.setContent(json.toString().getBytes(StandardCharsets.UTF_8));
            service.doPost(request, response);
        }
    }

    public JsonObject buildJSON(String message, String level) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", String.valueOf(UUID.randomUUID()));
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("timestamp", ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()));
        jsonObject.addProperty("thread", "main");
        jsonObject.addProperty("logger", "example.logger");
        jsonObject.addProperty("level", level);
        jsonObject.addProperty("errorDetails", "string");
        return jsonObject;
    }
}