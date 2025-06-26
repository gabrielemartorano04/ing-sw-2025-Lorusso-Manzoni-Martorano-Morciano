package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.events.clientevents.AlienCoordinatesRequest;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.clientevents.SendAlienCoord;
import it.polimi.ingsw.net.events.clientevents.ShowOtherShipEvent;
import it.polimi.ingsw.net.events.clientevents.buildingevents.*;
import it.polimi.ingsw.net.events.clientevents.setupevents.ChooseToResume;
import it.polimi.ingsw.net.events.clientevents.setupevents.CreateGameEvent;
import it.polimi.ingsw.net.events.clientevents.setupevents.LoginEvent;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Class that manage the inputs from the client to start the right action
 */
public class InputParser {

    /**
     * Game level
     */
    private int level;

    /**
     * User interface
     */
    private final UI ui;

    /**
     * Class constructor
     * @param ui user interface
     */
    public InputParser(UI ui) {
        this.ui = ui;
    }

    /**
     * Set the level of the game
     * @param level of the game
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Manages the connection of the first player for the creation of the game
     * @param input from the client
     * @return the client event that manages that input
     */
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
        level = Integer.parseInt(tokens[1]);
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

    /**
     * Manages the login of the other players for the creation of the game
     * @param input from the client
     * @return the client event that manages that input
     */
    public ClientEvent parseLogin(String input){
        if (input == null || input.isEmpty()) {
            return null;
        }

        String[] tokens = input.split("\\s+", 2);
        if (tokens.length < 2) {
            ui.showError("Error: username and color are required and they have to be divided by space.");
            return null;
        }
        if (tokens.length > 2) {
            ui.showError("Error: type only username and color and they have to be divided by space.");
            return null;
        }
        PlayerColors color = switch (tokens[1].toLowerCase()) {
            case "blue" -> PlayerColors.BLUE;
            case "red" -> PlayerColors.RED;
            case "green" -> PlayerColors.GREEN;
            case "yellow" -> PlayerColors.YELLOW;
            default -> null;
        };
        if(color!=null) {
            return new LoginEvent(tokens[0].trim(), color);
        }
        ui.showError("Error: illegal color, please write an available color");
        return null;
    }

