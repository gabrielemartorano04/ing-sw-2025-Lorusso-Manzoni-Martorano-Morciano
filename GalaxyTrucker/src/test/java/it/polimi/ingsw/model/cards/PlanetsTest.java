package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlanetsTest {

    private Planets card;
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

        ArrayList<ArrayList<Goods>> planets = new ArrayList<>();
        planets.add(new ArrayList<>(Arrays.asList(new Goods(2), new Goods(4))));
        planets.add(new ArrayList<>(Arrays.asList(new Goods(1))));
        card = new Planets(false, 2, 1, planets, "", "", 0);
    }

    @Test
    void testStartCard() {
        StartCardEvent e = (StartCardEvent) card.startCard();
        assertEquals("Planets", e.getCardName());
        assertEquals(2, e.getPlanets().size());
    }

    @Test
    void testPlayerLanding() throws Exception {
        ServerEvent ev1 = card.playerLandingPlanets(0);
        assertEquals(1, card.getTruckersLanded().size());
        ServerEvent ev2 = card.playerLandingPlanets(-1);

        switch (ev2){
            case PlotGood pg -> assertEquals(Game.getIstance().getTruckerList().get(card.getIndexOfTrucker()).getNickname(), pg.getNickname());
            case PlanetDecision pd -> assertEquals(Game.getIstance().getTruckerList().get(card.getIndexOfTrucker()).getNickname(), pd.getNickname());
            default -> fail();

        }
    }

    @Test
    void testInvalidLandingIndex() {
        assertThrows(GeneralError.class, () -> card.playerLandingPlanets(99));
    }

    @Test
    void testOccupiedPlanet() throws Exception {
        card.playerLandingPlanets(0);
        card.setTruckersLanded(new ArrayList<>());
        assertThrows(PlanetNotFreeException.class, () -> card.playerLandingPlanets(0));
    }

    @Test
    void testAddGoodAccept() throws Exception {
        card.playerLandingPlanets(0);
        card.playerLandingPlanets(-1);
        card.setCurrGood(new Goods(2));
        card.setState(CardState.GOODSTOADD);
        int[] coord = {9, 8};
        ServerEvent ev = card.addGood(true, coord, 0);
        assertNotNull(ev);
    }

    @Test
    void testAddGoodReject() throws Exception {
        card.playerLandingPlanets(0);
        card.playerLandingPlanets(-1);
        card.setCurrGood(new Goods(2));
        card.setState(CardState.GOODSTOADD);
        ServerEvent ev = card.addGood(false, null, 0);
        assertNotNull(ev);
    }

    @Test
    void testAddGoodInvalid() throws Exception {
        card.playerLandingPlanets(0);
        card.playerLandingPlanets(-1);
        card.setCurrGood(new Goods(2));
        card.setState(CardState.GOODSTOADD);
        int[] invalidCoord = {1, 1};
        assertThrows(InvalidCoordinates.class, () -> card.addGood(true, invalidCoord, 0));
    }

    @Test
    void testRearrangeStopTrue() throws Exception {
        card.playerLandingPlanets(0);
        card.playerLandingPlanets(-1);
        card.setState(CardState.REARRANGE);
        card.setTruckersLanded(new ArrayList<>(List.of(Game.getIstance().getTruckerList().get(0))));
        ServerEvent e = card.rearrange(null, null, 0, 0, true);
        switch (e){
            case CardFinished cf -> assertEquals(Game.getIstance().getTruckerList(), cf.getTruckers());
            default -> fail();
        }
    }

    @Test
    void testRearrangeInvalid() throws Exception {
        card.playerLandingPlanets(0);
        card.playerLandingPlanets(-1);
        card.setState(CardState.REARRANGE);
        assertThrows(InvalidPositionException.class, () -> card.rearrange(new int[]{0,0}, null, 0, 0, false));
    }

    @Test
    void testCardEffect() throws Exception {
        card.setState(CardState.ESECUZIONE);
        card.setTruckersLanded(new ArrayList<>(List.of(Game.getIstance().getTruckerList().get(0))));
        ServerEvent ev = card.cardEffect();
        switch (ev){
            case CardFinished cf -> assertEquals(Game.getIstance().getTruckerList(), cf.getTruckers());
            default -> fail();
        }
    }

    @Test
    void testCardEffectWrongState() {
        assertThrows(GeneralError.class, () -> card.cardEffect());
    }

    @Test
    void testProceedToNextAvailableGoodOrRearrange() {
        card.setState(CardState.GOODSTOADD);
        card.setTruckersLanded(new ArrayList<>(List.of(Game.getIstance().getTruckerList().get(0))));
        card.getCurrentGoodsList().add(new Goods(4));
        card.setCurrGood(new Goods(4));
        ServerEvent ev = null;
        try {
            ev = card.addGood(false, null, 0);
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(ev);
    }

    @Test
    void test_playerLandingPlanets_invalidIndex() {
        assertThrows(GeneralError.class, () -> card.playerLandingPlanets(5));
    }

    @Test
    void test_playerLandingPlanets_duplicatedLanding() throws GeneralError, PlanetNotFreeException {
        card.playerLandingPlanets(0); // prima volta
        Game.getIstance().getTruckerList().add(Game.getIstance().getTrucker(0));// simulate che stesso trucker venga ripetuto
        card.playerLandingPlanets(1);
        assertThrows(GeneralError.class, () -> card.playerLandingPlanets(-1));
    }

    @Test
    void test_playerLandingPlanets_planetAlreadyTaken() throws GeneralError, PlanetNotFreeException {
        card.playerLandingPlanets(0); // prima occupazione
        card.setTruckersLanded(new ArrayList<>(Game.getIstance().getTruckerList())); // finta avanzamento
        card.setState(CardState.PIANETI);
        card.getPlanetsOccupied().set(0, Game.getIstance().getTrucker(0)); // già occupato
        Game.getIstance().getTruckerList().add(new Trucker("Fake", null)); // aggiunto finto
        assertThrows(PlanetNotFreeException.class, () -> card.playerLandingPlanets(0));
    }

    @Test
    void test_addGood_invalidCoords() {
        card.setState(CardState.GOODSTOADD);
        card.setTruckersLanded(Game.getIstance().getTruckerList());
        assertThrows(InvalidCoordinates.class, () -> card.addGood(true, new int[]{0, 0}, 0));
    }

    @Test
    void test_rearrange_stopTrue() throws GeneralError, InvalidCoordinates, InvalidPositionException {
        card.setState(CardState.REARRANGE);
        ServerEvent event = card.rearrange(null, null, 0, 0, true);
        switch (event){
            case CardFinished cf -> assertEquals(Game.getIstance().getTruckerList(), cf.getTruckers());
            default -> fail();
        }
    }

    @Test
    void test_cardEffect_invalidState() {
        card.setState(CardState.GOODSTOADD);
        assertThrows(GeneralError.class, () -> card.cardEffect());
    }

    @Test
    void test_getCardState(){
        assertEquals(CardState.PIANETI, card.getCardState());
    }

    @Test
    void testRearrange_ProceedToNextTrucker() throws Exception {
        ArrayList<Goods> goodsPlanet1 = new ArrayList<>();
        goodsPlanet1.add(new Goods(2));
        ArrayList<Goods> goodsPlanet2 = new ArrayList<>();
        goodsPlanet2.add(new Goods(1));
        ArrayList<ArrayList<Goods>> planets = new ArrayList<>();
        planets.add(goodsPlanet1);
        planets.add(goodsPlanet2);

        Planets card = new Planets(false, 1, 1, planets, "", "", 1);
        card.setState(CardState.REARRANGE);

        card.getTruckersLanded().add(Game.getIstance().getTrucker(0));
        card.getTruckersLanded().add(Game.getIstance().getTrucker(1));
        card.getPlanetsOccupied().set(0, Game.getIstance().getTrucker(0));
        card.getPlanetsOccupied().set(1, Game.getIstance().getTrucker(1));

        card.setCurrGood(new Goods(2));
        card.getCurrentGoodsList().add(new Goods(2));

        ServerEvent ev = card.rearrange(null, null, 0, 0, true);
        switch (ev){
            case PlotGood pg -> assertEquals("Bob", pg.getNickname());
            default -> fail();
        }
    }

}