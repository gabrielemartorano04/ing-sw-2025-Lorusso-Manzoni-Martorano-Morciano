package it.polimi.ingsw.model.cardcontrollers;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.utilities.exceptions.*;
import it.polimi.ingsw.utilities.results.*;

import java.util.ArrayList;

public interface CardControllerInterface {


    // TODO: mettere lo startCard come metodo di CardController da fare in override

    // TODO: scrivo qui la mia idea della gestione delle carte.
    /**
     * Card controller diventa direttamente il visitor generico che gestisce gli eventi che arrivano in ingresso e
     * ci rida l'evento da inviare ai client. Non si prende piu in ingresso i parametri ma prende direttamente degli eventi
     */

    default CardStateResult handle(Epidemic card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(AbandonedShip card, Trucker trucker, boolean landing, int numplayer)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(AbandonedShip card, ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(AbandonedShip card, Game game)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(AbandonedStation card, Trucker trucker, boolean landing, int numPlayer, Bank bank) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default GoodsResult handle(AbandonedStation card, boolean choice) throws GeneralError, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(AbandonedStation card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws GeneralError, InvalidCoordinates, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(AbandonedStation card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws GeneralError, InvalidCoordinates, InvalidPositionException, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(AbandonedStation card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CoordinatesResult handle(MeteorShower card) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(MeteorShower card, Game game)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(MeteorShower card, boolean answer) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(OpenSpace card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers) throws InvalidCoordinates, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(OpenSpace card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Pirates card, Trucker trucker, ArrayList<int[]> doubleCannonsCoord, int numPlayer) throws GeneralError, WrongCallerException, InvalidComponentsException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Pirates card, boolean choice) throws GeneralError, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CoordinatesResult handle(Pirates card) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default IntResult callRollDice(Pirates card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult callCardEffect(Pirates card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Pirates card, boolean answers, Trucker turcker) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CoordinatesResult callGetBank(Planets card, Bank bank) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult CallInitialize(Planets card, Bank bank) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Planets card, int indexOfLanding, Trucker trucker, int numPlayers) throws GeneralError, PlanetNotFreeException, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default GoodsResult handle(Planets card) throws GeneralError, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Planets card, boolean choice) throws GeneralError, WrongCallerException{throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Planets card, int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws GeneralError, InvalidCoordinates, WrongCallerException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Planets card, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws GeneralError, InvalidCoordinates, InvalidPositionException, WrongCallerException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Planets card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Slavers card, Trucker trucker, ArrayList<int[]> coord, int numPlayer) throws InvalidCoordinates, WrongCallerException, GeneralError, InvalidComponentsException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Slavers card, boolean choice) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Slavers card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Smugglers card, Trucker trucker, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Bank bank) throws GeneralError, InvalidComponentsException, WrongCallerException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default GoodsResult callPlotGood(Smugglers card)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default IntResult callGetBatteries(Smugglers card)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Smugglers card, boolean choice)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Smugglers card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws GeneralError, InvalidCoordinates, WrongCallerException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Smugglers card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws GeneralError, InvalidCoordinates, InvalidPositionException, WrongCallerException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Smugglers card, Trucker trucker, Bank bank)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default GoodsListResult handle(Smugglers card, Trucker trucker) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Smugglers card, ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws InvalidGoodChosen, NotEnoughGoodsChosen, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Smugglers card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(Smugglers card, Game game) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(StarDust card, Game game)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(WarZone card, Game game)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(WarZone card, Game game, Trucker trucker, ArrayList<int[]> coord) throws InvalidComponentsException, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCoordinates)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(WarZone card, Trucker trucker, Bank bank)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default GoodsListResult handle(WarZone card, Trucker trucker) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(WarZone card, ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws InvalidGoodChosen, NotEnoughGoodsChosen, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default IntResult callGetBatteries(WarZone card)throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CoordinatesResult callGetMeteor(WarZone card) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(WarZone card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws InvalidCoordinates, GeneralError, WrongNumberOfBatteriesSelected, NotEnoughBatteries, WrongCallerException {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(WarZone card, boolean choice) throws WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};
    default CardStateResult handle(WarZone card, ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates, WrongCallerException, GeneralError {throw new WrongCallerException("This method does not exist in the card " + card.getName());};

}
