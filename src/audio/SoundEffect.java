package audio;

public enum SoundEffect {

    NORMAL_BRICK("/sounds/normalBrick.wav"),
    STRONG_BRICK("/sounds/strongBrick.wav");

    private final String path;

    SoundEffect(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
