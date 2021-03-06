package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/* Game.Country object which represents the information pertaining
 * to a country in the game
 */
public class Country implements Comparable<Country>,Cloneable {
    public Set<Country> adjacentCountries;
    private String name;

    public int numSoldiers;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean selected;
    private Player owner = null;
    private Set<Country> continent;

    public Country (String name, int x, int y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        numSoldiers = 1;
    }

    public Set<Country> getContinent() {
        return continent;
    }

    public void setContinent(Set<Country> continent) {
        this.continent = continent;
    }

    public Set<Country> getAdjacentCountries() {
        return adjacentCountries;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
 
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    /* draws the country and displays the number of soldiers
     */
    public void draw(Graphics g) {
        if (selected) {
            Color highlighted = g.getColor().darker();
            g.setColor(highlighted);
        } 
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        int offset = 3;
        if (numSoldiers >= 10) {
            offset = 7;
        }
        g.drawString("" + numSoldiers, x - offset + width / 2, y + 4 + height / 2);
    }

    public void highlight() {
        selected = true;
    }
    
    public void unhighlight() {
        selected = false;
    }
    
    public boolean inBounds(Point mouse) {
        int mouseX = (int) mouse.getX();
        int mouseY = (int) mouse.getY();
        
        if (mouseX < x + 1 || mouseX > x + width - 1) {
            return false;
        }
        if (mouseY < y + 1 || mouseY > y + height - 1) {
            return false;
        }
        return true;
    }
    
    public int compareTo(Country c) {
        return name.compareTo(c.name);
    }


    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }

    /*public Country clone() {
        Country c = new Country(this.name,this.x,this.y,this.width,this.height);
        c.adjacentCountries = new HashSet<>();
        c.continent = new HashSet<>();
        c.numSoldiers = this.numSoldiers;
        c.selected = this.selected;
        c.owner = this.owner;
        for(Country adjacent : this.adjacentCountries) {
            c.adjacentCountries.add(adjacent.clone());
        }
        for(Country country : continent) {
            c.continent.add(country.clone());
        }

        return c;
    }*/


    @Override
    public String toString() {
        return this.name;
    }
}