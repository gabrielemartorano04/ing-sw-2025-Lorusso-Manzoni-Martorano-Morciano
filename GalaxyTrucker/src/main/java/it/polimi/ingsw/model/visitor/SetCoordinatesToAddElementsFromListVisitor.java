package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.cards.*;

/**
 *
 */
public class SetCoordinatesToAddElementsFromListVisitor implements ParametricCardVisitor {

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
    public Object visitForPlanets(Planets planets, Object... params) throws InvalidCoordinates {
        try {
            int[] coordinatesToAddGoods = (int[]) params[0];
            int positionInLoad = (int) params[1];
            Bank bank = (Bank) params[2];
            return planets.coordinatesToAddFromList(coordinatesToAddGoods,positionInLoad, bank);
        } catch (InvalidCoordinates e) {
            throw new InvalidCoordinates(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
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
    public Object visitForWarZone(WarZone warZone, Object... params) {
        return null;
    }
}