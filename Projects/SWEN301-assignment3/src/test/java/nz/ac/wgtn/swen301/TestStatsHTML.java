package nz.ac.wgtn.swen301;

import com.google.gson.JsonObject;
import nz.ac.wgtn.swen301.server.LogsServlet;
import nz.ac.wgtn.swen301.server.Persistency;
import nz.ac.wgtn.swen301.server.StatsCSVServlet;
import nz.ac.wgtn.swen301.server.StatsServlet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.ofPattern;

public class TestStatsHTML {

    MockHttpServletRequest request;
    MockHttpServletResponse response;
    LogsServlet service;
    StatsServlet statsService;

    Persistency p = new Persistency();
    ArrayList<String> levels = p.getAll_levels();

    @Before
    public void init() throws ServletException, IOException {
        service = new LogsServlet();
        statsService = new StatsServlet();
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        generateLogs();
    }

    @Test
    public void testValidHTMLresponse() throws IOException, ServletException {
        StatsServlet service = new StatsServlet();
        service.doGet(request, response);
        assert response.getStatus() == 200;
    }

    @Test
    public void testValidHTML() throws UnsupportedEncodingException {
        StatsCSVServlet service = new StatsCSVServlet();
        service.doGet(request, response);
        Document doc = Jsoup.parse(response.getContentAsString());
        String body = doc.body().wholeText();

        assert body.startsWith("Logger\tALL\tTRACE\tDEBUG\tINFO\tWARN\tERROR\tFATAL\tOFF\n");
        String[] body_content = body.split("\n");

        int logs = 0;
        for (int i = 1; i < body_content.length; i++) {     //for each line
            String[] line = body_content[i].split("\t");
            for (int j = 1; j < line.length; j++) {         //for each element in the line
                assert Integer.parseInt(line[j]) >= 0;      //make sure the number of logs of that type is not a negative
                logs += Integer.parseInt(line[j]);
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