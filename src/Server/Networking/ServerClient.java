package Server.Networking;

import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;
import NetworkUtils.SocketConnection;

public class ServerClient {
    private final PacketHandler packetHandler;
    private final ServerNetworkHandler networkHandler;
    private final SocketConnection connection;
    private final int id;
    private String userFolder;

    public ServerClient(SocketConnection connection, ServerNetworkHandler networkHandler, int id) {
        this.packetHandler = new PacketHandler(connection);
        this.networkHandler = networkHandler;
        this.connection = connection;
        this.id = id;
        this.userFolder = userFolder;
    }

    public String getUserFolder() {
        return userFolder;
    }

    public int getId() {
        return id;
    }

    public void processPacket(Packet packet) {
        packet.execute(this.packetHandler);
    }

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }

    public ServerNetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public SocketConnection getConnection() {
        return connection;
    }
}
