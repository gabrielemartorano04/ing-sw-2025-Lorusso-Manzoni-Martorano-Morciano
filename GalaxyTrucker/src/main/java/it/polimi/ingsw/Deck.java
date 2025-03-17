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
        cards.addFirst(new Slavers(false, 1, 5, 1, 3,6, "src/main/resources/Cards/cards/GT-cards_I_IT_01.jpg",  "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new Smugglers(true, 4, 2, 1, , 1, "src/main/resources/Cards/cards/GT-cards_I_IT_012.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new Pirates( false, 1, 4, 1,5, , "src/main/resources/Cards/cards/GT-cards_I_IT_013.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new StarDust(true, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_014.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new OpenSpace(true, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_015.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new OpenSpace(false, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_018.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new OpenSpace(false, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_018.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new OpenSpace(false, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_018.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new MeteorShower(true, , 1, "src/main/resources/Cards/cards/GT-cards_I_IT_019.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new MeteorShower(false, , 1, "src/main/resources/Cards/cards/GT-cards_I_IT_0110.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new MeteorShower(false, , 1, "src/main/resources/Cards/cards/GT-cards_I_IT_0111.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new Planets(false, 3, 1, , "src/main/resources/Cards/cards/GT-cards_I_IT_0112.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new Planets(true, 2, 1, , "src/main/resources/Cards/cards/GT-cards_I_IT_0113.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new Planets(false, 3, 1, , "src/main/resources/Cards/cards/GT-cards_I_IT_0114.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new Planets(false, 1, 1, , "src/main/resources/Cards/cards/GT-cards_I_IT_0114.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new WarZone());
        cards.addFirst(new AbandonedShip(false, 1, 2, 3, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_0117.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new AbandonedShip(true, 1, 3, 4, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_0118.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new AbandonedStation(true, 1, , 1,5, "src/main/resources/Cards/cards/GT-cards_I_IT_0119.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new AbandonedStation(false, 1, , 1,6, "src/main/resources/Cards/cards/GT-cards_I_IT_0120.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addFirst(new Slavers(false, 2, 8, 2, 4,7, "src/main/resources/Cards/cards/GT-cards_II_IT_01.jpg",  "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new Smugglers(false, 8, 3, 1, , 2, "src/main/resources/Cards/cards/GT-cards_II_IT_012.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new Pirates( false, 2, 7, 2,6, , "src/main/resources/Cards/cards/GT-cards_II_IT_013.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new StarDust(false, 2, "src/main/resources/Cards/cards/GT-cards_II_IT_014.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new Epidemic(false, 2, "src/main/resources/Cards/cards/GT-cards_II_IT_015.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new OpenSpace(false, 2, "src/main/resources/Cards/cards/GT-cards_I_IT_018.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new OpenSpace(false, 2, "src/main/resources/Cards/cards/GT-cards_I_IT_018.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new OpenSpace(false, 2, "src/main/resources/Cards/cards/GT-cards_I_IT_018.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new MeteorShower(false, , 2, "src/main/resources/Cards/cards/GT-cards_II_IT_019.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new MeteorShower(false, , 2, "src/main/resources/Cards/cards/GT-cards_II_IT_0110.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new MeteorShower(false, , 2, "src/main/resources/Cards/cards/GT-cards_II_IT_0111.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new Planets(false, 4, 2, ,"src/main/resources/Cards/cards/GT-cards_II_IT_0112.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg" ));
        cards.addFirst(new Planets(false, 3, 2, ,"src/main/resources/Cards/cards/GT-cards_II_IT_0113.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg" ));
        cards.addFirst(new Planets(false, 2, 2, ,"src/main/resources/Cards/cards/GT-cards_II_IT_0114.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg" ));
        cards.addFirst(new Planets(false, 3, 2, ,"src/main/resources/Cards/cards/GT-cards_II_IT_0115.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg" ));
        cards.addFirst(new WarZone());
        cards.addFirst(new AbandonedShip(false, 2, 4, 6, 1, "src/main/resources/Cards/cards/GT-cards_II_IT_0117.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new AbandonedShip(false, 2, 5, 8, 2, "src/main/resources/Cards/cards/GT-cards_II_IT_0118.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new AbandonedStation(false, 2, , 1, 7, "src/main/resources/Cards/cards/GT-cards_II_IT_0119.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addFirst(new AbandonedStation(false, 2, , 2, 8, "src/main/resources/Cards/cards/GT-cards_II_IT_0120.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
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

