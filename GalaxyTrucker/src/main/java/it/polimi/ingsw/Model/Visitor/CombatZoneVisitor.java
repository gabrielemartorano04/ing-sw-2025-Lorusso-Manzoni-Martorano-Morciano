package it.polimi.ingsw.Model.Visitor;

import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Game;

import java.util.ArrayList;
import java.util.Optional;

public class CombatZoneVisitor implements CardVisitor {
    @Override
    public Optional<Object> visit(AbandonedShip card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(AbandonedStation card) {
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
    public Optional<Object> visit(OpenSpace card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Pirates card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Planets card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Slavers card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Smugglers card) {
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
    public Optional<Object> visit(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCannonsCoordinates, ArrayList<ArrayList<int[]>> truckersEnginesCoordinates) {
        card.combatZone(game, truckersCannonsCoordinates, truckersEnginesCoordinates);
        return Optional.empty();
    }
}
