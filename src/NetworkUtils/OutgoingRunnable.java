package NetworkUtils;

public class OutgoingRunnable implements Runnable {
    SocketConnection connection;
    private final static int SLEEP_TIME = 200;

    public OutgoingRunnable(SocketConnection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        while (connection.isActive()) {
            if (!connection.writeOut()) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
