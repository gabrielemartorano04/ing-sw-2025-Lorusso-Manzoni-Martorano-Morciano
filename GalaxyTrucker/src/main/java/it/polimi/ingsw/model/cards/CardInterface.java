package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.utilities.exceptions.*;

import java.util.ArrayList;

public interface CardInterface {

    default ServerEvent truckerLanding(ArrayList<Trucker> truckers, boolean landing) throws GeneralError, WrongCallerException{
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent setHumansCoordinates(ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates, GeneralError, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

//    default CardState truckerLanding(Trucker trucker, boolean landing, int numPlayer, Bank bank) throws GeneralError, WrongCallerException {
//        throw new WrongCallerException("This Card doesn't have this method");
//    }

    default ServerEvent setChoiceToGain(boolean choice) throws GeneralError, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent setGoodsCoordinates(Boolean stop, int[] goodsCoordinates, int value) throws GeneralError, InvalidCoordinates, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent rearrange(int[] coord1, int[] coord2, int value1, int value2, boolean stop) throws GeneralError, InvalidCoordinates, InvalidPositionException, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent rollDice() throws GeneralError, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent setShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, WrongCallerException, InvalidCoordinates {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default ServerEvent setEnginesCoordinates(Boolean activate, ArrayList<int[]> truckerCoordinate) throws InvalidCoordinates, GeneralError, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default CardState setCannonsCoordinates(Boolean activate, ArrayList<int[]> doubleCannonsCoord) throws GeneralError, InvalidComponentsException, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default int[] getCurrentShoot() throws GeneralError, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default int rollDice(Game game) throws GeneralError, WrongCallerException{
        throw new WrongCallerException("This Card doesn't have this method");
    }

    default CardState setAnswers(boolean answers, Trucker trucker) throws GeneralError, WrongCallerException {
        throw new WrongCallerException("This Card doesn't have this method");
    }


    //planets
    default int[] getBank(Bank bank) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState getState()throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState initialize(Bank bank) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState setPlayerLanding(int indexOfLanding, Trucker trucker, int numPlayers) throws WrongCallerException, GeneralError, PlanetNotFreeException {throw new WrongCallerException("This method does not exist in the card ");}

    default Goods plotGoodFromPlanet() throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default Goods plotGoodFromList() throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState chooseToGain (boolean choice) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState chooseFromList (boolean choice) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState setCoordinatesToAddGoods(int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws WrongCallerException, GeneralError, InvalidCoordinates {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState coordinatesToAddFromList(int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws WrongCallerException, GeneralError, InvalidCoordinates {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState rearrange(int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws WrongCallerException, GeneralError, InvalidCoordinates, InvalidPositionException {throw new WrongCallerException("This method does not exist in the card ");}

    // Slavers

    default CardState addTruckersCoordinatesToRemoveHumans(Trucker trucker, ArrayList<int[]> truckersCoordinatesToRemoveHumans, int numPlayer) throws WrongCallerException, GeneralError, InvalidCoordinates {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState addDoubleCannonsCoord(Trucker trucker, ArrayList<int[]> doubleCannonsCoord, int numPlayer) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState setChooseToGain(Boolean choice) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    // Smugglers

    default CardState addDoubleCannonsCoord(Trucker trucker, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Bank bank)throws WrongCallerException, GeneralError, InvalidComponentsException {throw new WrongCallerException("This method does not exist in the card ");}

    default Goods plotCurrGood() throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default int getNumOfBatteriesToRemove() throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState setWinnerCoordinatesToAddGoods(int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank)throws WrongCallerException, GeneralError, InvalidCoordinates {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState manageRemoveGoods(Trucker trucker, Bank bank) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default ArrayList<Goods> plotGoodsList(Trucker trucker) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState removeAsking(ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState removeBatteries(ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker)throws WrongCallerException, GeneralError, InvalidGoodChosen, NotEnoughGoodsChosen {throw new WrongCallerException("This method does not exist in the card ");}

    default CardState combatZone(Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default void rollDice(Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default void setCannonCoordinates(Game game, Trucker trucker, ArrayList<int[]> truckerCannonsCoordinates) throws WrongCallerException, GeneralError, InvalidComponentsException {throw new WrongCallerException("This method does not exist in the card ");}

    default void setEnginesCoordinates(Game game, Trucker trucker, ArrayList<int[]> truckerEnginesCoordinates) throws WrongCallerException, GeneralError, InvalidComponentsException {throw new WrongCallerException("This method does not exist in the card ");}

    default void findWeakestTrucker(Game game, ArrayList<ArrayList<int[]>> truckersCoordinates)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default void removeAsking(ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws WrongCallerException, GeneralError, InvalidGoodChosen, NotEnoughGoodsChosen {throw new WrongCallerException("This method does not exist in the card ");}

    default int[] getCurrentMeteor()throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default void removeBatteries(ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws WrongCallerException, GeneralError, InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries {throw new WrongCallerException("This method does not exist in the card ");}

    default void setAnswers(boolean answers) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card ");}

    default synchronized CardState removeCrew(ArrayList<int[]> coordinates) throws WrongCallerException, GeneralError, InvalidComponentsException, InvalidCoordinates {throw new WrongCallerException("This method does not exist in the card ");}
}
