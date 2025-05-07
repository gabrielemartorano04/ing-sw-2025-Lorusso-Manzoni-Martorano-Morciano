package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

/**
 *
 */
public class SetDoubleCannonsCoordinatesVisitor implements ParametricCardVisitor {

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
        try {
            Trucker trucker = (Trucker) params[0];
            ArrayList<int[]> doubleCannonCoordinates = (ArrayList<int[]>) params[1];
            int numPlayer = (Integer) params[2];
            return pirates.addDoubleCannonsCoord(trucker, doubleCannonCoordinates, numPlayer);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (InvalidComponentsException e) {
            throw new RuntimeException(e);
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Object visitForPlanets(Planets planets, Object... params) {
        return null;
    }

    @Override
    public Object visitForSlavers(Slavers slavers, Object... params) {
        try {
            Trucker trucker = (Trucker) params[0];
            ArrayList<int[]> doubleCannonCoord = (ArrayList<int[]>) params[1];
            int numPlayer = (Integer) params[2];
            return slavers.addDoubleCannonsCoord(trucker, doubleCannonCoord, numPlayer);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (InvalidComponentsException e) {
            throw new RuntimeException(e);
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Object visitForSmugglers(Smugglers smugglers, Object... params) throws InvalidComponentsException {
        try {
            Trucker trucker = (Trucker) params [0];
            ArrayList<int[]> doubleCannonCoord = (ArrayList<int[]>) params[1];
            int numPlayer = (Integer) params[2];
            Bank bank = (Bank) params[3];
            return smugglers.addDoubleCannonsCoord(trucker, doubleCannonCoord, numPlayer, bank);
        } catch (InvalidComponentsException e) {
            throw new InvalidComponentsException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object visitForStarDust(StarDust starDust, Object... params) {
        return null;
    }

    @Override
    public Object visitForWarZone(WarZone warZone, Object... params) throws InvalidComponentsException {
        try {
            Game game = (Game) params[0];
            Trucker trucker = (Trucker) params[1];
            ArrayList<int[]> truckerEngineCoordinates = (ArrayList<int[]>) params[2];
            warZone.setEnginesCoordinates(game, trucker, truckerEngineCoordinates);
            return null;
        } catch (InvalidComponentsException e) {
            throw new InvalidComponentsException(e.getMessage());
        } catch (GeneralError e) {
            throw new RuntimeException(e);
        }
    }
}