package screen.playscreen;

import audio.SoundType;
import network.*;
import object.UI.GameButton;
import object.UI.Text.GameText;
import screen.Screen;
import screen.ScreenType;
import screen.menuscreen.MenuScreen;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Represents the multiplayer play screen of the game.
 * <p>
 * This class extends {@link PlayScreen} and manages multiplayer gameplay logic
 * using a client-server network model. It handles both hosting and joining a multiplayer session,
 * synchronizes player scores, manages connection states, and coordinates game end conditions
 * between two connected players.
 * </p>
 */
public class MultiPlayerPlayScreen extends PlayScreen {

    private transient GameServer gameServer;
    private transient GameClient gameClient;

    private final GameText opponentScoreText;
    private final GameText opponentNumScoreText;

    private final GameText waitingForConnectionText;
    private final GameButton exitButton;

    private boolean isHost = false;
    private boolean hasSentEndState = false;
    private boolean connected = false;

    /**
     * Constructs a new {@code MultiPlayerPlayScreen}.
     *
     * @param screen      the previous screen used to initialize base UI components.
     * @param screenType  the type of the current screen (e.g., MULTI_PLAYER_LEVEL_1).
     */
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

    /**
     * Initializes and positions UI components related to the opponent’s display area and the wait screen.
     *
     * @param multiPlayerPlayScreen the screen used as a template for positioning and alignment.
     */
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

    /**
     * Handles the player’s choice to either host or join a multiplayer session.
     * <p>
     * Displays a dialog box allowing the player to create a new game, join an existing one,
     * or cancel the multiplayer setup.
     * </p>
     */
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

