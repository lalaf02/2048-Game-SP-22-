package org.cis120.twentyfoureight;

/*
 * CIS 120 HW09 - 2048
 * (c) University of Pennsylvania
 * Created by Lauryn Fuld
 */

import java.awt.*;
import javax.swing.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 *
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games.
 *
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a TwentyFourEightGameBoard. The
 * TwentyFourEightGameBoard will handle the rest of the game's
 * view and controller functionality, and it will instantiate a
 * TwentyFourEight object to serve as the game's model.
 */
public class RunTwentyFourEightView implements Runnable {
    private void saveGameHelper(TwentyFourEightGameBoard board, JFrame jF) {
        JTextField inputField = new JTextField(
                "Enter the name you the file you want to be save to"
        );
        String proposedFileName = inputField.getText();
        String fName = board.saveGameToFile();
        JOptionPane.showMessageDialog(jF, "File Name: " + fName);
        // JLabel filename = new JLabel(fName);
        jF.dispose();
    }

    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("2048");
        frame.setLocation(400, 400);
        JOptionPane.showMessageDialog(
                frame, "The game that I implemented is 2048. \n" +
                        "\n" +
                        "To play, use the up, down left and right arrows, corresponding to up, " +
                        "down, left and right shifts, \n"
                        +
                        "respectively, of the game board. When the game is over " +
                        "(meaning the board is full & no more shifts \n"
                        +
                        "are possible), a message will appear at the top of the frame, " +
                        "telling the user to click the reset \n"
                        +
                        "button at the lower right hand corner of the screen. \n" +
                        "\n" +
                        "To save the game to a file, click the save button (located " +
                        "on the lower portion of the screen),\n"
                        +
                        "and upon clicking this button, the application will close " +
                        "immediately. To restart to the current \n"
                        +
                        "game, create a RunTwentyFourEightView object in the main " +
                        "method of the Game class, passing the in \n"
                        +
                        "the filename of the saved game as the argument."
        );

        // Status panel at the top of the screen -> shows the current score and the
        // highest score
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.NORTH);
        final JLabel currScoreStatus = new JLabel();
        final JLabel highestScore = new JLabel(
                "High Score: "
        );
        status_panel.add(currScoreStatus);
        status_panel.add(highestScore);

        // Game board -> displayed in the middle of the screen
        final TwentyFourEightGameBoard board = new TwentyFourEightGameBoard(
                currScoreStatus, highestScore
        );
        frame.add(board, BorderLayout.CENTER);

        // panel at the bottom of the screen, having 3 buttons
        // one button to save the game
        // one button to reset the game
        JPanel controlPanel = new JPanel();
        frame.add(controlPanel, BorderLayout.SOUTH);
        final JButton undoButton = new JButton("Undo");
        controlPanel.add(undoButton);
        undoButton.addActionListener((e -> board.undoLastMove()));

        final JButton saveButton = new JButton("Save Game");
        controlPanel.add(saveButton);
        saveButton.addActionListener((e -> saveGameHelper(board, frame)));
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset()); // what is board in this context?
        controlPanel.add(reset);

        // Put the frame on the screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
