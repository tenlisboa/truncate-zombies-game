package com.devthunder.main;

import java.awt.*;
import java.io.File;

public class Menu {

    public String[] options = {"New Game", "Load Game", "Exit"};
    public int currentOption = 0;
    public int maxOptions = options.length - 1;

    public boolean up, down, enter;

    public void tick() {
        File save = new File("save.txt");
        Saver.saveExists = save.exists();

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
        if (enter) {
            enter = false;
            if (options[currentOption] == "New Game") {
                Game.gameState = "NORMAL";

                // TODO: Save the game
                String[] key = {"level"};
                int[] value = {1};
                Saver.saveGame(key, value, Saver.ENCODE);

                Game.initialize();
            } else if (options[currentOption] == "Load Game") {
                if (Saver.saveExists) {
                    String saver = Saver.loadGame(Saver.ENCODE);
                    Saver.applySave(saver);
                }
            } else if (options[currentOption] == "Exit") {
                System.exit(1);
            }
        }
    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
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
