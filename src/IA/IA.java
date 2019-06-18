package IA;

import Game.Board;
import Game.Country;
import Game.Player;
import IA.Behavior.AggressiveBehavior;
import IA.Behavior.Behavior;
import IA.Behavior.DefensiveBehavior;
import IA.Behavior.NeutralBehavior;

import java.util.List;
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
    private Pair<GameState, Move> simPlayTurn(GameState state, Strategy strategy, Player player) {
        Behavior behavior;
        switch (strategy) {
            case DEFENSIVE:
                behavior = new DefensiveBehavior(state , player);
                break;
            case AGGRESSIVE:
                behavior = new AggressiveBehavior(state , player);
                break;
            default:
            case NEUTRAL:
                behavior = new NeutralBehavior(state , player);
                break;
        }

        behavior.placement();
        behavior.attack();
        behavior.reinforcement();
        return behavior.getActions();
    }

    public void play() {
        Move moveToPlay = minimax(new GameState(board), 10);
        //playMove(moveToPlay);
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
            playRenforcements(move.reinforcementList);
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
        return ! (owner.numSoldiers / 3 > enemy.numSoldiers);
    }

    private void playRenforcements(List<Triple<Integer, Country,Country>> reinforcements) {
        ClickSimulator clickRobot = ClickSimulator.getInstance();
        for (Triple<Integer, Country, Country> reinforcement : reinforcements) {
            clickRobot.clickOnCountry(reinforcement.second);
            for (int i=0; i<reinforcement.first; i++) {
                clickRobot.clickOnCountry(reinforcement.third);
            }
        }
    }

    public static int getContinentBonuses(GameState state, Player player) {
        int bonus = 0;
        bonus += getNorthAmericaBonus(state, player);
        bonus += getSouthAmericaBonus(state, player);
        bonus += getEuropeBonus(state, player);
        bonus += getAsiaBonus(state, player);
        bonus += getAustraliaBonus(state, player);
        bonus += getAfricaBonus(state, player);
        return bonus;
    }

    private static int getNorthAmericaBonus(GameState state, Player player) {
        boolean isContinentOwned = true;
        for (int i = 0; i < 9; i++) {
            if (state.getCountryOwnerList().get(i) != player)
                isContinentOwned = false;
        }
        return isContinentOwned ? 5 : 0;
    }

    private static int getSouthAmericaBonus(GameState state, Player player) {
        boolean isContinentOwned = true;
        for (int i = 9; i < 13; i++) {
            if (state.getCountryOwnerList().get(i) != player)
                isContinentOwned = false;
        }
        return isContinentOwned ? 2 : 0;
    }

    private static int getEuropeBonus(GameState state, Player player) {
        boolean isContinentOwned = true;
        for (int i = 13; i < 20; i++) {
            if (state.getCountryOwnerList().get(i) != player)
                isContinentOwned = false;
        }
        return isContinentOwned ? 5 : 0;
    }

    private static int getAfricaBonus(GameState state, Player player) {
        boolean isContinentOwned = true;
        for (int i = 20; i < 26; i++) {
            if (state.getCountryOwnerList().get(i) != player)
                isContinentOwned = false;
        }
        return isContinentOwned ? 3 : 0;
    }

    private static int getAsiaBonus(GameState state, Player player) {
        boolean isContinentOwned = true;
        for (int i = 26; i < 38; i++) {
            if (state.getCountryOwnerList().get(i) != player)
                isContinentOwned = false;
        }
        return isContinentOwned ? 7 : 0;
    }

    private static int getAustraliaBonus(GameState state, Player player) {
        boolean isContinentOwned = true;
        for (int i = 38; i < 42; i++) {
            if (state.getCountryOwnerList().get(i) != player)
                isContinentOwned = false;
        }
        return isContinentOwned ? 2 : 0;
    }

    public static int getNbCountries(GameState state, Player player) {
        return Collections.frequency(state.getCountryOwnerList(), player);
    }

    /**
     * apply a heuristic to a State object to determine how advantageous the situation is for a given player
     * @todo
     * @param state state the game is in
     * @param player player for whom apply the heuristic // should be a Player variable but for a 2 player implementation its just whether we play or not
     * @return heuristic calculated
     */
    private int evaluateState(GameState state, Player player) {
        int myCountries = getNbCountries(state, player);
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
