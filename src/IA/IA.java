package IA;

import Game.Board;
import Game.Player;

public class IA extends Player {

    private Board board;

    public IA(Board board) {
        this.board = board;
    }

    /**
     * @todo
     * @param state
     * @param strategy
     * @return
     */
    public Pair<GameState, Move> simPlayTurn(GameState state, Strategy strategy) {
        return null;
    }

    public void play() {
        Move moveToPlay = minimax(new GameState(board), 10);
        playMove(moveToPlay);
    }

    /**
     * @todo
     * @param move
     */
    private void playMove(Move move) {

    }

    /**
     * @todo
     * @param state
     * @return
     */
    private int evaluateState(GameState state) {
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
            return new Pair<Integer, Move>(evaluateState(state), null);

        //later on we can change this with a "for each player: if player == ourPlayer : else: "
        if (maximizingPlayer) {
            int best = Integer.MIN_VALUE;

            Move bestMovePossible = null;
            // Recur for every strategy
            for (int i = 0; i < strategies.length; i++) {
                Pair<GameState, Move> turnPlayed = simPlayTurn(state, strategies[i]);
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
                Pair<GameState, Move> turnPlayed = simPlayTurn(state, strategies[i]);
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
