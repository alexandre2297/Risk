package Modes;

import Game.Board;

import java.awt.*;

public class AttackFromMode implements Mode {


    private Board board;

    public AttackFromMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (board.getTurn() + 1) + ": ";
        return init + "Choose country to attack from: ___ -> ___";
    }

    @Override
    public void nextButtonIsPushed() {
        board.setSelectedCountry(null);
        board.setMode(new FortifyFromMode(board));
    }

    @Override
    public Mode nextMode() {
        return new AttackToMode(board);
    }

    @Override
    public void mouseClick(Point mouse, boolean isRightClick) {
        board.getRules().selectOwnerCountry(mouse);
        System.out.println("Attack From Mode for player " + (board.getTurn() + 1));

    }
}
