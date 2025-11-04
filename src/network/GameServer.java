package network;

import com.esotericsoftware.kryonet.*;
import exception.ExceptionHandler;

import java.io.IOException;

public class GameServer extends AbstractNetwork {
    private final Server server;

    public GameServer() {
        super();
        server = new Server();
        Network.register(server.getKryo());
    }

    @Override
    public void start() {
        server.start();
        try {
            server.bind(Network.TCP_PORT, Network.UDP_PORT);
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                update(connection, object);
            }
        });
    }

    @Override
    public void update(Connection connection, Object object) {

        if (object instanceof Integer newOpponentScore) {
            setOpponentScore(newOpponentScore);
        } else if (object instanceof PlayerState newOpponentState) {
            setOpponentState(newOpponentState);
        }
    }

    @Override
    public void sendTCP(Object object) {
        server.sendToAllTCP(object);
    }

    @Override
    public void stop() {
        server.stop();
    }
}
