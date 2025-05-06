package it.polimi.ingsw.model.resulthandlers;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.utilities.results.CardStateResult;

import java.util.ArrayList;

public class AbandonedShipHandler implements ResultHandler {
    private ArrayList<Trucker> truckers;
    private int currId;

    public AbandonedShipHandler(ArrayList<Trucker> truckers) {
        this.truckers = truckers;
        this.currId = 0;
    }

    public Event handleResult(CardStateResult result) {
        switch (result.getState()){
            case ATTESA_RISPOSTE -> {
                String question = "Would you like to lose the number of members of your crew, and the number of flight days indicated in the card to obtain the number of cosmic credits indicated";

            }
        }
    }
}
