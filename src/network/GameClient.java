package network;

import com.esotericsoftware.kryonet.*;
import screen.playscreen.PlayScreen;

import java.io.IOException;

public class GameClient extends AbstractNetwork {

    private final Client client;

    public GameClient() {
        super();
        client = new Client();
        Network.register(client.getKryo());
    }

    @Override
    public void start() {
        client.start();

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                update(connection, object);
            }
        });

        opponentScore = 0;
        opponentState = PlayerState.PLAYING;
    }

    @Override
    public void update(Connection connection, Object object) {

        if (object instanceof Integer newOpponentScore) {
            setOpponentScore(newOpponentScore);
        } else if(object instanceof PlayerState newOpponentState) {
            setOpponentState(newOpponentState);
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

    public void connect(String hostIP) throws IOException {
        client.connect(5000, hostIP, Network.TCP_PORT, Network.UDP_PORT);
    }
}
