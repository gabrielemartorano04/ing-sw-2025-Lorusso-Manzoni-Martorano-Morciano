package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.CreateShipTest;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.events.serverevents.cardevents.BooleanDecision;
import it.polimi.ingsw.net.events.serverevents.cardevents.CardFinished;
import it.polimi.ingsw.net.events.serverevents.cardevents.PlotGood;
import it.polimi.ingsw.net.events.serverevents.cardevents.StartCardEvent;
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
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        try {
            ship2 = createShip.createShip3(ship2);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        Game.initializeForTest(ship1, ship2);
        ArrayList<Goods> goods2 = new ArrayList<>();
        goods2.addLast(new Goods(4));
        goods2.addLast(new Goods(4));
        abandonedStation= new AbandonedStation(false, 1, goods2, 1,6, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0120.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg");
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
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void truckerLanding_noTrucker(){
        try {
            ServerEvent sEvent = abandonedStation.truckerLanding(false);
            //non metto -1 al size perché io sto uno indietro rispetto a dopo aver chiamto truckerlanding()
            if(abandonedStation.getIndexOfCurrTrucker() == Game.getIstance().getTruckerList().size()){
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
            } else {
                BooleanDecision event = (BooleanDecision) sEvent;
                assertEquals(ServerEventType.LAND, event.getEventType());
                assertTrue(event.isForAll());
                assertEquals(ParserState.LAND, event.getParserState());
                assertEquals("Do you want to land on this AbandonedStation?", event.getMessage());
                assertEquals(Game.getIstance().getTruckerList().get(abandonedStation.getIndexOfCurrTrucker()).getNickname(), event.getNickname());
            }
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void setGoodsCoordinates() {
    }

    @Test
    void rearrange() {
    }

    @Test
    void cardEffect() {
    }
}