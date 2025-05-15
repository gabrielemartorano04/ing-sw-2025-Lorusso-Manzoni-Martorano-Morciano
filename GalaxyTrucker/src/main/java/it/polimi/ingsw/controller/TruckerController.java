package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.utilities.exceptions.ComponentNotAvailable;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidStateException;

import java.util.ArrayList;

public class TruckerController  {
    private Trucker trucker;
    private int selectedComponentIndex;

    /**
     * we keep both selectedComponent and its index so that we use selectedComponent for the methods that need it
     * and we use its index to check every time that the selectedComponent on the server is the same as the one
     * on the view for that client, given the fact that the index is relative to the list that never changes its size
     * the check clientIndex = selectedComponentIndex is valid and works. In fact we couldn't compare this selectedComponent
     * with a copy coming from the net.
     */

    public TruckerController(String nickname, PlayerColors color){
        trucker=new Trucker(nickname,color);
    }


    public void setPosition(int position) {
        trucker.setPosition(position);
    }

    public int getPosition() {
        return trucker.getPosition();
    }

    public PlayerColors getColor() {
        return trucker.getColor();
    }

    public void addCosmicCredit(int cosmicCredit) {
        trucker.addCosmicCredit(cosmicCredit);
    }

    public int getCosmicCredit() {
        return trucker.getCosmicCredit();
    }

    public String getNickname() {
        return trucker.getNickname();
    }

    public SpaceShip getSpaceShip() {
        return trucker.getSpaceShip();
    }

    public ArrayList<Card> showStack(int index, Game game) {
        return trucker.showStack(index, game);
    }

    public void keep(Component component, Game game) throws InvalidComponentsException {
        trucker.keep(component, game);
    }

    public void link(Component component, int x, int y) throws InvalidCoordinates {
        trucker.link(component, x, y);
    }

    public void selectComponent(int index, AvailableComponents availableComponents) throws ComponentNotAvailable {
        trucker.selectComponent(index, availableComponents);
        this.selectedComponentIndex = index;
    }

    public Component getSelectedComponent() {
        return trucker.getSelectedComponent();
    }

    public int getSelectedComponentIndex() {
        return selectedComponentIndex;
    }

    public void selectKeptComponent(int index){

    }

    public void finish() throws InvalidStateException {
        trucker.finish();
    }

    public void fillSpaceship(ArrayList<int[]> coordAliens) {
        trucker.fillSpaceship(coordAliens);
    }

    public Trucker getTrucker() {
        return trucker;
    }

}
