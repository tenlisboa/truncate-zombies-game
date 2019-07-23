package com.devthunder.entities;

import com.devthunder.main.Game;
import com.devthunder.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    private double speed = 0.4;
    private int maskx = 8, masky = 8, maskw = 5, maskh = 5;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void tick() {
        if (
                x < Game.player.getX() &&
                World.isFree((int) (x+speed), this.getY()) &&
                !isColliding((int) (x+speed), this.getY())
        ) {
            x+=speed;
        } else if (
                    x > Game.player.getX() &&
                    World.isFree((int) (x-speed), this.getY()) &&
                    !isColliding((int) (x-speed), this.getY())
        ) {
            x-=speed;
        }

        if (
            y < Game.player.getY() &&
            World.isFree(this.getX(), (int)(y+speed)) &&
            !isColliding(this.getX(), (int)(y+speed))
        ) {
            y+=speed;
        } else if (
                    y > Game.player.getY() &&
                    World.isFree(this.getX(), (int)(y-speed)) &&
                    !isColliding(this.getX(), (int)(y-speed))
        ) {
            y-=speed;
        }
    }

    public boolean isColliding(int xnext, int ynext) {
        Rectangle currentEnemy = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);

        for (int i = 0; i < Game.enemies.size(); i++) {
            Enemy enemy = Game.enemies.get(i);
            if (enemy == this)
                continue;

            Rectangle targetEnemy = new Rectangle(enemy.getX() + maskx, enemy.getY() + masky, maskw, maskh);

            if (currentEnemy.intersects(targetEnemy)) {
                return true;
            }
        }

        return false;
    }
}
