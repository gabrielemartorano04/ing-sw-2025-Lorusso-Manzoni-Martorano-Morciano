package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class RollDice extends CardEvent {

    private int currIndex;
    private int diceResult;
    private String message;

    public RollDice(String Message, CardState cardState, int currIndex, int diceResult, String nickname, boolean forAll){
        super(ServerEventType.ROLL_DICE, forAll, cardState, nickname);
        this.currIndex = currIndex;
        this.diceResult = diceResult;
        this.message = message;
    }

    public int getCurrIndex() {return currIndex;}

    public int getDiceResult() {return diceResult;}

    @Override
    public void call(ServerEventHandlerVisitor visitor) {

    }

    @Override
    public String getMessage(){
        return message;
    }

    public String getInfoForOthers(){
        return getNickname()+" got "+diceResult+ " from the dice\n";
    }
}
