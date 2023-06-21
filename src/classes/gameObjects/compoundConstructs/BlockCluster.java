package classes.gameObjects.compoundConstructs;

import classes.GameConstants;
import classes.gameObjects.Block;

import java.util.ArrayList;

import static classes.gameObjects.compoundConstructs.Orientations.N;

/**
 * BlockClusters assume basic forms and assume one of four orientations (refer to Enumeration Orientations)
 * Forms (also refer to enumeration Forms_:
 * ...  .  ...   ...    ...   ...   ...   ...   ...     ...    ...
 *  .              .     ..   . .   .       .   ...     . .    . .
 *                                          .   ...       .    . .
 *
 *  They contain Blocks.
 *  They consist of a single 9 element array where each element isa block (which can be null (since null can be passed as a parameter where a Block is expected)
 *
 *  Attributes:
 *  blocks: this is the ONE-DIMENSIONAL array of blocks (up to nine) which belong to a block cluster; array occupancy and indices determine the type and orientation of the block).
 *  form: (refer to enumeration Forms). Note that the order is correspondent i.e. the 0th depiction refers to F0 and so on (in all truth this is a bit of refactoring that needs to be done... TODO)
 *  orientation: (refer to enumeration Orientations). Block Clusters have orientations merely so mitigate against repeated forms.
 *
 *  NOTE: any and all "update" methods i.e. methods that enforce a change in the locale of the blocks of a block cluster
 *          REQUIRE a call to each specific cell's respective method. Furthermore, since a block CAN be null, there are methods
 *          implemented (getBlocksToCheck()...) and other null checks that pre-empt the nullPointerException.
 *
 *  NOTE: The FactoryDesignPattern was not implemented because things should be made as simple as possible but not simpler:
 *          there in no reason whatsover to have different blockClusters be of different classes.
 */
public class BlockCluster {
    protected ArrayList<Block> blocks;
    protected Forms form; // [0, 10] inclusive
    protected Orientations orientation;

    /**
     * xCoord and yCoord are computed based on the position of the 0th blocks element (even if it is null).
     * May not be used at all since blocks themselves have coordinates.
     */
    protected int xCoord;
    protected int yCoord;

    /**
     *
     * @param x is the x coordinate of the 0th element of the grid i.e. the top left in 3x3; CAN refer to null
     *                 is used to determine the position of the created blocks. Is provided by the calling body. Uses a random number generator.
     *                 CONSTRAINT: x E [0, BOARD_WIDTH - MAX_DIMENSION_OF_BLOCK_CLUSTER]
     * @param o is the orientation of the selected block
     * @param f is the parameter which determines which block to create.
     *
     * NOTE: BlockCluster is invoked by class Generator which relies on the enumerations, Forms and Orienrations. There is
     *          coupling here: in order to introduce or remove an orientation or form one must update the enumerations AND
     *          must update the generateBlockCluster() method and the private members therein (the arrayLists9
     * NOTE: any operation on a BlockCluster requires invocation of the proper methods on the non-null blocks (e.g. if a
     *          block's orientation must be updated, the coordinates of the blocks contained must be updated as well via
     *          methods moveLeft()...
     *
     */
    public BlockCluster(int x, Orientations o, Forms f) {
        blocks = new ArrayList<>(9); // >>> TODO what does capacity do anyways
        for (int i = 0; i < 9; i++) {
            blocks.add(null);
        }
        switch(f) {
            case F0 -> {
                form = f;
                BlockClusterZero(x, o);
            }
            case F1 -> {
                form = f;
                BlockClusterOne(x, o);
            }
            case F2 -> {
                form = f;
                BlockClusterTwo(x, o);
            }
            case F3 -> {
                form = f;
                BlockClusterThree(x, o);
            }
            case F4 -> {
                form = f;
                BlockClusterFour(x, o);
            }
            case F5 -> {
                form = f;
                BlockClusterFive(x, o);
            }
            case F6 -> {
                form = f;
                BlockClusterSix(x, o);
            }
            case F7 -> {
                form = f;
                BlockClusterSeven(x, o);
            }
            case F8 -> {
                form = f;
                BlockClusterEight(x, o);
            }
            case F9 -> {
                form = f;
                BlockClusterNine(x, o);
            }
            case F10 -> {
                form = f;
                BlockClusterTen(x, o);
            }
        }
    }

    /**
     * ...
     *  .
     *
     */

