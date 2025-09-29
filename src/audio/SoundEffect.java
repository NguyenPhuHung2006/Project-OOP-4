package audio;

public enum SoundEffect {

    // these are only used to test by Hung, we can change this once we get further
    BOUNCE("/assets/bounce.wav"),
    BRICK("/assets/brick.wav"),
    MUSIC("/asset/chest_open.wav");

    private final String path;

    SoundEffect(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
