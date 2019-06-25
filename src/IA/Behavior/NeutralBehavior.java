package IA.Behavior;

import Game.Country;
import Game.Misc;
import Game.Player;
import IA.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.CheckedOutputStream;

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

    private int getPlacementTroops() {
        return IA.getContinentBonuses(inputState, player) + Math.max(IA.getNbCountries(inputState, player) / 3, 3);
    }

    private Country selectRandomCountry() {
        List<Country> countries = countriesPlayer();
        int countryIndex = Misc.RandomInt(0,countries.size()-1);
        return countries.get(countryIndex);
    }

    private List<Country> countriesPlayer() {
        List<Country> countries = new ArrayList<>();
        for(Country c : outputState.getCountryList()) {
            if(c.getOwner()==player) {
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
            if(c.numSoldiers >1) {
                countries.add(c);
            }
        }
        return countries;
    }

    public void placement() {
        int troopsToPlace = getPlacementTroops();
        while(troopsToPlace > 0) {
            Country selectedCountry = selectRandomCountry();
            if(selectedCountry.getOwner() == player) {
                int nbSoldier = Misc.RandomInt(1,troopsToPlace);
                selectedCountry.numSoldiers += nbSoldier;
                move.placementList.add(new Pair<>(nbSoldier,selectedCountry));
                troopsToPlace -= nbSoldier;
            }
        }
    }

    private Country getRandomAdjacent(Country c) {
        int nbTry = Misc.RandomInt(1,3);
        for(int i=0;i<nbTry;i++) {
            int indexAdjacent = Misc.RandomInt(0, c.getAdjacentCountries().size() - 1);
            int indexResearch = 0;
            for (Country adjacent : c.getAdjacentCountries()) {
                if (indexResearch == indexAdjacent) {
                    if (adjacent.getOwner() != player) {
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
        if(countryFrom == null) {
            return ;
        }

        //Select random adjacent CountryFrom
        Country countryTo = getRandomAdjacent(countryFrom);
        if(countryTo!= null) {
            int approximateLoss = (int) Math.ceil(countryTo.numSoldiers / Misc.threeVTwo);
            if (approximateLoss < countryFrom.numSoldiers) {
                countryTo.numSoldiers -= approximateLoss -1;
                countryFrom.numSoldiers = 1;
                countryTo.setOwner(player);

                move.attackList.add(new Triple<>(countryFrom,countryTo,countryTo));
            }
        }
    }
    public void attack() {
        int nbAttack = Misc.RandomInt(1,50);
        for(int i=0;i<nbAttack;i++) {
            simulateAttack();
        }
    }
    public void reinforcement() {}
    public Pair<GameState, Move> getActions() {return new Pair<>(outputState, move);}
}

