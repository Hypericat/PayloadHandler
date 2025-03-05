package NetworkUtils;

public class IncomingRunnable implements Runnable {
    SocketConnection connection;

    public IncomingRunnable(SocketConnection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        while (connection.isActive()) {
            connection.readIn();
        }
    }
}
