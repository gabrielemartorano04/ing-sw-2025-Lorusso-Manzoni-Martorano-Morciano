package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.GeneralError;
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
import java.util.stream.Collectors;

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

                // a uno tolgo tutti gli umani per vedere se lo elimina, l'altro ne avrà di più e non verrà eliminato

                Game.getIstance().getTruckerList().getFirst().getSpaceShip().removeAllHumans();

                assertEquals(Game.getIstance().getTruckerOut().stream()
                        .map(Trucker::getNickname)
                        .collect(Collectors.toCollection(ArrayList::new)), event.getTruckersRemoved());

                Map<String, Integer> track = new HashMap<>();
                for (Trucker t : Game.getIstance().getTruckerList()) {
                    track.put(t.getNickname(), t.getPosition());
                }

                assertTrue(event.isForAll());
                assertEquals(Game.getIstance().getTruckerList(), event.getTruckersList());
                assertEquals(track, event.getTrack());
            }catch (Exception e){
                fail();
            }
        }else{
            try {
                ServerEvent sEvent = abandonedShip.truckerLanding(false);
                BooleanDecision event = (BooleanDecision) sEvent;

                assertEquals(ServerEventType.LAND, event.getEventType());
                assertTrue(event.isForAll());
                assertEquals(Game.getIstance().getTrucker(abandonedShip.getIndexOfCurrTrucker()).getNickname(), event.getNickname());
                assertEquals("Do you want to land on this Abandoned Ship?", event.getMessage());
                assertEquals(ParserState.LAND, event.getParserState());
            }catch (Exception e){}
        }
    }

    @Test
    void setHumansCoordinates() {
        ArrayList<int[]> coordinates = new ArrayList<>();
        coordinates.add(new int[]{7,7, 1});
        coordinates.add(new int[]{6,5, 1});

        abandonedShip.setState(CardState.HUMANSTOREMOVE);
        abandonedShip.setTruckerThatHasDecided(Game.getIstance().getTruckerList().getFirst());
        Game.getIstance().getTruckerList().getFirst().getSpaceShip().setNumHuman();

        ServerEvent sEvent = null;
        try {
            sEvent = abandonedShip.setHumansCoordinates(coordinates);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }

        CardFinished event = (CardFinished) sEvent;

        // a uno tolgo tutti gli umani per vedere se lo elimina, l'altro ne avrà di più e non verrà eliminato

        Game.getIstance().getTruckerList().getFirst().getSpaceShip().removeAllHumans();

        assertEquals(Game.getIstance().getTruckerOut().stream()
                .map(Trucker::getNickname)
                .collect(Collectors.toCollection(ArrayList::new)), event.getTruckersRemoved());

        Map<String, Integer> track = new HashMap<>();
        for (Trucker t : Game.getIstance().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }

        assertTrue(event.isForAll());
        assertEquals(Game.getIstance().getTruckerList(), event.getTruckersList());
        assertEquals(track, event.getTrack());
    }

    @Test
    void cardEffect() {
        abandonedShip.setState(CardState.HUMANSTOREMOVE);
        abandonedShip.setTruckerThatHasDecided(Game.getIstance().getTruckerList().getFirst());
        Game.getIstance().getTruckerList().getFirst().getSpaceShip().setNumHuman();
        Game.getIstance().getTruckerList().get(1).getSpaceShip().setNumHuman();
        ArrayList<int[]> coordinates = new ArrayList<>();
        coordinates.add(new int[]{7,7, 1});
        coordinates.add(new int[]{6,5, 1});
        try {
            abandonedShip.setHumansCoordinates(coordinates);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }

        abandonedShip.setState(CardState.ESECUZIONE);
        //con trucker che atterrno
        ServerEvent sEvent = null;
        try {
            sEvent = abandonedShip.cardEffect();
        }catch (GeneralError e) {
            throw new RuntimeException(e);
        }

        CardFinished event = (CardFinished) sEvent;

        // a uno tolgo tutti gli umani per vedere se lo elimina, l'altro ne avrà di più e non verrà eliminato

        Game.getIstance().getTruckerList().getFirst().getSpaceShip().removeAllHumans();

        assertEquals(Game.getIstance().getTruckerOut().stream()
                .map(Trucker::getNickname)
                .collect(Collectors.toCollection(ArrayList::new)), event.getTruckersRemoved());

        Map<String, Integer> track = new HashMap<>();
        for (Trucker t : Game.getIstance().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }

        assertTrue(event.isForAll());
        assertEquals(Game.getIstance().getTruckerList(), event.getTruckersList());
        assertEquals(track, event.getTrack());

    }

    @Test
    void cardEffect_noLanding() {
        abandonedShip.setState(CardState.ESECUZIONE);
        Game.getIstance().getTruckerList().getFirst().getSpaceShip().setNumHuman();

        //senza trucker che atterrno
        ServerEvent sEvent = null;
        try {
            sEvent = abandonedShip.cardEffect();
        }catch (GeneralError e) {
            throw new RuntimeException(e);
        }

        CardFinished event = (CardFinished) sEvent;

        // a uno tolgo tutti gli umani per vedere se lo elimina, l'altro ne avrà di più e non verrà eliminato

        Game.getIstance().getTruckerList().getFirst().getSpaceShip().removeAllHumans();

        assertEquals(Game.getIstance().getTruckerOut().stream()
                .map(Trucker::getNickname)
                .collect(Collectors.toCollection(ArrayList::new)), event.getTruckersRemoved());

        Map<String, Integer> track = new HashMap<>();
        for (Trucker t : Game.getIstance().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }

        assertTrue(event.isForAll());
        assertEquals(Game.getIstance().getTruckerList(), event.getTruckersList());
        assertEquals(track, event.getTrack());

    }


}