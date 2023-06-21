package ui.managers;

import classes.GameConstants;
import classes.gameObjects.compoundConstructs.BlockCluster;
import classes.gameObjects.compoundConstructs.Forms;
import classes.gameObjects.compoundConstructs.Orientations;

import java.util.ArrayList;
import java.util.Random;

/**
 * Is the primary class responsible for the generation of all block clusters. Uses Java's Random class.
 *
 * The generator class implements the singleton pattern.
 *
 * CLASS IS HIGHLY COUPLED WITH ENUMERATIONS Orientations and Forms.
 */
public class Generator {

    private static Generator generator = new Generator();

    public ArrayList<Orientations> orientations;
    public ArrayList<Forms> forms;

    public Random randomGenerator;

    /**
     * coupling is inherent with enumeration...
     */
    private Generator() {
        orientations = new ArrayList<>();
        orientations.add(Orientations.N);
        orientations.add(Orientations.E);
        orientations.add(Orientations.S);
        orientations.add(Orientations.W);

        forms = new ArrayList<>();
        forms.add(Forms.F0);
        forms.add(Forms.F1);
        forms.add(Forms.F2);
        forms.add(Forms.F3);
        forms.add(Forms.F4);
        forms.add(Forms.F5);
        forms.add(Forms.F6);
        forms.add(Forms.F7);
        forms.add(Forms.F8);
        forms.add(Forms.F9);
        forms.add(Forms.F10);

        randomGenerator = new Random();
    }

    public static Generator getInstance() {
//        if (generator == null) {
//            generator = new Generator();
//        }
        return generator;
    }

    /**
     * Generates a new BlockCluster
     * @return BlockCluster randomly generated
     */
    public BlockCluster generateBlockCluster() {
        // generates three random values for all the parameters of a block cluster; x o f
        // >>>>>>>>>> DOES PRODUCE ZERO
        int xValue = randomGenerator.nextInt(GameConstants.MAXIMUM_X - GameConstants.MAX_DIMENSION_OF_BLOCK_CLUSTER + 1);
        int orient = randomGenerator.nextInt(4); // > [0, 3]
        int f = randomGenerator.nextInt(11); // > [0, 10]

        BlockCluster blockCluster = new BlockCluster(xValue, orientations.get(orient), forms.get(f));

        return blockCluster;


//        // CODE BELOW WAS FOR TESTING ROW CLEARING.
//        int xValue = randomGenerator.nextInt(Constants.MAXIMUM_X - Constants.MAX_DIMENSION_OF_BLOCK_CLUSTER + 1);
//
//        BlockCluster blockCluster = new BlockCluster(xValue, Orientation.N, Forms.F2);
//
//        return blockCluster;

        // CODE BELOW IS FOR TESTING ROTATION
//        int xValue = randomGenerator.nextInt(Constants.MAXIMUM_X - Constants.MAX_DIMENSION_OF_BLOCK_CLUSTER + 1);
//
//        BlockCluster blockCluster = new BlockCluster(xValue, Orientation.N, Forms.F2);
//
//        return blockCluster;
    }
}

// >>> classes are not static apparently...
