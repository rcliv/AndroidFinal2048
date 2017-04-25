package group2048.csse2048;

/**
 * Created by patrickbush on 4/11/17.
 */

public class Tile extends BoardSpot {
    final int value;
    private Tile[] mergedWith;

    public Tile(int x, int y, int value) {
        super(x, y);
        this.value = value;
    }

    public Tile(BoardSpot spot, int value) {
        super(spot.getX(), spot.getY());
        this.value = value;
    }

    void updatePosition(BoardSpot spot) {
        this.setX(spot.getX());
        this.setY(spot.getY());
    }

    int getValue() { return this.value; }

    Tile[] getTilesMergedWith() { return mergedWith; }

    void setMergedWith(Tile[] tiles) { mergedWith = tiles; }
}
