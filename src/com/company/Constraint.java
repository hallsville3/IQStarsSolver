package com.company;

import com.company.Pieces.Piece;

import java.awt.*;

public class Constraint {
    public int row, col;
    public TileColor color;

    public Star outer, inner;
    public Constraint(int row, int col, TileColor color) {
        this.row = row;
        this.col = col;
        this.color = color;

        outer = new Star(50 + col * 100 + 50 * (row % 2) + 50, 50 + row * 100 + 50, 110, Tile.decodeColor(color));
        inner = new Star(50 + col * 100 + 50 * (row % 2) + 50, 50 + row * 100 + 50, 70, Color.WHITE);
    }

    public boolean isSatisfiedBy(Piece p) {
        if (p.color != color) {
            for (Tile t: p.tiles) {
                if (t.loc[0] == row && t.loc[1] == col) {
                    return false; // Can't go here!
                }
            }
            return true;
        }

        for (Tile t: p.tiles) {
            if (t.loc[0] == row && t.loc[1] == col) {
                return true; // Satisfied by one tile at right position
            }
        }

        return false;
    }

    public void draw(Graphics g) {
        outer.draw(g);
        inner.draw(g);
    }
}
