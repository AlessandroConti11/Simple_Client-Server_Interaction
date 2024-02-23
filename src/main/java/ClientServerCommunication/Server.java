package ClientServerCommunication;


import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The Server class is responsible for creating a server that can accept multiple client connections.
 */
public class Server {
    /**
     * Server port number.
     */
    private final int portNumber;


    /**
     * Server constructor.
     *
     * @param portNumber the server port number
     */
    public Server(int portNumber) {
        this.portNumber = portNumber;
    }


    /**
     * Starts the server and listens for incoming client connections.
     */
    public void startServer() {
        //the server creates multiple threads in order to accept multiple connections from clients
        ExecutorService executor = Executors.newCachedThreadPool(); //create threads but if possible reuse existing ones
        ServerSocket serverSocket;

        //socket creation
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.err.println(e.getMessage()); //port not available
            return;
        }

        System.out.println("Server ready");

        //server is always listening
        while (true) {
            try {
                //accept connection
                Socket socket = serverSocket.accept();
                System.out.println("Accept");
                executor.submit(new ServerClientHandler(socket));
            } catch(IOException e) {
                break;
            }
        }
        //kill all thread
        executor.shutdown();
    }

    private static String getLocalIPAddress() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface current = interfaces.nextElement();
            if (!current.isUp() || current.isLoopback() || current.isVirtual()) {
                continue;
            }
            Enumeration<InetAddress> addresses = current.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress currentAddr = addresses.nextElement();
                if (currentAddr.isLoopbackAddress()) {
                    continue;
                }
                if (currentAddr instanceof Inet4Address) {
                    return currentAddr.getHostAddress();
                }
            }
        }
        return null;
    }


    /**
     * Server main.
     *
     * @param args input args.
     */
    public static void main(String[] args) {
        Server server = new Server(1234);

        try {
            System.out.println("Server IP: " + getLocalIPAddress());
        } catch (SocketException e) {
            System.out.println("Gets server IP error.");
        }

        server.startServer();
    }
}
