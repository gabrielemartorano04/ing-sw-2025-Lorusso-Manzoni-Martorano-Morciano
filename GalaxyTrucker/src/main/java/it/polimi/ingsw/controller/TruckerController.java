package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.components.Component;

/**
 * This class is the player controller.
 * Attributes:
 * - Trucker trucker, the player
 * - int selectedComponentIndex, the index of the selected component in the available components.
 */
public class TruckerController  {
    private final Trucker trucker;
    private int selectedComponentIndex;

    /**
     * we keep both selectedComponent and its index so that we use selectedComponent for the methods that need it
     * and we use its index to check every time that the selectedComponent on the server is the same as the one
     * on the view for that client, given the fact that the index is relative to the list that never changes its size
     * the check clientIndex = selectedComponentIndex is valid and works. In fact we couldn't compare this selectedComponent
     * with a copy coming from the net.
     */


    /**
     * Class constructor, it creates a linked trucker instance
     * @param nickname nickname of the player
     * @param color color choosen by the player
     */
    public TruckerController(String nickname, PlayerColors color){
        trucker=new Trucker(nickname,color);
    }

    /**
     * This methos sets the Trucker's position in the board
     * @param position new trucker's position
     */
    public void setPosition(int position) {
        trucker.setPosition(position);
    }

    /**
     * @return trucker's position
     */
    public int getPosition() {
        return trucker.getPosition();
    }

    /**
     * @return trucker's color
     */
    public PlayerColors getColor() {
        return trucker.getColor();
    }

    /**
     * This method adds cosmicCredit to the trucker
     * @param cosmicCredit cosmicCredit to add
     */
    public void addCosmicCredit(int cosmicCredit) {
        trucker.addCosmicCredit(cosmicCredit);
    }

    /**
     * @return trucker's cosmic credits
     */
    public int getCosmicCredit() {
        return trucker.getCosmicCredit();
    }

    /**
     * @return trucker's nickname
     */
    public String getNickname() {
        return trucker.getNickname();
    }

    /**
     * @return trucker's spaceship
     */
    public SpaceShip getSpaceShip() {
        return trucker.getSpaceShip();
    }

    /**
     * This method shows the selected stack
     * @param index requested stack's index
     * @param game trucker's game
     * @return the selected stack if it's free
     * @throws GeneralError if the stack is occupied by another trucker
     */
    public Stack showStack(int index, Game game) throws GeneralError {
        try {
            return trucker.showStack(index, game);
        } catch (GeneralError e) {
            throw new GeneralError(e.getMessage());
        }
    }

    /**
     * This method adds the component to kept list
     * @param component component that the player wants to keep
     * @throws InvalidComponentsException if the player already has 2 kept components
     */
    public void keep(Component component) throws InvalidComponentsException {
        trucker.keep(component);
    }

    /**
     * This method links a components to the trucker's spaceship
     * @param component component that the player wants to link
     * @param x x coordinate of the position
     * @param y y coordinate of the position
     * @throws InvalidCoordinates if the player can't place the component in that position
     */
    public void link(Component component, int x, int y) throws InvalidCoordinates {
        trucker.link(component, x, y);
        selectedComponentIndex = -1;
    }

    /**
     * This method select a component from the available components list
     * @param index index of the component
     * @param availableComponents this object contains all the available components
     * @throws GeneralError if the component is already selected or the index is not valid
     */
    public void selectComponent(int index, AvailableComponents availableComponents) throws GeneralError {
        trucker.selectComponent(index, availableComponents);
        this.selectedComponentIndex = index;
    }

    /**
     * @return component selected by the player
     */
    public Component getSelectedComponent() {
        return trucker.getSelectedComponent();
    }

    /**
     * @return index of the selected component
     */
    public int getSelectedComponentIndex() {
        return selectedComponentIndex;
    }

    /**
     * This method returns one of the kept components, based on the index
     * @param index index of the kept component (0 or 1)
     * @return the kept component with the given index
     * @throws GeneralError if there is no kept component with such index
     */
    public Component getKeptComponent(int index) throws GeneralError {
        return trucker.getKeptComponent(index);
    }

    /**
     * @return Trucker linked to this controller
     */
    public Trucker getTrucker() {
        return trucker;
    }

    public void initSpaceShip(int level) {
        this.trucker.initSpaceShip(level);
    }

}
