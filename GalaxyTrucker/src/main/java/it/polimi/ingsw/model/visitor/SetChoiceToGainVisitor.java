package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.model.cards.*;

/**
 *
 */
public class SetChoiceToGainVisitor implements ParametricCardVisitor {

    @Override
    public Object visit(Card card, Object... params) {
        return null;
    }

    @Override
    public Object visitForAbandonedShip(AbandonedShip abandonedShip, Object... params) {
        return null;
    }

    @Override
    public Object visitForAbandonedStation(AbandonedStation abandonedStation, Object... params) {
        try {
            boolean choice = (Boolean) params[0];
            return abandonedStation.setChoiceToGain(choice);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }

    }

    @Override
    public Object visitForEpidemic(Epidemic epidemic, Object... params) {
        return null;
    }

    @Override
    public Object visitForMeteorShowers(MeteorShower meteorShowers, Object... params) {
        return null;
    }

    @Override
    public Object visitForOpenSpace(OpenSpace openSpace, Object... params) {
        return null;
    }

    @Override
    public Object visitForPirates(Pirates pirates, Object... params) {
        try {
            boolean choice = (Boolean) params[0];
            return pirates.setChooseToGain(choice);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }

    }

    @Override
    public Object visitForPlanets(Planets planets, Object... params) {
        try {
            boolean choice = (Boolean) params[0];
            return planets.chooseToGain(choice);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }

    }

    @Override
    public Object visitForSlavers(Slavers slavers, Object... params) {
        try {
            boolean choice = (Boolean) params[0];
            return slavers.setChooseToGain(choice);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }

    }

    @Override
    public Object visitForSmugglers(Smugglers smugglers, Object... params) {
        try {
            boolean choice = (Boolean) params[0];
            return smugglers.setChoiceToGain(choice);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public Object visitForStarDust(StarDust starDust, Object... params) {
        return null;
    }

    @Override
    public Object visitForWarZone(WarZone warZone, Object... params) {
        return null;
    }
}