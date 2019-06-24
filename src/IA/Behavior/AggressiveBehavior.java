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
                    //outputState.setCountryArmyVal(countryIndex, currentArmy + pair.first);
                    int armyToPlace = Math.min(pair.first, troopsToPlace);
                    pair.second.numSoldiers = currentArmy + armyToPlace;
                    troopsToPlace -= armyToPlace;
                    move.placementList.add(new Pair<>(armyToPlace, pair.second));
                } else {
                    pair.second.numSoldiers = currentArmy + troopsToPlace;
                    //outputState.setCountryArmyVal(countryIndex, currentArmy + troopsToPlace);
                }
            }
        }

        //put remaining troops to a pseudorandom country
        if (troopsToPlace > 0 && borderingArmies.size() > 0) {
            //int countryIndex = inputState.getCountryList().indexOf(borderingArmies.get(0).second);
            int currentArmy = borderingArmies.get(0).second.numSoldiers;
            //outputState.setCountryArmyVal(countryIndex, currentArmy + troopsToPlace);
            borderingArmies.get(0).second.numSoldiers = currentArmy + troopsToPlace;
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
                        borderingArmy += country2.numSoldiers;
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

    //si au moins un de nos pays a plus de 5 troupes
    private boolean haveITheStrengthToAttack(int threshold) {
        for (Country country1 : outputState.getCountryList()) {
            if (country1.getOwner() == player && country1.numSoldiers > threshold) {
                for (Country country2 : country1.adjacentCountries) {
                    if (country2.getOwner() != player)
                        return true;
                }
            }
        }
        return false;
    }


    /**
     * @todo everything
     */
    public void reinforcement() {}


    public Pair<GameState, Move> getActions() {
        System.out.println(move);
        return new Pair<>(outputState, move);}

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
                        int countryArmy = country2.numSoldiers;
                        if (countryArmy <= maxArmyPositioned) {
                            adjacentCountryList.add(new Triple<>(country1, country2, countryArmy));
                        }
                    }
                }
            }
        }
        adjacentCountryList = Misc.removeDuplicates(adjacentCountryList);
        //Collections.sort(adjacentCountryList, Collections.reverseOrder());
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
            if (country2.getOwner() != country.getOwner())
                return false;
        }
        return true;
    }

    private int getTotalAdjacentEnnemyArmies(Country country) {
        int adjacentArmies = 0;
        for (Country country2 : country.adjacentCountries) {
            if (country2.getOwner() != player)
                adjacentArmies += country2.numSoldiers;
        }
        return adjacentArmies;
    }

    private void takeCountry(Country countryFrom, Country countryTo) {
        if (countryFrom.numSoldiers >= weakCountriesThreshold && countryFrom.numSoldiers > 1) { // base case
            //estimates the average number of troops lost to take a country
            int approximateLoss = (int) Math.ceil(countryTo.numSoldiers / Misc.threeVTwo);
            if (approximateLoss < countryFrom.numSoldiers) {
                countryFrom.numSoldiers -= approximateLoss;
                countryTo.setOwner(player);
                //outputState.setCountryOwnerVal(outputState.getCountryList().indexOf(countryTo), player);
                replaceTroops(countryFrom, countryTo);
            }
        }
    }

    private void replaceTroops(Country countryFrom, Country countryTo) {
        int troopsToPlace = countryFrom.numSoldiers - 1;
        if (countryFrom.numSoldiers - 1 > getTotalAdjacentEnnemyArmies(countryTo)) {
            move.attackList.add(new Triple<>(countryFrom, countryTo, countryTo));
            countryFrom.numSoldiers = 1;
            countryTo.numSoldiers = troopsToPlace;
        }
        else {
            move.attackList.add(new Triple<>(countryFrom, countryTo, countryFrom));
            countryTo.numSoldiers = 1;
            countryFrom.numSoldiers = troopsToPlace;
        }
    }
}
