package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.utilities.exceptions.*;

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

    default ServerEvent activateShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, WrongCallerException, InvalidCoordinates {
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

    //planets

    default ServerEvent playerLandingPlanets(int indexOfPlanet) throws WrongCallerException, GeneralError, PlanetNotFreeException {throw new WrongCallerException("This method does not exist in the card ");}

    // Smugglers

    default int getNumOfBatteriesToRemove() throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState setWinnerCoordinatesToAddGoods(int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank)throws WrongCallerException, GeneralError, InvalidCoordinates {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState manageRemoveGoods(Trucker trucker, Bank bank) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default ArrayList<Goods> plotGoodsList(Trucker trucker) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState removeAsking(ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState removeBatteries(ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker)throws WrongCallerException, GeneralError, InvalidGoodChosen, NotEnoughGoodsChosen {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState combatZone(Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default void findWeakestTrucker(Game game, ArrayList<ArrayList<int[]>> truckersCoordinates)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default void removeAsking(ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws WrongCallerException, GeneralError, InvalidGoodChosen, NotEnoughGoodsChosen {throw new WrongCallerException("This method does not exist in the card ");}

    default int[] getCurrentMeteor()throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default void setAnswers(boolean answers) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState removeCrew(ArrayList<int[]> coordinates) throws WrongCallerException, GeneralError, InvalidComponentsException, InvalidCoordinates {throw new WrongCallerException("This method does not exist in the card ");}
}
