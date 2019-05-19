package Modes;

import Game.Board;

public class FortifyToMode implements Mode {

    private Board board;

    public FortifyToMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (Board.getTurn() + 1) + ": ";
        return init + "Choose country to fortify: " + Board.getSelectedCountry().getName() + " -> ___";
    }

    @Override
    public void nextButtonIsPushed() {

    }

    @Override
    public Mode nextMode() {
        return null;
    }
}
