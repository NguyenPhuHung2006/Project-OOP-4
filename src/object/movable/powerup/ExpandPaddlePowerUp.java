package object.movable.powerup;

import object.movable.Paddle;

/**
 * A {@link PowerUp} that increases the width of the paddle for a limited duration.
 * <p>
 * This makes the game easier temporarily by enlarging the hitbox of the paddle.
 * </p>
 */
public class ExpandPaddlePowerUp extends PowerUp {

    private final float widthMultiplier;

    public ExpandPaddlePowerUp(PowerUp powerUp) {

        super(powerUp);

        ExpandPaddlePowerUp expandPaddlePowerUp = (ExpandPaddlePowerUp) powerUp;
        this.widthMultiplier = expandPaddlePowerUp.widthMultiplier;
    }

    /**
     * Doubles (or multiplies by a factor) the paddle width if not already scaled.
     */
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

    /**
     * Restores the paddle to its original size when the power-up expires.
     */
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
