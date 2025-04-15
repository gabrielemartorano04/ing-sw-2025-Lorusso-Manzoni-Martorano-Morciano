package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;

import java.util.ArrayList;
import java.util.Optional;

public interface CardVisitor {
    Optional<Object> visitForAbandonedShip(AbandonedShip abandonedShip);

    Optional<Object> visitForAbandonedStation(AbandonedStation abandonedStation);

    Optional<Object> visitForEpidemic(Epidemic epidemic);

    Optional<Object> visitForMeteorShowers(MeteorShower meteorShowers);

    Optional<Object> visitForOpenSpace(OpenSpace openSpace);

    Optional<Object> visitForPirates(Pirates pirates);

    Optional<Object> visitForPlanets(Planets planets);

    Optional<Object> visitForSlavers(Slavers slavers);

    Optional<Object> visitForSmugglers(Smugglers smugglers);

    Optional<Object> visitForStarDust(StarDust starDust);

    Optional<Object> visitForWarZone(WarZone warZone);
}
