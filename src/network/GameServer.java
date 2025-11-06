package network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

/**
 * The {@code GameServer} class manages server-side networking logic,
 * including listening for incoming connections, validating clients,
 * and broadcasting messages.
 */
public class GameServer extends AbstractNetwork {

    /** The underlying KryoNet server instance. */
    private final Server server;

    /**
     * Constructs a new {@code GameServer} and registers its classes for Kryo serialization.
     */
    public GameServer() {
        super();
        server = new Server();
        Network.register(server.getKryo());
    }

    /**
     * Starts the server and listens for incoming client connections.
     *
     * @throws IOException if the server fails to start or bind.
     */
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

    /**
     * Binds the server to the configured TCP and UDP ports.
     *
     * @throws IOException if binding fails.
     */
    public void bind() throws IOException {
        server.bind(Network.TCP_PORT, Network.UDP_PORT);
    }

    /**
     * Sends an object to all connected clients via TCP.
     *
     * @param object the object to send.
     */
    @Override
    public void sendTCP(Object object) {
        server.sendToAllTCP(object);
    }

    /**
     * Stops the server and closes all active connections.
     */
    @Override
    public void stop() {
        server.stop();
        connected = false;
    }
}
