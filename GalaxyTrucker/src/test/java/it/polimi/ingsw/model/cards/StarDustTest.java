package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.CreateShipTest;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.StarDustEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StarDustTest {

    private StarDust starDust;
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
        starDust = new StarDust(false, 1, "front.jpg", "back.jpg", 10);
    }

    @Test
    void testCardEffectExecution() throws GeneralError {
        ServerEvent event = starDust.cardEffect();
        StarDustEvent sdEvent = (StarDustEvent) event;
        assertEquals("Star Dust", sdEvent.getCardName());
        assertEquals("A cloud of cosmic dust", sdEvent.getCardDescription());
        assertEquals(this.starDust, sdEvent.getCard());
        assertEquals(Game.getIstance().getTrack(), sdEvent.getTrack());
    }

    @Test
    void testStartCardEffectCorrectlyTriggered() {
        ServerEvent event = starDust.startCard();
        switch (event){
            case StarDustEvent sd ->  assertEquals(Game.getIstance().getTrack(), sd.getTrack());
            default -> fail();

        }
    }

    @Test
    void testCardEffectThrowsIfWrongState() throws Exception {
        starDust.setState(CardState.LAND);
        starDust.startCard();

        assertThrows(GeneralError.class, () -> starDust.cardEffect());
    }

    @Test
    void testGetCardState() {
        assertEquals(CardState.ESECUZIONE, starDust.getCardState());
    }


}