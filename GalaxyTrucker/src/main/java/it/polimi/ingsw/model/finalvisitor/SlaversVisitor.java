package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.Slavers;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.params.Params;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.Result;

import java.util.ArrayList;

public class SlaversVisitor {
    public Result visit(Slavers card, Trucker trucker, ArrayList<int[]> coord, int numPlayer) throws InvalidCoordinates{
        if(card.getCardState() == CardState.HUMANSTOREMOVE){
            return new CardStateResult(card.addTruckersCoordinatesToRemoveHumans(trucker, coord, numPlayer));
        }else{
            return new CardStateResult(card.addDoubleCannonsCoord(trucker, coord, numPlayer));
        }
    }

    public Result visit(Slavers card, boolean choice){
        return new CardStateResult(card.setChooseToGain(choice));
    }

    public Result visit(Slavers card, Game game){
        return new CardStateResult(card.cardEffect(game));
    }
}
