package NetworkUtils;

import NetworkUtils.Packets.FileChunkPacket;
import NetworkUtils.Packets.FileCompletePacket;
import NetworkUtils.Packets.FileUploadStartPacket;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class NetworkUtil {

    public static final short port = 420;
    public static final String serverDDNS = "winstonchurchili.ddns.net";
    public static final int dataPacketByteSize = 1200;

    public static boolean uploadFile(File file, String dst, SocketConnection connection) {
        return uploadFile(file, dst, connection.getRandomFileID(), connection);
    }
    public static boolean uploadFile(File file, String dst, int id, SocketConnection connection) {
        String name = file.getName();
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            return false;
        }
        if (!file.exists()) return false;
        if (!file.isFile()) return false;

        FileUploadStartPacket packet = new FileUploadStartPacket(name, dst, id);
        connection.sendPacket(packet);
        for(int i = 0; i < bytes.length; i += dataPacketByteSize){
            byte[] array = Arrays.copyOfRange(bytes, i, Math.min(bytes.length,i + dataPacketByteSize));
            FileChunkPacket chunk = new FileChunkPacket(array, packet.getId());
            connection.sendPacket(chunk);
        }

        FileCompletePacket completePacket = new FileCompletePacket(packet.getId());
        connection.sendPacket(completePacket);
        return true;
    }
}
