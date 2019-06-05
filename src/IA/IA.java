package IA;

import Game.Board;
import Game.Country;
import Game.Player;

import java.util.List;

public class IA extends Player {

    private Board board;

    public IA(Board board) {
        this.board = board;
    }

    /**
     * Simulate a turn played by a player and return the new state of the game and the moveset to get there
     * @todo
     * @param state input state to apply the simulation on
     * @param strategy strategy used by the player
     * @param player player that should play. NOTE that for a 2 players implementation, this field iàs null when this is the opponent's turn
     * @return return the new state of the game and the moveset to get there
     */
    public Pair<GameState, Move> simPlayTurn(GameState state, Strategy strategy, Player player) {
        return null;
    }

    public void play() {
        Move moveToPlay = minimax(new GameState(board), 10);
        playMove(moveToPlay);
    }

    /**
     * Actually play the moveset recorded in the Move object by emulating the clicks on countries and buttons.
     * @todo
     * @param move move to play
     */
    private void playMove(Move move) {
        playPlacements(move.placementList);
        if(playAttacks(move.attackList)) {
            board.getRules().nextButtonIsPushed();
            playRenforcements(move.renforcementList);
        }
        board.getRules().nextButtonIsPushed();
    }

    private void playPlacements(List<Pair<Integer,Country>> placements) {
        ClickSimulator clickRobot = ClickSimulator.getInstance();
        for (Pair<Integer,Country> placement : placements) {
            for(int i=0;i<placement.first;i++) {
                clickRobot.clickOnCountry(placement.second);
            }
        }
    }

    private boolean playAttacks(List<Triple<Integer,Country,Country>> attacks) {
        ClickSimulator clickRobot = ClickSimulator.getInstance();
        for (Triple<Integer,Country,Country> attack : attacks) {
            clickRobot.clickOnCountry(attack.second);
            for(int i=0;i<attack.first;i++) {
                clickRobot.clickOnCountry(attack.third);
                if(attackIsToRisky(attack.second,attack.third)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean attackIsToRisky(Country owner, Country enemy) {
        if(owner.numSoldiers / 3 > enemy.numSoldiers) {
            return false;
        }
        return true;
    }

    private void playRenforcements(List<Triple<Integer,Country,Country>> renforcements) {
        ClickSimulator clickRobot = ClickSimulator.getInstance();
        for (Triple<Integer,Country,Country> renforcement : renforcements) {
            clickRobot.clickOnCountry(renforcement.second);
            for(int i=0;i<renforcement.first;i++) {
                clickRobot.clickOnCountry(renforcement.third);
            }
        }
    }

    /**
     * apply a heuristic to a State object to determine how advantageous the situation is for a given player
     * @todo
     * @param state state the game is in
     * @param player player for whom apply the heuristic // should be a Player variable but for a 2 player implementation its just whether we play or not
     * @return heuristic calculated
     */
    private int evaluateState(GameState state, boolean player) {
        return 0;
    }

    private enum Strategy {
        DEFENSIVE,
        AGGRESSIVE,
        NEUTRAL
    }

    private final Strategy[] strategies = Strategy.values();

    /**
     * lance le minimax de base
     * @param maxDepth profondeur maximale recherchée
     * @return best heuristic found
     */
    private Move minimax(GameState state, int maxDepth) {
        Pair<Integer, Move> result = this.minimaxRecursive(state, 0, true,
                                                            Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth);
        return result.second;
    }

    /**
     *
     * @param state state of the game
     * @param depth current depth
     * @param maximizingPlayer is it our turn?
     * @param alpha current value for alpha
     * @param beta current value for beta
     * @param maxDepth maximum recursion depth
     * @return best heuristic found, first move to get to the state which has the best heuristic found
     */
    private Pair<Integer, Move> minimaxRecursive(GameState state, int depth, Boolean maximizingPlayer,
                                                 int alpha, int beta, int maxDepth) {

        // Terminating condition (max depth is reached)
        if (depth == maxDepth)
            return new Pair<Integer, Move>(evaluateState(state, maximizingPlayer), null);

        //later on we can change this with a "for each player: if player == ourPlayer : else: "
        if (maximizingPlayer) {
            int best = Integer.MIN_VALUE;

            Move bestMovePossible = null;
            // Recur for every strategy
            for (int i = 0; i < strategies.length; i++) {
                Pair<GameState, Move> turnPlayed = simPlayTurn(state, strategies[i], this);
                Pair<Integer, Move> result = minimaxRecursive(turnPlayed.first, depth + 1,false, alpha, beta, maxDepth);
                int val = result.first;
                best = Math.max(best, val);
                alpha = Math.max(alpha, best);

                if (val == best)
                    bestMovePossible = turnPlayed.second;

                if (beta <= alpha) // Alpha Beta Pruning
                    break;
            }
            if (depth != 0)
                return new Pair<Integer, Move>(best, null);

            return new Pair<>(best, bestMovePossible);
        }
        else {
            int best = Integer.MAX_VALUE;

            // Recur for every strategy
            for (int i = 0; i < strategies.length; i++) {
                Pair<GameState, Move> turnPlayed = simPlayTurn(state, strategies[i], null);
                Pair<Integer, Move> result = minimaxRecursive(turnPlayed.first, depth + 1,true, alpha, beta, maxDepth);
                int val = result.first;
                best = Math.min(best, val);
                beta = Math.min(beta, best);

                if (beta <= alpha) // Alpha Beta Pruning
                    break;
            }
            return new Pair<Integer, Move>(best, null);
        }
    }
}
