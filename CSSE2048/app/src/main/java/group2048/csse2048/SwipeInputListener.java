package group2048.csse2048;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by patrickbush on 4/29/17.
 */

public class SwipeInputListener implements View.OnTouchListener {

    private static final int MIN_SWIPE_DISTANCE = 0;
    private static final int SWIPE_VELOCITY_THRESHOLD = 25;
    private static final int MOVE_THRESHOLD = 250;
    private static final int RESET_POSITION = 10;

    private final BoardView boardView;

    private float x, y;
    private float lastDx, lastDy;
    private float previousX, previousY;
    private float startingX, startingY;
    private int previousDirection = 1, mostRecentDirection = 1;
    private boolean hasMoved = false;

    public SwipeInputListener(BoardView boardView) {
        super();
        this.boardView = boardView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                startingX = x;
                startingY = y;
                previousX = x;
                previousY = y;
                lastDx = 0;
                lastDy = 0;
                hasMoved = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                if (boardView.game.gameIsActive()) {
                    float dx = x - previousX;
                    if (Math.abs(lastDx + dx) < Math.abs(lastDx) + Math.abs(dx) && Math.abs(dx) > RESET_POSITION
                            && Math.abs(x - startingX) > MIN_SWIPE_DISTANCE) {
                        startingX = x;
                        startingY = y;
                        lastDx = dx;
                        previousDirection = mostRecentDirection;
                    }
                    if (lastDx == 0) {
                        lastDx = dx;
                    }
                    float dy = y - previousY;
                    if (Math.abs(lastDy + dy) < Math.abs(lastDy) + Math.abs(dy) && Math.abs(dy) > RESET_POSITION
                            && Math.abs(y - startingY) > MIN_SWIPE_DISTANCE) {
                        startingX = x;
                        startingY = y;
                        lastDy = dy;
                        previousDirection = mostRecentDirection;
                    }
                    if (lastDy == 0) {
                        lastDy = dy;
                    }
                    if (pathMoved() > MIN_SWIPE_DISTANCE * MIN_SWIPE_DISTANCE && !hasMoved) {
                        boolean moved = false;
                        //Vertical
                        if (((dy >= SWIPE_VELOCITY_THRESHOLD && Math.abs(dy) >= Math.abs(dx)) || y - startingY >= MOVE_THRESHOLD) && previousDirection % 2 != 0) {
                            moved = true;
                            previousDirection = previousDirection * 2;
                            mostRecentDirection = 2;
                            boardView.game.move(2);
                        } else if (((dy <= -SWIPE_VELOCITY_THRESHOLD && Math.abs(dy) >= Math.abs(dx)) || y - startingY <= -MOVE_THRESHOLD) && previousDirection % 3 != 0) {
                            moved = true;
                            previousDirection = previousDirection * 3;
                            mostRecentDirection = 3;
                            boardView.game.move(0);
                        }
                        //Horizontal
                        if (((dx >= SWIPE_VELOCITY_THRESHOLD && Math.abs(dx) >= Math.abs(dy)) || x - startingX >= MOVE_THRESHOLD) && previousDirection % 5 != 0) {
                            moved = true;
                            previousDirection = previousDirection * 5;
                            mostRecentDirection = 5;
                            boardView.game.move(1);
                        } else if (((dx <= -SWIPE_VELOCITY_THRESHOLD && Math.abs(dx) >= Math.abs(dy)) || x - startingX <= -MOVE_THRESHOLD) && previousDirection % 7 != 0) {
                            moved = true;
                            previousDirection = previousDirection * 7;
                            mostRecentDirection = 7;
                            boardView.game.move(3);
                        }
                        if (moved) {
                            hasMoved = true;
                            startingX = x;
                            startingY = y;
                        }
                    }
                }
                previousX = x;
                previousY = y;
                return true;
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                previousDirection = 1;
                mostRecentDirection = 1;
        }
        return true;
    }

    private float pathMoved() {
        return (x - startingX) * (x - startingX) + (y - startingY) * (y - startingY);
    }
}
