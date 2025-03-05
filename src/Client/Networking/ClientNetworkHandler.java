package Client.Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientNetworkHandler {
    private static Socket socket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;

    public ClientNetworkHandler() {
    }

    // Connect to the server
    public boolean connect(short port, String serverHostname) {
        try {
            InetAddress serverAddress = InetAddress.getByName(serverHostname);
            System.out.println("Resolved IP: " + serverAddress.getHostAddress());

            // Connect to the server
            socket = new Socket(serverAddress, port);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Connected to the server.");
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    // Getter method for inputStream
    public DataInputStream getInputStream() {
        return inputStream;
    }

    // Getter method for outputStream
    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    // Close the connection and streams
    public void close() {
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
