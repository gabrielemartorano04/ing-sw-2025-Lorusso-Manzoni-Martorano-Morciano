package it.polimi.ingsw.Model.Visitor;

import it.polimi.ingsw.Model.Cards.*;

import java.util.ArrayList;
import java.util.Optional;

public class SetCoordinatesVisitor implements CardVisitor{
    @Override
    public Optional<Object> visit(AbandonedShip card) {
        return Optional.empty();
    }

    public Optional<Object> visit(AbandonedShip card, ArrayList<int[]> coord){
        card.setCoordinates(coord);
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(AbandonedStation card) {
        return Optional.empty();
    }

    public Optional<Object> visit(AbandonedStation card, ArrayList<int[]> coord){
        card.setCoord(coord);
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
    public Optional<Object> visit(OpenSpace card,ArrayList<int[]> coord, int numTruckers) {
        card.setCoordinates(coord, numTruckers);
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
    public Optional<Object> visit(Planets card,ArrayList<int[]> coord, int numTruckers) {
        card.setTruckersCoordinates(coord, numTruckers);
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
}
