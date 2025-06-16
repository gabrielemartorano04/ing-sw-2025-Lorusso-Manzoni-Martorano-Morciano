package it.polimi.ingsw.view.text;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.cards.Effect;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.events.serverevents.AlienCabinCoord;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.flightevents.FlightStartEvent;
import it.polimi.ingsw.net.events.serverevents.flightevents.InvalidPositionError;
import it.polimi.ingsw.net.events.serverevents.setupevents.AvailableColorsEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.ColorAlreadyInError;
import it.polimi.ingsw.net.events.serverevents.setupevents.NicknameAlreadyInError;
import it.polimi.ingsw.view.InputReader;
import it.polimi.ingsw.view.ParserState;
import it.polimi.ingsw.view.UI;
import it.polimi.ingsw.view.ViewFlow;

import java.util.*;
import java.util.List;

public class TUI implements UI {

    private final InputReader inputReader;
    private final ViewFlow viewFlow;

    public TUI(ViewFlow viewFlow) {
        this.inputReader = new InputReader();
        this.viewFlow = viewFlow;
    }

    // Metodi omolagati tra GUI e TUI

    @Override
    public void welcomeScene(AvailableColorsEvent event){
        this.showWelcome();
        this.showChooseColor(event.getColors());
    }


    @Override
    public void nickNameAlreadyTaken(NicknameAlreadyInError event){
        this.showNicknameTaken();
        this.showChooseColor(event.getColors());
    }

    @Override
    public void colorAlreadyTaken(ColorAlreadyInError event){
        this.showError(event.getMessage());
        this.showColorTaken();
        this.showChooseColor(event.getAvailableColors());
    }

    @Override
    public void showPlayerJoined(String nickname) {
        System.out.println("Congratulation " + nickname + "! You've joined the game!.");
    }

    @Override
    public void startBuildingScene(StartBuildingEvent event){
        this.showPlayers(event.getPlayers());
        this.showBuildPhaseStart(event.getComponents());
        this.showBuildOptions();
    }

