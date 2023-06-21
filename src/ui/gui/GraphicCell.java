package ui.gui;

import classes.gameSpace.CellSpatialUnit;

import javax.swing.*;
import java.awt.*;

/**
 * Primary contained class for the grid for the GUI. Contains a reference to the corresponding cell in the underlying
 * game container.
 */
public class GraphicCell extends JPanel {
    private CellSpatialUnit cell;

    public GraphicCell(CellSpatialUnit cell) {
        this.cell = cell;
        setBackground(Color.white); // >>> initial should be white always since the board is emtpy
        setSize(GraphicsConstants.BLOCK_DIMENSION, GraphicsConstants.BLOCK_DIMENSION);
    }

    /**
     * testing
     */
    public CellSpatialUnit getCell() {
        return cell;
    }

    /**
     * Updates the colour of the JPanel based on the state of the CellSpatialUnit it refers to.
     */
    public void update() {
        if (cell.isOccupied()) {
            setBackground(Color.black);
        } else {
            setBackground(Color.white);
        }
    }
}
