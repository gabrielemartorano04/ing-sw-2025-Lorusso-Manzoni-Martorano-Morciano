package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class RollDiceResult extends CardEvent {

    private int currIndex;
    private int diceResult;
    private String hitType;
    private int[] shoot,meteor;

    public RollDiceResult(ParserState parserState, int currIndex,int[] shoot,int [] meteor, int diceResult, String nickname, boolean forAll, String hitType){
        super(ServerEventType.ROLL_DICE, forAll, parserState, nickname);
        this.currIndex = currIndex;
        this.shoot=shoot;
        this.meteor=meteor;
        this.diceResult = diceResult;
        this.hitType = hitType;
    }

    public int getCurrIndex() {return currIndex;}

    public int[] getShoot(){
        return this.shoot;
    }

    public int[] getMeteor(){
        return meteor;
    }

    public int getDiceResult() {return diceResult;}

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getMessage(){
        return "ERRORE IN ROLLDICERESULT, hai chiamato getMessage";
    }

    public String getInfoForOthers(){
        return getNickname()+" got "+diceResult+ " from the dice\n";
    }

    public String getHitType() {return hitType;}
}



