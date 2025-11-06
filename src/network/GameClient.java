package network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

/**
 * The {@code GameClient} class handles client-side networking logic for connecting
 * to a {@link GameServer}, sending data, and receiving updates.
 */
public class GameClient extends AbstractNetwork {

    /** The underlying KryoNet client instance. */
    private final Client client;

    /** The server IP address to connect to. */
    private String hostIP;

    /**
     * Constructs a new {@code GameClient} and registers its classes for Kryo serialization.
     */
    public GameClient() {
        super();
        client = new Client();
        Network.register(client.getKryo());
    }

    /**
     * Starts the client and connects to the specified server.
     *
     * @throws IOException          if the connection fails or is rejected.
     * @throws InterruptedException if the thread is interrupted during the connection handshake.
     */
    @Override
    public void start() throws IOException, InterruptedException {
        client.start();

        final ConnectionResponse[] connectionResponse = new ConnectionResponse[1];

        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                connected = true;
            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof ConnectionResponse response) {
                    connectionResponse[0] = response;
                }
                update(connection, object);
            }
        });

        connect(hostIP);

        // Wait briefly for connection response
        Thread.sleep(300);

        if (connectionResponse[0] == ConnectionResponse.REJECTED) {
            throw new IOException("Another client has already connected to the server");
        }
    }

    /**
     * Sends an object to the server using TCP.
     *
     * @param object the object to send.
     */
    @Override
    public void sendTCP(Object object) {
        client.sendTCP(object);
    }

    /**
     * Stops the client and closes the connection.
     */
    @Override
    public void stop() {
        client.stop();
    }

    /**
     * Attempts to connect to the specified server IP address.
     *
     * @param hostIP the server's IP address.
     * @throws IOException if the connection fails.
     */
    private void connect(String hostIP) throws IOException {
        client.connect(5000, hostIP, Network.TCP_PORT, Network.UDP_PORT);
    }

    /**
     * Sets the server IP address for this client.
     *
     * @param hostIP the server IP address.
     */
    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    /**
     * Returns the currently configured server IP address.
     *
     * @return the host IP address.
     */
    public String getHostIP() {
        return hostIP;
    }
}