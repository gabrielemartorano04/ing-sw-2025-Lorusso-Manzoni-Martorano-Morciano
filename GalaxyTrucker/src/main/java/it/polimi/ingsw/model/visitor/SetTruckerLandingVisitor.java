package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.PlanetNotFreeException;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;

public class SetTruckerLandingVisitor implements ParametricCardVisitor {

    @Override
    public Object visit(Card card, Object... params) {
        return null;
    }

    @Override
    public Object visitForAbandonedShip(AbandonedShip abandonedShip, Object... params) {
        Trucker trucker = (Trucker) params[0];
        boolean landing = (Boolean) params[1];
        int numPlayers = (Integer) params[2];
        return abandonedShip.setTruckerThatHasDecided(trucker, landing, numPlayers);
    }

    @Override
    public Object visitForAbandonedStation(AbandonedStation abandonedStation, Object... params) {
        Trucker trucker = (Trucker) params[0];
        boolean landing = (Boolean) params[1];
        int numPlayers = (Integer) params[2];
        Bank bank = (Bank) params[3];
        return abandonedStation.truckerLanding(trucker, landing, numPlayers, bank);
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
            int indexOfLanding = (Integer) params[0];
            Trucker trucker = (Trucker) params[1];
            int numPlayers = (Integer) params[2];
            return planets.setPlayerLanding(indexOfLanding, trucker, numPlayers);
        } catch (PlanetNotFreeException e) {
            return e;
        }
    }

    @Override
    public Object visitForSlavers(Slavers slavers, Object... params) {
        return null;
    }

    @Override
    public Object visitForSmugglers(Smugglers smugglers, Object... params) {
        return null;
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