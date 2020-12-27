package com.company.Pieces;

import com.company.Tile;
import com.company.TileColor;

import java.awt.*;

public abstract class Piece {
    // Represents one of the 7 pieces that can be placed on the board
    public int[] pattern;
    public int[] location; // Location of first piece
    public int rotation;
    public Tile[] tiles;
    public TileColor color;

    // rotations are the sequence of clockwise rotations for tiles on a piece
    public int[][] evenRotations = {{0, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};
    public int[][] oddRotations = {{0, 1}, {1, 1}, {1, 0}, {0, -1}, {-1, 0}, {-1, 1}};
    public Piece(int[] pat, int[] loc, int rot, TileColor c) {
        pattern = pat;
        location = loc;
        rotation = rot;
        color = c;
        generateTiles();
    }

    public void updateRotation(int rot) {
        rotation = rot;
        generateTiles();
    }

    public void generateTiles() {
        tiles = new Tile[pattern.length];
        tiles[0] = new Tile(location[0], location[1], color);
        for (int i = 1; i < pattern.length; i++) {
            int[] rot;
            int[] pos = tiles[i - 1].loc;
            if (pos[0] % 2 == 1) {
                rot = oddRotations[(pattern[i] + rotation) % 6];
            } else {
                rot = evenRotations[(pattern[i] + rotation) % 6];
            }

            tiles[i] = new Tile(pos[0] + rot[0], pos[1] + rot[1], color);
        }
    }

    public String toString() {
        return "(" + location[0] + ", " + location[1] + ") " + rotation;
    }
}
