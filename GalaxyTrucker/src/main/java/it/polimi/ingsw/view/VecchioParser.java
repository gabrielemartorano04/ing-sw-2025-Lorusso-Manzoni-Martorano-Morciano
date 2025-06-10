//package it.polimi.ingsw.view;
//
//import it.polimi.ingsw.model.components.Component;
//import it.polimi.ingsw.net.events.clientevents.ClientEvent;
//import it.polimi.ingsw.net.events.clientevents.ShowOtherShipEvent;
//import it.polimi.ingsw.net.events.clientevents.buildingevents.*;
//
//import java.awt.*;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//
//public class VecchioParser {
//    @Deprecated
//    public ClientEvent parseBuildingOld(String input, Component component, int index, ArrayList<Component> kept, boolean isFromKept, int stackSelected){
//        if (input == null || input.isEmpty()) {
//            return null;
//        }
//        String[] tokens = input.split("\\s+", 3);
//        String command = tokens[0].toLowerCase();
//        System.out.println(level);
//
//        if(level==2){
//            if(stackSelected==-1) {
//                if (component != null) {
//                    if (isFromKept) {
//                        switch (command) {
//                            case "help":
//                                String message = "Commands:\n";
//                                message+="- rules : to open pdf file with game rules\n";
//                                message+="- turn : to turn selected component\n";
//                                message+="- link <x> <y> : to link selected component in position (x,y)\n";
//                                message+="- keep : to keep the selected component\n";
//                                message+="- ship <nickname> : to see the ship of another player\n";
//                                System.out.println(message);
//                                return null;
//                            case "rules":
//                                try (InputStream is = getClass().getClassLoader().getResourceAsStream("it/polimi/ingsw/view/galaxy-trucker-rules-en.pdf")) {
//                                    if (is == null) {
//                                        System.out.println("File not found");
//                                        return null;
//                                    }
//
//                                    File tempPdf = File.createTempFile("galaxy-trucker-rules", ".pdf");
//                                    tempPdf.deleteOnExit();
//
//                                    try (FileOutputStream os = new FileOutputStream(tempPdf)) {
//                                        byte[] buffer = new byte[1024];
//                                        int bytesRead;
//                                        while ((bytesRead = is.read(buffer)) != -1) {
//                                            os.write(buffer, 0, bytesRead);
//                                        }
//                                    }
//
//                                    Desktop.getDesktop().open(tempPdf);
//
//                                } catch (IOException e) {
//                                    System.out.println("Error in file opening: " + e.getMessage());
//                                }
//                                return null;
//                            case "turn":
//                                if (tokens.length != 1) {
//                                    System.out.println("Error: illegal command.");
//                                    return null;
//                                }
//                                return new TurnComponentEvent(component);
//                            case "link":
//                                if (tokens.length != 3) {
//                                    System.out.println("Error: illegal command. Type 'link <x> <y>'.");
//                                    return null;
//                                }
//                                int x=Integer.parseInt(tokens[1].trim());
//                                int y=Integer.parseInt(tokens[2].trim());
//                                if(x<5 || x>9 || y<4 || y>10){
//                                    System.out.println("Error: illegal coordinates. Type 'link <x> <y>'.");
//                                    return null;
//                                }
//                                return new LinkTileEvent(index, x, y, component);
//                            case "keep":
//                                if (tokens.length != 1) {
//                                    System.out.println("Error: illegal command. Type 'keep <index>'.");
//                                    return null;
//                                }
//                                return new KeepTileEvent(component);
//                            case "ship":
//                                if (tokens.length != 2) {
//                                    System.out.println("Error: illegal command. Type 'ship <nickname>'.");
//                                    return null;
//                                }
//                                return new ShowOtherShipEvent(tokens[1].trim());
//                            default:
//                                System.out.println("Error: unknown command: " + command);
//                                return null;
//                        }
//                    }
//                    switch (command) {
//                        case "help":
//                            String message = "Commands:\n";
//                            message+="- rules : to open pdf file with game rules\n";
//                            message+="- turn : to turn selected component\n";
//                            message+="- link <x> <y> : to link selected component in position (x,y)\n";
//                            message+="- keep : to keep the selected component\n";
//                            message+="- ship <nickname> : to see the ship of another player\n";
//                            message+="- discard : to discard the selected component\n";
//                            System.out.println(message);
//                            return null;
//                        case "rules":
//                            try (InputStream is = getClass().getClassLoader().getResourceAsStream("it/polimi/ingsw/view/galaxy-trucker-rules-en.pdf")) {
//                                if (is == null) {
//                                    System.out.println("File not found");
//                                    return null;
//                                }
//
//                                File tempPdf = File.createTempFile("galaxy-trucker-rules", ".pdf");
//                                tempPdf.deleteOnExit();
//
//                                try (FileOutputStream os = new FileOutputStream(tempPdf)) {
//                                    byte[] buffer = new byte[1024];
//                                    int bytesRead;
//                                    while ((bytesRead = is.read(buffer)) != -1) {
//                                        os.write(buffer, 0, bytesRead);
//                                    }
//                                }
//
//                                Desktop.getDesktop().open(tempPdf);
//
//                            } catch (IOException e) {
//                                System.out.println("Error in file opening: " + e.getMessage());
//                            }
//                            return null;
//                        case "turn":
//                            if (tokens.length != 1) {
//                                System.out.println("Error: illegal command.");
//                                return null;
//                            }
//                            return new TurnComponentEvent(component);
//                        case "ship":
//                            if (tokens.length != 2) {
//                                System.out.println("Error: illegal command. Type 'ship <nickname>'.");
//                                return null;
//                            }
//                            return new ShowOtherShipEvent(tokens[1].trim());
//
//                        case "link":
//                            if (tokens.length != 3) {
//                                System.out.println("Error: illegal command. Type 'link <x> <y>'.");
//                                return null;
//                            }
//                            if((Integer.parseInt(tokens[1])<5 || Integer.parseInt(tokens[1])>9) && (Integer.parseInt(tokens[2])<4 || Integer.parseInt(tokens[2])>10)){
//                                System.out.println("Error: illegal command. Please set a valiid index\n");
//                                return null;
//                            }
//                            return new LinkTileEvent(index, Integer.parseInt(tokens[1].trim()), Integer.parseInt(tokens[2].trim()), component);
//                        case "keep":
//                            if (tokens.length != 1) {
//                                System.out.println("Error: illegal command. Type 'keep' if you want to keep the selected component.");
//                                return null;
//                            }
//                            return new KeepTileEvent(component);
//                        case "discard":
//                            if (tokens.length != 1) {
//                                System.out.println("Error: illegal command. Type 'discard' if you want to discard the selected component.");
//                                return null;
//                            }
//                            return new DiscardTileEvent(index);
//                        default:
//                            System.out.println("Error: unknown command: " + command);
//                            return null;
//                    }
//                }
//                else {
//                    switch (command) {
//                        case "help":
//                            String message = "Commands:\n";
//                            message+="- rules : to open pdf file with game rules\n";
//                            message+="- ship <nickname> : to see the ship of another player\n";
//                            message+="- hourglass : to turn the hourglass\n";
//                            message+="- select <index> : to select a component\n";
//                            message+="- selectkept <index> : to select one of your kept components\n";
//                            message+="- stack <index> : to see a stack\n";
//                            System.out.println(message);
//                            return null;
//                        case "rules":
//                            try (InputStream is = getClass().getClassLoader().getResourceAsStream("it/polimi/ingsw/view/galaxy-trucker-rules-en.pdf")) {
//                                if (is == null) {
//                                    System.out.println("File not found");
//                                    return null;
//                                }
//
//                                File tempPdf = File.createTempFile("galaxy-trucker-rules", ".pdf");
//                                tempPdf.deleteOnExit();
//
//                                try (FileOutputStream os = new FileOutputStream(tempPdf)) {
//                                    byte[] buffer = new byte[1024];
//                                    int bytesRead;
//                                    while ((bytesRead = is.read(buffer)) != -1) {
//                                        os.write(buffer, 0, bytesRead);
//                                    }
//                                }
//
//                                Desktop.getDesktop().open(tempPdf);
//
//                            } catch (IOException e) {
//                                System.out.println("Error in file opening: " + e.getMessage());
//                            }
//                            return null;
//                        case "ship":
//                            if (tokens.length != 2) {
//                                System.out.println("Error: illegal command. Type 'ship <nickname>'.");
//                                return null;
//                            }
//                            return new ShowOtherShipEvent(tokens[1].trim());
//                        case "hourglass":
//                            if (tokens.length != 1) {
//                                System.out.println("Error: illegal command. Type 'hourglass' if you want to turn the hourglass");
//                                return null;
//                            }
//                            return new TurnHourglassEvent();
//                        case "select":
//                            if (tokens.length != 2) {
//                                System.out.println("Error: illegal command. Type 'select <index>'.");
//                                return null;
//                            }
//                            return new SelectTileEvent(Integer.parseInt(tokens[1].trim()));
//
//                        case "selectkept":
//
//                            if (tokens.length != 2) {
//                                System.out.println("Error: illegal command. Type 'selectkept <index>'.");
//                                return null;
//                            }
//                            int ind = Integer.parseInt(tokens[1].trim());
//                            if (ind < 0 || ind > kept.size() - 1) {
//                                System.out.println("Please type a valid index. Type 'selectkept <index>'.");
//                                return null;
//                            }
//                            return new SelectKeptTileEvent(ind);
//
//                        case "stack":
//                            if (tokens.length != 2) {
//                                System.out.println("Error: illegal command. Type 'stack <index>'.");
//                                return null;
//                            }
//                            if (Integer.parseInt(tokens[1].trim()) >= 1 && Integer.parseInt(tokens[1].trim()) <= 3) {
//                                return new ShowStackEvent(Integer.parseInt(tokens[1].trim()) - 1);
//                            } else {
//                                System.out.println("Error: illegal stack number. Please select a stack between 1 and 3 or choose another command");
//                            }
//                            return null;
//                        case "end":
//                            if (tokens.length != 1) {
//                                System.out.println("Error: illegal command. Type 'end' if you want to end building phase.");
//                                return null;
//                            }// il client finisce e aspetta o che tutti finiscono o che il timer
//                            return new EndBuildingEvent();
//                        default:
//                            System.out.println("Error: unknown command: " + command + ". Please select a valid command");
//                            return null;
//                    }
//                }
//            }else{
//                if(command.equals("help") && tokens.length == 1){
//                    String message = "Commands:\n";
//                    message+="- dropstack: to drop the stack\n";
//                    System.out.println(message);
//                    return null;
//                }
//
//                if(command.equals("dropstack") && tokens.length == 1) {
//                    return new StackDroppedEvent(stackSelected);
//                }else {
//                    System.out.println("Error: illegal command. Type 'dropstack' if you want to drop your stack and proceed");
//                    return null;
//                }
//            }
//        }
//        else{
//            if (component != null) {
//                if (isFromKept) {
//                    switch (command) {
//                        case "help":
//                            String message = "Commands:\n";
//                            message+="- rules : to open pdf file with game rules\n";
//                            message+="- turn : to turn selected component\n";
//                            message+="- link <x> <y> : to link selected component in position (x,y)\n";
//                            message+="- keep : to keep the selected component\n";
//                            message+="- ship <nickname> : to see the ship of another player\n";
//                            System.out.println(message);
//                            return null;
//                        case "rules":
//                            try (InputStream is = getClass().getClassLoader().getResourceAsStream("it/polimi/ingsw/view/galaxy-trucker-rules-en.pdf")) {
//                                if (is == null) {
//                                    System.out.println("File not found");
//                                    return null;
//                                }
//
//                                File tempPdf = File.createTempFile("galaxy-trucker-rules", ".pdf");
//                                tempPdf.deleteOnExit();
//
//                                try (FileOutputStream os = new FileOutputStream(tempPdf)) {
//                                    byte[] buffer = new byte[1024];
//                                    int bytesRead;
//                                    while ((bytesRead = is.read(buffer)) != -1) {
//                                        os.write(buffer, 0, bytesRead);
//                                    }
//                                }
//
//                                Desktop.getDesktop().open(tempPdf);
//
//                            } catch (IOException e) {
//                                System.out.println("Error in file opening: " + e.getMessage());
//                            }
//                            return null;
//                        case "turn":
//                            if (tokens.length != 1) {
//                                System.out.println("Error: illegal command.");
//                                return null;
//                            }
//                            return new TurnComponentEvent(component);
//                        case "link":
//                            if (tokens.length != 3) {
//                                System.out.println("Error: illegal command. Type 'link <x> <y>'.");
//                                return null;
//                            }
//                            int x=Integer.parseInt(tokens[1].trim());
//                            int y=Integer.parseInt(tokens[2].trim());
//                            if(x<5 || x>9 || y<4 || y>10){
//                                System.out.println("Error: illegal coordinates. Type 'link <x> <y>'.");
//                                return null;
//                            }
//                            return new LinkTileEvent(index, x, y, component);
//                        case "keep":
//                            if (tokens.length != 1) {
//                                System.out.println("Error: illegal command. Type 'keep <index>'.");
//                                return null;
//                            }
//                            return new KeepTileEvent(component);
//                        case "ship":
//                            if (tokens.length != 2) {
//                                System.out.println("Error: illegal command. Type 'ship <nickname>'.");
//                                return null;
//                            }
//                            return new ShowOtherShipEvent(tokens[1].trim());
//                        default:
//                            System.out.println("Error: unknown command: " + command);
//                            return null;
//                    }
//                }
//                switch (command) {
//                    case "help":
//                        String message = "Commands:\n";
//                        message+="- rules : to open pdf file with game rules\n";
//                        message+="- turn : to turn selected component\n";
//                        message+="- link <x> <y> : to link selected component in position (x,y)\n";
//                        message+="- keep : to keep the selected component\n";
//                        message+="- ship <nickname> : to see the ship of another player\n";
//                        message+="- discard : to discard the selected component\n";
//                        System.out.println(message);
//                        return null;
//                    case "rules":
//                        try (InputStream is = getClass().getClassLoader().getResourceAsStream("it/polimi/ingsw/view/galaxy-trucker-rules-en.pdf")) {
//                            if (is == null) {
//                                System.out.println("File not found");
//                                return null;
//                            }
//
//                            File tempPdf = File.createTempFile("galaxy-trucker-rules", ".pdf");
//                            tempPdf.deleteOnExit();
//
//                            try (FileOutputStream os = new FileOutputStream(tempPdf)) {
//                                byte[] buffer = new byte[1024];
//                                int bytesRead;
//                                while ((bytesRead = is.read(buffer)) != -1) {
//                                    os.write(buffer, 0, bytesRead);
//                                }
//                            }
//
//                            Desktop.getDesktop().open(tempPdf);
//
//                        } catch (IOException e) {
//                            System.out.println("Error in file opening: " + e.getMessage());
//                        }
//                        return null;
//                    case "turn":
//                        if (tokens.length != 1) {
//                            System.out.println("Error: illegal command.");
//                            return null;
//                        }
//                        return new TurnComponentEvent(component);
//                    case "ship":
//                        if (tokens.length != 2) {
//                            System.out.println("Error: illegal command. Type 'ship <nickname>'.");
//                            return null;
//                        }
//                        return new ShowOtherShipEvent(tokens[1].trim());
//
//                    case "link":
//                        if (tokens.length != 3) {
//                            System.out.println("Error: illegal command. Type 'link <x> <y>'.");
//                            return null;
//                        }
//                        if((Integer.parseInt(tokens[1])<5 || Integer.parseInt(tokens[1])>9) && (Integer.parseInt(tokens[2])<4 || Integer.parseInt(tokens[2])>10)){
//                            System.out.println("Error: illegal command. Please set a valiid index\n");
//                            return null;
//                        }
//                        return new LinkTileEvent(index, Integer.parseInt(tokens[1].trim()), Integer.parseInt(tokens[2].trim()), component);
//                    case "keep":
//                        if (tokens.length != 1) {
//                            System.out.println("Error: illegal command. Type 'keep' if you want to keep the selected component.");
//                            return null;
//                        }
//                        return new KeepTileEvent(component);
//                    case "discard":
//                        if (tokens.length != 1) {
//                            System.out.println("Error: illegal command. Type 'discard' if you want to discard the selected component.");
//                            return null;
//                        }
//                        return new DiscardTileEvent(index);
//                    default:
//                        System.out.println("Error: unknown command: " + command);
//                        return null;
//                }
//            } else {
//                switch (command) {
//                    case "help":
//                        String message = "Commands:\n";
//                        message+="- rules : to open pdf file with game rules\n";
//                        message+="- ship <nickname> : to see the ship of another player\n";
//                        message+="- select <index> : to select a component\n";
//                        message+="- selectkept <index> : to select one of your kept components\n";
//                        System.out.println(message);
//                        return null;
//                    case "rules":
//                        try (InputStream is = getClass().getClassLoader().getResourceAsStream("it/polimi/ingsw/view/galaxy-trucker-rules-en.pdf")) {
//                            if (is == null) {
//                                System.out.println("File not found");
//                                return null;
//                            }
//
//                            File tempPdf = File.createTempFile("galaxy-trucker-rules", ".pdf");
//                            tempPdf.deleteOnExit();
//
//                            try (FileOutputStream os = new FileOutputStream(tempPdf)) {
//                                byte[] buffer = new byte[1024];
//                                int bytesRead;
//                                while ((bytesRead = is.read(buffer)) != -1) {
//                                    os.write(buffer, 0, bytesRead);
//                                }
//                            }
//
//                            Desktop.getDesktop().open(tempPdf);
//
//                        } catch (IOException e) {
//                            System.out.println("Error in file opening: " + e.getMessage());
//                        }
//                        return null;
//                    case "ship":
//                        if (tokens.length != 2) {
//                            System.out.println("Error: illegal command. Type 'ship <nickname>'.");
//                            return null;
//                        }
//                        return new ShowOtherShipEvent(tokens[1].trim());
//                    case "select":
//                        if (tokens.length != 2) {
//                            System.out.println("Error: illegal command. Type 'select <index>'.");
//                            return null;
//                        }
//                        return new SelectTileEvent(Integer.parseInt(tokens[1].trim()));
//
//                    case "selectkept":
//                        if (tokens.length != 2) {
//                            System.out.println("Error: illegal command. Type 'selectkept <index>'.");
//                            return null;
//                        }
//                        int ind = Integer.parseInt(tokens[1].trim());
//
//                        if (ind < 0 || ind > kept.size() - 1) {
//                            System.out.println("Please type a valid index. Type 'selectkept <index>'.");
//                            return null;
//                        }
//                        return new SelectKeptTileEvent(ind);
//                    case "end":
//                        if (tokens.length != 1) {
//                            System.out.println("Error: illegal command. Type 'end' if you want to end building phase.");
//                            return null;
//                        }// il client finisce e aspetta o che tutti finiscono o che il timer
//                        return new EndBuildingEvent();
//                    default:
//                        System.out.println("Error: unknown command: " + command + ". Please select a valid command");
//                        return null;
//                }
//            }
//        }
//
//    }
//
//}
