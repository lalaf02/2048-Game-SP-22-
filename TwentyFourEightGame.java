package org.cis120;

import org.cis120.twentyfoureight.RunTwentyFourEightView;

import javax.swing.*;

public class TwentyFourEightGame {
    /**
     * Main method run to start and run the game. Initializes the runnable game
     * class of your choosing and runs it. IMPORTANT: Do NOT delete! You MUST
     * include a main method in your final submission.
     */
    public static void main(String[] args) {
        Runnable game = new RunTwentyFourEightView();
        // here
        SwingUtilities.invokeLater(game);
    }
}
