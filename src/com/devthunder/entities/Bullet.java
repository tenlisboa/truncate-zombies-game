package com.devthunder.entities;

import com.devthunder.main.Game;
import com.devthunder.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet extends Entity {

    private int dx, dy;
    private double speed = 4;
    private int life = 120, currentLife = 0;

    public Bullet(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
        super(x, y, width, height, sprite);
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void tick() {
        x += dx * speed;
        y += dy * speed;
        currentLife++;
        if (currentLife == life) {
            Game.bullets.remove(this);
            return;
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
    }
}
