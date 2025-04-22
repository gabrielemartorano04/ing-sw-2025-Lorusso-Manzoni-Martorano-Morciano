package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.model.cards.*;

public interface ParametricCardVisitor<R, C> {
    R visit(Card card, C context);

    R visitForAbandonedShip(AbandonedShip abandonedShip, C context);

    R visitForAbandonedStation(AbandonedStation abandonedStation, C context);

    R visitForEpidemic(Epidemic epidemic, C context);

    R visitForMeteorShowers(MeteorShower meteorShowers, C context);

    R visitForOpenSpace(OpenSpace openSpace, C context);

    R visitForPirates(Pirates pirates, C context);

    R visitForPlanets(Planets planets, C context);

    R visitForSlavers(Slavers slavers, C context);

    R visitForSmugglers(Smugglers smugglers, C context);

    R visitForStarDust(StarDust starDust, C context);

    R visitForWarZone(WarZone warZone, C context);
}
