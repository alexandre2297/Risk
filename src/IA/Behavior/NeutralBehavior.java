package IA.Behavior;

import Game.Country;
import Game.Player;
import IA.GameState;
import IA.Move;
import IA.Pair;
import IA.Triple;

import java.util.List;

public class NeutralBehavior implements Behavior {

    private GameState inputState;
    private Player player;
    private Move move = new Move();
    private GameState outputState = new GameState(inputState.getBoard());

    public NeutralBehavior(GameState state, Player player) {
        this.inputState = state;
        this.player = player;
    }

    public void placement() {}
    public void attack() {}
    public void reinforcement() {}
    public Pair<GameState, Move> getActions() {return null;}
}

