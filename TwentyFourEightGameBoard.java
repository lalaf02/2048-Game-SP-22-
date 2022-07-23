package org.cis120.twentyfoureight;

/*
 * CIS 120 HW09 - 2048
 * (c) University of Pennsylvania
 * Created by Lauryn Fuld
 */

import java.awt.*;
import java.awt.event.*;
import java.util.TreeMap;
import javax.swing.*;
import java.util.*;
import java.io.*;

/**
 * This class instantiates a TwentyFourEightModel object, which
 * is the model for the game. As the user clicks the game board,
 * the model is updated. Whenever the model is updated, the game
 * board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 *
 * In a Model-View-Controller framework, TwentyFourEightGameBoard
 * stores the model as a field and acts as both the controller
 * (with a MouseListener & keyListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class TwentyFourEightGameBoard extends JPanel {

    private TwentyFourEightModel twentyFourEightModel; // model for the game
    private JLabel currScoreStatus; // current status text
    private JLabel highestGameScoreStatus; // current highest game score

    // Game constants
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    /**
     * Initializes the game board.
     */
    public TwentyFourEightGameBoard(JLabel statusInit, JLabel highScore) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        twentyFourEightModel = new TwentyFourEightModel(); // initializes model for the game
        currScoreStatus = statusInit;
        highestGameScoreStatus = highScore;

        /*
         * Listens for keys being pressed. Updates the model, then updates the game
         * board based off of the updated model. If the game is over, display a pop-up
         * to the screen indicating if the game was over
         */
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    twentyFourEightModel.move("up");
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    twentyFourEightModel.move("down");
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    twentyFourEightModel.move("left");
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    twentyFourEightModel.move("right");
                }

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });
    }

    public void undoLastMove() {
        this.twentyFourEightModel.undoMove();
        currScoreStatus.setText("Current Score: " + twentyFourEightModel.getGameScore());
        repaint();
        requestFocusInWindow();
    }

    public TwentyFourEightModel getTwentyFourEightModel() {
        return twentyFourEightModel;
    }

    /**
     * NEED TO IMPLEMENT
     */
    public void reset() {
        this.twentyFourEightModel.reset2048();
        currScoreStatus.setText(twentyFourEightModel.getGameScore() + "");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Need a save method to save the current board to a file
     */
    public String saveGameToFile() {
        String fName = "";
        Random randomNumber = new Random();
        int randomFileNum = randomNumber.nextInt(Integer.MAX_VALUE);

        File f = new File("2048 Game_" + randomFileNum);
        boolean result;
        try {
            result = f.createNewFile();
            if (result) {
                fName = "2048 Game_" + randomFileNum;
                // NEED TO ADD THE CONTENTS TO THE FILE
                try {
                    FileWriter myWriter = new FileWriter(f.getName());
                    for (int r = 0; r < 4; r++) {
                        for (int c = 0; c < 4; c++) {
                            myWriter.write(twentyFourEightModel.getCellValue(r, c) + "\n");
                        }
                    }
                    myWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fName.equals("")) {
            fName = "Error occurred: file could not be saved";
        }
        return fName;
    }

    /**
     * Updates the JLabels (current score and highest score) to reflect
     * the current state of the game. -> update the current highest game score
     */
    private void updateStatus() {
        // check to see if we need to update the high score
        TwentyFourEightModel twentyFourEight = null;
        if (twentyFourEightModel.getGameScore() > twentyFourEightModel.getBestScore()) {
            twentyFourEightModel.setBestScore(twentyFourEightModel.getGameScore());
            highestGameScoreStatus
                    .setText("High Score: " + twentyFourEightModel.getBestScore() + "");
        }
        // update the current game score, if game is over, current game score will be
        // set to game is over
        if (!twentyFourEightModel.getGameIsOver()) {
            currScoreStatus.setText("Current Score: " + twentyFourEightModel.getGameScore() + "");
        } else {
            currScoreStatus.setText("Game Over - Click the reset button to play again");
        }
    }

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid -> the border is not drawn in this state
        g.drawLine(100, 0, 100, 400);
        g.drawLine(200, 0, 200, 400);
        g.drawLine(300, 0, 300, 400);
        g.drawLine(0, 100, 400, 100);
        g.drawLine(0, 200, 400, 200);
        g.drawLine(0, 300, 400, 300);

        // creates a map that assigns the row and col values
        // to the correct location in the graphics context
        Map<Integer, Integer> rowToGctxConversion = new TreeMap<>();
        rowToGctxConversion.put(0, 50);
        rowToGctxConversion.put(1, 150);
        rowToGctxConversion.put(2, 250);
        rowToGctxConversion.put(3, 350);
        Map<Integer, Integer> colToGctxConversion = new TreeMap<>();
        colToGctxConversion.put(0, 50);
        colToGctxConversion.put(1, 150);
        colToGctxConversion.put(2, 250);
        colToGctxConversion.put(3, 350);

        // Draws values into the correct grid location
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int[][] gameBoard = twentyFourEightModel.getGameBoard();
                String value = gameBoard[i][j] + "";
                if (value.equals("0")) {
                    value = "";
                }
                int xCord = colToGctxConversion.get(j);
                int yCord = rowToGctxConversion.get(i);
                g.drawString(value, xCord, yCord);
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
