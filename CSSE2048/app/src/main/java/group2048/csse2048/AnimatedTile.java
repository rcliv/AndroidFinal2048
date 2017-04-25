package group2048.csse2048;

/**
 * Created by robbylagen on 4/20/17.
 */

public class AnimatedTile extends BoardSpot {
    private int animation;
    private double animationTime;
    private double animationDelay;
    private double animationTimeElapsed;
    public int[] previousXandY;



    public AnimatedTile(int x, int y, int animation, long animationTime, long animationDelay, int[] previousXandY) {
        super(x, y);
        this.animation = animation;
        this.animationTime = animationTime;
        this.animationDelay = animationDelay;
        this.previousXandY = previousXandY;
    }

    public int getAnimation() { return animation; }

    public double animationPercentageFinished() {
        return Math.max(0, 1.0 * (animationTimeElapsed - animationDelay) / animationTime);
    }

    public void increaseAnimationTimeElapsed(double timeElapsed) {
        this.animationTimeElapsed += timeElapsed;
    }

    public boolean isAnimationFinished() {
        return animationTime + animationDelay < animationTimeElapsed;
    }

    public boolean isAnimationActive() {
        return animationDelay <= animationTimeElapsed;
    }
}
