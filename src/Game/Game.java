package Game;

import IA.ClickSimulator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class Game implements Runnable {

    public void run() {

        final JFrame frame = new JFrame("Risk");
        final Start startScreen = new Start();
        frame.add(startScreen);
        startScreen.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int numPlayers = startScreen.selectPlayers(e);
                if (numPlayers != -1) {
                    startScreen.setVisible(false);
                    startScreen.setEnabled(false);
                    initializeGame(frame, numPlayers);
                }
            }

        });

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);    

    }

    private static void initializeGame(JFrame frame, int numPlayers) {

        final JPanel turnPanel = new JPanel();
        final JLabel turnInfo = new JLabel();
        turnPanel.add(turnInfo);

        final JPanel statusPanel = new JPanel();
        statusPanel.setPreferredSize(new Dimension(120, 0));
        statusPanel.setBackground(new Color(170,170,170));

        Dice diceInfo = new Dice();
        JLabel bonus = new JLabel("Bonus : 0");
        final Board board = new Board(turnInfo, bonus, diceInfo, numPlayers);
        frame.add(board, BorderLayout.CENTER);

        final JButton next = new JButton("Next action");
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.getRules().next();
            }
        });
        statusPanel.add(bonus);
        statusPanel.add(diceInfo);
        statusPanel.add(next);
        frame.add(statusPanel, BorderLayout.WEST);
        frame.add(turnPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        frame.revalidate();

        ClickSimulator clickSimulator = ClickSimulator.getInstance();
        clickSimulator.setCountries(board.countries);

        Point framePosition = new Point(frame.getX(),frame.getY()+30);
        Point boardPosition = new Point(board.getX(),board.getY());
        clickSimulator.setPosition(boardPosition,framePosition);
        clickSimulator.clickOnCountry(board.countries[0]);


    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

}
