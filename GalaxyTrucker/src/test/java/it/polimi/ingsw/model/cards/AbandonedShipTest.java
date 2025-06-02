package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.CreateShipTest;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.events.serverevents.cardevents.BooleanDecision;
import it.polimi.ingsw.net.events.serverevents.cardevents.CardFinished;
import it.polimi.ingsw.net.events.serverevents.cardevents.HumanCoordinates;
import it.polimi.ingsw.net.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.view.ParserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class AbandonedShipTest {
    private AbandonedShip abandonedShip;
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
            ship2 = createShip.createShip3(ship2);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        Game.initializeForTest(ship1, ship2);
        abandonedShip= new AbandonedShip(false, 1, 2, 3, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0117.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg");
    }

    @Test
    void getDays() {
        assertEquals(1, abandonedShip.getDays());
    }

    @Test
    void getCredits() {
        assertEquals(3, abandonedShip.getCredits());
    }

    @Test
    void getNumHuman() {
        assertEquals(2, abandonedShip.getNumHuman());
    }

    @Test
    void getCardState() {
        assertEquals(CardState.LAND, abandonedShip.getCardState());
    }

    @Test
    void startCard() {
        ServerEvent sEvent = abandonedShip.startCard();
        StartCardEvent event = (StartCardEvent) sEvent;
        assertEquals("Abandoned Ship", event.getCardName());
        assertEquals(abandonedShip.getDescription(), event.getCardDescription());
        assertEquals(Game.getIstance().getTruckerList().getFirst().getNickname(), event.getFirstPlayerNickname());
        assertEquals("Do you want to land on this Abandoned Ship?", event.getQuestion());
        assertEquals(abandonedShip.getDays(), event.getDays());
        assertEquals(abandonedShip.getCredits(), event.getCredits());
        assertEquals(abandonedShip.getNumHuman(), event.getNumHumans());
        assertNull(event.getGoods());
        assertNull(event.getMeteorites());
        assertEquals(-1, event.getPower());
        assertEquals(-1, event.getAstronautsLost());
        assertEquals(-1, event.getAstronautsLost());
        assertNull(event.getShoots());
        assertNull(event.getPlanets());
        assertNull(event.getConditionsOrder());
        assertNull(event.getPenalties());
        assertEquals(ParserState.LAND, event.getParserState());
    }

    @Test
    void truckerLanding() {
        try {
            ServerEvent sEvent = abandonedShip.truckerLanding(true);

            HumanCoordinates event = (HumanCoordinates) sEvent;

            assertEquals(ServerEventType.HUMAN_COORDINATES, event.getEventType());
            assertFalse(event.isForAll());
            assertEquals(Game.getIstance().getTrucker(abandonedShip.getIndexOfCurrTrucker()).getNickname(), event.getNickname());
        }catch (Exception e){
            fail();
        }
        if(abandonedShip.getIndexOfCurrTrucker()==Game.getIstance().getTruckerList().size()-1){
            try {
                ServerEvent sEvent = abandonedShip.truckerLanding(false);

                CardFinished event = (CardFinished) sEvent;



                Map<String, Integer> track = new HashMap<>();
                for (Trucker t : Game.getIstance().getTruckerList()) {
                    track.put(t.getNickname(), t.getPosition());
                }

                assertTrue(event.isForAll());
                assertEquals(Game.getIstance().getTruckerList(), event.getTruckersList());
                //assertEquals(new ArrayList<>(), event.getTruckerRemoved());
                assertEquals(track, event.getTrack());
            }catch (Exception e){
                fail();
            }
        }else{
            try {
                assertEquals( new BooleanDecision(ServerEventType.LAND, true, ParserState.LAND, "Do you want to land on this Abandoned Ship?",  Game.getIstance().getTrucker(abandonedShip.getIndexOfCurrTrucker()).getNickname()), abandonedShip.truckerLanding(false));
            }catch (Exception e){}
        }
    }

    @Test
    void setHumansCoordinates() {
        ArrayList<int[]> coordinates = new ArrayList<>();
        coordinates.add(new int[]{7,7});
        coordinates.add(new int[]{,});
        try {
            assertEquals(abandonedShip.cardEffect(), abandonedShip.setHumansCoordinates(coordinates));
        }catch(Exception e){
            fail();
        }
    }

    @Test
    void cardEffect() {
        ArrayList<String> truckerRemoved = Game.getIstance().endTurn();
        Map<String, Integer> track = new HashMap<>();
        for (Trucker t : Game.getIstance().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }
        try {
            assertEquals( new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, track), abandonedShip.cardEffect());
        }catch (Exception e){
            fail();
        }
    }
}