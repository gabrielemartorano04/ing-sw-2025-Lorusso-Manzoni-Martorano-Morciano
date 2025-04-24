package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.model.cards.*;

/**
 *
 */
public class GetDaysVisitor implements CardVisitor {

    @Override
    public Object visit(Card card) {
        return null;
    }

    @Override
    public Object visitForAbandonedShip(AbandonedShip abandonedShip) {
        return abandonedShip.getDays();
    }

    @Override
    public Object visitForAbandonedStation(AbandonedStation abandonedStation) {
        return abandonedStation.getDays();
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
        return pirates.getDays();
    }

    @Override
    public Object visitForPlanets(Planets planets) {
        return null;
    }

    @Override
    public Object visitForSlavers(Slavers slavers) {
        return slavers.getDays();
    }

    @Override
    public Object visitForSmugglers(Smugglers smugglers) {
        return smugglers.getDays();
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
