package it.polimi.ingsw.model.resulthandlers;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;
import it.polimi.ingsw.net.socket.events.serverevents.AskBooleanEvent;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.IntResult;
import it.polimi.ingsw.utilities.results.Result;

import java.util.ArrayList;

public class AbandonedShipHandler implements ResultHandler {


    public Event handleResult(CardStateResult result, AbandonedShip card) {

        Event e = result.getState().processStateForThisClass();
        return e;


        switch (result.getState()){
            case ATTESA_RISPOSTE -> {
                String question = "Would you like to lose the number of members of your crew, and the number of flight days indicated in the card to obtain the number of cosmic credits indicated";
                return new AskBooleanEvent(EventType.ASK_BOOLEAN, question, card.getPlayerDecisions());
            }
            default -> throw new IllegalStateException("Unexpected value: " + result.getState());
        }
    }

    public Event handleresult(IntResult result){
        return null;
    }
}


