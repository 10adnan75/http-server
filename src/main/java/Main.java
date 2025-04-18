import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {

    System.out.println("Logs from your program will appear here!");

    try {
      ServerSocket serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
    
      Socket socket = serverSocket.accept(); 
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String op = "";
      String HTTPRequest = reader.readLine().split(" ")[1];
      String host = reader.readLine().split(" ")[1];
      String accept = reader.readLine();
      String userAgent = reader.readLine().split(" ")[1];

      System.out.println("------------------------------------------");
      System.out.println("CLIENT REQUEST\n");
      System.out.println("1. HTTP: " + HTTPRequest);
      System.out.println("2. HOST NAME: " + host);
      System.out.println("3. STATUS: " + accept);
      System.out.println("4. USER AGENT: " + userAgent);

      if (HTTPRequest.equals("/")) {
        op = "HTTP/1.1 200 OK\r\n\r\n";
      } else if (HTTPRequest.startsWith("/echo/")) {
        op = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + (HTTPRequest.length()-6) + "\r\n\r\n" + HTTPRequest.substring(6);
      } else if (HTTPRequest.startsWith("/user-agent"))  {
        op = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + (userAgent.length()) + "\r\n\r\n" + userAgent;
      } else {
        op = "HTTP/1.1 404 Not Found\r\n\r\n";
      }

      System.out.println("------------------------------------------");
      System.out.println("SERVER RESPONSE\n\n" + op);
      System.out.println("------------------------------------------");

      socket.getOutputStream().write(op.getBytes());
      System.out.println("Accepted a new connection: " + socket);
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
