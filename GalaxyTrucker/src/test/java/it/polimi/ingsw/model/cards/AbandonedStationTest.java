package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.CreateShipTest;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.view.ParserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
class AbandonedStationTest {
    private AbandonedStation abandonedStation;
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
        abandonedStation= new AbandonedStation(false, 1, goods2, 1,6, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0120.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg", 1);
    }

    @Test
    void getDays() {
        assertEquals(1, abandonedStation.getDays());
    }

    @Test
    void getNumHuman() {
        assertEquals(6, abandonedStation.getNumHuman());
    }

    @Test
    void getGoods() {
        assertEquals(4, abandonedStation.getGoods().get(0).getValue());
        assertEquals(4, abandonedStation.getGoods().get(1).getValue());
    }

    @Test
    void getCardState() {
        assertEquals(CardState.LAND, abandonedStation.getCardState());
    }

    @Test
    void startCard() {
        ServerEvent sEvent = abandonedStation.startCard();
        StartCardEvent event = (StartCardEvent) sEvent;
        assertEquals("Abandoned Station", event.getCardName());
        assertEquals(abandonedStation.getDescription(), event.getCardDescription());
        assertEquals(Game.getIstance().getTruckerList().getFirst().getNickname(), event.getFirstPlayerNickname());
        assertEquals("Do you want to land on this Abandoned Station?", event.getQuestion());
        assertEquals(abandonedStation.getDays(), event.getDays());
        assertEquals(-1, event.getCredits());
        assertEquals(abandonedStation.getNumHuman(), event.getNumHumans());
        assertEquals(abandonedStation.getGoods(), event.getGoods());
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
            ServerEvent sEvent = abandonedStation.truckerLanding(true);
            if(Game.getIstance().getBank().getGoodsInBank(abandonedStation.getGoods().get(abandonedStation.getCurrGood()).getValue()) >= 1){
                PlotGood event = (PlotGood) sEvent;
                assertTrue(event.isForAll());
                assertEquals(Game.getIstance().getTrucker(abandonedStation.getIndexOfCurrTrucker()).getNickname(), event.getNickname());
                assertEquals(abandonedStation.getGoods().get(abandonedStation.getCurrGood()), event.getGood());
            } else {
                CardFinished event = (CardFinished) sEvent;
                Game.getIstance().getTruckerList().getFirst().getSpaceShip().removeAllHumans();

                assertEquals(Game.getIstance().getTruckerOut().stream()
                        .map(Trucker::getNickname)
                        .collect(Collectors.toCollection(ArrayList::new)), event.getTruckersRemoved());

                Map<String, Integer> track = new HashMap<>();
                for (Trucker t : Game.getIstance().getTruckerList()) {
                    track.put(t.getNickname(), t.getPosition());
                }

                assertTrue(event.isForAll());
                assertEquals(Game.getIstance().getTruckerList(), event.getTruckers());
                assertEquals(track, event.getTrack());
            }
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void truckerLanding_noTrucker(){
        try {
            ServerEvent sEvent = abandonedStation.truckerLanding(false);
           if(abandonedStation.getIndexOfCurrTrucker() == Game.getIstance().getTruckerList().size()){
                CardFinished event = (CardFinished) sEvent;

                Game.getIstance().getTruckerList().getFirst().getSpaceShip().removeAllHumans();

                assertEquals(Game.getIstance().getTruckerOut().stream()
                        .map(Trucker::getNickname)
                        .collect(Collectors.toCollection(ArrayList::new)), event.getTruckersRemoved());

                Map<String, Integer> track = new HashMap<>();
                for (Trucker t : Game.getIstance().getTruckerList()) {
                    track.put(t.getNickname(), t.getPosition());
                }

                assertTrue(event.isForAll());
                assertEquals(Game.getIstance().getTruckerList(), event.getTruckers());
                assertEquals(track, event.getTrack());
            } else {
                BooleanDecision event = (BooleanDecision) sEvent;
                assertTrue(event.isForAll());
                assertEquals(ParserState.LAND, event.getParserState());
                assertEquals(Game.getIstance().getTruckerList().get(abandonedStation.getIndexOfCurrTrucker()).getNickname(), event.getNickname());
            }
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addGood() {
        int[] goodsCoord = new int[2];
        goodsCoord[0] = 7;
        goodsCoord[1] = 8;
        abandonedStation.setCardState(CardState.GOODSTOADD);
        abandonedStation.setTruckerThatHasDecided(Game.getIstance().getTruckerList().getFirst());
        try {
            ServerEvent sEvent = abandonedStation.addGood(true, goodsCoord, 0);
            if(abandonedStation.getCurrGood()==abandonedStation.getGoods().size()){
                RearrangeEvent event = (RearrangeEvent) sEvent;
                assertFalse(event.isForAll());
                assertEquals(abandonedStation.getTruckerThatHasDecided().getNickname(), event.getNickname());
            }else{
                if (Game.getIstance().getBank().getGoodsInBank(abandonedStation.getGoods().get(abandonedStation.getCurrGood()).getValue()) >= 1){
                    PlotGood event = (PlotGood) sEvent;
                    assertTrue(event.isForAll());
                    assertEquals(Game.getIstance().getTrucker(abandonedStation.getIndexOfCurrTrucker()).getNickname(), event.getNickname());
                    assertEquals(abandonedStation.getGoods().get(abandonedStation.getCurrGood()), event.getGood());
                }else{
                    RearrangeEvent event = (RearrangeEvent) sEvent;
                    assertFalse(event.isForAll());
                    assertEquals(abandonedStation.getTruckerThatHasDecided().getNickname(), event.getNickname());
                }
            }
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void addGood_stop() {
        abandonedStation.setCardState(CardState.GOODSTOADD);
        abandonedStation.setTruckerThatHasDecided(Game.getIstance().getTruckerList().getFirst());
        try {
            ServerEvent sEvent = null;
            try {
                sEvent = abandonedStation.addGood(false, null, -1);
            } catch (InvalidCoordinates e) {
                throw new RuntimeException(e);
            } catch (InvalidPositionException e) {
                throw new RuntimeException(e);
            }
            PlotGood event = (PlotGood) sEvent;
            assertTrue(event.isForAll());
            assertEquals(abandonedStation.getTruckerThatHasDecided().getNickname(), event.getNickname());
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void rearrange() {
        abandonedStation.setCardState(CardState.REARRANGE);
        abandonedStation.setTruckerThatHasDecided(Game.getIstance().getTruckerList().getFirst());
        int[] coord1 = new int[2];
        coord1[0] = 7;
        coord1[1] = 8;
        try {
            Game.getIstance().getTruckerList().getFirst().getSpaceShip().addGood(new Goods(3), coord1,0, Game.getIstance().getBank());
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }

        int[] coord2= new int[2];
        coord2[0]=8;
        coord2[1]=9;
        ServerEvent sEvent = null;
        try {
            sEvent = abandonedStation.rearrange(coord1, coord2, 0, 0, false);
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }
        RearrangeEvent event = (RearrangeEvent) sEvent;
        assertFalse(event.isForAll());
        assertEquals(abandonedStation.getTruckerThatHasDecided().getNickname(), event.getNickname());
    }

    @Test
    void rearrange_stop() {
        abandonedStation.setCardState(CardState.REARRANGE);
        Game.getIstance().getTruckerList().getFirst().getSpaceShip().setNumHuman();
        ServerEvent sEvent = null;
        try {
            sEvent = abandonedStation.rearrange(null, null, 0, 0, true);
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }
        CardFinished event = (CardFinished) sEvent;
         Game.getIstance().getTruckerList().getFirst().getSpaceShip().removeAllHumans();

        assertEquals(Game.getIstance().getTruckerOut().stream()
                .map(Trucker::getNickname)
                .collect(Collectors.toCollection(ArrayList::new)), event.getTruckersRemoved());

        Map<String, Integer> track = new HashMap<>();
        for (Trucker t : Game.getIstance().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }

        assertTrue(event.isForAll());
        assertEquals(Game.getIstance().getTruckerList(), event.getTruckers());
        assertEquals(track, event.getTrack());
    }

    @Test
    void cardEffect() {
        abandonedStation.setCardState(CardState.ESECUZIONE);
        Game.getIstance().getTruckerList().getFirst().getSpaceShip().setNumHuman();
        abandonedStation.setTruckerThatHasDecided(Game.getIstance().getTruckerList().getFirst());
        ServerEvent sEvent = null;
        try {
            sEvent = abandonedStation.cardEffect();
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
        CardFinished event = (CardFinished) sEvent;

        Game.getIstance().getTruckerList().getFirst().getSpaceShip().removeAllHumans();

        assertEquals(Game.getIstance().getTruckerOut().stream()
                .map(Trucker::getNickname)
                .collect(Collectors.toCollection(ArrayList::new)), event.getTruckersRemoved());

        Map<String, Integer> track = new HashMap<>();
        for (Trucker t : Game.getIstance().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }

        assertTrue(event.isForAll());
        assertEquals(Game.getIstance().getTruckerList(), event.getTruckers());
        assertEquals(track, event.getTrack());
    }
}