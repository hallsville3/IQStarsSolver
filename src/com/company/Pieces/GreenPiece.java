package com.company.Pieces;

import com.company.TileColor;

public class GreenPiece extends Piece {
    public GreenPiece(int[] loc, int rot) {
        super(new int[]{0, 5, 0, 0}, loc, rot, TileColor.GREEN);
    }
}