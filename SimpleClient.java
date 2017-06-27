package homeWorkClientServer;

import java.io.*;
import java.net.*;
import java.util.*;

class SimpleClient {

    final String SERVER_ADDR = "127.0.0.1"; // or "localhost"
    final int SERVER_PORT = 2048;
    final String PROMPT = "$ ";
    final String EXIT_COMMAND = "exit"; // command for exit
    final String CONNECTION_START = "Connection to server established.";
    final String CONNECTION_CLOSED = "Connection closed.";

    Socket socket;
    Scanner scanner;
    PrintWriter writer;

    public static void main(String[] args) {
        new SimpleClient();
    }

    SimpleClient() {
        scanner = new Scanner(System.in); // for keyboard input
        String message;
        try {
            socket = new Socket(SERVER_ADDR, SERVER_PORT);
            System.out.println(CONNECTION_START);
            writer = new PrintWriter(socket.getOutputStream());
            Thread listener = new Thread(new Listener());
            listener.start();

            do {
                System.out.print(PROMPT);
                message = scanner.nextLine();
                writer.println(scanner.nextLine());
                writer.flush();
            } while (!message.equals(EXIT_COMMAND));

            writer.close();
            socket.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(CONNECTION_CLOSED);
    }

    class Listener implements Runnable {
        BufferedReader reader;

        Listener() {
            try {
                reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
            } catch (Exception ex) {
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

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}