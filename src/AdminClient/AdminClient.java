package AdminClient;

import Client.Networking.ClientNetworkHandler;
import NetworkUtils.NetworkUtil;  // Correctly reference NetworkUtils
import NetworkUtils.Packets.AdminIDPacket;
import NetworkUtils.Packets.IPacket;
import NetworkUtils.PacketRegistry;

import java.util.Scanner;

public class AdminClient {

    private static ClientNetworkHandler networkHandler;

    public static void run() {
        System.out.println("Running Admin Client!");
        networkHandler = new ClientNetworkHandler();

        // Init connection
        while (true) {
            if (!networkHandler.connect(NetworkUtil.port, NetworkUtil.serverDDNS)) {  // Use ClientNetworkHandler for connection
                System.out.println("Connection failed. Attempting to reconnect...");
                try {
                    Thread.sleep(1000); // Wait before retrying
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            break;
        }
        System.out.println("Established a connection!");

        // Send admin packet to server to identify as an Admin client
        sendAdminPacket();

        // Start manual packet sender to send packets on command
        manualPacketSender();
    }

    private static void sendAdminPacket() {
        // Create the Admin Identification Packet
        AdminIDPacket adminPacket = new AdminIDPacket();

        // Send the packet using NetworkUtils' sendPacket method (this method expects an IPacket, not the ClientNetworkHandler)
        if (NetworkUtil.sendPacket(adminPacket, networkHandler)) {  // Pass the packet, not the network handler
            System.out.println("Admin client identification packet sent!");
        } else {
            System.out.println("Failed to send Admin client identification packet.");
        }
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
                    // Send the packet to the server using NetworkUtils
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
