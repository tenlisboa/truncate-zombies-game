package com.devthunder.main;

import java.io.*;

public class Saver {

    public static final int ENCODE = 184;
    public static boolean saveExists = false;
    public static boolean saveGame = false;

    public static void saveGame(String[] val1, int[] val2, int encode) {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter("save.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < val1.length; i++) {
            String current = val1[i];
            current += ":";
            char[] value = Integer.toString(val2[i]).toCharArray();

            for (int n = 0; n < value.length; n++) {
                value[n] += encode;
                current += value[n];
            }

            try {
                writer.write(current);
                if (i < val1.length - 1)
                    writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String loadGame(int encode) {
        String line = "";
        File file = new File("save.txt");
        if (file.exists()) {
            try {
                String singleLine = null;
                BufferedReader reader = new BufferedReader(new FileReader("save.txt"));

                while ((singleLine = reader.readLine()) != null) {
                    String[] lineTransition = singleLine.split(":");
                    char[] val = lineTransition[1].toCharArray();
                    lineTransition[1] = "";
                    for (int i = 0; i < val.length; i++) {
                        val[i] -= encode;
                        lineTransition[1] += val[i];
                    }
                    line += lineTransition[0];
                    line += ":";
                    line += lineTransition[1];
                    line += "/";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return line;
    }

    public static void applySave(String str) {
        String[] spl = str.split("/");
        for (int i = 0; i < spl.length; i++) {
            String[] spl2 = spl[i].split(":");
            switch (spl2[0]) {
                case "level":
                    Game.initialize("level" + spl2[1] + ".png");
                    Game.gameState = "NORMAL";
                    break;
            }
        }
    }
}
