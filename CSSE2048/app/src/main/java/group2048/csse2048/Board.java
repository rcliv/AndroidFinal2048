package group2048.csse2048;

import java.util.ArrayList;

/**
 * Created by patrickbush on 4/11/17.
 */


public class Board {

    public final Tile[][] board;
    public final Tile[][] undoBoard;
    public final Tile[][] bufferedBoard;

    public Board(int xSize, int ySize) {
        board = new Tile[xSize][ySize];
        undoBoard = new Tile[xSize][ySize];
        bufferedBoard = new Tile[xSize][ySize];
        this.clearBoard();
        this.clearUndoBoard();
    }

    public BoardSpot getRandomAvailableBoardSpot() {
        ArrayList<BoardSpot> boardSpotsAvailable = getAllAvailableBoardSpots();
        if (boardSpotsAvailable.size() >= 1) {
            return getAllAvailableBoardSpots().get((int) Math.floor(Math.random() * boardSpotsAvailable.size()));
        }
        return null;
    }

    private ArrayList<BoardSpot> getAllAvailableBoardSpots() {
        ArrayList<BoardSpot> boardSpotsAvailable = new ArrayList<>();
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == null) {
                    // the spot is available
                    boardSpotsAvailable.add(new BoardSpot(x, y));
                }
            }
        }
        return boardSpotsAvailable;
    }

    public boolean areBoardSpotsAvailable() { return getAllAvailableBoardSpots().size() >= 1; }

    public boolean isBoardSpotAvailable(BoardSpot spot) { return !isBoardSpotOccupied(spot); }

    public boolean isBoardSpotOccupied(BoardSpot spot) {
        return (getSpotContent(spot) != null);
    }

    public Tile getSpotContent(BoardSpot spot) {
        if (spot != null && isSpotWithinBounds(spot)) {
            return board[spot.getX()][spot.getY()];
        }
            return null;
    }

    public Tile getSpotContent(int x, int y) {
        if (isSpotWithinBounds(x, y)) {
            return board[x][y];
        }
        return null;
    }

    boolean isSpotWithinBounds(BoardSpot spot) {
        return 0 <= spot.getX() && spot.getX() < board.length
                && 0 <= spot.getY() && spot.getY() < board[0].length;
    }

    private boolean isSpotWithinBounds(int x, int y) {
        return 0 <= x && x < board.length && 0 <= y && y < board[0].length;
    }

    void insertTile(Tile tile) { board[tile.getX()][tile.getY()] = tile; }

    void removeTile(Tile tile) { board[tile.getX()][tile.getY()] = null; }

    void saveTiles() {
        for (int x = 0; x < bufferedBoard.length; x++) {
            for (int y = 0; y < bufferedBoard[0].length; y++) {
                if (bufferedBoard[x][y] == null) {
                    undoBoard[x][y] = null;
                } else {
                    undoBoard[x][y] = new Tile(x, y, bufferedBoard[x][y].getValue());
                }
            }
        }
    }

    void prepareSaveTiles() {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == null) {
                    bufferedBoard[x][y] = null;
                } else {
                    bufferedBoard[x][y] = new Tile(x, y, board[x][y].getValue());
                }
            }
        }
    }

    void revertTiles() {
        for (int x = 0; x < undoBoard.length; x++) {
            for (int y = 0; y < undoBoard[0].length; y++) {
                if (undoBoard[x][y] == null) {
                    board[x][y] = null;
                } else {
                    board[x][y] = new Tile(x, y, undoBoard[x][y].getValue());
                }
            }
        }
    }

    void clearBoard() {
        for (int x = 0; x < undoBoard.length; x++) {
            for (int y = 0; y < undoBoard[0].length; y++) {
                board[x][y] = null;
            }
        }
    }

    void clearUndoBoard() {
        for (int x = 0; x < undoBoard.length; x++) {
            for (int y = 0; y < undoBoard[0].length; y++) {
                undoBoard[x][y] = null;
            }
        }
    }
}

