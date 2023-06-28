package nz.ac.wgtn.swen301.server;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class LogsServlet extends HttpServlet {

  Persistency p = new Persistency();
  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  ArrayList<String> levels = p.getAll_levels();

  //constructor with no parameters as per assignment
  public LogsServlet(){}


  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    resp.setContentType("application/json");

    String _limit = req.getParameter("limit");
    String _level = req.getParameter("level");

    //sets the status code to '400' when parameters are incorrect
    if(!checkParameters(req)) resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    else {
      //converts parameter values to appropriate types for process
      int limit = Integer.parseInt(_limit);
      String level = _level.toUpperCase();
      int level_index = levels.indexOf(level);

      int count = 0;
      PrintWriter pw = resp.getWriter();
      ArrayList<LogEvent> jsonList = new ArrayList<>();
      for (LogEvent d : p.getDB()) {
        if (count < limit && levels.indexOf(d.getLevel()) >= level_index){
          jsonList.add(d);
        }
      count++;
      }
      pw.println(gson.toJson(jsonList));
    }
  }


  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    // converts log into a 'LogEvent' object which is then stored.
    StringBuilder sb = new StringBuilder();
    BufferedReader br = req.getReader();
    String line;
    while((line = br.readLine()) != null){
      sb.append(line);
    }

    LogEvent log = gson.fromJson(sb.toString(), LogEvent.class);
    if(checkJson(log) && checkID(log)){
      p.postLog(log);
      //if the post is valid, then set status to 200
      resp.setStatus(HttpServletResponse.SC_OK);
    }else {

      //if post is invalid, set status to 409
      if(!checkJson(log)) resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      if(!checkID(log)) resp.setStatus(HttpServletResponse.SC_CONFLICT);
    }
  }

  // deletes all logs
  @Override
  public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    p.clearDB();
    if(p.getDB().size() == 0) resp.setStatus(HttpServletResponse.SC_OK);
    else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }


  //checks ID....? maybe expand to more
  private boolean checkID(LogEvent lg){
    ArrayList<LogEvent> logs = p.getDB();
    try {
        for (LogEvent log : logs) {
          if (log != null && lg != null) {
            assertNotEquals(lg.getId(), log.getId());
          }
        }

    }catch(AssertionError e){
      return false;
    }
    return true;
  }

  private boolean checkJson(LogEvent lg){
    try {
      if(lg == null) return false;
      assertNotEquals("", lg.getMessage());
      assertNotEquals("", lg.getId());
      assertNotEquals("", lg.getLogger());
      assertNotEquals("", lg.getThread());
      assertNotEquals("", lg.getTimestamp());
      assertNotEquals("", lg.getLevel());
    }catch(AssertionError e){
      return false;
    }
    return true;
  }

  //checks the parameters - throws assertion error parameters or parameter names are invalid
  private boolean checkParameters(HttpServletRequest req){

    try {
      String limit = req.getParameter("limit");
      String level = req.getParameter("level");

      // if the parameters are null (parameter values are incorrectly inputted or incorrect parameter names)
      assertNotNull(limit);
      assertNotNull(level);

      // non-integer string and ensuring at least 1 log is viewed
      assertTrue(Integer.parseInt(limit) > 0);
      // level is not correct
      assertTrue(levels.contains(level.toUpperCase()));

    }catch (AssertionError e){
      return false;
    }

    return true;
  }

}