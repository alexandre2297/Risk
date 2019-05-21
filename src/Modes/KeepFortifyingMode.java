package Modes;

import Game.Board;

import java.awt.*;

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
        board.getRules().nextPlayer();
    }

    @Override
    public Mode nextMode() {
        board.getRules().nextPlayer();
        return board.getMode();
    }

    @Override
    public void mouseClick(Point mouse, boolean isRightClick) {
        if (Board.getSelectedSecondCountry().inBounds(mouse)) {
            board.getRules().fortify();
            System.out.println("Keep Fortifying Mode for player " + (Board.getTurn() + 1));
        }

    }
}
