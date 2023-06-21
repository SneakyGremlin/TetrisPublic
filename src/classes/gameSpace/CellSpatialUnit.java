package classes.gameSpace;

import classes.gameObjects.Block;

/**
 * Primary 2D Spatial Unit in the Game Space.
 *
 * Attributes:
 *      occupant serves a two-fold function: if it is null then the cell is empty,
 *          else it is occupied.
 *      xCoord: its location inside the container (column)
 *      yCoord: its location inside the container (row)
 *
 * NOTE: CONTRARY to expectation a cell is unoccupied if there is a "floating" object.
 *       This has been regurgitated several times, please refer to other documentations (classes.gameObjects.Block) as well
 */
public class CellSpatialUnit {

    private Block occupant;
    private int xCoord;
    private int yCoord;

    public CellSpatialUnit(int x, int y) {
        occupant = null;
        xCoord = x;
        yCoord = y;
    }

    /**
     *
     * Is used for setting the occupant (invoked by amalgamateGraphics() in Container class) and when a row is cleared (in which case gameObject is null).
     *
     * @param gameObject: contained is set to this parameter.
     */
    public void setOccupant(Block gameObject) {
        occupant = gameObject;
    }

    /**
     *
     * @return whether the spatial unit is occupied.
     */
    public boolean isOccupied() {
        return (occupant != null);
    }

    /**
     * Testing suite.
     * @return is the block occupied.
     */
    public Block getOccupant() {
        return occupant;
    }
}