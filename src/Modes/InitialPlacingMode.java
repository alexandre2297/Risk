package Modes;

import Game.Board;

import java.awt.*;

public class InitialPlacingMode implements Mode {

    private Board board;

    public InitialPlacingMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (Board.getTurn() + 1) + ": ";
        return init + "Welcome to Risk! Place troops: " + Board.getTroopsToPlace() + " remaining";
    }

    @Override
    public void nextButtonIsPushed() {

    }

    @Override
    public Mode nextMode() {
        return new PlacingMode(board);
    }

    @Override
    public void mouseClick(Point mouse, boolean isRightClick) {

        board.placeSoldier(mouse, isRightClick);
        System.out.println("Initial Placing Mode for player" + (Board.getTurn() + 1));
    }
}
