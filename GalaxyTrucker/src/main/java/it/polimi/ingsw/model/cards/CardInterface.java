package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

public interface CardInterface {

    /**
     * Handles the landing of a player on an Abandoned ship or Abandoned Station
     */
    default ServerEvent truckerLanding(boolean landing) throws GeneralError, WrongCallerException{
        throw new WrongCallerException("This Card doesn't have this method");
    }

    /**
     * handle remotion of humans from the current player spaceShip
     */
    default ServerEvent removeHumans(ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates, GeneralError, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    /**
     * Adds the current good to the current trucker spaceship or handles the evolution of the card
     */
    default ServerEvent addGood(Boolean stop, int[] goodsCoordinates, int positionInLoad) throws GeneralError, InvalidCoordinates, WrongCallerException, InvalidPositionException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    /**
     * Handles the rearrangement of a pair of goods in the current player spaceship
     */
    default ServerEvent rearrange(int[] coord1, int[] coord2, int value1, int value2, boolean stop) throws GeneralError, InvalidCoordinates, InvalidPositionException, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    /**
     * handles the roll of the dice
     */
    default ServerEvent rollDice() throws GeneralError, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    /**
     * handles activation of Shields
     */
    default ServerEvent activateShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, WrongCallerException, InvalidCoordinates, NotEnoughBatteries {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    /**
     * handles activation of Double Engines
     */
    default ServerEvent activateEngines(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord ) throws InvalidCoordinates, GeneralError, WrongCallerException, NotEnoughBatteries {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    /**
     * Handles activation of double Cannons
     */
    default ServerEvent activateCannons(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws GeneralError, InvalidComponentsException, WrongCallerException, InvalidCoordinates, NotEnoughBatteries {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    /**
     * Handles the decision of the winner against smugglers, slavers or pirates
     */
    default ServerEvent setWinDecision(boolean answer)throws GeneralError, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    /**
     * Handles the land of a player on a Planet
     */
    default ServerEvent playerLandingPlanets(int indexOfPlanet) throws WrongCallerException, GeneralError, PlanetNotFreeException {
        throw new WrongCallerException("This method does not exist in the card ");
    }

    /**
     * Handles the remotion of goods from a player spaceship
     */
    default ServerEvent removeGoods() throws WrongCallerException, GeneralError {
        throw new WrongCallerException("This method does not exist in the card ");
    }

    /**
     * Removes the goods decided by the player from his/her spaceship
     */
    default ServerEvent removeGoodsDecided(ArrayList<Integer> indexes) throws WrongCallerException, GeneralError, InvalidGoodChosen, NotEnoughGoodsChosen {
        throw new WrongCallerException("This method does not exist in the card ");
    }

    /**
     * Handles the remotion of batteries from a player spaceship
     */
    default ServerEvent removeBatteries(ArrayList<int[]> coordinatesToRemoveBatteries)throws WrongCallerException, GeneralError, InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries  {
        throw new WrongCallerException("This method does not exist in the card ");
    }

    /**
     * Handles the current condition in the warzone card
     */
    default ServerEvent handleCurrentCondition() throws WrongCallerException, GeneralError {
        throw new WrongCallerException("This method does not exist in the card ");
    }

}
