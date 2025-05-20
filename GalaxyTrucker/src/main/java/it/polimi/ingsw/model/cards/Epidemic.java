package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.net.socket.events.serverevents.GenericErrorEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.CardFinished;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.EpidemicEvent;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.utilities.exceptions.GeneralError;

import java.io.Serializable;
import java.util.List;

/**
 * Class that manages the single card epidemic
 * Every trucker has to remove a crew member from each occupied cabin that is connected to another occupied cabin
 */
public class Epidemic extends Card implements Serializable {
    private static final long serialVersionUID = 1L;
    public CardState state;
    /**
     * Epidemic constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public Epidemic(boolean trialFlight, int level, String frontImage, String backImage) {
        super("Epidemic", "A contagious outbreak", trialFlight, level, frontImage, backImage);
        this.state = CardState.ESECUZIONE;
    }

    @Override
    public ServerEvent startCard() {
        // TODO: mettere le informazioni per stampare la carta dentro l'epidemicEvent
        try {
            return cardEffect();
        } catch (GeneralError e) {
            return new GenericErrorEvent("errore", true);
        }
    }

    /**
     * Applies the changes provided by the card
     */
    @Override
    public ServerEvent cardEffect() throws GeneralError {
        if(state != CardState.ESECUZIONE) {
            throw new GeneralError("Non puoi settare fare CardEffect nello stato: " + state);
        }
        List <Trucker> truckers = Game.getIstance().getTruckerList();
        for(Trucker trucker : truckers) {
            trucker.getSpaceShip().epidemic();
        }
        state = CardState.EXIT;
        this.setIsCardFinished(true);
        Game.getIstance().endTurn();
        return new EpidemicEvent(this.getName(), this.getDescription(), true, this.getCardState(), Game.getIstance().getTruckerList());
    }


    public CardState getCardState() {
        return state;
    }

    public void setCardState(CardState cardState) {
        this.state = cardState;
    }
}
