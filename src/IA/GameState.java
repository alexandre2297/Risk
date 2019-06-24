package IA;

import Game.Board;
import Game.Country;
import Game.Misc;
import Game.Player;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Defines the state the game is in
 * Usable for IAs
 */
public class GameState {

    private Board board;

    private ArrayList<Country> countryList;
    private ArrayList<Player> countryOwnerList;
    private ArrayList<Integer> countryArmyList;

    public GameState(Board board) {
        this.board = board;
        countryList = new ArrayList<>();
        countryOwnerList = new ArrayList<>();
        countryArmyList = new ArrayList<>();
        try {
            for (int i = 0; i < board.getCountries().length; i++) {
                countryList.add((Country) board.getCountries()[i].clone());
                countryOwnerList.add(board.getCountries()[i].getOwner());
                countryArmyList.add(board.getCountries()[i].numSoldiers);
            }
        } catch (CloneNotSupportedException e) {}
    }

    public GameState(GameState state) {
        board = state.board;
        countryList = new ArrayList<>();
        try {
            for (Country c : state.getCountryList())
                countryList.add((Country) c.clone());
        } catch (CloneNotSupportedException e) {}
        countryOwnerList = state.getCountryOwnerList();
        countryArmyList = state.getCountryArmyList();

    }

    public ArrayList<Country> getCountryList() {
        return countryList;
    }

    public ArrayList<Player> getCountryOwnerList() {
        return countryOwnerList;
    }

    public ArrayList<Integer> getCountryArmyList() {
        return countryArmyList;
    }

    public void setCountryArmyVal(int index, int val) {
        countryArmyList.set(index, val);
    }

    public void setCountryOwnerVal(int index, Player val) {
        countryOwnerList.set(index, val);
    }


    public Board getBoard() {
        return board;
    }
}
