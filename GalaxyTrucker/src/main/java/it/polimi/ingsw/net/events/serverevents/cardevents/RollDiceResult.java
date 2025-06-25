package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class RollDiceResult extends CardEvent {

    private final int currIndex;
    private final int diceResult;
    private final String hitType;
    private final int[] shoot;
    private final int[] meteor;

    public RollDiceResult(ParserState parserState, int currIndex, int[] shoot, int [] meteor, int diceResult, String nickname, boolean forAll, String hitType, ArrayList<Trucker> truckers){
        super(forAll, parserState, nickname, truckers);
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

    public String getInfoForOthers(){
        return getNickname()+" got "+diceResult+ " from the dice\n";
    }

    public String getHitType() {return hitType;}
}



