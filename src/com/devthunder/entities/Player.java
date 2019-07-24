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
    public double life = 100, maxLife = 100;
    public boolean isDamaged = false;

    private int frames, maxFrames = 5, index = 0, maxIndex = 3;
    private boolean moved = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage playerDamageLeft, playerDamageRight;
    private int damageFrames = 0;
    private boolean hasGun = false;
    public boolean shooting = false;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];
        playerDamageLeft = Game.spritesheet.getSprite(0, 16, 16, 16);
        playerDamageRight = Game.spritesheet.getSprite(16, 16, 16, 16);

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

        if (shooting) {
            shooting = false;
            if (hasGun && ammo > 0) {
                ammo--;
                int dx = dir == right_dir ? 1 : -1;
                int px = dir == right_dir ? 14 : -4;
                double py = 6.3;

                Bullet bullet = new Bullet(this.getX() + px, this.getY() + (int) py, 3, 3, null, dx, 0);
                Game.bullets.add(bullet);
            }
        }

        checkLifePackCollision();
        checkAmmoCollision();
        checkWeaponCollision();

        if (isDamaged) {
            damageFrames++;
            if (damageFrames == 8) {
                damageFrames = 0;
                isDamaged = false;
            }
        }

        if (life <= 0) {
            Game.initialize();
            return;
        }

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGTH * 16 - Game.HEIGHT);
    }

    public void checkWeaponCollision() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e instanceof Weapon) {
                if (Entity.isColliding(this, e)) {
                    hasGun = true;
                    Game.entities.remove(i);
                    return;
                }
            }
        }
    }

    public void checkAmmoCollision() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e instanceof Ammo) {
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
        BufferedImage playerSpriteSelected = rightPlayer[index];
        BufferedImage weaponSpriteSelected = dir == right_dir ? Entity.WEAPON_RIGHT : Entity.WEAPON_LEFT;
        int xPosition = dir == right_dir ? (this.getX() - Camera.x) + 6 : (this.getX() - Camera.x) - 6;

        if (dir == right_dir) {
            playerSpriteSelected = !isDamaged ? rightPlayer[index] : playerDamageRight;

        } else if (dir == left_dir) {
            playerSpriteSelected = !isDamaged ? leftPlayer[index] : playerDamageLeft;
        }

        g.drawImage(playerSpriteSelected, this.getX() - Camera.x, this.getY() - Camera.y, null);
        if (hasGun)
            g.drawImage(weaponSpriteSelected, xPosition, (this.getY() - Camera.y) + 1, null);
    }
}
