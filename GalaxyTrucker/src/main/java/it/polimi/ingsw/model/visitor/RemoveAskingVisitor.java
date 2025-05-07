package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.exceptions.InvalidGoodChosen;
import it.polimi.ingsw.utilities.exceptions.NotEnoughGoodsChosen;
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
    public Object visitForSmugglers(Smugglers smugglers, Object... params) throws InvalidGoodChosen, NotEnoughGoodsChosen {
        try {
            ArrayList<Integer> indexes = (ArrayList<Integer>) params[0];
            Trucker trucker = (Trucker) params[1];
            Bank bank = (Bank) params[2];
            return smugglers.removeAsking(indexes, trucker, bank);
        } catch (InvalidGoodChosen e) {
            throw new InvalidGoodChosen(e.getMessage());
        } catch (NotEnoughGoodsChosen e) {
            throw new NotEnoughGoodsChosen(e.getMessage());
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object visitForStarDust(StarDust starDust, Object... params) {
        return null;
    }

    @Override
    public Object visitForWarZone(WarZone warZone, Object... params) throws InvalidGoodChosen, NotEnoughGoodsChosen {
        try {
            ArrayList<Integer> indexes = (ArrayList<Integer>) params[0];
            Trucker trucker = (Trucker) params[1];
            Bank bank = (Bank) params[2];
            warZone.removeAsking(indexes, trucker, bank);
            return null;
        } catch (InvalidGoodChosen e) {
            throw new InvalidGoodChosen(e.getMessage());
        } catch (NotEnoughGoodsChosen e) {
            throw new NotEnoughGoodsChosen(e.getMessage());
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
    }
}