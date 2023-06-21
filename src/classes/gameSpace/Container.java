package classes.gameSpace;

import classes.GameConstants;
import classes.gameObjects.Block;
import classes.gameObjects.compoundConstructs.BlockCluster;
import ui.gui.GraphicCell;

import java.util.ArrayList;

/**
 * Primary containing class;
 * Is composed of a grid of CellSpatialUnits which contains blocks (block can be null but this means is unoccupied; really just to mitigate against Type Casting
 *
 * Implements Singleton Pattern: none of the information required by the program is dynamic; everything is statically known:
 *          ref. classes.GameConstants
 *
 * 
 */
public class Container {

    /**
     * static instance of class.
     */
    private static Container container = new Container();

    /**
     * the container contains a reference to the single "floating" gameObject in the game space (at any one time there
     * is only ever a single "floating" object (INVARIANT) which is shared by ALL classes that keep references to "floating
     * objects" i.e. Updater (there is only one point when this invariant is broken: when amalgamate() is invoked, Updater
     * temporarily retains its reference to the amalgamated blockCluster. This is dealt with forthwith by the GameCycle.
     *
     * serves two functions: if is null (at beginning of game and after amalgamate() can introduce a new object.
     *
     */
    private BlockCluster blockCluster;

    /**
     * the format for cells is rows[column(s)] i.e. the outer array refers to the rows. The simple reason for this is in
     *           Tetris a row is cleared and not a column.
     */
    private ArrayList<ArrayList<CellSpatialUnit>> cells;

    /**
     * private constructor.
     */
    private Container() {
        cells = new ArrayList<>();
        for (int row = 0; row <= GameConstants.MAXIMUM_Y; row++) {
            ArrayList<CellSpatialUnit> toAdd = new ArrayList<>();
            for (int column = 0; column <= GameConstants.MAXIMUM_X; column++) {
                toAdd.add(new CellSpatialUnit(column, row));
            }
            cells.add(toAdd);
        }
        blockCluster = null;
    }

    /**
     * @return static Container instance
     */
    public static Container getInstance() {
//        if (container == null) {
//            container = new Container();
//        }
        return container;
    }

    /**
     * TODO boolean for including a sound upon clearing a row
     *
     * REQUIRES: gameObject's member variables have already been updated (by the Updater).
     *           Class managers.Updater ensured that the BlockCluster was moved according to the user input and via invocation
     *           to the methods in this class that ensure space.
     *           gameObject is not null.
     *
     * Works as follows: if any of the blocks present in a blockCluster VERTICALLY adjoin a pre-present block (in the cells spatial
     * unit). gameObject is set to null and the positioning of the blocks is retained. Thereafter a sweep of the entire
     * board is effected which checks if any rows need clearing and the game board is updated accordingly.
     *
     * DOES NOT UPDATE gameObject in any way whatsover (merely updates the occupancy of the CellSpatialUnits)
     *
     * Cycle is as follows:
     *          BEFORE INVOCATION: it is ensured that the blockClusters present coordinates were correctly updated (by the Updater).
                Gist: checks if any block in the BlockCluster vertically adjoins an unoccupied cell (for other cases see below).
                invoke amalgamate() if requisites are met ^^^ and horizontalClear() which clears a horizontal row effectively.
     *
     */
    public void checkAndUpdateGrid() {
        // first iterate the block cluster and check if for each the cell below is unoccupied.
        ArrayList<Block> blocksToCheck = blockCluster.getBlocksToCheck();
        boolean invokeAmalgamation = false;
        for (Block block: blocksToCheck) {
            if (block.getyCoord() < GameConstants.MINIMUM_Y) {
                // do nothing >>> this is owing to the particular way in which Block Clusters are constructed; they exist outside the game space
                invokeAmalgamation = false;
                break; // nothing else needs to be done: they are outside the game space.
            } else if (block.getyCoord() == GameConstants.MAXIMUM_Y) {
                invokeAmalgamation = true; // Guard is needed here (is implemented inside amalgamate) since the BlockCLuster may not have fully loaded into the gameSpace causing an ArrayOutOfBounds exception
            } else if (cells.get(block.getyCoord() + 1).get(block.getxCoord()).isOccupied()) { // an array out of index bounds should not occur owing to the control flow above.
                invokeAmalgamation = true;
            } else {
                // nothing
            }
        }

        if (invokeAmalgamation) {
            amalgamate(blocksToCheck);
            horizontalClear();
        }

    }

