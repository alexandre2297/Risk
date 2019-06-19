package IA.Behavior;

import Game.Country;
import Game.Player;
import IA.*;

import java.util.ArrayList;
import java.util.Collections;

public class AggressiveBehavior implements Behavior {

    private GameState inputState;
    private Player player;
    private Move move = new Move();
    private GameState outputState;

    public AggressiveBehavior(GameState state, Player player) {
        this.inputState = state;
        this.player = player;
        outputState = new GameState(state);
    }

    private int getPlacementTroops() {
        return IA.getContinentBonuses(inputState, player) + Math.max(IA.getNbCountries(inputState, player) / 3, 3);
    }

    /**
     * Place les unités sur tous les pays mitoyens avec un pays ennemi ayant un nombre de soldats elevé
     *
     * @todo warning: sorting pairs is fucking twisting
     */
    public void placement() {
        int troopsToPlace = getPlacementTroops();

        //get bordering countries and corresponding armies
        ArrayList<Pair> borderingArmies = new ArrayList<>();
        for (Country country1 : inputState.getCountryList()) {
            if (country1.getOwner() == player) {
                int borderingArmy = 0;
                for (Country country2 : country1.adjacentCountries) {
                    if (country2.getOwner() != player) {
                        borderingArmy += (Integer) inputState.getCountryArmyList().get(inputState.getCountryList().indexOf(country2));
                    }
                }
                borderingArmies.add(new Pair<>(borderingArmy, country1));
            }
        }

        // put troops next to the countries that have the biggest armies
        Collections.sort(borderingArmies, Collections.reverseOrder());
        for (Pair<Integer, Country> pair : borderingArmies) {
            if (troopsToPlace <= 0)
                break;
            int countryIndex = inputState.getCountryList().indexOf(pair.second);
            int currentArmy = (Integer) outputState.getCountryArmyList().get(countryIndex);
            if (currentArmy < pair.first) {
                if (troopsToPlace < pair.first) {
                    outputState.setCountryArmyVal(countryIndex, currentArmy + pair.first);
                    troopsToPlace -= pair.first;
                    move.placementList.add(new Pair<>(pair.first, pair.second));
                } else {
                    outputState.setCountryArmyVal(countryIndex, currentArmy + troopsToPlace);
                    move.placementList.add(new Pair<>(troopsToPlace, pair.second));
                    troopsToPlace = 0;
                }
            }
        }

        //put remaining troops to a pseudorandom country
        if (troopsToPlace > 0) {
            int countryIndex = inputState.getCountryList().indexOf(borderingArmies.get(0).second);
            int currentArmy = (Integer) outputState.getCountryArmyList().get(countryIndex);
            outputState.setCountryArmyVal(countryIndex, currentArmy + troopsToPlace);
            move.placementList.add(new Pair<>(troopsToPlace, (Country) borderingArmies.get(0).second));
            troopsToPlace = 0;
        }
    }


    public void attack() {}
    public void reinforcement() {}
    public Pair<GameState, Move> getActions() {return null;}
}
//for (Country country1 : inputState.getBoard().getCountries()) {
//
//        if (country1.)
//        for (Country country2 : country1.getAdjacentCountries()) {
//        if()
//        }
//        }
