package group2048.csse2048;

/**
 * Created by patrickbush on 4/11/17.
 */

public class BoardSpot {
    private int x;
    private int y;

    public BoardSpot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return this.x; }

    public int getY() { return this.y; }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }
}
