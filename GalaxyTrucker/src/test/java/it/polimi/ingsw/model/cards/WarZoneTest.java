
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.CreateShipTest;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.view.ParserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WarZoneTest {

    private WarZone warZone;
    private CreateShipTest createShip = new CreateShipTest();

    @BeforeEach
    void setUp() throws InvalidCoordinates {
        SpaceShip ship1 = createShip.createShip1(new SpaceShip());
        ship1.setBattery();
        ship1.setNumHuman();

        SpaceShip ship2 = createShip.createShip2(new SpaceShip());
        ship2.setBattery();
        ship2.setNumHuman();

        Game.initializeForTest(ship1, ship2);

        ArrayList<int[]> warShoots = new ArrayList<>();
        warShoots.add(new int[]{0, 2});
        warShoots.add(new int[]{1, 2});

        warZone = new WarZone(
                true, 1,
                new ArrayList<>(List.of(Condition.HUMANS, Condition.ENGINES, Condition.CANNONS)),
                Map.of(
                        Condition.HUMANS, new Effect(TypeOfEffect.LOSE_DAYS, 3),
                        Condition.ENGINES, new Effect(TypeOfEffect.LOSE_HUMANS, 2),
                        Condition.CANNONS, new Effect(TypeOfEffect.GET_SHOT, 2)
                ),
                warShoots,
                "/img/front.jpg",
                "/img/back.jpg",
                0
        );
    }

    @Test
    void testStartCard() {
        // Act
        ServerEvent event = warZone.startCard();

        // Assert: è del tipo corretto
        assertTrue(event instanceof StartCardEvent);

        StartCardEvent startEvent = (StartCardEvent) event;

        // Controllo contenuti principali
        assertEquals("War Zone", startEvent.getCardName());
        assertEquals("A dangerous conflict zone", startEvent.getCardDescription());
        assertEquals(ParserState.WARZONE, startEvent.getParserState());

        // Controllo condizioni e penalità
        assertEquals(List.of(Condition.HUMANS, Condition.ENGINES, Condition.CANNONS), startEvent.getConditionsOrder());
        Map<Condition, Effect> expected = startEvent.getPenalties();

        assertEquals(3, expected.size());
        Effect expected1 = new Effect(TypeOfEffect.LOSE_DAYS, 3);
        Effect actual1 = warZone.getPenalties().get(Condition.HUMANS);
        assertEquals(expected1.getType(), actual1.getType());
        Effect expected2 = new Effect(TypeOfEffect.LOSE_HUMANS, 2);
        Effect actual2 = warZone.getPenalties().get(Condition.ENGINES);
        assertEquals(expected2.getType(), actual2.getType());
        Effect expected3 = new Effect(TypeOfEffect.GET_SHOT, 2);
        Effect actual3 = warZone.getPenalties().get(Condition.CANNONS);
        assertEquals(expected3.getType(), actual3.getType());

        // Traccia e giocatore iniziale
        assertEquals(Game.getIstance().getTruckerList().getFirst().getNickname(), startEvent.getFirstPlayerNickname());
        assertEquals(Game.getIstance().getTrack(), startEvent.getTrack());

        // Riferimento alla carta
        assertSame(warZone, startEvent.getCard());
    }

    @Test
    void testGetCardState(){
        assertEquals(CardState.WARZONE, warZone.getCardState());
    }

    @Test
    void testHandleCurrentCondition_OnlyOneTruckerEndsGame() throws GeneralError {
        try {
            Game.initializeForTest(createShip.createShip1(new SpaceShip()), null);
            Game.getIstance().getTrucker(0).getSpaceShip().setNumHuman();
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }

        warZone.setState(CardState.WARZONE);
        ServerEvent result = warZone.handleCurrentCondition();

        switch(result){
            case CardFinished cf-> {
                assertEquals(1, cf.getTruckers().size());
            }
            default -> fail();
        }

    }

    @Test
    void testHandleCurrentCondition_FirstConditionHumans() throws GeneralError {
        warZone.setCurrConditionIndex(0);

        ServerEvent result = warZone.handleCurrentCondition();

        switch (result){
            case CurrentCondition cc-> assertEquals(Condition.ENGINES, cc.getCondition());
            default -> fail();
        }
    }

    @Test
    void testHandleCurrentCondition_CannonCondition() throws GeneralError {
        ArrayList<int[]> warShoots = new ArrayList<>();
        warShoots.add(new int[]{0, 2});
        warShoots.add(new int[]{1, 2});

        warZone = new WarZone(
                true, 1,
                new ArrayList<>(List.of(Condition.CANNONS, Condition.HUMANS, Condition.ENGINES)),
                Map.of(
                        Condition.CANNONS, new Effect(TypeOfEffect.GET_SHOT, 2),
                        Condition.HUMANS, new Effect(TypeOfEffect.LOSE_DAYS, 3),
                        Condition.ENGINES, new Effect(TypeOfEffect.LOSE_HUMANS, 2)
                ),
                warShoots,
                "/img/front.jpg",
                "/img/back.jpg",
                0
        );

        warZone.setCurrConditionIndex(0);

        ServerEvent result = warZone.handleCurrentCondition();

        assertEquals(CardState.ACTIVATE_CANNONS, warZone.getCardState());
        switch (result){
            case BooleanDecision bl -> assertEquals(ParserState.ACTIVATE_CANNONS, bl.getParserState());
            default -> fail();
        }
    }

    @Test
    void testHandleCurrentCondition_EngineCondition() throws GeneralError {
        ArrayList<int[]> warShoots = new ArrayList<>();
        warShoots.add(new int[]{0, 2});
        warShoots.add(new int[]{1, 2});

        warZone = new WarZone(
                true, 1,
                new ArrayList<>(List.of(Condition.ENGINES, Condition.HUMANS, Condition.CANNONS)),
                Map.of(
                        Condition.ENGINES, new Effect(TypeOfEffect.LOSE_HUMANS, 2),
                        Condition.CANNONS, new Effect(TypeOfEffect.GET_SHOT, 2),
                        Condition.HUMANS, new Effect(TypeOfEffect.LOSE_DAYS, 3)
                ),
                warShoots,
                "/img/front.jpg",
                "/img/back.jpg",
                0
        );

        warZone.setCurrConditionIndex(0);

        ServerEvent result = warZone.handleCurrentCondition();

        assertEquals(CardState.ACTIVATE_ENGINES, warZone.getCardState());
        switch (result) {
            case BooleanDecision bl -> assertEquals(ParserState.ACTIVATE_ENGINES, bl.getParserState());
            default -> fail();
        }
    }

    @Test
    void testActivateCannons_WrongState_ThrowsError() {
        warZone.setState(CardState.WARZONE);  // stato errato

        assertThrows(GeneralError.class, () ->
                warZone.activateCannons(true, new ArrayList<>(), new ArrayList<>())
        );
    }

    @Test
    void testActivateCannons_InvalidBatteryCoords_ThrowsException() {
        ArrayList<int[]> cannons = new ArrayList<>(List.of(new int[]{3, 3}));
        ArrayList<int[]> invalidBatteryCoords = new ArrayList<>(List.of(new int[]{1, 1})); // non è batteria
        warZone.setState(CardState.ACTIVATE_CANNONS);
        assertThrows(InvalidComponentsException.class, () ->
                warZone.activateCannons(true, cannons, invalidBatteryCoords)
        );
    }

    @Test
    void testActivateCannons_InvalidCannonCoords_ThrowsException() {
        ArrayList<int[]> invalidCannons = new ArrayList<>(List.of(new int[]{1, 1})); // non cannone
        ArrayList<int[]> battery = new ArrayList<>(List.of(new int[]{4, 4}));

        warZone.setState(CardState.ACTIVATE_CANNONS);
        assertThrows(InvalidComponentsException.class, () ->
                warZone.activateCannons(true, invalidCannons, battery)
        );
    }

    @Test
    void testActivateCannons_ValidActivation_ReturnsNextPlayer() throws Exception {
        ArrayList<int[]> cannons = new ArrayList<>(List.of(new int[]{5, 8}));
        ArrayList<int[]> battery = new ArrayList<>(List.of(new int[]{5, 6}));

        warZone.setState(CardState.ACTIVATE_CANNONS);
        ServerEvent result = warZone.activateCannons(true, cannons, battery);

        switch (result){
            case BooleanDecision bd -> {
                assertEquals(ParserState.ACTIVATE_CANNONS, bd.getParserState());
                assertEquals(Game.getIstance().getTrucker(1).getNickname(), bd.getNickname());
            }
            default -> fail();
        }
    }

    @Test
    void testActivateCannons_LastPlayer_FindWeakest() throws Exception {
        warZone.setIndexOfCurrTrucker(1);  // ultimo trucker

        ArrayList<int[]> cannons = new ArrayList<>(List.of(new int[]{5, 8}));
        ArrayList<int[]> battery = new ArrayList<>(List.of(new int[]{6, 6}));

        warZone.setState(CardState.ACTIVATE_CANNONS);
        warZone.setCurrConditionIndex(2);
        warZone.setCurrCondition();
        ServerEvent result = warZone.activateCannons(true, cannons, battery);

        assertEquals(CardState.ROLL_DICE, warZone.getCardState()); //da handleCurrEffect
        switch(result){
            case BooleanDecision bd-> {
                assertEquals(warZone.getWeakestTrucker().getNickname(), bd.getNickname());
            }
            default -> fail();
        }
    }

    @Test
    void testActivateCannons_noDoubleCannons() throws Exception {
        try {
            Game.initializeForTest(createShip.createShip3(new SpaceShip()), null);
            Game.getIstance().getTrucker(0).getSpaceShip().setNumHuman();
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        warZone.setState(CardState.ACTIVATE_CANNONS);
        warZone.setCurrConditionIndex(2);
        warZone.setCurrCondition();
        ArrayList<int[]> cannons = new ArrayList<>(List.of(new int[]{5, 8}));
        ArrayList<int[]> battery = new ArrayList<>(List.of(new int[]{7, 6}));
        assertThrows(InvalidComponentsException.class, () -> warZone.activateCannons(true, cannons, battery));

    }

    @Test
    void testActivateEngines_WrongState_ThrowsError() {
        warZone.setState(CardState.ACTIVATE_CANNONS); // stato sbagliato

        assertThrows(GeneralError.class, () ->
                warZone.activateEngines(true, new ArrayList<>(), new ArrayList<>())
        );
    }

    @Test
    void testActivateEngines_InvalidBatteryCoords_Throws() {
        warZone.setState(CardState.ACTIVATE_ENGINES);

        ArrayList<int[]> engines = new ArrayList<>(List.of(new int[]{5, 8}));
        ArrayList<int[]> badBatteryCoords = new ArrayList<>(List.of(new int[]{1, 1})); // non batteria

        assertThrows(InvalidCoordinates.class, () ->
                warZone.activateEngines(true, engines, badBatteryCoords)
        );
    }

    @Test
    void testActivateEngines_InvalidEngineCoords_Throws() {
        warZone.setState(CardState.ACTIVATE_ENGINES);

        ArrayList<int[]> fakeEngines = new ArrayList<>(List.of(new int[]{1, 1}));
        ArrayList<int[]> battery = new ArrayList<>(List.of(new int[]{5, 6})); // corretta

        assertThrows(InvalidCoordinates.class, () ->
                warZone.activateEngines(true, fakeEngines, battery)
        );
    }

    @Test
    void testActivateEngines_NoDoubleEngines_Throws() throws Exception {
        warZone.setState(CardState.ACTIVATE_ENGINES);
        try {
            Game.initializeForTest(createShip.createShip3(new SpaceShip()), null);
            Game.getIstance().getTrucker(0).getSpaceShip().setNumHuman();
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        ArrayList<int[]> engines = new ArrayList<>(List.of(new int[]{5, 8}));
        ArrayList<int[]> battery = new ArrayList<>(List.of(new int[]{7, 6}));

        assertThrows(InvalidCoordinates.class, () ->
                warZone.activateEngines(true, engines, battery)
        );
    }

    @Test
    void testActivateEngines_ValidActivation_ReturnsNext() throws Exception {
        warZone.setState(CardState.ACTIVATE_ENGINES);

        ArrayList<int[]> engines = new ArrayList<>(List.of(new int[]{8, 7}));
        ArrayList<int[]> battery = new ArrayList<>(List.of(new int[]{5, 6}));

        ServerEvent result = warZone.activateEngines(true, engines, battery);

        switch (result){
            case BooleanDecision bd -> {
                assertEquals(ParserState.ACTIVATE_ENGINES, bd.getParserState());
                assertEquals(Game.getIstance().getTrucker(1).getNickname(), bd.getNickname());
            }
            default -> fail();
        }
    }

    @Test
    void testActivateEngines_LastPlayer_ReturnsFindWeakest() throws Exception {
        warZone.setState(CardState.ACTIVATE_ENGINES);
        warZone.setIndexOfCurrTrucker(1);  // ultimo

        ArrayList<int[]> engines = new ArrayList<>(List.of(new int[]{9, 9}));
        ArrayList<int[]> battery = new ArrayList<>(List.of(new int[]{6, 6}));

        warZone.setCurrConditionIndex(1);
        warZone.setCurrCondition();
        ServerEvent result = warZone.activateEngines(true, engines, battery);

        assertEquals(CardState.HUMANSTOREMOVE, warZone.getCardState()); //da handleCurrEffect
        switch(result){
            case HumanCoordinates hc-> {
                assertEquals(warZone.getWeakestTrucker().getNickname(), hc.getNickname());
            }
            default -> fail();
        }
    }

    @Test
    void testRemoveGoods_WrongState_Throws() {
        warZone.setState(CardState.ACTIVATE_CANNONS);

        assertThrows(GeneralError.class, () -> warZone.removeGoods());
    }

    @Test
    void testRemoveGoods_NullWeakest_Throws() {
        warZone.setState(CardState.REMOVE_GOODS);
        warZone.setWeakestTrucker(null); // forza null

        assertThrows(GeneralError.class, () -> warZone.removeGoods());
    }

    @Test
    void testRemoveGoods_CurrPenaltyNotLoseGoods_Throws() {

        warZone = new WarZone(
                true, 1,
                new ArrayList<>(List.of(Condition.HUMANS, Condition.ENGINES)),
                Map.of(
                        Condition.HUMANS, new Effect(TypeOfEffect.LOSE_HUMANS, 2),
                        Condition.ENGINES, new Effect(TypeOfEffect.LOSE_HUMANS, 2)
                ),
                null,
                "/img/front.jpg",
                "/img/back.jpg",
                0
        );

        warZone.setState(CardState.HUMANSTOREMOVE);
        warZone.setWeakestTrucker(Game.getIstance().getTrucker(0));

        warZone.setCurrConditionIndex(0);
        warZone.setCurrCondition();

        assertThrows(GeneralError.class, () -> warZone.removeGoods());
    }

    @Test
    void testRemoveGoods_AutoRemoveWithoutDecision() throws Exception {
        warZone = new WarZone(
                true, 1,
                new ArrayList<>(List.of(Condition.HUMANS, Condition.ENGINES)),
                Map.of(
                        Condition.HUMANS, new Effect(TypeOfEffect.LOSE_GOODS, 2),
                        Condition.ENGINES, new Effect(TypeOfEffect.LOSE_HUMANS, 2)
                ),
                null,
                "/img/front.jpg",
                "/img/back.jpg",
                0
        );
        warZone.setState(CardState.REMOVE_GOODS);
        warZone.setWeakestTrucker(Game.getIstance().getTrucker(0));

        Goods g = new Goods(3);
        Goods g1 = new Goods(3);
        g.setX(8);
        g.setY(9);
        g1.setX(8);
        g1.setY(9);
        // Setup nave con 2 beni blu (color 3)
        Game.getIstance().getTrucker(0).getSpaceShip().addGood(g, new int[]{8,9}, 0, Game.getIstance().getBank());
        Game.getIstance().getTrucker(0).getSpaceShip().addGood(g1, new int[]{8,9}, 1, Game.getIstance().getBank());
        Game.getIstance().getTrucker(0).getSpaceShip().addGoodsList(g);
        Game.getIstance().getTrucker(0).getSpaceShip().addGoodsList(g1);
        warZone.setColorOfCurrGoodToRemove(3);
        warZone.setGoodsToLose(2);
        warZone.setInitializeRemoveGoods(true);

        warZone.setCurrConditionIndex(0);
        warZone.setCurrCondition();
        ServerEvent result = warZone.removeGoods();

        switch(result){
            case CurrentCondition cc -> assertEquals(warZone.getWeakestTrucker().getNickname(), cc.getNickname());
            default -> fail();
        }
    }

    @Test
    void testRemoveGoods_ManualChoiceRequested() throws Exception {

        warZone = new WarZone(
                true, 1,
                new ArrayList<>(List.of(Condition.HUMANS, Condition.ENGINES)),
                Map.of(
                        Condition.HUMANS, new Effect(TypeOfEffect.LOSE_GOODS, 2),
                        Condition.ENGINES, new Effect(TypeOfEffect.LOSE_HUMANS, 2)
                ),
                null,
                "/img/front.jpg",
                "/img/back.jpg",
                0
        );
        warZone.setState(CardState.REMOVE_GOODS);
        warZone.setWeakestTrucker(Game.getIstance().getTrucker(0));

        Goods g = new Goods(3);
        Goods g1 = new Goods(3);
        Goods g2 = new Goods(3);
        g.setX(8);
        g.setY(9);
        g1.setX(8);
        g1.setY(9);
        g2.setX(8);
        g2.setY(9);
        // Setup nave con 2 beni blu (color 3)
        Game.getIstance().getTrucker(0).getSpaceShip().addGood(g, new int[]{8,9}, 0, Game.getIstance().getBank());
        Game.getIstance().getTrucker(0).getSpaceShip().addGood(g1, new int[]{8,9}, 1, Game.getIstance().getBank());
        Game.getIstance().getTrucker(0).getSpaceShip().addGood(g1, new int[]{8,9}, 2, Game.getIstance().getBank());
        Game.getIstance().getTrucker(0).getSpaceShip().addGoodsList(g);
        Game.getIstance().getTrucker(0).getSpaceShip().addGoodsList(g1);
        Game.getIstance().getTrucker(0).getSpaceShip().addGoodsList(g2);


        warZone.setGoodsToLose(2);
        warZone.setInitializeRemoveGoods(true);

        warZone.setCurrConditionIndex(0);
        warZone.setCurrCondition();

        ServerEvent result = warZone.removeGoods();

        assertEquals(CardState.REMOVE_ASKING, warZone.getCardState());
        switch (result){
            case RemoveGoods rg -> assertEquals(warZone.getWeakestTrucker().getNickname(), rg.getNickname());
            default -> fail();
        }
    }

    @Test
    void testRemoveGoods_NoGoods_PassesToRemoveBatteries() throws Exception {
        warZone = new WarZone(
                true, 1,
                new ArrayList<>(List.of(Condition.HUMANS, Condition.ENGINES)),
                Map.of(
                        Condition.HUMANS, new Effect(TypeOfEffect.LOSE_GOODS, 2),
                        Condition.ENGINES, new Effect(TypeOfEffect.LOSE_HUMANS, 2)
                ),
                null,
                "/img/front.jpg",
                "/img/back.jpg",
                0
        );
        warZone.setState(CardState.REMOVE_GOODS);
        warZone.setWeakestTrucker(Game.getIstance().getTrucker(0));
        Game.getIstance().getTrucker(0).getSpaceShip().setBattery(); // Nessun bene, ma batterie presenti

        warZone.setGoodsToLose(2);

        warZone.setCurrConditionIndex(0);
        warZone.setCurrCondition();

        ServerEvent result = warZone.removeGoods();

        assertEquals(CardState.REMOVE_BATTERIES, warZone.getCardState());
        switch (result){
            case RemoveBatteries rb -> assertEquals(warZone.getWeakestTrucker().getNickname(), rb.getNickname());
            default -> fail();
        }
    }

    @Test
    void testRemoveGoods_NoGoodsNoBatteries_ProceedsToEsecuzione() throws Exception {
        try {
            Game.initializeForTest(createShip.createShip1(new SpaceShip()), null);
            Game.getIstance().getTrucker(0).getSpaceShip().setNumHuman();
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        warZone = new WarZone(
                true, 1,
                new ArrayList<>(List.of(Condition.HUMANS)),
                Map.of(
                        Condition.HUMANS, new Effect(TypeOfEffect.LOSE_GOODS, 2)
                ),
                null,
                "/img/front.jpg",
                "/img/back.jpg",
                0
        );
        warZone.setState(CardState.REMOVE_GOODS);
        warZone.setWeakestTrucker(Game.getIstance().getTrucker(0));
        warZone.setGoodsToLose(2);
        warZone.setCurrConditionIndex(0);
        warZone.setCurrCondition();

        ServerEvent result = warZone.removeGoods();

        assertEquals(CardState.ESECUZIONE, warZone.getCardState());
        switch (result){
            case CardFinished cf -> assertEquals(Game.getIstance().getTruckerList(), cf.getTruckers());
            default -> fail();
        }

    }

    @Test
    void testRemoveGoodsDecided_WrongState_Throws() {
        warZone.setState(CardState.REMOVE_GOODS); // stato sbagliato

        assertThrows(GeneralError.class, () ->
                warZone.removeGoodsDecided(new ArrayList<>(List.of(0, 1)))
        );
    }

    @Test
    void testRemoveGoodsDecided_WrongNumberOfGoods_Throws() {
        warZone.setState(CardState.REMOVE_ASKING);
        warZone.setGoodsToLose(2);
        warZone.addGoodsToRemove(new Goods(2));

        assertThrows(NotEnoughGoodsChosen.class, () ->
                warZone.removeGoodsDecided(new ArrayList<>(List.of(0)) // solo uno invece di due
        ));
    }

    @Test
    void testRemoveGoodsDecided_CorrectRemoval_ProceedsToEsecuzione() throws Exception {
        warZone = new WarZone(
                true, 1,
                new ArrayList<>(List.of(Condition.HUMANS)),
                Map.of(
                        Condition.HUMANS, new Effect(TypeOfEffect.LOSE_GOODS, 2)
                ),
                null,
                "/img/front.jpg",
                "/img/back.jpg",
                0
        );
        warZone.setState(CardState.REMOVE_ASKING);
        warZone.setGoodsToLose(2);

        warZone.setWeakestTrucker(Game.getIstance().getTrucker(0));

        Goods rg = new Goods(2);
        Goods rg1 = new Goods(3);
        rg.setX(8);
        rg.setY(9);
        rg1.setX(8);
        rg1.setY(9);
        warZone.addGoodsToRemove(rg);
        warZone.addGoodsToRemove(rg1);


        Goods g = new Goods(2);
        Goods g1 = new Goods(3);
        g.setX(8);
        g.setY(9);
        g1.setX(8);
        g1.setY(9);
        Game.getIstance().getTrucker(0).getSpaceShip().addGood(g, new int[]{8,9}, 0, Game.getIstance().getBank());
        Game.getIstance().getTrucker(0).getSpaceShip().addGood(g1, new int[]{8,9}, 1, Game.getIstance().getBank());


        warZone.setCurrConditionIndex(0);
        warZone.setCurrCondition();
        ServerEvent result = warZone.removeGoodsDecided(new ArrayList<>(List.of(0, 1)));

        assertEquals(CardState.ESECUZIONE, warZone.getCardState());
        switch(result){
            case CardFinished cf -> assertTrue(true);
            default -> fail();
        }

        assertEquals(0, Game.getIstance().getTrucker(0).getSpaceShip().getGoodsList().size());
    }


}
