package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

/**
 *
 */
public class SetDoubleEnginesCoordinatesVisitor implements ParametricCardVisitor {

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
    public Object visitForOpenSpace(OpenSpace openSpace, Object... params) throws InvalidCoordinates {
        try {
            Trucker trucker = (Trucker) params[0];
            ArrayList<int[]> truckerCoordinate = (ArrayList<int[]>) params[1];
            int numTrucker = (Integer) params[2];
            return openSpace.setCoordinates(trucker, truckerCoordinate, numTrucker);
        } catch (InvalidCoordinates e) {
            throw new InvalidCoordinates(e.getMessage());
        }
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
    public Object visitForSmugglers(Smugglers smugglers, Object... params) {
        return null;
    }

    @Override
    public Object visitForStarDust(StarDust starDust, Object... params) {
        return null;
    }

    @Override
    public Object visitForWarZone(WarZone warZone, Object... params) throws InvalidComponentsException {
        try {
            Game game = (Game) params[0];
            Trucker trucker = (Trucker) params[1];
            ArrayList<int[]> truckerCannonCoordinates = (ArrayList<int[]>) params[2];
            warZone.setCannonCoordinates(game, trucker, truckerCannonCoordinates);
            return null;
        } catch (InvalidComponentsException e) {
            throw new InvalidComponentsException(e.getMessage());
        }
    }
}
