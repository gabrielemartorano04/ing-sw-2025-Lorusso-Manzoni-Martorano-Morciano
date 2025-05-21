package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.events.clientevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.socket.events.clientevents.setupevents.LoginEvent;
import it.polimi.ingsw.net.socket.events.serverevents.GenericErrorInsertedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InputParser {
    // TODO: AGGIUNGERE COMANDI CHE SONO VALIDI SEMPRE
public ClientEvent parseFirstConnection(String input) {
    if (input == null || input.isEmpty()) {
        return null;
    }

    input = input.trim();
    String[] tokens = input.split("\\s+");

    if (tokens.length != 2) {
        System.out.println("Error: insert number of players (1-4) and level (0 o 2) divided by space");
        return null;
    }

    try {
        int numOfPlayers = Integer.parseInt(tokens[0]);
        int level = Integer.parseInt(tokens[1]);
        //TODO: CAMBIARE NUMERO GIOCATORI DA 2 A 4 DOPO DEBUG
        if (numOfPlayers < 1 || numOfPlayers > 4) {
            System.out.println("Error: invalid number of players (2-4)");
            return null;
        }
        if (level != 0 && level != 2) {
            System.out.println("Error: invalid level (0 o 2)");
            return null;
        }

        return new CreateGameEvent(numOfPlayers, level);
    } catch (NumberFormatException e) {
        System.out.println("Error: both value needs to be integer numbers");
        return null;
    }
}
    public ClientEvent parseLogin(String input){
        if (input == null || input.isEmpty()) {
            return null;
        }

        String[] tokens = input.split("\\s+", 2);
        if (tokens.length < 2) {
            System.out.println("Error: username and color are required and they have to be divided by space.");
            return null;
        }
        if (tokens.length > 2) {
            System.out.println("Error: type only username and color and they have to be divided by space.");
            return null;
        }
        PlayerColors color=null;
        switch (tokens[1].toLowerCase()) {
            case "blue": color= PlayerColors.BLUE; break;
            case "red": color = PlayerColors.RED; break;
            case "green": color = PlayerColors.GREEN; break;
            case "yellow": color = PlayerColors.YELLOW; break;

        }
        if(color!=null) {
            return new LoginEvent(tokens[0].trim(), color);
        }
        System.out.println("Error: illegal color, please write an available color");
        return null;
    }

    public ClientEvent parseBuilding(String input, Component component, int index,ArrayList<Component> kept, boolean isFromKept, int stackSelected){
        if (input == null || input.isEmpty()) {
            return null;
        }
        String[] tokens = input.split("\\s+", 3);
        String command = tokens[0].toLowerCase();
        if(stackSelected==-1) {
            if (component != null) {
                if (isFromKept) {
                    switch (command) {
                        case "turn":
                            if (tokens.length != 1) {
                                System.out.println("Error: illegal command.");
                                return null;
                            }
                            return new TurnComponentEvent(component);
                        case "link":
                            if (tokens.length != 3) {
                                System.out.println("Error: illegal command. Type 'link <x> <y>'.");
                                return null;
                            }
                            int x=Integer.parseInt(tokens[1].trim());
                            int y=Integer.parseInt(tokens[2].trim());
                            if(x<5 || x>9 || y<4 || y>10){
                                System.out.println("Error: illegal coordinates. Type 'link <x> <y>'.");
                                return null;
                            }
                            return new LinkTileEvent(index, x, y, component);
                        case "keep":
                            if (tokens.length != 1) {
                                System.out.println("Error: illegal command. Type 'keep <index>'.");
                                return null;
                            }
                            return new KeepTileEvent(component);
                        case "ship":
                            if (tokens.length != 2) {
                                System.out.println("Error: illegal command. Type 'ship <nickname>'.");
                                return null;
                            }
                            return new ShowOtherShipEvent(tokens[1].trim());
                        default:
                            System.out.println("Error: unknown command: " + command);
                            return null;
                    }
                }
                switch (command) {
                    case "turn":
                        if (tokens.length != 1) {
                            System.out.println("Error: illegal command.");
                            return null;
                        }
                        return new TurnComponentEvent(component);
                    case "ship":
                        if (tokens.length != 2) {
                            System.out.println("Error: illegal command. Type 'ship <nickname>'.");
                            return null;
                        }
                        return new ShowOtherShipEvent(tokens[1].trim());

                    case "link":
                        if (tokens.length != 3) {
                            System.out.println("Error: illegal command. Type 'link <x> <y>'.");
                            return null;
                        }
                        return new LinkTileEvent(index, Integer.parseInt(tokens[1].trim()), Integer.parseInt(tokens[2].trim()), component);
                    case "keep":
                        if (tokens.length != 1) {
                            System.out.println("Error: illegal command. Type 'keep' if you want to keep the selected component.");
                            return null;
                        }
                        return new KeepTileEvent(component);
                    case "discard":
                        if (tokens.length != 1) {
                            System.out.println("Error: illegal command. Type 'discard' if you want to discard the selected component.");
                            return null;
                        }
                        return new DiscardTileEvent(index);
                    default:
                        System.out.println("Error: unknown command: " + command);
                        return null;
                }
            } else {
                switch (command) {
                    case "ship":
                        if (tokens.length != 2) {
                            System.out.println("Error: illegal command. Type 'ship <nickname>'.");
                            return null;
                        }
                        return new ShowOtherShipEvent(tokens[1].trim());
                    case "hourglass":
                        if (tokens.length != 1) {
                            System.out.println("Error: illegal command. Type 'hourglass' if you want to turn the hourglass");
                            return null;
                        }
                        return new TurnHourglassEvent();
                    case "select":
                        if (tokens.length != 2) {
                            System.out.println("Error: illegal command. Type 'select <index>'.");
                            return null;
                        }
                        return new SelectTileEvent(Integer.parseInt(tokens[1].trim()));

                    case "selectkept":
                        int ind = Integer.parseInt(tokens[1].trim());
                        if (tokens.length != 2) {
                            System.out.println("Error: illegal command. Type 'selectkept <index>'.");
                            return null;
                        }
                        if (ind < 0 || ind > kept.size() - 1) {
                            System.out.println("Please type a valid index. Type 'selectkept <index>'.");
                            return null;
                        }
                        return new SelectKeptTileEvent(ind);

                    case "stack":
                        if (tokens.length != 2) {
                            System.out.println("Error: illegal command. Type 'stack <index>'.");
                            return null;
                        }
                        if (Integer.parseInt(tokens[1].trim()) >= 1 && Integer.parseInt(tokens[1].trim()) <= 3) {
                            return new ShowStackEvent(Integer.parseInt(tokens[1].trim()) - 1);
                        } else {
                            System.out.println("Error: illegal stack number. Please select a stack between 1 and 3 or choose another command");
                        }
                        return null;
                    case "end":
                        if (tokens.length != 1) {
                            System.out.println("Error: illegal command. Type 'end' if you want to end building phase.");
                            return null;
                        }// il client finisce e aspetta o che tutti finiscono o che il timer
                        return new EndBuildingEvent();
                    default:
                        System.out.println("Error: unknown command: " + command + ". Please select a valid command");
                        return null;
                }
            }
        }else{
            if(command.equals("dropstack")) {
                if (tokens.length != 1) {
                    System.out.println("Error: illegal command. Type 'dropstack' if you want to drop your stack and proceed");
                    return null;
                }
                return new StackDroppedEvent(stackSelected);
            }else {
                System.out.println("Error: illegal command. Type 'dropstack' if you want to drop your stack and proceed");
                return null;
            }
        }
    }

    public ClientEvent parseFillTheShip(String input){
        if (input == null || input.isEmpty()) {
            return null;
        }
        String[] tokens = input.split("\\s+", 3);
        String command = tokens[0].toLowerCase();
        switch (command){
            case "coord":
                if(tokens.length%2 !=0){
                    ArrayList<int[]> coord=new ArrayList<>();
                    for(int i=1;i<tokens.length-1;i+=2){
                        if(tokens[i]!=null && tokens[i+1]!=null){
                            coord.add(new int[]{Integer.parseInt(tokens[i]),Integer.parseInt(tokens[i+1])});
                        }
                    }
                    return new SendAlienCoord(coord);
                }else {
                    System.out.println("Error: illegal command. Type 'coord <x1> <y1> <x2> <y2>...' .");
                    return null;
                }
            default:
                System.out.println("Error: unknown command: " + command);
                return null;
        }

    }
}

