package Modes;

import java.awt.*;

public interface Mode {

    /* returns a String that contains information
     * on the game state
     */
    String getStringForMode();


    /* Allows the player to move on to the next phase of the game
     * This function is used by the Next button
     */
    void nextButtonIsPushed();


    /* iterates the mode to the next
     * on the end of a turn, iterates to next player
     */
    Mode nextMode();

    void mouseClick(Point mouse, boolean isRightClick);
}
