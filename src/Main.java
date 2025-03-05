import Server.Server;
import Client.Client;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            Client.run();
            return;
        }



        Server.run();
    }
}