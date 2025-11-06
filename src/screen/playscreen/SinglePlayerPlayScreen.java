package screen.playscreen;

import screen.Screen;
import screen.ScreenType;
import utils.JsonLoaderUtils;

import javax.swing.*;

/**
 * The {@code SinglePlayerPlayScreen} class represents the main gameplay
 * screen for single-player mode.
 * <p>
 * It extends {@link PlayScreen} and implements game-specific logic such as
 * saving/loading progress, handling pause functionality, score updates, and
 * determining win/lose conditions for a single player.
 * </p>
 *
 * <p>
 * This class handles persistence by serializing and deserializing game data
 * (context, bricks, power-ups, UI state, and background) to and from JSON files.
 * </p>
 */
public class SinglePlayerPlayScreen extends PlayScreen {

    /**
     * Constructs a new {@code SinglePlayerPlayScreen}.
     *
     * @param screen      the base screen used as a reference for layout and data
     * @param screenType  the screen type identifier (e.g., SINGLE_PLAYER)
     */
    public SinglePlayerPlayScreen(Screen screen, ScreenType screenType) {
        super(screen, screenType);
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void handlePauseGame() {
        screenManager.push(ScreenType.SINGLE_PLAYER_PAUSE);
    }

    /**
     * Saves the current game progress to a JSON file specified by {@link #levelSavePath}.
     * <p>
     * This includes serializing all key game components such as:
     * <ul>
     *     <li>{@link object.GameContext}</li>
     *     <li>{@link object.movable.powerup.PowerUpManager}</li>
     *     <li>{@link object.brick.BrickManager}</li>
     *     <li>UI elements and background</li>
     * </ul>
     * </p>
     * The total play time is updated before saving.
     */
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

    /**
     * Loads the previously saved single-player game progress from the JSON file
     * specified by {@link #levelSavePath}.
     * <p>
     * This method restores serialized objects, including game state, bricks,
     * power-ups, UI elements, and background. It also resumes all active
     * power-up timers.
     * </p>
     */
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

    /**
     * Handles checking and loading saved progress if available.
     * <p>
     * If saved progress is detected, the player is prompted to choose whether
     * to continue the saved game. If the player agrees, progress is restored
     * from the saved data. Otherwise, a new game session begins.
     * </p>
     *
     * @return {@code true} if saved progress was successfully loaded;
     *         {@code false} otherwise
     */
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

    /**
     * Updates the player’s score whenever bricks are destroyed.
     * <p>
     * The displayed score value is synchronized with the
     * {@link object.brick.BrickManager} destroyed brick count.
     * </p>
     */
    @Override
    protected void handleScore() {

        if (brickManager.isIncremented()) {
            numScoreText.setContent(String.valueOf(brickManager.getDestroyedBricksCount()));
        }
    }

    /**
     * Handles the logic when the game ends, determining whether the player has
     * won or lost. Pushes the corresponding end screen
     * ({@link ScreenType#GAME_OVER} or {@link ScreenType#GAME_WIN})
     * onto the screen stack.
     */
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
