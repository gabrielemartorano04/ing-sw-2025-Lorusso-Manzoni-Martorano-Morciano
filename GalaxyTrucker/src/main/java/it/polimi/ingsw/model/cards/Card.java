package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.callers.CardController;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.utilities.exceptions.*;

import javafx.scene.image.Image;

import java.io.Serializable;


/**
 * Class that manages the single adventure card
 */
public abstract class Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String description;
    private final Integer level;
    private final boolean  trialFlight;
    private final String frontImagePath;
    private final String backImagePath;
    private final CardController controller;
    private boolean isCardFinished;

    /**
     * Card constructor, initialise the attributes of this class
     * @param name name of the card
     * @param description short description of what the card do
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param name_frontImage front image of the card
     * @param name_backImage back image of the card
     */
    public Card(String name, String description, boolean trialFlight, Integer level, String name_frontImage, String name_backImage, CardController controller) {
        this.name = name;
        this.description = description;
        this.trialFlight = trialFlight;
        this.level = level;
        this.frontImagePath = name_frontImage;
        this.backImagePath = name_backImage;
        this.controller = controller;
        this.isCardFinished = false;
    }

    public CardController getController() {
        return controller;
    }

    public void setIsCardFinished(boolean isCardFinished) {
        this.isCardFinished = isCardFinished;
    }

    public boolean getIsCardFinished() {
        return isCardFinished;
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
     * Returns the rear image of the Card
     * @return rear image
     */
    public Image getBackImage() {
        return new Image(getClass().getResourceAsStream(backImagePath));
    }

    /**
     * Returns the front image of the Card
     * @return front image
     */
    public Image getFrontImage() {
        return new Image(getClass().getResourceAsStream(frontImagePath));
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    public abstract CardState cardEffect(Game game) throws GeneralError;

    /**
     * method to be called after the card is picked from the deck, it returns the event to be sent to the clients
     */
    public abstract ServerEvent startCard(String currPlayerNickname);
}