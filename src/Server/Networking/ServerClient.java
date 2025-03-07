package Server.Networking;

import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;
import NetworkUtils.SocketConnection;

public class ServerClient {
    private final PacketHandler packetHandler;
    private final ServerNetworkHandler networkHandler;
    private final SocketConnection connection;
    private final int id;

    public ServerClient(SocketConnection connection, ServerNetworkHandler networkHandler, int id) {
        this.packetHandler = new PacketHandler(connection);
        this.networkHandler = networkHandler;
        this.connection = connection;
        this.id = id;
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