    /**
     * Creates a new multiplayer session and starts a {@link GameServer}.
     * <p>
     * Displays the host’s IP address so that another player can connect using it.
     * </p>
     */
    private void createGameSession() {
        try {
            isHost = true;

            gameServer = new GameServer();
            gameServer.start();
            gameServer.bind();

            String hostIP = InetAddress.getLocalHost().getHostAddress();
            JOptionPane.showMessageDialog(null,
                    "Game created!\nYour IP address: " + hostIP,
                    "Game Host",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to start server: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            exited = true;
        }
    }

    /**
     * Joins an existing multiplayer session as a {@link GameClient}.
     * <p>
     * Prompts the player to enter the host’s IP address and attempts to connect to it.
     * </p>
     */
    private void joinGameSession() {
        String hostIP = JOptionPane.showInputDialog(null, "Enter Host IP Address:");
        if (hostIP == null || hostIP.isEmpty()) {
            exited = true;
            return;
        }

        try {
            gameClient = new GameClient();
            gameClient.setHostIP(hostIP);
            gameClient.start();

        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to connect: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            exited = true;
        }
    }

    /**
     * Handles pause events by pushing the multiplayer pause screen onto the screen stack.
     */
    @Override
    protected void handlePauseGame() {
        screenManager.push(ScreenType.MULTIPLE_PLAYER_PAUSE);
    }

    @Override
    protected boolean handleSavedProgress() {
        return false;
    }

    /**
     * Updates and synchronizes the player and opponent scores.
     * <p>
     * Sends score updates to the connected player using TCP communication
     * and receives the opponent’s score for display.
     * </p>
     */
    @Override
    protected void handleScore() {

        if (brickManager.isIncremented()) {

            Integer newScore = brickManager.getDestroyedBricksCount();
            numScoreText.setContent(String.valueOf(newScore));

            if (isHost) {
                gameServer.sendTCP(newScore);
            } else {
                gameClient.sendTCP(newScore);
            }
        }

        int opponentScore = (isHost ? gameServer.getOpponentScore() : gameClient.getOpponentScore());
        opponentNumScoreText.setContent(
                Integer.toString(opponentScore)
        );
    }

    /**
     * Handles the game’s end conditions and synchronizes win/loss states between players.
     * <p>
     * Sends the player’s end state to the opponent and listens for the opponent’s result.
     * Displays the corresponding end screen when the game concludes.
     * </p>
     */
    @Override
    public void handleGameEnd() {

        isGameOver = isGameOver || gameContext.isGameOver();
        isGameWin = isGameWin || brickManager.isCleared();

        if ((isGameWin || isGameOver) && !hasSentEndState) {

            PlayerState newCurrentState = (isGameWin ? PlayerState.WIN : PlayerState.LOSE);

            if (isHost) {
                gameServer.sendTCP(newCurrentState);
            } else {
                gameClient.sendTCP(newCurrentState);
            }

            hasSentEndState = true;
        }

        PlayerState opponentState = isHost
                ? gameServer.getOpponentState()
                : gameClient.getOpponentState();

        if (opponentState == PlayerState.WIN) {
            isGameOver = true;
        } else if (opponentState == PlayerState.LOSE) {
            isGameWin = true;
        }

        if (isGameWin || isGameOver) {
            endTime = System.currentTimeMillis();
            powerUpManager.revertAllPowerUps();

            screenManager.push(isGameOver
                    ? ScreenType.GAME_OVER
                    : ScreenType.GAME_WIN);
        }

    }

    /**
     * Updates the multiplayer play screen each frame.
     * <p>
     * If not yet connected, updates the waiting screen logic.
     * Otherwise, continues the standard {@link PlayScreen#update()} cycle.
     * </p>
     */
    @Override
    public void update() {

        checkConnected();

        if (!connected) {
            updateWaiting();
            return;
        }

        super.update();
    }

    /**
     * Renders the multiplayer play screen.
     * <p>
     * Displays a waiting screen if the connection is not established,
     * otherwise renders all gameplay elements including the opponent’s score.
     * </p>
     *
     * @param graphics2D the {@link Graphics2D} object used for rendering.
     */
    @Override
    public void render(Graphics2D graphics2D) {

        checkConnected();

        if (!connected) {
            renderWaiting(graphics2D);
            return;
        }

        super.render(graphics2D);

        opponentScoreText.render(graphics2D);
        opponentNumScoreText.render(graphics2D);
    }

    /**
     * Handles updates while waiting for a connection.
     * <p>
     * Allows the player to exit back to the main menu using the exit button.
     * </p>
     */
    public void updateWaiting() {

        if (mouseManager.isLeftClicked()) {
            soundManager.play(SoundType.CLICK_BUTTON);
            if (exitButton.isClicked(mouseManager)) {
                stopConnection();
                goToMenu();
            }
        }
    }

    /**
     * Renders the waiting screen shown before connection establishment.
     *
     * @param graphics2D the {@link Graphics2D} context used for rendering.
     */
    private void renderWaiting(Graphics2D graphics2D) {

        background.render(graphics2D);
        waitingForConnectionText.render(graphics2D);
        exitButton.render(graphics2D);
    }

    /**
     * Checks and updates the connection status between players.
     * <p>
     * Records the start time once a connection is successfully established.
     * </p>
     */
    private void checkConnected() {
        boolean previouslyConnected = connected;

        connected = (isHost ? gameServer.isConnected() : gameClient.isConnected());

        if (!previouslyConnected && connected) {
            startTime = System.currentTimeMillis();
        }
    }

    /**
     * Stops and closes the network connections for both server and client.
     */
    private void stopConnection() {
        if (gameServer != null) {
            gameServer.stop();
        }
        if (gameClient != null) {
            gameClient.stop();
        }
    }

    /**
     * Returns the player to the main menu screen by closing the multiplayer session
     * and navigating through the screen stack.
     */
    private void goToMenu() {
        exited = true;
        while (!(screenManager.top() instanceof MenuScreen)) {
            screenManager.pop();
        }
    }

    /**
     * Called when the screen is exited.
     * <p>
     * Ensures that network connections are closed when the game ends.
     * </p>
     */
    @Override
    public void onExit() {
        super.onExit();

        if (isGameOver || isGameWin) {
            stopConnection();
        }
    }
}
