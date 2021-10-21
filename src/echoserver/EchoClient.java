package echoserver;

import java.net.*;
import java.io.*;


public class EchoClient {
  public static final int PORT_NUMBER = 6013;
  public static final int BUFFER_SIZE = 1024;


  public static void main(String[] args) {
    String serverName;

    // Use localhost unless we're given a server name on the command line.
    if (args.length == 0) {
      serverName = "127.0.0.1";
    } else {
      serverName = args[0];
    }

    try {
      Socket socket = new Socket(serverName, PORT_NUMBER);
      InputStream socketIn = socket.getInputStream();
      OutputStream socketOut = socket.getOutputStream();
      byte[] buffer = new byte[BUFFER_SIZE];
      int bytesRead;

      // Read from the stdin and send it over the socket.
      // If we're at the end of the stdin, exit the loop.
      while ((bytesRead = System.in.read(buffer)) != -1) {
        socketOut.write(buffer, 0, bytesRead);
        socketOut.flush();

        // Read data back from the socket and write it to the stdout.
        bytesRead = socketIn.read(buffer);
        System.out.write(buffer, 0, bytesRead);
        System.out.flush();
      }

      // Clean up.
      // Shut down our "output" half of the socket.
      socket.shutdownOutput();
      // Wait for the server to shut down its "output" half.
      while ((bytesRead = socketIn.read(buffer)) != -1) {
        // Read any data that's still coming back from the server, and write it
        // to the standard output.
        System.out.write(buffer, 0, bytesRead);
        System.out.flush();
      }

      socket.close();
    } catch (IOException ioe) {
      System.err.println("Unexpected exception when talking to server:");
      ioe.printStackTrace();
    }
  }
}
