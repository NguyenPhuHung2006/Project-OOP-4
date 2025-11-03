package screen;

import com.esotericsoftware.kryonet.*;
import network.*;
import object.UI.Text.GameText;
import object.brick.BrickManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

public class MultiPlayerPlayScreen extends PlayScreen {

    private transient Server server;
    private transient Client client;

    private final GameText opponentScoreText;
    private final GameText opponentNumScoreText;

    private boolean isHost = false;
    private boolean connected = false;

    public MultiPlayerPlayScreen(Screen screen, ScreenType screenType) {
        super(screen, screenType);

        MultiPlayerPlayScreen multiPlayerPlayScreen = (MultiPlayerPlayScreen) screen;

        initOpponentObjects(multiPlayerPlayScreen);

        opponentScoreText = new GameText(multiPlayerPlayScreen.opponentScoreText);
        opponentNumScoreText = new GameText(multiPlayerPlayScreen.opponentNumScoreText);

        handleMultiplayerOption();
    }

    private void initOpponentObjects(MultiPlayerPlayScreen multiPlayerPlayScreen) {
        GameText baseOpponentScoreText = multiPlayerPlayScreen.opponentScoreText;
        GameText baseOpponentNumScoreText = multiPlayerPlayScreen.opponentNumScoreText;

        baseOpponentScoreText.updateSizeFromFontData();
        baseOpponentScoreText.alignAbove(scoreText);

        baseOpponentNumScoreText.updateSizeFromFontData();
        baseOpponentNumScoreText.alignRightOf(baseOpponentScoreText);
        baseOpponentNumScoreText.centerHorizontallyTo(numScoreText);
    }

    private void handleMultiplayerOption() {
        String[] options = {"Create Game", "Join Game", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Select multiplayer option:",
                "Multiplayer Mode",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            createGameSession();
        } else if (choice == 1) {
            joinGameSession();
        } else {
            exited = true;
        }
    }

    private void createGameSession() {
        try {
            isHost = true;
            server = new Server();
            Network.register(server.getKryo());
            server.bind(54555, 54777);
            server.start();

            server.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof Network.JoinRequest joinRequest) {
                        System.out.println("Player joined from IP: " + connection.getRemoteAddressTCP());
                        JOptionPane.showMessageDialog(null, "Player joined! Starting game...");
                        connected = true;
                    } else if (object instanceof Network.ScoreUpdate update) {
                        BrickManager.getInstance().setDestroyedBricksCount(update.playerScore);
                    }
                }
            });

            String hostIP = InetAddress.getLocalHost().getHostAddress();
            JOptionPane.showMessageDialog(null,
                    "Game created!\nShare this IP with your friend:\n" + hostIP,
                    "Game Host",
                    JOptionPane.INFORMATION_MESSAGE);

            System.out.println("Server started on IP: " + hostIP);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to start server: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void joinGameSession() {
        String hostIP = JOptionPane.showInputDialog(null, "Enter Host IP Address:");
        if (hostIP == null || hostIP.isEmpty()) {
            return;
        }

        try {
            client = new Client();
            Network.register(client.getKryo());
            client.start();

            client.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof Network.ScoreUpdate update) {
                        BrickManager.getInstance().setDestroyedBricksCount(update.opponentScore);
                    } else if (object instanceof Network.GameState state) {
                        System.out.println("Received game state: " + state.playerState);
                    }
                }
            });

            client.connect(5000, hostIP, 54555, 54777);

            Network.JoinRequest req = new Network.JoinRequest();
            req.playerCode = "N/A";
            client.sendTCP(req);

            connected = true;
            JOptionPane.showMessageDialog(null,
                    "Connected to host: " + hostIP + "\nWaiting for host to start...",
                    "Connected", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to connect: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update() {
        if (!connected) {
            return;
        }

        super.update();

        int currentScore = BrickManager.getInstance().getDestroyedBricksCount();

        Network.ScoreUpdate update = new Network.ScoreUpdate();
        update.playerScore = currentScore;
        update.opponentScore = currentScore;

        if (isHost && server != null) {
            server.sendToAllTCP(update);
        } else if (client != null) {
            client.sendTCP(update);
        }
    }

    @Override
    public void onExit() {
        super.onExit();
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        opponentScoreText.render(g);
        opponentNumScoreText.render(g);

        if (!connected) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Waiting for other player...", 350, 200);
        }
    }
}
