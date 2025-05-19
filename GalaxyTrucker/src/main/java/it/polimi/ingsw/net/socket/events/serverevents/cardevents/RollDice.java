package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class RollDice extends CardEvent {

    private int currMeteorIndex;
    private int diceResult;

    public RollDice(CardState cardState, int currMeteorIndex, int diceResult, String nickname, boolean forAll){
        super(ServerEventType.ROLL_DICE, forAll, cardState, nickname);
        this.currMeteorIndex = currMeteorIndex;
        this.diceResult = diceResult;
    }

    public int getCurrMeteorIndex() {return currMeteorIndex;}

    public int getDiceResult() {return diceResult;}

    @Override
    public void call(ServerEventHandlerVisitor visitor) {

    }

    public String getInfoForOthers(){
        return getNickname()+" got "+diceResult+ " from the dice\n";
    }
}
