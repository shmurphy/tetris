/*
 * TCSS 305
 * Assignment 6 - Tetris
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import sound.SoundPlayer;

/**
 * Panel where the score and other statistics are displayed.
 * 
 * @author Shannon Murphy
 * @version 12 March 2015
 */            
@SuppressWarnings("serial")
public class ScorePanel extends JPanel implements PropertyChangeListener {
    
    /** The default font to be used throughout the program. */
    private static final Font DEFAULT_FONT = new Font("Georgia", Font.BOLD, 15);
    
    /** Default size for this panel. */
    private static final Dimension DEFAULT_SIZE = new Dimension(200, 150);
    
    /** The amount to increase the line target by after each new level. */
    private static final int LINE_TARGET = 5;
    
    /** The amount the timer's delay decreases by with each level. */
    private static final int TIMER_DECREASE = 200;
    
    /** The amount the text is indented in the game stats panel. */
    private static final int TEXT_INDENT = 10;
    
    /** The amount of spacing in between each line of text. */
    private static final int TEXT_SPACING = 30;
    
    /** The initial x value for first line of text. */
    private static final int FIRST_LINE = 45;
    
    /** Array of point values for each level, ordered respectively. */
    private static final int[] POINT_VALUES = {40, 100, 300, 1200};
    
    // instance fields
    
    /** The number of lines cleared. */
    private int myClearedLines;
    
    /** The level the game is on. */
    private int myLevel;
    
    /** The timer controlling the game. */
    private final Timer myTimer;
    
    /** The target for lines that need to be cleared for a new level. */
    private int myLineTarget;
    
    /** The score. */
    private int myScore;
    
    /** Used to track whether the game is muted or not. */
    private boolean myMuteStatus;
    
    /** The font to be used for thet title. */
    private final Font myTitleFont;
    
    /**
     * Constructs a new Score Panel.
     * 
     * @param theTimer the timer that controls the animation.
     * @param theFont the font used for the titles.
     */
    public ScorePanel(final Timer theTimer, final Font theFont) {
        super();
        myTimer = theTimer;
        myTitleFont = theFont;
        myClearedLines = 0;
        myScore = 0;
        myLevel = 1;
        myLineTarget = LINE_TARGET;
        myMuteStatus = false;
        setUpPanel();
    }
    
    /** Creates the border for this panel. */
    private void setUpPanel() {
        setPreferredSize(DEFAULT_SIZE);
        setBackground(Color.WHITE);
        TitledBorder title;
        title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder
            (Color.BLACK, 2), "Game Stats:");
        title.setTitleJustification(TitledBorder.CENTER);
        title.setTitleFont(myTitleFont);
        setBorder(title);
    }
    
    /** Resets all of the game statistics for a new game. */
    public void resetStats() {
        myClearedLines = 0;
        myLevel = 1;
        myLineTarget = LINE_TARGET;
        myScore = 0;
    }
    
    /** 
     * Sets the ScorePanel to mute its sound effects.
     * 
     * @param theStatus true if they are muted, false otherwise.
     */
    public void setMute(final boolean theStatus) {
        myMuteStatus = theStatus;
    }
    
    @Override
    /** Draws the various game statistics on the panel. */
    /** {@inheritDoc} */
    public void paintComponent(final Graphics theGraphics) {
        int textX = FIRST_LINE; // increases by 30 for each new line
        
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(DEFAULT_FONT);
        g2d.drawString("Lines cleared: " + myClearedLines, TEXT_INDENT, textX);
        textX += TEXT_SPACING;
        g2d.drawString("Level: " + myLevel, TEXT_INDENT, textX);
        textX += TEXT_SPACING;
        g2d.drawString("Lines until next level: " + (myLineTarget - myClearedLines), 
                       TEXT_INDENT, textX);
        textX += TEXT_SPACING;
        g2d.drawString("Score: " + myScore, TEXT_INDENT, textX);
    }

    @Override
    /** 
     * Increases the amount of lines cleared and increases the level whenever
     * the amount of lines cleared is greater than the target.
     */
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final SoundPlayer s = new SoundPlayer();
        if ("lines".equals(theEvent.getPropertyName())) {
            if (!myMuteStatus) {
                s.play("./sounds//applause_y.wav");
            }
            myClearedLines += (int) theEvent.getNewValue();
            if (myClearedLines >= myLineTarget) {
                myLevel++;
                myLineTarget += LINE_TARGET;
                myTimer.setDelay(myTimer.getDelay() - TIMER_DECREASE);
            } 
            calculateScore((int) theEvent.getNewValue());
        } else if ("piece landed".equals(theEvent.getPropertyName())) {
            myScore += myLevel * LINE_TARGET;
        }
    }
    
    /**
     * Calculates the score based on how many lines were cleared. 
     * 
     * @param theLines the number of lines that were cleared.
     */
    private void calculateScore(final int theLines) {
        myScore += POINT_VALUES[theLines - 1] * myLevel;
    }
    
    
}
