package Modes;

import Game.Board;

public class FortifyFromMode implements Mode {

    private Board board;

    public FortifyFromMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (Board.getTurn() + 1) + ": ";
        return init + "Choose country to fortify from: ___ -> ___";
    }

    @Override
    public void nextButtonIsPushed() {

    }

    @Override
    public Mode nextMode() {
        return null;
    }
}
