package screen;

import screen.endscreen.GameOverScreen;
import screen.endscreen.GameWinScreen;
import screen.menuscreen.MenuScreen;
import screen.menuscreen.PlayerStatusScreen;
import screen.menuscreen.StartScreen;
import screen.pausescreen.MultiPlayerPauseScreen;
import screen.pausescreen.PauseScreen;
import screen.pausescreen.SinglePlayerPauseScreen;
import screen.playscreen.MultiPlayerPlayScreen;
import screen.playscreen.PlayScreen;
import screen.playscreen.SinglePlayerPlayScreen;

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
            return new SinglePlayerPlayScreen(screen, PLAY_LEVEL1);
        }
    },
    PLAY_LEVEL2 {
        @Override
        public Screen create(Screen screen) {
            return new SinglePlayerPlayScreen(screen, PLAY_LEVEL2);
        }
    },
    PLAY_LEVEL3 {
        @Override
        public Screen create(Screen screen) {
            return new SinglePlayerPlayScreen(screen, PLAY_LEVEL3);
        }
    },
    MULTI_PLAYER {
        @Override
        public Screen create(Screen screen) {
            return new MultiPlayerPlayScreen(screen, MULTI_PLAYER);
        }
    },
    SINGLE_PLAYER_PAUSE {
        @Override
        public Screen create(Screen screen) {
            return new SinglePlayerPauseScreen(screen);
        }
    },
    MULTIPLE_PLAYER_PAUSE {
        @Override
        public Screen create(Screen screen) {
            return new MultiPlayerPauseScreen(screen);
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

