package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardState;

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
}
