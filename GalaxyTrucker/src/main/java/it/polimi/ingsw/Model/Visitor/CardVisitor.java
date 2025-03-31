package it.polimi.ingsw.Model.Visitor;

import it.polimi.ingsw.Model.Cards.*;

public interface CardVisitor {
    void visit(AbandonedShip card);
    void visit(AbandonedStation card);
    void visit(Epidemic card);
    void visit(MeteorShower card);
    void visit(OpenSpace card);
    void visit(Pirates card);
    void visit(Planets card);
    void visit(Slavers card);
    void visit(Smugglers card);
    void visit(StarDust card);
    void visit(WarZone card);
}
