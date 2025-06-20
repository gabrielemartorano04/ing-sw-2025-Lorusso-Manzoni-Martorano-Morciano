package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.components.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Class that manages cards in the deck.
 */
class Deck implements Serializable {

    /**
     * List of cards in th deck
     */
    private final ArrayList<Card> cards;

    /**
     * Class constructor, initialise the list of cards that fills the deck
     */
    public Deck(int gameLevel) {
        cards = new ArrayList<>();
        fillDeck(gameLevel);
        //shuffleDeck(); todo togli commento dopo debug
    }

    /**
     * This method fills the deck with cards. Some cards are added only if the game level is 2.
     * @param gameLevel game level
     */
    private void fillDeck(int gameLevel) {
        Goods[] goodsGained1 = new Goods[3];
        goodsGained1[0] = new Goods(3);
        goodsGained1[1] = new Goods(2);
        goodsGained1[2] = new Goods(1);

        Goods[] goodsGained2 = new Goods[3];
        goodsGained2[0] = new Goods(4);
        goodsGained2[1] = new Goods(3);
        goodsGained2[2] = new Goods(3);

        ArrayList<int[]> shoots1 = new ArrayList<>();
        shoots1.addLast(new int[]{0,0});
        shoots1.addLast(new int[]{1,0});
        shoots1.addLast(new int[]{0,0});

        ArrayList<int[]> shoots2 = new ArrayList<>();
        shoots2.addLast(new int[]{1,0});
        shoots2.addLast(new int[]{0,0});
        shoots2.addLast(new int[]{1,0});

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
        planets1.getFirst().addLast(new Goods(4));
        planets1.getFirst().addLast(new Goods(2));
        planets1.getFirst().addLast(new Goods(1));
        planets1.getFirst().addLast(new Goods(1));
        planets1.getFirst().addLast(new Goods(1));
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
        planets2.getFirst().addLast(new Goods(4));
        planets2.getFirst().addLast(new Goods(4));
        planets2.get(1).addLast(new Goods(4));
        planets2.get(1).addLast(new Goods(1));
        planets2.get(1).addLast(new Goods(1));
        planets2.get(2).addLast(new Goods(3));

        ArrayList<ArrayList<Goods>> planets3 = new ArrayList<>();
        planets3.addLast(new ArrayList<>());
        planets3.addLast(new ArrayList<>());
        planets3.getFirst().addLast(new Goods(3));
        planets3.getFirst().addLast(new Goods(2));
        planets3.getFirst().addLast(new Goods(1));
        planets3.getFirst().addLast(new Goods(1));
        planets3.get(1).addLast(new Goods(3));
        planets3.get(1).addLast(new Goods(3));

        ArrayList<ArrayList<Goods>> planets4 = new ArrayList<>();
        planets4.addLast(new ArrayList<>());
        planets4.addLast(new ArrayList<>());
        planets4.addLast(new ArrayList<>());
        planets4.getFirst().addLast(new Goods(2));
        planets4.getFirst().addLast(new Goods(2));
        planets4.get(1).addLast(new Goods(3));
        planets4.get(2).addLast(new Goods(1));
        planets4.get(2).addLast(new Goods(1));
        planets4.get(2).addLast(new Goods(1));

        ArrayList<ArrayList<Goods>> planets5 = new ArrayList<>();
        planets5.addLast(new ArrayList<>());
        planets5.addLast(new ArrayList<>());
        planets5.addLast(new ArrayList<>());
        planets5.getFirst().addLast(new Goods(4));
        planets5.getFirst().addLast(new Goods(4));
        planets5.getFirst().addLast(new Goods(4));
        planets5.getFirst().addLast(new Goods(3));
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
        planets6.getFirst().addLast(new Goods(4));
        planets6.getFirst().addLast(new Goods(4));
        planets6.get(1).addLast(new Goods(2));
        planets6.get(1).addLast(new Goods(2));
        planets6.get(1).addLast(new Goods(2));
        planets6.get(1).addLast(new Goods(2));

        ArrayList<ArrayList<Goods>> planets7 = new ArrayList<>();
        planets7.addLast(new ArrayList<>());
        planets7.addLast(new ArrayList<>());
        planets7.addLast(new ArrayList<>());
        planets7.addLast(new ArrayList<>());
        planets7.getFirst().addLast(new Goods(4));
        planets7.getFirst().addLast(new Goods(3));
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
        planets8.getFirst().addLast(new Goods(2));
        planets8.getFirst().addLast(new Goods(2));
        planets8.getFirst().addLast(new Goods(2));
        planets8.getFirst().addLast(new Goods(2));
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
                new ArrayList<>(List.of(Condition.HUMANS, Condition.ENGINES, Condition.CANNONS)),
                Map.of(
                        Condition.HUMANS, new Effect(TypeOfEffect.LOSE_DAYS, 3),
                        Condition.ENGINES, new Effect(TypeOfEffect.LOSE_HUMANS, 2),
                        Condition.CANNONS, new Effect(TypeOfEffect.GET_SHOT, 2)
                ),
                warShoots,
                "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0116.jpg",
                "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",
                0
        );

