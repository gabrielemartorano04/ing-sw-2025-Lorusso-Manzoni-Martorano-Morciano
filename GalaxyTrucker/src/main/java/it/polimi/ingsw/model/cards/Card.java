package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.callers.CardCaller;
import it.polimi.ingsw.model.visitor.CardVisitor;
import it.polimi.ingsw.model.visitor.ParametricCardVisitor;
import it.polimi.ingsw.utilities.exceptions.*;

import javax.swing.ImageIcon;


/**
 * Class that manages the single adventure card
 */
public abstract class Card {
    private final String name;
    private final String description;
    private final Integer level;
    private final boolean  trialFlight;
    private final ImageIcon frontImage;
    private final ImageIcon backImage;
    private final CardCaller caller;

    /**
     * Card constructor, initialise the attributes of this class
     * @param name name of the card
     * @param description short description of what the card do
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param name_frontImage front image of the card
     * @param name_backImage back image of the card
     */
    public Card(String name, String description, boolean trialFlight, Integer level, String name_frontImage, String name_backImage, CardCaller caller) {
        this.name = name;
        this.description = description;
        this.trialFlight = trialFlight;
        this.level = level;
        this.frontImage = new ImageIcon(getClass().getResource(name_frontImage));
        this.backImage = new ImageIcon(getClass().getResource(name_backImage));
        this.caller = caller;
    }

    public CardCaller getCaller() {
        return caller;
    }

    /**
     * Returns the name of the card
     * @return name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the short description of what the card do
     * @return description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the level of the game the truckers are playing
     * @return level of the game
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Returns if the truckers are playing the trial flight
     * @return specify if truckers are in trial flight
     */
    public boolean isTrialFlight() {
        return trialFlight;
    }

    /**
     * Returns the front image of the card
     * @return front image
     */
    public ImageIcon getFrontImage() {
        return frontImage;
    }

    /**
     * Returns the back image of the card
     * @return back image
     */
    public ImageIcon getBackImage() {
        return backImage;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    public abstract CardState cardEffect(Game game) throws GeneralError;


}