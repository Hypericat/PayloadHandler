package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class FileCompletePacket extends Packet {

    private String fileName;
    private int operationId;

    public FileCompletePacket() {

    }

    public FileCompletePacket(String fileName, int operationId) {
        this.fileName = fileName;
        this.operationId = operationId;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.fileName = buf.readString();
        this.operationId = buf.readInt();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeString(fileName);
        buf.writeInt(operationId);
    }

    @Override
    public byte getPacketID() {
        return 0x07;
    }

    @Override
    public String toString() {
        return "FileCompletePacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onFileComplete(this);
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getOperationId() {
        return this.operationId;
    }
}
