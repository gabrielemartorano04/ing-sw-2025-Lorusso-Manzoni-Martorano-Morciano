package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;

/**
 *
 */
public class RearrangeVisitor implements ParametricCardVisitor {

    @Override
    public Object visit(Card card, Object... params) {
        return null;
    }

    @Override
    public Object visitForAbandonedShip(AbandonedShip abandonedShip, Object... params) {
        return null;
    }

    @Override
    public Object visitForAbandonedStation(AbandonedStation abandonedStation, Object... params) throws InvalidCoordinates, InvalidPositionException {
        try {
            Trucker trucker = (Trucker) params[0];
            int[] coord1 = (int[]) params[1];
            int[] coord2 = (int[]) params[2];
            int value1 = (Integer) params[3];
            int value2 = (Integer) params[4];
            boolean contiune = (Boolean) params[5];
            return abandonedStation.rearrange(trucker, coord1, coord2, value1, value2, contiune);
        } catch (InvalidCoordinates e) {
            throw new InvalidCoordinates(e.getMessage());
        } catch (InvalidPositionException e) {
            throw new InvalidPositionException(e.getMessage());
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
        return null;
    }

    @Override
    public Object visitForPlanets(Planets planets, Object... params) {
        try {
            int[] coord1 = (int[]) params[0];
            int[] coord2 = (int[]) params[1];
            int value1 = (Integer) params[2];
            int value2 = (Integer) params[3];
            boolean contiune = (Boolean) params[4];
            planets.rearrange(coord1, coord2, value1, value2, contiune);
            return null;
        } catch (InvalidCoordinates | InvalidPositionException e) {
            return e;
        }
    }

    @Override
    public Object visitForSlavers(Slavers slavers, Object... params) {
        return null;
    }

    @Override
    public Object visitForSmugglers(Smugglers smugglers, Object... params) {
        try {
            Trucker trucker = (Trucker) params[0];
            int[] coord1 = (int[]) params[1];
            int[] coord2 = (int[]) params[2];
            int value1 = (Integer) params[3];
            int value2 = (Integer) params[4];
            boolean contiune = (Boolean) params[5];
            return smugglers.rearrange(trucker, coord1, coord2, value1, value2, contiune);
        } catch (InvalidCoordinates | InvalidPositionException e) {
            return e;
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