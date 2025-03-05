package NetworkUtils;

public class IncomingRunnable implements Runnable {
    SocketConnection connection;
    private final static int SLEEP_TIME = 200;


    public IncomingRunnable(SocketConnection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        while (connection.isActive()) {
            if (!connection.readIn()) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
