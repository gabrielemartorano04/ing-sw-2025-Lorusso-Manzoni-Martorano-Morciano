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
    private CardState cardState;

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
    public synchronized ServerEvent startCard() {
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
    public synchronized ServerEvent cardEffect() throws GeneralError {
        if(cardState != CardState.ESECUZIONE) {
            throw new GeneralError("Not callable in the state " + cardState);
        }
        ArrayList<Trucker> truckers = Game.getIstance().getTruckerList();
        for (int i = truckers.size() - 1; i > 0; i--) {
            System.out.println("Current trucker " + truckers.get(i).getNickname());
            Trucker trucker = truckers.get(i);
            int exposedConnectors = trucker.getSpaceShip().countExposedConnectors();
            System.out.println("exposed connectors " + exposedConnectors);
            Game.getIstance().moveBackward(trucker, exposedConnectors);
        }

        ArrayList<Trucker> truckerRemoved;
        truckerRemoved = Game.getIstance().endTurn();

        GamePersistence.saveGame(GameController.getIstance());

        return new StarDustEvent(this.getName(), this.getDescription(), true, Game.getIstance().getTruckerList(), null, truckerRemoved, Game.getIstance().getTrack(), this);
    }

    public void setState(CardState state){
        this.cardState=state;
    }

}
