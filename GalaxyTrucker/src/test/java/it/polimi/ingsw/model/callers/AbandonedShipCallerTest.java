package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.controller.Trucker;
import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.WrongCallerException;
import it.polimi.ingsw.utilities.results.CardStateResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipCallerTest {

    private AbandonedShipCaller caller;
    private AbandonedShip card;
    private Trucker trucker;
    private Game game;

    @BeforeEach
    void setUp() {
        caller = new AbandonedShipCaller();
        card = new AbandonedShip(false, 1, 2, 3, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0117.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg");
        trucker = new Trucker("gabri", PlayerColors.RED);
        game = new Game(1, 2);
    }

    @Test
    void call() {
        CardStateResult actual = null;
        try {
            actual = caller.call(card, trucker, true, 1);
        } catch (WrongCallerException e) {

        } catch (GeneralError e) {

        }
        try {
            CardStateResult expected = new CardStateResult(card.setTruckerThatHasDecided(trucker, true, 1));
            assertNotNull(actual);
            assertEquals(expected,actual);
        } catch (GeneralError e) {

        }
    }

    @Test
    void testCall() {
        CardStateResult actual = null;
        ArrayList<int[]> coord = new ArrayList<>();
        coord.add(new int[]{1, 2});
        coord.add(new int[]{2, 3});

        try {
            actual = caller.call(card, coord);
        } catch (WrongCallerException e) {

        } catch (GeneralError e) {

        } catch (InvalidComponentsException e) {

        } catch (InvalidCoordinates e) {

        }
        try {
            CardStateResult expected = new CardStateResult(card.setCoordinates(coord));
            assertNotNull(actual);
            assertEquals(expected,actual);
        } catch (GeneralError e) {

        } catch (InvalidComponentsException e) {

        } catch (InvalidCoordinates e) {

        }
    }

    @Test
    void testCall1() {
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