package it.polimi.ingsw;
import java.util.Random;
import java.util.ArrayList;

class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>();
        fillDeck();
    }

    //le rpime 20 sono di livello 1 e le altre 20 di livello 2
    private void fillDeck() {
        cards.addFirst(new Slavers());
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
        cards.addFirst();
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    public ArrayList<Card> getDeck() {
        return cards;
    }

//    public void shuffleDeck() {
//        Random rand = new Random();
//        for (int i = cards.length - 1; i > 0; i--) {
//            int j = rand.nextInt(i + 1);
//            Card temp = cards[i];
//            cards[i] = cards[j];
//            cards[j] = temp;
//        }
//    }
}

