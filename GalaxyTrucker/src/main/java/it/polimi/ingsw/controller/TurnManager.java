package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidStateException;

import java.util.ArrayList;

/**
 * class that handles the fact that when the game is in state "FLIGHT" the game evolves with players that can send event to the server
 * only in their turn.
 */

public class TurnManager {
    private final ArrayList<ClientHandlerSocket> players;
    private int currentTurnIndex = 0;

    public TurnManager(ArrayList<ClientHandlerSocket> players) {
        this.players = new ArrayList<>(players);
    }

    /**
     * @return the ClientHandlerServer of the current player
     */
    public ClientHandlerSocket getCurrentClient() {
        return players.get(currentTurnIndex);
    }

    /**
     * @return the current player
     */
    public TruckerController getCurrentTrucker() {
        return getCurrentClient().getTrucker();
    }

    /**
     * @param client: the client that is currently sending an event that needs to respect the turn
     * @return if it is the turn of the client sending the event
     */

    public boolean isPlayerTurn(ClientHandlerSocket client) {
        return getCurrentClient().equals(client);
    }

    /**
     * sets the new index corresponding to the turn of the current Player
     */

    public void nextTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % players.size();
    }

    /**
     * reorders the list based on the position of the players
     */

    public void reorderPlayersByPosition() {
        players.sort((a, b) -> Integer.compare(a.getTrucker().getPosition(), b.getTrucker().getPosition()));
        currentTurnIndex = 0; // reset del turno
    }

    /**
     * resets the turn index
     */

    public void resetTurnIndex() {
        currentTurnIndex = 0;
    }

    /**
     *
     */
    public static class TruckerController {


        private PlayerColors color;
        private String nickname;
        private final SpaceShip spaceShip;
        private int position; //posizione assoluta
        private int cosmicCredit;
        private BuildingState buildingState;

        public TruckerController(String nickName, PlayerColors color) {
            this.nickname = nickName;
            this.color = color;
            this.spaceShip = new SpaceShip(color);
            this.cosmicCredit = 0;
            buildingState=BuildingState.BUILDING;
        }

        public BuildingState getBuildingState() {
            return buildingState;
        }

        public void setBuildingState(BuildingState buildingState) {
            this.buildingState = buildingState;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public PlayerColors getColor() {
            return color;
        }

        public void addCosmicCredit(int cosmicCredit) {
            this.cosmicCredit += cosmicCredit;
        }

        public int getCosmicCredit() {
            return cosmicCredit;
        }



        public String getNickname() {return nickname;}

        public SpaceShip getSpaceShip() {
            return spaceShip;
        }





        //FSM

        public void chooseWhatToDo(String cmd){
            switch(buildingState){
                case BUILDING:
                    switch (cmd){
                        case "Turn Hourglass":
                            buildingState=BuildingState.TURN_HOURGLASS;
                            break;
                        case "Select Component":
                            buildingState=BuildingState.SELECT_COMPONENT;
                            break;
                        case "Show Stack":
                            buildingState=BuildingState.SHOW_STACK;
                            break;
                        case "End":
                            buildingState=BuildingState.WAIT_END;

                    }
                case SELECT_COMPONENT:
                    switch (cmd){
                        case "Link":
                            buildingState=BuildingState.LINK;
                            break;
                        case "Keep":
                            buildingState=BuildingState.KEEP;
                            break;
                        case "Discard":
                            buildingState=BuildingState.BUILDING;
                            break;
                    }

            }


        }


        public ArrayList<Card> showStack(int index, Game game){
            if(buildingState!=BuildingState.SHOW_STACK){
                throw new IllegalStateException("Trucker is not in show stack state");
            }
            buildingState=BuildingState.BUILDING;
            return game.viewStack(index);

        }

        public void keep(Component component, Game game) throws InvalidComponentsException, InvalidStateException {
            if(buildingState!=BuildingState.KEEP){
                throw new InvalidStateException("Trucker is not in keep component state");
            }
            if(spaceShip.getKept().size()<2){
                spaceShip.getKept().add(component);
                buildingState=BuildingState.BUILDING;
            }
            else {
                component.setCovered(false);
                game.getComponents().getComponents().addFirst(component);
                buildingState=BuildingState.BUILDING;
                throw new InvalidComponentsException("No available space in kept component");
            }

        }

        public void link(Component component, int x, int y) throws InvalidStateException, InvalidCoordinates {
            if(buildingState!=BuildingState.LINK){
                throw new InvalidStateException("Trucker is not in link component state");
            }

            spaceShip.addComponent(component,x,y);
            buildingState=BuildingState.BUILDING;
        }

        public Component selectComponent(int index, AvailableComponents availableComponents) throws InvalidComponentsException, InvalidStateException {
            if(buildingState!=BuildingState.SELECT_COMPONENT){
                throw new InvalidStateException("Trucker is not in select component state");
            }
            if (index >= 0 && index < availableComponents.getComponents().size()) {
                Component selectedComponent;
                selectedComponent = availableComponents.getComponents().get(index);
                availableComponents.removeComponent(index);
                buildingState=BuildingState.SELECT_COMPONENT;
                return selectedComponent;
            } else {
                buildingState=BuildingState.SELECT_COMPONENT;
                throw new InvalidComponentsException("You're not in the range of components");
            }

        }




        public void finish() throws InvalidStateException {
            if(buildingState!=BuildingState.WAIT_END){
                throw new InvalidStateException("Trucker is not in finish state");
            }
            if(!spaceShip.wellDone()){
                spaceShip.increaseLost(spaceShip.getKept().size());
                spaceShip.getKept().removeAll(spaceShip.getKept());
            }
            buildingState=BuildingState.END;
        }

        public void fillSpaceship(ArrayList<int[]> coordAliens){
            spaceShip.setBattery();
            int goodsCapacity = spaceShip.getGoodsCapacity();
            spaceShip.setNumAlien(coordAliens);
            spaceShip.setNumHuman();
        }

        //FSM ingresso

    }
}
