package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class WebsitePacket extends Packet {
    private String url;
    public WebsitePacket() {

    }

    public WebsitePacket(String message) {
        this.url = message;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.url = buf.readString();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeString(url);
    }

    @Override
    public byte getPacketID() {
        return 0x03;
    }

    @Override
    public String toString() {
        return "WebsitePacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onWebsite(this);
    }

    public String getUrl() {
        return this.url;
    }


}
