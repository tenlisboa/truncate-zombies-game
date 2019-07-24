package com.devthunder.world;

import com.devthunder.entities.*;
import com.devthunder.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {

    public static Tile[] tiles;
    public static int WIDTH,HEIGTH;
    public static final int TILE_SIZE = 16;

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
                            Enemy enemy = new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_EN);
                            Game.entities.add(enemy);
                            Game.enemies.add(enemy);
                            break;
                        case 0xFFFF6A00: // Weapon
                            Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN));
                            break;
                        case 0xFFFFD800: // Bullet
                            Bullet blt = new Bullet(xx*16, yy*16, 16, 16, Entity.BULLET_EN);
                            blt.setMask(0, 10, 5, 5);
                            Game.entities.add(blt);
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

    public static boolean isFree(int xnext, int ynext) {
        int x1 = xnext / TILE_SIZE;
        int y1 = ynext / TILE_SIZE;

        int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
        int y2 = ynext / TILE_SIZE;

        int x3 = xnext / TILE_SIZE;
        int y3 = (ynext + TILE_SIZE -1) / TILE_SIZE;

        int x4 = (xnext + TILE_SIZE -1) / TILE_SIZE;
        int y4 = (ynext + TILE_SIZE -1) / TILE_SIZE;

        return !(
            tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile ||
            tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile ||
            tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile ||
            tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile
        );
    }

    public void render(Graphics g) {
        int xstart = Camera.x / 16;
        int ystart = Camera.y / 16;

        int xfinal = xstart + (Game.WIDTH / 16);
        int yfinal = ystart + (Game.HEIGHT / 16);

        for (int xx = xstart; xx <= xfinal; xx++) {
            for (int yy = ystart; yy <= yfinal; yy++) {
                if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGTH)
                    continue;
                Tile tile = tiles[xx + (yy*WIDTH)];
                tile.render(g);
            }
        }
    }
}
