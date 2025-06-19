package it.polimi.ingsw.controller;

import java.io.*;

/**
 * This class manages game saves
 */
public class GamePersistence {
    /**
     * Path for the file in which matches are saved
     */
    private static final String SAVE_PATH = "saves/game.ser";

    /**
     * This method saves the game state
     * @param gameController game controller of the game
     */
    public static void saveGame(GameController gameController) {
        try {
            File dir = new File("saves");
            if (!dir.exists()) dir.mkdirs();

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_PATH))) {
                out.writeObject(gameController);
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method loads an old match
     * @return game controller of the game
     */
    public static GameController loadGame() {
        File file = new File(SAVE_PATH);
        if (!file.exists()) return null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_PATH))) {
            GameController gameController = (GameController) in.readObject();
            System.out.println("Partita caricata da disco.");
            return gameController;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento: " + e.getMessage());
            return null;
        }
    }

    /**
     * This method checks if the saves file does exist
     * @return if the saves file exists or not
     */
    public static boolean hasSaveFile() {
        return new File("saves/game.ser").exists();
    }

    /**
     * This method deletes the saves file
     */
    public static void deleteSave() {
        File file = new File(SAVE_PATH);
        if (file.exists()) file.delete();
    }

}
