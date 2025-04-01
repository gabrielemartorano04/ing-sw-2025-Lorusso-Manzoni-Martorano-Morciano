package it.polimi.ingsw.Model.Visitor;

import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Components.Goods;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.Map;

public class ConcreteVisitor implements CardVisitor {

    @Override
    public int visitGetSlaversPower(Slavers card) {
        return card.getSlaversPower();
    }

    @Override
    public int visitGetAstronautsLost(Slavers card) {
        return card.getAstronautsLost();
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
    public void visitFindWeakestTrucker(WarZone card, ArrayList<Trucker> truckers, Condition condition, ArrayList<ArrayList<int[]>> coord) {
        card.findWeakestTrucker(truckers, condition, coord);
    }
}
