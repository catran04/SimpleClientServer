package homeWorkClientServer;

import java.io.*;
import java.net.*;

public class SimpleServer {

    final int SERVER_PORT = 2048;
    final String SERVER_START = "Server is started...";
    final String SERVER_STOP = "Server stopped.";
    final String CLIENT_JOINED = " client joined.";
    final String EXIT_COMMAND = "exit"; // command for exit


    ServerSocket server;
    Socket socket;

    public static void main(String[] args) {
        new SimpleServer();
    }

    SimpleServer() {
        try {
            server = new ServerSocket(SERVER_PORT);
            System.out.println(SERVER_START);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writer;
                socket = server.accept();
                System.out.println(CLIENT_JOINED);
                String message;
            writer = new PrintWriter(socket.getOutputStream());


            Thread listenerServer = new Thread(new ListenerServer());
            listenerServer.start();

            do {
                message = reader.readLine();
                writer.println(message);

            } while (!message.equalsIgnoreCase(EXIT_COMMAND));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                server.close();
                System.out.println(SERVER_STOP);
                socket.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * ClientHandler: service requests of clients
     */
    class ListenerServer implements Runnable {
        BufferedReader reader;

        ListenerServer() {
            try {
                reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        @Override
        public void run() {
            String message;
            try {
                do {
                    message = reader.readLine();
                    System.out.println(message);
                } while (!message.equalsIgnoreCase(EXIT_COMMAND));
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}