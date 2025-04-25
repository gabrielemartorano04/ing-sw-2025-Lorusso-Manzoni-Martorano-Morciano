package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardControllerTest {

    private CardController cardController;

    @BeforeEach
    void setUp() {
        this.cardController = new CardController();
    }

    @Test
    void getCardName() {
        AbandonedShip card=new AbandonedShip(false, 2,2,2,2,"","");
        assertEquals(cardController.getCardName(card), "Abandoned Ship");
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

    @org.junit.jupiter.api.Test
    void getDirections() {
    }

    @org.junit.jupiter.api.Test
    void getCurrentMeteor() {
    }

    @org.junit.jupiter.api.Test
    void getCurrentShoot() {
    }

    @org.junit.jupiter.api.Test
    void getSlaversPower() {
    }

    @org.junit.jupiter.api.Test
    void getSmugglersPower() {
    }

    @org.junit.jupiter.api.Test
    void getPiratesPower() {
    }

    @org.junit.jupiter.api.Test
    void getShoots() {
    }

    @org.junit.jupiter.api.Test
    void getPlanets() {
    }

    @org.junit.jupiter.api.Test
    void getGoodsFromPlanet() {
    }

    @org.junit.jupiter.api.Test
    void getGoodsFromList() {
    }

    @org.junit.jupiter.api.Test
    void getGoodsToLose() {
    }

    @org.junit.jupiter.api.Test
    void getGoodsToGain() {
    }

    @org.junit.jupiter.api.Test
    void getCurrentGood() {
    }

    @org.junit.jupiter.api.Test
    void getBatteriesToRemove() {
    }

    @org.junit.jupiter.api.Test
    void getConditionsOrder() {
    }

    @org.junit.jupiter.api.Test
    void getPenalties() {
    }

    @org.junit.jupiter.api.Test
    void setTruckerLandingOnAbandonedShip() {
    }

    @org.junit.jupiter.api.Test
    void setTruckerLandingOnAbandonedStation() {
    }

    @org.junit.jupiter.api.Test
    void setTruckerLandingOnPlanets() {
    }

    @org.junit.jupiter.api.Test
    void setCoordinatesToRemoveCrew() {
    }

    @org.junit.jupiter.api.Test
    void setCoordinatesToRemoveCrewForSlavers() {
    }

    @org.junit.jupiter.api.Test
    void setCoordinatesToRemoveBatteries() {
    }

    @org.junit.jupiter.api.Test
    void setCoordinatesToAddGoods() {
    }

    @org.junit.jupiter.api.Test
    void setCoordinatesToAddGoodsFromPlanet() {
    }

    @org.junit.jupiter.api.Test
    void setCoordinatesToAddGoodsFromList() {
    }

    @org.junit.jupiter.api.Test
    void setChoiceToGain() {
    }

    @org.junit.jupiter.api.Test
    void setChoiceToGainFromList() {
    }

    @org.junit.jupiter.api.Test
    void rearrange() {
    }

    @org.junit.jupiter.api.Test
    void rearrangeForPlanet() {
    }

    @org.junit.jupiter.api.Test
    void rollDice() {
    }

    @org.junit.jupiter.api.Test
    void setAnswear() {
    }

    @org.junit.jupiter.api.Test
    void setTruckersAnswear() {
    }

    @org.junit.jupiter.api.Test
    void setDoubleEnginesCoordinates() {
    }

    @org.junit.jupiter.api.Test
    void setDoubleEnginesCoordinatesforWarZone() {
    }

    @org.junit.jupiter.api.Test
    void setDoubleCannonsCoordinates() {
    }

    @org.junit.jupiter.api.Test
    void setDoubleCannonsCoordinatesForSmugglers() {
    }

    @org.junit.jupiter.api.Test
    void setDoubleCannonsCoordinatesForWarZone() {
    }

    @org.junit.jupiter.api.Test
    void setDefeatedTruckers() {
    }

    @org.junit.jupiter.api.Test
    void setWinnerTrucker() {
    }

    @org.junit.jupiter.api.Test
    void getBank() {
    }

    @org.junit.jupiter.api.Test
    void initialize() {
    }

    @org.junit.jupiter.api.Test
    void manageRemoveGoods() {
    }

    @org.junit.jupiter.api.Test
    void plotGoods() {
    }

    @org.junit.jupiter.api.Test
    void removeAsking() {
    }

    @org.junit.jupiter.api.Test
    void combactZone() {
    }

    @org.junit.jupiter.api.Test
    void findWeakestTrucker() {
    }
}