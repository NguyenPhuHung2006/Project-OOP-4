package screen;

public enum ScreenType {
    START {
        @Override
        public Screen create(Screen screen) {
            return new StartScreen(screen);
        }
    },
    MENU {
        @Override
        public Screen create(Screen screen) {
            return new MenuScreen(screen);
        }
    },
    PLAY_LEVEL1 {
        @Override
        public Screen create(Screen screen) {
            return new PlayScreen(screen, PLAY_LEVEL1);
        }
    },
    PLAY_LEVEL2 {
        @Override
        public Screen create(Screen screen) {
            return new PlayScreen(screen, PLAY_LEVEL2);
        }
    },
    PLAY_LEVEL3 {
        @Override
        public Screen create(Screen screen) {
            return new PlayScreen(screen, PLAY_LEVEL3);
        }
    },
    MULTI_PLAYER {
        @Override
        public Screen create(Screen screen) {
            return new MultiPlayerPlayScreen(screen, MULTI_PLAYER);
        }
    },
    PAUSE {
        @Override
        public Screen create(Screen screen) {
            return new PauseScreen(screen);
        }
    },
    LOADING {
        @Override
        public Screen create(Screen screen) {
            return new LoadingScreen();
        }
    },
    GAME_OVER {
        @Override
        public Screen create(Screen screen) {
            return new GameOverScreen(screen);
        }
    },
    GAME_WIN {
        @Override
        public Screen create(Screen screen) {
            return new GameWinScreen(screen);
        }
    },
    PLAYER_STATUS {
        @Override
        public Screen create(Screen screen) {
            return new PlayerStatusScreen(screen);
        }
    };

    public abstract Screen create(Screen screen);
}

