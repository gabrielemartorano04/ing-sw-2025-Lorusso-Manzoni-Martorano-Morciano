package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.InvalidGoodChosen;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.exceptions.NotEnoughGoodsChosen;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

/**
 *
 */
public class RemoveAskingVisitor implements ParametricCardVisitor {

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
        return null;
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
        return null;
    }

    @Override
    public Object visitForSlavers(Slavers slavers, Object... params) {
        return null;
    }

    @Override
    public Object visitForSmugglers(Smugglers smugglers, Object... params) {
        try {
            ArrayList<Integer> indexes = (ArrayList<Integer>) params[0];
            Trucker trucker = (Trucker) params[1];
            Bank bank = (Bank) params[2];
            return smugglers.removeAsking(indexes, trucker, bank);
        } catch (InvalidGoodChosen | NotEnoughGoodsChosen e) {
            return e;
        }
    }

    @Override
    public Object visitForStarDust(StarDust starDust, Object... params) {
        return null;
    }

    @Override
    public Object visitForWarZone(WarZone warZone, Object... params) {
        try {
            ArrayList<Integer> indexes = (ArrayList<Integer>) params[0];
            Trucker trucker = (Trucker) params[1];
            Bank bank = (Bank) params[2];
            warZone.removeAsking(indexes, trucker, bank);
            return null;
        } catch (InvalidGoodChosen | NotEnoughGoodsChosen e) {
            return e;
        }
    }
}