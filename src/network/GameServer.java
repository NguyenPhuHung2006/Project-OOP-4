package network;

import com.esotericsoftware.kryonet.*;

import java.io.IOException;

public class GameServer {
    private final Server server;
    private Runnable onPlayerJoin;

    public GameServer() {
        server = new Server();
        Network.register(server.getKryo());
    }

    public void start() throws IOException {
        server.start();
        server.bind(54555, 54777);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Network.JoinRequest join) {
                    System.out.println("Client joined with code: " + join.playerCode);
                    if (onPlayerJoin != null) onPlayerJoin.run();
                } else if (object instanceof Network.ScoreUpdate update) {
                    server.sendToAllTCP(update);
                }
            }
        });
    }

    public void addConnectionListener(Runnable r) {
        this.onPlayerJoin = r;
    }

    public void sendToAll(Object obj) {
        server.sendToAllTCP(obj);
    }

    public void stop() {
        server.stop();
    }
}
