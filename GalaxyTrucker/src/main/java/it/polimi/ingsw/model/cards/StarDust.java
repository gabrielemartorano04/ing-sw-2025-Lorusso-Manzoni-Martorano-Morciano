package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.callers.StarDustCaller;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.utilities.exceptions.GeneralError;

import java.io.Serializable;
import java.util.*;

/**
 * Class that manages the single card star dust
 * In reverse order of position, the truckers lose as many days of flight as the number of connectors exposed
 */
public class StarDust extends Card implements Serializable {
    private static final long serialVersionUID = 1L;

    private CardState cardState;
    /**
     * Star dust constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public StarDust(boolean trialFlight, int level, String frontImage, String backImage) {
        super("Star Dust", "A cloud of cosmic dust", trialFlight, level, frontImage, backImage, new StarDustCaller());
        this.cardState = CardState.ESECUZIONE;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public CardState cardEffect(Game game) throws GeneralError {
        if(cardState != CardState.ESECUZIONE) {
            throw new GeneralError("Not callable in the state " + cardState);
        }
        List<Trucker> truckers = game.getTruckerList();
        Collections.reverse(truckers);
        for (Trucker trucker : truckers) {
            int exposedConnectors = trucker.getSpaceShip().countExposedConnectors();
            game.moveBackward(trucker, exposedConnectors);
        }
        Collections.reverse(truckers); //rigiro i Truckers così ritorno all'ordine originale della lista
        return CardState.EXIT;
    }

    @Override
    public ServerEvent startCard(String currPlayerNickname) {
        return null;
    }
}
