package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class BooleanDecision extends CardEvent{

    public BooleanDecision(boolean forAll, ParserState parserState, String nickname, ArrayList<Trucker> truckers) {
        super(forAll, parserState, nickname, truckers);
    }

    public String getInfoForOthers(){
        switch(getParserState()){
            case ROLL_DICE -> {
                return getNickname() + " is rolling the dice";
            }
            case ACTIVATE_CANNONS -> {
                return getNickname() + " is deciding which cannons to activate";
            }
            case ACTIVATE_ENGINES -> {
                return getNickname() + " is deciding which engines to activate";
            }
            case ACTIVATE_SHIELD -> {
                return getNickname() + " is deciding which shield to activate";
            }
            case LAND -> {
                return getNickname() + " is deciding whether to land or not";
            }
        }
        return "";
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
