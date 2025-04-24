package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.model.cards.*;

/**
 *
 * @param <R>
 */
public interface ParametricCardVisitor<R> {
    R visit(Card card, Object... params);

    R visitForAbandonedShip(AbandonedShip abandonedShip, Object... params);

    R visitForAbandonedStation(AbandonedStation abandonedStation, Object... params);

    R visitForEpidemic(Epidemic epidemic, Object... params);

    R visitForMeteorShowers(MeteorShower meteorShowers, Object... params);

    R visitForOpenSpace(OpenSpace openSpace, Object... params);

    R visitForPirates(Pirates pirates, Object... params);

    R visitForPlanets(Planets planets, Object... params);

    R visitForSlavers(Slavers slavers, Object... params);

    R visitForSmugglers(Smugglers smugglers, Object... params);

    R visitForStarDust(StarDust starDust, Object... params);

    R visitForWarZone(WarZone warZone, Object... params);
}
