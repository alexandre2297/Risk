package Modes;

import Game.Board;

import java.awt.*;

public class FortifyFromMode implements Mode {

    private Board board;

    public FortifyFromMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (board.getTurn() + 1) + ": ";
        return init + "Choose country to fortify from: ___ -> ___";
    }

    @Override
    public void nextButtonIsPushed() {
        board.getRules().nextPlayer();
    }

    @Override
    public Mode nextMode() {
        return new FortifyToMode(board);
    }

    @Override
    public void mouseClick(Point mouse, boolean isRightClick) {
        board.getRules().selectOwnerCountry(mouse);
        //System.out.println("Fortify From Mode for player " + (board.getTurn() + 1));

    }
}
