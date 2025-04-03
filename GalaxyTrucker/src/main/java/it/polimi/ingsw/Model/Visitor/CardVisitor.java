package it.polimi.ingsw.Model.Visitor;

import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.Optional;

public interface CardVisitor {
    Optional<Object> visit(AbandonedShip card);
    Optional<Object> visitForSetCoordinatesToRemoveElements(AbandonedShip card, ArrayList<int[]> coord);
    Optional<Object> visitForSetTruckerThatHasDecided(AbandonedShip card, Trucker trucker);

    Optional<Object> visit(AbandonedStation card);
    Optional<Object> visitForSetCoordinatesToAddElements(AbandonedStation card, ArrayList<int[]> coord);
    Optional<Object> visitForSetTruckerThatHasDecided(AbandonedStation card, Trucker trucker);

    Optional<Object> visit(Epidemic card);

    Optional<Object> visit(MeteorShower card);
    Optional<Object> visitForRollDice(MeteorShower card, Game game);
    Optional<Object> VisitForSetAnswers(MeteorShower card, boolean answers);

    Optional<Object> visit(OpenSpace card);
    Optional<Object> visitForSetDoubleCannonsCoordinates(OpenSpace card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers);

    Optional<Object> visit(Pirates card);
    Optional<Object> visitForRollDice(Pirates card, Game game);
    Optional<Object> visitForSetChooseToGain(Pirates card, Boolean choice);
    Optional<Object> visitForSetDefeatedTruckers(Pirates card,  ArrayList<Trucker> defeatedTruckers);
    Optional<Object> visitForSetWinnerTrucker(Pirates card, Trucker winnerTrucker);
    Optional<Object> visitForSetDoubleCannonsCoordinates(Pirates card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker);
    Optional<Object> visitForSetAnswers(Pirates card, boolean answers, Trucker trucker);

    //ANCORA IN ATTESA
    Optional<Object> visit(Planets card);
    Optional<Object> visit(Planets card,ArrayList<int[]> coord, int numTruckers);
    Optional<Object> visit(Planets card, int indexOfLanding, Trucker trucker, int numPlayers);


    Optional<Object> visit(Slavers card);
    Optional<Object> visitForSetDoubleCannonsCoordinates(Slavers card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker);
    Optional<Object> visitForSetCoordinatesToRemoveElements(Slavers card, ArrayList<int[]> truckersCoordinatesToRemoveHumans, int numPlayer, Trucker trucker);
    Optional<Object> visitForSetChooseToGain(Slavers card, boolean choice);
    Optional<Object> visitForSetDefeatedTruckers(Slavers card,  ArrayList<Trucker> defeatedTruckers);
    Optional<Object> visitForSetWinnerTrucker(Slavers card, Trucker winnerTrucker);

    //ANCORA IN ATTESA
    Optional<Object> visit(Smugglers card);
    Optional<Object> visit(Smugglers card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker);
    Optional<Object> visit(Smugglers card, String cmd, ArrayList<int[]> truckersCoordinatesToRemove, int numPlayer, Trucker trucker);
    Optional<Object> visit(Smugglers card, Boolean choice);
    Optional<Object> visit(Smugglers card,  ArrayList<Trucker> defeatedTruckers);
    Optional<Object> visit(Smugglers card, ArrayList<int[]> coord);
    Optional<Object> visit(Smugglers card, Trucker winnerTrucker);

    Optional<Object> visit(StarDust card);

    //ANCORA IN ATTESA
    Optional<Object> visit(WarZone card);
    Optional<Object> visit(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCannonsCoordinates, ArrayList<ArrayList<int[]>> truckersEnginesCoordinates);
    Optional<Object> visit(WarZone card, ArrayList<Trucker> truckers, Condition condition, ArrayList<ArrayList<int[]>> coord);
    Optional<Object> visit(WarZone card, Game game);
    Optional<Object> visit(WarZone card, boolean answers);
    Optional<Object> visit(WarZone card, ArrayList<int[]> coordinatesGoods, ArrayList<int[]> coordinatesCrew);
}
