package Modes;

import Game.Board;

import java.awt.*;

public class AttackToMode implements Mode {


    private Board board;

    public AttackToMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (board.getTurn() + 1) + ": ";
        return init + "Choose country to attack: " + board.getSelectedCountry().getName() + " -> ___";
    }

    @Override
    public void nextButtonIsPushed() {

        board.setSelectedCountry(null);
        board.setSelectedSecondCountry(null);
        board.setMode(new FortifyFromMode(board));
    }

    @Override
    public Mode nextMode() {
        return new KeepAttackingMode(board);
    }

    @Override
    public void mouseClick(Point mouse, boolean isRightClick) {
        board.getRules().selectEnemyCountry(mouse);
        //System.out.println("Attack To Mode for player " + (board.getTurn() + 1));

    }
}
