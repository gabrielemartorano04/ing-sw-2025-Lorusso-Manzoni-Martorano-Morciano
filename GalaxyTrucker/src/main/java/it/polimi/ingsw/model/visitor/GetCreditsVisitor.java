package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.model.cards.*;

/**
 *
 */
public class GetCreditsVisitor implements CardVisitor {

    @Override
    public Object visit(Card card) {
        return null;
    }

    @Override
    public Object visitForAbandonedShip(AbandonedShip abandonedShip) {
        return abandonedShip.getCredits();
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
        return pirates.getCredits();
    }

    @Override
    public Object visitForPlanets(Planets planets) {
        return null;
    }

    @Override
    public Object visitForSlavers(Slavers slavers) {
        return slavers.getCredits();
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