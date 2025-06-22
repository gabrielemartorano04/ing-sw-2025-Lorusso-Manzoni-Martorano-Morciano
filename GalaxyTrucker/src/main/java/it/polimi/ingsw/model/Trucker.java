package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.components.Component;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This method identifies the player
 */

public class Trucker implements Serializable {

    /**
     * Player's color
     */
    private final PlayerColors color;

    /**
     * Player's nickname
     */
    private final String nickname;

    /**
     * Player's spaceship
     */
    private SpaceShip spaceShip;

    /**
     * Absolute position of the trucker in the track
     */
    private int position;

    /**
     * Cosmic credits gained by the player during the game
     */
    private int cosmicCredit;

    /**
     * Player's building state in building phase
     */
    private BuildingState buildingState;

    /**
     * List of kept component during building phase
     */
    private final ArrayList<Component> keptComponents;

    /**
     * Component selected by the player during building phase
     */
    private Component selectedComponent;

    /**
     * Class constructor
     * @param nickName player's nickname
     * @param color player's color
     */
    public Trucker(String nickName, PlayerColors color) {
        this.nickname = nickName;
        this.color = color;
        this.spaceShip = new SpaceShip();
        this.cosmicCredit = 0;
        buildingState=BuildingState.BUILDING;
        keptComponents = new ArrayList<>();
        this.position=-1;
    }

    /**
     * Kept component getter
     * @return list of kept component
     */
    public ArrayList<Component> getKeptComponents() {
        return keptComponents;
    }

    /**
     * Building state getter
     * @return building state during building phase
     */
    public BuildingState getBuildingState() {
        return buildingState;
    }

    /**
     * Building state setter
     * @param buildingState the value of the new building state
     */
    public void setBuildingState(BuildingState buildingState) {
        this.buildingState = buildingState;
    }

    /**
     * Player's position setter
     * @param position the new player's position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Player's absolute position getter
     * @return player absolute position in the track
     */
    public int getPosition() {
        return position;
    }

    /**
     * Player's color getter
     * @return player's color
     */
    public PlayerColors getColor() {
        return color;
    }

    /**
     * This method increase player's cosmic credits
     * @param cosmicCredit cosmic credits to add
     */
    public void addCosmicCredit(int cosmicCredit) {
        this.cosmicCredit += cosmicCredit;
    }

    /**
     * Player's cosmic credits getter
     * @return number of player's cosmic credits
     */
    public int getCosmicCredit() {
        return cosmicCredit;
    }

    /**
     * Player's selected component getter
     * @return component selected by the player
     */
    public Component getSelectedComponent() {
        return selectedComponent;
    }

    /**
     * Player's nickname getter
     * @return player's nickname
     */
    public String getNickname() {return nickname;}

    /**
     * Player's spaceship getter
     * @return player's spaceship
     */
    public SpaceShip getSpaceShip() {
        return this.spaceShip;
    }

    /**
     * This method let a player see a card game stack
     * @param index index of the stack the player wants to see
     * @param game game the player is in, it contains card stacks
     * @return the stack the player asked
     * @throws GeneralError if the stack is occupied by another player
     */
    public Stack showStack(int index, Game game) throws GeneralError{
        try {
            return game.viewStack(index);
        } catch (GeneralError e) {
            throw new GeneralError(e.getMessage());
        }
    }

    /**
     * Method that inserts a component in kept component list
     * @param component component the player wants to keep
     * @throws InvalidComponentsException if there are already two kept components
     */
    public void keep(Component component) throws InvalidComponentsException {
        if(keptComponents.size()<2){
            keptComponents.add(component);
        }
        else { //non c'è spazio per tenerlo quindi lancio eccezione
            throw new InvalidComponentsException("No available spots to keep this component cause both places are taken");
        }

    }

    /**
     * Method that links a component in player's spaceship
     * @param component component that the player wants to link
     * @param x x coordinate to link the component
     * @param y y coordinate to link the component
     * @throws InvalidCoordinates if the player can't link that component in that position, for link issue or position
     */
    public void link(Component component, int x, int y) throws InvalidCoordinates {
        spaceShip.addComponent(component,x,y);
    }

    /**
     * Method that lets the player select a component from available components list
     * @param index component index from available components list
     * @param availableComponents available components list
     * @throws GeneralError if the component was selected by another player at the moment or linked in another spaceship
     */
    public void selectComponent(int index, AvailableComponents availableComponents) throws GeneralError {
        if(availableComponents.getComponents().get(index).getSelected()){
            throw new GeneralError("Component already selected");
        }
        Component component = availableComponents.getComponents().get(index);
        synchronized (component) {
            this.selectedComponent = component;
        }

    }

    /**
     * Kept component getter
     * @param index requested kept component index
     * @return requested kept component
     * @throws GeneralError if there isn't a kept component with that index
     */
    public Component getKeptComponent(int index) throws GeneralError{
        if(!(0 <= index && index < keptComponents.size())){
            throw new GeneralError("There isn't a kept component at this index " + index);
        }
        return keptComponents.get(index);
    }

    /**
     * This method remove a component from kept components list
     * @param index index of the component to remove
     */
    public void removeKeptComponent(int index) {
        keptComponents.remove(index);
    }

    /**
     * Player's spaceship setter. It's used for test
     * @param spaceShip the new player's spaceship
     */
    public void setSpaceShip(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
    }

    /**
     * This method initializes the player's spaceship with available positions abd main cabin color
     * @param level game level for the shape of the spaceship
     */
    public void initSpaceShip(int level) {
        this.spaceShip.initSpaceShip(level, color);
    }
}

