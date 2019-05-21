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
        String init = "Game.Player " + (board.getTurn() + 1) + ": ";
        return init + "Choose country to fortify: " + board.getSelectedCountry().getName() + " -> ___";
    }

    @Override
    public void nextButtonIsPushed() {
        board.getRules().nextPlayer();
    }

    @Override
    public Mode nextMode() {
        return new KeepFortifyingMode(board);
    }

    @Override
    public void mouseClick(Point mouse, boolean isRightClick) {
        board.getRules().selectFortify(mouse);
        System.out.println("Fortify To Mode for player " + (board.getTurn() + 1));

    }
}
