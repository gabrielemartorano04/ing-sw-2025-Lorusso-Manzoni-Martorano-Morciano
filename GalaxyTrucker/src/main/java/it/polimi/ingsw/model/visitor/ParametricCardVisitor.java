package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.*;

/**
 *
 * @param <R>
 */
public interface ParametricCardVisitor<R> {
    R visit(Card card, Object... params);

    R visitForAbandonedShip(AbandonedShip abandonedShip, Object... params) throws InvalidCoordinates, InvalidComponentsException;

    R visitForAbandonedStation(AbandonedStation abandonedStation, Object... params) throws InvalidCoordinates, InvalidPositionException;

    R visitForEpidemic(Epidemic epidemic, Object... params);

    R visitForMeteorShowers(MeteorShower meteorShowers, Object... params);

    R visitForOpenSpace(OpenSpace openSpace, Object... params) throws InvalidCoordinates;

    R visitForPirates(Pirates pirates, Object... params);

    R visitForPlanets(Planets planets, Object... params) throws InvalidCoordinates, InvalidPositionException;

    R visitForSlavers(Slavers slavers, Object... params) throws InvalidCoordinates;

    R visitForSmugglers(Smugglers smugglers, Object... params) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries;

    R visitForStarDust(StarDust starDust, Object... params);

    R visitForWarZone(WarZone warZone, Object... params) throws InvalidGoodChosen, NotEnoughGoodsChosen, InvalidCoordinates, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries;
}
