package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.AbandonedShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardControllerTest {

    private CardController cardController;
    private AbandonedShip card;
    @BeforeEach
    void setUp() {
        cardController = new CardController();
        card=new AbandonedShip(false,1,1,1,1,"GT-cards_I_IT_0117.jpg","GT-cards_I_IT_0121.jpg");
    }

    @Test
    void getCardName() {
        String cardName=cardController.getCardName(card);
        assertEquals("Abandoned Ship",cardName);

    }

    @Test
    void getCardDescription() {
        String cardDescription=cardController.getCardDescription(card);
        assertEquals("A drifting ghost ship",cardDescription);
    }

    @Test
    void getCardLevel() {
        int cardLevel=cardController.getCardLevel(card);
        assertEquals(1,cardLevel);
    }

    @Test
    void isTrialFlight() {
        assertFalse(cardController.isTrialFlight(card));
    }

    @Test
    void getCardFrontImage() {
        assertNotNull(cardController.getCardFrontImage(card));
    }

    @Test
    void getCardBackImage() {
        assertNotNull(cardController.getCardBackImage(card));
    }

    @Test
    void applyEffects() {
    }

    @Test
    void getDays() {
        assertEquals(1,cardController.getDays(card));
    }

    @Test
    void getCredits() {
        assertEquals(1,cardController.getCredits(card));
    }

    @Test
    void getCrewMembersToLose() {
    }

    @Test
    void getCrewMembersRequired() {
    }

    @Test
    void getGoods() {
    }

    @Test
    void getDirections() {
    }

    @Test
    void getCurrentMeteor() {
    }

    @Test
    void getCurrentShoot() {
    }

    @Test
    void getSlaversPower() {
    }

    @Test
    void getSmugglersPower() {
    }

    @Test
    void getPiratesPower() {
    }

    @Test
    void getShoots() {
    }

    @Test
    void getPlanets() {
    }

    @Test
    void getGoodsFromPlanet() {
    }

    @Test
    void getGoodsFromList() {
    }

    @Test
    void getGoodsToLose() {
    }

    @Test
    void getGoodsToGain() {
    }

    @Test
    void getCurrentGood() {
    }

    @Test
    void getBatteriesToRemove() {
    }

    @Test
    void getConditionsOrder() {
    }

    @Test
    void getPenalties() {
    }

    @Test
    void setTruckerLandingOnAbandonedShip() {
    }

    @Test
    void setTruckerLandingOnAbandonedStation() {
    }

    @Test
    void setTruckerLandingOnPlanets() {
    }

    @Test
    void setCoordinatesToRemoveCrew() {
    }

    @Test
    void setCoordinatesToRemoveCrewForSlavers() {
    }

    @Test
    void setCoordinatesToRemoveBatteries() {
    }

    @Test
    void setCoordinatesToAddGoods() {
    }

    @Test
    void setCoordinatesToAddGoodsFromPlanet() {
    }

    @Test
    void setCoordinatesToAddGoodsFromList() {
    }

    @Test
    void setChoiceToGain() {
    }

    @Test
    void setChoiceToGainFromList() {
    }

    @Test
    void rearrange() {
    }

    @Test
    void rearrangeForPlanet() {
    }

    @Test
    void rollDice() {
    }

    @Test
    void setAnswear() {
    }

    @Test
    void setTruckersAnswear() {
    }

    @Test
    void setDoubleEnginesCoordinates() {
    }

    @Test
    void setDoubleEnginesCoordinatesforWarZone() {
    }

    @Test
    void setDoubleCannonsCoordinates() {
    }

    @Test
    void setDoubleCannonsCoordinatesForSmugglers() {
    }

    @Test
    void setDoubleCannonsCoordinatesForWarZone() {
    }

    @Test
    void setDefeatedTruckers() {
    }

    @Test
    void setWinnerTrucker() {
    }

    @Test
    void getBank() {
    }

    @Test
    void initialize() {
    }

    @Test
    void manageRemoveGoods() {
    }

    @Test
    void plotGoods() {
    }

    @Test
    void removeAsking() {
    }

    @Test
    void combactZone() {
    }

    @Test
    void findWeakestTrucker() {
    }
}