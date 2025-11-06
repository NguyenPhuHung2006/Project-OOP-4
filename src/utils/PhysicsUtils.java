package utils;

import object.movable.*;

/**
 * A utility class that provides physics-related calculations and behaviors
 * for movable game objects such as {@link Ball} and {@link Paddle}.
 * <p>
 * This class is not meant to be instantiated.
 * </p>
 */
public class PhysicsUtils {

    private PhysicsUtils() {
    }

    /**
     * The maximum bounce angle (in radians) that a ball can reflect off a paddle.
     */
    static final float MAX_BOUNCE_ANGLE = (float) Math.toRadians(50);

    /**
     * Calculates and applies the bounce direction and velocity of a {@link Ball}
     * after colliding with a {@link Paddle}.
     * <p>
     * The bounce angle is determined by where the ball hits the paddle. Hitting
     * closer to the paddle's edge results in a steeper bounce angle. A small
     * random offset is also added to make the bounce behavior less predictable.
     * </p>
     *
     * @param ball   the {@link Ball} object that bounces off the paddle
     * @param paddle the {@link Paddle} object the ball collides with
     * @throws NullPointerException if either {@code ball} or {@code paddle} is {@code null}
     */
    public static void bounceOffPaddle(Ball ball, Paddle paddle) {

        float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
        float ballCenter = ball.getX() + ball.getWidth() / 2f;

        float relativeHit = (ballCenter - paddleCenter) / (paddle.getWidth() / 2f);
        relativeHit = Math.max(-1f, Math.min(1f, relativeHit));

        float baseAngle = relativeHit * MAX_BOUNCE_ANGLE;

        float randomOffset = RandomUtils.nextFloat(0f, 0.2f);
        float bounceAngle = baseAngle + randomOffset;

        float speed = (float) Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());

        float signDx = ball.getDx() > 0 ? 1 : -1;
        float signDy = ball.getDy() > 0 ? 1 : -1;

        float newDx = signDx * speed * (float) Math.abs(Math.sin(bounceAngle));
        float newDy = signDy * speed * (float) Math.abs(Math.cos(bounceAngle));

        ball.setDx(newDx);
        ball.setDy(newDy);
    }
}
