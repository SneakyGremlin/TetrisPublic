package classes.gameObjects;

/**
 * Primary contained class and world object. Is also the constitutional unit of the primary Game Object BlockCluster.
 * Note that a Block object can be null (whilst inside BlockCluster).
 *
 * Attributes:
 *          xCoord: this is the individual cell's column location when it is either in EITHER the Block Cluster in Generator, Updater, or Container
 *          yCoord: this is the individual cell's row location when it is either in EITHER the Block Cluster in Generator, Updater, or Container
 *          note that all the classes which contain a reference to a "current" blockCluster refer to the same blockCluster at ALL times except one: this one-off location is inside the updateCluster() inside the Update Class.
 *          location refers to the gameSpace.Container's cellSpatialUnits.
 *
 * NOTE: A cell (when is part of a blockCluster) is NOT consider to be at that location (hence the terminology float, This
 *       invariant is broken for rendering of the board via the invocation of amalgamateGraphics() inside the gui.GameBoard Class.
 */
public class Block {
    int xCoord;
    int yCoord;

    /**
     *
     * @param x is the x coordinate i.e. the column
     * @param y is the y coordinate i.e. the row; is set to default
     */
    public Block(int x, int y) {
        xCoord = x;
        yCoord = y;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public void moveLeft() {
        xCoord--;
    }

    public void moveRight() {
        xCoord++;
    }

    /**
     * Principle of decoupling (kind of): a Block doesn't need to know whether the board has sufficient place below it; as such
     *      the caller itself should calculate and specify the change in the coordinate
     * @param x number of steps to move down [1,5]
     */
    public void moveDown(int x) {
        yCoord += x;
    }

    public void moveUp() {
        yCoord--;
    }
}
