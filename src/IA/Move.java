package IA;

import Game.Country;

import java.util.ArrayList;
import java.util.List;

public class Move {
    public List<Pair<Integer,Country>> placementList;
    public List<Triple<Country,Country,Country>> attackList;
    public List<Triple<Integer,Country,Country>> reinforcementList;

    public Move() {
        placementList = new ArrayList<>();
        attackList = new ArrayList<>();
        reinforcementList = new ArrayList<>();
    }

}
