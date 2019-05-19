package Modes;

import Game.Board;

public class AttackFromMode implements Mode {


    private Board board;

    public AttackFromMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (Board.getTurn() + 1) + ": ";
        return init + "Choose country to attack from: ___ -> ___";
    }

    @Override
    public void nextButtonIsPushed() {
        Board.setSelectedCountry(null);
        board.setMode(new FortifyFromMode(board));
    }

    @Override
    public Mode nextMode() {
        return null;
    }
}
