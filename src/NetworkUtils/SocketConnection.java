package NetworkUtils;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
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
        return !outgoing.isEmpty();
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
                System.out.println("Added buf : " + buf);
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
                System.out.println("Sent bytes from queue!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
