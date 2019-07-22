package com.devthunder.world;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {

    private Tile[] tiles;
    public static int WIDTH,HEIGTH;

    public World(String path) {
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));
            int worldLength = map.getWidth() * map.getHeight();

            int[] pixels = new int[worldLength];

            WIDTH = map.getWidth();
            HEIGTH = map.getHeight();

            tiles = new Tile[worldLength];

            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
            for (int xx = 0; xx < map.getWidth(); xx++) {
                for (int yy = 0; yy < map.getWidth(); yy++) {
                    int actualPixel = pixels[xx + yy * map.getWidth()];

                    if (actualPixel == 0xFF000000) {
                        // Floor
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    } else if (actualPixel == 0xFFFFFFFF) {
                        // Wall
                        tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
                    } else if (actualPixel == 0xFF0026FF) {
                        // Player
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    } else {
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    }
                }
            }
       } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        for (int xx = 0; xx < WIDTH; xx++) {
            for (int yy = 0; yy < HEIGTH; yy++) {
                Tile tile = tiles[xx + (yy*WIDTH)];
                tile.render(g);
            }
        }
    }
}
