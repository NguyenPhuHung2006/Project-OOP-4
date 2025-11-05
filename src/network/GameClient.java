package network;

import com.esotericsoftware.kryonet.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GameClient extends AbstractNetwork {

    private final Client client;
    private String hostIP;

    public GameClient() {
        super();
        client = new Client();
        Network.register(client.getKryo());
    }

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

        Thread.sleep(300);

        if(connectionResponse[0] == ConnectionResponse.REJECTED) {
            throw new IOException("Another client has already connected to the server");
        }
    }

    @Override
    public void sendTCP(Object object) {
        client.sendTCP(object);
    }

    @Override
    public void stop() {
        client.stop();
    }

    private void connect(String hostIP) throws IOException {
        client.connect(5000, hostIP, Network.TCP_PORT, Network.UDP_PORT);
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    public String getHostIP() {
        return hostIP;
    }

}
