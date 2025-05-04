package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;

import java.util.ArrayList;

/**
 *
 */
public class SetCoordinatesToRemoveElementsVisitor implements ParametricCardVisitor {

    @Override
    public Object visit(Card card, Object... params) {
        return null;
    }

    @Override
    public Object visitForAbandonedShip(AbandonedShip abandonedShip, Object... params) throws InvalidCoordinates, InvalidComponentsException {
        try {
            ArrayList<int[]> coordinates = (ArrayList<int[]>) params[0];
            return abandonedShip.setCoordinates(coordinates);
        } catch (InvalidCoordinates e) {
            throw new InvalidCoordinates(e.getMessage());
        } catch (InvalidComponentsException e) {
            throw new InvalidComponentsException(e.getMessage());
        }
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
    public Object visitForSlavers(Slavers slavers, Object... params) throws InvalidCoordinates {
        try {
            ArrayList<int[]> truckerCoordinatesToRemoveHuman = (ArrayList<int[]>) params[0];
            int numTrucker = (Integer) params[1];
            Trucker trucker = (Trucker) params[2];
            return slavers.addTruckersCoordinatesToRemoveHumans(trucker, truckerCoordinatesToRemoveHuman, numTrucker);
        } catch (InvalidCoordinates e) {
            throw new InvalidCoordinates(e.getMessage());
        }
    }

    @Override
    public Object visitForSmugglers(Smugglers smugglers, Object... params) {
        return null;
    }

    @Override
    public Object visitForStarDust(StarDust starDust, Object... params) {
        return null;
    }

    @Override
    public Object visitForWarZone(WarZone warZone, Object... params) throws InvalidCoordinates, InvalidComponentsException {
        try {
            ArrayList<int[]> coordinates = (ArrayList<int[]>) params[0];
            return warZone.removeCrew(coordinates);
        } catch (InvalidComponentsException e) {
            throw new InvalidComponentsException(e.getMessage());
        } catch (InvalidCoordinates e) {
            throw new InvalidCoordinates(e.getMessage());
        }
    }
}