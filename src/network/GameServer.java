package network;

import com.esotericsoftware.kryonet.*;

import java.io.IOException;

public class GameServer extends AbstractNetwork {
    private final Server server;

    public GameServer() {
        super();
        server = new Server();
        Network.register(server.getKryo());
    }

    @Override
    public void start() throws IOException {
        server.start();

        server.addListener(new Listener() {

            @Override
            public void connected(Connection connection) {
                if (server.getConnections().length > 1) {
                    connection.sendTCP(ConnectionResponse.REJECTED);
                    connection.close();
                    return;
                }
                connection.sendTCP(ConnectionResponse.ACCEPTED);
                connected = true;
            }

            @Override
            public void received(Connection connection, Object object) {
                update(connection, object);
            }
        });
    }

    public void bind() throws IOException {
        server.bind(Network.TCP_PORT, Network.UDP_PORT);
    }

    @Override
    public void sendTCP(Object object) {
        server.sendToAllTCP(object);
    }

    @Override
    public void stop() {
        server.stop();
        connected = false;
    }
}
