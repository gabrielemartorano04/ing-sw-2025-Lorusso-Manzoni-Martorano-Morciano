package it.polimi.ingsw;

/**
 * Value rappresenta il colore della merce:
 * value = 4 implica che il colore è red
 * value = 3 implica che il colore è giallo
 * value = 2 implica che il colore è verde
 * value = 1 implica che il colore è blu
 */

class Goods {
    private int value;
    // x e y sono le coordinate della posizione della load che tiene la merce dentro la SpaceShip. Vengono usate solo per le merci
    // dentro la SpaceShip quindi non vengono messo nel costruttore.
    private int x, y;

    public Goods(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

}