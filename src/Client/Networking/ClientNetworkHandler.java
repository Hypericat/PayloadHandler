package Client.Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientNetworkHandler {
    private static Socket socket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;

    public ClientNetworkHandler() {

    }

    // Resolves ip from hostname
    public boolean connect(short port, String serverHostname) {
        try {
            InetAddress serverAddress = InetAddress.getByName(serverHostname);
            System.out.println("Resolved IP: " + serverAddress.getHostAddress());

            // Connect to server
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddress, port), 2000);
            //inputStream = new DataInputStream(socket.getInputStream());
            //outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return false;
        }
        return true;
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
                //connect();  // reconnect
            }
        }
    }

    // Gracefully close the connection
    public void closeConnection() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
