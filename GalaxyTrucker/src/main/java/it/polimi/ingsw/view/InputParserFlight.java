package it.polimi.ingsw.view;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.*;

import java.util.ArrayList;

public class InputParserFlight {
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
