package screen;

import audio.SoundManager;
import audio.SoundType;
import config.LevelConfig;
import exception.ExceptionHandler;
import exception.ResourceLoadException;
import input.MouseManager;
import object.GameContext;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;
import object.brick.BrickManager;
import object.movable.powerup.PowerUpManager;
import utils.JsonLoaderUtils;

import java.awt.*;

public class PlayScreen implements Screen {

    private transient GameContext gameContext;
    private transient BrickManager brickManager;
    private transient PowerUpManager powerUpManager;
    private transient ScreenType levelId;

    private GameText scoreText;
    private GameText numScoreText;
    private GameButton pauseButton;
    private Background background;
    private String levelPath;

    public PlayScreen(Screen screen, ScreenType screenType) {

        gameContext = GameContext.getInstance();
        brickManager = BrickManager.getInstance();
        powerUpManager = PowerUpManager.getInstance();
        this.levelId = screenType;

        init(screen);

        PlayScreen playScreen = (PlayScreen) screen;

        scoreText = new GameText(playScreen.scoreText);
        numScoreText = new GameText(playScreen.numScoreText);
        pauseButton = new GameButton(playScreen.pauseButton);
        background = new Background(playScreen.background);
        levelPath = playScreen.levelPath;

        initObjects(levelPath);

    }

    @Override
    public void init(Screen screen) {

        if (!(screen instanceof PlayScreen playScreen)) {
            return;
        }

        GameText baseScoreText = playScreen.scoreText;
        GameText baseNumScoreText = playScreen.numScoreText;
        GameButton basePauseButton = playScreen.pauseButton;

        baseScoreText.updateSizeFromFontData();
        baseScoreText.alignBottomLeft();

        baseNumScoreText.updateSizeFromFontData();
        baseNumScoreText.alignRightOf(baseScoreText);

        basePauseButton.applyRelativeSize();
        basePauseButton.centerHorizontally();
        basePauseButton.alignBottom();

    }

    private void initObjects(String levelPath) {

        LevelConfig levelConfig = JsonLoaderUtils.loadFromJson(levelPath, LevelConfig.class);

        if (levelConfig == null) {
            ExceptionHandler.handle(new ResourceLoadException(levelPath, null));
        }

        gameContext.loadFromJson(levelConfig);
        brickManager.loadFromJson(levelConfig);
        powerUpManager.loadFromJson(levelConfig);

        numScoreText.setContent(String.valueOf(0));
    }

    @Override
    public void update() {

        boolean isGameOver = gameContext.isGameOver();
        boolean isGameWin = brickManager.isCleared();

        if(isGameOver || isGameWin) {
            screenManager.pop();
            if(isGameOver) {
                screenManager.push(ScreenType.GAME_OVER);
            } else {
                screenManager.push(ScreenType.GAME_WIN);
            }
            return;
        }

        if(mouseManager.isLeftClicked()) {
            soundManager.play(SoundType.CLICK_BUTTON);
            if(pauseButton.isClicked(mouseManager)) {
                screenManager.push(ScreenType.PAUSE);
                return;
            }
        }

        gameContext.updateContext();
        brickManager.updateBricks();

        if (brickManager.isIncremented()) {
            numScoreText.setContent(String.valueOf(brickManager.getDestroyedBricksCount()));
        }

        powerUpManager.updateFallingPowerUps();
    }

    @Override
    public void render(Graphics2D graphics2D) {

        background.render(graphics2D);

        brickManager.renderBricks(graphics2D);
        gameContext.getPaddle().render(graphics2D);
        gameContext.getBall().render(graphics2D);

        powerUpManager.renderPowerUps(graphics2D);

        brickManager.renderBricks(graphics2D);

        scoreText.render(graphics2D);
        numScoreText.render(graphics2D);
        pauseButton.render(graphics2D);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }

    public ScreenType getLevelId() {
        return levelId;
    }
}
