import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/* Player object which represents the information pertaining to
 * a player in the game
 */
public class Player {
    protected Set<Country> countriesOwned;
    protected boolean dead;

    public Player() {
        countriesOwned = new TreeSet<Country>();
    }
}