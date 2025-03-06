package Server;

import NetworkUtils.NetworkUtil;
import NetworkUtils.PacketHandler;
import NetworkUtils.Packets.*;
import Server.Networking.ServerNetworkHandler;
import NetworkUtils.Packet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
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

        // Start CLI interface for server commands
        startCLI();
        //networkHandler.getConnection(0).sendPacket(new SpeakPacket("Shut yo bitch ass up nigga, this shit pissing me off"));\
        NetworkUtil.uploadFile(new File("C:\\Users\\Hypericats\\Downloads\\robloxstudiobs.txt"), "superFile.txt", networkHandler.getConnection(0));

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
                // Display the command prompt
                System.out.println("Enter command (view, change, upload, download, print, website, speak): ");
                String command = reader.readLine();

                // Process the command
                if (command.equalsIgnoreCase("view")) {
                    viewDirectory();
                } else if (command.equalsIgnoreCase("change")) {
                    changeDirectory();
                } else if (command.equalsIgnoreCase("upload")) {
                    handleUploadRequest();
                } else if (command.equalsIgnoreCase("download")) {
                    handleDownloadRequest();
                } else if (command.equalsIgnoreCase("print")) {
                    handlePrint();
                } else if (command.equalsIgnoreCase("website")) {
                    handleWebsite();
                } else if (command.equalsIgnoreCase("speak")) {
                    handleSpeak();
                } else {
                    System.out.println("Unknown command! Try again.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Command: view
    private static void viewDirectory() {
        String currentDirectory = System.getProperty("user.dir");  // Get the current working directory
        System.out.println("Current Directory: " + currentDirectory);

        // Send the directory contents back to the client
        ViewDirectoryPacket viewPacket = new ViewDirectoryPacket(currentDirectory);
        networkHandler.getConnection(0).sendPacket(viewPacket);
    }

    // Command: change [directory]
    private static void changeDirectory() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter directory path to change to:");
            String newPath = reader.readLine();

            // Create the change directory packet and send to the client
            ChangeDirectoryPacket changePacket = new ChangeDirectoryPacket(newPath);
            networkHandler.getConnection(0).sendPacket(changePacket);

            // Output to server
            System.out.println("Changing directory to: " + newPath);
        } catch (Exception e) {
            System.out.println("Failed to change directory.");
            e.printStackTrace();
        }
    }

    // Command: upload [source] [destination]
    private static void handleUploadRequest() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter source file path to upload:");
            String sourceFilePath = reader.readLine();
            System.out.println("Enter destination file path (server side):");
            String dstFilePath = reader.readLine();

            // Get file name from the source file path (e.g., "example.txt" from "C:/path/to/example.txt")
            String fileName = new File(sourceFilePath).getName();

            // Create and send the UploadRequestPacket
            UploadRequestPacket uploadRequest = new UploadRequestPacket(fileName, sourceFilePath, dstFilePath);
            networkHandler.getConnection(0).sendPacket(uploadRequest);

            System.out.println("Upload request sent. Source: " + sourceFilePath + ", Destination: " + dstFilePath);

        } catch (Exception e) {
            System.out.println("Failed to handle upload.");
            e.printStackTrace();
        }
    }

    // Command: download [file]
    private static void handleDownloadRequest() {
        // Handle download requests (not yet implemented, but similar to upload)
        System.out.println("Download request received (currently not implemented).");
    }

    // Command: print [message]
    private static void handlePrint() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter message to print:");
            String message = reader.readLine();

            // Create PrintPacket and send to client
            PrintPacket printPacket = new PrintPacket(message);
            networkHandler.getConnection(0).sendPacket(printPacket);

            System.out.println("Message sent to client: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Command: website [url]
    private static void handleWebsite() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter website URL to open:");
            String url = reader.readLine();

            // Create WebsitePacket and send to client
            WebsitePacket websitePacket = new WebsitePacket(url);
            networkHandler.getConnection(0).sendPacket(websitePacket);

            System.out.println("Website URL sent to client: " + url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Command: speak [message]
    private static void handleSpeak() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter message to speak:");
            String message = reader.readLine();

            // Send the SpeakPacket to the client
            SpeakPacket speakPacket = new SpeakPacket(message);
            networkHandler.getConnection(0).sendPacket(speakPacket);

            System.out.println("Spoken message sent to client: " + message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
