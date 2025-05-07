package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.utilities.exceptions.*;

/**
 *
 * @param <R>
 */
public interface ParametricCardVisitor<R> {
    R visit(Card card, Object... params);

    R visitForAbandonedShip(AbandonedShip abandonedShip, Object... params) throws InvalidCoordinates, InvalidComponentsException;

    R visitForAbandonedStation(AbandonedStation abandonedStation, Object... params) throws InvalidCoordinates, InvalidPositionException, GeneralError;

    R visitForEpidemic(Epidemic epidemic, Object... params);

    R visitForMeteorShowers(MeteorShower meteorShowers, Object... params) throws GeneralError;

    R visitForOpenSpace(OpenSpace openSpace, Object... params) throws InvalidCoordinates;

    R visitForPirates(Pirates pirates, Object... params) throws GeneralError;

    R visitForPlanets(Planets planets, Object... params) throws InvalidCoordinates, InvalidPositionException, PlanetNotFreeException, GeneralError;

    R visitForSlavers(Slavers slavers, Object... params) throws InvalidCoordinates;

    R visitForSmugglers(Smugglers smugglers, Object... params) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries, GeneralError;

    R visitForStarDust(StarDust starDust, Object... params);

    R visitForWarZone(WarZone warZone, Object... params) throws InvalidGoodChosen, NotEnoughGoodsChosen, InvalidCoordinates, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries, GeneralError;
}
