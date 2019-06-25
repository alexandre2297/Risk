package Modes;

import Game.Board;
import Game.Player;
import IA.IA;

import java.awt.*;

public class PlacingMode implements Mode {

    private Board board;

    public PlacingMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (board.getTurn() + 1) + ": ";
        return init + "Place troops: " + (board.getTroopsToPlace() - 1) + " remaining";
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
        //System.out.println("Placing Mode for player " + (board.getTurn() + 1));

    }
}
