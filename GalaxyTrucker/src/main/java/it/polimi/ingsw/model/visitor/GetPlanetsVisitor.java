package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.model.cards.*;

/**
 *
 */
public class GetPlanetsVisitor implements CardVisitor {

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
        return null;
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
        return planets.getPlanets();
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
}