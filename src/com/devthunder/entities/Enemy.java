package com.devthunder.entities;

import com.devthunder.main.Game;
import com.devthunder.world.World;

import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    private double speed = 0.2;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void tick() {
        if (x < Game.player.getX() && World.isFree((int) (x+speed), this.getY())) {
            x+=speed;
        } else if (x > Game.player.getX() && World.isFree((int) (x-speed), this.getY())) {
            x-=speed;
        }

        if (y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))) {
            y+=speed;
        } else if (y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))) {
            y-=speed;
        }
    }
}
