package com.company.Pieces;

import com.company.TileColor;

public class RedPiece extends Piece {
    public RedPiece(int[] loc, int rot) {
        super(new int[] {0, 0, 1, 3}, loc, rot, TileColor.RED);
    }
}
