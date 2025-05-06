package it.polimi.ingsw.model.resulthandlers;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.utilities.results.CardStateResult;

public class AbandonedShipHandler implements ResultHandler {
    public Event handleResult(CardStateResult result) {
        switch (result.getState()){
            case ATTESA_RISPOSTE -> {

            }
        }
    }
}
