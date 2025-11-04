package network;

import com.esotericsoftware.kryonet.Connection;

public abstract class AbstractNetwork {

    protected Integer opponentScore;
    protected PlayerState opponentState;
    protected volatile boolean connected;

    public abstract void start();

    public abstract void stop();

    public abstract void sendTCP(Object object);

    protected void update(Connection connection, Object object) {

        if (object instanceof Integer newOpponentScore) {
            setOpponentScore(newOpponentScore);
        } else if (object instanceof PlayerState newOpponentState) {
            setOpponentState(newOpponentState);
        }
    }

    public AbstractNetwork() {
        opponentScore = 0;
        opponentState = PlayerState.PLAYING;
        connected = false;
    }

    public boolean isOpponentWin() {
        return opponentState == PlayerState.WIN;
    }

    public boolean isOpponentLose() {
        return opponentState == PlayerState.LOSE;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public PlayerState getOpponentState() {
        return opponentState;
    }

    public void setOpponentScore(Integer opponentScore) {
        this.opponentScore = opponentScore;
    }

    public void setOpponentState(PlayerState opponentState) {
        this.opponentState = opponentState;
    }

    protected void setConnected(boolean state) {
        connected = state;
    }

    public boolean isConnected() {
        return connected;
    }

}
