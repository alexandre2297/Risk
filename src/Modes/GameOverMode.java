package Modes;

import Game.Board;

public class GameOverMode implements Mode {


    private Board board;

    public GameOverMode(Board board) {
        this.board = board;
    }

    @Override
    public String getStringForMode() {
        String init = "Game.Player " + (Board.getTurn() + 1) + ": ";
        return init + "You won!!!";
    }

    @Override
    public void nextButtonIsPushed() {

    }

    @Override
    public Mode nextMode() {
        return this;
    }
}
