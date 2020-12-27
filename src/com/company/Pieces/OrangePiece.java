package com.company.Pieces;

import com.company.TileColor;

public class OrangePiece extends Piece {
    public OrangePiece(int[] loc, int rot) {
        super(new int[]{0, 5, 0}, loc, rot, TileColor.ORANGE);
    }
}