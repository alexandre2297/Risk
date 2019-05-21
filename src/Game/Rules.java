package Game;

import Modes.*;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Rules {

    private Board board;

    public Rules(Board board, JLabel turnInfo, JLabel bonusInfo, Dice diceInfo) {
        this.board = board;
    }

    /* ends the game if only one player is remaining
     */
    private void checkWin() {
        int numDead = 0;
        for (Player p : Board.getPlayers()) {
            if (p.dead) {
                numDead++;
            }
        }
        if (numDead == Board.getPlayers().length - 1) {
            board.setMode(new GameOverMode(board));
            board.getTurnInfo().setText(board.getMode().getStringForMode());
            board.repaint();
        }
    }

    /* places soldier in a country provided the current player owns the country
     * moves on to next mode after all soldiers have been placed
     * @param: mouse for the mouse click location
     */
    public void placeSoldier(Point mouse, boolean isRightClick) {

        for (Country c : Board.getPlayers()[Board.getTurn()].countriesOwned) {
            if (! c.inBounds(mouse)) { continue; }

            if (isRightClick && c.numSoldiers != 1) {
                c.numSoldiers--;
                Board.setTroopsToPlace(Board.getTroopsToPlace() + 1);
            } else if (! isRightClick) {
                c.numSoldiers++;
                Board.setTroopsToPlace(Board.getTroopsToPlace() - 1);
            }
        }
        if (Board.getTroopsToPlace() != 1) { return; }

        if (board.getMode() instanceof InitialPlacingMode){
            Board.setTurn(Board.getTurn() + 1);
            if (Board.getTurn() == Board.getPlayers().length) {
                Board.setTurn(0);
                updateTroopsToPlace();
                board.setMode(board.getMode().nextMode());
                return;
            }
            initialTroopsToPlace();
            return;
        }

        board.setMode(board.getMode().nextMode());
    }

    /* calculates the initial troops for a player to place
     */
    public void initialTroopsToPlace() {

        int countriesOwned = Board.getPlayers()[Board.getTurn()].countriesOwned.size();
        Board.setTroopsToPlace(40 - countriesOwned - (Board.getPlayers().length - 2) * 5);
    }


    /* return true if current player owns the continent, false otherwise
     * @param continent index for continent
     */
    private boolean continentOwned(int continent) {
        for (Country c : Board.getContinents().get(continent)) {
            if (!Board.getPlayers()[Board.getTurn()].countriesOwned.contains(c)) {
                return false;
            }
        }
        return true;
    }

    /* calculates the number of troops a player can place at
     * the beginning of his/her turn
     */
    private void updateTroopsToPlace() {

        int countryBonus = Board.getPlayers()[Board.getTurn()].countriesOwned.size() / 3;
        Board.setTroopsToPlace(Math.max(3, countryBonus));

        for (int i = 0; i < Board.getContinentBonuses().length; i++) {
            if (continentOwned(i)) {
                Board.setTroopsToPlace(Board.getTroopsToPlace() + Board.getContinentBonuses()[i]);
            }
        }
    }


    /* selects a country and stores it given that the current player owns it
     * @param mouse for the mouse click location
     */
    public void selectOwnerCountry(Point mouse) {
        for (Country c : Board.getPlayers()[Board.getTurn()].countriesOwned) {
            if (c.inBounds(mouse) && c.numSoldiers > 1) {
                Board.setSelectedCountry(c);
                board.setMode(board.getMode().nextMode());
                break;
            }
        }
    }

    /* selects a country and stores it given that the current player does not own it
     * @param mouse for the mouse click location
     */
    public void selectEnemyCountry(Point mouse) {

        // unselect the country to attack from
        if (Board.getSelectedCountry().inBounds(mouse)) {
            Board.setSelectedCountry(null);
            board.setMode(new AttackFromMode(board));
            return;
        }
        for (Country c : Board.getSelectedCountry().adjacentCountries) {
            if (c.inBounds(mouse) && ! Board.getPlayers()[Board.getTurn()].countriesOwned.contains(c)) {
                Board.setSelectedSecondCountry(c);
                attack(Board.getSelectedCountry(), Board.getSelectedSecondCountry());
                checkOutcome();
                if (board.getMode() instanceof AttackToMode) {
                    board.setMode(board.getMode().nextMode());
                }
            }
        }
    }


    /* returns a random int from 1-6
     */
    private int roll() {
        return (int) Math.ceil(6 * Math.random());
    }

    /* simulates the dice rolling for an attack
     * number of dice is dependent on available soldiers
     * @param own for the attacking country
     * @param enemy for the defending country
     */
    private void attack(Country own, Country enemy) {
        int[] atkDice = new int[3];
        int[] defDice = new int[2];

        for (int i = 0; i < Math.min(atkDice.length, own.numSoldiers - 1); i++) {
            atkDice[i] = roll();
        }

        for (int i = 0; i < Math.min(defDice.length, enemy.numSoldiers); i++) {
            defDice[i] = roll();
        }

        Arrays.sort(atkDice);
        Arrays.sort(defDice);

        if (atkDice[0] > defDice[0]) {
            enemy.numSoldiers--;
        } else {
            own.numSoldiers--;
        }
        if (atkDice[1] != 0 && defDice[1] != 0) {
            if (atkDice[1] > defDice[1] && atkDice[1] != 0 && defDice[1] != 0) {
                enemy.numSoldiers--;
            } else {
                own.numSoldiers--;
            }
        }

        board.getDiceInfo().dice[0].update(atkDice[0]);
        board.getDiceInfo().dice[1].update(defDice[0]);
        board.getDiceInfo().dice[2].update(atkDice[1]);
        board.getDiceInfo().dice[3].update(defDice[1]);
        board.getDiceInfo().dice[4].update(atkDice[2]);
        board.getDiceInfo().repaint();
    }

    /* checks the number of available soldiers to see if a battle is over
     */
    private void checkOutcome() {
        if (Board.getSelectedCountry().numSoldiers == 1) {
            Board.setSelectedCountry(null);
            Board.setSelectedSecondCountry(null);
            board.setMode(new AttackFromMode(board));
            return;
        }
        if (Board.getSelectedSecondCountry().numSoldiers < 1) {
            board.setMode(new NewCountryMode(board));
            conquer();
        }
    }

    public void keepAttacking(Point mouse) {

        // unselect the country to attack from
        if (Board.getSelectedCountry().inBounds(mouse)) {
            Board.setSelectedCountry(null);
            Board.setSelectedSecondCountry(null);
            board.setMode(new AttackFromMode(board));
            return;
        }

        if (Board.getSelectedSecondCountry().inBounds(mouse)) {
            attack(Board.getSelectedCountry(), Board.getSelectedSecondCountry());
            checkOutcome();
        }

    }

    /* takes all the troops remaining after a conquest and allow them to be placed
     */
    private void conquer() {
        Player enemy = null;
        for (Player p : Board.getPlayers()) {
            if (p.countriesOwned.contains(Board.getSelectedSecondCountry())) {
                enemy = p;
            }
        }
        enemy.countriesOwned.remove(Board.getSelectedSecondCountry());
        Board.getPlayers()[Board.getTurn()].countriesOwned.add(Board.getSelectedSecondCountry());

        if (enemy.countriesOwned.isEmpty()) {
            enemy.dead = true;
           /* for (int i = 0; i < enemy.cards.length; i++) {
                players[turn].cards[i] += enemy.cards[i];
            }*/
        }
        checkWin();
        Board.getSelectedSecondCountry().numSoldiers = 1;
        Board.setTroopsToPlace(Board.getSelectedCountry().numSoldiers - 2);
        Board.getSelectedCountry().numSoldiers = 1;

        // deal with edge case where there are no remaining soldiers right after a conquest
        if (Board.getTroopsToPlace() == 0) {
            Board.setSelectedCountry(null);
            Board.setSelectedSecondCountry(null);
            board.setMode(board.getMode().nextMode());
        }
    }

    /* place a soldier in a newly conquered country
     * if there are no more soldiers, move on to the next mode
     * @param mouse for the mouse click location
     */
    public void placeSoldierNewCountry(Point mouse) {

        if (Board.getSelectedCountry().inBounds(mouse)) {
            Board.setTroopsToPlace(Board.getTroopsToPlace() - 1);
            Board.getSelectedCountry().numSoldiers++;
        }
        if (Board.getSelectedSecondCountry().inBounds(mouse)) {
            Board.setTroopsToPlace(Board.getTroopsToPlace() - 1);
            Board.getSelectedSecondCountry().numSoldiers++;
        }

        if (Board.getTroopsToPlace() == 0) {
            Board.setSelectedCountry(null);
            Board.setSelectedSecondCountry(null);
            board.setMode(board.getMode().nextMode());
        }
    }

    /* fortifies a soldier from one country to another given that they are adjacent
     * @param mouse for the mouse click location
     */
    public void selectFortify(Point mouse) {
        if (Board.getSelectedCountry().inBounds(mouse)) {
            Board.setSelectedCountry(null);
            board.setMode(new FortifyFromMode(board));
            return;
        }

        for (Country c : Board.getSelectedCountry().adjacentCountries) {
            if (c.inBounds(mouse) && Board.getPlayers()[Board.getTurn()].countriesOwned.contains(c)) {
                Board.setSelectedSecondCountry(c);
                fortify();
                board.setMode(board.getMode().nextMode());
                break;
            }
        }
    }

    /* fortifies a soldier from one country to another
     * if there are no more soldiers available, move on to next mode
     */
    public void fortify() {
        Board.getSelectedCountry().numSoldiers--;
        Board.getSelectedSecondCountry().numSoldiers++;

        // immediately switch to next mode if no longer possible to fortify
        if (Board.getSelectedCountry().numSoldiers == 1) {
            board.setMode(board.getMode().nextMode());
        }
    }


    /* Allows the player to move on to the next phase of the game
     * This function is used by the Next button
     */
    public void next() {

        board.getMode().nextButtonIsPushed();
        board.getTurnInfo().setText(board.getMode().getStringForMode());
        board.repaint();
    }

    /* increments the turn to the next living player and resets all of the
     * board state information to the current player
     */
    public void nextPlayer() {
        Board.setSelectedCountry(null);
        Board.setSelectedSecondCountry(null);

        Board.setTurn((Board.getTurn() + 1) % Board.getPlayers().length);
        while (Board.getPlayers()[Board.getTurn()].dead) {
            Board.setTurn((Board.getTurn() + 1) % Board.getPlayers().length);
        }


        board.setMode(new PlacingMode (board));

        int bonus =0;
        for (int i = 0; i < Board.getContinentBonuses().length; i++) {
            if (continentOwned(i)) {
                bonus += Board.getContinentBonuses()[i];
            }
        }
        board.getBonusInfo().setText("Bonus : "+bonus);

        updateTroopsToPlace();


    }
}
