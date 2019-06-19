package IA.Behavior;

import Game.Country;
import Game.Misc;
import Game.Player;
import IA.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class AggressiveBehavior implements Behavior {

    //@TODO we need to create deep copies of GameState and Countries at least

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
        ArrayList<Pair<Integer, Country>> borderingArmies = getNotSafeCountries();


        // put troops next to the countries that have the biggest armies
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
            move.placementList.add(new Pair<>(troopsToPlace, borderingArmies.get(0).second));
            troopsToPlace = 0;
        }
    }

    /**
     * return a list of the player countries that have common border with enemy countries, and the total armies that are bordering them (in a croissant sort)
     * @return list of pair<country, surroundingArmies>
     */
    private ArrayList<Pair<Integer, Country>> getNotSafeCountries() {
        //get bordering countries and corresponding armies
        ArrayList<Pair<Integer, Country>> borderingArmies = new ArrayList<>();
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
        Collections.sort(borderingArmies, Collections.reverseOrder());
        return borderingArmies;
    }


    /*
    Attaquer un pays parmi les pays en bordure des nôtres appartenant à un continent possédé entièrement par l’ennemi
     */
    public void attack() {
        ArrayList<Triple<Country, Country, Integer>> startAttackeableCountries = getAdjacentEnemyCountries(weakCountriesThreshold);

        //take all the countries possible that make the ennemy have continents
        for (Triple<Country, Country, Integer> countryTriple : startAttackeableCountries) {
            Country enemyCountry = countryTriple.second;
            if (isACompleteContinent(enemyCountry)) {
                takeCountry(countryTriple.first, enemyCountry);
            }
        }

        //attack weak countries
        ArrayList<Triple<Country, Country, Integer>> attackeableCountries = getAdjacentEnemyCountries(weakCountriesThreshold);
        for (Triple<Country, Country, Integer> countryTriple : attackeableCountries)
            takeCountry(countryTriple.first, countryTriple.second);


    }


    /**
     * Place les unités sur tous les pays mitoyens avec un pays ennemi ayant un nombre de soldats elevé
     *
     * @todo warning: sorting pairs is fucking twisting
     * @todo EVERYTHINF IS HJUST COPIED FROM PLACEMENT AND NEEDS TO BE ADJUSTED en renforcant un pays avec un autre
     */
    public void reinforcement() {
        int troopsToPlace = getPlacementTroops();
        ArrayList<Pair<Integer, Country>> borderingArmies = getNotSafeCountries();


        // put troops next to the countries that have the biggest armies
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
    }

    //put remaining troops to a pseudorandom country
        if (troopsToPlace > 0) {
        int countryIndex = inputState.getCountryList().indexOf(borderingArmies.get(0).second);
        int currentArmy = outputState.getCountryArmyList().get(countryIndex);
        outputState.setCountryArmyVal(countryIndex, currentArmy + troopsToPlace);
        move.placementList.add(new Pair<>(troopsToPlace, borderingArmies.get(0).second));
        troopsToPlace = 0;
    }
}


    public Pair<GameState, Move> getActions() {return null;}

    /**
     * get a croissant sorted list of all the countries adjacent to our countries
     * @param maxArmyPositioned filter out enemy countries than have more than given parameter
     * @return list of croissant sorted list of enemy countries
     */
    private ArrayList<Triple<Country, Country, Integer>> getAdjacentEnemyCountries(int maxArmyPositioned) {
        ArrayList<Triple<Country, Country, Integer>> adjacentCountryList = new ArrayList<>();
        for (Country country1 : outputState.getCountryList()) {
            if (country1.getOwner() == player) {
                for (Country country2 : country1.adjacentCountries) {
                    if (country2.getOwner() != player) {
                        int countryArmy = outputState.getCountryArmyList().get(outputState.getCountryList().indexOf(country2));
                        if (countryArmy <= maxArmyPositioned)
                            adjacentCountryList.add(new Triple<>(country1, country2, countryArmy));
                    }
                }
            }
        }
        adjacentCountryList = Misc.removeDuplicates(adjacentCountryList);
        Collections.sort(adjacentCountryList, Collections.reverseOrder());

        return adjacentCountryList;
    }


    /**
     * Is the continent of a given country owned by only the player owning the country as well
     */
    private boolean isACompleteContinent(Country country) {
        Set<Country> continent = country.getContinent();
        for (Country country2 : continent) {
            if (country2.getOwner() != country.getOwner())
                return false;
        }
        return true;
    }

    private void takeCountry(Country countryFrom, Country countryTo) {
        if (countryFrom.numSoldiers <= weakCountriesThreshold)
            return;

    }
}
