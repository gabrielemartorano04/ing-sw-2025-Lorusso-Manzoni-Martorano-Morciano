package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.utilities.exceptions.GeneralError;

/**
 *
 * @param <R>
 */
public interface CardVisitor<R> {
    R visit(Card card);

    R visitForAbandonedShip(AbandonedShip abandonedShip);

    R visitForAbandonedStation(AbandonedStation abandonedStation);

    R visitForEpidemic(Epidemic epidemic);

    R visitForMeteorShowers(MeteorShower meteorShowers);

    R visitForOpenSpace(OpenSpace openSpace);

    R visitForPirates(Pirates pirates) throws GeneralError;

    R visitForPlanets(Planets planets);

    R visitForSlavers(Slavers slavers);

    R visitForSmugglers(Smugglers smugglers) throws GeneralError;

    R visitForStarDust(StarDust starDust);

    R visitForWarZone(WarZone warZone);
}
