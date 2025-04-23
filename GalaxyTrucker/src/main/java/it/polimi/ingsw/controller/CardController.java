package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.visitor.*;

import javax.swing.*;
import java.util.ArrayList;

/**
 *
 */
public class CardController {

    public String getCardName(Card card) {
        return card.getName();
    }

    public String getCardDescription(Card card) {
        return card.getDescription();
    }

    public int getCardLevel(Card card) {
        return card.getLevel();
    }

    public boolean isTrialFlight(Card card) {
        return card.isTrialFlight();
    }

    public ImageIcon getCardFrontImage(Card card) {
        return card.getFrontImage();
    }

    public ImageIcon getCardBackImage(Card card) {
        return card.getBackImage();
    }

    public CardState applyEffects(Card card, Game game) {
        return card.cardEffect(game);
    }

    public Object getDays(Card card) {
        CardVisitor visitor = new GetDaysVisitor();
        return card.accept(visitor);
    }

    public Object getCredits(Card card) {
        CardVisitor visitor = new GetCreditsVisitor();
        return card.accept(visitor);
    }

    public Object getCrewMembersToLose(Card card) {
        CardVisitor visitor = new GetElementsToLoseVisitor();
        return card.accept(visitor);
    }

    public Object getCrewMembersRequired(Card card) {
        CardVisitor visitor = new GetElementsRequired();
        return card.accept(visitor);
    }

    public Object getGoods(Card card) {
        CardVisitor visitor = new GetGoodsVisitor();
        return card.accept(visitor);
    }

    public Object getDirections(Card card) {
        CardVisitor visitor = new GetDirectionVisitor();
        return card.accept(visitor);
    }

    public Object getCurrentMeteor(Card card) {
        CardVisitor visitor = new GetCurrentAttackVisitor();
        return card.accept(visitor);
    }

    public Object getCurrentShoot(Card card) {
        CardVisitor visitor = new GetCurrentAttackVisitor();
        return card.accept(visitor);
    }

    public Object getSlaversPower(Card card) {
        CardVisitor visitor = new GetEnemyPowerVisitor();
        return card.accept(visitor);
    }

    public Object getSmugglersPower(Card card) {
        CardVisitor visitor = new GetEnemyPowerVisitor();
        return card.accept(visitor);
    }

    public Object getPiratesPower(Card card) {
        CardVisitor visitor = new GetEnemyPowerVisitor();
        return card.accept(visitor);
    }

    public Object getShoots(Card card) {
        CardVisitor visitor = new GetShootsVisitor();
        return card.accept(visitor);
    }

    public Object getPlanets(Card card) {
        CardVisitor visitor = new GetPlanetsVisitor();
        return card.accept(visitor);
    }

    public Object getGoodsFromPlanet(Card card) {
        CardVisitor visitor = new GetGoodsFromPlanetsVisitor();
        return card.accept(visitor);
    }

    public Object getGoodsFromList(Card card) {
        CardVisitor visitor = new GetGoodsVisitor();
        return card.accept(visitor);
    }

    public Object getGoodsToLose(Card card) {
        CardVisitor visitor = new GetElementsToLoseVisitor();
        return card.accept(visitor);
    }

    public Object getGoodsToGain(Card card) {
        CardVisitor visitor = new GetElementsToGainVisitor();
        return card.accept(visitor);
    }

    public Object getCurrentGood(Card card) {
        CardVisitor visitor = new GetCurrentGoodVisitor();
        return card.accept(visitor);
    }

    public Object getBatteriesToRemove(Card card) {
        CardVisitor visitor = new GetElementsToRemove();
        return card.accept(visitor);
    }

    public Object getConditionsOrder(Card card) {
        CardVisitor visitor = new GetConditionsOrderVisitor();
        return card.accept(visitor);
    }

    public Object getPenalties(Card card) {
        CardVisitor visitor = new GetPenaltiesVisitor();
        return card.accept(visitor);
    }

    public Object setTruckerThatHasDecided(Card card, Trucker trucker, boolean landing, int numPlayer) {
        ParametricCardVisitor visitor = new SetTruckerLandingVisitor();
        return card.accept(visitor, trucker, landing, numPlayer);
    }

    public Object setTruckerLanding(Card card, Trucker trucker, boolean landing, int numPlayer, Bank bank) {
        ParametricCardVisitor visitor = new SetTruckerLandingVisitor();
        return card.accept(visitor, trucker, landing, numPlayer, bank);
    }

    public Object setCoordinatesToRemoveCrew(Card card, ArrayList<int[]> coordinates) {
        ParametricCardVisitor visitor = new SetCoordinatesToRemoveElementsVisitor();
        return card.accept(visitor, coordinates);
    }

    public Object setCoordinatesToAddGoods(Card card, int[] coordinatesToAddGood, int value, Trucker trucker, Bank bank) {
        ParametricCardVisitor visitor = new SetCoordinatesToAddElementsVisitor();
        return card.accept(visitor, coordinatesToAddGood, value, trucker, bank);
    }

    public Object setChoiceToGain(Card card, boolean choice) {
        ParametricCardVisitor visitor = new SetChoiceToGainVisitor();
        return card.accept(visitor, choice);
    }

    public Object rearrange(Card card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) {
        ParametricCardVisitor visitor = new RearrangeVisitor();
        return card.accept(visitor, coord1, coord2, value1, value2, contiune);
    }

    public Object rollDice(Card card, Game game) {
        ParametricCardVisitor visitor = new RollDiceVisitor();
        return card.accept(visitor, game);
    }

    public Object setAnswear(Card card, boolean answear) {
        ParametricCardVisitor visitor = new SetAnswersVisitor();
        return card.accept(visitor, answear);
    }

    public Object setDoubleCannonsCoordinates(Card card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers) {
        ParametricCardVisitor visitor = new SetDoubleCannonsCoordinatesVisitor();
        return card.accept(visitor, truckerCoordinate, numTruckers);
    }
}
