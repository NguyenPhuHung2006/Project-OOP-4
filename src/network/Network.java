package network;

import screen.playscreen.PlayScreen;

public class Network {

    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54777;

    public static void register(com.esotericsoftware.kryo.Kryo kryo) {
        kryo.register(String.class);
        kryo.register(PlayScreen.class);
        kryo.register(Integer.class);
    }
}
