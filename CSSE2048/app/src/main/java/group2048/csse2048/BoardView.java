package group2048.csse2048;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by robbylagen on 4/12/17.
 */

public class BoardView extends View {

    public int startingX;
    public int startingY;
    public int endingX;
    public int endingY;
    public int iconSize;
    public int numTileTypes = 12;
    public final MainGame game;
    public boolean refresh = true;
    public boolean lastRefresh = true;
    public double lastRefreshTime = System.nanoTime();

    private Bitmap background = null;
    private final Paint paint = new Paint();
    private final BitmapDrawable[] bitmapCell = new BitmapDrawable[numTileTypes];

    private Drawable boardBackground;

    private int tileSize = 0;
    private int boardWidth = 0;
    private float textSize = 0;

    public static final int BASE_ANIMATION_TIME = 100000000;

    public BoardView(Context context) {
        super(context);

        // Load Resources
        game = new MainGame(context, this);
        try {
            boardBackground = getDrawable(R.drawable.board_backgroud);
            paint.setAntiAlias(true);
        } catch (Exception e) {
            Log.e("BoardView", "Error getting assets", e);
        }

        // set on touch listner
        setOnTouchListener(new SwipeInputListener(this));
        game.newGame();
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Reset the transparency of the screen
        canvas.drawBitmap(background, 0, 0, paint);

        drawCells(canvas);

        if (game.animatedBoard.isAnimationActive()) {
            invalidate(startingX, startingY, endingX, endingY);
            increaseTimeElapsed();
        } else if (!game.gameIsActive() && lastRefresh) {
            invalidate();
            lastRefresh = false;
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldW, int oldH) {
        super.onSizeChanged(width, height, oldW, oldH);
        getLayout(width, height);
        createBitmapTiles();
        createBitmapBackground(width, height);
    }

    private void drawCells(Canvas canvas) {
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        // Outputting the individual cells
        for (int x = 0; x < game.numTilesX; x++) {
            for (int y = 0; y < game.numTilesY; y++) {
                int startX = startingX + boardWidth + (tileSize + boardWidth) * x;
                int endX = startX + tileSize;
                int startY = startingY + boardWidth + (tileSize + boardWidth) * y;
                int endY = startY + tileSize;

                Tile currentTile = game.board.getSpotContent(x, y);
                if (currentTile != null) {
                    // Get and represent the value of the tile
                    int value = currentTile.getValue();
                    int index = log2(value);

                    ArrayList<AnimatedTile> animatedTiles = game.animatedBoard.getAnimatedTileFromBoard(x, y);
                    boolean animated = false;
                    animated = animateTile(canvas, startX, endX, startY, endY, currentTile, index, animatedTiles, animated);
                    if (!animated) {
                        bitmapCell[index].setBounds(startX, startY, endX, endY);
                        bitmapCell[index].draw(canvas);
                    }
                }
            }
        }
    }

    private boolean animateTile(Canvas canvas, int startX, int endX, int startY, int endY, Tile currentTile, int index, ArrayList<AnimatedTile> animatedTiles, boolean animated) {
        for (int i = 0; i < animatedTiles.size(); i++) {
            AnimatedTile animatedTile = animatedTiles.get(i);
            if (animatedTile.getAnimation() == MainGame.NEW_TILE_ANIMATION)
                animated = true;
            if (!animatedTile.isAnimationActive())
                continue;
            if (animatedTile.getAnimation() == MainGame.MOVING_TILE_ANIMATION) {
                double percentDone = animatedTile.animationPercentageFinished();
                int tempIndex = index;
                if (animatedTiles.size() >= 2) {
                    tempIndex = tempIndex - 1;
                }
                int previousX = animatedTile.previousXandY[0];
                int previousY = animatedTile.previousXandY[1];
                int currentX = currentTile.getX();
                int currentY = currentTile.getY();
                int dX = (int) ((currentX - previousX) * (tileSize + boardWidth) * (percentDone - 1) * 1.0);
                int dY = (int) ((currentY - previousY) * (tileSize + boardWidth) * (percentDone - 1) * 1.0);
                bitmapCell[tempIndex].setBounds(startX + dX, startY + dY, endX + dX, endY + dY);
                bitmapCell[tempIndex].draw(canvas);
            }
            animated = true;
        }
        return animated;
    }


    private void getLayout(int width, int height) {
        tileSize = Math.min(width / (game.numTilesX + 1), height / (game.numTilesY + 3));
        boardWidth = 70;
        int screenMidX = width / 2;
        int screenMidY = height / 2;

        iconSize = tileSize / 2;

        // Board Dimensions
        double halfNumSquaresX = 2;
        double halfNumSquaresY = 2;
        startingX = (int) (screenMidX - (tileSize + boardWidth) * halfNumSquaresX - boardWidth / 2);
        endingX = (int) (screenMidX + (tileSize + boardWidth) * halfNumSquaresX + boardWidth / 2);
        startingY = (int) (screenMidY - (tileSize + boardWidth) * halfNumSquaresY - boardWidth / 2);
        endingY = (int) (screenMidY + (tileSize + boardWidth) * halfNumSquaresY + boardWidth / 2);

        textSize = tileSize * tileSize / Math.max(tileSize, paint.measureText("0000"));

        paint.setTextAlign(Paint.Align.CENTER);
    }

    private void createBitmapTiles() {
        Resources res = getResources();
        int[] tileIds = getTileIds();
        for (int i = 0; i < bitmapCell.length; i++) {
            int val = (int) Math.pow(2, i);
            Bitmap bitmap = Bitmap.createBitmap(tileSize, tileSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawDrawable(canvas, getDrawable(tileIds[i]), 0, 0, tileSize, tileSize);
            drawTileText(canvas, val);
            bitmapCell[i] = new BitmapDrawable(res, bitmap);
        }
    }

    private void createBitmapBackground(int width, int height) {
        background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        drawBackground(canvas);
        drawBackgroundBoard(canvas);
    }

    private Drawable getDrawable(int resId) {
        return ResourcesCompat.getDrawable(getResources(), resId, null);
    }

    private void drawDrawable(Canvas canvas, Drawable draw, int startingX, int startingY, int endingX, int endingY) {
        draw.setBounds(startingX, startingY, endingX, endingY);
        draw.draw(canvas);
    }

    private void drawTileText(Canvas canvas, int value) {
        int textShiftY = centerText();
        if (value >= 8) {
            paint.setColor(ResourcesCompat.getColor(getResources(), R.color.text_white, null));
        } else {
            paint.setColor(ResourcesCompat.getColor(getResources(), R.color.text_black, null));
        }
        paint.setTextSize(scaleTextSize(value));
        canvas.drawText("" + value, tileSize / 2, tileSize / 2 - textShiftY, paint);
    }

    private float scaleTextSize(int value) {
        int numDigits = (int) (Math.log10(value) + 1);
        return 100 * (1 - (numDigits / 10.0f));

    }

    private void drawBackground(Canvas canvas) {
        drawDrawable(canvas, boardBackground, startingX, startingY, endingX, endingY);
    }

    private void drawBackgroundBoard(Canvas canvas) {
        Drawable backgroundTile = getDrawable(R.drawable.tile);
        for (int x = 0; x < game.numTilesX; x++) {
            for (int y = 0; y < game.numTilesY; y++) {
                int startX = startingX + boardWidth + (tileSize + boardWidth) * x;
                int endX = startX + tileSize;
                int startY = startingY + boardWidth + (tileSize + boardWidth) * y;
                int endY = startY + tileSize;
                drawDrawable(canvas, backgroundTile, startX, startY, endX, endY);
            }
        }
    }

    private int[] getTileIds() {
        int[] tileIds = new int[numTileTypes];
        tileIds[0] = R.drawable.tile;
        tileIds[1] = R.drawable.tile_2;
        tileIds[2] = R.drawable.tile_4;
        tileIds[3] = R.drawable.tile_8;
        tileIds[4] = R.drawable.tile_16;
        tileIds[5] = R.drawable.tile_32;
        tileIds[6] = R.drawable.tile_64;
        tileIds[7] = R.drawable.tile_128;
        tileIds[8] = R.drawable.tile_256;
        tileIds[9] = R.drawable.tile_512;
        tileIds[10] = R.drawable.tile_1024;
        tileIds[11] = R.drawable.tile_2048;
        return tileIds;
    }

    private int centerText() {
        return (int) ((paint.descent() + paint.ascent()) / 2);
    }

    private static int log2(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    public void resynch() {
        lastRefreshTime = System.nanoTime();
    }

    private void increaseTimeElapsed() {
        double currentTime = System.nanoTime();
        game.animatedBoard.increaseAnimationTimeElapsedForAll(currentTime - lastRefreshTime);
        lastRefreshTime = currentTime;
    }
}
