package utils;

import object.Ball;
import object.Paddle;

public class PhysicsUtils {

    private PhysicsUtils() {}

    public static void bounceOffPaddle(Ball ball, Paddle paddle) {

        float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
        float ballCenter = ball.getX() + ball.getWidth() / 2f;

        float relativeHit = (ballCenter - paddleCenter) / (paddle.getWidth() / 2f);
        relativeHit = Math.max(-1f, Math.min(1f, relativeHit));

        final float MAX_BOUNCE_ANGLE = (float) Math.toRadians(75);
        float baseAngle = relativeHit * MAX_BOUNCE_ANGLE;

        float randomOffset = (float) RandomUtils.nextFloat(0f, 0.2f);
        float bounceAngle = baseAngle + randomOffset;

        float speed = (float) Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());

        float signDx = ball.getDx() > 0 ? 1 : -1;
        float signDy = ball.getDy() > 0 ? 1 : -1;

        float newDx = signDx * speed * (float) Math.sin(bounceAngle);
        float newDy = signDy * speed * (float) Math.cos(bounceAngle);

        ball.setDx(newDx);
        ball.setDy(newDy);
    }
}
