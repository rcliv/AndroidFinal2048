package group2048.csse2048;

import java.util.ArrayList;

/**
 * Created by robbylagen on 4/20/17.
 */

public class AnimatedBoard {
    private ArrayList<AnimatedTile>[][] animatedBoard;
    private int activeAnimations = 0;
    private boolean lastActiveTile = false;

    public AnimatedBoard(int x, int y) {
        animatedBoard = new ArrayList[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                animatedBoard[i][j] = new ArrayList<>();
            }
        }
    }

    public void animateTile(int x, int y, int animation, long animationTime, long animationDelay, int[] previousXandY) {
        AnimatedTile tileToAnimate = new AnimatedTile(x, y, animation, animationTime, animationDelay, previousXandY);
        animatedBoard[x][y].add(tileToAnimate);
        activeAnimations++;
    }

    public boolean isAnimationActive() {
        if (activeAnimations != 0) {
            lastActiveTile = true;
            return true;
        } else if (lastActiveTile) {
            lastActiveTile = false;
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<AnimatedTile> getAnimatedTileFromBoard(int x, int y) {
        return animatedBoard[x][y];
    }

    public void endAnimations() {
        activeAnimations = 0;
        for (ArrayList<AnimatedTile>[] a : animatedBoard) {
            for (ArrayList<AnimatedTile> b : a) {
                b.clear();
            }
        }
    }

    public void endAnimation(AnimatedTile animatedTile) {
        animatedBoard[animatedTile.getX()][animatedTile.getY()].remove(animatedTile);
    }

    public void increaseAnimationTimeElapsedForAll(double animationTimeElapsed) {
        ArrayList<AnimatedTile> animationsToEnd = new ArrayList<>();
        for (ArrayList<AnimatedTile>[] a : animatedBoard) {
            for (ArrayList<AnimatedTile> b : a){
                for (AnimatedTile tile : b) {
                    tile.increaseAnimationTimeElapsed(animationTimeElapsed);
                    if (tile.isAnimationFinished()) {
                        animationsToEnd.add(tile);
                        activeAnimations--;
                    }
                }
            }
        }

        for (AnimatedTile tile : animationsToEnd) {
            endAnimation(tile);
        }
    }

}
