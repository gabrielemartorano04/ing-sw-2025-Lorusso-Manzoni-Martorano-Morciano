package it.polimi.ingsw.view;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.*;

import java.util.ArrayList;

public class InputParserFlight {
    private ParserState parserState;
    //lo stato cambia nel serverhandler

    public void setParserState(ParserState parserState) {
        this.parserState = parserState;
    }

    public ParserState getParserState() {
        return parserState;
    }


    public ClientEvent parseFlight(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        String[] tokens = input.split("\\s+", 70); // dividi in massimo 70 pezzi
        String command = tokens[0].toLowerCase();

        switch (parserState) {
            case HUMAN_COORD:
                if (tokens.length % 2 == 0) {
                    ArrayList<int[]> humanCoord=new ArrayList<>();
                    for(int i=1;i<tokens.length-1;i+=2){
                        if(tokens[i]!=null && tokens[i+1]!=null){
                            humanCoord.add(new int[]{Integer.parseInt(tokens[i]),Integer.parseInt(tokens[i+1])});
                        }
                    }
                    return new SendHumanCoordEvent(humanCoord);
                } else {
                    System.out.println("Error: coordinates expected");
                    return null;
                }

            case PLOT_GOOD:
                return null;
            case ROLL_DICE:
                return null;
            case STARDUST:
                return null;
            case BOOLEAN_DECISION:
                return null;
            case CARD_FINISH:
                return null;
            case EPIDEMIC:
                return null;
            case EXECUTION:
                return null;
            case PLANET_DECISION:
                if (tokens.length == 1){
                    //todo: cosa devo passare??
                } else {
                    System.out.println("Error: wrong number of planet");
                    return null;
                }
            default: return null;
        }
    }
}
