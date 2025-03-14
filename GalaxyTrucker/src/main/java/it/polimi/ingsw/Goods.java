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

    public Goods(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}