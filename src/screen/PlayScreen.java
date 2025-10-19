package screen;

import config.LevelConfig;
import exception.ExceptionHandler;
import exception.InvalidGameStateException;
import exception.ResourceLoadException;
import object.GameContext;
import object.UI.Background;
import object.UI.GameButton;
import object.UI.Text.GameText;
import object.brick.BrickManager;
import object.movable.powerup.PowerUpManager;
import utils.JsonLoaderUtils;
import utils.TextUtils;

import java.awt.*;

public class PlayScreen implements Screen {

    private transient GameContext gameContext;
    private transient BrickManager brickManager;
    private transient PowerUpManager powerUpManager;

    private GameText scoreText;
    private GameText numScoreText;
    private GameButton pauseButton;
    private Background background;
    private String levelPath;

    public PlayScreen(PlayScreen playScreen) {

        init(playScreen);

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

        gameContext = GameContext.getInstance();
        brickManager = BrickManager.getInstance();
        powerUpManager = PowerUpManager.getInstance();

        int windowWidth = gameContext.getWindowWidth();
        int windowHeight = gameContext.getWindowHeight();
        
        GameText baseScoreText = playScreen.scoreText;
        GameText baseNumScoreText = playScreen.numScoreText;
        GameButton basePauseButton = playScreen.pauseButton;

        Font scoreBaseFont = TextUtils.toFont(baseScoreText.getFontData());
        Font numScoreBaseFont = TextUtils.toFont(playScreen.scoreText.getFontData());

        if (scoreBaseFont == null) {
            ExceptionHandler.handle(new InvalidGameStateException("Can't load the brick destroyed text font", null));
            return;
        }

        if (numScoreBaseFont == null) {
            ExceptionHandler.handle(new InvalidGameStateException("Can't load the score text font", null));
            return;
        }

        Font scoreFont = TextUtils.derivedFont(baseScoreText.getRelativeSize(), windowHeight, scoreBaseFont);
        Font numScoreFont = TextUtils.derivedFont(baseNumScoreText.getRelativeSize(), windowHeight, numScoreBaseFont);

        Dimension scoreSize = TextUtils.getTextSize(baseScoreText.getContent(), scoreFont);
        Dimension numScoreSize = TextUtils.getTextSize(playScreen.scoreText.getContent(), numScoreFont);

        baseScoreText.setFont(scoreFont);
        baseScoreText.setWidth(scoreSize.width);
        baseScoreText.setHeight(scoreSize.height);
        baseScoreText.setX(windowWidth * baseScoreText.getRelativeX());
        baseScoreText.setY(windowHeight);

        baseNumScoreText.setFont(scoreFont);
        baseNumScoreText.setWidth(scoreSize.width);
        baseNumScoreText.setHeight(numScoreSize.height);
        baseNumScoreText.setX(baseScoreText.getX() + baseScoreText.getWidth());
        baseNumScoreText.setY(baseScoreText.getY());

        basePauseButton.setWidth(baseScoreText.getHeight());
        basePauseButton.setHeight(baseScoreText.getHeight());
        basePauseButton.setX((windowWidth - basePauseButton.getWidth()) / 2f);
        basePauseButton.setY(baseScoreText.getY() - baseScoreText.getHeight());

    }

    private void initObjects(String levelPath) {

        LevelConfig levelConfig = JsonLoaderUtils.loadFromJson(levelPath, LevelConfig.class);

        if(levelConfig == null) {
            ExceptionHandler.handle(new ResourceLoadException(levelPath, null));
        }

        gameContext.loadFromJson(levelConfig);
        brickManager.loadFromJson(levelConfig);
        powerUpManager.loadFromJson(levelConfig);

        numScoreText.setContent(String.valueOf(0));

    }

    @Override
    public void update() {

        gameContext.getPaddle().update();
        gameContext.getBall().update();
        brickManager.updateBricks();

        if(brickManager.isIncremented()) {
            numScoreText.setContent(String.valueOf(brickManager.getDestroyedBricksCount()));
            brickManager.setIsIncremented(false);
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
}