    @Override
    public void showAvailableComponent(ArrayList<Component> components) {
        System.out.println("========================================================= AVAILABLE COMPONENTS =========================================================");

        int numCols = 6;
        int colCount = 0;

        List<StringBuilder> lines = new ArrayList<>();
        for (int i = 0; i < 7; i++) lines.add(new StringBuilder()); // ogni componente ha 7 righe di output

        int i = 0;

        for (int originalIndex = 0; originalIndex < components.size(); originalIndex++) {
            Component c = components.get(originalIndex);

            if (!c.getSelected()) {
                String id = String.format("%3d", i);

                if (c.getCovered()) {
                    lines.get(0).append(id).append(") _______            ");
                    lines.get(1).append("    |       |           ");
                    lines.get(2).append("    |       |           ");
                    lines.get(3).append("    |       |           ");
                    lines.get(4).append("    |_______|           ");

                } else {
                    lines.get(0).append(id).append(") _______            ");
                    String mid1, mid2, mid3 = "";

                    switch (c) {
                        case Cannon cannon -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], cannon.getIsDouble() ? "DOUBLE" : "CANNON");
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], cannon.getIsDouble() ? "CANNON" : " ");
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], " ");
                        }
                        case Engine engine -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], engine.getIsDouble() ? "DOUBLE" : "ENGINE");
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], engine.getIsDouble() ? "ENGINE" : " ");
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], " ");
                        }
                        case Load load -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], load.getIsSpecial() ? "SPECIAL" : "LOAD");
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], load.getIsSpecial() ? "LOAD" : String.format("(Cap: %d)", load.getCapacity()));
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], load.getIsSpecial() ? String.format("(Cap: %d)", load.getCapacity()) : " ");
                        }
                        case BatteryStorage battery -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], "BATTERY");
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], "STORAGE");
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], String.format("(Cap: %d)", battery.getCapacity()));
                        }
                        case LifeSupport ls -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], "LIFE");
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], "SUPPORT");
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], String.format("(Col: %s)", ls.getColor() == AlienColor.PURPLE ? "🟣" : "🟤"));
                        }
                        case Shield sh -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], "SHIELD");
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], String.format("(Dir: %s)", sh.getDirection()));
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], " ");
                        }
                        default -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], c.getType().toUpperCase());
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], " ");
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], " ");
                        }
                    }

                    lines.get(1).append(mid1);
                    lines.get(2).append(mid2);
                    lines.get(3).append(mid3);
                    lines.get(4).append("    |_______|           ");
                }

                colCount++;
                if (colCount == numCols || originalIndex == components.size() - 1) {
                    for (StringBuilder line : lines) {
                        System.out.println(line);
                        line.setLength(0); // reset per la prossima riga
                    }
                    colCount = 0;
                }
                i++;
            }else{
                i++;
            }
        }
    }


    @Override
    public void componentLinked(ComponentLinkedEvent event){
        this.showComponentPlaced(event.getComponent(), event.getX(), event.getY());
        this.showAvailableComponent(event.getAvComponents());
    }

    @Override
    public void componentNotAvailable(ComponentNotAvailableError event){
        this.showError(event.getMessage());
        this.showAvailableComponent(this.viewFlow.getAvailableComponents());
        this.showMessage("Please select a component between 0 and " + (this.viewFlow.getAvailableComponents().size()-1)+"\n");
    }

    @Override
    public void componentSelected (ComponentSelectedEvent event){
        this.showComponentSelected(this.viewFlow.getSelectedComponent());
        this.showComponentOptions();
    }

    @Override
    public void invalidCoordinates(InvalidCoordinatesError event){
        this.showError(event.getMessage());
        this.askCoordinates();
    }

    @Override
    public void invalidPosition(InvalidPositionError event){
        this.showInvalidPlacement(event.getMessage());
        this.askCoordinates();
    }

    @Override
    public void showKeptComponents(ArrayList<Component> kept) {
        if (!kept.isEmpty()) {
            System.out.println("Components kept: ");
            for (int i=0; i<kept.size(); i++) {
                System.out.println("- " + kept.get(i).getType());
            }
        }
    }

    @Override
    public void turnHourGlassError(TurnHourglassError event){
        this.showError(event.getMessage());
    }

    @Override
    public void turnHourGlass(HourglassTurnEvent event){}

    @Override
    public void buildingComplete(BuildCompleteEvent event){
        this.showBuildComplete();

        if(viewFlow.getLevel()==2){
            this.showMessage("Type 'alien' to see where you can place aliens\n" +
                    "> ");

        }
        else{
            this.showMessage("Type 'continue' to start flight phase\n" +
                    "> ");
        }
    }

    @Override
    public void keptComponent(KeptComponentEvent event){
        this.showAvailableComponent(event.getAvComponents());
    }

    @Override
    public void componentTurned(TurnedComponentSuccessEvent event){
        this.showComponentSelected(event.getComponent());
    }


    @Override
    public void keptSelected(SuccessSelectKept event){
        this.showComponentSelected(viewFlow.getSelectedComponent());
    }

    @Override
    public void askAlienCabin(AlienCabinCoord event){
        ArrayList<int[]> coord=event.getAlienCabinCoord();

        if(event.getAlienCabinCoord().isEmpty()){
            this.showMessage("you can't place any alien. Please type 'coord' to go on\n");
        }
        else {
            StringBuilder message= new StringBuilder();
            for(int[] c:coord){
                message.append("in ( ").append(c[0]).append(" , ").append(c[1]).append(" ): ");
                if(c[1]==0){
                    message.append(" purple alien\n");
                }
                else{
                    message.append(" brown alien\n");
                }
            }
            this.showMessage("\nList of cabin available for aliens:\n" + message);
            this.showMessage("Insert coordinates to add aliens (coord <x1> <y1> <color> <x2> <y2> <color>...), otherwise just 'coord':\n");
        }
    }

    @Override
    public void dropStack(StackDropOk event){
        this.showMessage(event.getMessage());
    }


    @Override
    public void refreshAvailableComponents(){
        // serve per la GUI, non so se si riesce a integrare anche nella tui al massimo lo lasciamo vuoto
        return;
    }



    //TODO NON USATO SISTEMA
    @Override
    public void flightScene(FlightStartEvent event){
        this.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        this.showPlayerShipStatus(viewFlow.getPosition(),viewFlow.getBatteryCount(), viewFlow.getHumanCount(), viewFlow.getCannonPower(), viewFlow.getEnginePower(), viewFlow.getGoodsByColor(), viewFlow.getCredits());
        this.showTrack(viewFlow.getTrack(), viewFlow.getLevel());
        this.showFlightPhaseStart();
        this.showMessage("Please type 'state' to see your spaceship state\n");
    }


    @Override
    public void showCurrCondition(CurrentCondition event){
        System.out.println("Current condition:" + event.getCondition());
    }

    @Override
    public void cardFinished(CardFinished event){
        for(String nicknames: event.getTruckersRemoved()){
            System.out.println(nicknames + " died");
        }
        String message = "Truckers' positions order:\n";
        int i = 1;
        for (Trucker t : event.getTruckersList()) {
            message += i + ") " + t.getNickname() + "\n";
            i++;
        }
        this.showTrack(viewFlow.getTrack(), viewFlow.getLevel());
        System.out.println(message);
    }

    @Override
    public  void epidemic(EpidemicEvent event){
        System.out.println("Card effect:");
        for (Trucker t: event.getTruckers()) {
            System.out.println(t.getNickname() + " has lost ");
        }
        for(String nicknames: event.getTruckersRemoved()){
            System.out.println(nicknames + " died");
        }

        String message = "Truckers' positions order:\n";
        int i = 1;
        for (Trucker t : event.getTruckers()) {
            message += i + ") " + t.getNickname() + "\n";
            i++;
        }
        System.out.println(message);
    }

    @Override
    public void stardust(StarDustEvent event){
        System.out.println("Card effect:");
        for (Trucker t: event.getTruckers()) {
            System.out.println(t.getNickname() + " has lost " + t.getSpaceShip().countExposedConnectors() + String.format(t.getSpaceShip().countExposedConnectors() == 1 ? " day\n" : " days\n"));
        }
        for(String nicknames: event.getTruckersRemoved()){
            System.out.println(nicknames + " died");
        }

        String message = "Truckers' positions order:\n";
        int i = 1;
        for (Trucker t : event.getTruckers()) {
            message += i + ") " + t.getNickname() + "\n";
            i++;
        }
        System.out.println(message);
    }

    @Override
    public void showInputFormat(ParserState parserState) {
        switch (parserState){
            case STATE-> System.out.println("Type 'state' to see your spaceship status");
            case ACTIVATE_ENGINES -> System.out.println("Choose if you want to activate the double engines. If yes type the coordinates of the engines then 'battery' and battery storage(s) coordinates . Type 'y, <x1> <y1>, <x2> <y2>,..., battery, <xs1> <ys1> - <numofbatteries>,...' or 'n': ");
            case ACTIVATE_CANNONS -> System.out.println("Choose if you want to activate the double cannons. If yes type the coordinates of the cannons then 'battery' and battery storage(s) coordinates . Type 'y, <x1> <y1>, <x2> <y2>,..., battery, <xs1> <ys1> - <numofbatteries>,...' or 'n': ");
            case ACTIVATE_SHIELD -> System.out.println("Choose if you want to activate the shield. If yes type the coordinates of the shield and the coordinates of the battery storage. Type 'y, <x1> <y1>, <xB1> <yB1>>' or 'n': ");
            case LAND -> System.out.println("Type 'y' if you want to land, else 'n'.");
            case HUMAN_COORD -> System.out.println("Insert the coordinates of the cabins to remove the number of human indicated in the card description. Type '<x1> <y1> - <number of crew>, <x2> <y2> - <number of crew>,...' : ");
            case PLOT_GOOD -> System.out.println("Choose if you want the good, if yes type the coordinates of the load and the position in the load. Type 'y, <x> <y>, <position>' or 'n': ");
            case ROLL_DICE -> System.out.println("Type 'roll' to roll the dices when yu're ready.");
            case PLANET_DECISION->System.out.println("Choose the planet you want to fly to. Type '<index>': ");
            case DRAW_CARD ->System.out.println("Type 'draw' when you're ready to draw the next card.");
            case REMOTION_CONFIRMED -> System.out.println("Remotion confirmed. Type 'continue' when you're ready to go on.");
            case REMOVE_BATTERIES -> System.out.println("Choose from where you  want to remove batteries. Type '<x1> <y1> - <quantity>,<x2> <y2> - <quantity>...");
            case REMOVE_GOODS -> System.out.println("Choose goods you want to remove. Type '<index1>,<index2>...'.");
            case REARRANGE -> System.out.println("Choose if you want to rearrange your goods. If yes, insert coordinates.\n" +
                    "Type 'y, <x1> <y1> - <value1>, <x2> <y2> - <value2>' or 'n'");
            case COSMIC_CREDITS -> System.out.println("Do you want to gain that cosmic credits? Type '<y/n>'.");
            case WARZONE -> System.out.println("Type 'warzone' to start warzone card");
            case HUMAN_REMOVED ->  System.out.println("Remotion confirmed. Type 'continue' when you're ready to go on.");
        }
    }

    @Override
    public void showResult(RollDiceResult event){
        switch (event.getHitType()) {
            case "bigmeteor" -> {
                System.out.println("The dices result for the meteor # " + event.getCurrIndex() + " is " + event.getDiceResult() + "\n");
            }
            case "smallmeteor" -> {
                System.out.println("The dices result for the meteor # " + event.getCurrIndex() + " is " + event.getDiceResult() + "\n");
            }
            case "smallshot"-> {
                System.out.println("The dices result for the shot # " + event.getCurrIndex() + " is " + event.getDiceResult() + "\n");
            }
        }
    }

    @Override
    public void showDead(){
        System.out.println("You died");
    }


    // fine metodi omologati tra GUI e TUI

    private void showWelcome() {
        System.out.println("""
                         __        __   _                            _       \s
                         \\ \\      / /__| | ___ ___  _ __ ___   ___  | |_ ___ \s
                          \\ \\ /\\ / / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\\s
                           \\ V  V /  __/ | (_| (_) | | | | | |  __/ | || (_) |
                            \\_/\\_/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/\s        
          ____    _    _        _    __  ____   __    _____ ____  _   _  ____ _  _______ ____ \s
         / ___|  / \\  | |      / \\   \\ \\/ /\\ \\ / /   |_   _|  _ \\| | | |/ ___| |/ / ____|  _ \\\s
        | |  _  / _ \\ | |     / _ \\   \\  /  \\ V /      | | | |_) | | | | |   | ' /|  _| | |_) |
        | |_| |/ ___ \\| |___ / ___ \\  /  \\   | |       | | |  _ <| |_| | |___| . \\| |___|  _ <\s
         \\____/_/   \\_\\_____/_/   \\_\\/_/\\_\\  |_|       |_| |_| \\_\\\\___/ \\____|_|\\_\\_____|_| \\_\\ 
                                          by Cranio Creation            
        """);
    }

    private void showChooseColor(EnumSet<PlayerColors> availableColors) {
        System.out.println("Colours available: " + String.join(", ", availableColors.toString()));
        System.out.print("Insert nickname and choose a colour: ");
    }

    private void showColorTaken() {
        System.out.println("Colour already chosen, choose another one.");
    }

    private void showNicknameTaken() {
        System.out.println("Nickname already taken, choose another one.");
    }

    private void showPlayers(ArrayList<String> players) {
        System.out.println("Players in the game:");
        for(String player : players) {
            System.out.println(player);
        }
    }

    private void showBuildPhaseStart(ArrayList<Component> availableComponents) {
        System.out.println("\nStart BUILDING phase. Build your spaceship!");
        showAvailableComponent(availableComponents);
    }


    private void showComponentOptions() {
        System.out.println("Options: [Link <row> <column> (to link the component in the position x,y of your spaceship)] [Keep] [Discard]");
        System.out.print("Choose your action: ");
    }

    @Override
    public void showBuildOptions() {
        System.out.println("Type 'help' if you want to see commands.");
    }

    @Override
    public void showDiscardComponent(Component component) {
        System.out.println("Discard component " + component.getType());
    }

    public void showComponentPlaced(Component component, int x, int y) {
        System.out.println("Component " + component.getType() + " linked in [" + x + "," + y + "]");
    }

    @Override
    public void showInvalidPlacement(String reason) {
        System.out.println("Place not correct: " + reason);
    }


    @Override
    public void showTimeExpired() {
        System.out.println("Time expired, end of turn.!");
    }

    @Override
    public void showWaitForOthers() {
        System.out.println("Wait for other players...");
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showBuildComplete() {
        System.out.println("Building completed!");
    }

    @Override
    public void showFlightPhaseStart() {
        System.out.println("Start FLIGHT phase. Get ready for space events!");
    }

    @Override
    public void showCardDrawn(StartCardEvent event) {
        System.out.println("Card drawn: " + event.getCardName().toUpperCase());
        System.out.println("Card description: " + event.getCardDescription());
        if(event.getPower() != -1){
            System.out.println( "Enemy cannon power: "+event.getPower());
        }
        if(event.getMeteorites() != null) {
            String message = "Meteorites arriving:\n";
            int index = 0;
            for (int[] meteor : event.getMeteorites()) {
                message += getMeteoriteDescription(meteor, index);
                index++;
            }
            System.out.println(message);
        }
        if(event.getShoots() != null) {
            String message = "Shoots arriving:\n";
            int index = 0;
            for (int[] shoot : event.getShoots()) {
                message += getShootDescription(shoot, index);
                index++;
            }
            System.out.println(message);
        }
        if(event.getPlanets() != null) {
            String message = "Planets to visit:\n";
            int i = 0;
            for (List<Goods> list: event.getPlanets()){
                int red = 0, yellow = 0, green = 0, blue = 0;
                message += "Planets " + i + ": ";
                i++;
                for(Goods g: list){
                    if(g.getValue() == 1) blue++;
                    else if(g.getValue() == 2) green++;
                    else if(g.getValue() == 3) yellow++;
                    else if(g.getValue() == 4) red++;
                }
                message += red + " red goods, " + yellow + " yellow goods, " + green + " green goods, " + blue + " blue goods";
            }
            System.out.println(message);
        }
        if(event.getPenalties() != null && event.getConditionsOrder() != null) {
            System.out.println("writing penalties to the client");
            String message = "Conditions and penalties to apply:\n";
            for (Condition condition : event.getConditionsOrder()) {
                Effect effect = event.getPenalties().get(condition);
                message += condition.toString() + " → " + effect.toString() + "\n";
            }
            System.out.println(message);
        }
        if(event.getDays() != -1) {
            System.out.println("Days to lose: "+event.getDays());
        }
        if(event.getAstronautsLost() != -1) {
            System.out.println("Crew members to lose: "+event.getAstronautsLost());
        }
        if(event.getGoodsLost() != -1){
            System.out.println("Goods to lose: "+event.getGoodsLost() );
        }
        if(event.getCredits() != -1){
            System.out.println("Credits to gain: "+event.getCredits());
        }
        if(event.getNumHumans() != -1) {
            System.out.println( "Humans required: "+event.getNumHumans() );
        }
        if(event.getGoods() != null) {
            int red = 0, yellow = 0, green = 0, blue = 0;
            for (Goods g : event.getGoods()) {
                if(g.getValue() == 1) blue++;
                if(g.getValue() == 2) green++;
                if(g.getValue() == 3) yellow++;
                if(g.getValue() == 4) red++;
            }
            System.out.println("Goods to gain: " + red + " 🟥, " + yellow + " 🟨, " + green + " 🟩, " + blue + " 🟦");
        }
        if(event.getBank() != null) {
            System.out.println("Bank deposit: " + event.getBank().getGoodsInBank(1) + " red goods, " + event.getBank().getGoodsInBank(2) + " yellow goods, " + event.getBank().getGoodsInBank(3) + " green goods, " + event.getBank().getGoodsInBank(4) + " blue goods");
        }

    }

    private String getMeteoriteDescription(int[] meteor, int index) {
        int size = meteor[0];
        int direction = meteor[1];

        String sizeText = (size == 1) ? "big" : "small";
        String dirText;
        switch (direction) {
            case 0 -> dirText = "from the top";
            case 1 -> dirText = "from the right side";
            case 2 -> dirText = "from the bottom";
            case 3 -> dirText = "from the left side";
            default -> dirText = "from an unknown direction";
        }

        return "Meteorite " + index + " (" + sizeText + ") arriving " + dirText + "\n";
    }

    private String getShootDescription(int[] shoot, int index) {
        int size = shoot[0];
        int direction = shoot[1];

        String sizeText = (size == 1) ? "big" : "small";
        String dirText;
        switch (direction) {
            case 0 -> dirText = "from the top";
            case 1 -> dirText = "from the right side";
            case 2 -> dirText = "from the bottom";
            case 3 -> dirText = "from the left side";
            default -> dirText = "from an unknown direction";
        }

        return "Shoot " + index + " (" + sizeText + ") arriving " + dirText + "\n";
    }


    @Override
    public void showStardustCard(StarDustEvent event) {
        System.out.println("Card drawn: " + event.getCardName().toUpperCase());
        System.out.println("Card description: " + event.getCardDescription());
    }

    @Override
    public void updateCurrGood(Goods good) {
        System.out.println("Good to gain: "+ good.getColor());
    }

    @Override
    public void showEpidemicCard(EpidemicEvent event) {
        System.out.println("Card drawn: " + event.getCardName().toUpperCase());
        System.out.println("Card description: " + event.getCardDescription());
    }


    @Override
    public void showTruckerWithdrawn(String nickname) {
        System.out.println(nickname + " withdraw.");
    }

    @Override
    public void showGameEnded() {
        System.out.println("Game ended. Thanks for playing!");
    }

    @Override
    public void showScores(Map<String, Integer> finalScores) {
        if(finalScores.isEmpty()){
            System.out.println("No scores. The game isn't started");
            return;
        }
        System.out.println("Final scores:");
        finalScores.forEach((name, score) -> System.out.println("- " + name + ": " + score));
        System.out.println("Winner: " + finalScores.keySet().iterator().next() + "!");
    }

    @Override
    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }

    @Override
    public void showPlayerShipStatus(int position, int batteryCount, int humanCount,
                                     float cannonPower, int enginePower, Map<String, Integer> goods, int credits) {
        System.out.println("Position: " + position);
        System.out.println("Total batteries: " + batteryCount);
        System.out.println("Total crew members: " + humanCount);
        System.out.println("Total cannon power (only single): " + cannonPower);
        System.out.println("Total engine power (only single): " + enginePower);
        System.out.println("Cosmic credits: " + credits);

        System.out.println("Goods on the ship:");
        if (goods != null) {
            for (Map.Entry<String, Integer> entry : goods.entrySet()) {
                switch (entry.getKey()) {
                    case "RED" -> System.out.println("  - " + entry.getValue() + " 🟥");
                    case "YELLOW" -> System.out.println("  - " + entry.getValue() + " 🟨");
                    case "GREEN" -> System.out.println("  - " + entry.getValue() + " 🟩");
                    case "BLUE" -> System.out.println("  - " + entry.getValue() + " 🟦");
                    default -> System.out.println("  - " + entry.getValue() + " " + entry.getKey());
                }
            }
        }

        System.out.println("Cosmic credits: " + viewFlow.getCredits());

    }

    @Override
    public void showShipLayout(String nickname, Component[][] layout) {
        System.out.println("\nShip layout of " + nickname + ":\n");

        // Calcolo dinamico celle valide
        boolean[][] validCells = new boolean[11][12];
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                Component c = layout[i][j];
                if(c == null || !c.getType().equals("Empty Space")) {
                    validCells[i][j] = true;
                } else {
                    validCells[i][j] = false;
                }
            }
        }

        // Intestazione colonne (sopra)
        System.out.print("       ");
        for (int col = 4; col < 11; col++) {
            System.out.printf("   %2d    ", col);
        }
        System.out.println();

        for (int i = 5; i < 10; i++) {
            String rowStr = String.format("%2d", i);

            // Riga superiore
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                System.out.print(validCells[i][j] ? " _______ " : "         ");
            }
            System.out.println();

            // Riga link sopra
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else System.out.printf("|   %s   |", c == null ? " " : c.getLink()[0]);
            }
            System.out.println();

            // Riga centrale con numero riga sinistra
            System.out.print("  " + rowStr + "   ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else if (c == null) System.out.print("|       |");
                else System.out.printf("| %d %c %d |", c.getLink()[3], c.getType().toUpperCase().charAt(0), c.getLink()[1]);
            }
            System.out.println("   " + rowStr);

            // Riga link sotto
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else System.out.printf("|   %s   |", c == null ? " " : c.getLink()[2]);
            }
            System.out.println();

            // Riga inferiore
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                System.out.print(validCells[i][j] ? "|_______|" : "         ");
            }
            System.out.println();
        }

        // Intestazione colonne (sotto)
        System.out.print("       ");
        for (int col = 4; col < 11; col++) {
            System.out.printf("   %2d    ", col);
        }
        System.out.println();

        // Dettagli componenti
        System.out.println("\nComponents details:");
        for (int i = 5; i < 10; i++) {
            for (int j = 4; j < 11; j++) {
                if (!validCells[i][j]) continue;
                Component c = layout[i][j];
                if (c == null) continue;

                StringBuilder details = new StringBuilder();
                switch (c) {
                    case Engine engine -> {
                        if (engine.getIsDouble())
                            details.append("[").append(i).append(",").append(j).append("] DOUBLE ENGINE");
                        else
                            details.append("[").append(i).append(",").append(j).append("] ENGINE");
                    }
                    case Cannon cannon -> {
                        if (cannon.getIsDouble())
                            details.append("[").append(i).append(",").append(j).append("] DOUBLE CANNON (Direction: ").append(cannon.getOrientation()).append(")");
                        else
                            details.append("[").append(i).append(",").append(j).append("] CANNON (Direction: ").append(cannon.getOrientation()).append(")");
                    }
                    case Load load -> {
                        if (load.getIsSpecial()) {
                            details.append("[").append(i).append(",").append(j).append("] SPECIAL LOAD (Capacity: ").append(load.getCapacity()).append(")");
                            if (!load.isEmpty()) {
                                details.append(" contains: ");
                                for (Goods g : load.getGoods()) {
                                    details.append(g.getColor()).append(" ");
                                }
                            } else {
                                details.append(" is empty");
                            }
                        } else {
                            details.append("[").append(i).append(",").append(j).append("] LOAD (Capacity: ").append(load.getCapacity()).append(")");
                            if (!load.isEmpty()) {
                                details.append(" contains: ");
                                for (Goods g : load.getGoods()) {
                                    details.append(g.getColor()).append(" ");
                                }
                            } else {
                                details.append(" is empty");
                            }
                        }
                    }
                    case Shield shield -> {
                        details.append("[").append(i).append(",").append(j).append("] SHIELD (direction: ").append(shield.getDirection()).append(")");
                    }
                    case BatteryStorage battery -> {
                        details.append("[").append(i).append(",").append(j).append("] BATTERY STORAGE (Capacity: ").append(battery.getCapacity()).append(")");
                        if(battery.getQuantity() != 0) {
                            details.append(" contains ").append(battery.getQuantity()).append(battery.getQuantity() == 1 ? " battery" : " batteries");
                        } else {
                            details.append(" is empty");
                        }
                    }
                    case Cabin cabin -> {
                        details.append("[").append(i).append(",").append(j).append("] CABIN");
                        if(cabin.getIsMain()){
                            System.out.println(cabin.getColor());
                        }
                        if(cabin.getQuantity() != 0){
                            if (cabin.getAlien() != null) {
                                details.append(cabin.getQuantity()).append(" contains 1 " + cabin.getAlien().getColor() + " alien");
                            } else {
                                details.append(" contains ").append(cabin.getQuantity()).append(cabin.getQuantity() == 1 ? " human" : " humans");
                            }
                        } else {
                            details.append(" is empty");
                        }

                    }
                    case LifeSupport lifeSupport -> {
                        details.append("[").append(i).append(",").append(j).append("] ").append("LIFE SUPPORT is ").append(lifeSupport.getColor());
                    }
                    default -> {
                        details.append("[").append(i).append(",").append(j).append("] ").append(c.getType().toUpperCase());
                    }
                }
                System.out.println(details);
            }
        }

    }


    @Override
    public void showotherShipLayout(String nickname, Component[][] layout) {
        System.out.println("\nShip layout of " + nickname + ":\n");

        // Calcolo dinamico celle valide
        boolean[][] validCells = new boolean[11][12];
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                Component c = layout[i][j];
                if(c == null || !c.getType().equals("Empty Space")) {
                    validCells[i][j] = true;
                } else {
                    validCells[i][j] = false;
                }
            }
        }

        // Intestazione colonne (sopra)
        System.out.print("       ");
        for (int col = 4; col < 11; col++) {
            System.out.printf("   %2d    ", col);
        }
        System.out.println();

        for (int i = 5; i < 10; i++) {
            String rowStr = String.format("%2d", i);

            // Riga superiore
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                System.out.print(validCells[i][j] ? " _______ " : "         ");
            }
            System.out.println();

            // Riga link sopra
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else System.out.printf("|   %s   |", c == null ? " " : c.getLink()[0]);
            }
            System.out.println();

            // Riga centrale con numero riga sinistra
            System.out.print("  " + rowStr + "   ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else if (c == null) System.out.print("|       |");
                else System.out.printf("| %d %c %d |", c.getLink()[3], c.getType().toUpperCase().charAt(0), c.getLink()[1]);
            }
            System.out.println("   " + rowStr);

            // Riga link sotto
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else System.out.printf("|   %s   |", c == null ? " " : c.getLink()[2]);
            }
            System.out.println();

            // Riga inferiore
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                System.out.print(validCells[i][j] ? "|_______|" : "         ");
            }
            System.out.println();
        }

        // Intestazione colonne (sotto)
        System.out.print("       ");
        for (int col = 4; col < 11; col++) {
            System.out.printf("   %2d    ", col);
        }
        System.out.println();

        // Dettagli componenti
        System.out.println("\nComponents details:");
        for (int i = 5; i < 10; i++) {
            for (int j = 4; j < 11; j++) {
                if (!validCells[i][j]) continue;
                Component c = layout[i][j];
                if (c == null) continue;

                StringBuilder details = new StringBuilder();
                switch (c) {
                    case Engine engine -> {
                        if (engine.getIsDouble())
                            details.append("[").append(i).append(",").append(j).append("] DOUBLE ENGINE");
                        else
                            details.append("[").append(i).append(",").append(j).append("] ENGINE");
                    }
                    case Cannon cannon -> {
                        if (cannon.getIsDouble())
                            details.append("[").append(i).append(",").append(j).append("] DOUBLE CANNON (Direction: ").append(cannon.getOrientation()).append(")");
                        else
                            details.append("[").append(i).append(",").append(j).append("] CANNON (Direction: ").append(cannon.getOrientation()).append(")");
                    }
                    case Load load -> {
                        if (load.getIsSpecial()) {
                            details.append("[").append(i).append(",").append(j).append("] SPECIAL LOAD (Capacity: ").append(load.getCapacity()).append(")");
                            if (!load.isEmpty()) {
                                details.append(" contains: ");
                                for (Goods g : load.getGoods()) {
                                    details.append(g.getColor()).append(" ");
                                }
                            } else {
                                details.append(" is empty");
                            }
                        } else {
                            details.append("[").append(i).append(",").append(j).append("] LOAD (Capacity: ").append(load.getCapacity()).append(")");
                            if (!load.isEmpty()) {
                                details.append(" contains: ");
                                for (Goods g : load.getGoods()) {
                                    details.append(g.getColor()).append(" ");
                                }
                            } else {
                                details.append(" is empty");
                            }
                        }
                    }
                    case Shield shield -> {
                        details.append("[").append(i).append(",").append(j).append("] SHIELD (direction: ").append(shield.getDirection()).append(")");
                    }
                    case BatteryStorage battery -> {
                        details.append("[").append(i).append(",").append(j).append("] BATTERY STORAGE (Capacity: ").append(battery.getCapacity()).append(")");
                        if(battery.getQuantity() != 0) {
                            details.append(" contains ").append(battery.getQuantity()).append(battery.getQuantity() == 1 ? " battery" : " batteries");
                        } else {
                            details.append(" is empty");
                        }
                    }
                    case Cabin cabin -> {
                        details.append("[").append(i).append(",").append(j).append("] CABIN");
                        if(cabin.getIsMain()){
                            System.out.println(cabin.getColor());
                        }
                        if(cabin.getQuantity() != 0){
                            if (cabin.getAlien() != null) {
                                details.append(cabin.getQuantity()).append(" contains 1 " + cabin.getAlien().getColor() + " alien");
                            } else {
                                details.append(" contains ").append(cabin.getQuantity()).append(cabin.getQuantity() == 1 ? " human" : " humans");
                            }
                        } else {
                            details.append(" is empty");
                        }

                    }
                    case LifeSupport lifeSupport -> {
                        details.append("[").append(i).append(",").append(j).append("] ").append("LIFE SUPPORT is ").append(lifeSupport.getColor());
                    }
                    default -> {
                        details.append("[").append(i).append(",").append(j).append("] ").append(c.getType().toUpperCase());
                    }
                }
                System.out.println(details);
            }
        }
    }


    private String abbreviate(String name) {
        // restituisce le prime 3 lettere in maiuscolo, o meno se più corto
        return name.length() >= 3 ? name.substring(0, 3).toUpperCase() : name.toUpperCase();
    }


    public void askCoordinates() {
        System.out.print("Insert coordinates (x y): ");
    }



    @Override
    public void showGoodsGained(Goods good) {
        System.out.println("Goods gained:");
        System.out.println("- " + good.getColor() + " (" + good.getValue() + ")");
    }

    @Override
    public void updatePlanetsOccupied(PlanetDecision event) {
        int index=0;
        for(Trucker planets: event.getPlanetsOccupied()){
            if(planets==null){
                System.out.println("Planets "+index+" is free");
            }
            else{
                System.out.println("Planets "+index+" is occupied by: "+planets.getNickname());
            }
            index++;
        }
    }

    @Override
    public void showTrack(Map<String, Integer> truckers, int level) {
        int index;
        String[] symbols = {"1️⃣", "2️⃣", "3️⃣", "4️⃣"};

        // Ordina i trucker per posizione crescente
        List<Map.Entry<String, Integer>> sortedTruckers = new ArrayList<>(truckers.entrySet());
        sortedTruckers.sort(Comparator.comparingInt(Map.Entry::getValue));

        Map<Integer, String> board = new HashMap<>();

        if (level == 2) {
            for (int i = 0; i <= 6; i++) board.put(i, "▶️");
            for (int i = 7; i <= 11; i++) board.put(i, "🔽");
            for (int i = 12; i <= 18; i++) board.put(i, "◀️");
            for (int i = 19; i <= 23; i++) board.put(i, "🔼");

            index = 0;
            for (Map.Entry<String, Integer> entry : sortedTruckers) {
                int pos = entry.getValue();
                board.put(pos, symbols[index]);
                index++;
            }

            System.out.println();
            System.out.printf("         %s %s %s %s %s %s %s\n", board.get(0), board.get(1), board.get(2), board.get(3), board.get(4), board.get(5), board.get(6));
            System.out.printf("      %s                       %s\n", board.get(23), board.get(7));
            System.out.printf("      %s                       %s\n", board.get(22), board.get(8));
            System.out.printf("      %s                       %s\n", board.get(21), board.get(9));
            System.out.printf("      %s                       %s\n", board.get(20), board.get(10));
            System.out.printf("      %s                       %s\n", board.get(19), board.get(11));
            System.out.printf("         %s %s %s %s %s %s %s\n", board.get(18), board.get(17), board.get(16), board.get(15), board.get(14), board.get(13), board.get(12));
            System.out.println();
        } else if (level == 0) {
            for (int i = 0; i <= 4; i++) board.put(i, "▶️");
            for (int i = 5; i <= 8; i++) board.put(i, "🔽");
            for (int i = 9; i <= 13; i++) board.put(i, "◀️");
            for (int i = 14; i <= 17; i++) board.put(i, "🔼");

            index = 0;
            for (Map.Entry<String, Integer> entry : sortedTruckers) {
                int pos = entry.getValue();
                board.put(pos, symbols[index]);
                index++;
            }

            System.out.println();
            System.out.printf("         %s %s %s %s %s\n", board.get(0), board.get(1), board.get(2), board.get(3), board.get(4));
            System.out.printf("      %s                       %s\n", board.get(17), board.get(5));
            System.out.printf("      %s                       %s\n", board.get(16), board.get(6));
            System.out.printf("      %s                       %s\n", board.get(15), board.get(7));
            System.out.printf("      %s                       %s\n", board.get(14), board.get(8));
            System.out.printf("         %s %s %s %s %s\n", board.get(13), board.get(12), board.get(11), board.get(10), board.get(9));
            System.out.println();
        }

        // Legenda con i simboli e i nomi, ordinata
        index = 0;
        for (Map.Entry<String, Integer> entry : sortedTruckers) {
            System.out.printf("%s %s\n", symbols[index], entry.getKey());
            index++;
        }
    }

    @Override
    public void showInsertNumPlayers() {
        System.out.println("\nInsert the number of players and the game level:");
    }

    @Override
    public void showStack(Stack stack) {
        System.out.println("\nCards in the stack:");

        if (stack == null || stack.getStack().isEmpty()) {
            System.out.println("[ Empty stack ]");
            return;
        }

        for (int i = 0; i < stack.getStack().size(); i++) {
            Card card = stack.getStack().get(i);
            System.out.printf(" [%02d] %-15s (Level %d)%n",
                    i, card.getName(), card.getLevel());
        }
    }

    public void showComponentSelected(Component c) {
        StringBuilder info = new StringBuilder();

        switch(c) {
            case Engine engine -> {
                if(engine.getIsDouble()) {
                    info.append("DOUBLE ");
                }
                info.append("ENGINE");
            }
            case Cannon cannon -> {
                if(cannon.getIsDouble()) {
                    info.append("DOUBLE ");
                }
                info.append("CANNON (Dir: " + cannon.getOrientation() + ")");
            }
            case Load load -> {
                if(load.getIsSpecial()) {
                    info.append("SPECIAL ");
                }
                info.append("LOAD (Capacity: " + load.getCapacity() + ")");
            }
            case Shield shield -> {
                info.append("SHIELD (Direction: " + shield.getDirection() + ")");
            }
            case BatteryStorage batteryStorage -> {
                info.append("BATTERY STORAGE (Capacity: " + batteryStorage.getCapacity() + ")");
            }
            case LifeSupport lifeSupport -> {
                info.append("LIFE SUPPORT (Colour: " + lifeSupport.getColor() + ")");
            }
            default -> {
                info.append(c.getType().toUpperCase());
            }
        }
        System.out.println(info);
        System.out.print(" _______ \n");
        System.out.printf("|   %s   |\n", c.getLink()[0]);
        System.out.printf("| %d %c %d |\n", c.getLink()[3], c.getType().charAt(0), c.getLink()[1]);
        System.out.printf("|   %s   |\n", c.getLink()[2]);
        System.out.print("|_______|\n");

    }

    @Override
    public void welcomeOldScene(ArrayList<PlayerColors> colors, ArrayList<String> nicknames) {
        System.out.println("vecchi players:\n");
        System.out.println(colors.getFirst());
        System.out.println(nicknames.getFirst());
        for(int i = 0; i < colors.size(); i++) {
            System.out.println("nickname: " + nicknames.get(i) + " color: " + colors.get(i));
        }
        System.out.println("choose a player");
    }

    public void showContinue(){
        System.out.println("Type Continue to proceed");
    }

    public void setGoodImage(Goods good) {

    }
}