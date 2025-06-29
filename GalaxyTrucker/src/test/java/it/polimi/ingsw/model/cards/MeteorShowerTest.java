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
import it.polimi.ingsw.net.events.serverevents.cardevents.RollDiceResult;
import it.polimi.ingsw.net.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.view.ParserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MeteorShowerTest {

    private MeteorShower meteorShower;
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
        ArrayList<int[]> direction1 = new ArrayList<>();
        direction1.addLast(new int[]{0,0});
        direction1.addLast(new int[]{0,3});
        direction1.addLast(new int[]{0,1});
        meteorShower = new MeteorShower(true, direction1, 1, "/it/polimi/ingsw/model/cards/GT-cards_I_IT_019.jpg", "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg",6);
    }

    @Test
    void getCardState() {
        assertEquals(CardState.ROLL_DICE, meteorShower.getCardState());
    }

    @Test
    void startCard() {
        ServerEvent event = meteorShower.startCard();
        StartCardEvent e = (StartCardEvent) event;
        assertEquals(meteorShower.getName(), e.getCardName());
        assertEquals(meteorShower. getDescription(), e.getCardDescription());
        assertEquals(Game.getIstance().getTruckerList().getFirst().getNickname(), e.getFirstPlayerNickname());
        assertEquals(-1, e.getDays());
        assertEquals(-1, e.getCredits());
        assertEquals(-1, e.getNumHumans());
        assertNull(e.getGoods());
        assertEquals(meteorShower.getMeteors(), e.getMeteorites());
        assertEquals(-1, e.getPower());
        assertEquals(-1, e.getAstronautsLost());
        assertEquals(-1, e.getGoodsLost());
        assertNull(e.getShoots());
        assertNull(e.getPlanets());
        assertNull(e.getConditionsOrder());
        assertNull(e.getPenalties());
        assertEquals(ParserState.ROLL_DICE, e.getParserState());
        assertEquals(this.meteorShower, e.getCard());

    }

    @Test
    void rollDice() {
        try {
            ServerEvent event = meteorShower.rollDice();
            RollDiceResult e = (RollDiceResult) event;
            if(meteorShower.getMeteors().get(meteorShower.getCurrMeteorIndex())[0]==1){
                assertEquals(ParserState.ACTIVATE_CANNONS, e.getParserState());
                assertEquals(meteorShower.getCurrMeteorIndex(), e.getCurrIndex());
                assertNull(e.getShoot());
                assertEquals(meteorShower.getMeteors().get(meteorShower.getCurrMeteorIndex()), e.getMeteor());
                assertEquals(Game.getIstance().getTrucker(meteorShower.getIndexOfCurrTrucker()).getNickname(), e.getNickname());
                assertTrue(e.isForAll());
            }else{
                assertEquals(ParserState. ACTIVATE_SHIELD, e.getParserState());
                assertEquals(meteorShower.getCurrMeteorIndex(), e.getCurrIndex());
                assertNull(e.getShoot());
                assertEquals(meteorShower.getMeteors().get(meteorShower.getCurrMeteorIndex()-1), e.getMeteor());
                assertEquals(Game.getIstance().getTrucker(meteorShower.getIndexOfCurrTrucker()).getNickname(), e.getNickname());
                assertTrue(e.isForAll());
            }
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void activateShield() {
        int[] coordBatt = {6, 9};
        int[] shieldCoord = {8, 8};

        meteorShower.setState(CardState.ACTIVATE_SHIELDS);
        try {
            ServerEvent event = meteorShower.activateShield(true, shieldCoord, coordBatt);
            if(meteorShower.getIndexOfCurrTrucker() == Game.getIstance().getTruckerList().size()-1){
                if(meteorShower.getMeteors().size()==meteorShower.getCurrMeteorIndex()) {
                    CardFinished e = (CardFinished) event;
                    assertTrue(e.isForAll());
                    assertEquals(Game.getIstance().getTruckerList(), e.getTruckers());
                    assertEquals(Game.getIstance().getTrack(), e.getTrack());
                    assertEquals(meteorShower.getTruckerRemoved(), e.getTruckersRemoved());
                }else{
                    BooleanDecision e=(BooleanDecision) event;
                    assertEquals(ParserState.ACTIVATE_SHIELD, e.getParserState());
                    assertTrue(e.isForAll());
                    assertEquals(Game.getIstance().getTruckerList().get(meteorShower.getIndexOfCurrTrucker()).getNickname(), e.getNickname());
                }

            }else{
                BooleanDecision e=(BooleanDecision) event;
                assertEquals(ParserState.ACTIVATE_SHIELD, e.getParserState());
                assertTrue(e.isForAll());
                assertEquals("vuoi attivare uno scudo?", e.getMessage());
                assertEquals(Game.getIstance().getTrucker(meteorShower.getIndexOfCurrTrucker()).getNickname(), e.getNickname());
            }

        } catch (GeneralError e) {
            throw new RuntimeException(e);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }



    }

    @Test
    void activateCannons() {
        ArrayList<int[]> coordBatt = new ArrayList<>();
        ArrayList<int[]> cannonsCoord = new ArrayList<>();
        coordBatt.add(new int[]{5, 6});
        cannonsCoord.add(new int[]{5, 8});
        meteorShower.setState(CardState.ACTIVATE_CANNONS);
        try {
            ServerEvent event = meteorShower.activateCannons(true, cannonsCoord, coordBatt);
            if(meteorShower.getIndexOfCurrTrucker() == Game.getIstance().getTruckerList().size()-1){
                if(meteorShower.getMeteors().size()==meteorShower.getCurrMeteorIndex()) {
                    CardFinished e = (CardFinished) event;
                    assertTrue(e.isForAll());
                    assertEquals(Game.getIstance().getTruckerList(), e.getTruckers());
                    assertEquals(Game.getIstance().getTrack(), e.getTrack());
                    assertEquals(meteorShower.getTruckerRemoved(), e.getTruckersRemoved());
                }else{
                    BooleanDecision e=(BooleanDecision) event;
                    assertEquals(ParserState.ACTIVATE_CANNONS, e.getParserState());
                    assertTrue(e.isForAll());
                    assertEquals(Game.getIstance().getTruckerList().get(meteorShower.getIndexOfCurrTrucker()).getNickname(), e.getNickname());
                }

            }else{
                BooleanDecision e=(BooleanDecision) event;
                assertEquals(ParserState.ACTIVATE_CANNONS, e.getParserState());
                assertTrue(e.isForAll());
                assertEquals("vuoi attivare uno scudo?", e.getMessage());
                assertEquals(Game.getIstance().getTrucker(meteorShower.getIndexOfCurrTrucker()).getNickname(), e.getNickname());
            }
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void cardEffect() {
        ServerEvent event = null;
        meteorShower.setState(CardState.ESECUZIONE);
        try {
            meteorShower.setMeteor();
            event = meteorShower.cardEffect();
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
        if(meteorShower.getMeteors().size()==meteorShower.getCurrMeteorIndex()) {
            CardFinished e = (CardFinished) event;
            assertTrue(e.isForAll());
            assertEquals(Game.getIstance().getTruckerList(), e.getTruckers());
            assertEquals(Game.getIstance().getTrack(), e.getTrack());
            assertEquals(meteorShower.getTruckerRemoved(), e.getTruckersRemoved());
        }else{
            BooleanDecision e=(BooleanDecision) event;
            assertEquals(ParserState.ROLL_DICE, e.getParserState());
            assertTrue(e.isForAll());
            assertEquals(Game.getIstance().getTruckerList().getFirst().getNickname(), e.getNickname());
        }
    }

    @Test
    void getMeteors() {
        ArrayList<int[]> direction1 = new ArrayList<>();
        direction1.addLast(new int[]{0,0});
        direction1.addLast(new int[]{0,3});
        direction1.addLast(new int[]{0,1});
        for(int i=0; i<meteorShower.getMeteors().size(); i++) {
            assertEquals(direction1.get(i)[0], meteorShower.getMeteors().get(i)[0]);
            assertEquals(direction1.get(i)[1], meteorShower.getMeteors().get(i)[1]);

        }
    }

    @Test
    void getCurrMeteorIndex() {
        assertEquals(0, meteorShower.getCurrMeteorIndex());
    }

    @Test
    void getIndexOfCurrTrucker() {
        assertEquals(0, meteorShower.getIndexOfCurrTrucker());
    }

    @Test
    void getTruckerRemoved() {
        assertTrue(meteorShower.getTruckerRemoved().isEmpty());
    }
}