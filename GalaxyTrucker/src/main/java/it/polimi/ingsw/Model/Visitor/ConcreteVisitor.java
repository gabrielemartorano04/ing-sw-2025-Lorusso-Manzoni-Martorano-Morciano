package it.polimi.ingsw.Model.Visitor;

import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Components.Goods;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.Map;

public class ConcreteVisitor implements CardVisitor {





    @Override
    public ArrayList<Goods> visitGetGoods(AbandonedStation card) {
        return card.getGoods();
    }





    @Override
    public ArrayList<int[]> visitGetDirection(MeteorShower card) {
        return card.getDirection();
    }

    @Override
    public int[] visitGetCurrentMeteor(MeteorShower card) {
        return card.getCurrentMeteor();
    }

    @Override
    public void visitSetAnswers(MeteorShower card, boolean[] answers) {
        card.setAnswers(answers);
    }

    @Override
    public void visitSetCoordinates(OpenSpace card,ArrayList<int[]> coord, int numTruckers) {
        card.setCoordinates(coord, numTruckers);
    }


    @Override
    public int visitGetPiratesPower(Pirates card) {
        return card.getPiratesPower();
    }

    @Override
    public ArrayList<int[]> visitGetShoots(Pirates card) {
        return card.getShoots();
    }

    @Override
    public void visitSetDefeatedTruckers(Pirates card, ArrayList<Trucker> defeatedTruckers) {
        card.setDefeatedTruckers(defeatedTruckers);
    }

    @Override
    public void visitSetWinnerTrucker(Pirates card, Trucker winnerTrucker) {
        card.setWinnerTrucker(winnerTrucker);
    }

    @Override
    public ArrayList<ArrayList<Goods>> visitGetPlanets(Planets card) {
        return card.getPlanets();
    }

    @Override
    public void visitSetTruckersCoordinates(Planets card, ArrayList<int[]> coord, int numTruckers) {
        card.setTruckersCoordinates(coord, numTruckers);
    }

    @Override
    public void visitSetPlayersLanded(Planets card, int playerlanded, int numTruckers) {
        card.setPlayersLanded(playerlanded, numTruckers);
    }

    @Override
    public int visitGetSlaversPower(Slavers card) {
        return card.getSlaversPower();
    }

    @Override
    public int visitGetAstronautsLost(Slavers card) {
        return card.getAstronautsLost();
    }



    @Override
    public void visitSetDefeatedTrcukers(Slavers card, ArrayList<Trucker> defeatedTruckers) {
        card.setDefeatedTruckers(defeatedTruckers);
    }

    @Override
    public void visitSetWinnerTrucker(Slavers card, Trucker trucker) {
        card.setWinnerTrucker(trucker);
    }

    @Override
    public void visitAddTruckersCoordinatesToRemoveHumans(Slavers card, ArrayList<int[]> truckersCoordinatedToRemoveHuman, int numPlayer) {
        card.addTruckersCoordinatesToRemoveHumans(truckersCoordinatedToRemoveHuman, numPlayer);
    }

    @Override
    public void visitAddDoubleCannonsCoord(Slavers card, ArrayList<int[]> coord, int numPlayer, Trucker trucker) {
        card.addDoubleCannonsCoord(coord, numPlayer, trucker);
    }

    @Override
    public void visitSetChooseToGain(Slavers card, boolean choice) {
        card.setChooseToGain(choice);
    }

    @Override
    public int visitGetSmugglersPower(Smugglers card) {
        return card.getSmugglersPower();
    }

    @Override
    public int visitGetGoodsLost(Smugglers card) {
        return card.getGoodsLost();
    }

    @Override
    public Goods[] visitGetGoodsGained(Smugglers card) {
        return card.getGoodsGained();
    }



    @Override
    public void visitSetDefeatedTruckers(Smugglers card, ArrayList<Trucker> truckers) {
        card.setDefeatedTruckers(truckers);
    }

    @Override
    public void visitSetWinnerTrucker(Smugglers card,Trucker trucker) {
        card.setWinnerTrucker(trucker);
    }

    @Override
    public void visitSetWinnerCoordinatesToAddGoods(Smugglers card, ArrayList<int[]> coord) {
        card.setWinnerCoordinatesToAddGoods(coord);
    }

    @Override
    public void visitSetTruckersCoordinatesToRemoveGoods(Smugglers card, ArrayList<ArrayList<int[]>> coord) {
        card.setTruckersCoordinatesToRemoveGoods(coord);
    }


    @Override
    public ArrayList<Condition> visitGetConditionsOrder(WarZone card) {
        return card.getConditionsOrder();
    }

    @Override
    public Map<Condition,Effect> visitGetPenalties(WarZone card) {
        return card.getPenalties();
    }

    @Override
    public ArrayList<int[]> visitGetShoots(WarZone card) {
        return card.getShoots();
    }

    @Override
    public void visitFindWeakestTrucker(WarZone card, ArrayList<Trucker> truckers, Condition condition, ArrayList<ArrayList<int[]>> coord) {
        card.findWeakestTrucker(truckers, condition, coord);
    }
}
