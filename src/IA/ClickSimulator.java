package IA;

import Game.Board;
import Game.Country;

import java.awt.*;
public class ClickSimulator {

    private static ClickSimulator instance;
    private Board board;

    public static ClickSimulator getInstance() {
        if(instance == null) {
            instance = new ClickSimulator();
        }
        return instance;
    }

    private ClickSimulator() {
    }

    public void setBoard(Board b) {
        this.board = b;
    }

    public void clickOnCountry(Country c) {
        Point mouse = new Point(c.getX()+1,c.getY()+1);
        board.getMode().mouseClick(mouse, false);
        board.getTurnInfo().setText(board.getMode().getStringForMode());
        board.repaint();
    }

    public void clickOnNext() {
        board.getRules().nextButtonIsPushed();
    }
}
