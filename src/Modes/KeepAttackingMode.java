package Modes;

import Game.Board;

public class KeepAttackingMode implements Mode {

    private Board board;

    public KeepAttackingMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (Board.getTurn() + 1) + ": ";
        return init + "Keep Attacking? " + Board.getSelectedCountry().getName() +
                " -> " + Board.getSelectedSecondCountry().getName();
    }

    @Override
    public void nextButtonIsPushed() {

    }

    @Override
    public Mode nextMode() {
        return null;
    }
}
