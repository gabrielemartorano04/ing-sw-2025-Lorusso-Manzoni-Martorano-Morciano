package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.net.socket.events.*;
import it.polimi.ingsw.net.socket.events.clientevents.*;

import java.util.ArrayList;

public class InputParser {

    public Event parse(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        String[] tokens = input.split("\\s+", 70); // dividi in massimo 70 pezzi
        String command = tokens[0].toLowerCase();

        switch (command) {
            case "create":
                if (tokens.length < 2) {
                    System.out.println("Error: number of players required.");
                    return null;
                }
                int num=Integer.parseInt(tokens[1]);
                if (num<2 || num>4) {
                    System.out.println("Error: number of players has to be between 2 and 4.");
                }
                return new CreateGameEvent(num);
            case "endbuilding":
                return new EndBuildingEvent();
            case "link":
                if (tokens.length < 2 || tokens.length < 3) {
                    System.out.println("Coordinates required.");
                    return null;
                }
                int x=Integer.parseInt(tokens[1]);
                int y=Integer.parseInt(tokens[2]);
                return new LinkTileEvent(x,y);
            case "login":
                if (tokens.length < 2) {
                    System.out.println("Errore: devi specificare uno username e un colore.");
                    return null;
                }
                if (tokens.length < 3) {
                    System.out.println("Errore: devi specificare uno username e un colore.");
                    return null;
                }
                PlayerColors color=null;
                switch (tokens[2].toLowerCase()) {
                    case "blue": color = PlayerColors.BLUE; break;
                    case "red": color = PlayerColors.RED; break;
                    case "green": color = PlayerColors.GREEN; break;
                    case "yellow": color = PlayerColors.YELLOW; break;
                }
                return new LoginEvent(tokens[1].trim(),color);
            case "selecttile":
                int index=Integer.parseInt(tokens[1]);
                return new SelectTileEvent(index);
            case "choice":
                boolean choice=Boolean.parseBoolean(tokens[1]);
                return new SendBooleanEvent(choice);
            case "coordinate":
                if (tokens.length < 2 || tokens.length < 3) {
                    System.out.println("Coordinates required.");
                    return null;
                }
                int x1=Integer.parseInt(tokens[1]);
                int y1=Integer.parseInt(tokens[2]);
                return new LinkTileEvent(x1,y1);
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
            case "turnhourglass":
                return new TurnHourglassEvent();
            case "hello":
                if (tokens.length < 2) {
                    System.out.println("Errore: devi scrivere un messaggio.");
                    return null;
                }
                return new HelloEvent(tokens[1].trim(), EventType.HELLO_MESSAGE);

            default:
                System.out.println("Comando sconosciuto: " + command);
                return null;
        }
    }
}

