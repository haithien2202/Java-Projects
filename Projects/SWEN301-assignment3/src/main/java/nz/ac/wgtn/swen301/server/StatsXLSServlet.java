package nz.ac.wgtn.swen301.server;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("serial")
public class StatsXLSServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    procressXLS(resp);
  }

  public void procressXLS(HttpServletResponse resp) throws IOException {
    resp.setContentType("application/vnd.ms-excel");
    OutputStream out = resp.getOutputStream();
    Persistency p = new Persistency();
    ArrayList<String> all_levels = p.getAll_levels();
    HashMap<String, int[]> table = p.getLogLevels();

    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("XLS Log Stats");

    int rowCount = 0;
    Row row = sheet.createRow(rowCount);
    int columnCount = 1;
    Cell cell = row.createCell(0);
    cell.setCellValue("Logger");
    for(String header : all_levels){
      cell = row.createCell(columnCount++);
      cell.setCellValue(header);
    }


    for (String logger : table.keySet()) {
      columnCount = 1;
      row = sheet.createRow(++rowCount);
      cell = row.createCell(0);
      cell.setCellValue(logger);
      for (Integer field : table.get(logger)) {
        cell = row.createCell(columnCount++);
        cell.setCellValue(field);
      }
    }
    //cleanup
    workbook.write(out);
    workbook.close();
    out.flush();
    out.close();
  }
}