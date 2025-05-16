package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.callers.EpidemicController;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.flightevents.StartCardEvent;
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
        super("Epidemic", "A contagious outbreak", trialFlight, level, frontImage, backImage, new EpidemicController());
        this.state = CardState.ESECUZIONE;
    }

    @Override
    public ServerEvent startCard(String currPlayerNickname) {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                currPlayerNickname,
                "Ready for the Epidemic effect?",
                -1,
                -1,
                -1,
                null,
                null,
                -1,
                -1,
                -1,
                null,
                null,
                null,
                null
        );
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public CardState cardEffect(Game game) throws GeneralError {
        if(state != CardState.ESECUZIONE) {
            throw new GeneralError("Non puoi settare fare CardEffect nello stato: " + state);
        }
        List <Trucker> truckers = game.getTruckerList();
        for(Trucker trucker : truckers) {
            trucker.getSpaceShip().epidemic();
        }
        state = CardState.EXIT;
        this.setIsCardFinished(true);
        return state;
    }


    public CardState getCardState() {
        return state;
    }

    public void setCardState(CardState cardState) {
        this.state = cardState;
    }
}
