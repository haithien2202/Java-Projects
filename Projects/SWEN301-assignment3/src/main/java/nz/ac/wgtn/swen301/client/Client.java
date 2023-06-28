package nz.ac.wgtn.swen301.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;

public class Client {

  public static void main(String args[]) {

    if(args.length != 2){
      System.out.println("Incorrect Parameters");
    }
    String format = args[0];
    String fileName = args[1];

    var client = HttpClient.newHttpClient();

    if(format.equals("excel")){
      try{
        var request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/resthome4logs/statsxls")).setHeader("Content-Disposition", "attachment;filename=" + fileName).build();
        client.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get(fileName)));
      } catch(Exception e){
        System.out.println("404: XLS Server not found");
      }
    } else if(format.equals("csv")) {
      try {
        var request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/resthome4logs/statscsv")).setHeader("Content-Disposition", "attachment;filename=" + fileName).build();
        client.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get(fileName)));
      } catch (Exception e) {
        System.out.println("404: CSV Server not found");
      }
    }
  }
}