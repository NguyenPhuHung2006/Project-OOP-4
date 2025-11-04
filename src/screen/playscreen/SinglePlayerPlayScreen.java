package screen.playscreen;

import screen.Screen;
import screen.ScreenType;
import utils.JsonLoaderUtils;

import javax.swing.*;

public class SinglePlayerPlayScreen extends PlayScreen {
    public SinglePlayerPlayScreen(Screen screen, ScreenType screenType) {
        super(screen, screenType);
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void handlePauseGame() {
        screenManager.push(ScreenType.SINGLE_PLAYER_PAUSE);
    }

    public void saveGameProgress() {

        gameContext.serializeGameContext();
        powerUpManager.serializePowerUps();
        brickManager.serializeBricks();

        scoreText.serializeToJson();
        numScoreText.serializeToJson();
        pauseButton.serializeToJson();
        numTimeText.serializeToJson();
        background.serializeToJson();

        getTotalTimePlayedBeforeExit();

        JsonLoaderUtils.saveToJson(levelSavePath, this);

        exited = true;
    }

    private void loadSavedProgress() {

        PlayScreen savedPlayScreen = JsonLoaderUtils.loadFromJson(levelSavePath, SinglePlayerPlayScreen.class);

        assert savedPlayScreen != null;

        gameContext.deserializeGameContext(savedPlayScreen.gameContext);
        brickManager.deserializeBricks(savedPlayScreen.brickManager);
        powerUpManager.deserializePowerUps(savedPlayScreen.powerUpManager);

        scoreText = savedPlayScreen.scoreText;
        numScoreText = savedPlayScreen.numScoreText;
        pauseButton = savedPlayScreen.pauseButton;
        numTimeText = savedPlayScreen.numTimeText;
        background = savedPlayScreen.background;

        levelInitPath = savedPlayScreen.levelInitPath;
        levelSavePath = savedPlayScreen.levelSavePath;

        scoreText.deserializeFromJson();
        numScoreText.deserializeFromJson();
        pauseButton.deserializeFromJson();
        numTimeText.deserializeFromJson();
        background.deserializeFromJson();

        totalTimePlayed = savedPlayScreen.totalTimePlayed;
        isPaused = false;
        powerUpManager.resumeTimers();
    }

    protected boolean handleSavedProgress() {

        if(!JsonLoaderUtils.isJsonDataAvailable(levelSavePath)) {
            return false;
        }

        int option = JOptionPane.showConfirmDialog(
                null,
                "Do you want to continue playing the saved progress",
                "WARNING",
                JOptionPane.YES_NO_OPTION
        );
        if (option == JOptionPane.YES_OPTION) {
            loadSavedProgress();
            return true;
        } else if (option == JOptionPane.CLOSED_OPTION) {
            exited = true;
        }
        return false;
    }

    @Override
    protected void handleScore() {

        if (brickManager.isIncremented()) {
            numScoreText.setContent(String.valueOf(brickManager.getDestroyedBricksCount()));
        }
    }

    @Override
    protected void handleGameEnd() {

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

}
