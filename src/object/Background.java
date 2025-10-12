package object;

import object.GameObject;

public class Background extends GameObject {

    public Background(Background background) {
        // Sử dụng constructor tiện lợi: sẽ lấy textureX=0,textureY=0,numberOfFrames=1
        // và scale ảnh vào width=screenWidth, height=screenHeight
        super(background);
    }

    @Override
    public void update() {
        
    }

    @Override
    protected void initScreenBounds(GameObject gameObject) {

        x = 0;
        y = 0;
        width = gameContext.getWindowWidth();
        height = gameContext.getWindowHeight();
    }

}
