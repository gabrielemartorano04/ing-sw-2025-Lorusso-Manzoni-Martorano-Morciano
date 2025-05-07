package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.utilities.exceptions.GeneralError;

/**
 *
 */
public class GetGoodsVisitor implements CardVisitor, ParametricCardVisitor {

    @Override
    public Object visit(Card card) {
        return null;
    }

    @Override
    public Object visitForAbandonedShip(AbandonedShip abandonedShip) {
        return null;
    }

    @Override
    public Object visitForAbandonedStation(AbandonedStation abandonedStation) {
        return abandonedStation.getGoods();
    }

    @Override
    public Object visitForEpidemic(Epidemic epidemic) {
        return null;
    }

    @Override
    public Object visitForMeteorShowers(MeteorShower meteorShowers) {
        return null;
    }

    @Override
    public Object visitForOpenSpace(OpenSpace openSpace) {
        return null;
    }

    @Override
    public Object visitForPirates(Pirates pirates) {
        return null;
    }

    @Override
    public Object visitForPlanets(Planets planets) {
        try {
            return planets.plotGoodFromList();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Object visitForSlavers(Slavers slavers) {
        return null;
    }

    @Override
    public Object visitForSmugglers(Smugglers smugglers) {
        return null;
    }

    @Override
    public Object visitForStarDust(StarDust starDust) {
        return null;
    }

    @Override
    public Object visitForWarZone(WarZone warZone) {
        return null;
    }

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
    public Object visitForSmugglers(Smugglers smugglers, Object... params) throws GeneralError {
        Trucker trucker = (Trucker) params[0];
        return smugglers.plotGoodsList(trucker);
    }

    @Override
    public Object visitForStarDust(StarDust starDust, Object... params) {
        return null;
    }

    @Override
    public Object visitForWarZone(WarZone warZone, Object... params) throws GeneralError {
        Trucker trucker = (Trucker) params[0];
        return warZone.plotGoodsList(trucker);
    }
}