package it.polimi.ingsw.model;

import java.io.*;

public class SpaceShipSerializationTest {
    public static void main(String[] args) {
        SpaceShip spaceship = new SpaceShip();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("spaceship.ser"));
             ObjectInputStream in = new ObjectInputStream(new FileInputStream("spaceship.ser"))) {

            // Serializzazione: scriviamo l'oggetto nel file
            out.writeObject(spaceship);

            // Deserializzazione: leggiamo l'oggetto dal file
            SpaceShip deserializedShip = (SpaceShip) in.readObject();

            // Verifica del successo della serializzazione e deserializzazione
            System.out.println("Successfully serialized and deserialized SpaceShip.");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
