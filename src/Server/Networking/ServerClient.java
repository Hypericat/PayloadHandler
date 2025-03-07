package Server.Networking;

import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;
import NetworkUtils.SocketConnection;

public class ServerClient {
    PacketHandler packetHandler;
    ServerNetworkHandler networkHandler;
    SocketConnection connection;

    public ServerClient(SocketConnection connection, ServerNetworkHandler networkHandler) {
        this.packetHandler = new PacketHandler(connection);
        this.networkHandler = networkHandler;
        this.connection = connection;
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
