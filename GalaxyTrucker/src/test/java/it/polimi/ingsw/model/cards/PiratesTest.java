
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.model.CreateShipTest;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.BooleanDecision;
import it.polimi.ingsw.net.events.serverevents.cardevents.CardFinished;
import it.polimi.ingsw.net.events.serverevents.cardevents.RollDiceResult;
import it.polimi.ingsw.net.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.view.ParserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PiratesTest {

    private Pirates pirates;
    private SpaceShip ship1, ship2;
    private CreateShipTest createShip = new CreateShipTest();

    @BeforeEach
    void setUp() throws InvalidCoordinates {
        ship1 = createShip.createShip1(new SpaceShip());
        ship1.setBattery();
        ship1.setNumHuman();
        ship2 = createShip.createShip3(new SpaceShip());
        ship2.setBattery();
        ship2.setNumHuman();
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
            pirates.gainCredits(true); // changes state
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
    void testGainCreditsAndCardEffectEnd() throws Exception {
        pirates.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        ServerEvent ev = pirates.gainCredits(true);
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
        pirates.gainCredits(true);
        // simulate shoot overflow
        pirates.setState(CardState.ROLL_DICE);
        pirates.setCurrShootIndex(10);
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
        pirates.gainCredits(true);
        pirates.setState(CardState.ROLL_DICE);
        pirates.rollDice(); // small
        assertThrows(InvalidCoordinates.class, () -> pirates.activateShield(true, new int[]{9,9}, new int[]{0,0}));
    }

    @Test
    void testActivateShieldInvalidBattery() throws Exception {
        pirates.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        pirates.gainCredits(true);
        pirates.setState(CardState.ROLL_DICE);
        pirates.rollDice();
        assertThrows(InvalidCoordinates.class, () -> pirates.activateShield(true, new int[]{5, 6}, new int[]{9, 9}));
    }

    @Test
    void testActivateShieldNotEnoughBatteries() throws Exception {
        pirates.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        pirates.gainCredits(true);
        pirates.setState(CardState.ROLL_DICE);
        pirates.rollDice();
        ship1 = createShip.createShip1(new SpaceShip());
        ship1.setNumHuman();
        ship2 = createShip.createShip1(new SpaceShip());
        ship2.setNumHuman();
        Game.initializeForTest(ship1, ship2);
        assertThrows(NotEnoughBatteries.class, () -> pirates.activateShield(true, new int[]{8,8}, new int[]{5,6}));
    }

    @Test
    void testCardEffectWrongState() {
        assertThrows(GeneralError.class, () -> pirates.cardEffect());
    }

    @Test
    void testGetters() {
        assertEquals(3, pirates.getCredits());
        assertEquals(2, pirates.getDays());
        assertEquals(2, pirates.getPiratesPower());
        assertEquals(3, pirates.getShoots().size());
        assertEquals(CardState.ACTIVATE_CANNONS, pirates.getCardState());
    }

    @Test
    void testStartCard(){
        ServerEvent sEvent = pirates.startCard();
        StartCardEvent event = (StartCardEvent) sEvent;
        assertEquals("Pirates", event.getCardName());
        assertEquals(pirates.getDescription(), event.getCardDescription());
        assertEquals(Game.getIstance().getTruckerList().getFirst().getNickname(), event.getFirstPlayerNickname());
        assertEquals("whether to activate double cannons",event.getQuestion());
        assertEquals(pirates.getDays(), event.getDays());
        assertEquals(pirates.getCredits(), event.getCredits());
        assertEquals(-1, event.getNumHumans());
        assertNull(event.getGoods());
        assertNull(event.getMeteorites());
        assertEquals(pirates.getPiratesPower(), event.getPower());
        assertEquals(-1, event.getAstronautsLost());
        assertEquals(-1, event.getAstronautsLost());
        assertEquals(pirates.getShoots(), event.getShoots());
        assertNull(event.getPlanets());
        assertNull(event.getConditionsOrder());
        assertNull(event.getPenalties());
        assertEquals(ParserState.ACTIVATE_CANNONS, event.getParserState());
    }

    @Test
    void testHandleDefeatThenRollDice() throws Exception {
        pirates.setIndexOfCurrentTrucker(0);
        pirates.setCurrShootIndex(0);
        pirates.setState(CardState.ACTIVATE_CANNONS);
        pirates.setIndexOfCurrentTrucker(1);
        pirates.setDefeatedTrucker(Game.getIstance().getTruckerList().get(0));
        ServerEvent ev1 = pirates.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        switch (ev1){
            case BooleanDecision bl -> assertEquals(Game.getIstance().getTruckerList().get(pirates.getIndexOfCurrentTrucker()).getNickname(), bl.getNickname());
            default -> fail();
        }
    }

    @Test
    void testCardEffectFlowBigAndSmallHit() throws Exception {
        pirates.setState(CardState.ROLL_DICE);
        pirates.setCurrShootIndex(0);
        pirates.setIndexOfCurrentTrucker(0);
        pirates.setState(CardState.ACTIVATE_CANNONS);
        pirates.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        pirates.gainCredits(true);
        pirates.setState(CardState.ROLL_DICE);
        pirates.setDefeatedTrucker(Game.getIstance().getTrucker(0));
        pirates.rollDice();
        pirates.activateShield(false, new int[]{0,0}, new int[]{0,0});
        pirates.setState(CardState.ROLL_DICE);
        pirates.setDefeatedTrucker(Game.getIstance().getTrucker(0));
        pirates.rollDice();
        pirates.setState(CardState.ESECUZIONE);
        pirates.deleteDefeated();
        ServerEvent e = pirates.cardEffect();
        switch (e) {
            case CardFinished cf -> assertEquals(Game.getIstance().getTrack(), cf.getTrack());
            default -> fail();
        }
    }

    @Test
    void testCardEffectAfterAllShootsFinished() throws Exception {
        pirates.setCurrShootIndex(2);
        pirates.setState(CardState.ESECUZIONE);
        ServerEvent e = pirates.cardEffect();
        assertNotNull(e);
    }

    @Test
    void testCardEffectBigShot() throws Exception {
        pirates.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        pirates.gainCredits(true);
        pirates.setState(CardState.ROLL_DICE);
        pirates.rollDice(); // big
        pirates.setState(CardState.ESECUZIONE);
        CardFinished result = (CardFinished) pirates.cardEffect();
        assertNotNull(result);
    }

    @Test
    void testCardEffectSmallShotWithBlock() throws Exception {
        pirates.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        pirates.gainCredits(true);
        pirates.setState(CardState.ROLL_DICE);
        pirates.setDefeatedTrucker(Game.getIstance().getTruckerList().get(0));
        pirates.rollDice(); // big shot (0)
        pirates.setState(CardState.ESECUZIONE);
        pirates.cardEffect();

        pirates.setCurrShootIndex(1); // small shot
        pirates.setState(CardState.ROLL_DICE);
        pirates.setDefeatedTrucker(Game.getIstance().getTruckerList().get(0));
        pirates.rollDice();
        pirates.setState(CardState.ESECUZIONE);
        assertNotNull(pirates.cardEffect());
    }

    @Test
    void testCardEffectNoMoreShootsNoDefeated() throws Exception {
        pirates.setState(CardState.ESECUZIONE);
        pirates.deleteDefeated();
        pirates.setCurrShootIndex(2); // all shoots done
        ServerEvent e = pirates.cardEffect();
        switch (e){
            case CardFinished cf -> assertEquals(Game.getIstance().getTruckerList(), cf.getTruckers());
            default -> fail();
        }
    }

    @Test
    void getCurrentTrucker() throws Exception {
        assertTrue(pirates.getIndexOfCurrentTrucker()<=4 && pirates.getIndexOfCurrentTrucker()>=0);
    }
}
