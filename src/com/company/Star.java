package com.company;

import java.awt.*;

public class Star {
    public int x, y, r;
    public int tri_height;
    public int[] xPoints1, xPoints2, yPoints1, yPoints2;

    public Color color;
    public Star(int x, int y, int r, Color c) {
        this.x = x;
        this.y = y;
        this.r = r;

        color = c;

        initialize();
    }

    public void initialize() {
        tri_height = (int)(Math.sin(Math.PI / 3) * this.r);

        xPoints1 = new int[] {x - r / 2, x, x + r / 2};
        xPoints2 = new int[] {x - r / 2, x, x + r / 2};
        yPoints1 = new int[] {y + tri_height / 2, y - tri_height / 2, y + tri_height / 2};
        yPoints2 = new int[]{y - tri_height / 2, y + tri_height / 2, y - tri_height / 2};
        for (int i = 0; i < 3; i++) {
            yPoints1[i] -= tri_height / 6;
            yPoints2[i] += tri_height / 6;
        }

        // Now, rotate 15 + 30 degrees
        for (int i = 0; i < 3; i++) {
            int r = (int)Math.pow((xPoints1[i] - x) * (xPoints1[i] - x) +
                                  (yPoints1[i] - y) * (yPoints1[i] - y), .5);
            double theta = Math.atan2(yPoints1[i] - y, xPoints1[i] - x);
            theta -= Math.PI / 12 + Math.PI / 6;
            xPoints1[i] = (int)(x + Math.cos(theta) * r);
            yPoints1[i] = (int)(y + Math.sin(theta) * r);

            r = (int)Math.pow((xPoints2[i] - x) * (xPoints2[i] - x) +
                              (yPoints2[i] - y) * (yPoints2[i] - y), .5);
            theta = Math.atan2(yPoints2[i] - y, xPoints2[i] - x);
            theta -= Math.PI / 12 + Math.PI / 6;
            xPoints2[i] = (int)(x + Math.cos(theta) * r);
            yPoints2[i] = (int)(y + Math.sin(theta) * r);
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillPolygon(xPoints1, yPoints1, 3);
        g.fillPolygon(xPoints2, yPoints2, 3);
    }
}
