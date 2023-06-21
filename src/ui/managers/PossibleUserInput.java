package ui.managers;

/**
 * This is used for the action listeners in the GameBoard (and are passed to the Updater); This was a design decision I made
 *      based on abstraction: I had reservations about importing Swing components in class Updater.
 *
 *  There is potential for extensibility here since this depends on
 */
public enum PossibleUserInput {
    LEFT,
    RIGHT,
    DOWN,
    ROTATE_RIGHT,
    ROTATE_LEFT;
}
