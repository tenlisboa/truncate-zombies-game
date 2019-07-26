package com.devthunder.main;

import com.devthunder.entities.Bullet;
import com.devthunder.entities.Enemy;
import com.devthunder.entities.Entity;
import com.devthunder.entities.Player;
import com.devthunder.graphics.Spritesheet;
import com.devthunder.graphics.UI;
import com.devthunder.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas implements Runnable, KeyListener {

    public static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    public static final int WIDTH = 240;
    public static final int HEIGHT = 160;
    public static final int SCALE = 3;

    private BufferedImage image;

    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<Bullet> bullets;
    public static Spritesheet spritesheet;

    public static World world;
    public static Player player;
    public static Random rand;

    private int CUR_LEVEL = 1, MAX_LEVELS = 2;

    public UI ui;
    public static Menu menu;

    public static String gameState = "MENU";
    private boolean showMessageGameOver = true;
    private int framesGameOver = 0;
    private boolean restartGame = false;

    public Game() {
        rand = new Random();
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();

        ui = new UI();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        Game.initialize();
    }

    public static void initialize(String... args) {
        entities = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        bullets = new ArrayList<Bullet>();
        spritesheet = new Spritesheet("/spritesheet.png");
        player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
        entities.add(player);
        String mapPath = args.length > 0 ? args[0] : "level1.png";
        world = new World("/" + mapPath);

        menu = new Menu();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    public void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initFrame() {
        frame = new JFrame("Zelda");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void tick() {
        if (gameState == "NORMAL") {
            // Prevent for the user see the game over if press enter
            restartGame = false;
            for (int i = 0; i < entities.size(); i++) {
                entities.get(i).tick();
            }

            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).tick();
            }

            if (enemies.size() == 0) {
                // Go to next level
                CUR_LEVEL++;
                if (CUR_LEVEL > MAX_LEVELS) {
                    CUR_LEVEL = 1;
                }

                String newWorld = "level" + CUR_LEVEL + ".png";
                initialize(newWorld);
            }
        } else if (gameState == "GAME_OVER") {
            // GAYME OUVE
            framesGameOver++;
            if (framesGameOver == 15) {
                framesGameOver = 0;
                showMessageGameOver = !showMessageGameOver;
            }

            if (restartGame) {
                restartGame = false;
                gameState = "NORMAL";
                CUR_LEVEL = 1;
                String newWorld = "level" + CUR_LEVEL + ".png";
                Game.initialize(newWorld);
            }
        } else if (gameState == "MENU") {
            menu.tick();
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        world.render(g);

        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).render(g);
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).render(g);
        }

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

        ui.render(g);

        if (gameState == "GAME_OVER") {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

            g.setFont(new Font("arial", Font.BOLD, 30));
            g.setColor(Color.white);
            g.drawString("GAYME OUVE", ((WIDTH * SCALE) / 2) - 100, (HEIGHT * SCALE) / 2);
            if (showMessageGameOver)
                g.drawString(">Press enter to restart", ((WIDTH * SCALE) / 2) - 150, (HEIGHT * SCALE) / 2 + 40);
        } else if (gameState == "MENU") {
            menu.render(g);
        }
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                tick();
                render();
                frames++;
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println(frames);
                frames = 0;
                timer += 1000;
            }
        }

        stop();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
                e.getKeyCode() == KeyEvent.VK_D) {
            player.right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
                e.getKeyCode() == KeyEvent.VK_A) {
            player.left = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP ||
                e.getKeyCode() == KeyEvent.VK_W) {
            player.up = true;
            menu.up = gameState == "MENU" ? true : false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
                e.getKeyCode() == KeyEvent.VK_S) {
            player.down = true;
            menu.down = gameState == "MENU" ? true : false;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.shooting = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            restartGame = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
                e.getKeyCode() == KeyEvent.VK_D) {
            player.right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
                e.getKeyCode() == KeyEvent.VK_A) {
            player.left = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP ||
                e.getKeyCode() == KeyEvent.VK_W) {
            player.up = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
                e.getKeyCode() == KeyEvent.VK_S) {
            player.down = false;
        }
    }
}
