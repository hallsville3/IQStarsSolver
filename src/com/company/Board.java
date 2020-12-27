package com.company;

import com.company.Pieces.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class Board {
    public Tile[][] tiles;
    public Tile[][] background;
    public ArrayList<Piece> pieces;
    public HashMap<TileColor, ArrayList<Constraint>> constraints;
    public int iterations;
    public boolean failed;
    public Board() {
        tiles = new Tile[4][7];
        background = new Tile[4][7];
        pieces = new ArrayList<>();
        constraints = new HashMap<>();

        initialize();
    }

    public void setFailed() {
        failed = true;
    }

    public void addConstraint(int x, int y, TileColor color) {
        constraints.get(color).add(new Constraint(x, y, color));
    }

    public void addPiece(Piece p) {
        pieces.add(p);
        for (int i = 0; i < p.tiles.length; i++) {
            Tile t = p.tiles[i];
            tiles[t.loc[0]][t.loc[1]] = t;
        }
    }

    public void removePiece(Piece p) {
        if (!pieces.contains(p)) {
            return;
        }
        pieces.remove(p);
        for (int i = 0; i < p.tiles.length; i++) {
            Tile t = p.tiles[i];
            tiles[t.loc[0]][t.loc[1]] = null;
        }
    }

    public boolean conflicting(Piece p) {
        for (int i = 0; i < p.tiles.length; i++) {
            Tile t = p.tiles[i];
            if (tiles[t.loc[0]][t.loc[1]] != null) {
                return true;
            }
        }
        return false;
    }

    public void initialize() {
        failed = false;
        for (TileColor t: TileColor.values()) {
            if (t != TileColor.BLACK) {
                constraints.put(t, new ArrayList<>());
            }
        }
        setBackground(false);
    }

    public void setBackground(boolean failed) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 7; col++) {
                background[row][col] = new Tile(row, col, TileColor.BLACK);
                if (row % 2 == 1 && col == 6) {
                    background[row][col] = null;
                }
            }
        }
    }

    public boolean solve(boolean verbose, Window window) throws InterruptedException {
        // First determine where constrained pieces can go based on the given constraints
        // Also make lists of where the rest of the pieces can go
        long time = System.currentTimeMillis();
        HashMap<TileColor, Piece> pieceMap = new HashMap<>();
        pieceMap.put(TileColor.RED, new RedPiece(new int[]{2, 1}, 0));
        pieceMap.put(TileColor.PURPLE, new PurplePiece(new int[]{0, 0}, 0));
        pieceMap.put(TileColor.BLUE, new BluePiece(new int[]{3, 5}, 0));
        pieceMap.put(TileColor.YELLOW, new YellowPiece(new int[]{2, 6}, 0));
        pieceMap.put(TileColor.ORANGE, new OrangePiece(new int[]{2, 3}, 0));
        pieceMap.put(TileColor.GREEN, new GreenPiece(new int[]{1, 2}, 0));
        pieceMap.put(TileColor.PINK, new PinkPiece(new int[]{3, 0}, 0));

        HashMap<TileColor, ArrayList<int[]>> moveMap = new HashMap<>();
        for (TileColor t: TileColor.values()) {
            if (t == TileColor.BLACK) { continue; }
            ArrayList<int[]> moves = new ArrayList<>();
            Piece p = pieceMap.get(t);
            for (int rot = 0; rot < 6; rot++) {
                p.updateRotation(rot);
                for (int row = 0; row < 4; row++) {
                    for (int col = 0; col < 7; col++) {
                        p.location = new int[]{row, col};
                        p.generateTiles();
                        boolean fits = true;
                        for (Tile tile : p.tiles) {
                            if (tile.loc[0] + row - p.location[0] > 3 || tile.loc[0] + row - p.location[0] < 0) {
                                fits = false;
                            }
                            if (tile.loc[1] + col - p.location[1] > 6 || tile.loc[1] + col - p.location[1] < 0) {
                                fits = false;
                            }
                            if ((tile.loc[0] + row - p.location[0]) % 2 == 1 && tile.loc[1] + col - p.location[1] == 6) {
                                fits = false;
                            }
                            if (!fits) {
                                break;
                            }
                        }
                        if (fits) {
                            // Add this move if it satisfies all constraints
                            boolean isSatisfied = true;
                            for (TileColor constraintType: TileColor.values()) {
                                if (constraintType != TileColor.BLACK) {
                                    for (Constraint c : constraints.get(constraintType)) {
                                        if (!c.isSatisfiedBy(p)) {
                                            isSatisfied = false;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (isSatisfied) {
                                // Does it block off a piece of the board corner? If not, it is a legal move
                                moves.add(new int[]{row, col, rot});
                            }
                        }
                    }
                }
            }
            moveMap.put(t, moves);
        }
        if (verbose) {
            System.out.println("Moves computed in " + (System.currentTimeMillis() - time)+ " ms.");
        }

        // Now we have moveMap which contains possible moves for each piece that satisfy all of its constraints

        // The next step is to remove any duplicates. We can disregard any rotation 3 from the red or the purple
        ArrayList<int[]> redMoves = new ArrayList<>();
        for (int[] move: moveMap.get(TileColor.RED)) {
            if (move[2] != 3) {
                redMoves.add(move);
            }
        }
        moveMap.put(TileColor.RED, redMoves);

        ArrayList<int[]> purpleMoves = new ArrayList<>();
        for (int[] move: moveMap.get(TileColor.PURPLE)) {
            if (move[2] != 3) {
                purpleMoves.add(move);
            }
        }
        moveMap.put(TileColor.PURPLE, purpleMoves);

        // Now we only have states that are actually unique
        if (verbose) {
            System.out.println("Moves Per Color:");
        }
        long total = 1;
        ArrayList<TileColor> colors = new ArrayList<>();
        for (TileColor t: TileColor.values()) {
            if (t != TileColor.BLACK) {
                colors.add(t);
                if (verbose) {
                    System.out.println("  " + t + ": " + moveMap.get(t).size());
                }
                total *= moveMap.get(t).size();
            }
        }
        if (verbose) {
            System.out.println();
            System.out.println("Total combinations: " + total);
        }

        // We want to order our tile colors by who has the least options first
        class SortByCombinations implements Comparator<TileColor> {
            public int compare(TileColor a, TileColor b) {
                return moveMap.get(a).size() - moveMap.get(b).size();
            }
        }
        colors.sort(new SortByCombinations());

        // At this point, colors is ordered from least to most options
        // Now we can actually start solving

        iterations = 0;
        boolean success = DFS(0, colors, moveMap, pieceMap, verbose, window);
        if (!success && verbose) {
            System.out.println("Failed.");
        }
        if (verbose) {
            System.out.println("Took " + (System.currentTimeMillis() - time) + " ms.");
        }
        return success;

    }

    public boolean DFS(int colorIndex, ArrayList<TileColor> colors, HashMap<TileColor, ArrayList<int[]>> moveMap, HashMap<TileColor, Piece> pieceMap, boolean verbose, Window window) throws InterruptedException {
        if (colorIndex == 7) {
            // Solved
            if (verbose) {
                System.out.println("Solved in " + iterations + " iterations!");
            }
            return true;
        }
        TileColor color = colors.get(colorIndex);
        Piece p = pieceMap.get(color);
        for (int[] move: moveMap.get(color)) { // Make a move at this depth and move on
            iterations++;
            if (iterations % (100 * 17) == 0) { // 6000 iterations per second
                window.repaint();
                Thread.sleep(17);
            }
            removePiece(p);
            p.location = new int[] {move[0], move[1]};
            p.updateRotation(move[2]);
            if (!conflicting(p)) { // If it is not conflicting, add it and go deeper.
                addPiece(p);
                boolean value = DFS(colorIndex + 1, colors, moveMap, pieceMap, verbose, window);
                if (value) {
                    return true;
                }
                removePiece(p);
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 7; col++) {
                if (background[row][col] != null) {
                    background[row][col].draw(g);
                }
            }
        }

        for (TileColor t: TileColor.values()) {
            if (t == TileColor.BLACK) { continue; }
            for (Constraint c : constraints.get(t)) {
                c.draw(g);
            }
        }

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 7; col++) {
                if (tiles[row][col] != null) {
                    tiles[row][col].draw(g);
                }
            }
        }

        if (failed) {
            g.setColor(Color.DARK_GRAY);
            g.fillRoundRect(150, 150, 500, 200, 40, 40);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 100));
            Rectangle2D r = g.getFontMetrics().getStringBounds("No Solution", g);
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("No Solution", (int)(400 - r.getWidth() / 2), (int)(250 + r.getHeight() / 2));
        }
    }
}
