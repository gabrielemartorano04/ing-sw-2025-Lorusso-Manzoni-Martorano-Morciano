package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.model.CreateShipTest;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.view.ParserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SlaversTest {

    private Slavers slavers;
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
        slavers = new Slavers(false, 1, 5, 1, 3, 2,
                "/it/polimi/ingsw/model/cards/GT-cards_I_IT_01.jpg",
                "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg", 9);
    }

    @Test
    void testStartCard() {
        StartCardEvent event = (StartCardEvent) slavers.startCard();
        assertEquals("Slavers", event.getCardName());
        assertEquals("Intergalactic slave traders", event.getCardDescription());
        assertEquals(1, event.getDays());
        assertEquals(5, event.getCredits());
        assertEquals(2, event.getPower());
        assertEquals(3, event.getAstronautsLost());
        assertEquals(ParserState.ACTIVATE_CANNONS, event.getParserState());
    }

    @Test
    void testGetters() {
        assertEquals(1, slavers.getDays());
        assertEquals(5, slavers.getCredits());
        assertEquals(2, slavers.getSlaversPower());
        assertEquals(3, slavers.getAstronautsLost());
        assertEquals(CardState.ACTIVATE_CANNONS, slavers.getCardState());
        assertEquals(CardState.ACTIVATE_CANNONS, slavers.getCardState());
    }

    @Test
    void testActivateCannonsWrongState() {
        assertThrows(GeneralError.class, () -> {
            slavers.gainCredits(true);
        });
    }

    @Test
    void testActivateCannonsFalsePath() throws Exception {
        ServerEvent e = slavers.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        assertNotNull(e);
    }

    @Test
    void testActivateCannonsTrueButNoCannons() throws Exception {
        Game.getIstance().getTrucker(0).getSpaceShip().deactivateDoubleCannons();
        ServerEvent e = slavers.activateCannons(true, new ArrayList<>(), new ArrayList<>());
        assertNotNull(e);
    }

    @Test
    void testActivateCannonsInvalidCoords() {
        ArrayList<int[]> wrong = new ArrayList<>();
        wrong.add(new int[]{9, 9});
        assertThrows(InvalidComponentsException.class, () -> {
            slavers.activateCannons(true, wrong, new ArrayList<>());
        });
    }

    @Test
    void testActivateCannonsInvalidBatteries() {
        ArrayList<int[]> cannons = new ArrayList<>();
        cannons.add(new int[]{5, 8});
        ArrayList<int[]> batteries = new ArrayList<>();
        batteries.add(new int[]{9, 9});
        assertThrows(InvalidComponentsException.class, () -> {
            slavers.activateCannons(true, cannons, batteries);
        });
    }

    @Test
    void testActivateCannonsSuccess() throws Exception {
        ArrayList<int[]> cannons = new ArrayList<>();
        cannons.add(new int[]{5, 8});
        ArrayList<int[]> batteries = new ArrayList<>();
        batteries.add(new int[]{5, 6});
        ServerEvent e = slavers.activateCannons(true, cannons, batteries);
        assertNotNull(e);
    }

    @Test
    void testGainCreditsTrueAndDefeatedEmpty() throws Exception {
        slavers.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        ServerEvent e = slavers.gainCredits(true);
        assertEquals(CardFinished.class, e.getClass());
    }

    @Test
    void testRemoveHumansTooFewCrew() throws Exception {
        slavers.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        slavers.gainCredits(true);
        Game.getIstance().getTrucker(1).getSpaceShip().removeAllHumans();
        ArrayList<int[]> empty = new ArrayList<>();
        slavers.setState(CardState.HUMANSTOREMOVE);
        slavers.addDefeatedTruckers(Game.getIstance().getTrucker(1));
        ServerEvent e = slavers.removeHumans(empty);
        assertNotNull(e);
    }

    @Test
    void testRemoveHumansValid() throws Exception {
        slavers.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        slavers.gainCredits(true);
        ArrayList<int[]> coords = new ArrayList<>();
        coords.add(new int[]{6, 5, 2});
        coords.add(new int[]{7, 6, 1});
        slavers.setState(CardState.HUMANSTOREMOVE);
        slavers.addDefeatedTruckers(Game.getIstance().getTruckerList().get(1));
        ServerEvent e = slavers.removeHumans(coords);
        assertNotNull(e);
    }

    @Test
    void testRemoveHumansInvalidCoords() throws Exception {
        slavers.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        slavers.gainCredits(true);
        ArrayList<int[]> wrong = new ArrayList<>();
        wrong.add(new int[]{9, 9, 0});
        slavers.setState(CardState.HUMANSTOREMOVE);
        slavers.addDefeatedTruckers(Game.getIstance().getTrucker(0));
        assertThrows(InvalidCoordinates.class, () -> {
            slavers.removeHumans(wrong);
        });
    }

    @Test
    void testCardEffectWrongState() {
        assertThrows(GeneralError.class, () -> slavers.cardEffect());
    }

    @Test
    void testCardEffectCorrect() throws Exception {
        slavers.activateCannons(false, new ArrayList<>(), new ArrayList<>());
        slavers.gainCredits(true);
        ServerEvent e = slavers.cardEffect();
        assertEquals(CardFinished.class, e.getClass());
    }

    @Test
    void testHandleDraw(){
        try {
            ship1 = createShip.createShip3(new SpaceShip());
            ship1.setBattery();
            ship1.setNumHuman();
            ship2 = createShip.createShip3(new SpaceShip());
            ship2.setBattery();
            ship2.setNumHuman();
            Game.initializeForTest(ship1, ship2);
            //non ultimo
            slavers = new Slavers(false, 1, 5, 1, 3, 2,
                    "/it/polimi/ingsw/model/cards/GT-cards_I_IT_01.jpg",
                    "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg", 9);
            slavers.setState(CardState.ACTIVATE_CANNONS);
            slavers.setIndexOfCurrentTrucker(0);
            ServerEvent event = slavers.activateCannons(false, new ArrayList<>(), new ArrayList<>());
            switch(event){
                case CardFinished cf-> assertEquals(Game.getIstance().getTruckerList(), cf.getTruckers());
                case HumanCoordinates hc -> assertEquals(slavers.getDefeatedTruckers().getFirst().getNickname(), hc.getNickname());
                case BooleanDecision bd -> assertEquals(Game.getIstance().getTrucker(slavers.getIndexOfCurrentTrucker()).getNickname(), bd.getNickname());
                default -> fail();
            }
            // ultimo e no defeated
            slavers.setState(CardState.ACTIVATE_CANNONS);
            ServerEvent event1 = slavers.activateCannons(false, new ArrayList<>(), new ArrayList<>());
            slavers.setIndexOfCurrentTrucker(1);
            switch(event1){
                case CardFinished cf-> assertEquals(Game.getIstance().getTruckerList(), cf.getTruckers());
                default -> fail();
            }
            //ultimo e defeated
            slavers.setState(CardState.ACTIVATE_CANNONS);
            slavers.addDefeatedTruckers(Game.getIstance().getTrucker(0));
            ServerEvent event2 = slavers.activateCannons(false, new ArrayList<>(), new ArrayList<>());
            slavers.setIndexOfCurrentTrucker(1);
            switch(event2){
                case HumanCoordinates hc -> assertEquals(Game.getIstance().getTrucker(slavers.getIndexOfCurrentTrucker()-1).getNickname(), hc.getNickname());
                default -> fail();
            }
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        } catch (InvalidComponentsException e) {
            throw new RuntimeException(e);
        } catch (NotEnoughBatteries | InvalidCoordinates e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void testHandleDefeat(){
        try {
            ship1 = createShip.createShip1(new SpaceShip());
            ship1.setBattery();
            ship1.setNumHuman();
            ship2 = createShip.createShip3(new SpaceShip());
            ship2.setBattery();
            ship2.setNumHuman();
            Game.initializeForTest(ship1, ship2);
            //no ultimo
            slavers = new Slavers(false, 1, 5, 1, 3, 4,
                    "/it/polimi/ingsw/model/cards/GT-cards_I_IT_01.jpg",
                    "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg", 9);
            slavers.setState(CardState.ACTIVATE_CANNONS);
            ServerEvent event = slavers.activateCannons(false, new ArrayList<>(), new ArrayList<>());
            switch(event){
                case BooleanDecision bd -> assertEquals(Game.getIstance().getTrucker(slavers.getIndexOfCurrentTrucker()).getNickname(), bd.getNickname());
                default -> fail();
            }
            slavers = new Slavers(false, 1, 5, 1, 3, 4,
                    "/it/polimi/ingsw/model/cards/GT-cards_I_IT_01.jpg",
                    "/it/polimi/ingsw/model/cards/GT-cards_I_IT_0121.jpg", 9);
            slavers.setState(CardState.ACTIVATE_CANNONS);
            slavers.setIndexOfCurrentTrucker(1);
            ServerEvent event1 = slavers.activateCannons(false, new ArrayList<>(), new ArrayList<>());
            switch(event1){
                case HumanCoordinates hc -> assertEquals(slavers.getDefeatedTruckers().getFirst().getNickname(), hc.getNickname());
                default -> fail();
            }
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        } catch (InvalidComponentsException e) {
            throw new RuntimeException(e);
        } catch (NotEnoughBatteries e) {
            throw new RuntimeException(e);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
    }


}