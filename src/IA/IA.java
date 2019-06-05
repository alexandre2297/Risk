package IA;

import Game.Board;
import Game.Player;

import java.util.Collections;

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

    }

    private int getContinentBonuses(GameState state, Player player) {
        int bonus = 0;
        bonus += getNorthAmericaBonus(state, player);
        bonus += getSouthAmericaBonus(state, player);
        bonus += getEuropeBonus(state, player);
        bonus += getAsiaBonus(state, player);
        bonus += getAustraliaBonus(state, player);
        bonus += getAfricaBonus(state, player);
        return bonus;
    }

    private int getNorthAmericaBonus(GameState state, Player player) {
        boolean isContinentOwned = true;
        for (int i = 0; i < 9; i++) {
            if (state.getCountryOwnerList().get(i) != player)
                isContinentOwned = false;
        }
        return isContinentOwned ? 5 : 0;
    }

    private int getSouthAmericaBonus(GameState state, Player player) {
        boolean isContinentOwned = true;
        for (int i = 9; i < 13; i++) {
            if (state.getCountryOwnerList().get(i) != player)
                isContinentOwned = false;
        }
        return isContinentOwned ? 2 : 0;
    }

    private int getEuropeBonus(GameState state, Player player) {
        boolean isContinentOwned = true;
        for (int i = 13; i < 20; i++) {
            if (state.getCountryOwnerList().get(i) != player)
                isContinentOwned = false;
        }
        return isContinentOwned ? 5 : 0;
    }

    private int getAfricaBonus(GameState state, Player player) {
        boolean isContinentOwned = true;
        for (int i = 20; i < 26; i++) {
            if (state.getCountryOwnerList().get(i) != player)
                isContinentOwned = false;
        }
        return isContinentOwned ? 3 : 0;
    }

    private int getAsiaBonus(GameState state, Player player) {
        boolean isContinentOwned = true;
        for (int i = 26; i < 38; i++) {
            if (state.getCountryOwnerList().get(i) != player)
                isContinentOwned = false;
        }
        return isContinentOwned ? 7 : 0;
    }

    private int getAustraliaBonus(GameState state, Player player) {
        boolean isContinentOwned = true;
        for (int i = 38; i < 42; i++) {
            if (state.getCountryOwnerList().get(i) != player)
                isContinentOwned = false;
        }
        return isContinentOwned ? 2 : 0;
    }

    /**
     * apply a heuristic to a State object to determine how advantageous the situation is for a given player
     * @todo
     * @param state state the game is in
     * @param player player for whom apply the heuristic // should be a Player variable but for a 2 player implementation its just whether we play or not
     * @return heuristic calculated
     */
    private int evaluateState(GameState state, Player player) {
        int myCountries = Collections.frequency(state.getCountryOwnerList(), player);
        double myCountryBonus = Math.max((double) myCountries / 3, 3.0);
        double theirCountryBonus = Math.max((double) (state.getCountryOwnerList().size() - myCountries) / 3, 3.0);

        double myContinentBonus = (double) getContinentBonuses(state, this);
        double theirContinentBonus = 0;
        for (Player enemyPlayer : board.getPlayers()) {
            if (player == enemyPlayer)
                continue;
            theirContinentBonus += (double) getContinentBonuses(state, player);
        }
        double a = 1.0;
        double b = 1.0;
        double c = 1.0;
        double d = 1.0;

        double heuristic = a * myCountryBonus + b * myContinentBonus - c * theirCountryBonus - d * theirContinentBonus;
        return (int) Math.round(heuristic * 10);
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
        int index = 0;
        for (int i = 0; i != board.getPlayers().length; i++) {
            if (board.getPlayers()[i] == this)
                index = i;
        }

        Pair<Integer, Move> result = this.minimaxRecursive(state, 0, index,
                                                            Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth);
        return result.second;
    }

    /**
     *
     * @param state state of the game
     * @param depth current depth
     * @param playerIndex player index found in board.getPlayers()
     * @param alpha current value for alpha
     * @param beta current value for beta
     * @param maxDepth maximum recursion depth
     * @return best heuristic found, first move to get to the state which has the best heuristic found
     */
    private Pair<Integer, Move> minimaxRecursive(GameState state, int depth, int playerIndex,
                                                 int alpha, int beta, int maxDepth) {
        Player player = board.getPlayers()[playerIndex];

        // Terminating condition (max depth is reached)
        if (depth == maxDepth)
            return new Pair<Integer, Move>(evaluateState(state, player), null);

        int nextPlayerIndex = playerIndex + 1 % board.getPlayers().length;
        //later on we can change this with a "for each player: if player == ourPlayer : else: "
        if (player == this) {
            int best = Integer.MIN_VALUE;

            Move bestMovePossible = null;
            // Recur for every strategy
            for (int i = 0; i < strategies.length; i++) {
                Pair<GameState, Move> turnPlayed = simPlayTurn(state, strategies[i], this);
                Pair<Integer, Move> result = minimaxRecursive(turnPlayed.first, depth + 1, nextPlayerIndex,
                                                              alpha, beta, maxDepth);
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
                Pair<Integer, Move> result = minimaxRecursive(turnPlayed.first, depth + 1,nextPlayerIndex,
                                                              alpha, beta, maxDepth);
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
