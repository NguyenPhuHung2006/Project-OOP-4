package network;

import com.esotericsoftware.kryo.Kryo;

/**
 * The {@code Network} class defines shared configuration for both the client and server.
 * It includes port numbers and handles Kryo serialization registration.
 */
public class Network {

    /** The TCP port used for communication. */
    public static final int TCP_PORT = 54555;

    /** The UDP port used for communication. */
    public static final int UDP_PORT = 54777;

    /**
     * Registers all required classes for Kryo serialization to ensure compatibility
     * between client and server.
     *
     * @param kryo the Kryo instance used by the network.
     */
    public static void register(Kryo kryo) {
        kryo.register(String.class);
        kryo.register(PlayerState.class);
        kryo.register(Integer.class);
        kryo.register(ConnectionResponse.class);
    }
}
