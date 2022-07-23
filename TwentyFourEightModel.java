package org.cis120.twentyfoureight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * CIS 120 HW09 - 2048
 * (c) University of Pennsylvania
 * Created by Lauryn Fuld
 */

/**
 * This class is a model for 2048.
 *
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 *
 */
public class TwentyFourEightModel {

    private final int columns;
    private final int rows;
    private int[][] gameBoard;
    private boolean gameOver;
    private static int bestScore = 0;
    private List<int[][]> gameStates;

    /**
     * Constructor sets up game state, initializing a new
     * game with a board of the default size (4 x 4). The
     * board will be randomly populated with two 2 values.
     */
    public TwentyFourEightModel() {
        columns = 4;
        rows = 4;
        gameBoard = new int[4][4];
        gameOver = false;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                gameBoard[r][c] = 0;
            }
        }
        addRandom2Or4();
        addRandom2Or4();
        gameStates = new LinkedList<>();

        // deep copy board and save to gameStates
        int[][] result = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = gameBoard[i][j];
            }
        }
        gameStates.add(result);

    }

    public TwentyFourEightModel(int k) {
        columns = 4;
        rows = 4;
        gameBoard = new int[4][4];
        gameOver = false;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                gameBoard[r][c] = 0;
            }
        }
        gameBoard[1][2] = 2;
        gameBoard[2][1] = 4;
        gameStates = new LinkedList<>();

        // deep copy board and save to gameStates
        int[][] result = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = gameBoard[i][j];
            }
        }
        gameStates.add(result);
        // System.out.println("new GS");
        // printGameStates();
    }

    public int[][] getGameBoard() {
        int[][] result = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = gameBoard[i][j];
            }
        }
        return result;
    }

    public boolean getGameIsOver() {
        return gameOver;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int i) {
        bestScore = i;
    }

    /**
     * each number must be on a new line, the first four lines
     * are placed in row 1, second 4 numbers place in row 2, etc.
     * 
     * @param filename the file in which the game board's
     *                 contents will be read from
     */
    public TwentyFourEightModel(String filename) {
        columns = 4;
        rows = 4;
        gameBoard = new int[4][4];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                gameBoard[r][c] = 0;
            }
        }
        gameOver = false;

        File file = new File(filename);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    String currVal = br.readLine();
                    // when there is nothing left to read throw exception because there should be 16
                    // digits
                    if (currVal == null) {
                        throw new IOException("must be 16 numbers in the file: this file has less");
                    }
                    int currValInt = Integer.parseInt(currVal);
                    if (currValInt % 2 != 0) {
                        throw new IOException("All file values must be a multiple of 2");
                    }
                    gameBoard[r][c] = 0;
                }
            }
            if (br.readLine() != null) {
                throw new IOException("must be 16 numbers in the file: this file has more");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        gameStates = new LinkedList<>();
        gameStates.add(gameBoard);
    }

    public List<int[][]> getGameStates() {
        return gameStates;
    }

    public void printGameStates() {
        for (int[][] gameState : gameStates) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    System.out.print(gameState[row][col] + " ");
                }
                System.out.println();
            }
        }
    }

    /**
     * get the value of a particular cell in the 2D in the game board
     *
     * @param r the row target in the game board
     * @param c the column target in the game board
     * @return the value of the cell that corresponds to the
     *         row and col values that were entered into the function
     * @throws IndexOutOfBoundsException
     */
    public int getCellValue(int r, int c) throws IndexOutOfBoundsException {
        if (c < 0 || c >= columns || r < 0 || r > rows) {
            throw new IndexOutOfBoundsException();
        }
        return gameBoard[r][c];
    }

    // method is implemented for testing purpose
    public void setCellValue(int r, int c, int v) {
        if (r < 0 || r > 3) {
            throw new IllegalArgumentException("not valid row");
        } else if (c < 0 || c > 3) {
            throw new IllegalArgumentException("not valid col");
        }
        gameBoard[r][c] = v;

    }

    /**
     * Shifts all the values in the game board to the left.
     * All empty squares (denoted as housing 0s) are filled in
     * by the value in the same row and adjacent (to the right) col.
     * If two values are equal, they will combine, housing their sum
     * in the left most col of the two values.
     */
    private void moveLeft() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int currentVal = gameBoard[row][col];
                if (currentVal != 0) {
                    int currentCol = col;
                    // stopped here
                    while (currentCol >= 0) {
                        if (gameBoard[row][currentCol] != 0) {
                            if (currentVal == gameBoard[row][currentCol] && currentCol != col) {
                                gameBoard[row][currentCol] = currentVal * 2;
                                gameBoard[row][currentCol + 1] = 0;
                            }
                        } else {
                            gameBoard[row][currentCol] = currentVal;
                            gameBoard[row][currentCol + 1] = 0;
                        }
                        currentCol--;
                    }
                }
            }
        }
    }

    /**
     * Shifts all the values in the game board to the top.
     * All empty squares (denoted as housing 0s) are filled in
     * by the value in the same col and adjacent (below) row.
     * If two values are equal, they will combine, housing their sum
     * in the top most col row the two values.
     */
    private void moveUp() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int currentVal = gameBoard[row][col];
                if (currentVal != 0) {
                    int currentRow = row;
                    // stopped here
                    while (currentRow >= 0) {
                        if (gameBoard[currentRow][col] != 0) {
                            if (currentVal == gameBoard[currentRow][col] && currentRow != row) {
                                gameBoard[currentRow][col] = currentVal * 2;
                                gameBoard[currentRow + 1][col] = 0;
                            }
                        } else {
                            gameBoard[currentRow][col] = currentVal;
                            gameBoard[currentRow + 1][col] = 0;
                        }
                        currentRow--;
                    }
                }
            }
        }
    }

    /**
     * Shifts all the values in the game board to the right.
     * All empty squares (denoted as housing 0s) are filled in
     * by the value in the same row and adjacent (to the left) col.
     * If two values are equal, they will combine, housing their sum
     * in the right most col of the two values.
     */
    private void moveRight() {
        for (int row = 0; row < 4; row++) {
            for (int col = 3; col >= 0; col--) {
                int currentVal = gameBoard[row][col];
                if (currentVal != 0) {
                    int currentCol = col;
                    // stopped here
                    while (currentCol < 4) {
                        if (gameBoard[row][currentCol] != 0) {
                            if (currentVal == gameBoard[row][currentCol] && currentCol != col) {
                                gameBoard[row][currentCol] = currentVal * 2;
                                gameBoard[row][currentCol - 1] = 0;
                            }
                        } else {
                            gameBoard[row][currentCol] = currentVal;
                            gameBoard[row][currentCol - 1] = 0;
                        }
                        currentCol++;
                    }
                }
            }
        }
    }

    /**
     * Shifts all the values in the game board to the bottom.
     * All empty squares (denoted as housing 0s) are filled in
     * by the value in the same col and adjacent (below) row.
     * If two values are equal, they will combine, housing their sum
     * in the lower most col row the two values.
     */
    private void moveDown() {
        for (int row = 3; row >= 0; row--) {
            for (int col = 0; col < 4; col++) {
                int currentVal = gameBoard[row][col];
                if (currentVal != 0) {
                    int currentRow = row;
                    // stopped here
                    while (currentRow < 4) {
                        if (gameBoard[currentRow][col] != 0) {
                            if (currentVal == gameBoard[currentRow][col] && currentRow != row) {
                                gameBoard[currentRow][col] = currentVal * 2;
                                gameBoard[currentRow - 1][col] = 0;
                            }
                        } else {
                            gameBoard[currentRow][col] = currentVal;
                            gameBoard[currentRow - 1][col] = 0;
                        }
                        currentRow++;
                    }
                }
            }
        }
    }

    /**
     * Places a new value, 2 or 4, randomly at one of
     * the empty positions in the game board. The method
     * is only public for testing purposes
     */
    public void addRandom2Or4() {
        if (boardIsFull()) {
            return;
        }

        Random r = new Random();
        int row = r.nextInt(rows);
        int col = r.nextInt(columns);
        if (gameBoard[row][col] != 0) {
            while (gameBoard[row][col] != 0) {
                row = r.nextInt(rows);
                col = r.nextInt(columns);
            }
        }
        Random random = new Random();
        double rng = random.nextInt(2);
        if (rng < 1) {
            gameBoard[row][col] = 2;
        } else {
            gameBoard[row][col] = 4;
        }
    }

    private boolean boardIsFull() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (gameBoard[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * check to see if there are any moves that can be played,
     * given the current game state
     *
     * @return true is there are feasible moves to be played in the game
     */
    public boolean movesLeftToPlay() {
        if (!boardIsFull()) {
            return true;
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int currCellVal = gameBoard[row][col];
                if (row != rows - 1 && currCellVal == gameBoard[row + 1][col]) {
                    return true;
                } else if (col != columns - 1 && currCellVal == gameBoard[row][col + 1]) {
                    return true;
                } else if (row != 0 && currCellVal == gameBoard[row - 1][col]) {
                    return true;
                } else if (col != 0 && currCellVal == gameBoard[row][col - 1]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * shifts the board according to the user's input then returns true
     * if that move ended the game (also return true if game is already over)
     * or return false if the game is still occurring
     *
     * @param direction a string that must be "left", "right", "up", "down"
     *                  to indicate which direction you want the board to shift
     * @throws IllegalArgumentException if any string besides "left", "right", "up",
     *                                  "down"
     *                                  is passed into the function (capitalization
     *                                  does not matter), this exception will
     *                                  immediately be thrown
     */
    public void move(String direction) throws IllegalArgumentException {
        String[] acceptableDirections = { "left", "right", "up", "down" };
        List<String> acceptableDirectionsList = new ArrayList<>(
                Arrays.asList(acceptableDirections)
        );

        if (!acceptableDirectionsList.contains(direction.toLowerCase())) {
            throw new IllegalArgumentException(
                    "direction must be either: left, right, up, or down"
            );
        }

        if (gameOver) {
            return;
        }
        direction = direction.toLowerCase();

        if (direction.equals("left")) {
            int scoreBeforeShift = getGameScore();
            int piecesBefore = numNonZeroPieces();
            moveLeft();
            int scoreAfterShift = getGameScore();
            int piecesAfter = numNonZeroPieces();
            moveScoreHelper(scoreBeforeShift, scoreAfterShift, piecesBefore, piecesAfter);

        } else if (direction.equals("right")) {
            int scoreBeforeShift = getGameScore();
            int piecesBefore = numNonZeroPieces();
            moveRight();
            printGameStates();
            int scoreAfterShift = getGameScore();
            int piecesAfter = numNonZeroPieces();
            moveScoreHelper(scoreBeforeShift, scoreAfterShift, piecesBefore, piecesAfter);

        } else if (direction.equals("up")) {
            int scoreBeforeShift = getGameScore();
            int piecesBefore = numNonZeroPieces();
            moveUp();
            int scoreAfterShift = getGameScore();
            int piecesAfter = numNonZeroPieces();
            moveScoreHelper(scoreBeforeShift, scoreAfterShift, piecesBefore, piecesAfter);

        } else if (direction.equals("down")) {
            int scoreBeforeShift = getGameScore();
            int piecesBefore = numNonZeroPieces();
            moveDown();
            int scoreAfterShift = getGameScore();
            int piecesAfter = numNonZeroPieces();
            moveScoreHelper(scoreBeforeShift, scoreAfterShift, piecesBefore, piecesAfter);
        }
        // deep copy board and save to gameStates
        int[][] result = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = gameBoard[i][j];
            }
        }
        gameStates.add(result);
        System.out.println("new GS");
        printGameStates();
    }

    /**
     * if the game score does not change from the shifting
     * we must test if the game board is full because if not
     * then we must randomly insert a two. If the board is full
     * and there are no moves to be played, then the game must
     * be over.
     *
     * @param score1 the game's score before the shift occurs
     * @param score2 the game's score after the shift occurs
     */
    private void moveScoreHelper(int score1, int score2, int nonZeroBefore, int nonZeroAfter) {
        boolean numTilesDecreased = nonZeroBefore > nonZeroAfter;
        if (score1 == score2 && !numTilesDecreased) {
            if (!boardIsFull()) {
                addRandom2Or4();
            }
            if (!movesLeftToPlay()) {
                gameOver = true;
            }
        }
    }

    private int numNonZeroPieces() {
        int count = 0;
        for (int row = 0; row < rows; row++) {
            for (int c = 0; c < columns; c++) {
                if (gameBoard[row][c] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Sums up all the values in the game board,
     * and returns that figure
     *
     * @return the sum of all the values in the
     *         game board
     */
    public int getGameScore() {
        int score = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                score += gameBoard[row][col];
            }
        }
        return score;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void print2048Game() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                System.out.print(gameBoard[row][col] + " ");
            }
            System.out.println();
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset2048() {
        if (gameOver) {
            return;
        }
        // when reset the game, should I delete all the prior game states?
        gameStates.clear();
        gameBoard = new int[rows][columns];
        gameOver = false;
        addRandom2Or4();
        addRandom2Or4();
        gameStates.add(gameBoard);
    }

    public void undoMove() {
        if (gameStates.size() > 1) {
            gameStates.remove(gameStates.size() - 1);
            gameBoard = gameStates.get(gameStates.size() - 1);
        }
    }
}