    /**
     * Manages the fill of the ship
     * @param input from the client
     * @return the client event that manages that input
     */
    public ClientEvent parseFillTheShip(String input){
        if (input == null || input.isEmpty()) {
            return null;
        }
        String[] tokens = input.split("\\s+");
        String command = tokens[0].toLowerCase();
        switch (command){
            case "rules":
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("it/polimi/ingsw/view/galaxy-trucker-rules-en.pdf")) {
                    if (is == null) {
                        System.out.println("File not found");
                        return null;
                    }

                    File tempPdf = File.createTempFile("galaxy-trucker-rules", ".pdf");
                    tempPdf.deleteOnExit();

                    try (FileOutputStream os = new FileOutputStream(tempPdf)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }

                    Desktop.getDesktop().open(tempPdf);

                } catch (IOException e) {
                    System.out.println("Error in file opening: " + e.getMessage());
                }
                return null;
            case "alien":
                if(tokens.length != 1){
                    System.out.println("Error: illegal command. Type 'alien' if you want to see where you can place aliens.");
                    return null;
                }
                return new AlienCoordinatesRequest();
            case "coord":
                if(tokens.length%3 ==1){
                    ArrayList<int[]> coord=new ArrayList<>();
                    for(int i=1;i<tokens.length-2;i+=3){
                        if(tokens[i]!=null && tokens[i+1]!=null){
                            if(tokens[i+2].trim().equalsIgnoreCase("purple")) {
                                coord.add(new int[]{Integer.parseInt(tokens[i]), Integer.parseInt(tokens[i + 1]),0});
                            }
                            else if(tokens[i+2].trim().equalsIgnoreCase("brown")){
                                coord.add(new int[]{Integer.parseInt(tokens[i]), Integer.parseInt(tokens[i + 1]),1});
                            }
                        }
                    }
                    return new SendAlienCoord(coord);
                }else {
                    System.out.println("Error: illegal command. Type 'coord <x1> <y1> <color> <x2> <y2> <color>...' .");
                    return null;
                }
            case "continue":
                if(tokens.length != 1){
                    System.out.println("Error: illegal command. Type 'continue' .");
                    return null;
                }
                return new SendAlienCoord(null);
            default:
                System.out.println("Error: unknown command: " + command);
                return null;
        }

    }

    /**
     * Manages the resumption of an old game
     * @param input from the client
     * @return the client event that manages that input
     */
    public ClientEvent parseOld(String input){
        if(input == null || input.isEmpty()) {
            return null;
        }
        input = input.trim();
        String[] tokens = input.split("\\s+");
        if (tokens.length != 1) {
            System.out.println("please write y or n");
            return null;
        }
        if (tokens[0].equalsIgnoreCase("y") && tokens[0].equalsIgnoreCase( "n")){
            System.out.println("please write y or n");
            return null;
        }
        if(tokens[0].equalsIgnoreCase("y")) {
            return new ChooseToResume(true);
        } else if (tokens[0].equalsIgnoreCase("n")) {
            return new ChooseToResume(false);
        }else return null;
    }

    /**
     * Manages the building of the spaceship
     * @param input from the client
     * @param component tha has to be linked
     * @param index of the component in the list of all available components or kept components
     * @param kept list of kept components
     * @param isFromKept specify if the component is from the list of kept or all available
     * @param stackSelected index of the selected stack to show
     * @param firstLinked specify if the component connected is the first
     * @return the client event that manages that input
     */
    public ClientEvent parseBuilding(String input, Component component, int index, ArrayList<Component> kept, boolean isFromKept, int stackSelected, boolean firstLinked,ArrayList<String> players) {
        if (input == null || input.isEmpty()) return null;

        String[] tokens = input.split("\\s+", 3);
        String command = tokens[0].toLowerCase();

        if (level == 2) {
            if (stackSelected == -1) {
                if (component != null) {
                    return isFromKept
                            ? handleFromKeptComponentCommand(command, tokens, component, index,players)
                            : handleSelectedFromListComponentCommand(command, tokens, component, index,players);
                } else {
                    return firstLinked
                            ? handleNoComponentSelectedCommand(command, tokens, kept,players)
                            : handleNoComponentSelectedCommandFirst(command, tokens, kept,players);
                }
            } else {
                if(command.equals("help") && tokens.length == 1){
                    String message = "Commands:\n";
                    message+="- dropstack: to drop the stack\n";
                    System.out.println(message);
                    System.out.println("Players in the game\n");
                    for(String nickname: players){
                        System.out.println(nickname);
                    }
                    return null;
                }

                if(command.equals("dropstack") && tokens.length == 1) {
                    return new StackDroppedEvent(stackSelected);
                }else {
                    System.out.println("Error: illegal command. Type 'dropstack' if you want to drop your stack and proceed");
                    return null;
                }
            }
        } else {
            if (component != null) {
                return isFromKept
                        ? handleTrialModeCommandsFromKept(command, tokens, component, index,players)
                        : handleTrialModeCommandsNotFromKept(command, tokens, component, index,players);
            } else {
                return handleTrialModeNoComponentCommands(command, tokens, kept,players);
            }
        }
    }

    /**
     * Manages all the commands that don't require a component to be selected
     * @param command selected from the client
     * @param tokens the string inserted from the client
     * @param kept list of selected components
     * @param players list of players nicknames
     * @return the client event that manages that input
     */
    private ClientEvent handleNoComponentSelectedCommandFirst(String command, String[] tokens, ArrayList<Component> kept,ArrayList<String> players) {
        switch (command) {
            case "help":
                String message = "Commands:\n";
                message+="- rules : to open pdf file with game rules\n";
                message+="- ship <nickname> : to see the ship of another player\n";
                message+="- hourglass : to turn the hourglass\n";
                message+="- select <index> : to select a component\n";
                message+="- selectkept <index> : to select one of your kept components\n";
                System.out.println(message);
                System.out.println("Players in the game\n");
                for(String nickname: players){
                    System.out.println(nickname);
                }
                return null;
            case "rules":
                openRulesPdf();
                return null;
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

                if (tokens.length != 2) {
                    System.out.println("Error: illegal command. Type 'selectkept <index>'.");
                    return null;
                }
                int ind = Integer.parseInt(tokens[1].trim());
                if (ind < 0 || ind > kept.size() - 1) {
                    System.out.println("Please type a valid index. Type 'selectkept <index>'.");
                    return null;
                }
                return new SelectKeptTileEvent(ind);
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


    private ClientEvent handleFromKeptComponentCommand(String command, String[] tokens, Component component, int index,ArrayList<String> players) {
        switch (command) {
            case "help":
                String message = "Commands:\n";
                message+="- rules : to open pdf file with game rules\n";
                message+="- turn : to turn selected component\n";
                message+="- link <x> <y> : to link selected component in position (x,y)\n";
                message+="- keep : to keep the selected component\n";
                message+="- ship <nickname> : to see the ship of another player\n";
                System.out.println(message);
                System.out.println("Players in the game\n");
                for(String nickname: players){
                    System.out.println(nickname);
                }
                return null;
            case "rules":
                openRulesPdf();
                return null;
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

    private ClientEvent handleSelectedFromListComponentCommand(String command, String[] tokens, Component component, int index,ArrayList<String> players) {
        switch (command) {
            case "help":
                String message = "Commands:\n";
                message+="- rules : to open pdf file with game rules\n";
                message+="- turn : to turn selected component\n";
                message+="- link <x> <y> : to link selected component in position (x,y)\n";
                message+="- keep : to keep the selected component\n";
                message+="- ship <nickname> : to see the ship of another player\n";
                message+="- discard : to discard the selected component\n";
                System.out.println(message);
                System.out.println("Players in the game\n");
                for(String nickname: players){
                    System.out.println(nickname);
                }
                return null;
            case "rules":
                openRulesPdf();
                return null;
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
                if((Integer.parseInt(tokens[1])<5 || Integer.parseInt(tokens[1])>9) && (Integer.parseInt(tokens[2])<4 || Integer.parseInt(tokens[2])>10)){
                    System.out.println("Error: illegal command. Please set a valiid index\n");
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
    }

    private ClientEvent handleNoComponentSelectedCommand(String command, String[] tokens, ArrayList<Component> kept,ArrayList<String> players) {
        switch (command) {
            case "help":
                String message = "Commands:\n";
                message+="- rules : to open pdf file with game rules\n";
                message+="- ship <nickname> : to see the ship of another player\n";
                message+="- hourglass : to turn the hourglass\n";
                message+="- select <index> : to select a component\n";
                message+="- selectkept <index> : to select one of your kept components\n";
                message+="- stack <index> : to see a stack\n";
                System.out.println(message);
                System.out.println("Players in the game\n");
                for(String nickname: players){
                    System.out.println(nickname);
                }
                return null;
            case "rules":
                openRulesPdf();
                return null;
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

                if (tokens.length != 2) {
                    System.out.println("Error: illegal command. Type 'selectkept <index>'.");
                    return null;
                }
                int ind = Integer.parseInt(tokens[1].trim());
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

    private void openRulesPdf() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("it/polimi/ingsw/view/galaxy-trucker-rules-en.pdf")) {
            if (is == null) {
                System.out.println("File not found");
                return;
            }

            File tempPdf = File.createTempFile("galaxy-trucker-rules", ".pdf");
            tempPdf.deleteOnExit();

            try (FileOutputStream os = new FileOutputStream(tempPdf)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

            Desktop.getDesktop().open(tempPdf);

        } catch (IOException e) {
            System.out.println("Error in file opening: " + e.getMessage());
        }
    }

    private ClientEvent handleTrialModeCommandsFromKept(String command, String[] tokens, Component component, int index,ArrayList<String> players) {
        switch (command) {
            case "help":
                String message = "Commands:\n";
                message+="- rules : to open pdf file with game rules\n";
                message+="- turn : to turn selected component\n";
                message+="- link <x> <y> : to link selected component in position (x,y)\n";
                message+="- keep : to keep the selected component\n";
                message+="- ship <nickname> : to see the ship of another player\n";
                System.out.println(message);
                System.out.println("Players in the game\n");
                for(String nickname: players){
                    System.out.println(nickname);
                }
                return null;
            case "rules":
                openRulesPdf();
                return null;
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

    private ClientEvent handleTrialModeCommandsNotFromKept(String command, String[] tokens, Component component, int index,ArrayList<String> players) {
        switch (command) {
            case "help":
                String message = "Commands:\n";
                message+="- rules : to open pdf file with game rules\n";
                message+="- turn : to turn selected component\n";
                message+="- link <x> <y> : to link selected component in position (x,y)\n";
                message+="- keep : to keep the selected component\n";
                message+="- ship <nickname> : to see the ship of another player\n";
                message+="- discard : to discard the selected component\n";
                System.out.println(message);
                System.out.println("Players in the game\n");
                for(String nickname: players){
                    System.out.println(nickname);
                }
                return null;
            case "rules":
                openRulesPdf();
                return null;
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
                if((Integer.parseInt(tokens[1])<5 || Integer.parseInt(tokens[1])>9) && (Integer.parseInt(tokens[2])<4 || Integer.parseInt(tokens[2])>10)){
                    System.out.println("Error: illegal command. Please set a valiid index\n");
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
    }

    private ClientEvent handleTrialModeNoComponentCommands(String command, String[] tokens, ArrayList<Component> kept,ArrayList<String> players) {
        switch (command) {
            case "help":
                String message = "Commands:\n";
                message+="- rules : to open pdf file with game rules\n";
                message+="- ship <nickname> : to see the ship of another player\n";
                message+="- select <index> : to select a component\n";
                message+="- selectkept <index> : to select one of your kept components\n";
                System.out.println(message);
                System.out.println("Players in the game\n");
                for(String nickname: players){
                    System.out.println(nickname);
                }
                return null;
            case "rules":
                openRulesPdf();
                return null;
            case "ship":
                if (tokens.length != 2) {
                    System.out.println("Error: illegal command. Type 'ship <nickname>'.");
                    return null;
                }
                return new ShowOtherShipEvent(tokens[1].trim());
            case "select":
                if (tokens.length != 2) {
                    System.out.println("Error: illegal command. Type 'select <index>'.");
                    return null;
                }
                return new SelectTileEvent(Integer.parseInt(tokens[1].trim()));

            case "selectkept":
                if (tokens.length != 2) {
                    System.out.println("Error: illegal command. Type 'selectkept <index>'.");
                    return null;
                }
                int ind = Integer.parseInt(tokens[1].trim());

                if (ind < 0 || ind > kept.size() - 1) {
                    System.out.println("Please type a valid index. Type 'selectkept <index>'.");
                    return null;
                }
                return new SelectKeptTileEvent(ind);
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

    // fine metodi per parse Building


}

