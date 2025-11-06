package network;

import com.esotericsoftware.kryonet.Connection;

import java.io.IOException;

/**
 * The {@code AbstractNetwork} class provides a base abstraction for networked communication
 * between two players (client and server) using KryoNet.
 * <p>
 * It defines common states such as the opponent's score and game state, and
 * provides an update mechanism for handling received network data.
 */
public abstract class AbstractNetwork {

    /** The opponent's latest score received from the network. */
    protected Integer opponentScore;

    /** The opponent's current {@link PlayerState}. */
    protected PlayerState opponentState;

    /** Indicates whether the network connection is currently established. */
    protected volatile boolean connected;

    /**
     * Constructs a new {@code AbstractNetwork} instance with default values.
     */
    public AbstractNetwork() {
        opponentScore = 0;
        opponentState = PlayerState.PLAYING;
        connected = false;
    }

    /**
     * Starts the network connection.
     *
     * @throws IOException           if the network fails to start.
     * @throws InterruptedException  if the thread is interrupted during startup.
     */
    public abstract void start() throws IOException, InterruptedException;

    /**
     * Stops the network connection and releases all resources.
     */
    public abstract void stop();

    /**
     * Sends an object to the connected peer using TCP.
     *
     * @param object the object to send.
     */
    public abstract void sendTCP(Object object);

    /**
     * Updates network-related fields when an object is received.
     *
     * @param connection the active network connection.
     * @param object     the received object.
     */
    protected void update(Connection connection, Object object) {
        if (object instanceof Integer newOpponentScore) {
            setOpponentScore(newOpponentScore);
        } else if (object instanceof PlayerState newOpponentState) {
            setOpponentState(newOpponentState);
        }
    }

    /**
     * Returns whether the opponent has won.
     *
     * @return {@code true} if the opponent's state is {@link PlayerState#WIN}, otherwise {@code false}.
     */
    public boolean isOpponentWin() {
        return opponentState == PlayerState.WIN;
    }

    /**
     * Returns whether the opponent has lost.
     *
     * @return {@code true} if the opponent's state is {@link PlayerState#LOSE}, otherwise {@code false}.
     */
    public boolean isOpponentLose() {
        return opponentState == PlayerState.LOSE;
    }

    /**
     * Gets the opponent's current score.
     *
     * @return the opponent's score.
     */
    public int getOpponentScore() {
        return opponentScore;
    }

    /**
     * Gets the opponent's current state.
     *
     * @return the opponent's {@link PlayerState}.
     */
    public PlayerState getOpponentState() {
        return opponentState;
    }

    /**
     * Sets the opponent's score.
     *
     * @param opponentScore the opponent's score.
     */
    public void setOpponentScore(Integer opponentScore) {
        this.opponentScore = opponentScore;
    }

    /**
     * Sets the opponent's state.
     *
     * @param opponentState the opponent's {@link PlayerState}.
     */
    public void setOpponentState(PlayerState opponentState) {
        this.opponentState = opponentState;
    }

    /**
     * Updates the connection status.
     *
     * @param state {@code true} if connected, {@code false} otherwise.
     */
    protected void setConnected(boolean state) {
        connected = state;
    }

    /**
     * Returns whether this instance is currently connected.
     *
     * @return {@code true} if connected, {@code false} otherwise.
     */
    public boolean isConnected() {
        return connected;
    }
}
