package Modes;

import Game.Board;

public class KeepFortifyingMode implements Mode {

    private Board board;

    public KeepFortifyingMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (Board.getTurn() + 1) + ": ";
        return init + "Continue to fortify " + Board.getSelectedCountry().getName() + " -> " +
                Board.getSelectedSecondCountry().getName() + "?";
    }

    @Override
    public void nextButtonIsPushed() {

    }

    @Override
    public Mode nextMode() {
        return null;
    }
}
