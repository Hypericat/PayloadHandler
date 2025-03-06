package NetworkUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SocketConnection {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Queue<ByteBuf> incoming;
    private final Queue<ByteBuf> outgoing;

    Thread outgoingThread;
    Thread incomingThread;


    public SocketConnection(Socket socket) {
        this.socket = socket;
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.incoming = new LinkedList<>();
        this.outgoing = new LinkedList<>();

        outgoingThread = new Thread(new OutgoingRunnable(this));
        incomingThread = new Thread(new IncomingRunnable(this));

        outgoingThread.start();
        incomingThread.start();
    }

    public boolean isActive() {
        return !socket.isClosed();
    }

    private Socket getSocket() {
        return socket;
    }

    public void close() {
        if (socket.isClosed()) return; // Socket is currently not connected so why would we disconnect
        try {
            socket.close();
        } catch (IOException e) {
            return;
        }
    }

    private int outQueueSize() {
        return outgoing.size();
    }

    private int incomingQueueSize() {
        return incoming.size();
    }

    public boolean hasOutgoing() {
        return !outgoing.isEmpty();
    }

    public boolean hasIncoming() {
        return !incoming.isEmpty();
    }

    public void send(ByteBuf buf) {
        synchronized (outgoing) {
            outgoing.add(buf);
        }
    }

    public ByteBuf receive() {
        synchronized (incoming) {
            if (!hasIncoming()) return null;
            return incoming.remove();
        }
    }

    public boolean readIn() {
        int available;
        try {
            available = in.available();
            if (available == 0) return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        synchronized (incoming) {
            ByteBuf buf = new ByteBuf(available);
            try {
                socket.setSoTimeout(2);
                while (true) {
                    buf.writeByte(in.readByte());
                }
            } catch (IOException e) {
                if (buf.writerIndex() < 1) return false;
                incoming.add(buf);
                return true;
            }
        }

    }

    public boolean writeOut() {
        synchronized (outgoing) {
            if (!hasOutgoing()) return false;

            ByteBuf buf = outgoing.remove();
            try {
                out.write(buf.getRawBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean sendPacket(Packet packet) {
        if (!this.isActive()) return false;
        ByteBuf buf = new ByteBuf();
        buf.writeByte(packet.getPacketID());
        packet.encode(buf);
        buf.writerIndex(0);

        buf.writeInt(buf.getWrittenByteCount());

        this.send(buf);
        return true;
    }



    public List<Packet> parseReceivedPackets() {
        List<Packet> packets = new ArrayList<>();
        synchronized (incoming) {
            while (this.hasIncoming()) {
                ByteBuf buf = incoming.remove();
                if (buf == null) throw new IllegalArgumentException("Peeking into null buffer!");
                while (buf.readableBytes() > 0) {
                    if (buf.readableBytes() < 4) break; // Size did not fit in this buffer
                    int size = buf.readInt();
                    if (size > buf.readableBytes()) break;
                    int readerIndex = buf.readerIndex();
                    byte packetID = buf.readByte();
                    Packet packet = PacketRegistry.createPacket(packetID);
                    if (packet == null) throw new IllegalArgumentException("Invalid packetID provided!");
                    packet.decode(new ByteBuf(buf, buf.readerIndex(), size - 1));
                    buf.readerIndex(readerIndex + size);
                    packets.add(packet);
                }
                if (buf.readableBytes() != 0) {
                    ByteBuf nextBuf = incoming.peek();
                    nextBuf.writerIndex(0); // This may crash if the following packet hasn't been received yet
                    nextBuf.writeBytes(new ByteBuf(buf, buf.readerIndex(), buf.readableBytes()).getRawBytes());
                }

            }
        }
        return packets;
    }
}
