package it.polimi.ingsw.Model.Visitor;

import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.Optional;

public class SetCoordinatesToRemoveElementsVisitor implements CardVisitor {

    @Override
    public Optional<Object> visit(AbandonedShip card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetCoordinatesToRemoveElements(AbandonedShip card, ArrayList<int[]> coord) {
        card.setCoordinates(coord);
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetTruckerThatHasDecided(AbandonedShip card, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(AbandonedStation card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetCoordinatesToAddElements(AbandonedStation card, ArrayList<int[]> coord) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetTruckerThatHasDecided(AbandonedStation card, Trucker trucker) {
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
    public Optional<Object> visitForRollDice(MeteorShower card, Game game) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> VisitForSetAnswers(MeteorShower card, boolean answers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(OpenSpace card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetDoubleCannonsCoordinates(OpenSpace card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Pirates card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForRollDice(Pirates card, Game game) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetChooseToGain(Pirates card, Boolean choice) {
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
    public Optional<Object> visit(Slavers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetDoubleCannonsCoordinates(Slavers card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetCoordinatesToRemoveElements(Slavers card, ArrayList<int[]> truckersCoordinatesToRemoveHumans, int numPlayer, Trucker trucker) {
        card.addTruckersCoordinatesToRemoveHumans(truckersCoordinatesToRemoveHumans, numPlayer, trucker);
        return Optional.empty();
    }

    @Override
    public Optional<Object> visitForSetChooseToGain(Slavers card, boolean choice) {
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
    public Optional<Object> visit(StarDust card) {
        return Optional.empty();
    }
}