package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

public interface CardInterface {

    default ServerEvent truckerLanding(boolean landing) throws GeneralError, WrongCallerException{
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent setHumansCoordinates(ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates, GeneralError, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent setGoodsCoordinates(Boolean stop, int[] goodsCoordinates, int positionInLoad) throws GeneralError, InvalidCoordinates, WrongCallerException, InvalidPositionException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent rearrange(int[] coord1, int[] coord2, int value1, int value2, boolean stop) throws GeneralError, InvalidCoordinates, InvalidPositionException, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent rollDice() throws GeneralError, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent activateShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, WrongCallerException, InvalidCoordinates, NotEnoughBatteries {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent activateEngines(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord ) throws InvalidCoordinates, GeneralError, WrongCallerException, NotEnoughBatteries {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent activateCannons(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws GeneralError, InvalidComponentsException, WrongCallerException, InvalidCoordinates, NotEnoughBatteries {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent setWinDecision(boolean answer)throws GeneralError, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent playerLandingPlanets(int indexOfPlanet) throws WrongCallerException, GeneralError, PlanetNotFreeException {
        throw new WrongCallerException("This method does not exist in the card ");
    }


    default ServerEvent removeGoods() throws WrongCallerException, GeneralError {
        throw new WrongCallerException("This method does not exist in the card ");
    }

    default ServerEvent removeAsking(ArrayList<Integer> indexes) throws WrongCallerException, GeneralError, InvalidGoodChosen, NotEnoughGoodsChosen {
        throw new WrongCallerException("This method does not exist in the card ");
    }

    default ServerEvent removeBatteries(ArrayList<int[]> coordinatesToRemoveBatteries)throws WrongCallerException, GeneralError, InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries  {
        throw new WrongCallerException("This method does not exist in the card ");
    }

    default ServerEvent handleCurrentCondition() throws WrongCallerException, GeneralError {
        throw new WrongCallerException("This method does not exist in the card ");
    }

}
