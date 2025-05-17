package it.polimi.ingsw.model.cardcontrollers;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.AbandonedStation;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidPositionException;
import it.polimi.ingsw.utilities.exceptions.WrongCallerException;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.GoodsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationCallerTest {

    private AbandonedStationController caller;
    private AbandonedStation card;
    private Trucker trucker;
    private Bank bank;
    private Game game;

    @BeforeEach
    void setUp() {
        caller=new AbandonedStationController();
        ArrayList<Goods> goods1 = new ArrayList<>();
        goods1.addLast(new Goods(3));
        goods1.addLast(new Goods(2));
        card=new AbandonedStation(true, 1,goods1 , 1,5, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0119.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg");
        trucker = new Trucker("gabri", PlayerColors.RED);
        bank=new Bank();
        game=new Game(1,2);
    }

    @Test
    void call() {
        CardStateResult actual=null;
        try {
            actual=caller.call(card, trucker, true, 1, bank);
        } catch (GeneralError e) {

        }
        try {
            CardStateResult expected = new CardStateResult(card.truckerLanding(trucker,true,1,bank));
            assertNotNull(actual);
            assertEquals(expected,actual);
        } catch (GeneralError e) {

        }
    }

    @Test
    void testCall() {
        GoodsResult actual=null;
        try {
            actual=caller.call(card,true);
        } catch (GeneralError e) {

        } catch (WrongCallerException e) {

        }
        GoodsResult expected=new GoodsResult(CardState.GOODSTOADD,card.getGoods().get(0));
        assertNotNull(actual);
        assertEquals(expected,actual);
    }

    @Test
    void testCall1() {
        CardStateResult actual=null;
        try {
            actual=caller.call(card, new int[]{1,2}, 1,trucker, bank);
        } catch (GeneralError e) {

        } catch (WrongCallerException e) {

        } catch (InvalidCoordinates e) {

        }
        try {
            CardStateResult expected = new CardStateResult(card.setWinnerCoordinatesToAddGoods(new int[]{1,2}, 1,trucker, bank));
            assertNotNull(actual);
            assertEquals(expected,actual);
        } catch (GeneralError e) {

        } catch (InvalidCoordinates e) {

        }
    }

    @Test
    void testCall2() {
        CardStateResult actual=null;
        try {
            actual = caller.call(card, trucker, new int[]{1, 2}, new int[]{2, 3}, 1, 2, false);
        } catch (InvalidCoordinates e) {

        } catch (WrongCallerException e) {

        } catch (GeneralError e) {

        } catch (InvalidPositionException e) {

        }
        try {
            CardStateResult expected = new CardStateResult(card.rearrange(trucker, new int[]{1, 2}, new int[]{2, 3}, 1, 2, false));
            assertNotNull(actual);
            assertEquals(expected,actual);
        } catch (GeneralError e) {

        } catch (InvalidPositionException e) {

        } catch (InvalidCoordinates e) {

        }
    }

    @Test
    void testCall3() {
        CardStateResult actual = null;
        try {
            actual = caller.call(card, game);
        } catch (GeneralError e) {

        } catch (WrongCallerException e) {

        }
        try {
            CardStateResult expected = new CardStateResult(card.cardEffect(game));
            assertNotNull(actual);
            assertEquals(expected,actual);
        } catch (GeneralError e) {

        }
    }
}