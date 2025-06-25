package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.net.events.serverevents.GenericErrorEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.EpidemicEvent;
import it.polimi.ingsw.exceptions.GeneralError;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that manages the single card epidemic
 * Every trucker has to remove a crew member from each occupied cabin that is connected to another occupied cabin
 */
public class Epidemic extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * current state of the card, helper for debug and checking consistency
     */
    public CardState state;

    ArrayList<Trucker> truckerRemoved;

    /**
     * Epidemic constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public Epidemic(boolean trialFlight, int level, String frontImage, String backImage, int id) {
        super("Epidemic", "A contagious outbreak", trialFlight, level, frontImage, backImage, id);
        this.truckerRemoved = new ArrayList<>();
        this.state = CardState.ESECUZIONE;
    }

    public CardState getCardState() {
        return state;
    }


    /**
     * start the card and applies its effect since there is no need of interaction with clients
     * @return serverEvent to send to clients
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
     * Applies the changes provided by the card:
     * removes a crew member from each occupied cabin that is connected to another occupied cabin for each player
     *
     * @return serverEvent to send to clients
     */
    @Override
    public ServerEvent cardEffect() throws GeneralError {
        if(state != CardState.ESECUZIONE) {
            throw new GeneralError("Non puoi settare fare CardEffect nello stato: " + state);
        }
        List <Trucker> truckers = Game.getIstance().getTruckerList();
        for(Trucker trucker : truckers) {
            System.out.println(trucker.getNickname());
            trucker.getSpaceShip().epidemic();
        }
        truckerRemoved = Game.getIstance().endTurn();
        Map<String, Integer> track = new HashMap<>();
        for (Trucker t : Game.getIstance().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }
        GamePersistence.saveGame(GameController.getIstance());
        return new EpidemicEvent(this.getName(), this.getDescription(), true, Game.getIstance().getTruckerList(), null, truckerRemoved, track, this);
    }

    //per test

    public ArrayList<Trucker> getTruckersRemoved(){
        return truckerRemoved;
    }

}
