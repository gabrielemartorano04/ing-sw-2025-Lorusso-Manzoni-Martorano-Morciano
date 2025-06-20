package it.polimi.ingsw.view;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.clientevents.cardevents.*;


import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

        if (input.trim().equalsIgnoreCase("rules")) {
            openRulesPdf();
            return null;
        }

        //TODO: aggiungere withdraw e ship

        return switch (parserState) {
            case HUMAN_COORD -> parseHumanCoord(input, nickname);
            case PLOT_GOOD -> parsePlotGood(input, nickname);
            case ROLL_DICE -> parseRollDice(input);
            case ACTIVATE_SHIELD -> parseActivateShield(input, nickname);
            case ACTIVATE_CANNONS -> parseActivateCannons(input, nickname);
            case ACTIVATE_ENGINES -> parseActivateEngines(input, nickname);
            case DRAW_CARD -> parseDrawCard(input, nickname);
            case PLANET_DECISION -> parsePlanetDecision(input, nickname);
            case DO_NOTHING -> parseDoNothing();
            case LAND -> parseLand(input);
            case WARZONE -> parseWarzone(input);
            case COSMIC_CREDITS -> parseCosmicCredits(input);
            case REARRANGE -> parseRearrange(input, nickname);
            case REMOTION_CONFIRMED -> parseRemotionConfirmed(input);
            case CURRENT_CONDITION -> parseCurrentCondition(input);
            case REMOVE_BATTERIES -> parseRemoveBatteries(input, nickname);
            case REMOVE_GOODS -> parseRemoveGoods(input, nickname);
            case DEAD -> {
                setParserState(ParserState.DO_NOTHING);
                yield new DeadTrucker();
            }
            default -> {
                System.out.println("Error: invalid command. Stato non gestito");
                yield null;
            }
        };
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

    private ClientEvent parseHumanCoord(String input, String nickname) {
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
    }

    private ClientEvent parsePlotGood(String input, String nickname) {
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
    }

    private ClientEvent parseRollDice(String input) {
        String[] rollDice=input.split("\\s+");
        if(rollDice.length!=1 || !rollDice[0].trim().equals("roll")){
            System.out.println("Error: invalid command");
            return null;
        }
        return new RollDiceEvent();
    }

    private ClientEvent parseActivateShield(String input, String nickname) {
        String[] activateShield = input.split("\\s*,\\s*");

        if (activateShield[0].trim().equalsIgnoreCase("n")) {
            return new ActivateShieldEvent(false, null, null, nickname);
        } else if (activateShield[0].trim().equalsIgnoreCase("y")) {

            if (activateShield.length < 3) {
                System.out.println("Error: missing coordinates for shield or battery storage");
                return null;
            }

            try {
                String[] coordShield = activateShield[1].trim().split("\\s+");
                if (coordShield.length != 2) {
                    System.out.println("Error: invalid shield coordinates");
                    return null;
                }
                int x = Integer.parseInt(coordShield[0].trim());
                int y = Integer.parseInt(coordShield[1].trim());

                String[] coordBS = activateShield[2].trim().split("\\s+");
                if (coordBS.length != 2) {
                    System.out.println("Error: invalid battery storage coordinates");
                    return null;
                }
                int xB = Integer.parseInt(coordBS[0].trim());
                int yB = Integer.parseInt(coordBS[1].trim());

                return new ActivateShieldEvent(true, new int[]{x, y}, new int[]{xB, yB}, nickname);

            } catch (NumberFormatException e) {
                System.out.println("Error: coordinates must be numeric");
                return null;
            }
        }

        System.out.println("Error: invalid command (must start with 'y' or 'n')");
        return null;
    }

    private ClientEvent parseActivateCannons(String input, String nickname) {
        String[] activateCannons = input.split("\\s*,\\s*");
        ArrayList<int[]> coordCannons = new ArrayList<>();
        ArrayList<int[]> coordBS = new ArrayList<>();
        int index = -1;

        if (activateCannons[0].trim().equalsIgnoreCase("n")) {
            return new ActivateCannonsEvent(false, null, null, nickname);
        } else if (activateCannons[0].trim().equalsIgnoreCase("y")) {

            for (int i = 1; i < activateCannons.length; i++) {
                if (activateCannons[i].trim().equalsIgnoreCase("battery")) {
                    index = i + 1;
                    break;
                }
                String[] coord = activateCannons[i].trim().split("\\s+");
                if (coord.length != 2) {
                    System.out.println("Error: invalid cannon coordinates");
                    return null;
                }
                try {
                    int x = Integer.parseInt(coord[0].trim());
                    int y = Integer.parseInt(coord[1].trim());
                    coordCannons.add(new int[]{x, y});
                } catch (NumberFormatException e) {
                    System.out.println("Error: coordinates must be numbers");
                    return null;
                }
            }

            if (index == -1 || index >= activateCannons.length) {
                System.out.println("Error: missing or misplaced 'battery' keyword");
                return null;
            }

            for (int i = index; i < activateCannons.length; i++) {
                String[] part = activateCannons[i].trim().split("\\s*-\\s*");
                if (part.length != 2) {
                    System.out.println("Error: invalid battery format");
                    return null;
                }
                String[] coord = part[0].trim().split("\\s+");
                if (coord.length != 2) {
                    System.out.println("Error: invalid battery coordinates");
                    return null;
                }
                try {
                    int x = Integer.parseInt(coord[0].trim());
                    int y = Integer.parseInt(coord[1].trim());
                    int cont = Integer.parseInt(part[1].trim());
                    coordBS.add(new int[]{x, y, cont});
                } catch (NumberFormatException e) {
                    System.out.println("Error: battery values must be numbers");
                    return null;
                }
            }

            return new ActivateCannonsEvent(true, coordCannons, coordBS, nickname);
        }

        System.out.println("Error: invalid command (missing y/n)");
        return null;
    }

    private ClientEvent parseActivateEngines(String input, String nickname) {
        String[] activateEngines = input.split("\\s*,\\s*");
        ArrayList<int[]> coordEngines = new ArrayList<>();
        ArrayList<int[]> coordBattery = new ArrayList<>();
        int indexE = -1;

        if (activateEngines[0].trim().equalsIgnoreCase("n")) {
            return new ActivateEnginesEvent(false, null, null, nickname);
        } else if (activateEngines[0].trim().equalsIgnoreCase("y")) {

            for (int i = 1; i < activateEngines.length; i++) {
                if (activateEngines[i].trim().equalsIgnoreCase("battery")) {
                    indexE = i + 1;
                    break;
                }
                String[] coord = activateEngines[i].trim().split("\\s+");
                if (coord.length != 2) {
                    System.out.println("Error: invalid engine coordinates");
                    return null;
                }
                try {
                    int x = Integer.parseInt(coord[0].trim());
                    int y = Integer.parseInt(coord[1].trim());
                    coordEngines.add(new int[]{x, y});
                } catch (NumberFormatException e) {
                    System.out.println("Error: engine coordinates must be numbers");
                    return null;
                }
            }

            if (indexE == -1 || indexE >= activateEngines.length) {
                System.out.println("Error: missing or misplaced 'battery' keyword");
                return null;
            }

            for (int i = indexE; i < activateEngines.length; i++) {
                String[] part = activateEngines[i].trim().split("\\s*-\\s*");
                if (part.length != 2) {
                    System.out.println("Error: invalid battery format");
                    return null;
                }
                String[] coord = part[0].trim().split("\\s+");
                if (coord.length != 2) {
                    System.out.println("Error: invalid battery coordinates");
                    return null;
                }
                try {
                    int x = Integer.parseInt(coord[0].trim());
                    int y = Integer.parseInt(coord[1].trim());
                    int cont = Integer.parseInt(part[1].trim());
                    coordBattery.add(new int[]{x, y, cont});
                } catch (NumberFormatException e) {
                    System.out.println("Error: battery values must be numbers");
                    return null;
                }
            }

            return new ActivateEnginesEvent(true, coordEngines, coordBattery, nickname);
        }

        System.out.println("Error: invalid command (missing y/n)");
        return null;
    }

    private ClientEvent parseDrawCard(String input, String nickname) {
        String[] cardFinish=input.split("\\s+");
        if(cardFinish.length!=1 || !cardFinish[0].trim().equals("draw")){
            System.out.println("Error: invalid command");
            return null;
        }
        return new DrawCardEvent(nickname);
    }

    private ClientEvent parsePlanetDecision(String input, String nickname) {
        String[] planetDecision = input.trim().split("\\s+");

        if (planetDecision.length != 1) {
            System.out.println("Error: invalid command (expected a single number)");
            return null;
        }

        try {
            int choice = Integer.parseInt(planetDecision[0].trim());
            return new PlanetDecisionEvent(choice, nickname);
        } catch (NumberFormatException e) {
            System.out.println("Error: input must be a valid number");
            return null;
        }
    }

    private ClientEvent parseDoNothing() {
        System.out.println("It's not your turn");
        return null;
    }

    private ClientEvent parseLand(String input) {
        String cmd = input.trim().toLowerCase();

        if (cmd.equals("y")) {
            return new TruckerLandingEvent(true);
        } else if (cmd.equals("n")) {
            return new TruckerLandingEvent(false);
        } else {
            System.out.println("Error: invalid command, type y/n");
            return null;
        }
    }

    private ClientEvent parseWarzone(String input) {
        String[] warzone=input.split("\\s+");
        if(warzone.length!=1 || !warzone[0].trim().equals("warzone")){
            System.out.println("Error: invalid command");
            return null;
        }
        return new WarzoneStartEvent();
    }

    private ClientEvent parseCosmicCredits(String input) {
        String cmd = input.trim().toLowerCase();
        if (cmd.equals("y")) return new GainCosmicCreditsResponseEvent(true);
        if (cmd.equals("n")) return new GainCosmicCreditsResponseEvent(false);

        System.out.println("Error: invalid command, please type y/n");
        return null;
    }

    private ClientEvent parseRearrange(String input, String nickname) {
        String[] rearrange = input.split("\\s*,\\s*");

        if (rearrange.length == 0) {
            System.out.println("Error: empty input");
            return null;
        }

        if (rearrange[0].trim().equals("n")) {
            return new RearrangeEvent(null, null, -1, -1, true, nickname);
        }

        if (rearrange[0].trim().equals("y")) {
            if (rearrange.length < 3) {
                System.out.println("Error: not enough arguments for rearrange");
                return null;
            }

            try {
                String[] part1 = rearrange[1].trim().split("\\s*-\\s*");
                String[] coord1 = part1[0].trim().split("\\s+");
                if (part1.length != 2 || coord1.length != 2) {
                    System.out.println("Error: invalid format for first item");
                    return null;
                }

                int x1 = Integer.parseInt(coord1[0].trim());
                int y1 = Integer.parseInt(coord1[1].trim());
                int value1 = Integer.parseInt(part1[1].trim());

                String[] part2 = rearrange[2].trim().split("\\s*-\\s*");
                String[] coord2 = part2[0].trim().split("\\s+");
                if (part2.length != 2 || coord2.length != 2) {
                    System.out.println("Error: invalid format for second item");
                    return null;
                }

                int x2 = Integer.parseInt(coord2[0].trim());
                int y2 = Integer.parseInt(coord2[1].trim());
                int value2 = Integer.parseInt(part2[1].trim());

                return new RearrangeEvent(new int[]{x1, y1}, new int[]{x2, y2}, value1, value2, false, nickname);

            } catch (NumberFormatException e) {
                System.out.println("Error: coordinates and values must be integers");
                return null;
            } catch (Exception e) {
                System.out.println("Error: unexpected input format");
                return null;
            }
        }

        System.out.println("Error: invalid command, please type 'y' or 'n'");
        return null;
    }

    private ClientEvent parseRemotionConfirmed(String input) {
        String[] remotion=input.split("\\s+");
        if(remotion.length!=1 || !remotion[0].trim().equals("continue")){
            System.out.println("Error: invalid command, please type 'continue'");
            return null;
        }
        return new RemotionSeenEvent();
    }

    private ClientEvent parseCurrentCondition(String input) {
        String[] currCond=input.split("\\s+");
        if(currCond.length!=1 || !currCond[0].trim().equals("continue")){
            System.out.println("Error: invalid command, please type 'continue'");
            return null;
        }
        return new WarzoneStartEvent();
    }

    private ClientEvent parseRemoveBatteries(String input, String nickname) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Error: empty input");
            return null;
        }

        String[] removeBatteries = input.split("\\s*,\\s*");
        ArrayList<int[]> coordinates = new ArrayList<>();

        for (String entry : removeBatteries) {
            try {
                String[] parts = entry.trim().split("\\s*-\\s*");
                if (parts.length != 2) {
                    System.out.println("Error: expected '<x> <y> - <value>'");
                    return null;
                }

                String[] coord = parts[0].trim().split("\\s+");
                if (coord.length != 2) {
                    System.out.println("Error: coordinates must be two numbers");
                    return null;
                }

                int x = Integer.parseInt(coord[0].trim());
                int y = Integer.parseInt(coord[1].trim());
                int quantity = Integer.parseInt(parts[1].trim());

                if (x < 0 || y < 0 || quantity < 0) {
                    System.out.println("Error: negative values are not allowed");
                    return null;
                }

                coordinates.add(new int[]{x, y, quantity});
            } catch (NumberFormatException e) {
                System.out.println("Error: all values must be integers");
                return null;
            } catch (Exception e) {
                System.out.println("Error: invalid format");
                return null;
            }
        }

        return new RemoveBatteriesEvent(coordinates, nickname);
    }


    private ClientEvent parseRemoveGoods(String input, String nickname) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Error: input is empty");
            return null;
        }

        String[] removeGoods = input.split("\\s*,\\s*");
        ArrayList<Integer> indexes = new ArrayList<>();

        for (String s : removeGoods) {
            try {
                int index = Integer.parseInt(s.trim());
                if (index < 0) {
                    System.out.println("Error: index cannot be negative");
                    return null;
                }
                indexes.add(index);
            } catch (NumberFormatException e) {
                System.out.println("Error: invalid index '" + s + "', must be an integer");
                return null;
            }
        }

        return new RemoveAskingEvent(indexes, nickname);
    }



}
