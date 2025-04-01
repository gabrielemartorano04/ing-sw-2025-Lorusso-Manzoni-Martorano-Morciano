package it.polimi.ingsw.Model.Visitor;

import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Components.Goods;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public interface CardVisitor {
    Optional<Object> visit(AbandonedShip card);
    Optional<Object> visit(AbandonedStation card);
    Optional<Object> visit(Epidemic card);
    Optional<Object> visit(MeteorShower card);
    Optional<Object> visit(OpenSpace card);
    Optional<Object> visit(Pirates card);
    Optional<Object> visit(Planets card);
    Optional<Object> visit(Slavers card);
    Optional<Object> visit(Smugglers card);
    Optional<Object> visit(StarDust card);
    Optional<Object> visit(WarZone card);
}
