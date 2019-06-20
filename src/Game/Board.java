package Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

import IA.IA;
import Modes.*;


@SuppressWarnings("serial")
public class Board extends JPanel{

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    private Rules rules;
    private List<Set<Country>> continents;
    private final int[] continentBonuses = {5, 2, 5, 3, 7, 2};
    
    Country[] countries;
    public final int BOARD_WIDTH = 900;
    public final int BOARD_HEIGHT = 600;
    public final Color[] colors = {Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN,
        Color.CYAN, Color.MAGENTA};
    
    private int turn = 0;
    
    private int troopsToPlace;
    private Country selectedCountry;
    private Country selectedSecondCountry;
    private final JLabel turnInfo;
    private final JLabel bonusInfo;
    private final Dice diceInfo;
    private Player[] players;

    private Mode mode;

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        System.out.println("changement de Mode");
        System.out.println("ancien mode: " + this.mode);
        this.mode = mode;
        System.out.println("nouveau mode: " + this.mode);

        System.out.println("SelectedCountry: " + getSelectedCountry());
        System.out.println("SelectedSecondCountry: " + getSelectedSecondCountry());
        System.out.println("");
    }

    public JLabel getTurnInfo() {
        return turnInfo;
    }

    public JLabel getBonusInfo() {
        return bonusInfo;
    }

    public Dice getDiceInfo() {
        return diceInfo;
    }


    public List<Set<Country>> getContinents() {
        return continents;
    }

    public void setContinents(List<Set<Country>> continents) {
        this.continents = continents;
    }

    public int[] getContinentBonuses() {
        return continentBonuses;
    }

    public Country[] getCountries() {
        return countries;
    }

    public void setCountries(Country[] countries) {
        this.countries = countries;
    }

    public int getBoardWidth() {
        return BOARD_WIDTH;
    }


    public int getBoardHeight() {
        return BOARD_HEIGHT;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTroopsToPlace() {
        return troopsToPlace;
    }

    public void setTroopsToPlace(int troopsToPlace) {
        this.troopsToPlace = troopsToPlace;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public Country getSelectedSecondCountry() {
        return selectedSecondCountry;
    }

    public void setSelectedSecondCountry(Country selectedSecondCountry) {
        this.selectedSecondCountry = selectedSecondCountry;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }


    public Board(final JLabel turnInfo, JLabel bonus,Dice diceInfo, int numPlayers) {

        this.turnInfo = turnInfo;
        this.diceInfo = diceInfo;
        this.bonusInfo = bonus;
        this.rules = new Rules(this);

        initializeCountries();   
        initializeContinents();
        initializePlayers(numPlayers);
        initialCountryOwners(numPlayers);

        this.mode = new InitialPlacingMode(this);
        rules.initialTroopsToPlace();

        turnInfo.setText(mode.getStringForMode());

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Point mouse = e.getPoint();

                mode.mouseClick(mouse, e.isMetaDown());
                turnInfo.setText(mode.getStringForMode());

                repaint();
            }

        });


    }

    private void initializePlayers(int numPlayers) {
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            if (i == numPlayers - 1) {
                players[i] = new IA(this);
            }
            else {
                players[i] = new Player();
            }
        }
    }


    /* creates the ArrayList of Set<Game.Country> that represents continents
     * necessary for checking continent bonuses
     */
    private void initializeContinents() {

        continents = new ArrayList<Set<Country>>();
        for (int i = 0; i < 6; i++) {
            continents.add(i, new TreeSet<Country>());
        }
        // North America
        Set<Country> thisContinent = continents.get(0);
        for (int i = 0; i < 9; i++) {
            thisContinent.add(countries[i]);
            countries[i].setContinent(thisContinent);
        }

        // South America
        thisContinent = continents.get(1);
        for (int i = 9; i < 13; i++) {
            thisContinent.add(countries[i]);
            countries[i].setContinent(thisContinent);
        }

        // Europe
        thisContinent = continents.get(2);
        for (int i = 13; i < 20; i++) {
            thisContinent.add(countries[i]);
            countries[i].setContinent(thisContinent);
        }

        // Africa
        thisContinent = continents.get(3);
        for (int i = 20; i < 26; i++) {
            thisContinent.add(countries[i]);
            countries[i].setContinent(thisContinent);
        }

        // Asia
        thisContinent = continents.get(4);
        for (int i = 26; i < 38; i++) {
            thisContinent.add(countries[i]);
            countries[i].setContinent(thisContinent);
        }

        // Australia
        thisContinent = continents.get(5);
        for (int i = 38; i < 42; i++) {
            thisContinent.add(countries[i]);
            countries[i].setContinent(thisContinent);
        }
    }

    /* initializes the 42 countries that make up the standard risk map
     */
    private void initializeCountries() {
        countries = new Country[42];
        countries[0] = new Country("Alaska", 30, 30, 80, 60);
        countries[1] = new Country("Alberta", 110, 80, 80, 50);
        countries[2] = new Country("Central America", 140, 210, 65, 60);
        countries[3] = new Country("Eastern United States", 200, 130, 50, 80);
        countries[4] = new Country("Greenland", 280, 20, 80, 60);
        countries[5] = new Country("Northwest Territory", 110, 30, 120, 50);
        countries[6] = new Country("Ontario", 190, 80, 60, 50);
        countries[7] = new Country("Quebec", 250, 90, 50, 70);
        countries[8] = new Country("Western United States", 110, 130, 90, 80);
        countries[9] = new Country("Venezuela", 200, 270, 70, 50);
        countries[10] = new Country("Brazil", 230, 320, 90, 120);
        countries[11] = new Country("Peru", 190, 320, 40, 120);
        countries[12] = new Country("Argentina", 200, 440, 70, 100);
        countries[13] = new Country("Great Britain", 400, 110, 20, 40);
        countries[14] = new Country("Iceland", 370, 90, 20, 20);
        countries[15] = new Country("Northern Europe", 440, 140, 60, 40);
        countries[16] = new Country("Scandinavia", 450, 20, 50, 100);
        countries[17] = new Country("Ukraine", 500, 70, 100, 110);
        countries[18] = new Country("Southern Europe", 440, 180, 80, 40);
        countries[19] = new Country("Western Europe", 380, 160, 60, 40);
        countries[20] = new Country("Madagascar", 540, 400, 20, 40);
        countries[21] = new Country("Egypt", 450, 240, 70, 40);
        countries[22] = new Country("North Africa", 360, 240, 90, 90);
        countries[23] = new Country("East Africa", 450, 280, 80, 110);
        countries[24] = new Country("Congo", 390, 330, 60, 60);
        countries[25] = new Country("South Africa", 420, 390, 80, 110);
        countries[26] = new Country("Middle East", 520, 180, 110, 70);
        countries[27] = new Country("Afghanistan", 600, 110, 70, 70);
        countries[28] = new Country("Ural", 600, 55, 80, 55);
        countries[29] = new Country("India", 630, 180, 60, 100);
        countries[30] = new Country("China", 670, 110, 90, 70);
        countries[31] = new Country("Siberia", 680, 35, 30, 75);
        countries[32] = new Country("Siam", 690, 180, 40, 70);
        countries[33] = new Country("Mongolia", 710, 80, 60, 30);
        countries[34] = new Country("Irkutsk", 710, 55, 60, 25);
        countries[35] = new Country("Yakutsk", 710, 30, 60, 25);
        countries[36] = new Country("Kamchatka", 770, 30, 30, 70);
        countries[37] = new Country("Japan", 810, 80, 20, 40);
        countries[38] = new Country("Indonesia", 730, 300, 30, 30);
        countries[39] = new Country("Western Australia", 740, 360, 60, 70);
        countries[40] = new Country("Eastern Australia", 800, 360, 50, 70);
        countries[41] = new Country("New Guinea", 800, 310, 40, 25);

        countries[0].adjacentCountries = new TreeSet<Country>();
        countries[0].adjacentCountries.add(countries[1]);
        countries[0].adjacentCountries.add(countries[5]);
        countries[0].adjacentCountries.add(countries[36]);

        countries[1].adjacentCountries = new TreeSet<Country>();
        countries[1].adjacentCountries.add(countries[5]);
        countries[1].adjacentCountries.add(countries[6]);
        countries[1].adjacentCountries.add(countries[8]);

        countries[2].adjacentCountries = new TreeSet<Country>();
        countries[2].adjacentCountries.add(countries[3]);
        countries[2].adjacentCountries.add(countries[8]);
        countries[2].adjacentCountries.add(countries[9]);


        countries[3].adjacentCountries = new TreeSet<Country>();
        countries[3].adjacentCountries.add(countries[6]);
        countries[3].adjacentCountries.add(countries[7]);
        countries[3].adjacentCountries.add(countries[8]);

        countries[4].adjacentCountries = new TreeSet<Country>();
        countries[4].adjacentCountries.add(countries[5]);
        countries[4].adjacentCountries.add(countries[6]);
        countries[4].adjacentCountries.add(countries[7]);
        countries[4].adjacentCountries.add(countries[14]);

        countries[5].adjacentCountries = new TreeSet<Country>();
        countries[5].adjacentCountries.add(countries[6]);

        countries[6].adjacentCountries = new TreeSet<Country>();
        countries[6].adjacentCountries.add(countries[7]);
        countries[6].adjacentCountries.add(countries[8]);

        countries[7].adjacentCountries = new TreeSet<Country>();

        countries[8].adjacentCountries = new TreeSet<Country>();

        countries[9].adjacentCountries = new TreeSet<Country>();
        countries[9].adjacentCountries.add(countries[10]);
        countries[9].adjacentCountries.add(countries[11]);

        countries[10].adjacentCountries = new TreeSet<Country>();
        countries[10].adjacentCountries.add(countries[11]);
        countries[10].adjacentCountries.add(countries[12]);
        countries[10].adjacentCountries.add(countries[22]);

        countries[11].adjacentCountries = new TreeSet<Country>();
        countries[11].adjacentCountries.add(countries[12]);

        countries[12].adjacentCountries = new TreeSet<Country>();

        countries[13].adjacentCountries = new TreeSet<Country>();
        countries[13].adjacentCountries.add(countries[14]);
        countries[13].adjacentCountries.add(countries[15]);
        countries[13].adjacentCountries.add(countries[16]);
        countries[13].adjacentCountries.add(countries[19]);

        countries[14].adjacentCountries = new TreeSet<Country>();

        countries[15].adjacentCountries = new TreeSet<Country>();
        countries[15].adjacentCountries.add(countries[16]);
        countries[15].adjacentCountries.add(countries[17]);
        countries[15].adjacentCountries.add(countries[18]);
        countries[15].adjacentCountries.add(countries[19]);

        countries[16].adjacentCountries = new TreeSet<Country>();
        countries[16].adjacentCountries.add(countries[17]);

        countries[17].adjacentCountries = new TreeSet<Country>();
        countries[17].adjacentCountries.add(countries[18]);
        countries[17].adjacentCountries.add(countries[26]);
        countries[17].adjacentCountries.add(countries[27]);
        countries[17].adjacentCountries.add(countries[28]);

        countries[18].adjacentCountries = new TreeSet<Country>();
        countries[18].adjacentCountries.add(countries[19]);
        countries[18].adjacentCountries.add(countries[21]);
        countries[18].adjacentCountries.add(countries[22]);
        countries[18].adjacentCountries.add(countries[26]);

        countries[19].adjacentCountries = new TreeSet<Country>();
        countries[19].adjacentCountries.add(countries[22]);

        countries[20].adjacentCountries = new TreeSet<Country>();
        countries[20].adjacentCountries.add(countries[23]);
        countries[20].adjacentCountries.add(countries[25]);

        countries[21].adjacentCountries = new TreeSet<Country>();
        countries[21].adjacentCountries.add(countries[22]);
        countries[21].adjacentCountries.add(countries[23]);
        countries[21].adjacentCountries.add(countries[26]);

        countries[22].adjacentCountries = new TreeSet<Country>();
        countries[22].adjacentCountries.add(countries[23]);
        countries[22].adjacentCountries.add(countries[24]);

        countries[23].adjacentCountries = new TreeSet<Country>();
        countries[23].adjacentCountries.add(countries[24]);
        countries[23].adjacentCountries.add(countries[25]);
        countries[23].adjacentCountries.add(countries[26]);

        countries[24].adjacentCountries = new TreeSet<Country>();
        countries[24].adjacentCountries.add(countries[25]);

        countries[25].adjacentCountries = new TreeSet<Country>();

        countries[26].adjacentCountries = new TreeSet<Country>();
        countries[26].adjacentCountries.add(countries[27]);
        countries[26].adjacentCountries.add(countries[29]);

        countries[27].adjacentCountries = new TreeSet<Country>();
        countries[27].adjacentCountries.add(countries[28]);
        countries[27].adjacentCountries.add(countries[29]);
        countries[27].adjacentCountries.add(countries[30]);

        countries[28].adjacentCountries = new TreeSet<Country>();
        countries[28].adjacentCountries.add(countries[30]);
        countries[28].adjacentCountries.add(countries[31]);

        countries[29].adjacentCountries = new TreeSet<Country>();
        countries[29].adjacentCountries.add(countries[30]);
        countries[29].adjacentCountries.add(countries[32]);

        countries[30].adjacentCountries = new TreeSet<Country>();
        countries[30].adjacentCountries.add(countries[31]);
        countries[30].adjacentCountries.add(countries[32]);
        countries[30].adjacentCountries.add(countries[33]);

        countries[31].adjacentCountries = new TreeSet<Country>();
        countries[31].adjacentCountries.add(countries[33]);
        countries[31].adjacentCountries.add(countries[34]);
        countries[31].adjacentCountries.add(countries[35]);

        countries[32].adjacentCountries = new TreeSet<Country>();
        countries[32].adjacentCountries.add(countries[38]);

        countries[33].adjacentCountries = new TreeSet<Country>();
        countries[33].adjacentCountries.add(countries[34]);
        countries[33].adjacentCountries.add(countries[36]);
        countries[33].adjacentCountries.add(countries[37]);

        countries[34].adjacentCountries = new TreeSet<Country>();
        countries[34].adjacentCountries.add(countries[35]);
        countries[34].adjacentCountries.add(countries[36]);

        countries[35].adjacentCountries = new TreeSet<Country>();
        countries[35].adjacentCountries.add(countries[36]);

        countries[36].adjacentCountries = new TreeSet<Country>();
        countries[36].adjacentCountries.add(countries[37]);

        countries[37].adjacentCountries = new TreeSet<Country>();

        countries[38].adjacentCountries = new TreeSet<Country>();
        countries[38].adjacentCountries.add(countries[39]);
        countries[38].adjacentCountries.add(countries[41]);

        countries[39].adjacentCountries = new TreeSet<Country>();
        countries[39].adjacentCountries.add(countries[40]);
        countries[39].adjacentCountries.add(countries[41]);

        countries[40].adjacentCountries = new TreeSet<Country>();
        countries[40].adjacentCountries.add(countries[41]);

        countries[41].adjacentCountries = new TreeSet<Country>();

        for (Country c1 : countries) {
            for (Country c2 : c1.adjacentCountries) {
                c2.adjacentCountries.add(c1);
            }
        }

    }


    /* creates a shuffled array of countries
     */
    private Country[] shuffleCountries() {
        Country[] shuffledCountries = countries.clone();
        for (int i = 0; i < countries.length; i++) {
            int j = i + (int) ((countries.length - i) * Math.random());
            Country temp = shuffledCountries[i];
            shuffledCountries[i] = shuffledCountries[j];
            shuffledCountries[j] = temp;
        }
        return shuffledCountries;
    }


    /* iterates through a shuffled array of countries to randomly
     * assign owners to countries
     * @param numPlayers the number of players
     */
    private void initialCountryOwners(int numPlayers) {
        int playerID = 0;
        Country[] shuffledCountries = shuffleCountries();
        for (int i = 0; i < countries.length; i++) {
            players[playerID].countriesOwned.add(shuffledCountries[i]);
            shuffledCountries[i].setOwner(players[playerID]);
            playerID = (playerID + 1) % numPlayers;
        }
    }


    /* draws the connecting lines for countries that are adjacent
     * but not visibly so
     */
    private void drawLines(Graphics g) {
        g.drawLine(0, 60, 30, 60);
        g.drawLine(800, 60, 900, 60);

        g.drawLine(230, 55, 280, 50);
        g.drawLine(250, 80, 280, 50);
        g.drawLine(300, 90, 310, 80);
        g.drawLine(360, 80, 370, 90);
        g.drawLine(390, 95, 410, 110);
        g.drawLine(410, 150, 410, 160);
        g.drawLine(410, 110, 450, 70);
        g.drawLine(420, 130, 440, 140);
        g.drawLine(475, 120, 470, 140);
        g.drawLine(480, 220, 485, 240);
        g.drawLine(440, 220, 405, 240);
        g.drawLine(410, 200, 405, 240);
        g.drawLine(530, 280, 575, 250);
        g.drawLine(530, 390, 540, 400);
        g.drawLine(500, 445, 540, 420);
        g.drawLine(320, 320, 360, 285);
        g.drawLine(810, 120, 770, 110);
        g.drawLine(810, 80, 800, 65);
        g.drawLine(710, 250, 745, 300);
        g.drawLine(760, 315, 800, 323);
        g.drawLine(745, 330, 770, 360);
        g.drawLine(820, 335, 825, 360);
        g.drawLine(770, 360, 800, 335);
    }




    /* updates the text displaying the card status
     */

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(80, 80, 80));
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            for(Country c : player.countriesOwned) {
                g.setColor(colors[i]);

                if (c == selectedCountry || c == selectedSecondCountry) {
                    c.highlight();
                } else {
                    c.unhighlight();
                }
                c.draw(g);   
            }
        }
        drawLines(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

}
