package Modes;

import Game.Board;

public class InitialPlacingMode implements Mode {

    private Board board;

    public InitialPlacingMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (Board.getTurn() + 1) + ": ";
        return init + "Welcome to Risk! Place troops: " + Board.getTroopsToPlace() + " remaining";
    }

    @Override
    public void nextButtonIsPushed() {
        return;
    }

    @Override
    public Mode nextMode() {
        return null;
    }
}
