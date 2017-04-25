package group2048.csse2048;

/**
 * Created by andrewtoomey on 4/15/17.
 */

public interface IMainGame {
    enum DIRECTIONS {
     UP, RIGHT, DOWN, LEFT
    }

    void setCurrentGame(MainGame currentGame);
}
