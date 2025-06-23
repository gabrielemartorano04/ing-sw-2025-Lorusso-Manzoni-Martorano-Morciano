package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.net.events.serverevents.GenericErrorEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.StarDustEvent;
import it.polimi.ingsw.exceptions.GeneralError;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Class that manages the single card star dust
 * In reverse order of position, the truckers lose as many days of flight as the number of connectors exposed
 */
public class StarDust extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * current state of the card, helper for debug and checking consistency
     */
    private final CardState cardState;

    /**
     * Star dust constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public StarDust(boolean trialFlight, int level, String frontImage, String backImage, int id) {
        super("Star Dust", "A cloud of cosmic dust", trialFlight, level, frontImage, backImage, id);
        this.cardState = CardState.ESECUZIONE;
    }

    public CardState getCardState() {
        return cardState;
    }

    /**
     * start the card and applies its effect since there is no need of interaction with clients
     * @return cardEffect
     */
    @Override
    public ServerEvent startCard() {
        try {
            return cardEffect();
        } catch (GeneralError e) {
            return new GenericErrorEvent("errore", true);
        }
    }

    /**
     * Applies the changes provided by the card
     *
     * @return serverEvent to send to clients
     */
    @Override
    public ServerEvent cardEffect() throws GeneralError {
        if(cardState != CardState.ESECUZIONE) {
            throw new GeneralError("Not callable in the state " + cardState);
        }
        List<Trucker> truckers = Game.getIstance().getTruckerList();
        Collections.reverse(truckers);
        for (Trucker trucker : truckers) {
            int exposedConnectors = trucker.getSpaceShip().countExposedConnectors();
            Game.getIstance().moveBackward(trucker, exposedConnectors);
        }
        Collections.reverse(truckers); //rigiro i Truckers così ritorno all'ordine originale della lista
        ArrayList<Trucker> truckerRemoved;
        truckerRemoved = Game.getIstance().endTurn();
        Map<String, Integer> track = new HashMap<>();
        for (Trucker t : Game.getIstance().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }
        GamePersistence.saveGame(GameController.getIstance());
        return new StarDustEvent(this.getName(), this.getDescription(), true, Game.getIstance().getTruckerList(), null, truckerRemoved, track, this);
    }

}
