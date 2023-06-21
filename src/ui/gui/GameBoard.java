package ui.gui;

import classes.GameConstants;
import classes.gameSpace.Container;
import ui.managers.PossibleUserInput;
import ui.managers.Updater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * GameBoard Class is the visual depiction of this Tetris implementation i.e. its graphical counterpart
 *
 * Its duties do not entail the management of the game state (however they do entail appropriate invocation of management
 *      methods (using class managers.Update which ensures computation)
 *
 * Its duties entail appropriate invocations (as mentioned above) and appropriate updating of the JFrame(s):
 *      There are three:
 *      One is the welcome Frame
 *      Second is the main Frame wherein the game transpires
 *      Third is the termination Frame where we bid you adieu.
 *
 */
public class GameBoard {

    /** Attributes are expounded on unorthodoxly */

    /**
     *  For the implementation of the pause functionality. Press space.
     */
    private boolean pause = false;

    /**
     * Inner timer for the game cycle.
     */
    private Timer timer;

    /**
     * The welcome frame, frame 1; its members are temporary.
     */
    private JFrame welcomeFrame;

    /**
     * mainFrame has two panels upper, gridPanel (with grid) and lower panel, panelForScore.
     * lower panel has flow layout; scoreDisplay area is a part thereof.
     *
     * grid refers to the matrix of GraphicCells.
     *
     */
    private JFrame mainFrame;
    private JPanel gridPanel; /** gridPanel and grid are closely associated. in essence each element of the grid is tied to a portion of the gridPanel */
    private ArrayList<ArrayList<GraphicCell>> grid;
    private JPanel panelForScore;
    JTextArea scoreDisplayArea;

    /**
     * The hasta-la-vista frame. Its components are temporary.
     */
    private  JFrame terminationFrame;


    /**
     * Constructor.
     */
    public GameBoard() {
         // first have the initialFrame for welcoming them. Ask them to click on a button to proceed.
        initialiseWelcomingFrame();
    }


    /**
     * Frame for welcoming the user. No code in particular requiring exposition
     * All locals are kept local since its meretricious.
     *
     * Notable code is the action listener: it invokes the initialiseMainFrame().
     */
    public void initialiseWelcomingFrame() {
        int frameDimension = 500;

        welcomeFrame = new JFrame("Welcome to Tetris");
        welcomeFrame.setSize(new Dimension(frameDimension, frameDimension));

        welcomeFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    welcomeFrame.dispose();
                    initialiseMainFrame();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    welcomeFrame.dispose();
                    initialiseMainFrame();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        JPanel welcomePanel = new JPanel(new BorderLayout(5, 5));
        welcomePanel.setBackground(Color.black);

        JLabel welcomeLabel = new JLabel("  Welcome to Tetris! Click the Button Below to Begin. ");
        welcomeLabel.setFont(new Font("Apple LiGothic", Font.ITALIC, 20)); // >>> Syntax
        welcomeLabel.setForeground(Color.white);

        JButton welcomeButton = new JButton("CLICK ME!");
        welcomeButton.setBackground(Color.orange);
        welcomeButton.setFocusable(false);
        welcomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomeFrame.dispose();
                initialiseMainFrame();
            }
        });
        welcomeButton.setFont(new Font("Sans Serif", Font.BOLD, 50));

        JLabel welcomeLabelT = new JLabel(" T");
        welcomeLabelT.setForeground(Color.white);
        welcomeLabelT.setFont(new Font("Apple LiGothic", Font.BOLD, 400));

        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        welcomePanel.add(welcomeLabelT);
        welcomePanel.add(welcomeButton, BorderLayout.SOUTH);

        welcomeFrame.add(welcomePanel);

        welcomeFrame.setResizable(false);
        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        welcomeFrame.setVisible(true);
    }

    /**
     * Frame where all the substantial graphics portions is handled. This method INITIALISES the frame:
     *          it populates the grid and forms the association with the gridPanel (which has gridLayout).
     *
     *          Note the invocations at the bottom: initialiseGameCycle() and beginGame(). These are salient.
     */
    private void initialiseMainFrame() {
        mainFrame = new JFrame("Tetris");
        //mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // >>> Syntax

        // rows and columns are determined by the Constants;
        gridPanel = new JPanel(new GridLayout(GameConstants.ROWS, GameConstants.COLUMNS, 3, 3));
        gridPanel.setBackground(Color.gray);

        grid = new ArrayList<>();
        Container.getInstance().populateGraphicGrid(grid);

        for (int r = GameConstants.MINIMUM_Y; r <= GameConstants.MAXIMUM_Y; r++) {
            for (int c = GameConstants.MINIMUM_X; c <= GameConstants.MAXIMUM_X; c++) {
                gridPanel.add(grid.get(r).get(c));
            }
        }

        mainFrame.add(gridPanel);

        panelForScore = new JPanel();

        scoreDisplayArea = new JTextArea("SCORE: " + GameConstants.SCORE);
        scoreDisplayArea.setFont(new Font("Sans Serif", Font.BOLD, 30));
        scoreDisplayArea.setBackground(Color.gray);
        scoreDisplayArea.setForeground(Color.white);
        scoreDisplayArea.setEditable(false);
        scoreDisplayArea.setFocusable(false);

        panelForScore.setBackground(Color.gray);
        panelForScore.add(scoreDisplayArea);

        mainFrame.add(panelForScore, BorderLayout.SOUTH);

        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

        initialiseGameCycle();

        beginGame();
    }


    /**
     * As the name suggests: is responsible for the initialisation of all the action listeners (mainly the KeyListener for
     *              the mainFrame) and for initiating the timer.
     *
     *
     * For a coherent exposition on the game cycle kindly refer to the README file.
     *
     */
    public void initialiseGameCycle() {
        mainFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!pause) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_A -> {
                            Updater.getInstance().updateGeneral(PossibleUserInput.LEFT);
                        }
                        case KeyEvent.VK_D -> {
                            Updater.getInstance().updateGeneral(PossibleUserInput.RIGHT);
                        }
                        case KeyEvent.VK_S -> {
                            Updater.getInstance().updateGeneral(PossibleUserInput.DOWN);
                        }
                        case KeyEvent.VK_Q -> {
                            Updater.getInstance().updateGeneral(PossibleUserInput.ROTATE_LEFT);
                        }
                        case KeyEvent.VK_E -> {
                            Updater.getInstance().updateGeneral(PossibleUserInput.ROTATE_RIGHT);
                        }
                    }
                    Container.getInstance().amalgamateGraphics();
                    updateDisplayGrid();
                    Container.getInstance().unamalgamateGraphics();

                    checkForTermination();
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE -> {
                        terminate();
                    }
                    case KeyEvent.VK_SPACE -> {
                        if (pause) {
                            pause = false;
                            timer.start();

                        } else {
                            pause = true;
                            timer.stop();
                        }
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (!pause) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_A -> {
                            Updater.getInstance().updateGeneral(PossibleUserInput.LEFT);
                        }
                        case KeyEvent.VK_D -> {
                            Updater.getInstance().updateGeneral(PossibleUserInput.RIGHT);
                        }
                        case KeyEvent.VK_S -> {
                            Updater.getInstance().updateGeneral(PossibleUserInput.DOWN);
                        }
                        case KeyEvent.VK_Q -> {
                            Updater.getInstance().updateGeneral(PossibleUserInput.ROTATE_LEFT);
                        }
                        case KeyEvent.VK_E -> {
                            Updater.getInstance().updateGeneral(PossibleUserInput.ROTATE_RIGHT);
                        }
                    }
                    /**
                     * This is a delicate spot. Please refer to the documentation of amalgamateGraphics() and revise the notion of a floating object.
                     */
                    Container.getInstance().amalgamateGraphics();
                    updateDisplayGrid();
                    Container.getInstance().unamalgamateGraphics();

                    checkForTermination();
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE -> {
                        terminate();
                    }
                    case KeyEvent.VK_SPACE -> {
                        if (pause) {
                            pause = false;
                            timer.start();

                        } else {
                            pause = true;
                            timer.stop();
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        timer = new Timer(GraphicsConstants.DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Updater.getInstance().updateDefault();

                Container.getInstance().amalgamateGraphics();
                updateDisplayGrid();
                Container.getInstance().unamalgamateGraphics();

                updateScoreDisplay();

                checkForTermination();
            }
        });

    }

    /**
     * Checks if the Container instance set the gameOver flag as after the invocation of hasSpaceDown() by the two update() methods.
     * If it is set: the termination frame is instantiated.
     */
    private void checkForTermination() {
        if (GameConstants.GAME_OVER) {
            terminate();
        }
    }

    /**
     * Updates the scoreDisplay.
     */
    private void updateScoreDisplay() {
        scoreDisplayArea.setText("SCORE: " + GameConstants.SCORE);
    }

    /**
     * CAVEAT: The game actually begins the moment the mainFrame has been instantiated, but the blocks start
     *      cascading after the timer.
     */
    private void beginGame() {
        timer.start();
    }

    /**
     * Invokes the update method on all the GraphicCells which results in the grids color corresponding to the underlying
     * data (remember GraphicCells store a reference to a Block (which can be null) Refer to constructor.
     */
    public void updateDisplayGrid() {
        for (ArrayList<GraphicCell> row: grid) {
            for (GraphicCell graphicCell: row) {
                graphicCell.update();
            }
        }
    }

    /**
     * Termination routine. Simply displays the Score and a goodbye message.
     */
    public void terminate() {
        mainFrame.dispose();
        timer.stop();

        terminationFrame = new JFrame("Tetris");
        // terminationFrame.setLocationRelativeTo(null);
        terminationFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Window constants

        JPanel terminationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 50));
        terminationPanel.setBackground(Color.white);

        JLabel terminationText = new JLabel("Game Over! SCORE: " +  String.valueOf(GameConstants.SCORE)); // could use html tags
        terminationText.setFont(new Font("Sans Serif", Font.BOLD, 30));
        terminationText.setForeground(Color.black);
        terminationText.setBackground(Color.white);


        terminationPanel.add(terminationText);

        terminationFrame.add(terminationPanel);
        terminationFrame.pack();
        terminationFrame.setVisible(true);
    }
}