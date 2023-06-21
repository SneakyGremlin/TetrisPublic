package classes;

/**
 * Classes should be updated in tandem with constants here. Classes should be updated if any new gameObjects (ref. Extensibility in README.md are added, (especially BlockCluster; look for dependencies for specificity).
 *
 * >>> update the constants always following good form; follow discipline and patience for modifications; follow correspondence between variables (the dimensions).
 */
public class GameConstants {

    /**
     * Important static constants used throughout the program (specifically for the underlying computation and not the graphics)
     *
     * The only reason the "derived" attributes aren't in the form of an equation is because INTELLIJ was being persnickety.
     * PLEASE Update them accordingly.
     * Attributes:
     *          MINIMUM_X is the minimum x coordinate (n)
     *          MAXIMUM_X is the maximum x coordinate (s)
     *          MINIMUM_Y is the minimum y coordinate (w)
     *          MAXIMUM_X is the maximum x coordinate (e)
     *          COLUMNS = MAXIMUM_X + 1
     *          COLUMNS = MAXIMUM_Y + 1
     *          INITIAL_Y_COORDINATE is the "spawning point" for game objects if you will.
     *          MAX_DIMENSION_OF_BLOCK_CLUSTER = 3 since it's a 3x3 grid.
                SCORE: Very rudimentary implementation; updated in classes.gameSpace.Container class; utilised in the gui for displaying,
     *          GAME_OVER = signals for ending the game. keyword volatile is used owing to compiler optimisation. IDK if some actually reads this but finding that BUG WAS UGHHHHHHHHHHHHHHHHHHHH. Hypothesis: each thread has its own stack; possibility to do with Swing's Event Dispatch Thread preloading the value of GAME_OVER and compiler optimisations?
     */

    public final static int MINIMUM_X = 0;
    public final static int MAXIMUM_X = 45; // 50
    public final static int MINIMUM_Y = 0;
    public final static int MAXIMUM_Y = 45; //70
    public final static int COLUMNS = 46; //51
    public final static int ROWS = 46; // 71
    /**
     * EXPLANATION: a block cluster emerges with each row separate (the emergence is row by row).
     */
    public final static int INITIAL_Y_COORDINATE = -3;

    /**
     * Since all BlockClusters are 3x3.
     */
    public final static int MAX_DIMENSION_OF_BLOCK_CLUSTER = 3;

    /**
     * Counts the number of rows cleared.
     */
    public static int SCORE = 0;

    public volatile static boolean  GAME_OVER = false;
}