    private void BlockClusterZero(int x, Orientations o) {
        blocks.set(0, new Block(x, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(1, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(2, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE));

        blocks.set(4, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE + 1));

        orientation = N;
        orient(o);
    }

    /**
     *  .
     */
    private void BlockClusterOne(int x, Orientations o) {
        blocks.set(4, new Block(x, GameConstants.INITIAL_Y_COORDINATE + 1));

        orientation = N;
        orient(o);
    }

    /**
     * ...
     *
     */
    private void BlockClusterTwo(int x, Orientations o) {
        blocks.set(3, new Block(x, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(4, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(5, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE));

        orientation = N;
        orient(o);
    }

    /**
     * ...
     *   .
     */

    private void BlockClusterThree(int x, Orientations o) {
        blocks.set(0, new Block(x, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(1, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(2, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE));

        blocks.set(5, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE + 1));

        orientation = N;
        orient(o);
    }

    /**
     * ...
     *  ..
     */
    private void BlockClusterFour(int x, Orientations o) {
        blocks.set(0, new Block(x, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(1, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(2, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE));


        blocks.set(4, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE + 1));
        blocks.set(5, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE + 1));

        orientation = N;
        orient(o);
    }

    /**
     * ...
     * . .
     */
    private void BlockClusterFive(int x, Orientations o) {
        blocks.set(0, new Block(x, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(1, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(2, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE));


        blocks.set(3, new Block(x, GameConstants.INITIAL_Y_COORDINATE + 1));
        blocks.set(5, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE + 1));

        orientation = N;
        orient(o);
    }

    /**
     * ...
     * .
     */
    private void BlockClusterSix(int x, Orientations o) {
        blocks.set(0, new Block(x, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(1, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(2, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE));

        blocks.set(3, new Block(x, GameConstants.INITIAL_Y_COORDINATE + 1));

        orientation = N;
        orient(o);
    }

    /**
     * ...
     *   .
     *   .
     */
    private void BlockClusterSeven(int x, Orientations o) {
        blocks.set(0, new Block(x, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(1, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(2, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE));

        blocks.set(5, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE + 1));

        blocks.set(8, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE + 2));

        orientation = N;
        orient(o);
    }

    /**
     * ...
     * ...
     * ...
     */
    private void BlockClusterEight(int x, Orientations o) {
        // manual construction
        blocks.set(0, new Block(x, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(1, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(2, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE));

        blocks.set(3, new Block(x, GameConstants.INITIAL_Y_COORDINATE + 1));
        blocks.set(4, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE + 1));
        blocks.set(5, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE + 1));

        blocks.set(6, new Block(x, GameConstants.INITIAL_Y_COORDINATE + 2));
        blocks.set(7, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE + 2));
        blocks.set(8, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE + 2));

        orientation = N;
        orient(o);
    }

    /**
     * ...
     * . .
     *   .
     */
    private void BlockClusterNine(int x, Orientations o) {
        // manual construction
        blocks.set(0, new Block(x, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(1, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(2, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE));

        blocks.set(3, new Block(x, GameConstants.INITIAL_Y_COORDINATE + 1));
        blocks.set(5, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE + 1));

        blocks.set(8, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE + 2));

        orientation = N;
        orient(o);
    }

    /**
     * ...
     * . .
     * . .
     */
    private void BlockClusterTen(int x, Orientations o) {
        // manual construction
        blocks.set(0, new Block(x, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(1, new Block(x + 1, GameConstants.INITIAL_Y_COORDINATE));
        blocks.set(2, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE));

        blocks.set(3, new Block(x, GameConstants.INITIAL_Y_COORDINATE + 1));
        blocks.set(5, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE + 1));

        blocks.set(6, new Block(x, GameConstants.INITIAL_Y_COORDINATE + 2));
        blocks.set(8, new Block(x + 2, GameConstants.INITIAL_Y_COORDINATE + 2));

        orientation = N;
        orient(o);
    }

    /**
     * Orients the block cluster based on the parameter. Utilises switch statement and call(s) to rotateRight
     *
     * NOTE: Updating the orientation falls to the invoked method.
     * @param o the orientation
     */
    private void orient(Orientations o) {
        switch(o) {
            case N -> {
                // nothing; is default
            }
            case E -> {
                rotateRight();
            }
            case S -> {
                rotateRight();
                rotateRight();
            }
            case W -> {
                rotateRight();
                rotateRight();
                rotateRight();
            }
        }
    }


    /**
     * Single Rotation method exists since a left rotation can be achieved by three consecutive right rotations.
     * For arguments against inefficiency 3n is O(n).
     *
     * UPDATES THE ORIENTATION.
     *
     * ____________________________________________________________________________________________________________________
     * >>> note to self: I forgot to update the coordinates of each of the contained blocks. The program still worked fine
     *                  since the blocks themselves were still valid. But how did check if has space still work? Because the
     *                  cells returned were rendered and based on their inherent coordinates that were never updated. In essence
     *                  the array was storing cells that had never had their coordinates updated (the array was reconfigured but
     *                  the cells were not).
     * ____________________________________________________________________________________________________________________
     */
    public void rotateRight() {
        ArrayList<Block> updated = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            updated.add(null);
        }

        // manual reassignment
        updated.set(0, blocks.get(6));
        if (blocks.get(6) != null) {
            blocks.get(6).moveUp();
            blocks.get(6).moveUp();
        }
        updated.set(1, blocks.get(3));
        if (blocks.get(3) != null) {
            blocks.get(3).moveRight();
            blocks.get(3).moveUp();
        }
        updated.set(2, blocks.get(0));
        if (blocks.get(0) != null) {
            blocks.get(0).moveRight();
            blocks.get(0).moveRight();
        }
        updated.set(3, blocks.get(7));
        if (blocks.get(7) != null) {
            blocks.get(7).moveLeft();
            blocks.get(7).moveUp();
        }
        updated.set(4, blocks.get(4));
        if (blocks.get(4) != null) {
            // nothing
        }
        updated.set(5, blocks.get(1));
        if (blocks.get(1) != null) {
            blocks.get(1).moveRight();
            blocks.get(1).moveDown(1);
        }
        updated.set(6, blocks.get(8));
        if (blocks.get(8) != null) {
            blocks.get(8).moveLeft();
            blocks.get(8).moveLeft();
        }
        updated.set(7, blocks.get(5));
        if (blocks.get(5) != null) {
            blocks.get(5).moveLeft();
            blocks.get(5).moveDown(1);
        }
        updated.set(8, blocks.get(2));
        if (blocks.get(2) != null) {
            blocks.get(2).moveDown(1);
            blocks.get(2).moveDown(1);
        }

        blocks.clear();
        blocks = updated;

        switch(orientation) {
            case N -> {
                orientation = Orientations.E;
            }
            case E -> {
                orientation = Orientations.S;
            }
            case S -> {
                orientation = Orientations.W;
            }
            case W -> {
                orientation = N;
            }
        }
    }

    /**
     * In contrast to my statements above pertaining to the ability to implement this rotation via three right rotations,
     * I decide to include it anyway since classes in different packages shouldn't feel this "shackle" of sorts.
     */
    public void rotateLeft() {
        rotateRight();
        rotateRight();
        rotateRight();
    }

    /**
     * REQUIRES: space os ensured in concert by classes Updater and Container.
     * Moves the block down
     */
    public void moveDownDefault() {
        ArrayList<Block> toCheck = getBlocksToCheck();
        for (Block block: toCheck) {
            block.moveDown(1);
        }
    }

    /**
     * REQUIRES: space os ensured in concert by classes Updater and Container.
     * Moves the block left.
     */
    public void moveLeft() {
        ArrayList<Block> toCheck = getBlocksToCheck();
        for (Block block: toCheck) {
            block.moveLeft();
        }

    }


    /**
     * REQUIRES: space os ensured in concert by classes Updater and Container.
     * Moves the block right.
     */
    public void moveRight() {
        ArrayList<Block> toCheck = getBlocksToCheck();
        for (Block block: toCheck) {
            block.moveRight();
        }

    }

    /**
     * @return Array of blocks that need to be checked by the container class.
     *
     * NOTE: Entails minor DOCUMENTED coupling. This decision was taken since a BlockCluster shouldn't know about the occupancy
     *      of the locations that encompass it.
     * Is exemplified in the REQUIRES clause of the moveLeft/Right... functions.
     */
    public ArrayList<Block> getBlocksToCheck() {
        ArrayList<Block> returnArray = new ArrayList<>();
        for (Block block : blocks) {
            if (block != null) {
                returnArray.add(block);
            }
        }
        return returnArray;

    }

    /**
     * For Testing.
     * @return orientation.
     */
    public Orientations getOrientation() {
        return orientation;
    }

    /**
     * Prints the contents of the block. Used for testing.
     */
    public void print() {
        for (int i = 0; i < 9; i++) {
            if(i%3==0 && i!=0) {
                System.out.print("\n");
            }
            if (blocks.get(i) != null) {
                System.out.print(".");
            } else {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /**
     * This method like the method getBlocksToCheck entails coupling. The argument remains the same.
     * This method was required because of the nature of a rotation: checks for left, right and down are relatively simple
     *      owing to the getBlocksToCheckMethod() (null blocks (inside the array) automatically mean there is space). Here,
     *      however, I HAVE to account for the null blocks in the array list since if the block in the array is null but the
     *      cell in the container is occupied a rotation may still be possible.
     * @return a reference to the array of blocks
     */
    public ArrayList<Block> getAllBlocks() {
        return blocks;
    }
}