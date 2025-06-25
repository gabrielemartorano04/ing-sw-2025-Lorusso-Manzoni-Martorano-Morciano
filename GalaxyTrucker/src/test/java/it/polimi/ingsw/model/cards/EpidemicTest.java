package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.CreateShipTest;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.EpidemicEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.view.ParserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpidemicTest {

    private Epidemic epidemic;
    private CreateShipTest createShip = new CreateShipTest();

    @BeforeEach
    void setUp() {
        SpaceShip ship1=new SpaceShip();
        SpaceShip ship2=new SpaceShip();
        try {
            ship1 = createShip.createShip1(ship1);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        try {
            ship2 = createShip.createShip2(ship2);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        Game.initializeForTest(ship1, ship2);
        epidemic = new Epidemic(false, 2, "/it/polimi/ingsw/model/cards/GT-cards_II_IT_015.jpg", "/it/polimi/ingsw/model/cards/GT-cards_II_IT_0121.jpg",25);
    }

    @Test
    void getCardState() {
        assertEquals(CardState.ESECUZIONE, epidemic.getCardState());
    }

    @Test
    void startCard() {
        ServerEvent sEvent = epidemic.startCard();
        EpidemicEvent event = (EpidemicEvent) sEvent;
        assertEquals(epidemic.getName(), event.getCardName());
        assertEquals(epidemic.getDescription(), event.getCardDescription());
        assertTrue(event.isForAll());
        assertEquals(Game.getIstance().getTruckerList(), event.getTruckers());
        assertNull(event.getNickname());
        assertEquals(epidemic.getTruckersRemoved(), event.getTruckersRemoved());
        assertEquals(Game.getIstance().getTrack(), event.getTrack());
        assertEquals(this.epidemic, event.getCard());
    }

    @Test
    void cardEffect() {
        try {
            ServerEvent sEvent = epidemic.cardEffect();
            EpidemicEvent event = (EpidemicEvent) sEvent;
            assertEquals(epidemic.getName(), event.getCardName());
            assertEquals(epidemic.getDescription(), event.getCardDescription());
            assertTrue(event.isForAll());
            assertEquals(Game.getIstance().getTruckerList(), event.getTruckers());
            assertNull(event.getNickname());
            assertEquals(epidemic.getTruckersRemoved(), event.getTruckersRemoved());
            assertEquals(Game.getIstance().getTrack(), event.getTrack());
            assertEquals(this.epidemic, event.getCard());

        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getTruckersRemoved() {
        assertTrue(epidemic.getTruckersRemoved().isEmpty());
    }

}