package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.callers.EpidemicCaller;
import it.polimi.ingsw.model.visitor.CardVisitor;
import it.polimi.ingsw.model.visitor.ParametricCardVisitor;

import java.util.List;

/**
 * Class that manages the single card epidemic
 * Every trucker has to remove a crew member from each occupied cabin that is connected to another occupied cabin
 */
public class Epidemic extends Card {
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

    @Override
    public Object accept(CardVisitor visitor) {
        return visitor.visitForEpidemic(this);
    }

    @Override
    public Object accept(ParametricCardVisitor visitor, Object... params) {
        return visitor.visitForEpidemic(this, params);
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public CardState cardEffect(Game game) {
        if(state != CardState.ESECUZIONE) {
            throw new IllegalStateException("Non puoi settare fare CardEffect nello stato: " + state);
        }
        List <Trucker> truckers = game.getTruckerList();
        for(Trucker trucker: truckers) {
            trucker.getSpaceShip().epidemic();
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
