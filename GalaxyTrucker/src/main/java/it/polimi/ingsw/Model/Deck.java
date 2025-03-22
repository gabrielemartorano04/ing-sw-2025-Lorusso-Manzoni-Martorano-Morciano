package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Components.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that manages the cards as a deck
 */
class Deck {
    private final ArrayList<Card> cards;

    /**
     * Deck constructor, initialise the list of cards that will compose the deck
     */
    public Deck() {
        cards = new ArrayList<Card>();
        fillDeck();
    }

    //le prime 20 sono di livello 1 e le altre 20 di livello 2

    /**
     * Fills the deck with the 40 cards available, the first 20 of level 1 and the other 20 of level 2
     */
    private void fillDeck() {
        Goods[] goodsGained1 = new Goods[3];
        goodsGained1[0] = new Goods(3);
        goodsGained1[1] = new Goods(2);
        goodsGained1[2] = new Goods(1);

        Goods[] goodsGained2 = new Goods[3];
        goodsGained1[0] = new Goods(4);
        goodsGained1[1] = new Goods(3);
        goodsGained1[2] = new Goods(3);

        ArrayList<int[]> shoots1 = new ArrayList<>();
        shoots1.addLast(new int[]{0,0});
        shoots1.addLast(new int[]{1,0});
        shoots1.addLast(new int[]{0,0});

        ArrayList<int[]> shoots2 = new ArrayList<>();
        shoots1.addLast(new int[]{1,0});
        shoots1.addLast(new int[]{0,0});
        shoots1.addLast(new int[]{1,0});

        ArrayList<int[]> direction1 = new ArrayList<>();
        direction1.addLast(new int[]{0,0});
        direction1.addLast(new int[]{0,3});
        direction1.addLast(new int[]{0,1});

        ArrayList<int[]> direction2 = new ArrayList<>();
        direction2.addLast(new int[]{0,0});
        direction2.addLast(new int[]{0,0});
        direction2.addLast(new int[]{0,3});
        direction2.addLast(new int[]{0,1});
        direction2.addLast(new int[]{0,2});

        ArrayList<int[]> direction3 = new ArrayList<>();
        direction3.addLast(new int[]{0,1});
        direction3.addLast(new int[]{0,0});
        direction3.addLast(new int[]{0,1});

        ArrayList<int[]> direction4 = new ArrayList<>();
        direction4.addLast(new int[]{0,0});
        direction4.addLast(new int[]{0,0});
        direction4.addLast(new int[]{1,3});
        direction4.addLast(new int[]{0,3});
        direction4.addLast(new int[]{0,3});

        ArrayList<int[]> direction5 = new ArrayList<>();
        direction5.addLast(new int[]{1,0});
        direction5.addLast(new int[]{1,0});
        direction5.addLast(new int[]{0,2});
        direction5.addLast(new int[]{0,2});

        ArrayList<int[]> direction6 = new ArrayList<>();
        direction6.addLast(new int[]{0,0});
        direction6.addLast(new int[]{0,0});
        direction6.addLast(new int[]{1,1});
        direction6.addLast(new int[]{0,1});
        direction6.addLast(new int[]{0,1});

        ArrayList<ArrayList<Goods>> planets1 = new ArrayList<>();
        planets1.addLast(new ArrayList<>());
        planets1.addLast(new ArrayList<>());
        planets1.addLast(new ArrayList<>());
        planets1.addLast(new ArrayList<>());
        planets1.get(0).addLast(new Goods(4));
        planets1.get(0).addLast(new Goods(2));
        planets1.get(0).addLast(new Goods(1));
        planets1.get(0).addLast(new Goods(1));
        planets1.get(0).addLast(new Goods(1));
        planets1.get(1).addLast(new Goods(4));
        planets1.get(1).addLast(new Goods(3));
        planets1.get(1).addLast(new Goods(1));
        planets1.get(2).addLast(new Goods(4));
        planets1.get(2).addLast(new Goods(1));
        planets1.get(2).addLast(new Goods(1));
        planets1.get(2).addLast(new Goods(1));
        planets1.get(3).addLast(new Goods(4));
        planets1.get(3).addLast(new Goods(2));

        ArrayList<ArrayList<Goods>> planets2 = new ArrayList<>();
        planets2.addLast(new ArrayList<>());
        planets2.addLast(new ArrayList<>());
        planets2.addLast(new ArrayList<>());
        planets2.get(0).addLast(new Goods(4));
        planets2.get(0).addLast(new Goods(4));
        planets2.get(1).addLast(new Goods(4));
        planets2.get(1).addLast(new Goods(1));
        planets2.get(1).addLast(new Goods(1));
        planets2.get(2).addLast(new Goods(3));

        ArrayList<ArrayList<Goods>> planets3 = new ArrayList<>();
        planets3.addLast(new ArrayList<>());
        planets3.addLast(new ArrayList<>());
        planets3.get(0).addLast(new Goods(3));
        planets3.get(0).addLast(new Goods(2));
        planets3.get(0).addLast(new Goods(1));
        planets3.get(0).addLast(new Goods(1));
        planets3.get(1).addLast(new Goods(3));
        planets3.get(1).addLast(new Goods(3));

        ArrayList<ArrayList<Goods>> planets4 = new ArrayList<>();
        planets4.addLast(new ArrayList<>());
        planets4.addLast(new ArrayList<>());
        planets4.addLast(new ArrayList<>());
        planets4.get(0).addLast(new Goods(2));
        planets4.get(0).addLast(new Goods(2));
        planets4.get(1).addLast(new Goods(3));
        planets4.get(2).addLast(new Goods(1));
        planets4.get(2).addLast(new Goods(1));
        planets4.get(2).addLast(new Goods(1));

        ArrayList<ArrayList<Goods>> planets5 = new ArrayList<>();
        planets5.addLast(new ArrayList<>());
        planets5.addLast(new ArrayList<>());
        planets5.addLast(new ArrayList<>());
        planets5.get(0).addLast(new Goods(4));
        planets5.get(0).addLast(new Goods(4));
        planets5.get(0).addLast(new Goods(4));
        planets5.get(0).addLast(new Goods(3));
        planets5.get(1).addLast(new Goods(4));
        planets5.get(1).addLast(new Goods(4));
        planets5.get(1).addLast(new Goods(2));
        planets5.get(1).addLast(new Goods(2));
        planets5.get(2).addLast(new Goods(4));
        planets5.get(2).addLast(new Goods(1));
        planets5.get(2).addLast(new Goods(1));
        planets5.get(2).addLast(new Goods(1));
        planets5.get(2).addLast(new Goods(1));

        ArrayList<ArrayList<Goods>> planets6 = new ArrayList<>();
        planets6.addLast(new ArrayList<>());
        planets6.addLast(new ArrayList<>());
        planets5.get(0).addLast(new Goods(4));
        planets5.get(0).addLast(new Goods(4));
        planets5.get(1).addLast(new Goods(2));
        planets5.get(1).addLast(new Goods(2));
        planets5.get(1).addLast(new Goods(2));
        planets5.get(1).addLast(new Goods(2));

        ArrayList<ArrayList<Goods>> planets7 = new ArrayList<>();
        planets7.addLast(new ArrayList<>());
        planets7.addLast(new ArrayList<>());
        planets7.addLast(new ArrayList<>());
        planets7.addLast(new ArrayList<>());
        planets7.get(0).addLast(new Goods(4));
        planets7.get(0).addLast(new Goods(3));
        planets7.get(1).addLast(new Goods(3));
        planets7.get(1).addLast(new Goods(2));
        planets7.get(1).addLast(new Goods(1));
        planets7.get(2).addLast(new Goods(2));
        planets7.get(2).addLast(new Goods(2));
        planets7.get(3).addLast(new Goods(3));

        ArrayList<ArrayList<Goods>> planets8 = new ArrayList<>();
        planets8.addLast(new ArrayList<>());
        planets8.addLast(new ArrayList<>());
        planets8.addLast(new ArrayList<>());
        planets8.get(0).addLast(new Goods(2));
        planets8.get(0).addLast(new Goods(2));
        planets8.get(0).addLast(new Goods(2));
        planets8.get(0).addLast(new Goods(2));
        planets8.get(1).addLast(new Goods(3));
        planets8.get(1).addLast(new Goods(3));
        planets8.get(2).addLast(new Goods(1));
        planets8.get(2).addLast(new Goods(1));
        planets8.get(2).addLast(new Goods(1));
        planets8.get(2).addLast(new Goods(1));

        ArrayList<Goods> goods1 = new ArrayList<>();
        goods1.addLast(new Goods(3));
        goods1.addLast(new Goods(2));

        ArrayList<Goods> goods2 = new ArrayList<>();
        goods2.addLast(new Goods(4));
        goods2.addLast(new Goods(4));

        ArrayList<Goods> goods3 = new ArrayList<>();
        goods3.addLast(new Goods(4));
        goods3.addLast(new Goods(3));

        ArrayList<Goods> goods4 = new ArrayList<>();
        goods4.addLast(new Goods(3));
        goods4.addLast(new Goods(3));
        goods4.addLast(new Goods(2));

        ArrayList<int[]> warShoots = new ArrayList<>();
        warShoots.add(new int[]{0, 2});
        warShoots.add(new int[]{1, 2});

        ArrayList<int[]> warShoots2 = new ArrayList<>();
        warShoots2.add(new int[]{0, 0});
        warShoots2.add(new int[]{0, 3});
        warShoots2.add(new int[]{0, 1});
        warShoots2.add(new int[]{1, 2});

        WarZone warZone1 = new WarZone(
                true, 1,
                List.of(Condition.HUMANS, Condition.ENGINES, Condition.CANNONS),
                Map.of(
                        Condition.HUMANS, new Effect(TypeOfEffect.LOSE_DAYS, 3),
                        Condition.ENGINES, new Effect(TypeOfEffect.LOSE_HUMANS, 2),
                        Condition.CANNONS, new Effect(TypeOfEffect.GET_SHOT, 2)
                ),
                warShoots,
                "src/main/resources/Cards/cards/GT-cards_I_IT_0116.jpg",
                "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"
        );

        WarZone warZone2 = new WarZone(
                false, 2,
                List.of(Condition.CANNONS, Condition.ENGINES, Condition.HUMANS),
                Map.of(
                        Condition.CANNONS, new Effect(TypeOfEffect.LOSE_DAYS, 4),
                        Condition.ENGINES, new Effect(TypeOfEffect.LOSE_GOODS, 3),
                        Condition.HUMANS, new Effect(TypeOfEffect.GET_SHOT, 3)
                ),
                warShoots2,
                "src/main/resources/Cards/cards/GT-cards_II_IT_0116.jpg",
                "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"

        );

        cards.addLast(new Slavers(false, 1, 5, 1, 3,6, "src/main/resources/Cards/cards/GT-cards_I_IT_01.jpg",  "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new Smugglers(true, 4, 2, 1, goodsGained1, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_012.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new Pirates( false, 1, 4, 1,5, shoots1, "src/main/resources/Cards/cards/GT-cards_I_IT_013.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new StarDust(true, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_014.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new OpenSpace(true, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_015.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new OpenSpace(false, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_018.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new OpenSpace(false, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_018.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new OpenSpace(false, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_018.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new MeteorShower(true, direction1, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_019.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new MeteorShower(false, direction2, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_0110.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new MeteorShower(false, direction3, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_0111.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new Planets(false, 3, 1, planets1, "src/main/resources/Cards/cards/GT-cards_I_IT_0112.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new Planets(true, 2, 1, planets2, "src/main/resources/Cards/cards/GT-cards_I_IT_0113.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new Planets(false, 3, 1, planets3, "src/main/resources/Cards/cards/GT-cards_I_IT_0114.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new Planets(false, 1, 1, planets4, "src/main/resources/Cards/cards/GT-cards_I_IT_0114.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(warZone1);
        cards.addLast(new AbandonedShip(false, 1, 2, 3, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_0117.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new AbandonedShip(true, 1, 3, 4, 1, "src/main/resources/Cards/cards/GT-cards_I_IT_0118.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new AbandonedStation(true, 1, goods1, 1,5, "src/main/resources/Cards/cards/GT-cards_I_IT_0119.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new AbandonedStation(false, 1, goods2, 1,6, "src/main/resources/Cards/cards/GT-cards_I_IT_0120.jpg", "src/main/resources/Cards/cards/GT-cards_I_IT_0121.jpg"));
        cards.addLast(new Slavers(false, 2, 8, 2, 4,7, "src/main/resources/Cards/cards/GT-cards_II_IT_01.jpg",  "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new Smugglers(false, 8, 3, 1, goodsGained2, 2, "src/main/resources/Cards/cards/GT-cards_II_IT_012.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new Pirates( false, 2, 7, 2,6, shoots2, "src/main/resources/Cards/cards/GT-cards_II_IT_013.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new StarDust(false, 2, "src/main/resources/Cards/cards/GT-cards_II_IT_014.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new Epidemic(false, 2, "src/main/resources/Cards/cards/GT-cards_II_IT_015.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new OpenSpace(false, 2, "src/main/resources/Cards/cards/GT-cards_I_IT_018.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new OpenSpace(false, 2, "src/main/resources/Cards/cards/GT-cards_I_IT_018.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new OpenSpace(false, 2, "src/main/resources/Cards/cards/GT-cards_I_IT_018.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new MeteorShower(false, direction4, 2, "src/main/resources/Cards/cards/GT-cards_II_IT_019.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new MeteorShower(false, direction5, 2, "src/main/resources/Cards/cards/GT-cards_II_IT_0110.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new MeteorShower(false, direction6, 2, "src/main/resources/Cards/cards/GT-cards_II_IT_0111.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new Planets(false, 4, 2, planets5,"src/main/resources/Cards/cards/GT-cards_II_IT_0112.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg" ));
        cards.addLast(new Planets(false, 3, 2, planets6,"src/main/resources/Cards/cards/GT-cards_II_IT_0113.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg" ));
        cards.addLast(new Planets(false, 2, 2, planets7,"src/main/resources/Cards/cards/GT-cards_II_IT_0114.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg" ));
        cards.addLast(new Planets(false, 3, 2, planets8,"src/main/resources/Cards/cards/GT-cards_II_IT_0115.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg" ));
        cards.addLast(warZone2);
        cards.addLast(new AbandonedShip(false, 2, 4, 6, 1, "src/main/resources/Cards/cards/GT-cards_II_IT_0117.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new AbandonedShip(false, 2, 5, 8, 2, "src/main/resources/Cards/cards/GT-cards_II_IT_0118.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new AbandonedStation(false, 2, goods3, 1, 7, "src/main/resources/Cards/cards/GT-cards_II_IT_0119.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));
        cards.addLast(new AbandonedStation(false, 2, goods4, 2, 8, "src/main/resources/Cards/cards/GT-cards_II_IT_0120.jpg", "src/main/resources/Cards/cards/GT-cards_II_IT_0121.jpg"));

    }

    /**
     * Returns a specific card from the deck
     * @param index position in the deck
     * @return card
     */
    public Card getCard(int index) {
        return cards.get(index);
    }

    /**
     * Returns the deck with all the 40 cards
     * @return deck
     */
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

