package NetworkUtils;

import NetworkUtils.Packets.FileUploadStartPacket;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class SocketConnection {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Queue<ByteBuf> incoming;
    private final Queue<ByteBuf> outgoing;

    private final HashMap<Integer, FileTask> activeUploads = new HashMap<>();

    Thread outgoingThread;
    Thread incomingThread;


    public SocketConnection(Socket socket) {
        this.socket = socket;
        try {
            this.socket.setSoTimeout(2);
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
        try {
            return !(new PrintWriter(socket.getOutputStream(), true).checkError());
        } catch (Exception e) {
            return false;
        }
    }

    public Socket getSocket() {
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

    public int getRandomFileID() {
        Random random = new Random();
        int id = random.nextInt();
        while (activeUploads.containsKey(id)) {
            id = random.nextInt();
        }
        return id;
    }

    public void registerFileID(FileUploadStartPacket packet) {
        if (activeUploads.containsKey(packet.getId())) {
            throw new IllegalStateException("An already existing download ID was provided!");
        }
        activeUploads.put(packet.getId(), new FileTask(packet.getFileSrc(), packet.getFileDst(), packet.getId()));
    }

    public void addToFileID(int id, byte[] array) {
        if (!activeUploads.containsKey(id)) throw new IllegalStateException("Tried adding to file chunk with no registered start");
        activeUploads.get(id).addData(array);
    }

    public FileTask finishFileID(int id) {
        FileTask task = activeUploads.get(id);
        activeUploads.remove(id);
        return task;
    }



    public List<Packet> parseReceivedPackets() {
        List<Packet> packets = new ArrayList<>();
        synchronized (incoming) {
            while (this.hasIncoming()) {
                ByteBuf buf = incoming.remove();
                if (buf == null) throw new IllegalArgumentException("Peeking into null buffer!");
                while (buf.readableBytes() > 0) {
                    if (buf.readableBytes() < 4) break; // Size did not fit in this buffer
                    int readerIndex = buf.readerIndex();
                    int size = buf.readInt();
                    if (size > buf.readableBytes()) {
                        buf.readerIndex(readerIndex);
                        break;
                    }
                    readerIndex = buf.readerIndex();
                    byte packetID = buf.readByte();
                    Packet packet = PacketRegistry.createPacket(packetID);
                    if (packet == null) throw new IllegalArgumentException("Invalid packetID provided!");
                    packet.decode(new ByteBuf(buf, buf.readerIndex(), size - 1));
                    buf.readerIndex(readerIndex + size);
                    packets.add(packet);
                }
                if (buf.readableBytes() != 0) {
                    ByteBuf nextBuf = incoming.peek();
                    if (nextBuf == null) {
                        nextBuf = new ByteBuf();
                        incoming.add(nextBuf);
                        nextBuf.writerIndex(0);
                        nextBuf.writeBytes(new ByteBuf(buf, buf.readerIndex(), buf.readableBytes()).getRawBytes());
                        break;
                    }
                    nextBuf.writerIndex(0); // This may crash if the following packet hasn't been received yet
                    nextBuf.writeBytes(new ByteBuf(buf, buf.readerIndex(), buf.readableBytes()).getRawBytes());
                }

            }
        }
        return packets;
    }
}
