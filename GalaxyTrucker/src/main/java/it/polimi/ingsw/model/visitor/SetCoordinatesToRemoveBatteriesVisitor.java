package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.WrongNumberOfBatteriesSelected;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

/**
 *
 */
public class SetCoordinatesToRemoveBatteriesVisitor implements ParametricCardVisitor {

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
        return null;
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
        return null;
    }

    @Override
    public Object visitForPlanets(Planets planets, Object... params) {
        return null;
    }

    @Override
    public Object visitForSlavers(Slavers slavers, Object... params) {
        return null;
    }

    @Override
    public Object visitForSmugglers(Smugglers smugglers, Object... params) throws InvalidCoordinates, WrongNumberOfBatteriesSelected {
        try {
            ArrayList<int[]> coordinatesToRemoveBatteries = (ArrayList<int[]>) params[0];
            Trucker trucker = (Trucker) params[1];
            return smugglers.removeBatteries(coordinatesToRemoveBatteries, trucker);
        } catch (InvalidCoordinates e) {
            throw new InvalidCoordinates(e.getMessage());
        } catch (WrongNumberOfBatteriesSelected e) {
            throw new WrongNumberOfBatteriesSelected(e.getMessage());
        }
    }

    @Override
    public Object visitForStarDust(StarDust starDust, Object... params) {
        return null;
    }

    @Override
    public Object visitForWarZone(WarZone warZone, Object... params) throws InvalidCoordinates, WrongNumberOfBatteriesSelected {
        try {
            ArrayList<int[]> coordinatesToRemoveBatteries = (ArrayList<int[]>) params[0];
            Trucker trucker = (Trucker) params[1];
            warZone.removeBatteries(coordinatesToRemoveBatteries, trucker);
            return null;
        } catch (InvalidCoordinates e) {
            throw new InvalidCoordinates(e.getMessage());
        } catch (WrongNumberOfBatteriesSelected e) {
            throw new WrongNumberOfBatteriesSelected(e.getMessage());
        }
    }
}
