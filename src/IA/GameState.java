package IA;

import Game.Board;
import Game.Player;
import java.util.ArrayList;

/**
 * Defines the state the game is in
 * Usable for IAs
 */
public class GameState {

    Board board;

    private ArrayList<Player> countryOwnerList;
    private ArrayList<Integer> countryArmyList;

    public GameState(Board board) {
        for (int i = 0; i != board.getCountries().length; i++) {
           countryOwnerList.add(board.getCountries()[i].getOwner());
           countryArmyList.add(board.getCountries()[i].numSoldiers);
        }
    }

    public ArrayList getCountryOwnerList() {
        return countryOwnerList;
    }

    public ArrayList getCountryArmyList() {
        return countryArmyList;
    }
}
