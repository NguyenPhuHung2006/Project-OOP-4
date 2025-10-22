package object.movable.powerup;

import object.movable.Paddle;

public class ExpandPaddleWidthPowerUp extends PowerUp {

    private final float widthMultiplier;
    private final Paddle paddle;
    private final float originalWidth;
    private final float scaledWidth;

    public ExpandPaddleWidthPowerUp(ExpandPaddleWidthPowerUp expandPaddleWidthPowerUp) {

        super(expandPaddleWidthPowerUp);

        this.widthMultiplier = expandPaddleWidthPowerUp.widthMultiplier;
        this.paddle = gameContext.getPaddle();
        this.originalWidth = paddle.getOriginalWidth();
        this.scaledWidth = this.originalWidth * widthMultiplier;
    }

    @Override
    public void applyEffect() {
        if(paddle.isScaled()) {
            return;
        }
        paddle.setX(paddle.getX() + (originalWidth - scaledWidth) / 2f);
        paddle.setWidth(originalWidth * widthMultiplier);
        paddle.setScaled(true);
    }

    @Override
    public void revertEffect() {
        if(!paddle.isScaled()) {
            return;
        }
        paddle.setX(paddle.getX() - (originalWidth - scaledWidth) / 2f);
        paddle.setWidth(originalWidth);
        paddle.setScaled(false);
    }

    @Override
    public ExpandPaddleWidthPowerUp clone() {
        return (ExpandPaddleWidthPowerUp) super.clone();
    }

}
