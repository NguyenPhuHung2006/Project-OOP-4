package screen;

import com.esotericsoftware.kryonet.*;
import network.*;
import object.UI.Text.GameText;
import object.brick.BrickManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class MultiPlayerPlayScreen extends PlayScreen {

    private transient Server server;
    private transient Client client;
    private transient String gameCode;

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
                    if (object instanceof Network.JoinRequest joinReq) {
                        System.out.println("Player joined with code: " + joinReq.playerCode);
                        JOptionPane.showMessageDialog(null, "Player joined! Starting game...");
                        connected = true;
                    } else if (object instanceof Network.ScoreUpdate update) {
                        BrickManager.getInstance().setDestroyedBricksCount(update.playerScore);
                    }
                }
            });

            gameCode = generateGameCode();
            JOptionPane.showMessageDialog(null,
                    "Game created!\nShare this code with your friend:\n" + gameCode,
                    "Game Code",
                    JOptionPane.INFORMATION_MESSAGE);

            // Wait for a client to connect (the popup above remains visible)
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to start server: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void joinGameSession() {
        String code = JOptionPane.showInputDialog(null, "Enter game code:");
        if (code == null || code.isEmpty()) return;

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

            client.connect(5000, "localhost", 54555, 54777);

            Network.JoinRequest req = new Network.JoinRequest();
            req.playerCode = code;
            client.sendTCP(req);

            connected = true;
            JOptionPane.showMessageDialog(null,
                    "✅ Joined game with code: " + code + "\nWaiting for host to start...",
                    "Joined", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to connect: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateGameCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(r.nextInt(chars.length())));
        }
        return sb.toString();
    }

    @Override
    public void update() {
        super.update();

        if (!connected) {
            return;
        }

        // Example of sending score updates
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

        if (!connected) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Waiting for other player...", 350, 200);
        }
    }
}
