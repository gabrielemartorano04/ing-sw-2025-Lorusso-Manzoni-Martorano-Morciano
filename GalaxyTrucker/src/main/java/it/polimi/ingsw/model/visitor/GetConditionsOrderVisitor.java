package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.components.Goods;

import java.util.ArrayList;
import java.util.Optional;

public class GetConditionsOrderVisitor implements CardVisitor {
    @Override
    public Optional<Object> visit(AbandonedShip card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetDays(AbandonedShip card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetCredits(AbandonedShip card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetHumanToRemove(AbandonedShip card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetCoordinatesToRemoveCrew(AbandonedShip card, ArrayList<int[]> coordinates) throws InvalidCoordinates {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetTruckerLanding(AbandonedShip card, Trucker trucker, boolean landing, int numPlayer) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(AbandonedStation card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetDays(AbandonedStation card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetHumanToGain(AbandonedStation card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetGoods(AbandonedStation card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetCoordinatesToAddGoods(AbandonedStation card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws InvalidCoordinates, InvalidPositionException {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetTruckerLanding(AbandonedStation card, Trucker trucker, boolean landing, int numPlayer, Bank bank) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForRearrange(AbandonedStation card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws InvalidCoordinates, InvalidPositionException {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetChoiceToGain(AbandonedStation card, Boolean choice) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Epidemic card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(MeteorShower card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetDirection(MeteorShower card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetCurrentMeteor(MeteorShower card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForRollDice(MeteorShower card, Game game) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetAnswers(MeteorShower card, boolean answers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(OpenSpace card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetDoubleEnginesCoordinates(OpenSpace card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers) throws InvalidCoordinates {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Pirates card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetDays(Pirates card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetCredits(Pirates card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetPiratesPower(Pirates card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetShoots(Pirates card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetDirections(Pirates card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetCurrentShoot(Pirates card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForRollDice(Pirates card, Game game) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetChoiceToGain(Pirates card, Boolean choice) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetDefeatedTruckers(Pirates card, ArrayList<Trucker> defeatedTruckers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetWinnerTrucker(Pirates card, Trucker winnerTrucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetDoubleCannonsCoordinates(Pirates card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetAnswers(Pirates card, boolean answers, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Planets card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetPlanets(Planets card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetGoodsFromPlanets(Planets card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetGoodsFromList(Planets card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetBank(Planets card, Bank bank) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetTruckerLanding(Planets card, int indexOfLanding, Trucker trucker, int numPlayers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForInitialize(Planets card, Bank bank) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetChoiceToGain(Planets card, Boolean choice) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetChoiceToGainFromList(Planets card, boolean choice) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetCoordinatesToAddGoods(Planets card, int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws InvalidCoordinates, InvalidPositionException {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetCoordinatesToAddGoodsFromList(Planets card, int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws InvalidCoordinates, InvalidPositionException {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForRearrange(Planets card, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws InvalidCoordinates, InvalidPositionException {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Slavers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetSlaversPower(Slavers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetAstronautsLost(Slavers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetDays(Slavers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetCredits(Slavers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetDoubleCannonsCoordinates(Slavers card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetCoordinatesToRemoveCrew(Slavers card, ArrayList<int[]> truckersCoordinatesToRemoveHumans, int numPlayer, Trucker trucker) throws InvalidCoordinates {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetChoiceToGain(Slavers card, boolean choice) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetDefeatedTruckers(Slavers card, ArrayList<Trucker> defeatedTruckers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetWinnerTrucker(Slavers card, Trucker winnerTrucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Smugglers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetSmugglersPower(Smugglers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetGoodsToRemove(Smugglers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetGoodsToGain(Smugglers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetDays(Smugglers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetBatteriesToRemove(Smugglers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetCurrentGoods(Smugglers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetDefeatedTruckers(Smugglers card, ArrayList<Trucker> defeatedTruckers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetWinnerTrucker(Smugglers card, Trucker winnerTrucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetDoubleCannonsCoordinates(Smugglers card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker, Bank bank) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetChoiceToGain(Smugglers card, boolean choice) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetCoordinatesToAddGoods(Smugglers card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws InvalidCoordinates, InvalidPositionException {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForRearrange(Smugglers card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws InvalidCoordinates, InvalidPositionException {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForManageRemoveGoods(Smugglers card, Trucker trucker, Bank bank) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetGoodsList(Smugglers card, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForRemoveAsking(Smugglers card, ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws InvalidGoodChosen, NotEnoughGoodsChosen {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetCoordinatesToRemoveBatteries(Smugglers card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws InvalidCoordinates, WrongNumberOfBatteriesSelected {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(StarDust card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(WarZone card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetConditionsOrder(WarZone card) {
        return Optional.of(card.getConditionsOrder());
    }

    @Override
    public Optional<Object> visitForGetPenalties(WarZone card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetShoots(WarZone card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetBatteriesToRemove(WarZone card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetCurrentMeteor(WarZone card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForCombatZone(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCannonsCoordinates, ArrayList<ArrayList<int[]>> truckersEnginesCoordinates) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetDoubleCannonsCoordinates(WarZone card, Game game, Trucker trucker, ArrayList<int[]> truckerCannonsCoordinates) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetDoubleEnginesCoordinates(WarZone card, Game game, Trucker trucker, ArrayList<int[]> truckerEnginesCoordinates) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForFindWeakestTrucker(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCoordinates) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForManageRemoveGoods(WarZone card, Trucker trucker, Bank bank) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForGetGoodsList(WarZone card, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForRemoveAsking(WarZone card, Bank bank, ArrayList<Integer> indexes, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetCoordinatesToRemoveBatteries(WarZone card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForRollDice(WarZone card, Game game) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetAnswers(WarZone card, boolean answers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetCoordinatesToRemoveCrew(WarZone card, ArrayList<int[]> coordinates) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Card card) {
        return Optional.empty();
    }
}
