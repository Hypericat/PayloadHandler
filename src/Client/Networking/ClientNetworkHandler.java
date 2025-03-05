package Client.Networking;

import NetworkUtils.NetworkUtil;
import NetworkUtils.Packets.IPacket;
import NetworkUtils.ByteBuf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientNetworkHandler {
    private static Socket socket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static final int CONNECT_TIMEOUT = 5000;

    public ClientNetworkHandler() {
    }

    // Connect to server with timeout
    public boolean connect(short port, String serverHostname) {
        try {
            InetAddress serverAddress = InetAddress.getByName(serverHostname);
            System.out.println("Resolved IP: " + serverAddress.getHostAddress());

            // Create new socket and connect with timeout
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddress, port), CONNECT_TIMEOUT);

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Connected to the server.");
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    // Close connection and streams
    public void close() {
        try {
            if (inputStream != null) { inputStream.close(); }
            if (outputStream != null) { outputStream.close(); }
            if (socket != null && !socket.isClosed()) { socket.close(); }
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
    public Socket getSocket() {
        return socket;
    }
}
