package nz.ac.wgtn.swen301.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

public class StatsCSVServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp){
   processCSV(resp);
  }

  public void processCSV(HttpServletResponse response) {
    response.setContentType("text/csv");
    response.addHeader("content-disposition", "attachment; filename = statscsv.csv");
    PrintWriter writer = null;
    try {
      writer = response.getWriter();
    } catch (IOException e) {
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    Persistency p = new Persistency();

    if (writer != null) {
      writer.print("Logger\tALL\tTRACE\tDEBUG\tINFO\tWARN\tERROR\tFATAL\tOFF\n");
      LinkedHashMap<String, int[]> table = p.getLogLevels();

      for (String logger : table.keySet()) {
        writer.print(logger + "\t");
        for (Integer count : table.get(logger)) {
          writer.print(count.toString() + "\t");
        }
        writer.print("\n");
      }
      writer.flush();
    }
    writer.close();
  }
}