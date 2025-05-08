package it.polimi.ingsw.view;

import it.polimi.ingsw.model.BuildingState;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.events.*;
import it.polimi.ingsw.net.socket.events.clientevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.GenericErrorInsertedData;

import java.util.ArrayList;

public class InputParser {
// TODO: AGGIUNGERE COMANDI CHE SONO VALIDI SEMPRE
    public ClientEvent parseLogin(String input){
        if (input == null || input.isEmpty()) {
            return null;
        }

        String[] tokens = input.split("\\s+", 2);
        if (tokens.length < 2) {
            System.out.println("Error: username and color are required.");
            return null;
        }
        PlayerColors color=PlayerColors.BLUE;
        switch (tokens[1].toLowerCase()) {
            case "blue":  break;
            case "red": color = PlayerColors.RED; break;
            case "green": color = PlayerColors.GREEN; break;
            case "yellow": color = PlayerColors.YELLOW; break;
        }
        return new LoginEvent(tokens[0].trim(),color);

    }

    public ClientEvent parseBuilding(String input, Component component){
        if (input == null || input.isEmpty()) {
            return null;
        }
        String[] tokens = input.split("\\s+", 3);
        String command = tokens[0].toLowerCase();
        switch (command) {
            case "hourglass":
                return new TurnHourglassEvent();
            case "select":
                return new SelectTileEvent(Integer.parseInt(tokens[1].trim()));
            case "stack":
                return new ShowStackEvent(Integer.parseInt(tokens[1].trim()));
            case "end":
                return new EndBuildingEvent();
            case "link":
                return new LinkTileEvent(Integer.parseInt(tokens[1].trim()), Integer.parseInt(tokens[2].trim()));
            case "keep":
                return new KeepTileEvent();
            case "discard":
                return new DiscardTileEvent(component);
            default: System.out.println("Error: unknown command: " + command);
                return null;
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
                return new LinkTileEvent(Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]));
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

