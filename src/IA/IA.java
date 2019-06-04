package IA;

import Game.Player;

public class IA extends Player {

public Pair<GameState, Move> simPlayTurn(GameState state, Strategy strategy) {
    }

    public void play(Move move) {

    }


    private enum Strategy {
        DEFENSIVE,
        AGGRESSIVE,
        NEUTRAL
    }

    private final int strategiesLength = Strategy.values().length;

    /* Returns optimal value for current player (Initially called for root and maximizer)
    */

    /**
     * lance le minimax de base
     * @param values liste des heuristiques des feuilles dans l'ordres d'un arbre applati
     * @param maxDepth profondeur maximale recherchée
     * @return best heuristic found
     */
    private Move minimax(int values[], int maxDepth) {
        Pair<Integer, Move> result = this.minimaxRecursive(0, 0, true, values, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth);
        return result.second;
    }

    //a terme le minimax return un pair (val; move) dont move est null sans quand depth == 0
    private int minimaxRecursive(int depth, int nodeIndex, Boolean maximizingPlayer, int values[], int alpha, int beta, int maxDepth) {
        // Terminating condition (leaf node is reached)
        if (depth == maxDepth)
            return values[nodeIndex];

        //later on we can change this with a "for each player: if player == ourPlayer : else: "
        if (maximizingPlayer) {
            int best = Integer.MIN_VALUE;

            // Recur for every strategy
            for (int i = 0; i < strategiesLength; i++) {
                int val = minimaxRecursive(depth + 1, nodeIndex * 2 + i, false, values, alpha, beta, maxDepth);
                best = Math.max(best, val);
                alpha = Math.max(alpha, best);

                if (beta <= alpha) // Alpha Beta Pruning
                    break;
            }
            return best;
        }
        else {
            int best = Integer.MAX_VALUE;

            // Recur for every strategy
            for (int i = 0; i < strategiesLength; i++) {
                int val = minimaxRecursive(depth + 1, nodeIndex * 2 + i, true, values, alpha, beta, maxDepth);
                best = Math.min(best, val);
                beta = Math.min(beta, best);

                if (beta <= alpha) // Alpha Beta Pruning
                    break;
            }
            return best;
        }
    }
}
