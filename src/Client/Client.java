package Client;

import Client.Networking.ClientNetworkHandler;
import NetworkUtils.NetworkUtil;
import NetworkUtils.Packets.IPacket;
import NetworkUtils.PacketRegistry;

import java.util.Scanner;

public class Client {

    public static final int secondsRetryConnect = 0;
    public static final int msRetryConnect = 0;
    public static final int msRetryFinalConnect = secondsRetryConnect * 1000 + msRetryConnect;

    private static ClientNetworkHandler networkHandler;

    public static void run() {
        System.out.println("Running client!");
        networkHandler = new ClientNetworkHandler();

        // Init connection
        while (true) {
            if (!networkHandler.connect(NetworkUtil.port, NetworkUtil.serverDDNS)) {
                System.out.println("Connection failed. Attempting to reconnect...");
                try {
                    Thread.sleep(msRetryFinalConnect); // Suspend execution
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            break;
        }
        System.out.println("Established a connection!");

        // Start manual packet sender to send packets on command
        manualPacketSender();
    }

    // Allows user to manually input packet ID (hexadecimal) and send to server
    private static void manualPacketSender() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a packet ID (hex) to send (or type 'exit' to quit):");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                // Parse the input packet ID as a hexadecimal number
                byte packetID = (byte) Integer.parseInt(input, 16);
                // Use PacketRegistry to create the corresponding packet by packetID
                IPacket packet = PacketRegistry.createPacket(packetID);
                if (packet == null) {
                    System.out.println("No packet registered with ID: " + input);
                } else {
                    if (NetworkUtil.sendPacket(packet, networkHandler)) {
                        System.out.println("Sent packet: " + packet);
                    } else {
                        System.out.println("Failed to send packet.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid hexadecimal value.");
            }
        }
    }

    public static void main(String[] args) {
        run();
    }
}
