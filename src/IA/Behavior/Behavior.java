package IA.Behavior;

import Game.Country;
import Game.Player;
import IA.GameState;
import IA.Move;
import IA.Pair;
import IA.Triple;

import java.util.List;

public interface Behavior {

    void placement();
    void attack();
    void reinforcement();
    Pair<GameState, Move> getActions();

}
