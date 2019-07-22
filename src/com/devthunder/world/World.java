package com.devthunder.world;

import com.devthunder.entities.*;
import com.devthunder.main.Game;

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

                    tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);

                    switch (actualPixel) {
                        case 0xFFFFFFFF: // Wall
                            tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
                            break;
                        case 0xFF0026FF: // Player
                            Game.player.setX(xx * 16);
                            Game.player.setY(yy * 16);
                            break;
                        case 0xFFFF0000: // Enemy
                            Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_EN));
                            break;
                        case 0xFFFF6A00: // Weapon
                            Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN));
                            break;
                        case 0xFFFFD800: // Bullet
                            Game.entities.add(new Bullet(xx*16, yy*16, 16, 16, Entity.BULLET_EN));
                            break;
                        case 0xFFFF7F7F: // LifePack
                            Game.entities.add(new LifePack(xx*16, yy*16, 16, 16, Entity.LIFEPACK_EN));
                            break;
                        default: // Floor
                            tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                            break;
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
