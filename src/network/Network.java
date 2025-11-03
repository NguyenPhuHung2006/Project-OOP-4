package network;

public class Network {
    public static void register(com.esotericsoftware.kryo.Kryo kryo) {
        kryo.register(JoinRequest.class);
        kryo.register(ScoreUpdate.class);
        kryo.register(GameState.class);
        kryo.register(String.class);
    }

    public static class JoinRequest {
        public String playerCode;
    }

    public static class ScoreUpdate {
        public int playerScore;
        public int opponentScore;
    }

    public static class GameState {
        public PlayerState playerState;
        public PlayerState opponentState;
    }
}
