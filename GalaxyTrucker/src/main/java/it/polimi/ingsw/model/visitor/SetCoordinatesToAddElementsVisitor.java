package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

/**
 *
 */
public class SetCoordinatesToAddElementsVisitor implements ParametricCardVisitor {

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
        try {
            int[] winnerCoordinatesToAddGoods = (int[]) params[0];
            int value = (Integer) params[1];
            Trucker trucker = (Trucker) params[2];
            Bank bank = (Bank) params[3];
            return abandonedStation.setWinnerCoordinatesToAddGoods(winnerCoordinatesToAddGoods, value, trucker, bank);
        } catch (InvalidCoordinates e) {
            return e;
        }
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
        try {
            int[] coordinatesToAddGoods = (int[]) params[0];
            int positionInLoad = (int) params[1];
            Bank bank = (Bank) params[2];
            return planets.setCoordinatesToAddGoods(coordinatesToAddGoods,positionInLoad, bank);
        } catch (InvalidCoordinates e) {
            return e;
        }
    }

    @Override
    public Object visitForSlavers(Slavers slavers, Object... params) {
        return null;
    }

    @Override
    public Object visitForSmugglers(Smugglers smugglers, Object... params) {
        try {
            int[] coordinatesToAddGoods = (int[]) params[0];
            int value = (Integer) params[1];
            Trucker trucker = (Trucker) params[2];
            Bank bank = (Bank) params[3];
            return smugglers.setWinnerCoordinatesToAddGoods(coordinatesToAddGoods, value, trucker, bank);
        } catch (InvalidCoordinates e) {
            return e;
        }
    }

    @Override
    public Object visitForStarDust(StarDust starDust, Object... params) {
        return null;
    }

    @Override
    public Object visitForWarZone(WarZone warZone, Object... params) {
        return null;
    }
}