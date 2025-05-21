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


    public ClientEvent parseFlight(String input, String nickname) {
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
                return new HumanCoordEvent(humanCoord,nickname);

            case PLOT_GOOD:
                String[] plotGood=input.split("\\s*,\\s*");
                if(plotGood[0].trim().equals("y")){
                    String[] coord=plotGood[1].trim().split("\\s+");
                    int x=Integer.parseInt(coord[0].trim());
                    int y=Integer.parseInt(coord[1].trim());
                    int cont=Integer.parseInt(plotGood[2].trim());
                    return new PlotGoodEvent(true,new int[]{x,y}, cont,nickname);
                }
                else if(plotGood[0].trim().equals("n")){
                    return new PlotGoodEvent(false,null,-1,nickname);
                }
                System.out.println("Error: invalid command");
                return null;

            case ROLL_DICE:
                String[] rollDice=input.split("\\s+");
                if(rollDice.length!=1 || !rollDice[0].trim().equals("roll")){
                    System.out.println("Error: invalid command");
                    return null;
                }
                return new RollDiceEvent();
            case ACTIVATE_SHIELD:
                String[] activateShield=input.split("\\s*,\\s*");
                if(activateShield[0].trim().equals("n")){
                    return new ActivateShieldEvent(false,null,null,nickname);
                }else if(activateShield[0].trim().equals("y")){
                    String[] coordShield=activateShield[1].trim().split("\\s+");
                    if(coordShield.length!=2){
                        System.out.println("Error: invalid command");
                        return null;
                    }
                    int x=Integer.parseInt(coordShield[0].trim());
                    int y=Integer.parseInt(coordShield[1].trim());
                    String[] coordBS=activateShield[2].trim().split("\\s+");
                    if(coordBS.length!=2){
                        System.out.println("Error: invalid command");
                        return null;
                    }
                    int xB=Integer.parseInt(coordBS[0].trim());
                    int yB=Integer.parseInt(coordBS[1].trim());
                    return new ActivateShieldEvent(true,new int[]{x,y},new int[]{xB,yB},nickname);
                }
                System.out.println("Error: invalid command");
                return null;
            case ACTIVATE_CANNONS:
                String[] activateCannons=input.split("\\s*,\\s*");
                ArrayList<int[]> coordCannons=new ArrayList<>();
                ArrayList<int[]> coordBS=new ArrayList<>();
                int index=-1;
                if(activateCannons[0].trim().equals("n")){
                    return new ActivateCannonsEvent(false,null,null,nickname);
                }
                else if(activateCannons[0].trim().equals("y")){
                    for(int i=1;i<activateCannons.length;i++){
                        if(activateCannons[i].trim().equals("battery"))
                        {
                            index=i+1;
                            break;
                        }
                        String[] coord=activateCannons[i].trim().split("\\s+");
                        if(coord.length!=2){
                            System.out.println("Error: invalid command");
                            return null;
                        }
                        int x=Integer.parseInt(coord[0].trim());
                        int y=Integer.parseInt(coord[1].trim());
                        coordCannons.add(new int[]{x,y});
                    }
                    for (int i=index;i<activateCannons.length;i++){
                        String[] part=activateCannons[i].trim().split("\\s*-\\s*");
                        if(part.length!=2){
                            System.out.println("Error: invalid command");
                            return null;
                        }
                        String[] coord=part[0].trim().split("\\s+");
                        if(coord.length!=2){
                            System.out.println("Error: invalid command");
                            return null;
                        }
                        int x=Integer.parseInt(coord[0].trim());
                        int y=Integer.parseInt(coord[1].trim());
                        int cont=Integer.parseInt(part[1].trim());
                        coordBS.add(new int[]{x,y,cont});
                    }
                }
                return new ActivateCannonsEvent(true,coordCannons,coordBS,nickname);
            case ACTIVATE_ENGINES:
                String[] activateEngines=input.split("\\s*,\\s*");
                ArrayList<int[]> coordEngines=new ArrayList<>();
                ArrayList<int[]> coordBattery=new ArrayList<>();
                int indexE=-1;
                if(activateEngines[0].trim().equals("n")){
                    return new ActivateEnginesEvent(false,null,null,nickname);
                }
                else if(activateEngines[0].trim().equals("y")){
                    for(int i=1;i<activateEngines.length;i++){
                        if(activateEngines[i].trim().equals("battery"))
                        {
                            indexE=i+1;
                            break;
                        }
                        String[] coord=activateEngines[i].trim().split("\\s+");
                        if(coord.length!=2){
                            System.out.println("Error: invalid command");
                            return null;
                        }
                        int x=Integer.parseInt(coord[0].trim());
                        int y=Integer.parseInt(coord[1].trim());
                        coordEngines.add(new int[]{x,y});
                    }
                    for (int i=indexE;i<activateEngines.length;i++){
                        String[] part=activateEngines[i].trim().split("\\s*-\\s*");
                        if(part.length!=2){
                            System.out.println("Error: invalid command");
                            return null;
                        }
                        String[] coord=part[0].trim().split("\\s+");
                        if(coord.length!=2){
                            System.out.println("Error: invalid command");
                            return null;
                        }
                        int x=Integer.parseInt(coord[0].trim());
                        int y=Integer.parseInt(coord[1].trim());
                        int cont=Integer.parseInt(part[1].trim());
                        coordBattery.add(new int[]{x,y,cont});
                    }
                }
                return new ActivateEnginesEvent(true,coordEngines,coordBattery,nickname);
            case DRAW_CARD:
                String[] cardFinish=input.split("\\s+");
                if(cardFinish.length!=1 || !cardFinish[0].trim().equals("draw")){
                    System.out.println("Error: invalid command");
                    return null;
                }
                return new DrawCardEvent();
            case PLANET_DECISION:
                String[] planetDecision=input.split("\\s+");
                if(planetDecision.length!=1){
                    System.out.println("Error: invalid command");
                    return null;
                }
                return new PlanetDecisionEvent(Integer.parseInt(planetDecision[0].trim()));
            case DO_NOTHING:
                System.out.println("It's not your turn");
                return null;
            case LAND: //type yes or no if you want to land
                String[] land=input.split("\\s+");
                if(land.length!=1){
                    System.out.println("Error: invalid command");
                    return null;
                }
                if(land[0].trim().equals("n")){
                    return new TruckerLandingEvent(false);
                }
                else if(land[0].trim().equals("y")) {
                    return new TruckerLandingEvent(true);
                }
            case WARZONE://type warzone when you are ready
                //todo inserire input format
                String[] warzone=input.split("\\s+");
                if(warzone.length!=1 || !warzone[0].trim().equals("warzone")){
                    System.out.println("Error: invalid command");
                    return null;
                }
                return new WarzoneStartEvent();
            case COSMIC_CREDITS:
                String[] cosmicCredits=input.split("\\s+");
                if(cosmicCredits.length!=1){
                    System.out.println("Error: invalid command");
                    return null;
                }
                if(cosmicCredits[0].trim().equals("n")){
                    return new GainCosmicCreditsResponseEvent(false);
                }
                else if(cosmicCredits[0].trim().equals("y")) {
                    return new GainCosmicCreditsResponseEvent(true);
                }
            case REARRANGE:
                String[] rearrange=input.split("\\s*,\\s*");
                if(rearrange[0].trim().equals("n")){
                    return new RearrangeEvent(null,null,-1,-1,true,nickname);
                }
                if(rearrange[0].trim().equals("y")){
                    String[] part1=rearrange[1].trim().split("\\s*-\\s*");
                    String[] coord=part1[0].trim().split("\\s+");
                    if(coord.length!=2){
                        System.out.println("Error: invalid command");
                        return null;
                    }
                    int x1=Integer.parseInt(coord[0].trim());
                    int y1=Integer.parseInt(coord[1].trim());
                    int value1=Integer.parseInt(part1[1].trim());
                    String[] part2=rearrange[2].trim().split("\\s*-\\s*");
                    String[] coord2=part2[0].trim().split("\\s+");
                    if(coord2.length!=2){
                        System.out.println("Error: invalid command");
                        return null;
                    }
                    int x2=Integer.parseInt(coord2[0].trim());
                    int y2=Integer.parseInt(coord2[1].trim());
                    int value2=Integer.parseInt(part2[1].trim());

                    return new RearrangeEvent(new int[]{x1,y1},new int[]{x2,y2},value1,value2,false,nickname);

                }

            case REMOTION_CONFIRMED:
                String[] remotion=input.split("\\s+");
                if(remotion.length!=1 || !remotion[0].trim().equals("continue")){
                    System.out.println("Error: invalid command");
                    return null;
                }
                return new RemotionSeenEvent();
            case REMOVE_BATTERIES:
                String[] removeBatteries=input.split("\\s*,\\s*");
                ArrayList<int[]> coordinates=new ArrayList<>();
                for(int i=0;i<removeBatteries.length;i++){
                    String[] parts=removeBatteries[i].trim().split("\\s*-\\s*");
                    if(parts.length!=2){
                        System.out.println("Error: invalid command");
                        return null;
                    }
                    String[] coord=parts[0].trim().split("\\s+");
                    if(coord.length!=2){
                        System.out.println("Error: invalid command");
                        return null;
                    }
                    int x=Integer.parseInt(coord[0].trim());
                    int y=Integer.parseInt(coord[1].trim());
                    int cont=Integer.parseInt(parts[1].trim());
                    coordinates.add(new int[]{x,y,cont});
                }
                return new RemoveBatteriesEvent(coordinates,nickname);
            case REMOVE_GOODS:
                String[] removeGoods=input.split("\\s*,\\s*");
                ArrayList<Integer> indexes=new ArrayList<>();
                for(int i=0;i<removeGoods.length;i++){
                    indexes.add(Integer.parseInt(removeGoods[i].trim()));
                }
                return new RemoveAskingEvent(indexes);
            default: {
                System.out.println("Error: invalid command. Stato non gestito");
                return null;
            }
        }
    }
}
