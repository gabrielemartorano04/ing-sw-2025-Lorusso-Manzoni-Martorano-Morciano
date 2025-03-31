package it.polimi.ingsw.Model.Visitor;

import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Components.Goods;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.Map;

public interface CardVisitor {
    void visitGetDays(AbandonedShip card);
    void visitGetCredits(AbandonedShip card);
    int visitGetNumHuman(AbandonedShip card);
    void visitSetTruckerThatHasDecided(AbandonedShip card, Trucker trucker);
    void visitSetCoordinates(AbandonedShip card, ArrayList<int[]> coordinates);

    int visitGetDays(AbandonedStation card);
    int visitGetNumHuman(AbandonedStation card);
    ArrayList<Goods> visitGetGoods(AbandonedStation card);
    void visitSetTruckerThatHasDecided(AbandonedStation card, Trucker trucker);
    void visitSetCoord(AbandonedStation card, ArrayList<int[]> coordinates);

    void visit(Epidemic card);

    ArrayList<int[]> visitGetDirection(MeteorShower card);
    int[] visitGetCurrentMeteor(MeteorShower card);
    void visitSetAnswers(MeteorShower card, boolean[] answers);

    void visitSetCoordinates(OpenSpace card, ArrayList<int[]> coord, int numTruckers);

    int visitGetCredits(Pirates card);
    int visitGetDays(Pirates card);
    int visitGetPiratesPower(Pirates card);
    ArrayList<int[]> visitGetShoots(Pirates card);
    void visitSetDefeatedTruckers(Pirates card, ArrayList<Trucker> defeatedTruckers);
    void visitSetWinnerTrucker(Pirates card, Trucker winnerTrucker);

    ArrayList<ArrayList<Goods>> visitGetPlanets(Planets card);
    void visitSetTruckersCoordinates(Planets card, ArrayList<int[]> coord, int numTruckers);
    void visitSetPlayersLanded(Planets card, int playerlanded, int numTruckers);

    int visitGetSlaversPower(Slavers card);
    int visitGetAstronautsLost(Slavers card);
    int visitGetDays(Slavers card);
    int visitGetCredits(Slavers card);
    void visitSetDefeatedTrcukers(Slavers card, ArrayList<Trucker> defeatedTruckers);
    void visitSetWinnerTrucker(Slavers card, Trucker trucker);
    void visitAddTruckersCoordinatesToRemoveHumans(Slavers card,ArrayList<int[]> truckersCoordinatedToRemoveHuman, int numPlayer);
    void visitAddDoubleCannonsCoord(Slavers card, ArrayList<int[]> coord, int numPlayer, Trucker trucker);
    void visitSetChooseToGain(Slavers card, boolean choice);

    int visitGetSmugglersPower(Smugglers card);
    int visitGetGoodsLost(Smugglers card);
    Goods[] visitGetGoodsGained(Smugglers card);
    int visitGetDays(Smugglers card);
    void visitSetDefeatedTruckers(Smugglers card , ArrayList<Trucker> truckers);
    void visitSetWinnerTrucker(Smugglers card, Trucker trucker);
    void visitSetWinnerCoordinatesToAddGoods(Smugglers card, ArrayList<int[]> coord);
    void visitSetTruckersCoordinatesToRemoveGoods(Smugglers card, ArrayList<ArrayList<int[]>> coord);

    void visit(StarDust card);

    ArrayList<Condition> visitGetConditionsOrder(WarZone card);
    Map<Condition,Effect> visitGetPenalties(WarZone card);
    ArrayList<int[]> visitGetShoots(WarZone card);
    void visitFindWeakestTrucker(WarZone card, ArrayList<Trucker> truckers, Condition condition, ArrayList<ArrayList<int[]>> coord);

}
