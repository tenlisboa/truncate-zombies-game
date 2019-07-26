package com.devthunder.main;

import java.awt.*;

public class Menu {

    public String[] options = {"Novo Jogo", "Sair"};
    public int currentOption = 0;
    public int maxOptions = options.length - 1;

    public boolean up, down;

    public void tick() {
        if (up) {
            up = false;
            currentOption--;
            if (currentOption < 0)
                currentOption = maxOptions;
        }
        if (down) {
            down = false;
            currentOption++;
            if (currentOption > maxOptions)
                currentOption = 0;
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 36));
        g.drawString(">Truncate Zoobies<", (Game.WIDTH * Game.SCALE) / 2 - 170, (Game.HEIGHT * Game.SCALE) / 2 - 150);

        g.setFont(new Font("arial", Font.BOLD, 24));
        for (int i = 0; i < options.length; i++) {
            g.drawString(options[i], (Game.WIDTH * Game.SCALE) / 2 - 80, (Game.HEIGHT * Game.SCALE) / 2 + (i * 40));
            if (options[currentOption] == options[i])
                g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 100, (Game.HEIGHT * Game.SCALE) / 2 + (i * 40));
        }
    }
}
