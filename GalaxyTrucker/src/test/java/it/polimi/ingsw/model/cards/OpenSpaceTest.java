package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.model.CreateShipTest;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.BooleanDecision;
import it.polimi.ingsw.net.events.serverevents.cardevents.CardFinished;
import it.polimi.ingsw.net.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.view.ParserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceTest {

    private OpenSpace openSpace;
    private CreateShipTest createShip = new CreateShipTest();

    @BeforeEach
    void setUp() {
        SpaceShip ship1=new SpaceShip();
        SpaceShip ship2=new SpaceShip();
        try {
            ship1 = createShip.createShip1(ship1);
            ship1.setBattery();
            ship1.setNumHuman();
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        try {
            ship2 = createShip.createShip3(ship2);
            ship2.setBattery();
            ship2.setNumHuman();
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        Game.initializeForTest(ship1, ship2);
        ArrayList<Goods> goods2 = new ArrayList<>();
        goods2.addLast(new Goods(4));
        goods2.addLast(new Goods(4));
        openSpace = new OpenSpace(true, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_015.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",5 );

    }

    @Test
    void getCardState() {
        assertEquals(CardState.ACTIVATE_ENGINES, openSpace.getCardState());
    }

    @Test
    void startCard() {
        ServerEvent sEvent = openSpace.startCard();
        StartCardEvent event = (StartCardEvent) sEvent;
        assertEquals("Open Space", event.getCardName());
        assertEquals(openSpace.getDescription(), event.getCardDescription());
        assertEquals(Game.getIstance().getTruckerList().getFirst().getNickname(), event.getFirstPlayerNickname());
        assertEquals("Choose if you want to activate the double engines. If yes type the coordinates of the cannons then 'battery' and battery storage(s) coordinates . Type 'y, <x1> <y1>, <x2> <y2>,..., battery, <xs1> <ys1> - <numofbatteries>,...' or 'n': ", event.getQuestion());
        assertEquals(-1, event.getDays());
        assertEquals(-1, event.getCredits());
        assertEquals(-1, event.getNumHumans());
        assertNull(event.getGoods());
        assertNull(event.getMeteorites());
        assertEquals(-1, event.getPower());
        assertEquals(-1, event.getAstronautsLost());
        assertEquals(-1, event.getAstronautsLost());
        assertNull(event.getShoots());
        assertNull(event.getPlanets());
        assertNull(event.getConditionsOrder());
        assertNull(event.getPenalties());
        assertEquals(ParserState.ACTIVATE_ENGINES, event.getParserState());
        assertEquals(this.openSpace, event.getCard());
    }

    @Test
    void activateEngines_yes() {
        openSpace.setState(CardState.ACTIVATE_ENGINES);
        ArrayList<int[]> engineCoords = new ArrayList<>();
        engineCoords.add(new int[]{8,10});
        ArrayList<int[]> batteryCoords = new ArrayList<>();
        batteryCoords.add(new int[]{6,9});
        ServerEvent event = null;
        try {
            event = openSpace.activateEngines(true, engineCoords, batteryCoords);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }
        if(openSpace.getIndexOfCurrentTrucker() == Game.getIstance().getTruckerList().size()){
            CardFinished e = (CardFinished) event;
            assertTrue(e.isForAll());
            assertEquals(Game.getIstance().getTruckerList(), e.getTruckers());
            assertEquals(Game.getIstance().getTrack(), e.getTrack());
        }else{
            BooleanDecision e= (BooleanDecision) event;
            assertTrue(e.isForAll());
            assertEquals(ParserState.ACTIVATE_ENGINES, e.getParserState());
            assertEquals(Game.getIstance().getTruckerList().get(openSpace.getIndexOfCurrentTrucker()).getNickname(), e.getNickname());
        }
    }

    @Test
    void activateEngines_no() {
        openSpace.setState(CardState.ACTIVATE_ENGINES);
        ServerEvent event = null;
        try {
            event = openSpace.activateEngines(false, null, null);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }
        if(openSpace.getIndexOfCurrentTrucker() == Game.getIstance().getTruckerList().size()){
            CardFinished e = (CardFinished) event;
            assertTrue(e.isForAll());
            assertEquals(Game.getIstance().getTruckerList(), e.getTruckers());
            assertEquals(Game.getIstance().getTrack(), e.getTrack());
        }else{
            BooleanDecision e= (BooleanDecision) event;
            assertTrue(e.isForAll());
            assertEquals(ParserState.ACTIVATE_ENGINES, e.getParserState());
            assertEquals(Game.getIstance().getTruckerList().get(openSpace.getIndexOfCurrentTrucker()).getNickname(), e.getNickname());
        }
    }

    @Test
    void cardEffect() {
        ServerEvent event = null;
        try {
            openSpace.setState(CardState.ESECUZIONE);
            event = openSpace.cardEffect();
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
        CardFinished e = (CardFinished) event;
        assertTrue(e.isForAll());
        assertEquals(Game.getIstance().getTruckerList(), e.getTruckers());
        assertEquals(Game.getIstance().getTrack(), e.getTrack());
    }

    @Test
    void getIndexOfCurrentTrucker(){
        assertEquals(0, openSpace.getIndexOfCurrentTrucker());
    }

    @Test
    void setState(){
        openSpace.setState(CardState.ACTIVATE_ENGINES);
        assertEquals(CardState.ACTIVATE_ENGINES, openSpace.getCardState());
    }
}