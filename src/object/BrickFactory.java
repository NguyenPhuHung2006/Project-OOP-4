package object;

import java.awt.image.BufferedImage;

public class BrickFactory {
    public static NormalBrick createNormalBrick(NormalBrick normalBrick) {
        return new NormalBrick(normalBrick);
    }
    public static StrongBrick createStrongBrick(StrongBrick strongBrick) {
        return new StrongBrick(strongBrick);
    }
}
