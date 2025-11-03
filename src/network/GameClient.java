package network;

import com.esotericsoftware.kryonet.*;

import java.io.IOException;

public class GameClient {
    private final Client client;

    public GameClient() {
        client = new Client();
        Network.register(client.getKryo());
    }

    public void start() {
        client.start();

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Network.ScoreUpdate update) {
                    System.out.println("Received score update: " + update.playerScore + " / " + update.opponentScore);
                } else if (object instanceof Network.GameState state) {
                    System.out.println("Received game state: " + state.playerState);
                }
            }
        });
    }

    public void connect(String host) throws IOException {
        client.connect(5000, host, 54555, 54777);
    }

    public void sendJoinRequest(String code) {
        Network.JoinRequest req = new Network.JoinRequest();
        req.playerCode = code;
        client.sendTCP(req);
    }

    public void sendScore(int playerScore, int opponentScore) {
        Network.ScoreUpdate update = new Network.ScoreUpdate();
        update.playerScore = playerScore;
        update.opponentScore = opponentScore;
        client.sendTCP(update);
    }

    public void stop() {
        client.stop();
    }
}