    /**
     * Checks the entire grid for whether to clear a row. Updates accordingly. Entails updating each cell to reflects new
     *       occupancy status.
     *
     */
    private void horizontalClear() {

        // checking the uppermost row
        boolean performInitial = true;
        for (CellSpatialUnit cell: cells.get(0)) {
            if (!cell.isOccupied()) {
                performInitial = false;
                break;
            }
        }
        if (performInitial){
            for (CellSpatialUnit cell : cells.get(0)) {
                cell.setOccupant(null);
            }
            GameConstants.SCORE++;
        }

        // checking and updating all the other rows.
        int counter = 1; // goes up to MAXIMUM_Y
        while (counter <= GameConstants.MAXIMUM_Y) {
            boolean perform = true;
            for (CellSpatialUnit cell: cells.get(counter)) {
                if (!cell.isOccupied()) {
                    perform = false;
                    break;
                }
            }
            if (perform) { // >>> i.e. row should be cleared
                // traverse up. I believe this is O(n^2)
                int reverseCounter;
                for (reverseCounter = counter; reverseCounter > 0; reverseCounter--) {
                    ArrayList<CellSpatialUnit> toReplace = cells.get(reverseCounter);
                    ArrayList<CellSpatialUnit> toReplaceWith = cells.get(reverseCounter - 1);
                    for (int i = 0; i<= GameConstants.MAXIMUM_X; i++) {
                        toReplace.get(i).setOccupant(toReplaceWith.get(i).getOccupant());
                        toReplaceWith.get(i).setOccupant(null);
                    }
                }
                GameConstants.SCORE++;
            }
            counter++;
        }
    }

    /**
     *
     * @param blocks list of blocks to incorporate into the blocks already present inside the container. (Entails updating
     *               the cellSpatialUnits to reflect occupancy and setting their references).
     *  sets gameObject to null
     *
     */
    private void amalgamate(ArrayList<Block> blocks) {
        for (Block block: blocks) {
            // no need to check for the Maximum boundary since classes Update and Container were intended to be reliable.
            if (block.getyCoord() >= GameConstants.MINIMUM_Y) { // since invoke amalgamation can be called on a block cluster that hasn't entirely entered the game space.
                cells.get(block.getyCoord()).get(block.getxCoord()).setOccupant(block);
            }
        }
        blockCluster = null;
    }

    /**
     * This function and the function below (unamalgamateGraphics) exist because we do not choose to consider cells occupied
     *     when the "floating" blockCluster superposes them. They are invoked by classes in the GUI AND MUST BE IN TANDEM:
     *     highly coupled methds.
     */
    public void amalgamateGraphics() {
        ArrayList<Block> blocks = blockCluster.getBlocksToCheck();
        for (Block block: blocks) {
            if(block.getyCoord() >= GameConstants.MINIMUM_Y) {
                cells.get(block.getyCoord()).get(block.getxCoord()).setOccupant(block);
            }
        }
    }

    public void unamalgamateGraphics() {
        ArrayList<Block> blocks = blockCluster.getBlocksToCheck();
        for (Block block: blocks) {
            if(block.getyCoord() >= GameConstants.MINIMUM_Y) {
                cells.get(block.getyCoord()).get(block.getxCoord()).setOccupant(null);
            }
        }
    }


    /**
     * REQUIRES: gameObject is null
     *
     * @param blockCluster is the new GameObject that is to be added to the GameSpace i.e. this Container
     */
    public void setBlockCluster(BlockCluster blockCluster) {
        if (this.blockCluster != null) {
            throw new RuntimeException("Attempted to add an object to the game space when one was already present");
        }
        this.blockCluster = blockCluster;
    }


    /**
     *
     * @return whether the container has room for a new object.
     */
    public boolean noCurrentFloatingObject() {
        return (blockCluster == null);
    }

    /**
     *
     * @return whether there is space for the blockCluster to be moved left.
     */
    public boolean hasSpaceToLeft() {
        ArrayList<Block> toCheck = blockCluster.getBlocksToCheck();
        boolean isSpace = true;

        for (Block block: toCheck) {
            if (block.getyCoord() + 1 < GameConstants.MINIMUM_Y) {
                return false;
            } else if (block.getxCoord() <= GameConstants.MINIMUM_X) {
                return false;
            } else if (block.getxCoord() - 1 < GameConstants.MINIMUM_X || block.getyCoord() < GameConstants.MINIMUM_Y ||
                    cells.get(block.getyCoord()).get(block.getxCoord() - 1).isOccupied()) {
                return false;
            }
        }

        return isSpace;
    }

