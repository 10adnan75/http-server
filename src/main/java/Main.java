import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
      String line = reader.readLine();
      System.out.println(line);

      String HTTPRequest = line.split(" ", 0)[1], op = "";

      if (HTTPRequest.equals("/")) {
        op = "HTTP/1.1 200 OK\r\n\r\n";
      } else if (HTTPRequest.startsWith("/echo/")) {
        op = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + (HTTPRequest.length()-6) + "\r\n\r\n" + HTTPRequest.substring(6);
      } else {
        op = "HTTP/1.1 404 Not Found\r\n\r\n";
      }

      System.out.println(op);

      socket.getOutputStream().write(op.getBytes());
      System.out.println("Accepted a new connection: " + socket);
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
