//package IA;

import Game.Board;
import Game.Player;
import java.util.ArrayList;

/**
 * defines the state the game is in
 * usable for IAs
 */
public class GameState {

    Board board;

    ArrayList<Player> countryOwnerList;
    ArrayList<Integer> countryArmyList;

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
