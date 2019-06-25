package IA.Behavior;

import Game.*;
import IA.*;

import java.util.ArrayList;
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
     */
    public void placement() {
        int troopsToPlace = getPlacementTroops();
        ArrayList<Pair<Integer, Country>> borderingArmies = getNotSafeCountries();


        // put troops next to the countries that have the biggest armies
        for (Pair<Integer, Country> pair : borderingArmies) {
            if (troopsToPlace <= 0)
                break;
            int currentArmy = pair.second.numSoldiers;
            if (currentArmy < pair.first) {
                if (troopsToPlace < pair.first) {
                    int armyToPlace = Math.min(pair.first, troopsToPlace);
                    outputState.setCountryArmyVal(getIndex(pair.second), currentArmy + armyToPlace);
                    troopsToPlace -= armyToPlace;
                    move.placementList.add(new Pair<>(armyToPlace, pair.second));
                } else {
                    outputState.setCountryArmyVal(getIndex(pair.second), currentArmy + troopsToPlace);
                }
            }
        }

        //put remaining troops to a pseudorandom country
        if (troopsToPlace > 0 && borderingArmies.size() > 0) {
            int currentArmy = borderingArmies.get(0).second.numSoldiers;
            move.placementList.add(new Pair<>(troopsToPlace, borderingArmies.get(0).second));
            outputState.setCountryArmyVal(getIndex(borderingArmies.get(0).second), currentArmy + troopsToPlace);
            troopsToPlace = 0;
        }
    }

    /**
     * get the real index of a country based on its name
     */
    private int getIndex(Country country1) {
        for (int i = 0; i != outputState.getCountryList().size(); i++)
            if (country1.getName().equals(outputState.getCountryList().get(i).getName())) {
                return i;
        }
        return 9999; //the country is not found
    }

    /**
     * return a list of the player countries that have common border with enemy countries, and the total armies that are bordering them (in a croissant sort)
     * @return list of pair<country, surroundingArmies>
     */
    private ArrayList<Pair<Integer, Country>> getNotSafeCountries() {
        //get bordering countries and corresponding armies
        ArrayList<Pair<Integer, Country>> borderingArmies = new ArrayList<>();
        for (Country country1 : inputState.getCountryList()) {
            if (getOwner(country1) == player) {
                int borderingArmy = 0;
                for (Country country2 : country1.getAdjacentCountries()) {
                    if (getOwner(country2) != player) {
                        borderingArmy += outputState.getCountryArmyList().get(getIndex(country2));
                    }
                }
                borderingArmies.add(new Pair<>(borderingArmy, country1));
            }
        }
        QuickSortForPair qs = new QuickSortForPair(borderingArmies);
        borderingArmies = qs.getSortedArray();
        return borderingArmies;
    }


    /*
    Attaquer un pays parmi les pays en bordure des nôtres appartenant à un continent possédé entièrement par l’ennemi
     */
    public void attack() {
        ArrayList<Triple<Country, Country, Integer>> AttackeableCountries = getAdjacentEnemyCountries(weakCountriesThreshold);

        //take all the countries possible that make the ennemy have continents
        for (Triple<Country, Country, Integer> countryTriple : AttackeableCountries) {
            Country enemyCountry = countryTriple.second;
            if (isACompleteContinent(enemyCountry)) {
                takeCountry(countryTriple.first, enemyCountry);
            }
        }
        attackWeakCountries();
    }

    /**
     * attack weak countries
     * attack the weakest country found, and recursively iterate to update the last of countries attackeable
     */
    private void attackWeakCountries() {
        //if (!haveITheStrengthToAttack)
        //    return;
        for (int i = 0; i < 20; i++) { //on code salement et on aime ça
            ArrayList<Triple<Country, Country, Integer>> attackeableCountries = getAdjacentEnemyCountries(weakCountriesThreshold);
            Triple<Country, Country, Integer> countryTriple = attackeableCountries.get(i);
            takeCountry(countryTriple.first, countryTriple.second);
            //attackWeakCountries();
        }
    }

    /**
     * @todo everything
     */
    public void reinforcement() {}


    public Pair<GameState, Move> getActions() {
        return new Pair<>(outputState, move);}

    /**
     * get a croissant sorted list of all the countries adjacent to our countries
     * @param maxArmyPositioned filter out enemy countries than have more than given parameter
     * @return list of croissant sorted list of enemy countries
     */
    private ArrayList<Triple<Country, Country, Integer>> getAdjacentEnemyCountries(int maxArmyPositioned) {
        ArrayList<Triple<Country, Country, Integer>> adjacentCountryList = new ArrayList<>();
        for (Country country1 : outputState.getCountryList()) {
            if (getOwner(country1) == player) {
                for (Country country2 : country1.getAdjacentCountries()) {
                    if (getOwner(country2) != player) {
                        int countryArmy = country2.numSoldiers;
                        if (countryArmy <= maxArmyPositioned) {
                            adjacentCountryList.add(new Triple<>(country1, country2, countryArmy));
                        }
                    }
                }
            }
        }
        adjacentCountryList = Misc.removeDuplicates(adjacentCountryList);
        QuickSortForTriple qs = new QuickSortForTriple(adjacentCountryList);
        adjacentCountryList = qs.getSortedArray();

        return adjacentCountryList;
    }


    /**
     * Is the continent of a given country owned by only the player owning the country as well
     */
    private boolean isACompleteContinent(Country country) {
        Set<Country> continent = country.getContinent();
        for (Country country2 : continent) {
            if (getOwner(country2) != getOwner(country))
                return false;
        }
        return true;
    }

    private int getTotalAdjacentEnnemyArmies(Country country) {
        int adjacentArmies = 0;
        for (Country country2 : country.getAdjacentCountries()) {
            if (getOwner(country2) != player)
                adjacentArmies += outputState.getCountryArmyList().get(getIndex(country2));
        }
        return adjacentArmies;
    }

    private Player getOwner(Country c) {
        return outputState.getCountryOwnerList().get(getIndex(c));
    }

    private void takeCountry(Country countryFrom, Country countryTo) {
        if (countryFrom.numSoldiers >= weakCountriesThreshold && countryFrom.numSoldiers > 1) { // base case
            //estimates the average number of troops lost to take a country
            int approximateLoss = (int) Math.ceil(countryTo.numSoldiers / Misc.threeVTwo);
            if (approximateLoss < countryFrom.numSoldiers) {
                outputState.setCountryArmyVal(getIndex(countryFrom), outputState.getCountryArmyList().get(getIndex(countryFrom)) - approximateLoss);
                outputState.setCountryOwnerVal(getIndex(countryFrom), player);

                replaceTroops(countryFrom, countryTo);
            }
        }
    }

    private void replaceTroops(Country countryFrom, Country countryTo) {
        int troopsToPlace = countryFrom.numSoldiers - 1;
        if (countryFrom.numSoldiers - 1 > getTotalAdjacentEnnemyArmies(countryTo)) {
            move.attackList.add(new Triple<>(countryFrom, countryTo, countryTo));
            outputState.setCountryArmyVal(getIndex(countryFrom), 1);
            outputState.setCountryArmyVal(getIndex(countryTo), troopsToPlace);
        }
        else {
            move.attackList.add(new Triple<>(countryFrom, countryTo, countryFrom));
            outputState.setCountryArmyVal(getIndex(countryTo), 1);
            outputState.setCountryArmyVal(getIndex(countryFrom), troopsToPlace);
        }
    }
}
