package AdminClient;

import Client.Networking.ClientNetworkHandler;
import NetworkUtils.NetworkUtil;
import NetworkUtils.Packets.IPacket;
import NetworkUtils.PacketRegistry;

import java.util.Scanner;

public class AdminClient {

    public static ClientNetworkHandler networkHandler;

    public static void run() {
        System.out.println("Running admin client!");
        networkHandler = new ClientNetworkHandler();

        // Init connection to server
        while (true) {
            if (!networkHandler.connect(NetworkUtil.port, NetworkUtil.serverDDNS)) {
                System.out.println("Connection failed. Attempting to reconnect...");
                try {
                    Thread.sleep(1000); // Suspend execution
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            break;
        }
        System.out.println("Established a connection!");

        // Start manual packet sender for admin commands
        manualPacketSender();
    }

    // Allows user to manually input admin command and send to the server
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

                // Use existing packet system to create the corresponding packet by packetID
                IPacket packet = PacketRegistry.createPacket(packetID);

                if (packet == null) {
                    System.out.println("No packet registered with ID: " + input);
                } else {
                    // Send the packet using NetworkUtils
                    if (NetworkUtil.sendPacket(networkHandler, packet)) {
                        System.out.println("Sent packet: " + packet);
                    } else {
                        System.out.println("Failed to send packet.");
                    }

                    // Optionally, receive a response from the server (if the server sends back any response)
                    IPacket responsePacket = NetworkUtil.receivePacket(networkHandler);
                    if (responsePacket != null) {
                        System.out.println("Received response from server: " + responsePacket);
                    } else {
                        System.out.println("No response from server.");
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
