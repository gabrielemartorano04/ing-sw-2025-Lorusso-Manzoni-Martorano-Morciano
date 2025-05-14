package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TruckerController;
import it.polimi.ingsw.model.callers.EpidemicCaller;
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
        super("Epidemic", "A contagious outbreak", trialFlight, level, frontImage, backImage, new EpidemicCaller());
        this.state = CardState.ESECUZIONE;
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
        List <TruckerController> truckerControllers = game.getTruckerList();
        for(TruckerController truckerController : truckerControllers) {
            truckerController.getSpaceShip().epidemic();
        }
        state = CardState.EXIT;
        return state;
    }

    public CardState getCardState() {
        return state;
    }

    public void setCardState(CardState cardState) {
        this.state = cardState;
    }
}
