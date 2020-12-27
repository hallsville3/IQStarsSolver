package com.company.Pieces;

import com.company.TileColor;

public class BluePiece extends Piece {
    public BluePiece(int[] loc, int rot) {
        super(new int[]{0, 3, 3, 5}, loc, rot, TileColor.BLUE);
    }
}
