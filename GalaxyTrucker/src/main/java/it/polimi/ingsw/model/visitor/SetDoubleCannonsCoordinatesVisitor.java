package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

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
        ArrayList<int[]> doubleCannonCoordinates = (ArrayList<int[]>) params[0];
        int numPlayer = (Integer) params[1];
        Trucker trucker = (Trucker) params[2];
        return pirates.addDoubleCannonsCoord(doubleCannonCoordinates, numPlayer, trucker);
    }

    @Override
    public Object visitForPlanets(Planets planets, Object... params) {
        return null;
    }

    @Override
    public Object visitForSlavers(Slavers slavers, Object... params) {
        ArrayList<int[]> doubleCannonCoord = (ArrayList<int[]>) params[0];
        int numPlayer = (Integer) params[1];
        Trucker trucker = (Trucker) params[2];
        return slavers.addDoubleCannonsCoord(doubleCannonCoord, numPlayer, trucker);
    }

    @Override
    public Object visitForSmugglers(Smugglers smugglers, Object... params) {
        try {
            ArrayList<int[]> doubleCannonCoord = (ArrayList<int[]>) params[0];
            int numPlayer = (Integer) params[1];
            Trucker trucker = (Trucker) params [2];
            Bank bank = (Bank) params[3];
            return smugglers.addDoubleCannonsCoord(doubleCannonCoord, numPlayer, trucker, bank);
        } catch (InvalidComponentsException e) {
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
            Game game = (Game) params[0];
            Trucker trucker = (Trucker) params[1];
            ArrayList<int[]> truckerEngineCoordinates = (ArrayList<int[]>) params[2];
            warZone.setEnginesCoordinates(game, trucker, truckerEngineCoordinates);
            return null;
        } catch (InvalidComponentsException e) {
            return e;
        }
    }
}