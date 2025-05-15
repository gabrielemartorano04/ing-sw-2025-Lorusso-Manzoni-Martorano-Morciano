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
        System.out.println("Error: inserire numero giocatori (1-4) e livello (0 o 2) separati da spazio ");
        return null;
    }

    try {
        int numOfPlayers = Integer.parseInt(tokens[0]);
        int level = Integer.parseInt(tokens[1]);

        if (numOfPlayers < 1 || numOfPlayers > 4) {
            System.out.println("Errore: numero di giocatori non valido (2-4)");
            return null;
        }
        if (level != 0 && level != 2) {
            System.out.println("Errore: livello non valido (0 o 2)");
            return null;
        }

        return new CreateGameEvent(numOfPlayers, level);
    } catch (NumberFormatException e) {
        System.out.println("Errore: entrambi i valori devono essere numeri interi");
        return null;
    }
}
    public ClientEvent parseLogin(String input){
        if (input == null || input.isEmpty()) {
            return null;
        }

        String[] tokens = input.split("\\s+", 2);
        if (tokens.length < 2) {
            System.out.println("Error: username and color are required.");
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
        System.out.println("Errore: illegal color, please write an available color");
        return null;
    }

    public ClientEvent parseBuilding(String input, Component component, int index,ArrayList<Component> kept, boolean isFromKept){
        if (input == null || input.isEmpty()) {
            return null;
        }
        String[] tokens = input.split("\\s+", 3);
        String command = tokens[0].toLowerCase();
        if(component!=null){
            if(isFromKept){
                switch (command){
                    case "link":
                        return new LinkTileEvent(index, Integer.parseInt(tokens[1].trim()), Integer.parseInt(tokens[2].trim()), component);
                    case "keep":
                        return new KeepTileEvent(component);
                    case "turn":
                        // mettere un local event che semplicemente dopo aver turnato
                    case "ship":
                        return new ShowOtherShipEvent(tokens[1].trim());
                }
            }
            switch (command){
                case "turn":
                    // mettere un local event che semplicemente dopo aver turnato

                case "ship":
                    return new ShowOtherShipEvent(tokens[1].trim());

                case "link":
                    return new LinkTileEvent(index, Integer.parseInt(tokens[1].trim()), Integer.parseInt(tokens[2].trim()), component);
                case "keep":
                    return new KeepTileEvent(component);
                case "discard":
                    return new DiscardTileEvent(index);
                default: System.out.println("Error: unknown command: " + command);
                    return null;
            }
        }else {
            switch (command) {
                case "ship":
                    return new ShowOtherShipEvent(tokens[1].trim());
                case "componentlist":
                    return new ShowTilesListEvent();
                case "hourglass":
                    return new TurnHourglassEvent();
                case "select":
                    if(tokens.length==2){
                        return new SelectTileEvent(Integer.parseInt(tokens[1].trim()));
                    }else{
                        System.out.println("Please select an index.");
                    }
                case "selectkept":
                    int ind=Integer.parseInt(tokens[1].trim());
                    if(ind > 1 || ind<0)
                        System.out.println("Please type a valid index.");
                    else {
                        if(kept.isEmpty())
                            System.out.println("You have no kept components.");
                        else if(ind>kept.size()-1)
                            System.out.println("Please type a valid index.");
                        else return new SelectKeptTileEvent(ind);
                    }
                case "stack":
                    if(Integer.parseInt(tokens[1].trim())>=1 && Integer.parseInt(tokens[1].trim())<=3) {
                        return new ShowStackEvent(Integer.parseInt(tokens[1].trim()) - 1);
                    }else{
                        System.out.println("Error: illegal stack number. Please select a stack between 1 and 3 or choose another command");
                    }
                case "end":
                    return new EndBuildingEvent();
                default: System.out.println("Error: unknown command: " + command + ". Please select a valid command");
                    return null;
            }
        }
    }
    public ClientEvent parseFlight(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        String[] tokens = input.split("\\s+", 70); // dividi in massimo 70 pezzi
        String command = tokens[0].toLowerCase();

        switch (command) {
            case "choice":
                return new SendBooleanEvent(Boolean.parseBoolean(tokens[1]));
            case "coordinate":
                if (tokens.length < 2 || tokens.length < 3) {
                    System.out.println("Coordinates required.");
                    return null;
                }
                return new SendCoordEvent(Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]));
            case "coordlist":
                int cont=1;
                ArrayList<int[]> coords=new ArrayList<>();
                while (cont<tokens.length) {
                    coords.add(new int[]{Integer.parseInt(tokens[cont]),Integer.parseInt(tokens[cont+1])});
                    cont+=2;
                }
                return new SendCoordListEvent(coords);
            case "indexes":
                int cont1=1;
                ArrayList<Integer> indexes=new ArrayList<>();
                while (cont1<tokens.length) {
                    indexes.add(Integer.parseInt(tokens[cont1]));
                    cont1++;
                }
                return new SendIndexesEvent(indexes);
            case "value":
                return new SendIntEvent(Integer.parseInt(tokens[1]));

            default:
                System.out.println("Unknown command: " + command);
                return null;
        }
    }
}

