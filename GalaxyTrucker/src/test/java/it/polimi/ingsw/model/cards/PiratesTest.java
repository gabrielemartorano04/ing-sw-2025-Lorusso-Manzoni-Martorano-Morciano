
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.model.CreateShipTest;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PiratesTest_FullCoverage {

    private Pirates pirates;
    private SpaceShip ship1, ship2;
    private CreateShipTest createShip = new CreateShipTest();

    @BeforeEach
    void setUp() throws InvalidCoordinates {
        ship1 = createShip.createShip1(new SpaceShip());
        ship2 = createShip.createShip3(new SpaceShip());
        Game.initializeForTest(ship1, ship2);

        ArrayList<int[]> shoots = new ArrayList<>();
        shoots.add(new int[]{0, 0}); // small
        shoots.add(new int[]{1, 1}); // big
        shoots.add(new int[]{0, 2}); // small
        pirates = new Pirates(false, 1, 3, 2, 2, shoots, "front", "back", 33);
    }

    @Test
    void testActivateCannonsWrongState() {
        assertThrows(GeneralError.class, () -> {
            pirates.setWinDecision(true); // cambia stato
            pirates.activateCannons(true, new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    void testActivateCannonsInvalidComponents() {
        ArrayList<int[]> wrong = new ArrayList<>();
        wrong.add(new int[]{9, 9});
        assertThrows(InvalidComponentsException.class, () -> {
            pirates.activateCannons(true, wrong, new ArrayList<>());
        });
    }

    @Test
    void testActivateCannonsInvalidBatteries() {
        ArrayList<int[]> coord = new ArrayList<>();
        coord.add(new int[]{0, 3}); // valid cannon
        ArrayList<int[]> bat = new ArrayList<>();
        bat.add(new int[]{9, 9});
        assertThrows(InvalidComponentsException.class, () -> {
            pirates.activateCannons(true, coord, bat);
        });
    }

    @Test
    void testActivateCannonsAllValid() throws Exception {
        ArrayList<int[]> coord = new ArrayList<>();
        coord.add(new int[]{5, 8});
        ArrayList<int[]> bat = new ArrayList<>();
        bat.add(new int[]{5, 6});
        pirates.activateCannons(true, coord, bat);
    }

    @Test
    void testSetWinDecisionAndCardEffectEnd() throws Exception {
        pirates.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        ServerEvent ev = pirates.setWinDecision(true);
        assertNotNull(ev);
    }

    @Test
    void testRollDiceInvalidState() {
        assertThrows(GeneralError.class, () -> {
            pirates.rollDice();
        });
    }

    @Test
    void testRollDiceOutOfBounds() throws Exception {
        pirates.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        pirates.setWinDecision(true);
        // simulate shoot overflow
        pirates.setState(CardState.ROLL_DICE);
        pirates.rollDice(); pirates.activateShield(false, new int[]{0,0}, new int[]{0,0});
        pirates.setState(CardState.ROLL_DICE);
        pirates.rollDice(); pirates.activateShield(false, new int[]{0,0}, new int[]{0,0});
        pirates.setState(CardState.ROLL_DICE);
        pirates.rollDice(); pirates.activateShield(false, new int[]{0,0}, new int[]{0,0});
        assertThrows(GeneralError.class, () -> pirates.rollDice());
    }

    @Test
    void testActivateShieldWrongState() {
        assertThrows(GeneralError.class, () -> {
            pirates.activateShield(true, new int[]{0,0}, new int[]{0,0});
        });
    }

    @Test
    void testActivateShieldInvalidShield() throws Exception {
        pirates.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        pirates.setWinDecision(true);
        pirates.setState(CardState.ROLL_DICE);
        pirates.rollDice(); // small
        assertThrows(InvalidCoordinates.class, () -> pirates.activateShield(true, new int[]{9,9}, new int[]{0,0}));
    }

    @Test
    void testActivateShieldInvalidBattery() throws Exception {
        pirates.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        pirates.setWinDecision(true);
        pirates.setState(CardState.ROLL_DICE);
        pirates.rollDice();
        assertThrows(InvalidCoordinates.class, () -> pirates.activateShield(true, new int[]{5, 6}, new int[]{9, 9}));
    }

    @Test
    void testActivateShieldNotEnoughBatteries() throws Exception {
        pirates.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        pirates.setWinDecision(true);
        pirates.setState(CardState.ROLL_DICE);
        pirates.rollDice();
        assertThrows(NotEnoughBatteries.class, () -> pirates.activateShield(true, new int[]{5,6}, new int[]{5,6}));
    }

    @Test
    void testCardEffectWrongState() {
        assertThrows(GeneralError.class, () -> pirates.cardEffect());
    }
}