        //TODO: riportare piratesPower a 5 e slaversPower a 6

        cards.addLast(new AbandonedShip(true, 1, 3, 4, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0118.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg", 2));
        cards.addLast(warZone1);
        cards.addLast(new Smugglers(true, 4, 2, 1, goodsGained1, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_012.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg", 1));
        cards.addLast(new AbandonedStation(true, 1, goods1, 1,5, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0119.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg", 3));
        cards.addLast(new StarDust(true, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_014.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg", 4));
        cards.addLast(new OpenSpace(true, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_015.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",5 ));
        cards.addLast(new MeteorShower(true, direction1, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_019.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",6));
        cards.addLast(new Planets(true, 2, 1, planets2, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0113.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",7));

        //NO TRIAL FLIGHT
        if(gameLevel==2){
            WarZone warZone2 = new WarZone(
                    false, 2,
                    new ArrayList<>(List.of(Condition.CANNONS, Condition.ENGINES, Condition.HUMANS)),
                    Map.of(
                            Condition.CANNONS, new Effect(TypeOfEffect.LOSE_DAYS, 4),
                            Condition.ENGINES, new Effect(TypeOfEffect.LOSE_GOODS, 3),
                            Condition.HUMANS, new Effect(TypeOfEffect.GET_SHOT, 3)
                    ),
                    warShoots2,
                    "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0116.jpg",
                    "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",8

            );
            cards.addLast(new Slavers(false, 1, 5, 1, 3,2, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_01.jpg",  "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",9));
            cards.addLast(new Pirates( false, 1, 4, 1,2, shoots1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_013.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",10));
            cards.addLast(new OpenSpace(false, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_018.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",11));
            cards.addLast(new OpenSpace(false, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_018.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",12));
            cards.addLast(new OpenSpace(false, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_018.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",13));
            cards.addLast(new MeteorShower(false, direction2, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0110.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",14));
            cards.addLast(new MeteorShower(false, direction3, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0111.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",15));
            cards.addLast(new Planets(false, 3, 1, planets1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0112.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",16));
            cards.addLast(new Planets(false, 3, 1, planets3, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0114.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",17));
            cards.addLast(new Planets(false, 1, 1, planets4, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0114.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",18));
            cards.addLast(new AbandonedShip(false, 1, 2, 3, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0117.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",19));
            cards.addLast(new AbandonedStation(false, 1, goods2, 1,6, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0120.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",20));
            cards.addLast(new Slavers(false, 2, 8, 2, 4,7, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_01.jpg",  "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",21));
            cards.addLast(new Smugglers(false, 8, 3, 1, goodsGained2, 2, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_012.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",22));
            cards.addLast(new Pirates( false, 2, 7, 2,6, shoots2, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_013.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",23));
            cards.addLast(new StarDust(false, 2, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_014.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",24));
            cards.addLast(new Epidemic(false, 2, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_015.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",25));
            cards.addLast(new OpenSpace(false, 2, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_018.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",26));
            cards.addLast(new OpenSpace(false, 2, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_018.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",27));
            cards.addLast(new OpenSpace(false, 2, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_018.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",28));
            cards.addLast(new MeteorShower(false, direction4, 2, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_019.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",29));
            cards.addLast(new MeteorShower(false, direction5, 2, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0110.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",30));
            cards.addLast(new MeteorShower(false, direction6, 2, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0111.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",31));
            cards.addLast(new Planets(false, 4, 2, planets5,"/it/polimi/ingsw/model/cards/GT-cards_II_IT_0112.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",32));
            cards.addLast(new Planets(false, 3, 2, planets6,"/it/polimi/ingsw/model/cards/GT-cards_II_IT_0113.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",33));
            cards.addLast(new Planets(false, 2, 2, planets7,"/it/polimi/ingsw/model/cards/GT-cards_II_IT_0114.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",34));
            cards.addLast(new Planets(false, 3, 2, planets8,"/it/polimi/ingsw/model/cards/GT-cards_II_IT_0115.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",35));
            cards.addLast(warZone2);
            cards.addLast(new AbandonedShip(false, 2, 4, 6, 1, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0117.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",36));
            cards.addLast(new AbandonedShip(false, 2, 5, 8, 2, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0118.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",37));
            cards.addLast(new AbandonedStation(false, 2, goods3, 1, 7, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0119.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",38));
            cards.addLast(new AbandonedStation(false, 2, goods4, 2, 8, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0120.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",39));

        }

    }

    /**
     * This method returns a specific card from the deck
     * @param index position of the card in the deck
     * @return card with that position in the deck
     */
    public Card getCard(int index) {
        return cards.get(index);
    }

    /**
     * This method returns all the cards in the deck
     * @return cards in the deck
     */
    public ArrayList<Card> getDeck() {
        return cards;
    }

    /**
     * This method shuffles the deck
     */
    private void shuffleDeck() {
        Collections.shuffle(cards);
    }
}

