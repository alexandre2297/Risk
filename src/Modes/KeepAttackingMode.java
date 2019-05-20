package Modes;

import Game.Board;

import java.awt.*;

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

        Board.setSelectedCountry(null);
        Board.setSelectedSecondCountry(null);
        board.setMode(new AttackFromMode(board));
    }

    @Override
    public Mode nextMode() {
        return new NewCountryMode(board);
    }

    @Override
    public void mouseClick(Point mouse, boolean isRightClick) {
        board.keepAttacking(mouse);
        System.out.println("Keep Attacking Mode for player " + (Board.getTurn() + 1));

    }
}
