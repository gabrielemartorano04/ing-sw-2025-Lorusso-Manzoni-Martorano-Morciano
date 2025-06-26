
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.CreateShipTest;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.model.components.Load;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SmugglersTest {

    private Smugglers smugglers;
    private SpaceShip ship1, ship2;
    private CreateShipTest createShip = new CreateShipTest();
    private Goods[] goodsGained1 = new Goods[3];


    @BeforeEach
    void setUp() throws InvalidCoordinates {
        ship1 = createShip.createShip1(new SpaceShip());
        ship1.setBattery();
        ship1.setNumHuman();
        ship2 = createShip.createShip3(new SpaceShip());
        ship2.setBattery();
        ship2.setNumHuman();
        Game.initializeForTest(ship1, ship2);
        goodsGained1[0] = new Goods(3);
        goodsGained1[1] = new Goods(2);
        goodsGained1[2] = new Goods(1);
        smugglers = new Smugglers(true, 3, 2, 1, goodsGained1, 1, "front.jpg", "back.jpg", 1);
    }

    @Test
    void testStartCard() {
        ServerEvent e = smugglers.startCard();
        assertEquals("Smugglers", ((StartCardEvent)e).getCardName());
    }

    @Test
    void testActivateCannonsVictory() throws Exception {
        ArrayList<int[]> cannons = new ArrayList<>();
        ArrayList<int[]> batteries = new ArrayList<>();
        cannons.add(new int[]{5, 8});
        batteries.add(new int[]{5, 6, 1});
        ServerEvent e = smugglers.activateCannons(true, cannons, batteries);
        assertTrue(smugglers.getPower()<Game.getIstance().getTruckerList().get(smugglers.getIndexOfCurrentTrucker()).getSpaceShip().getTotalCannonPower());
        assertTrue(e instanceof PlotGood);
    }

    @Test
    void testActivateCannonsDraw() throws Exception {
        ship1 = createShip.createShip3(new SpaceShip());
        ship1.setBattery();
        ship1.setNumHuman();
        ship2 = createShip.createShip3(new SpaceShip());
        ship2.setBattery();
        ship2.setNumHuman();
        Game.initializeForTest(ship1, ship2);
        Game.getIstance().getTrucker(0).getSpaceShip().deactivateDoubleCannons();
        smugglers = new Smugglers(true, 2, 2, 1, goodsGained1, 1, "front.jpg", "back.jpg", 1);
        ServerEvent e = smugglers.activateCannons(false, null, null);
        assertTrue(smugglers.getPower()==Game.getIstance().getTruckerList().get(smugglers.getIndexOfCurrentTrucker()).getSpaceShip().getTotalCannonPower());
        switch(e){
            case BooleanDecision bl -> assertTrue(true);
            default -> fail();
        }
        //ultimo no defeated
        ship1 = createShip.createShip3(new SpaceShip());
        ship1.setBattery();
        ship1.setNumHuman();
        ship2 = createShip.createShip3(new SpaceShip());
        ship2.setBattery();
        ship2.setNumHuman();
        Game.initializeForTest(ship1, ship2);
        Game.getIstance().getTrucker(0).getSpaceShip().deactivateDoubleCannons();
        smugglers = new Smugglers(true, 2, 2, 1, goodsGained1, 1, "front.jpg", "back.jpg", 1);
        smugglers.setIndexOfCurrentTrucker(1);
        ServerEvent e1 = smugglers.activateCannons(false, null, null);
        assertTrue(smugglers.getPower()==Game.getIstance().getTruckerList().get(smugglers.getIndexOfCurrentTrucker()).getSpaceShip().getTotalCannonPower());
        switch(e1){
            case CardFinished cf -> assertTrue(true);
            default -> fail();
        }
        //ultimo e defeated
        ship1 = createShip.createShip3(new SpaceShip());
        ship1.setBattery();
        ship1.setNumHuman();
        ship2 = createShip.createShip3(new SpaceShip());
        ship2.setBattery();
        ship2.setNumHuman();
        Game.initializeForTest(ship1, ship2);
        Game.getIstance().getTrucker(0).getSpaceShip().deactivateDoubleCannons();
        smugglers = new Smugglers(true, 2, 2, 1, goodsGained1, 1, "front.jpg", "back.jpg", 1);
        smugglers.addDefeatedTruckers(Game.getIstance().getTrucker(0));
        smugglers.setIndexOfCurrentTrucker(1);
        ServerEvent e2 = smugglers.activateCannons(false, null, null);
        assertTrue(smugglers.getPower()==Game.getIstance().getTruckerList().get(smugglers.getIndexOfCurrentTrucker()).getSpaceShip().getTotalCannonPower());


    }

    @Test
    void testActivateCannonsDefeat() throws Exception {
        ship1 = createShip.createShip3(new SpaceShip());
        ship1.setBattery();
        ship1.setNumHuman();
        ship2 = createShip.createShip3(new SpaceShip());
        ship2.setBattery();
        ship2.setNumHuman();
        Game.initializeForTest(ship1, ship2);
        Game.getIstance().getTrucker(0).getSpaceShip().deactivateDoubleCannons();
        smugglers = new Smugglers(true, 4, 2, 1, goodsGained1, 1, "front.jpg", "back.jpg", 1);
        ServerEvent e = smugglers.activateCannons(false, null, null);
        assertTrue(smugglers.getPower()>Game.getIstance().getTruckerList().get(smugglers.getIndexOfCurrentTrucker()).getSpaceShip().getTotalCannonPower());
        switch(e){
            case BooleanDecision bl -> assertTrue(true);
            default -> fail();
        }
    }


    @Test
    void testAddGoodAccepted() throws Exception {
        testActivateCannonsVictory();
        int[] coords = new int[]{7, 8};
        ServerEvent e = smugglers.addGood(true, coords, 0);
        switch(e){
            case PlotGood bl -> assertTrue(true);
            case RearrangeEvent re -> assertTrue(true);
            default -> fail();
        }
    }

    @Test
    void testAddGoodRefused() throws Exception {
        testActivateCannonsVictory();
        ServerEvent e = smugglers.addGood(false, null, 0);
        switch(e){
            case PlotGood pg -> assertTrue(true);
            case RearrangeEvent re -> assertTrue(true);
            default -> fail();
        }
    }

    @Test
    void testRearrangeAndStop() throws Exception {
        testAddGoodAccepted();
        smugglers.setCardState(CardState.REARRANGE);
        ServerEvent e = smugglers.rearrange(null, null, 0, 0, true);
        switch(e){
            case RemotionConfirmed rc -> assertTrue(true);
            case CardFinished cf -> assertTrue(true);
            default -> fail();
        }
    }

    @Test
    void testRemoveGoodsAutomatic() throws Exception {
        testActivateCannonsDefeat();
        smugglers.setCardState(CardState.REMOVE_GOODS);
        ServerEvent e = smugglers.removeGoods();
        switch(e){
            case RemoveGoods rg -> assertTrue(true);
            case RemotionConfirmed rc -> assertTrue(true);
            case RemoveBatteries rb -> assertNotNull(rb.getShip());
            default -> fail();
        }
    }
    @Test
    void testRemoveGoodsDecided() throws Exception {
        smugglers.addDefeatedTruckers(Game.getIstance().getTruckerList().get(smugglers.getIndexOfCurrentTrucker()));
        ((Load) Game.getIstance().getTruckerList().get(smugglers.getIndexOfCurrentTrucker()).getSpaceShip().getComponents()[9][8]).addGood(0, new Goods(3));
        ((Load) Game.getIstance().getTruckerList().get(smugglers.getIndexOfCurrentTrucker()).getSpaceShip().getComponents()[9][8]).addGood(1, new Goods(3));
        ((Load) Game.getIstance().getTruckerList().get(smugglers.getIndexOfCurrentTrucker()).getSpaceShip().getComponents()[8][9]).addGood(0, new Goods(3));
        smugglers.addGoodToRemove(new Goods(3));
        smugglers.addGoodToRemove(new Goods(3));
        ArrayList<Integer> idx = new ArrayList<>();
        idx.add(0);
        idx.add(1);
        smugglers.setCardState(CardState.REMOVE_ASKING);
        ServerEvent e = smugglers.removeGoodsDecided(idx);
        switch (e){
            case RemotionConfirmed rc -> assertTrue(true);
            default -> fail();
        }
    }

    @Test
    void testRemoveBatteries() throws Exception {
        smugglers.addDefeatedTruckers(Game.getIstance().getTrucker(0));
        smugglers.setCardState(CardState.REMOVE_BATTERIES);
        int[] battery = new int[]{5, 6, 2};
        ArrayList<int[]> coords = new ArrayList<>();
        coords.add(battery);
        ServerEvent e = smugglers.removeBatteries(coords);
        switch (e){
            case RemotionConfirmed rc -> assertTrue(true);
            default -> fail();
        }
    }

    @Test
    void testCardEffectExecution() throws Exception {
        testActivateCannonsVictory();
        smugglers.setCardState(CardState.ESECUZIONE);
        ServerEvent e = smugglers.cardEffect();
        assertTrue(e instanceof CardFinished);
    }

    @Test
    void testExceptions() {
        assertThrows(GeneralError.class, () -> smugglers.cardEffect()); // not in ESECUZIONE
    }

    @Test
    void testRemoveWithoutDecision() throws Exception {
        smugglers.setGoodsToLoseCurrTrucker(1);
        smugglers.addDefeatedTruckers(Game.getIstance().getTrucker(0));
        System.out.println("trucker " + Game.getIstance().getTrucker(0).getNickname());
        Goods g = new Goods(3);
        g.setX(9);
        g.setY(8);
        Game.getIstance().getTruckerList().getFirst().getSpaceShip().addGood(g, new int[]{9,8}, 0, Game.getIstance().getBank());
        Game.getIstance().getTruckerList().getFirst().getSpaceShip().addGoodsList(g);
        smugglers.setCardState(CardState.REMOVE_GOODS);
        ServerEvent e= smugglers.removeGoods();
        switch(e){
            case RemotionConfirmed rc -> assertTrue(true);
            default -> fail();
        }
    }

    @Test
    void testAddGoodToRemove() {
        Goods g = new Goods(3);
        smugglers.addGoodToRemove(g);
        assertEquals(g, smugglers.getGoodsToRemove().getFirst());
    }

    @Test
    void rearrenge() {
        smugglers.setCardState(CardState.REARRANGE);
        int[] coord1 = new int[]{9,8};
        int[] coord2 = new int[]{8,9};
        Goods g = new Goods(3);
        g.setX(coord1[0]);
        g.setY(coord1[1]);
        smugglers.setWinnerTrucker(Game.getIstance().getTrucker(0));
        try {
            Game.getIstance().getTrucker(0).getSpaceShip().addGood(g, coord1, 0, Game.getIstance().getBank());
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }
        Game.getIstance().getTruckerList().getFirst().getSpaceShip().addGoodsList(g);
        try {
            smugglers.rearrange(coord1, coord2, 0, 0, false);
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }
        assertEquals(g.getValue(), ((Load) Game.getIstance().getTrucker(0).getSpaceShip().getComponents()[coord2[0]][coord2[1]]).getGood(0).getValue());
        assertEquals(-1, ((Load) Game.getIstance().getTrucker(0).getSpaceShip().getComponents()[coord1[0]][coord1[1]]).getGood(0).getValue());

    }

    @Test
    void testGetters(){
        assertEquals(CardState.ACTIVATE_CANNONS, smugglers.getCardState());
        assertEquals(1 , smugglers.getDays());
    }
}
