package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.utilities.exceptions.*;
import it.polimi.ingsw.utilities.results.*;

import java.util.ArrayList;

public interface CardController {
    default CardStateResult call(Epidemic card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(AbandonedShip card, Trucker trucker, boolean landing, int numplayer)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(AbandonedShip card, ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(AbandonedShip card, Game game)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(AbandonedStation card, Trucker trucker, boolean landing, int numPlayer, Bank bank) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default GoodsResult call(AbandonedStation card, boolean choice) throws GeneralError, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(AbandonedStation card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws GeneralError, InvalidCoordinates, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(AbandonedStation card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws GeneralError, InvalidCoordinates, InvalidPositionException, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(AbandonedStation card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CoordinatesResult call(MeteorShower card) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(MeteorShower card, Game game)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(MeteorShower card, boolean answer) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(OpenSpace card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers) throws InvalidCoordinates, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(OpenSpace card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Pirates card, Trucker trucker, ArrayList<int[]> doubleCannonsCoord, int numPlayer) throws GeneralError, WrongCallerException, InvalidComponentsException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Pirates card, boolean choice) throws GeneralError, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CoordinatesResult call(Pirates card) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default IntResult callRollDice(Pirates card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult callCardEffect(Pirates card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Pirates card, boolean answers, Trucker turcker) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CoordinatesResult callGetBank(Planets card, Bank bank) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult CallInitialize(Planets card, Bank bank) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Planets card, int indexOfLanding, Trucker trucker, int numPlayers) throws GeneralError, PlanetNotFreeException, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default GoodsResult call(Planets card) throws GeneralError, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Planets card, boolean choice) throws GeneralError, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Planets card, int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws GeneralError, InvalidCoordinates, WrongCallerException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Planets card, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws GeneralError, InvalidCoordinates, InvalidPositionException, WrongCallerException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Planets card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Slavers card, Trucker trucker, ArrayList<int[]> coord, int numPlayer) throws InvalidCoordinates, WrongCallerException, GeneralError, InvalidComponentsException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Slavers card, boolean choice) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Slavers card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Smugglers card, Trucker trucker, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Bank bank) throws GeneralError, InvalidComponentsException, WrongCallerException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default GoodsResult callPlotGood(Smugglers card)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default IntResult callGetBatteries(Smugglers card)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Smugglers card, boolean choice)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Smugglers card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws GeneralError, InvalidCoordinates, WrongCallerException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Smugglers card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws GeneralError, InvalidCoordinates, InvalidPositionException, WrongCallerException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Smugglers card, Trucker trucker, Bank bank)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default GoodsListResult call(Smugglers card, Trucker trucker) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Smugglers card, ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws InvalidGoodChosen, NotEnoughGoodsChosen, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Smugglers card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(Smugglers card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(StarDust card, Game game)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(WarZone card, Game game)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(WarZone card, Game game, Trucker trucker, ArrayList<int[]> coord) throws InvalidComponentsException, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCoordinates)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(WarZone card, Trucker trucker, Bank bank)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default GoodsListResult call(WarZone card, Trucker trucker) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(WarZone card, ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws InvalidGoodChosen, NotEnoughGoodsChosen, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default IntResult callGetBatteries(WarZone card)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CoordinatesResult callGetMeteor(WarZone card) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(WarZone card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws InvalidCoordinates, GeneralError, WrongNumberOfBatteriesSelected, NotEnoughBatteries, WrongCallerException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(WarZone card, boolean choice) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult call(WarZone card, ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};

}
