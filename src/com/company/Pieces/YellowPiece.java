package com.company.Pieces;

import com.company.TileColor;

public class YellowPiece extends Piece {
    public YellowPiece(int[] loc, int rot) {
        super(new int[]{0, 3, 5, 5}, loc, rot, TileColor.YELLOW);
    }
}
