package com.company;

import java.awt.*;

public class Tile {
    // A tile holds two stars, one for the color and one for the white center
    // It also holds an ID for its color
    public Star outer, inner;
    public TileColor id;
    public int[] loc;
    public Tile(int row, int col, TileColor id) {
        loc = new int[] {row, col};
        this.id = id;
        Color c = decodeColor(id);
        int size = id == TileColor.BLACK ? 120 : 110;
        outer = new Star(50 + col * 100 + 50 * (row % 2) + 50, 50 + row * 100 + 50, size, c);
        inner = new Star(50 + col * 100 + 50 * (row % 2) + 50, 50 + row * 100 + 50, 70, Color.WHITE);
    }

    public static Color decodeColor(TileColor c) {
        switch (c) {
            case RED -> {return new Color(220, 10, 40);}
            case ORANGE -> {return new Color(240, 140, 80);}
            case YELLOW -> {return new Color(250, 200, 80);}
            case GREEN -> {return new Color(170, 190, 80);}
            case BLUE -> {return new Color(0, 140, 210);}
            case PURPLE -> {return new Color(140, 75, 150);}
            case PINK -> {return new Color(220, 110, 170);}
            case BLACK -> {return new Color(40, 40, 40);}
        }
        return null;
    }

    public void draw(Graphics g) {
        outer.draw(g);
        if (id != TileColor.BLACK) {
            inner.draw(g);
        }
    }
}
