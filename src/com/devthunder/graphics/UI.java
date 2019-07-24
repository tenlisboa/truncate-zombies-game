package com.devthunder.graphics;

import com.devthunder.entities.Player;
import com.devthunder.main.Game;

import java.awt.*;

public class UI {
    public void render(Graphics g) {

        // Dark rect LIFE
        g.setColor(Color.darkGray);
        g.fillRect(15, 10, 150, 20);

        // LIFE
        g.setColor(Color.red);
        g.fillRect(15, 10, (int) ((Player.life / Player.maxLife) * 150), 20);

        // LIFE label
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 17));
        String lifeLabel = (int) Player.life + "/" + (int) Player.maxLife;
        g.drawString(lifeLabel, 20, 25);

        // AMMO
        g.setFont(new Font("arial", Font.BOLD, 17));
        g.setColor(Color.white);
        g.drawString("Ammo: " + Game.player.ammo, (Game.WIDTH * Game.SCALE) - 100, 20);
    }
}
