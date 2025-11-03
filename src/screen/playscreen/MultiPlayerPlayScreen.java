package screen.playscreen;

import audio.SoundType;
import com.esotericsoftware.kryonet.*;
import network.*;
import object.UI.GameButton;
import object.UI.Text.GameText;
import object.brick.BrickManager;
import screen.Screen;
import screen.ScreenType;
import screen.menuscreen.MenuScreen;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

public class MultiPlayerPlayScreen extends PlayScreen {

    private transient Server server;
    private transient Client client;

    private final GameText opponentScoreText;
    private final GameText opponentNumScoreText;

    private final GameText waitingForConnectionText;
    private final GameButton exitButton;

    private boolean isHost = false;
    private boolean connected = false;

    public MultiPlayerPlayScreen(Screen screen, ScreenType screenType) {
        super(screen, screenType);

        MultiPlayerPlayScreen multiPlayerPlayScreen = (MultiPlayerPlayScreen) screen;

        initOpponentObjects(multiPlayerPlayScreen);

        opponentScoreText = new GameText(multiPlayerPlayScreen.opponentScoreText);
        opponentNumScoreText = new GameText(multiPlayerPlayScreen.opponentNumScoreText);

        waitingForConnectionText = new GameText(multiPlayerPlayScreen.waitingForConnectionText);
        exitButton = new GameButton(multiPlayerPlayScreen.exitButton);

        handleMultiplayerOption();
    }

    private void initOpponentObjects(MultiPlayerPlayScreen multiPlayerPlayScreen) {

        GameText baseOpponentScoreText = multiPlayerPlayScreen.opponentScoreText;
        GameText baseOpponentNumScoreText = multiPlayerPlayScreen.opponentNumScoreText;
        GameText baseWaitingForConnectionText = multiPlayerPlayScreen.waitingForConnectionText;
        GameButton baseExitButton = multiPlayerPlayScreen.exitButton;

        baseOpponentScoreText.updateSizeFromFontData();
        baseOpponentScoreText.alignAbove(scoreText);

        baseOpponentNumScoreText.updateSizeFromFontData();
        baseOpponentNumScoreText.alignRightOf(baseOpponentScoreText);
        baseOpponentNumScoreText.centerHorizontallyTo(numScoreText);

        baseWaitingForConnectionText.updateSizeFromFontData();
        baseWaitingForConnectionText.center();

        baseExitButton.applyRelativeSize();
        baseExitButton.alignBelow(baseWaitingForConnectionText);
        baseExitButton.centerHorizontallyTo(baseWaitingForConnectionText);

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
                        startTime = System.currentTimeMillis();

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
            exited = true;
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
            exited = true;
        }
    }

    @Override
    public void update() {
        if (!connected) {
            updateWaiting();
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
    protected void handlePauseGame() {
        screenManager.push(ScreenType.MULTIPLE_PLAYER_PAUSE);
    }

    @Override
    protected boolean handleSavedProgress() {
        return false;
    }

    @Override
    public void render(Graphics2D graphics2D) {

        if (!connected) {
            renderWaiting(graphics2D);
            return;
        }

        super.render(graphics2D);

        opponentScoreText.render(graphics2D);
        opponentNumScoreText.render(graphics2D);
    }

    public void updateWaiting() {

        if (mouseManager.isLeftClicked()) {
            soundManager.play(SoundType.CLICK_BUTTON);
            if (exitButton.isClicked(mouseManager)) {
                goToMenu();
            }
        }
    }

    public void renderWaiting(Graphics2D graphics2D) {

        background.render(graphics2D);
        waitingForConnectionText.render(graphics2D);
        exitButton.render(graphics2D);
    }

    private void goToMenu() {
        exited = true;
        while (!(screenManager.top() instanceof MenuScreen)) {
            screenManager.pop();
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
}
