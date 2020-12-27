package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here

        JFrame frame = new JFrame("IQ Star");
        int[] size = {700 + 100, 400 + 100};
        frame.setSize(size[0], size[1] + 21);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBackground(new Color(40, 60, 90));

        Board b = new Board();

        // Constraints can be added to the initial state of the board
        b.addConstraint(0, 2, TileColor.YELLOW);
        b.addConstraint(0, 4, TileColor.ORANGE);

        boolean verbose = true;

        Window window = new Window(b);
        frame.add(window);

        frame.setVisible(true);

        if (!b.solve(verbose, window)) {
            // Failure can be drawn to window
            b.setFailed();
        }
        window.repaint();
    }
}
