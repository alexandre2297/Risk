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
        String init = "Game.Player " + (Board.getTurn() + 1) + ": ";
        return init + "Choose country to attack: " + Board.getSelectedCountry().getName() + " -> ___";
    }

    @Override
    public void nextButtonIsPushed() {

        Board.setSelectedCountry(null);
        Board.setSelectedSecondCountry(null);
        board.setMode(new FortifyFromMode(board));
    }

    @Override
    public Mode nextMode() {
        return new KeepAttackingMode(board);
    }

    @Override
    public void mouseClick(Point mouse, boolean isRightClick) {
        board.selectEnemyCountry(mouse);
        System.out.println("Attack To Modes.Mode");

    }
}
