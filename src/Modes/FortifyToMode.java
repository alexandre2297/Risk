package Modes;

import Game.Board;

import java.awt.*;

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
        board.nextPlayer();
    }

    @Override
    public Mode nextMode() {
        return new KeepFortifyingMode(board);
    }

    @Override
    public void mouseClick(Point mouse, boolean isRightClick) {
        board.selectFortify(mouse);
        System.out.println("Fortify To Mode for player " + (Board.getTurn() + 1));

    }
}
