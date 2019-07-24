package com.devthunder.entities;

import com.devthunder.main.Game;
import com.devthunder.world.Camera;
import com.devthunder.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    public boolean right, left, up, down;
    public int right_dir = 0, left_dir = 1;
    public int dir = right_dir;
    public double speed = 0.7;
    public int ammo = 0;
    public static double life = 100, maxLife = 100;
    public boolean isDamaged = false;

    private int frames, maxFrames = 5, index = 0, maxIndex = 3;
    private boolean moved = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage playerDamage;
    private int damageFrames = 0;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];
        playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);

        for (int i = 0; i < 4; i++) {
            rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
        }

        for (int i = 0; i < 4; i++) {
            leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
        }
    }

    @Override
    public void tick() {
        moved = false;
        if (right && World.isFree((int) (x + speed), this.getY())) {
            moved = true;
            dir = right_dir;
            x += speed;
        } else if (left && World.isFree((int) (x - speed), this.getY())) {
            moved = true;
            dir = left_dir;
            x -= speed;
        }
        if (up && World.isFree(this.getX(), (int) (y - speed))) {
            moved = true;
            y -= speed;
        } else if (down && World.isFree(this.getX(), (int) (y + speed))) {
            moved = true;
            y += speed;
        }

        if (moved) {
            frames++;
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex) {
                    index = 0;
                }
            }
        }

        checkLifePackCollision();
        checkAmmoCollision();

        if (isDamaged) {
            damageFrames++;
            if (damageFrames == 8) {
                damageFrames = 0;
                isDamaged = false;
            }
        }

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGTH * 16 - Game.HEIGHT);
    }

    public void checkAmmoCollision() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e instanceof Bullet) {
                if (Entity.isColliding(this, e)) {
                    ammo += 12;
                    Game.entities.remove(i);
                    return;
                }
            }
        }
    }

    public void checkLifePackCollision() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e instanceof LifePack) {
                if (Entity.isColliding(this, e)) {
                    life += 8;
                    if (life >= 100)
                        life = 100;
                    Game.entities.remove(i);
                    return;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (!isDamaged) {
            if (dir == right_dir) {
                g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            } else if (dir == left_dir) {
                g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            }
        } else {
            g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
        }
    }
}
