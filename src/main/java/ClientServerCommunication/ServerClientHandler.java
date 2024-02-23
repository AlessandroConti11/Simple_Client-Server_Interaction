package ClientServerCommunication;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


/**
 * The ServerClientHandler class is responsible for managing the client-server connection.
 */
public class ServerClientHandler implements Runnable{
    /**
     * Client socket.
     */
    private final Socket socket;


    /**
     * Server client-handler constructor.
     *
     * @param socket the client socket.
     */
    public ServerClientHandler(Socket socket) {
        this.socket = socket;
    }


    /**
     * Implements the Runnable interface and represents the main logic of the client-server connection.
     * It reads input from the client, processes it, and sends a response back to the client.
     */
    public void run() {
        try {
            //input
            Scanner in = new Scanner(socket.getInputStream());
            //output
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            //reads and writes into the connection until it gets "quit"
            while (true) {
                String line = in.nextLine();
                System.out.println(socket + " sends: " + line);
                if (line.equals("quit")) {
                    break;
                } else {
                    out.println("Received: " + line);
                    out.flush();
                }
            }

            //closing streams and socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
