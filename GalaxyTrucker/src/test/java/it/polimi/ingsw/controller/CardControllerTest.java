package it.polimi.ingsw.controller;

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
        assertEquals("AbandonedShip",cardName);

    }

    @Test
    void getCardDescription() {
    }

    @Test
    void getCardLevel() {
    }

    @Test
    void isTrialFlight() {
    }

    @Test
    void getCardFrontImage() {
    }

    @Test
    void getCardBackImage() {
    }

    @Test
    void applyEffects() {
    }

    @Test
    void getDays() {
    }

    @Test
    void getCredits() {
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