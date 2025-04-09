package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.visitor.CardVisitor;

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

    /**
     * Card constructor, initialise the attributes of this class
     * @param name name of the card
     * @param description short description of what the card do
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param name_frontImage front image of the card
     * @param name_backImage back image of the card
     */
    public Card(String name, String description, boolean trialFlight, Integer level, String name_frontImage, String name_backImage) {
        this.name = name;
        this.description = description;
        this.trialFlight = trialFlight;
        this.level = level;
        this.frontImage = new ImageIcon(getClass().getResource("/Cards.cards/" + name_frontImage + ".jpg"));
        this.backImage = new ImageIcon(getClass().getResource("/Cards.cards/" + name_backImage + ".jpg"));

    }

    void accept(CardVisitor visitor) {

        visitor.visit(this);
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
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    public abstract CardState cardEffect(Game game);

}