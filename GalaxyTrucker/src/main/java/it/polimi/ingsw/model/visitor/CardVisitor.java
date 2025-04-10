package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;

import java.util.ArrayList;
import java.util.Optional;

public interface CardVisitor {
    Optional<Object> visit(AbandonedShip card);
    Optional<Object> visitForSetCoordinatesToRemoveElements(AbandonedShip card, ArrayList<int[]> coordinates);
    Optional<Object> visitForSetTruckerLanding(AbandonedShip card, Trucker trucker, boolean landing, int numPlayer);

    Optional<Object> visit(AbandonedStation card);
    Optional<Object> visitForSetCoordinatesToAddElements(AbandonedStation card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank);
    Optional<Object> visitForSetTruckerLanding(AbandonedStation card, Trucker trucker, boolean landing, int numPlayer, Bank bank);
    Optional<Object> visitForRearrange(AbandonedStation card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune);
    Optional<Object> visitForSetChoiceToGain(AbandonedStation card, Boolean choice);


    Optional<Object> visit(Epidemic card);

    Optional<Object> visit(MeteorShower card);
    Optional<Object> visitForRollDice(MeteorShower card, Game game);
    Optional<Object> VisitForSetAnswers(MeteorShower card, boolean answers);

    Optional<Object> visit(OpenSpace card);
    Optional<Object> visitForSetDoubleCannonsCoordinates(OpenSpace card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers);

    Optional<Object> visit(Pirates card);
    Optional<Object> visitForRollDice(Pirates card, Game game);
    Optional<Object> visitForSetChoiceToGain(Pirates card, Boolean choice);
    Optional<Object> visitForSetDefeatedTruckers(Pirates card,  ArrayList<Trucker> defeatedTruckers);
    Optional<Object> visitForSetWinnerTrucker(Pirates card, Trucker winnerTrucker);
    Optional<Object> visitForSetDoubleCannonsCoordinates(Pirates card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker);
    Optional<Object> visitForSetAnswers(Pirates card, boolean answers, Trucker trucker);

    //ANCORA IN ATTESA
    Optional<Object> visit(Planets card);
    Optional<Object> visitForGetBank(Planets card, Bank bank);
    Optional<Object> visitForSetTruckerLanding(Planets card, int indexOfLanding, Trucker trucker, int numPlayers);
    Optional<Object> visitForInitialize(Planets card, Bank bank);
    Optional<Object> visitForSetChoiceToGain(Planets card, Boolean choice);
    Optional<Object> visitForSetChoiceToGainFromList(Planets card, boolean choice);
    Optional<Object> visitForSetCoordinatesToAddElements(Planets card, int[] coordinatesToAddGood, int positionInLoad, Bank bank);
    Optional<Object> visitForSetCoordinatesToAddElementsFromList(Planets card, int[] coordinatesToAddGood, int positionInLoad, Bank bank);
    Optional<Object> visitForRearrange(Planets card, int[] coord1, int[] coord2, int value1, int value2, boolean contiune);



    Optional<Object> visit(Slavers card);
    Optional<Object> visitForSetDoubleCannonsCoordinates(Slavers card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker);
    Optional<Object> visitForSetCoordinatesToRemoveElements(Slavers card, ArrayList<int[]> truckersCoordinatesToRemoveHumans, int numPlayer, Trucker trucker);
    Optional<Object> visitForSetChoiceToGain(Slavers card, boolean choice);
    Optional<Object> visitForSetDefeatedTruckers(Slavers card, ArrayList<Trucker> defeatedTruckers);
    Optional<Object> visitForSetWinnerTrucker(Slavers card, Trucker winnerTrucker);

    Optional<Object> visit(Smugglers card);
    Optional<Object> visitForSetDefeatedTruckers(Smugglers card, ArrayList<Trucker> defeatedTruckers);
    Optional<Object> visitForSetWinnerTrucker(Smugglers card, Trucker winnerTrucker);
    Optional<Object> visitForSetDoubleCannonsCoordinates(Smugglers card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker, Bank bank);
    Optional<Object> visitForSetChoiceToGain(Smugglers card, boolean choice);
    Optional<Object> visitForSetCoordinatesToAddElements(Smugglers card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank);
    Optional<Object> visitForRearrange(Smugglers card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune);
    Optional<Object> visitForManageRemoveGoods(Smugglers card, Trucker trucker, Bank bank);
    Optional<Object> visitForRemoveWithoutAsking(Smugglers card, Trucker trucker, ArrayList<Goods> goodsList, Bank bank);
    Optional<Object> visitForPlotGoodsList(Smugglers card, Trucker trucker);
    Optional<Object> visitForRemoveAsking(Smugglers card, ArrayList<Integer> indexes, Trucker trucker, Bank bank);
    Optional<Object> visitForSetCoordinatesToRemoveElements(Smugglers card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker)

    Optional<Object> visit(StarDust card);

    Optional<Object> visit(WarZone card);
    Optional<Object> visitForCombatZone(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCannonsCoordinates, ArrayList<ArrayList<int[]>> truckersEnginesCoordinates);
    Optional<Object> visitForSetDoubleCannonsCoordinates(WarZone card, Game game, Trucker trucker, ArrayList<int[]> truckerCannonsCoordinates);
    Optional<Object> visitForSetDoubleEngineCoordinates(WarZone card, Game game, Trucker trucker, ArrayList<int[]> truckerEnginesCoordinates);
    Optional<Object> visitForFindWeakestTrucker(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCoordinates);
    Optional<Object> visitForManageRemoveGoods(WarZone card, Trucker trucker, Bank bank);
    Optional<Object> visitForRemoveWithoutAsking(WarZone card, Trucker trucker, ArrayList<Goods> goodsList, Bank bank);
    Optional<Object> visitForPlotGoodsList(WarZone card, Trucker trucker);
    Optional<Object> visitForRemoveAsking(WarZone card, Bank bank, ArrayList<Integer> indexes, Trucker trucker);
    Optional<Object> visitForSetCoordinatesToRemoveElements(WarZone card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker);
    Optional<Object> visitForRollDice(WarZone card, Game game);
    Optional<Object> visitForSetAnswers(WarZone card, boolean answers);
    Optional<Object> visitForSetCoordinatesToRemoveElements(WarZone card, ArrayList<int[]> coordinates);






}
