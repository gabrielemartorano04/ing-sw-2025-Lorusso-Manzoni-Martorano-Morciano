package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.visitor.*;

import javax.swing.*;

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
}
