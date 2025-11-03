package network;

public class Network {
    public static void register(com.esotericsoftware.kryo.Kryo kryo) {
        kryo.register(JoinRequest.class);
        kryo.register(ScoreUpdate.class);
        kryo.register(GameOver.class);
        kryo.register(String.class);
    }

    public static class JoinRequest {
        public String playerName;
    }

    public static class ScoreUpdate {
        public int playerScore;
        public int opponentScore;
    }

    public static class GameOver {
        public String winnerName;
    }
}
