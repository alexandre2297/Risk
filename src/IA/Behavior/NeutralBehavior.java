package IA.Behavior;

import Game.Country;
import Game.Misc;
import Game.Player;
import IA.*;

import java.util.ArrayList;
import java.util.List;

public class NeutralBehavior implements Behavior {

    private GameState inputState;
    private Player player;
    private Move move = new Move();
    private GameState outputState;

    public NeutralBehavior(GameState state, Player player) {
        this.inputState = state;
        this.outputState = new GameState(state);
        this.player = player;
    }

    private Player getOwner(Country c) {
        return outputState.getCountryOwnerList().get(getIndex(c));
    }

    /**
     * get the real index of a country based on its name
     */
    private int getIndex(Country country1) {
        for (int i = 0; i != outputState.getCountryList().size(); i++)
            if (country1.getName().equals(outputState.getCountryList().get(i).getName())) {
                return i;
            }
        throw new IndexOutOfBoundsException(); //the country is not found
    }

    private int getPlacementTroops() {
        return IA.getContinentBonuses(outputState, player) + Math.max(IA.getNbCountries(outputState, player) / 3, 3);
    }

    private Country selectRandomCountry() {
        List<Country> countries = countriesPlayer();
        int countryIndex = Misc.RandomInt(0,countries.size()-1);
        return countries.get(countryIndex);
    }

    private List<Country> countriesPlayer() {
        List<Country> countries = new ArrayList<>();
        for(Country c : outputState.getCountryList()) {
            if(getOwner(c) == player) {
                countries.add(c);
            }
        }
        return countries;
    }

    private Country selectRandomCountryWhoCanAttack() {
        List<Country> countries = countriesWhoCanAttack();
        if(countries.size() == 0) {
            return null;
        }
        int countryIndex = Misc.RandomInt(0,countries.size()-1);
        return countries.get(countryIndex);
    }

    private List<Country> countriesWhoCanAttack() {
        List<Country> countries = new ArrayList<>();
        for(Country c : countriesPlayer()) {
            if(outputState.getCountryArmyList().get(getIndex(c)) > 1) {
                countries.add(c);
            }
        }
        return countries;
    }

    public void placement() {
        int troopsToPlace = getPlacementTroops();
        while(troopsToPlace > 0) {
            Country selectedCountry = selectRandomCountry();
            if(getOwner(selectedCountry) == player) {
                int nbSoldier = Misc.RandomInt(1 ,troopsToPlace);
                outputState.setCountryArmyVal(getIndex(selectedCountry), outputState.getCountryArmyList().get(getIndex(selectedCountry)) + nbSoldier);
                move.placementList.add(new Pair<>(nbSoldier, selectedCountry));
                troopsToPlace -= nbSoldier;
            }
        }
    }

    private Country getRandomAdjacent(Country c) {
        int nbTry = Misc.RandomInt(1,3);
        for(int i=0; i<nbTry; i++) {
            int indexAdjacent = Misc.RandomInt(0, c.getAdjacentCountries().size() - 1);
            int indexResearch = 0;
            for (Country adjacent : c.getAdjacentCountries()) {
                if (indexResearch == indexAdjacent) {
                    if (getOwner(adjacent) != player) {
                        return adjacent;
                    } else {
                        continue;
                    }
                }
                indexResearch++;
            }
        }
        return null;
    }


    public void simulateAttack() {
        //select random countryFrom IA
        Country countryFrom = selectRandomCountryWhoCanAttack();
        if (countryFrom == null) {
            return ;
        }

        //Select random adjacent CountryFrom
        Country countryTo = getRandomAdjacent(countryFrom);
        if(countryTo!= null) {
            int approximateLoss = (int) Math.ceil(countryTo.numSoldiers / Misc.threeVTwo);
            if (approximateLoss < outputState.getCountryArmyList().get(getIndex(countryFrom))) {
                outputState.setCountryArmyVal(getIndex(countryFrom), outputState.getCountryArmyList().get(getIndex(countryFrom)) - approximateLoss + 1);
                outputState.setCountryArmyVal(getIndex(countryTo), 1);
                outputState.setCountryOwnerVal(getIndex(countryTo), player);

                move.attackList.add(new Triple<>(countryFrom,countryTo,countryTo));
            }
        }
    }
    public void attack() {
        int nbAttack = Misc.RandomInt(1,5);
        for(int i = 0; i < nbAttack; i++) {
            simulateAttack();
        }
    }
    public void reinforcement() {}
    public Pair<GameState, Move> getActions() {return new Pair<>(outputState, move);}
}