    /**
     *
     * @return whether there is space for the blockCluster to be moved right.
     */
    public boolean hasSpaceToRight() {
        ArrayList<Block> toCheck = blockCluster.getBlocksToCheck();
        boolean isSpace = true;

        for (Block block: toCheck) {
            if (block.getyCoord() + 1 < GameConstants.MINIMUM_Y) {
                return false;
            } else if (block.getxCoord() >= GameConstants.MAXIMUM_X) {
                return false;
            } else if (block.getxCoord() + 1 > GameConstants.MAXIMUM_X || block.getyCoord() < GameConstants.MINIMUM_Y ||
                    cells.get(block.getyCoord()).get(block.getxCoord() + 1).isOccupied()) {
                return false;
            }
        }

        return isSpace;
    }

    /**
     *
     * @return whether there is space for the blockCluster to be moved down.
     *
     * Also sets the GAME_OVER.
     */
    public boolean hasSpaceDown() {
        ArrayList<Block> toCheck = blockCluster.getBlocksToCheck();
        boolean isSpace = true;



        for (Block block: toCheck) {
            if (block.getyCoord() + 1 == GameConstants.MINIMUM_Y && cells.get(0).get(block.getxCoord()).isOccupied()) {
                GameConstants.GAME_OVER = true;
                return false;
            }
            if (block.getyCoord() + 1 >= GameConstants.MINIMUM_Y && block.getyCoord() + 1 <= GameConstants.MAXIMUM_Y
                    && cells.get(block.getyCoord() + 1).get(block.getxCoord()).isOccupied()) {
                isSpace = false;
            }
        }
        return isSpace;
    }


    /**
     * Checks if the blockCluster being tracked can be rotated right.
     */
    public boolean rightRotationPossible() {
        ArrayList<Block> blocks = blockCluster.getAllBlocks();

        Block block;

        block = blocks.get(0);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord()).get(block.getxCoord() + 2).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(1);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord() + 1).get(block.getxCoord() + 1).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(2);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord() + 2).get(block.getxCoord()).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(3);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord() - 1).get(block.getxCoord() + 1).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(4);
        // nothing

        block = blocks.get(5);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord() + 1).get(block.getxCoord() - 1).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(6);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord() - 2).get(block.getxCoord()).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(7);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord() - 1).get(block.getxCoord() - 1).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(8);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord()).get(block.getxCoord() - 2).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        return true;

    }

    /**
     * Checks if the blockCluster being tracked can be rotated left.
     */
    public boolean leftRotationPossible() {
        ArrayList<Block> blocks = blockCluster.getAllBlocks();

        Block block;

        block = blocks.get(0);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord() + 2).get(block.getxCoord()).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(1);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord() + 1).get(block.getxCoord() - 1).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(2);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord()).get(block.getxCoord() - 2).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(3);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord() + 1).get(block.getxCoord() + 1).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(4);
        // nothing

        block = blocks.get(5);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord() - 1).get(block.getxCoord() - 1).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(6);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord()).get(block.getxCoord() + 2).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(7);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord() - 1).get(block.getxCoord() + 1).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }

        block = blocks.get(8);
        if (block != null) {
            try {
                if (cells.get(block.getyCoord() - 2).get(block.getxCoord()).isOccupied()) {
                    return false;
                }
            } catch (IndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prints contents of the container.
     * **Testing**
     */
    public void print() {
        int count = 0;
        for (ArrayList<CellSpatialUnit> row: cells) {
            for (CellSpatialUnit cell: row) {
                if (cell.isOccupied()) {
                    System.out.print(".");
                    count++;
                } else {
                    System.out.print("X");
                }
            }
            System.out.println();
        }
        System.out.println(count);
    }

    /**
     * For Testing
     */
    public BlockCluster getBlockCluster() {
        return blockCluster;
    }

    /**
     * For the GUI.
     * Introduces a new dependency to the GUI class GraphicCell.
     * Populates the provided nested array with GraphicCells with relations to CellSpatialUnits Contained herein.
     *
     * Was introduced for convenience but can be extended: a preset tetris board may be constructed.
     */

    public void populateGraphicGrid(ArrayList<ArrayList<GraphicCell>> grid) {
        for (ArrayList<CellSpatialUnit> row: cells) {
            ArrayList<GraphicCell> graphicRow = new ArrayList<>();
            for (CellSpatialUnit cell: row) {
                graphicRow.add(new GraphicCell(cell));
            }
            grid.add(graphicRow);
        }
    }
}
