/*
 * TCSS 305
 * Assignment 6 - Tetris
 */
package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Board;
import model.Point;
import model.TetrisPiece;

/**
 * Panel where the game is played.
 * 
 * @author Shannon Murphy
 * @version 3 March 2015
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Observer {

    /** Default size for the game panel. */
    private static final Dimension GAME_PANEL_SIZE = new Dimension(401, 440);
    
    /** Default size for the east panel. */
    private static final Dimension EAST_PANEL_SIZE = new Dimension(210, 420);
    
    /** The default spacing between the east panel components. */
    private static final int PADDING = 60;
    
    /** The size for the blocks of the tetris pieces. */
    private static final int BLOCK_SIZE = 20;

    /** The location for the "Game Over" text to be displayed. */    
    private static final Point GAME_OVER_LOCATION = new Point(40, 150);
    
    /** The size of the black box surrounding the "Game Over" text. */
    private static final Point GAME_OVER_BOX = new Point(200, 50);
    
    /** The default font to be used throughout the program. */
    private static final Font DEFAULT_FONT = new Font("Georgia", Font.BOLD, 20);
    
    // instance fields
    
    /** The Board for the game. */
    private final Board myBoard;
    
    /** The Panel for the next piece preview. */
    private final PreviewPanel myPreviewPanel;
    
    /** The panel that displays the score. */
    private final ScorePanel myScorePanel;
    
    /** The timer that controls the animation. */
    private final Timer myTimer;
    
    /** A list of the Board data. */
    private List<Color[]> myBoardData;
    
    /** X-value for drawing the Tetris blocks. */
    private int myX;
    
    /** A flag to restart the game. */
    private boolean myRestart;
    
    /** A flag to change the Panel when the game is over. */
    private boolean myGameOver;
    
    /** Sets the cat mode feature of this game. */
    private boolean myCatMode;
    
    /** Sets the panel to draw a new screen for a new game. */
    private boolean myNewScreen;
    
    /** Used as a flag to draw grid. */
    private boolean myGridStatus;
    
    /**
     * Constructs a new panel for the game.
     * 
     * @param theBoard the Board used for the game. 
     * @param theTimer the Timer that controls the game.
     */
    public GamePanel(final Board theBoard, final Timer theTimer) {
        super(new BorderLayout());
        myPreviewPanel = new PreviewPanel();
        myScorePanel = new ScorePanel(theTimer, DEFAULT_FONT);
        myTimer = theTimer;
        myBoard = theBoard;
        setUpGame();
        myX = 0;
    }
    
    /** Helper method for the constructor to set up the panel. */
    private void setUpGame() {
        myGridStatus = false;
        myGameOver = false;
        myRestart = false;
        myCatMode = false;
        myNewScreen = false;
        setPreferredSize(GAME_PANEL_SIZE);
        setBackground(Color.WHITE);
        setUpBoard();
        setUpEast();
        this.addPropertyChangeListener(myScorePanel);
    }
    
    /** Sets up the East Panel. */
    private void setUpEast() {
        final JPanel eastPanel = new JPanel();
        eastPanel.setOpaque(false);
        eastPanel.setPreferredSize(EAST_PANEL_SIZE);
        eastPanel.setBorder(BorderFactory.createMatteBorder(
                                                  0, 2, 0, 0, Color.BLACK));
        final Box eastBox = new Box(BoxLayout.PAGE_AXIS);
        eastBox.add(myPreviewPanel);
        eastBox.add(Box.createVerticalStrut(PADDING));
        eastBox.add(Box.createVerticalStrut(PADDING));
        eastBox.add(myScorePanel);
        eastPanel.add(eastBox);
        add(eastPanel, BorderLayout.EAST);
    }
    
    /** Sets up the Board for the game. */
    private void setUpBoard() {
        myBoard.addObserver(this);
        myBoardData = new ArrayList<Color[]>();
        myBoard.clear();
    }
    
    /** 
     * Sets the ScorePanel to either mute/unmute its sound effects.
     *  
     * @param theStatus true if muted, false otherwise.
     */
    public void setMute(final boolean theStatus) {
        myScorePanel.setMute(theStatus);
    }
    
    /** 
     * Sets the cat mode feature to set the background to cats.
     * 
     * @param theStatus true if cat mode is on, false otherwise.
     */
    public void setCatMode(final boolean theStatus) {
        myCatMode = theStatus;
    }
    
    /**
     * Used to set a flag to draw a blank screen. 
     * 
     * @param theStatus true if the game was restarted, false otherwise.
     */
    public void setRestart(final boolean theStatus) {
        myRestart = theStatus;
        if (myRestart) {
            myScorePanel.resetStats();
            myPreviewPanel.setGameStatus(true);
        }
    }
    
    /**
     * Used to set a flag to draw a grid.
     * 
     * @param theStatus true if grid is selected, false otherwise.
     */
    public void setGrid(final boolean theStatus) {
        myGridStatus = theStatus;
    }
    
    /** 
     * Used to set a flag to draw a blank screen when the game is first opened.
     * 
     * @param theStatus true if a game hasn't been played yet, false otherwise.
     */
    public void drawNewScreen(final boolean theStatus) {
        myNewScreen = theStatus;
    }
    
    /** 
     * Draws Tetris pieces on the panel.
     * Draws a game over screen if the game is over.
     * Also paints a new screen if the game is restarted.
     */
    /** {@inheritDoc} */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        if (myCatMode) {
            final ImageIcon icon = new ImageIcon("./images//cats.jpg");
            final Image cats = icon.getImage();
            g2d.drawImage(cats, 0, 0, null);
        } 
        drawPieces(g2d);
        if (myGameOver) {
            drawGameOver(g2d);
        }
        if (myRestart || myNewScreen) {
            drawRestart(g2d);
        }
        
        if (myGridStatus) {
            drawGrid(g2d);
        }
    }
    
    /** 
     * Draws the tetris pieces on the board.
     * 
     * @param theG2d theGraphics2D object to draw with.
     */
    private void drawPieces(final Graphics2D theG2d) {
        for (int i = myBoardData.size() - 1; i >= 0; i--) {
            final Color[] row = myBoardData.get(i);
            int index = 0;
            for (final Color c : row) {
                if (c != null) {
                    myX = index;
                    theG2d.setColor(Color.BLACK);
                    theG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                        RenderingHints.VALUE_ANTIALIAS_ON);
                    final RoundRectangle2D.Double rect = 
                                    new RoundRectangle2D.Double(myX * BLOCK_SIZE, 
                                            ((BLOCK_SIZE - i) + 1) * BLOCK_SIZE, 
                                            BLOCK_SIZE, BLOCK_SIZE, 8, 8);
                    theG2d.setStroke(new BasicStroke(2));
                    theG2d.draw(rect);
                    theG2d.setColor(c);
                    theG2d.fill(rect);
                }
                index++;
            }
        }
    }
    
    /** 
     * Draws the "Game Over" screen. 
     * 
     * @param theG2d the Graphics2D to draw with.
     */
    private void drawGameOver(final Graphics2D theG2d) {
        myTimer.stop();
        theG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        theG2d.setColor(Color.BLACK);
        theG2d.fill(new Rectangle2D.Double(0, GAME_OVER_LOCATION.y() - (PADDING / 2),
                                           GAME_OVER_BOX.x(), GAME_OVER_BOX.y()));
        theG2d.setColor(Color.WHITE);           
        theG2d.setFont(DEFAULT_FONT);
        theG2d.drawString("Game over!", GAME_OVER_LOCATION.x(), GAME_OVER_LOCATION.y());
        myGameOver = false;
    }
    
    /** 
     * Draws a blank screen to restart the game. 
     * 
     * @param theG2d the Graphics2D to draw with.
     */
    private void drawRestart(final Graphics2D theG2d) {
        theG2d.setColor(Color.WHITE);
        theG2d.fill(new Rectangle2D.Double(0, 0, (int) GAME_PANEL_SIZE.getWidth(),
                                        (int) GAME_PANEL_SIZE.getHeight() + BLOCK_SIZE));
        myRestart = false;
        myTimer.restart();
    }

    /** 
     * Draws a grid.
     * 
     * @param theG2d the Graphics2D to draw with.
     */
    private void drawGrid(final Graphics2D theG2d) {
        theG2d.setColor(Color.GRAY);
        theG2d.setStroke(new BasicStroke(1));
        /** draws the vertical lines */
        for (int i = 0; i <= getSize().width / 2; i += BLOCK_SIZE) {
            theG2d.drawLine(i, 0, i, getSize().height);
        }
        
        /** draws the horizontal lines */
        for (int i = 0; i <= getSize().height; i += BLOCK_SIZE) {
            theG2d.drawLine(0, i, getSize().width / 2, i);
        }
    }
    
    /** Receives updates from the Board. */
    /** {@inheritDoc} */
    @Override
    public void update(final Observable theObservable, final Object theArg) {
        if (theObservable instanceof Board) {
            if (theArg.getClass().equals(Board.BoardData.class)) {
                myBoardData = ((Board.BoardData) theArg).getBoardData();
            } else if (theArg.getClass().equals(TetrisPiece.class)) {
                final TetrisPiece nextPiece = (TetrisPiece) theArg;
                myPreviewPanel.setNextPiecePoints(nextPiece);
                myPreviewPanel.setColor(nextPiece.getColor());
                firePropertyChange("piece landed", null, null);
            } else if (theArg.getClass().equals(Board.GameStatus.class)) {
                myGameOver = true;
                firePropertyChange("game over", null, null);
            } else if (theArg.getClass().equals(Board.CompletedLines.class)) {
                final int lines = ((Board.CompletedLines) theArg).getCompletedLines().size();
                firePropertyChange("lines", null, lines);
            }
            repaint();
        }
    } 
}
