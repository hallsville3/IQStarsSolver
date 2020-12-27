package com.company;

import javax.swing.*;
import java.awt.*;

public class Window extends JPanel {

    public Board b;
    public Window(Board b) {
        super();
        this.b = b;
    }

    public void paintComponent(Graphics g) {
        g.setColor(new Color(40, 60, 90));
        g.fillRect(0, 0, 10000, 10000);
        b.draw(g);
    }
}
