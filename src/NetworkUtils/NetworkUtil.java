package NetworkUtils;

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

    public static void uploadFile(File file, SocketConnection connection) {
        uploadFile(file, "", connection);
    }
    public static void uploadFile(File file, String dst, SocketConnection connection) {
        String name = file.getName();
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileUploadStartPacket packet = new FileUploadStartPacket(name, dst, connection.getRandomFileID());
        connection.sendPacket(packet);
        for(int i = 0; i < bytes.length; i += dataPacketByteSize){
            byte[] array = Arrays.copyOfRange(bytes, i, Math.min(bytes.length,i + dataPacketByteSize));

        }

    }
}
