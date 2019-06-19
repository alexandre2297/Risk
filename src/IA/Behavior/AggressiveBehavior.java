package IA.Behavior;

import Game.Country;
import Game.Player;
import IA.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class AggressiveBehavior implements Behavior {

    private int weakCountriesThreshold;

    private GameState inputState;
    private Player player;
    private Move move = new Move();
    private GameState outputState;

    public AggressiveBehavior(GameState state, Player player, int randomizer) {
        this.inputState = state;
        this.player = player;
        outputState = new GameState(state);
        this.weakCountriesThreshold = randomizer;
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
                        borderingArmy += inputState.getCountryArmyList().get(inputState.getCountryList().indexOf(country2));
                    }
                }
                borderingArmies.add(new Pair<>(borderingArmy, country1));
            }
        }

        // put troops next to the countries that have the biggest armies
        borderingArmies = Misc.removeDuplicates(borderingArmies);
        Collections.sort(borderingArmies, Collections.reverseOrder());
        for (Pair<Integer, Country> pair : borderingArmies) {
            if (troopsToPlace <= 0)
                break;
            int countryIndex = inputState.getCountryList().indexOf(pair.second);
            int currentArmy = outputState.getCountryArmyList().get(countryIndex);
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
            int currentArmy = outputState.getCountryArmyList().get(countryIndex);
            outputState.setCountryArmyVal(countryIndex, currentArmy + troopsToPlace);
            move.placementList.add(new Pair<>(troopsToPlace, (Country) borderingArmies.get(0).second));
            troopsToPlace = 0;
        }
    }



    /*
    Attaquer un pays parmi les pays en bordure des nôtres appartenant à un continent possédé entièrement par l’ennemi
     */
    public void attack() {
        ArrayList<Pair<Country, Integer>> startAttackeableCountries = getAdjacentEnemyCountries(outputState, weakCountriesThreshold)

        for (Pair<Country, Integer> countryPair : startAttackeableCountries) {
            Country country = countryPair.first;
            if (isACompleteContinent(country)) {
                takeCountry(country);
            }
        }

    }

    /**
     * get a croissant sorted list of all the countries adjacent to our countries from a stae
     * @param maxArmyPositionned filter out enemy countries than have more than given parameter
     * @return list of croissant sorted list of enemy countries
     */
    private ArrayList<Pair<Country, Integer>> getAdjacentEnemyCountries(GameState state, int maxArmyPositionned) {
        ArrayList<Pair<Country, Integer>> adjacentCountryList = new ArrayList<>();
        for (Country country1 : state.getCountryList()) {
            if (country1.getOwner() == player) {
                for (Country country2 : country1.adjacentCountries) {
                    if (country2.getOwner() != player) {
                        int countryArmy = state.getCountryArmyList().get(state.getCountryList().indexOf(country2));
                        if (countryArmy <= maxArmyPositionned)
                            adjacentCountryList.add(new Pair<>(country2, countryArmy));
                    }
                }
            }
        }
        adjacentCountryList = Misc.removeDuplicates(adjacentCountryList);
        Collections.sort(adjacentCountryList, Collections.reverseOrder());

        return adjacentCountryList;
    }

    public void reinforcement() {}
    public Pair<GameState, Move> getActions() {return null;}


    /*
    Is the continent of a given country owned by only the player owning the country
     */
    private boolean isACompleteContinent(Country country) {
        Set<Country> continent = country.getContinent();
        for (Country country2 : continent) {
            if (country2.getOwner() != country.getOwner())
                return false;
        }
        return true;
    }

    private void takeCountry(Country country) {
    }
}
//for (Country country1 : inputState.getBoard().getCountries()) {
//
//        if (country1.)
//        for (Country country2 : country1.getAdjacentCountries()) {
//        if()
//        }
//        }
