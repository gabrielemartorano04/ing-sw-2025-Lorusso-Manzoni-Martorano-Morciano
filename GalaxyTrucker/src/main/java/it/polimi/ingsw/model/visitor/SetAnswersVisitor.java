package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.*;

public class SetAnswersVisitor implements ParametricCardVisitor {

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
        boolean answer = (boolean) params[0];
        return meteorShowers.setAnswers(answer);
    }

    @Override
    public Object visitForOpenSpace(OpenSpace openSpace, Object... params) {
        return null;
    }

    @Override
    public Object visitForPirates(Pirates pirates, Object... params) {
        boolean answers = (Boolean) params[0];
        Trucker trucker = (Trucker) params[1];
        return pirates.setAnswers(answers, trucker);
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