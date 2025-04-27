package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.AbandonedShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CardControllerTest {

    private CardController cardController;

    private AbandonedShip card;

    @BeforeEach
    void setUp() {
        this.cardController = new CardController();
        this.card = mock(AbandonedShip.class); // Creiamo il mock a mano
    }

    @Test
    void getCardName() {
        // Arrange
        expect(card.getName()).andReturn("Abandoned Ship");
        replay(card);

        // Act
        String cardName = cardController.getCardName(card);

        // Assert
        assertEquals("Abandoned Ship", cardName);
        verify(card); // Verifica che il metodo sia stato chiamato
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