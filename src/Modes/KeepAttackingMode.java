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
        String init = "Game.Player " + (board.getTurn() + 1) + ": ";
        return init + "Keep Attacking? " + board.getSelectedCountry().getName() +
                " -> " + board.getSelectedSecondCountry().getName();
    }

    @Override
    public void nextButtonIsPushed() {

        board.setSelectedCountry(null);
        board.setSelectedSecondCountry(null);
        board.setMode(new AttackFromMode(board));
    }

    @Override
    public Mode nextMode() {
        return new NewCountryMode(board);
    }

    @Override
    public void mouseClick(Point mouse, boolean isRightClick) {
        board.getRules().keepAttacking(mouse);
        //System.out.println("Keep Attacking Mode for player " + (board.getTurn() + 1));

    }
}
