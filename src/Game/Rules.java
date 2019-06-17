package Game;

import Modes.*;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

public class Rules {

    private Board board;

    public Rules(Board board) {
        this.board = board;
    }

    /* ends the game if only one player is remaining
     */
    private void checkWin() {
        int numDead = 0;
        for (Player p : board.getPlayers()) {
            if (p.dead) {
                numDead++;
            }
        }
        if (numDead == board.getPlayers().length - 1) {
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

        for (Country c : board.getPlayers()[board.getTurn()].countriesOwned) {
            if (! c.inBounds(mouse)) { continue; }

            if (isRightClick && c.numSoldiers != 1) {
                c.numSoldiers--;
                board.setTroopsToPlace(board.getTroopsToPlace() + 1);
            } else if (! isRightClick) {
                c.numSoldiers++;
                board.setTroopsToPlace(board.getTroopsToPlace() - 1);
            }
        }
        if (board.getTroopsToPlace() != 1) { return; }

        if (board.getMode() instanceof InitialPlacingMode){
            board.setTurn(board.getTurn() + 1);
            if (board.getTurn() == board.getPlayers().length) {
                board.setTurn(0);
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

        int countriesOwned = board.getPlayers()[board.getTurn()].countriesOwned.size();
        board.setTroopsToPlace(40 - countriesOwned - (board.getPlayers().length - 2) * 5);
    }


    /* return true if current player owns the continent, false otherwise
     * @param continent index for continent
     */
    private boolean continentOwned(int continent, Player player) {
        for (Country c : board.getContinents().get(continent)) {
            if (!player.countriesOwned.contains(c)) {
                return false;
            }
        }
        return true;
    }

    /* calculate the number of troops a player can place at
     * the beginning of their turn
     */
    private void updateTroopsToPlace() {
        Player player = board.getPlayers()[board.getTurn()];
        int countryBonus = player.countriesOwned.size() / 3;
        board.setTroopsToPlace(Math.max(3, countryBonus));

        for (int i = 0; i < board.getContinentBonuses().length; i++) {
            if (continentOwned(i, player)) {
                board.setTroopsToPlace(board.getTroopsToPlace() + board.getContinentBonuses()[i]);
            }
        }
    }


    /* selects a country and stores it given that the current player owns it
     * @param mouse for the mouse click location
     */
    public void selectOwnerCountry(Point mouse) {
        for (Country c : board.getPlayers()[board.getTurn()].countriesOwned) {
            if (c.inBounds(mouse) && c.numSoldiers > 1) {
                board.setSelectedCountry(c);
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
        if (board.getSelectedCountry().inBounds(mouse)) {
            board.setSelectedCountry(null);
            board.setMode(new AttackFromMode(board));
            return;
        }
        for (Country c : board.getSelectedCountry().adjacentCountries) {
            if (c.inBounds(mouse) && ! board.getPlayers()[board.getTurn()].countriesOwned.contains(c)) {
                board.setSelectedSecondCountry(c);
                attack(board.getSelectedCountry(), board.getSelectedSecondCountry());
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

        int numberDiceAtck = Math.min(3, own.numSoldiers - 1);
        int numberDiceDef = Math.min(2, enemy.numSoldiers);

        Integer[] atkDice = new Integer[numberDiceAtck];
        Integer[] defDice = new Integer[numberDiceDef];

        for (int i = 0; i < numberDiceAtck; i++) {
            atkDice[i] = roll();
        }

        for (int i = 0; i <numberDiceDef; i++) {
            defDice[i] = roll();
        }

        Arrays.sort(atkDice,Collections.reverseOrder());
        Arrays.sort(defDice,Collections.reverseOrder());
        
        if (atkDice[0] > defDice[0]) {
            enemy.numSoldiers--;
        } else {
            own.numSoldiers--;
        }
        if (atkDice[1] != 0 && defDice[1] != 0) {
            if (atkDice[1] > defDice[1]) {
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
        if (board.getSelectedCountry().numSoldiers == 1) {
            board.setSelectedCountry(null);
            board.setSelectedSecondCountry(null);
            board.setMode(new AttackFromMode(board));
            return;
        }
        if (board.getSelectedSecondCountry().numSoldiers < 1) {
            board.setMode(new NewCountryMode(board));
            conquer();
        }
    }

    public void keepAttacking(Point mouse) {

        // unselect the country to attack from
        if (board.getSelectedCountry().inBounds(mouse)) {
            board.setSelectedCountry(null);
            board.setSelectedSecondCountry(null);
            board.setMode(new AttackFromMode(board));
            return;
        }

        if (board.getSelectedSecondCountry().inBounds(mouse)) {
            attack(board.getSelectedCountry(), board.getSelectedSecondCountry());
            checkOutcome();
        }

    }

    /* takes all the troops remaining after a conquest and allow them to be placed
     */
    private void conquer() {
        Player enemy = null;
        Country country = board.getSelectedSecondCountry();
        for (Player p : board.getPlayers()) {
            if (p.countriesOwned.contains(country)) {
                enemy = p;
            }
        }
        enemy.countriesOwned.remove(country);
        Player player = board.getPlayers()[board.getTurn()];
        player.countriesOwned.add(country);
        country.setOwner(player);


        if (enemy.countriesOwned.isEmpty()) {
            enemy.dead = true;
           /* for (int i = 0; i < enemy.cards.length; i++) {
                players[turn].cards[i] += enemy.cards[i];
            }*/
        }
        checkWin();
        board.getSelectedSecondCountry().numSoldiers = 1;
        board.setTroopsToPlace(board.getSelectedCountry().numSoldiers - 2);
        board.getSelectedCountry().numSoldiers = 1;

        // deal with edge case where there are no remaining soldiers right after a conquest
        if (board.getTroopsToPlace() == 0) {
            board.setSelectedCountry(null);
            board.setSelectedSecondCountry(null);
            board.setMode(board.getMode().nextMode());
        }
    }

    /* place a soldier in a newly conquered country
     * if there are no more soldiers, move on to the next mode
     * @param mouse for the mouse click location
     */
    public void placeSoldierNewCountry(Point mouse) {

        if (board.getSelectedCountry().inBounds(mouse)) {
            board.setTroopsToPlace(board.getTroopsToPlace() - 1);
            board.getSelectedCountry().numSoldiers++;
        }
        if (board.getSelectedSecondCountry().inBounds(mouse)) {
            board.setTroopsToPlace(board.getTroopsToPlace() - 1);
            board.getSelectedSecondCountry().numSoldiers++;
        }

        if (board.getTroopsToPlace() == 0) {
            board.setSelectedCountry(null);
            board.setSelectedSecondCountry(null);
            board.setMode(board.getMode().nextMode());
        }
    }

    /* fortifies a soldier from one country to another given that they are adjacent
     * @param mouse for the mouse click location
     */
    public void selectFortify(Point mouse) {
        if (board.getSelectedCountry().inBounds(mouse)) {
            board.setSelectedCountry(null);
            board.setMode(new FortifyFromMode(board));
            return;
        }

        for (Country c : board.getSelectedCountry().adjacentCountries) {
            if (c.inBounds(mouse) && board.getPlayers()[board.getTurn()].countriesOwned.contains(c)) {
                board.setSelectedSecondCountry(c);
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
        board.getSelectedCountry().numSoldiers--;
        board.getSelectedSecondCountry().numSoldiers++;

        // immediately switch to next mode if no longer possible to fortify
        if (board.getSelectedCountry().numSoldiers == 1) {
            board.setMode(board.getMode().nextMode());
        }
    }


    /* Allows the player to move on to the next phase of the game
     * This function is used by the Next button
     */
    public void nextButtonIsPushed() {
        System.out.println("allo");
        board.getMode().nextButtonIsPushed();
        board.getTurnInfo().setText(board.getMode().getStringForMode());
        board.repaint();
    }

    /* increments the turn to the next living player and resets all of the
     * board state information to the current player
     */
    public void nextPlayer() {
        board.setSelectedCountry(null);
        board.setSelectedSecondCountry(null);

        board.setTurn((board.getTurn() + 1) % board.getPlayers().length);
        while (board.getPlayers()[board.getTurn()].dead) {
            board.setTurn((board.getTurn() + 1) % board.getPlayers().length);
        }


        board.setMode(new PlacingMode (board));
        int bonus = 0;
        Player player = board.getPlayers()[board.getTurn()];

        for (int i = 0; i < board.getContinentBonuses().length; i++) {
            if (continentOwned(i, player)) {
                bonus += board.getContinentBonuses()[i];
            }
        }
        board.getBonusInfo().setText("Bonus : " + bonus);

        updateTroopsToPlace();


    }
}
