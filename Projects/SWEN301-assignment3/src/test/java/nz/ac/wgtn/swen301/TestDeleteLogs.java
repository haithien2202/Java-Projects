package nz.ac.wgtn.swen301;

import nz.ac.wgtn.swen301.server.LogsServlet;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestDeleteLogs {

  @Test
  public void testInvalidRequestResponseCode2() throws IOException, ServletException {

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    LogsServlet service = new LogsServlet();
    service.doDelete(request,response);

    assertEquals(200, response.getStatus());
  }
}