package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;

import java.util.ArrayList;
import java.util.Optional;

public interface CardVisitor {
    Optional<Object> visit(AbandonedShip card);
    Optional<Object> visitForGetDays(AbandonedShip card);
    Optional<Object> visitForGetCredits(AbandonedShip card);
    Optional<Object> visitForGetHumanToRemove(AbandonedShip card);
    Optional<Object> visitForSetCoordinatesToRemoveCrew(AbandonedShip card, ArrayList<int[]> coordinates) throws InvalidCoordinates;
    Optional<Object> visitForSetTruckerLanding(AbandonedShip card, Trucker trucker, boolean landing, int numPlayer);

    Optional<Object> visit(AbandonedStation card);
    Optional<Object> visitForGetDays(AbandonedStation card);
    Optional<Object> visitForGetHumanToGain(AbandonedStation card);
    Optional<Object> visitForGetGoods(AbandonedStation card);
    Optional<Object> visitForSetCoordinatesToAddGoods(AbandonedStation card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws InvalidCoordinates, InvalidPositionException;
    Optional<Object> visitForSetTruckerLanding(AbandonedStation card, Trucker trucker, boolean landing, int numPlayer, Bank bank);
    Optional<Object> visitForRearrange(AbandonedStation card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws InvalidCoordinates, InvalidPositionException;
    Optional<Object> visitForSetChoiceToGain(AbandonedStation card, Boolean choice);

    Optional<Object> visit(Epidemic card);

    Optional<Object> visit(MeteorShower card);
    Optional<Object> visitForGetDirection(MeteorShower card);
    Optional<Object> visitForGetCurrentMeteor(MeteorShower card);
    Optional<Object> visitForRollDice(MeteorShower card, Game game);
    Optional<Object> visitForSetAnswers(MeteorShower card, boolean answers);

    Optional<Object> visit(OpenSpace card);
    Optional<Object> visitForSetDoubleEnginesCoordinates(OpenSpace card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers) throws InvalidCoordinates;

    Optional<Object> visit(Pirates card);
    Optional<Object> visitForGetDays(Pirates card);
    Optional<Object> visitForGetCredits(Pirates card);
    Optional<Object> visitForGetPiratesPower(Pirates card);
    Optional<Object> visitForGetShoots(Pirates card);
    Optional<Object> visitForGetDirections(Pirates card);
    Optional<Object> visitForGetCurrentShoot(Pirates card);
    Optional<Object> visitForRollDice(Pirates card, Game game);
    Optional<Object> visitForSetChoiceToGain(Pirates card, Boolean choice);
    Optional<Object> visitForSetDefeatedTruckers(Pirates card,  ArrayList<Trucker> defeatedTruckers);
    Optional<Object> visitForSetWinnerTrucker(Pirates card, Trucker winnerTrucker);
    Optional<Object> visitForSetDoubleCannonsCoordinates(Pirates card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker);
    Optional<Object> visitForSetAnswers(Pirates card, boolean answers, Trucker trucker);

    Optional<Object> visit(Planets card);
    Optional<Object> visitForGetPlanets(Planets card);
    Optional<Object> visitForGetGoodsFromPlanets(Planets card);
    Optional<Object> visitForGetGoodsFromList(Planets card);
    Optional<Object> visitForGetBank(Planets card, Bank bank);
    Optional<Object> visitForSetTruckerLanding(Planets card, int indexOfLanding, Trucker trucker, int numPlayers);
    Optional<Object> visitForInitialize(Planets card, Bank bank);
    Optional<Object> visitForSetChoiceToGain(Planets card, Boolean choice);
    Optional<Object> visitForSetChoiceToGainFromList(Planets card, boolean choice);
    Optional<Object> visitForSetCoordinatesToAddGoods(Planets card, int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws InvalidCoordinates, InvalidPositionException;
    Optional<Object> visitForSetCoordinatesToAddGoodsFromList(Planets card, int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws InvalidCoordinates, InvalidPositionException;
    Optional<Object> visitForRearrange(Planets card, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws InvalidCoordinates, InvalidPositionException;

    Optional<Object> visit(Slavers card);
    Optional<Object> visitForGetSlaversPower(Slavers card);
    Optional<Object> visitForGetAstronautsLost(Slavers card);
    Optional<Object> visitForGetDays(Slavers card);
    Optional<Object> visitForGetCredits(Slavers card);
    Optional<Object> visitForSetDoubleCannonsCoordinates(Slavers card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker);
    Optional<Object> visitForSetCoordinatesToRemoveCrew(Slavers card, ArrayList<int[]> truckersCoordinatesToRemoveHumans, int numPlayer, Trucker trucker) throws InvalidCoordinates;
    Optional<Object> visitForSetChoiceToGain(Slavers card, boolean choice);
    Optional<Object> visitForSetDefeatedTruckers(Slavers card, ArrayList<Trucker> defeatedTruckers);
    Optional<Object> visitForSetWinnerTrucker(Slavers card, Trucker winnerTrucker);

    Optional<Object> visit(Smugglers card);
    Optional<Object> visitForGetSmugglersPower(Smugglers card);
    Optional<Object> visitForGetGoodsToRemove(Smugglers card);
    Optional<Object> visitForGetGoodsToGain(Smugglers card);
    Optional<Object> visitForGetDays(Smugglers card);
    Optional<Object> visitForGetBatteriesToRemove(Smugglers card);
    Optional<Object> visitForGetCurrentGoods(Smugglers card);
    Optional<Object> visitForSetDefeatedTruckers(Smugglers card, ArrayList<Trucker> defeatedTruckers);
    Optional<Object> visitForSetWinnerTrucker(Smugglers card, Trucker winnerTrucker);
    Optional<Object> visitForSetDoubleCannonsCoordinates(Smugglers card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker, Bank bank);
    Optional<Object> visitForSetChoiceToGain(Smugglers card, boolean choice);
    Optional<Object> visitForSetCoordinatesToAddGoods(Smugglers card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws InvalidCoordinates, InvalidPositionException;
    Optional<Object> visitForRearrange(Smugglers card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws InvalidCoordinates, InvalidPositionException;
    Optional<Object> visitForManageRemoveGoods(Smugglers card, Trucker trucker, Bank bank);
    Optional<Object> visitForGetGoodsList(Smugglers card, Trucker trucker);
    Optional<Object> visitForRemoveAsking(Smugglers card, ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws InvalidGoodChosen, NotEnoughGoodsChosen;
    Optional<Object> visitForSetCoordinatesToRemoveBatteries(Smugglers card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws InvalidCoordinates, WrongNumberOfBatteriesSelected;

    Optional<Object> visit(StarDust card);

    Optional<Object> visit(WarZone card);
    Optional<Object> visitForGetConditionsOrder(WarZone card);
    Optional<Object> visitForGetPenalties(WarZone card);
    Optional<Object> visitForGetShoots(WarZone card);
    Optional<Object> visitForGetBatteriesToRemove(WarZone card);
    Optional<Object> visitForGetCurrentMeteor(WarZone card);
    Optional<Object> visitForCombatZone(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCannonsCoordinates, ArrayList<ArrayList<int[]>> truckersEnginesCoordinates);
    Optional<Object> visitForSetDoubleCannonsCoordinates(WarZone card, Game game, Trucker trucker, ArrayList<int[]> truckerCannonsCoordinates);
    Optional<Object> visitForSetDoubleEnginesCoordinates(WarZone card, Game game, Trucker trucker, ArrayList<int[]> truckerEnginesCoordinates);
    Optional<Object> visitForFindWeakestTrucker(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCoordinates);
    Optional<Object> visitForManageRemoveGoods(WarZone card, Trucker trucker, Bank bank);
    Optional<Object> visitForGetGoodsList(WarZone card, Trucker trucker);
    Optional<Object> visitForRemoveAsking(WarZone card, Bank bank, ArrayList<Integer> indexes, Trucker trucker) throws InvalidGoodChosen, NotEnoughGoodsChosen;
    Optional<Object> visitForSetCoordinatesToRemoveBatteries(WarZone card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws InvalidCoordinates, WrongNumberOfBatteriesSelected;
    Optional<Object> visitForRollDice(WarZone card, Game game);
    Optional<Object> visitForSetAnswers(WarZone card, boolean answers);
    Optional<Object> visitForSetCoordinatesToRemoveCrew(WarZone card, ArrayList<int[]> coordinates) throws InvalidCoordinates;
}
