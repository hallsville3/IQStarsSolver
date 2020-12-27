package com.company.Pieces;

import com.company.TileColor;

public class PinkPiece extends Piece {
    public PinkPiece(int[] loc, int rot) {
        super(new int[]{0, 4, 5, 0}, loc, rot, TileColor.PINK);
    }
}