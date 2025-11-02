package object.movable.powerup;

import object.movable.Paddle;

public class ExpandPaddlePowerUp extends PowerUp {

    private final float widthMultiplier;

    public ExpandPaddlePowerUp(PowerUp powerUp) {

        super(powerUp);

        ExpandPaddlePowerUp expandPaddlePowerUp = (ExpandPaddlePowerUp) powerUp;
        this.widthMultiplier = expandPaddlePowerUp.widthMultiplier;
    }

    @Override
    public void applyEffect() {
        Paddle paddle = gameContext.getPaddle();
        if(paddle.isSizeScaled()) {
            return;
        }
        float originalWidth = paddle.getWidth();
        float scaledWidth = originalWidth * widthMultiplier;
        paddle.setOriginalWidth(originalWidth);
        paddle.setWidth(scaledWidth);
        paddle.setX(paddle.getX() + (originalWidth - scaledWidth) / 2f);
        paddle.setSizeScaled(true);
    }

    @Override
    public void revertEffect() {
        Paddle paddle = gameContext.getPaddle();
        if(!paddle.isSizeScaled()) {
            return;
        }
        float originalWidth = paddle.getOriginalWidth();
        float scaledWidth = originalWidth * widthMultiplier;
        paddle.setX(paddle.getX() - (originalWidth - scaledWidth) / 2f);
        paddle.setWidth(paddle.getOriginalWidth());
        paddle.setSizeScaled(false);
    }

    @Override
    public ExpandPaddlePowerUp clone() {
        return (ExpandPaddlePowerUp) super.clone();
    }

}
