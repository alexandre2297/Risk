package Modes;

import Game.Board;

import java.awt.*;

public class NewCountryMode implements Mode {

    private Board board;

    public NewCountryMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (Board.getTurn() + 1) + ": ";
        return init + "You successfully conquered " + Board.getSelectedSecondCountry().getName() +
                "! Add troops to your new or old country: " + Board.getTroopsToPlace() + " remaining";
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
        board.getRules().placeSoldierNewCountry(mouse);
        System.out.println("New Country Mode for player " + (Board.getTurn() + 1));

    }
}
