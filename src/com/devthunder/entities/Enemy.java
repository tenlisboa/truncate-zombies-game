package com.devthunder.entities;

import com.devthunder.main.Game;
import com.devthunder.world.Camera;
import com.devthunder.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    private double speed = 0.4;
    private int frames, maxFrames = 20, index = 0, maxIndex = 2;

    private BufferedImage[] sprites;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, null);
        sprites = new BufferedImage[3];
        this.setMask(8, 8, 5, 5);
        int xPosition = 112;
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = Game.spritesheet.getSprite(xPosition, 16, 16, 16);
            xPosition += 16;
        }
    }

    public void tick() {
        if (isCollidingWithPlayer() == false) {
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
        } else {
            // Coliding with player
            if (Game.rand.nextInt(100) < 10) {
                Game.player.life -= Game.rand.nextInt(3);
                if (Game.player.life <= 0) {
                    // Gameover
                    System.exit(1);
                }
            }
        }

        frames++;
        if (frames == maxFrames) {
            frames = 0;
            index++;
            if (index > maxIndex) {
                index = 0;
            }
        }
    }

    public boolean isCollidingWithPlayer() {
        Rectangle currentEnemy = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
        Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

        return currentEnemy.intersects(player);
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

    public void render(Graphics g) {
        g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
    }
}
