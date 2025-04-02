package it.polimi.ingsw.Model.Visitor;

import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.Optional;

public class GetPenaltiesVisitor implements CardVisitor {
    @Override
    public Optional<Object> visit(AbandonedShip card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(AbandonedShip card, ArrayList<int[]> coord) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(AbandonedShip card, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(AbandonedStation card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(AbandonedStation card, ArrayList<int[]> coord) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(AbandonedStation card, Trucker trucker) {
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
    public Optional<Object> visit(MeteorShower card, Game game) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(MeteorShower card, boolean answers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(OpenSpace card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(OpenSpace card, ArrayList<int[]> coord, int numTruckers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Pirates card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Pirates card, Game game) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Pirates card, boolean activateShield) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Pirates card, Boolean choice) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Pirates card, ArrayList<Trucker> defeatedTruckers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Pirates card, Trucker winnerTrucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Planets card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Planets card, ArrayList<int[]> coord, int numTruckers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Planets card, int indexOfLanding, Trucker trucker, int numPlayers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Slavers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Slavers card, ArrayList<int[]> coord, int numPlayer, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Slavers card, ArrayList<int[]> truckersCoordinatedToRemoveHuman, int numPlayer) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Slavers card, boolean choice) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Slavers card, ArrayList<Trucker> defeatedTruckers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Slavers card, Trucker winnerTrucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Smugglers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Smugglers card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Smugglers card, String cmd, ArrayList<int[]> truckersCoordinatesToRemove, int numPlayer, Trucker trucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Smugglers card, Boolean choice) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Smugglers card, ArrayList<Trucker> defeatedTruckers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Smugglers card, ArrayList<int[]> coord) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Smugglers card, Trucker winnerTrucker) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(StarDust card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(WarZone card) {
        return Optional.of(card.getConditionsOrder());
    }

    @Override
    public Optional<Object> visit(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCannonsCoordinates, ArrayList<ArrayList<int[]>> truckersEnginesCoordinates) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(WarZone card, ArrayList<Trucker> truckers, Condition condition, ArrayList<ArrayList<int[]>> coord) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(WarZone card, Game game) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(WarZone card, boolean answers) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(WarZone card, ArrayList<int[]> coordinatesGoods, ArrayList<int[]> coordinatesCrew) {
        return Optional.empty();
    }
}
