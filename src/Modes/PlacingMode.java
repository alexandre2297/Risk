package Modes;

import Game.Board;

import java.awt.*;

public class PlacingMode implements Mode {

    private Board board;

    public PlacingMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (Board.getTurn() + 1) + ": ";
        return init + "Place troops: " + Board.getTroopsToPlace() + " remaining";
    }

    @Override
    public void nextButtonIsPushed() {

    }

    @Override
    public Mode nextMode() {
        return new AttackFromMode(board);
    }

    @Override
    public void mouseClick(Point mouse, boolean isRightClick) {
        board.getRules().placeSoldier(mouse, false);
        System.out.println("Placing Mode for player " + (Board.getTurn() + 1));

    }
}
