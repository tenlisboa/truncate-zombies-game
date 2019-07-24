package com.devthunder.graphics;

import com.devthunder.entities.Player;

import java.awt.*;

public class UI {
    public void render(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(5, 5, 50, 8);

        g.setColor(Color.red);
        g.fillRect(5, 5, (int)((Player.life / Player.maxLife)*50), 8);

        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.PLAIN, 8));
        String lifeLabel = (int)Player.life + "/" + (int)Player.maxLife;
        g.drawString(lifeLabel , 8, 12);
    }
}
