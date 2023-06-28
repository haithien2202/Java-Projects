package nz.ac.wgtn.swen301.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@SuppressWarnings("serial")
public class StatsServlet extends HttpServlet {

  public StatsServlet(){}

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processTable(req, resp);
  }

  private void processTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    response.setContentType("text/html");
    Persistency p = new Persistency();
    PrintWriter out = response.getWriter();

    out.println("<html>");
    out.println("<head>");
    out.println("<title>Log Information</title>");
    out.println("</head>");
    out.println("<body>");

    out.println("<h1>Echoing Log Information</h1>");
    out.println("<table border><th>Logger</th><th>ALL</th><th>TRACE</th><th>DEBUG</th><th>INFO</th><th>WARN</th><th>ERROR</th><th>FATAL</th><th>OFF</th>");
    // list headers send by the client
    LinkedHashMap<String, int[]> lg = p.getLogLevels();
    for(String logger_name : lg.keySet()) {
      out.print("<tr><td>");
      out.print(logger_name);
      out.print("<td>");
      out.print(lg.get(logger_name)[0]);
      out.print("</td>");
      out.print("<td>");
      out.print(lg.get(logger_name)[1]);
      out.print("</td>");
      out.print("<td>");
      out.print(lg.get(logger_name)[2]);
      out.print("</td>");
      out.print("<td>");
      out.print(lg.get(logger_name)[3]);
      out.print("</td>");
      out.print("<td>");
      out.print(lg.get(logger_name)[4]);
      out.print("</td>");
      out.print("<td>");
      out.print(lg.get(logger_name)[5]);
      out.print("</td>");
      out.print("<td>");
      out.print(lg.get(logger_name)[6]);
      out.print("</td>");
      out.print("<td>");
      out.print(lg.get(logger_name)[7]);
      out.print("</td>");
    }

    out.println("</td></tr>");
    out.println("</table>");
    out.println("</body>");
    out.println("</html>");

    out.close();
  }
}