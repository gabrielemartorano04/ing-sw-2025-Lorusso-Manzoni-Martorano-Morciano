package it.polimi.ingsw;
import java.util.Random;


class Deck {
    private Card[] cards = new Card[40];
    private int numOfMeteorShower;
    private int numOfPlanets;
    private int numOfStarDust;
    private int numOfOpenSpace;
    private int numOfAbandonedStation;
    private int numOfAbandonedShip;
    private int numOfPirates;
    private int numOfEpidemic;
    private int numOfSlavers;
    private int numOfSmugglers;
    private int numOfWarZone;

    public Deck() {
        fillDeck();
    }

    private void fillDeck() {
        int index = 0;
    }

    public Card getCard(int index) {
        return cards[index];
    }

    public Card[] getDeck() {
        return cards;
    }

    public void shuffleDeck() {
        Random rand = new Random();
        for (int i = cards.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Card temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
    }
}