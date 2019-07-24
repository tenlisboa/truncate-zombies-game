package com.devthunder.entities;

import com.devthunder.main.Game;
import com.devthunder.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6 * 16, 0, 16, 16);
    public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7 * 16, 0, 16, 16);
    public static BufferedImage WEAPON_RIGHT = Game.spritesheet.getSprite(8 * 16, 0, 16, 16);
    public static BufferedImage WEAPON_LEFT = Game.spritesheet.getSprite(9 * 16, 0, 16, 16);
    public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6 * 16, 16, 16, 16);
    public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7 * 16, 16, 16, 16);

    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected int maskx, masky, maskw, maskh;

    private BufferedImage sprite;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;

        this.setMask(0, 0, width, height);
    }

    public void setMask(int maskx, int masky, int maskw, int maskh) {
        this.maskx = maskx;
        this.masky = masky;
        this.maskw = maskw;
        this.maskh = maskh;
    }

    public void setX(int x) {
        this.x = (double) x;
    }

    public void setY(int y) {
        this.y = (double) y;
    }

    public int getX() {
        return (int) this.x;
    }

    public int getY() {
        return (int) this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void tick() {

    }

    public static boolean isColliding(Entity e1, Entity e2) {
        Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.maskw, e1.maskh);
        Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.maskw, e2.maskh);

        return e1Mask.intersects(e2Mask);
    }


    public void render(Graphics g) {
        g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
    }
}
