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

public class MultiPlayerPlayScreen extends PlayScreen {

    private transient GameServer gameServer;
    private transient GameClient gameClient;

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

            gameServer = new GameServer();
            gameServer.start();

            startTime = System.currentTimeMillis();

            connected = true;

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

    private void joinGameSession() {
        String hostIP = JOptionPane.showInputDialog(null, "Enter Host IP Address:");
        if (hostIP == null || hostIP.isEmpty()) {
            exited = true;
            return;
        }

        try {

            gameClient = new GameClient();
            gameClient.start();

            gameClient.connect(hostIP);

            startTime = System.currentTimeMillis();

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

//        PlayerState opponentState = (isHost ? gameServer.getOpponentState() : gameClient.getOpponentState());
//
//        switch (opponentState) {
//            case WIN -> {
//                isGameOver = true;
//                if (isHost) {
//                    gameServer.sendTCP();
//                } else {
//                    gameClient.sendTCP();
//                }
//            }
//            case LOSE -> {
//                isGameWin = true;
//                if (isHost) {
//                    gameServer.sendTCP();
//                } else {
//                    gameClient.sendTCP();
//                }
//            }
//        }

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

    @Override
    protected void handleGameEnd() {

        if (isGameWin || isGameOver) {

            PlayerState newOpponentState = (isGameWin ? PlayerState.LOSE : PlayerState.WIN);

            if(isHost) {
                gameServer.sendTCP(newOpponentState);
            } else {
                gameClient.sendTCP(newOpponentState);
            }
        }

        PlayerState opponentState = (isHost ? gameServer.getOpponentState() : gameClient.getOpponentState());

        if (opponentState == PlayerState.WIN) {
            isGameOver = true;
        } else if (opponentState == PlayerState.LOSE) {
            isGameWin = true;
        }

        isGameOver = gameContext.isGameOver() || isGameOver;
        isGameWin = brickManager.isCleared() || isGameWin;

        if (isGameOver || isGameWin) {
            endTime = System.currentTimeMillis();
            powerUpManager.revertAllPowerUps();
            if (isGameOver) {
                screenManager.push(ScreenType.GAME_OVER);
            } else {
                screenManager.push(ScreenType.GAME_WIN);
            }
        }

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
        if (gameServer != null) {
            gameServer.stop();
        }
        if (gameClient != null) {
            gameClient.stop();
        }
    }
}
