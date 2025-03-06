package NetworkUtils;

import NetworkUtils.Packets.AdminPacket;
import NetworkUtils.Packets.HandshakePacket;
import NetworkUtils.Packets.PrintPacket;
import NetworkUtils.Packets.WebsitePacket;

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

    public void onAdmin(AdminPacket packet) {

    }

    public void onPrint(PrintPacket packet) {
         System.out.println("[Message] : " + packet.getMessage());
    }

    public void onWebsite(WebsitePacket packet) {
        try {
            String s = packet.getUrl();
            if (!s.startsWith("http")) s = "https://" + s;
            Desktop.getDesktop().browse(new URI(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
