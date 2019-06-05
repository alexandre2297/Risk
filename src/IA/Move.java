package IA;

import Game.Country;

import java.util.ArrayList;
import java.util.List;

public class Move {
    public List<Pair<Integer,Country>> placementList;
    public List<Triple<Integer,Country,Country>> attackList;
    public List<Triple<Integer,Country,Country>> renforcementList;

    public Move() {
        placementList = new ArrayList<>();
        attackList = new ArrayList<>();
        renforcementList = new ArrayList<>();
    }

}
