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

        switch (parserState) {
            case HUMAN_COORD:
                String[] groups= input.split("\\s*,\\s*");
                ArrayList<int[]> humanCoord=new ArrayList<>();
                for(String group: groups){
                    String[] parts = group.split("\\s*-\\s*");
                    if(parts.length!=2){
                        System.out.println("Error: invalid command");
                        return null;
                    }
                    String[] coord=parts[0].trim().split("\\s+");
                    if(coord.length!=2){
                        System.out.println("Error: invalid command");
                        return null;
                    }
                    int num=Integer.parseInt(parts[1].trim());
                    int x=Integer.parseInt(coord[0].trim());
                    int y=Integer.parseInt(coord[1].trim());
                    humanCoord.add(new int[]{x,y,num});
                }
                return new SendHumanCoordEvent(humanCoord);

            case PLOT_GOOD:
                String[] plotGood=input.split("\\s+");
                if(plotGood[0].trim().equals("y")){
                    int x=Integer.parseInt(plotGood[1].trim());
                    int y=Integer.parseInt(plotGood[2].trim());
                    return new SendPlotGoodEvent(true,x,y);
                }
                else if(plotGood[0].trim().equals("n")){
                    return new SendPlotGoodEvent(false,-1,-1);
                }
                System.out.println("Error: invalid command");
                return null;

            case ROLL_DICE:
                String[] rollDice=input.split("\\s+");
                if(rollDice.length!=1 || !rollDice[0].trim().equals("roll")){
                    System.out.println("Error: invalid command");
                    return null;
                }
                return new SendRollDiceEvent();
            case ACTIVATE_SHIELD:
                String[] activateShield=input.split("\\s+");
                if(activateShield[0].trim().equals("n")){
                    return new SendActivateShieldEvent(false,-1,-1);
                }else if(activateShield[0].trim().equals("y")){
                    int x=Integer.parseInt(activateShield[1].trim());
                    int y=Integer.parseInt(activateShield[2].trim());
                    return new SendActivateShieldEvent(true,x,y);
                }
                System.out.println("Error: invalid command");
                return null;
            case ACTIVATE_CANNONS:
                String[] activateCannons=input.split("\\s*,\\s*");
                ArrayList<int[]> coordinates=new ArrayList<>();
                if(activateCannons[0].trim().equals("n")){
                    return new SendActivateCannonsEvent(false,null);
                }
                else if(activateCannons[0].trim().equals("y")){
                    for(int i=1;i<activateCannons.length;i++){
                        String[] coord=activateCannons[i].trim().split("\\s+");
                        if(coord.length!=2){
                            System.out.println("Error: invalid command");
                            return null;
                        }
                        int x=Integer.parseInt(coord[0].trim());
                        int y=Integer.parseInt(coord[1].trim());
                        coordinates.add(new int[]{x,y});
                    }
                }
                return new SendActivateCannonsEvent(true,coordinates);
            case ACTIVATE_ENGINES:
                String[] activateEngines=input.split("\\s*,\\s*");
                ArrayList<int[]> coordEngines=new ArrayList<>();
                if(activateEngines[0].trim().equals("n")){
                    return new SendActivateEnginesEvent(false,null);
                }
                else if(activateEngines[0].trim().equals("y")){
                    for(int i=1;i<activateEngines.length;i++){
                        String[] coord=activateEngines[i].trim().split("\\s+");
                        if(coord.length!=2){
                            System.out.println("Error: invalid command");
                            return null;
                        }
                        int x=Integer.parseInt(coord[0].trim());
                        int y=Integer.parseInt(coord[1].trim());
                        coordEngines.add(new int[]{x,y});
                    }
                }
                return new SendActivateCannonsEvent(true,coordEngines);
            case CARD_FINISH:
                return null;
            case PLANET_DECISION:
                String[] planetDecision=input.split("\\s+");
                if(planetDecision.length!=1){
                    System.out.println("Error: invalid command");
                    return null;
                }
                return new SendPlanetDecisionEvent(Integer.parseInt(planetDecision[0].trim()));
            default: {
                System.out.println("Error: invalid command. Stato non gestito");
                return null;
            }
        }
    }
}
