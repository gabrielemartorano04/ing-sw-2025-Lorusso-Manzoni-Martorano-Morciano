package it.polimi.ingsw.Model.Visitor;

import it.polimi.ingsw.Model.Cards.*;

import java.util.Optional;

public class GetEnemyPowerVisitor implements CardVisitor {
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
        return Optional.of(card.getPiratesPower());
    }

    @Override
    public Optional<Object> visit(Planets card) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> visit(Slavers card) {
        return Optional.of(card.getSlaversPower());
    }

    @Override
    public Optional<Object> visit(Smugglers card) {
        return Optional.of(card.getSmugglersPower());
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
