/*
 * TCSS 305
 * Assignment 6 - Tetris
 */

package gui;

import java.awt.EventQueue;

/**
 * Runs Tetris by instantiating and starting the TetrisGUI.
 * 
 * @author Shannon Murphy
 * @version 27 February 2015
 */
public final class TetrisMain {

    /**
     * Private constructor, to prevent instantiation of this class.
     */
    private TetrisMain() {
        throw new IllegalStateException();
    }

    /**
     * The main method, invokes the Tetris GUI. Command line arguments are
     * ignored.
     * 
     * @param theArgs Command line arguments.
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final TetrisGUI gui = new TetrisGUI();
                gui.start();
            }
        });
    }
}
