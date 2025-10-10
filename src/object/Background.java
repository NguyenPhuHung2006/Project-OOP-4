package object;

public class Background extends GameObject {

    /**
     * Tạo background tĩnh, chiếm full màn hình (0,0 -> screenWidth x screenHeight).
     * texturePath: đường dẫn tới file ảnh (ví dụ "assets/textures/background.png")
     * screenWidth/screenHeight: kích thước canvas / window.
     */
    public Background(String texturePath, int screenWidth, int screenHeight) {
        // Sử dụng constructor tiện lợi: sẽ lấy textureX=0,textureY=0,numberOfFrames=1
        // và scale ảnh vào width=screenWidth, height=screenHeight
        super(texturePath, 0, 0, screenWidth, screenHeight, 0, 0, screenWidth, screenHeight, 1);
    }

    @Override
    public void update() {
        // tĩnh -> không cần update
    }

    // Không cần override render nếu muốn dùng render() của GameObject (signature Graphics2D)
    // nếu muốn custom, có thể override:
    // @Override
    // public void render(Graphics2D graphics2D) { super.render(graphics2D); }
}
