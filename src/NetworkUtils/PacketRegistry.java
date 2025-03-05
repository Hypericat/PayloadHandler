package NetworkUtils;

import NetworkUtils.Packets.IPacket;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {
    private static final Map<Byte, Class<? extends IPacket>> registeredPackets = new HashMap<>();


     //Registers packet class with a packet ID

    public static void registerPacket(byte packetID, Class<? extends IPacket> packetClass) {
        registeredPackets.put(packetID, packetClass);
    }


     // Creates new packet instance based on received packet ID

    public static IPacket createPacket(byte packetID) {
        Class<? extends IPacket> packetClass = registeredPackets.get(packetID);
        if (packetClass == null) {
            return null; // Unknown packet ID
        }
        try {
            return packetClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.err.println("Failed to instantiate packet: " + e.getMessage());
            return null;
        }
    }
}
