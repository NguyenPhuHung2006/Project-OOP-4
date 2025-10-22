package object.movable.powerup;

import object.movable.Paddle;

public class ExpandPaddleWidthPowerUp extends PowerUp {

    private final float widthMultiplier;
    private float originalWidth;
    private final Paddle paddle;

    public ExpandPaddleWidthPowerUp(ExpandPaddleWidthPowerUp expandPaddleWidthPowerUp) {

        super(expandPaddleWidthPowerUp);

        this.widthMultiplier = expandPaddleWidthPowerUp.widthMultiplier;
        this.paddle = gameContext.getPaddle();
    }

    @Override
    public void applyEffect() {
        originalWidth = paddle.getWidth();
        paddle.setWidth(originalWidth * widthMultiplier);
        paddle.setScaled(true);
    }

    @Override
    public void revertEffect() {
        paddle.setWidth(originalWidth);
        paddle.setScaled(false);
    }

    @Override
    public ExpandPaddleWidthPowerUp clone() {
        return (ExpandPaddleWidthPowerUp) super.clone();
    }

}
