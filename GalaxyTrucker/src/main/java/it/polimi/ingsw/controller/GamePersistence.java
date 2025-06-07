package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import java.io.*;

public class GamePersistence {
    private static final String SAVE_PATH = "saves/game.ser";

    public static void saveGame(Game game) {
        try {
            File dir = new File("saves");
            if (!dir.exists()) dir.mkdirs();

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_PATH))) {
                out.writeObject(game);
                System.out.println("Partita salvata su disco.");
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    public static Game loadGame() {
        File file = new File(SAVE_PATH);
        if (!file.exists()) return null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_PATH))) {
            Game game = (Game) in.readObject();
            System.out.println("Partita caricata da disco.");
            return game;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento: " + e.getMessage());
            return null;
        }
    }

    public static boolean hasSaveFile() {
        return new File("saves/game.ser").exists();
    }

    public static void deleteSave() {
        File file = new File(SAVE_PATH);
        if (file.exists()) file.delete();
    }
}
