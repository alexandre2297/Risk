package IA;

import Game.Country;

import java.awt.*;
import java.awt.event.InputEvent;

public class ClickSimulator {

    private static ClickSimulator instance;
    private Country[] countries;
    private Robot bot;
    private Point windowPosition;
    private Point boardPosition;

    public static ClickSimulator getInstance() {
        if(instance == null) {
            instance = new ClickSimulator();
        }
        return instance;
    }

    private ClickSimulator() {
        try {
            bot = new Robot();
        }catch(java.awt.AWTException e) {
            System.out.println("Error with creation of click simulator");
        }
    }

    public void setCountries(Country[] countries) {
        this.countries = countries;
    }

    public void setPosition(Point boardPosition,Point windowPosition) {
        this.boardPosition = boardPosition;
        this.windowPosition = windowPosition;
    }

    public void clickOnCountry(Country c) {
        bot.mouseMove(boardPosition.x+windowPosition.x+c.getX()+1,boardPosition.y+windowPosition.y+c.getY()+1);
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
