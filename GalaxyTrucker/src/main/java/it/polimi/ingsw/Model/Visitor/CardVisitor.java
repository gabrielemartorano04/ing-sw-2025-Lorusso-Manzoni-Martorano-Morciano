package it.polimi.ingsw.Model.Visitor;

import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Components.Goods;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public interface CardVisitor {
    Optional<Object> visit(AbandonedShip card);
    Optional<Object> visit(AbandonedShip card, ArrayList<int[]> coord);
    Optional<Object> visit(AbandonedShip card, Trucker trucker);

    Optional<Object> visit(AbandonedStation card);
    Optional<Object> visit(AbandonedStation card, ArrayList<int[]> coord);
    Optional<Object> visit(AbandonedStation card, Trucker trucker);

    Optional<Object> visit(Epidemic card);

    Optional<Object> visit(MeteorShower card);
    Optional<Object> visit(MeteorShower card, Game game);
    Optional<Object> visit(MeteorShower card, boolean answers);

    Optional<Object> visit(OpenSpace card);
    Optional<Object> visit(OpenSpace card,ArrayList<int[]> coord, int numTruckers);

    Optional<Object> visit(Pirates card);
    Optional<Object> visit(Pirates card, Game game);
    Optional<Object> visit(Pirates card, boolean activateShield);
    Optional<Object> visit(Pirates card, Boolean choice);
    Optional<Object> visit(Pirates card,  ArrayList<Trucker> defeatedTruckers);
    Optional<Object> visit(Pirates card, Trucker winnerTrucker);

    Optional<Object> visit(Planets card);
    Optional<Object> visit(Planets card,ArrayList<int[]> coord, int numTruckers);
    Optional<Object> visit(Planets card, int indexOfLanding, Trucker trucker, int numPlayers);

    Optional<Object> visit(Slavers card);
    Optional<Object> visit(Slavers card, ArrayList<int[]> coord, int numPlayer, Trucker trucker);
    Optional<Object> visit(Slavers card, ArrayList<int[]> truckersCoordinatedToRemoveHuman, int numPlayer);
    Optional<Object> visit(Slavers card, boolean choice);
    Optional<Object> visit(Slavers card,  ArrayList<Trucker> defeatedTruckers);
    Optional<Object> visit(Slavers card, Trucker winnerTrucker);


    Optional<Object> visit(Smugglers card);
    Optional<Object> visit(Smugglers card, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker);
    Optional<Object> visit(Smugglers card, String cmd, ArrayList<int[]> truckersCoordinatesToRemove, int numPlayer, Trucker trucker);
    Optional<Object> visit(Smugglers card, Boolean choice);
    Optional<Object> visit(Smugglers card,  ArrayList<Trucker> defeatedTruckers);
    Optional<Object> visit(Smugglers card, ArrayList<int[]> coord);
    Optional<Object> visit(Smugglers card, Trucker winnerTrucker);

    Optional<Object> visit(StarDust card);

    Optional<Object> visit(WarZone card);
    Optional<Object> visit(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCannonsCoordinates, ArrayList<ArrayList<int[]>> truckersEnginesCoordinates);
    Optional<Object> visit(WarZone card, ArrayList<Trucker> truckers, Condition condition, ArrayList<ArrayList<int[]>> coord);
    Optional<Object> visit(WarZone card, Game game);
    Optional<Object> visit(WarZone card, boolean answers);
    Optional<Object> visit(WarZone card, ArrayList<int[]> coordinatesGoods, ArrayList<int[]> coordinatesCrew);
}
