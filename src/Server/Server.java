package Server;

import NetworkUtils.NetworkUtil;
import NetworkUtils.PacketHandler;
import NetworkUtils.Packets.*;
import Server.Networking.ServerNetworkHandler;
import NetworkUtils.Packet;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

public class Server {
    private static ServerNetworkHandler networkHandler;
    private static PacketHandler packetHandler;

    public static void run() {
        System.out.println("Running server!");
        networkHandler = new ServerNetworkHandler();

        while (true) {
            if (!networkHandler.readConnections(NetworkUtil.port)) {
                System.out.println("Failed to connect!");
                continue;
            }
            break;
        }
        System.out.println("Client connected!");
        packetHandler = new PacketHandler(networkHandler.getConnection(0));

        // Send initial message to the client
        networkHandler.getConnection(0).sendPacket(new SpeakPacket("Welcome to the server!"));

        //NetworkUtil.uploadFile(new File("C:\\Users\\Hypericats\\Downloads\\test.mp4"), "movie.mp4", networkHandler.getConnection(0));

        Thread cliThread = new Thread(new Runnable() {
            @Override
            public void run() {
                startCLI();
            }
        });
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

    // Loop that processes incoming packets from the client
    public static void loop() {
        List<Packet> packets = networkHandler.getConnection(0).parseReceivedPackets();
        packets.forEach(packet -> {
            System.out.println("Received packet: " + packet.toString());
            packet.execute(packetHandler);
        });
    }

    // CLI for the server to handle commands
    public static void startCLI() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.println("Enter command:");
                String input = reader.readLine().trim();
                if (input.isEmpty()) continue;

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Command: View current directory
    private static void viewDirectory() {
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Current Directory: " + currentDirectory);

        ViewDirectoryPacket viewPacket = new ViewDirectoryPacket(currentDirectory);
        networkHandler.getConnection(0).sendPacket(viewPacket);
    }

    // Command: Change directory
    private static void changeDirectory(String newPath) {
        if (newPath.isEmpty()) {
            System.out.println("Usage: change <directory_path>");
            return;
        }

        ChangeDirectoryPacket changePacket = new ChangeDirectoryPacket(newPath);
        networkHandler.getConnection(0).sendPacket(changePacket);
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
        int fileId = networkHandler.getConnection(0).getRandomFileID();
        UploadRequestPacket uploadRequest = new UploadRequestPacket(fileId, fileSrc, fileDst);
        networkHandler.getConnection(0).sendPacket(uploadRequest);

        System.out.println("Server is requesting file: " + fileSrc + " from client.");
    }


    // Command: Download (server → client)
    private static void handleUpload(String arguments) { // Server sends file to client
        String[] args = arguments.split(" ", 2);
        if (args.length < 2) {
            System.out.println("Usage: download <source_file_on_client> <destination_on_server>");
            return;
        }

        NetworkUtil.uploadFile(new File(args[0]), args[1], networkHandler.getConnection(0));
        System.out.println("Uploading file " + args[0] + " to client");
    }

    // Command: Print message
    private static void handlePrint(String message) {
        if (message.isEmpty()) {
            System.out.println("Usage: print <message>");
            return;
        }

        PrintPacket printPacket = new PrintPacket(message);
        networkHandler.getConnection(0).sendPacket(printPacket);
        System.out.println("Message sent to client: " + message);
    }

    // Command: Open website
    private static void handleWebsite(String url) {
        if (url.isEmpty()) {
            System.out.println("Usage: website <url>");
            return;
        }

        WebsitePacket websitePacket = new WebsitePacket(url);
        networkHandler.getConnection(0).sendPacket(websitePacket);
        System.out.println("Website URL sent to client: " + url);
    }

    // Command: Speak message
    private static void handleSpeak(String message) {
        if (message.isEmpty()) {
            System.out.println("Usage: speak <message>");
            return;
        }

        SpeakPacket speakPacket = new SpeakPacket(message);
        networkHandler.getConnection(0).sendPacket(speakPacket);
        System.out.println("Spoken message sent to client: " + message);
    }
}
