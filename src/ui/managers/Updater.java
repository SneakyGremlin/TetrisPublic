package ui.managers;

import classes.gameObjects.compoundConstructs.BlockCluster;
import classes.gameSpace.Container;

/**
 * Is the primary class for the updating of the generated BlockClusters. These updates are both in response to "ticks"
 *      of the chosen method of time and user induced updates.
 *      Also involves the incorporation of a BlockCluster in the blocks pre-present on the board
 *
 *      For a comprehensive overview of the cycle please refer to the README.md.
 *      It should, however, suffice to say that GameBoard invokes Updater which invokes Container methods.
 *
 *      The updater class implements the singleton pattern.
 */
public class Updater {

    private static Updater updater = new Updater();

    /**
     * keeps track of the BlockCluster produced by generator.
     * Here the requirements are much more lax since stringent bookkeeping is done by the Container class
     */
    private BlockCluster blockCluster;

    private Updater() {

    }

    public static Updater getInstance() {
        return updater;
    }

    /**
     * Block Cluster Update. Code repeated and thus was abstracted.
     */
    private void updateCluster() {
        if (Container.getInstance().noCurrentFloatingObject()) {
            BlockCluster newBlockCluster = Generator.getInstance().generateBlockCluster();
            blockCluster = newBlockCluster;
            Container.getInstance().setBlockCluster(blockCluster);
        }
    }

    /**
     * This is the default update method invoked by "ticks". Does not incorporate user actions.
     */
    public void updateDefault() {
        updateCluster();

        if (Container.getInstance().hasSpaceDown()) {
            blockCluster.moveDownDefault();
            Container.getInstance().checkAndUpdateGrid();
        }
        updateCluster(); // >>> required here since checkAndUpdateGrid may result in there being no tracked "floating" blockCluster
    }

    public void updateGeneral(PossibleUserInput userAction)  {
        updateCluster();

        switch (userAction) {
            case LEFT -> {
                if (Container.getInstance().hasSpaceToLeft()) {
                    blockCluster.moveLeft();
                    break;
                }
            }
            case RIGHT -> {
                if (Container.getInstance().hasSpaceToRight()) {
                    blockCluster.moveRight();
                    break;
                }
            }
            case DOWN -> {
                if (Container.getInstance().hasSpaceDown()) {
                    blockCluster.moveDownDefault();
                }
                break;
            }
            case ROTATE_RIGHT -> {
                if (Container.getInstance().rightRotationPossible()) {
                    blockCluster.rotateRight();
                }
            }
            case ROTATE_LEFT -> {
                if (Container.getInstance().leftRotationPossible()) {
                    blockCluster.rotateLeft();
                }
            }
        }

        Container.getInstance().checkAndUpdateGrid();
        updateCluster();
    }
}
