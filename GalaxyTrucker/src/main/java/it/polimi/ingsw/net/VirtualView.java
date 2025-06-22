package it.polimi.ingsw.net;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.*;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.setupevents.*;

import java.util.ArrayList;

public interface VirtualView {

    void visit(PlayerJoinedEvent event);

    void visit(NicknameAlreadyInError event);

    void visit(AvailableColorsEvent event);

    void visit(ColorAlreadyInError event);

    void visit(StartBuildingEvent event);

    void visit(ComponentLinkedEvent event);

    void visit(ComponentNotAvailableError event);

    void visit(ComponentSelectedEvent event);

    void visit(DiscardSuccessEvent event);

    void visit(InvalidCoordinatesError event);

    void visit(TurnHourglassError event);

    void visit(HourglassTurnEvent event);

    void visit(FirstConnectionEvent event);

    void visit(BuildCompleteEvent event);

    void visit(KeptComponentEvent event);

    void visit(TurnedComponentSuccessEvent event);

    void visit(CurrentCondition currentCondition);

    void visit(SuccessSelectKept event);

    void visit(ShowOtherShipResponse event);

    void visit(WaitOthersBuildingEvent event);

    void visit(AlienCabinCoord event);

    void visit(FlightStartEvent event);

    void visit(EndGameEvent event);

    void visit(TruckerWithdrawEvent event);

    void visit(GenericErrorInsertedData event);

    void visit(NoAvailableSpaceError event);

    void visit(ReturnStackEvent event);

    void visit(GenericErrorEvent event);

    void visit(WaitOthersStartFlightEvent event);

    void visit(BooleanDecision event);

    void visit(CardFinished event);

    void visit(EpidemicEvent event);

    void visit(HumanCoordinates event);

    void visit(PlanetDecision event);

    void visit(PlotGood event);

    void visit(RollDiceResult event);

    void visit(StarDustEvent event);

    void visit(StartCardEvent event);

    void visit(RearrangeEvent event);

    void visit(GainCosmicCredits event);

    void visit(InvalidCoordinatesEvent event);

    void visit(NotEnoughBatterieEvent event);

    void visit(InvalidComponentsExceptionEvent event);

    void visit(InvalidPositionExceptionEvent event);

    void visit(PlanetNotFreeExceptionEvent event);

    void visit(InvalidGoodChosenEvent event);

    void visit(NotEnoughGoodsChosenEvent event);

    void visit(WrongNumberOfBatteriesSelectedEvent event);

    void visit(RemotionConfirmed event);

    void visit(RemoveBatteries event);

    void visit(RemoveGoods event);

    void visit(StackDropOk event);

    void visit(PingEvent event);

    void visit(ResumeGameEvent resumeGameEvent);

    void visit(ChooseResumeEvent chooseResumeEvent);

    void visit(AvailablePlayersEvent availablePlayersEvent);

    void handleDeath(ArrayList<Trucker> truckersRemoved);

    void visit(DrawNewCardEvent drawNewCardEvent);
}
