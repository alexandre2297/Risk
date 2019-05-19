package Modes;

import Game.Board;

public class PlacingMode implements Mode {

    private Board board;

    public PlacingMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (Board.getTurn() + 1) + ": ";
        return init + "Place troops: " + Board.getTroopsToPlace() + " remaining";
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
