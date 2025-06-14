package it.polimi.ingsw.net;

import it.polimi.ingsw.net.events.serverevents.*;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.flightevents.*;
import it.polimi.ingsw.net.events.serverevents.setupevents.*;

import java.rmi.RemoteException;

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

    void visit(InvalidPositionError event);

    void visit(TurnHourglassError event);

    void visit(HourglassTurnEvent event) throws RemoteException;

    void visit(FirstConnectionEvent event) throws RemoteException;

    void visit(BuildCompleteEvent event) throws RemoteException;

    void visit(KeptComponentEvent event) throws RemoteException;

    void visit(TurnedComponentSuccessEvent event) throws RemoteException;

    void visit(CurrentCondition currentCondition) throws RemoteException;

    void visit(SuccessSelectKept event) throws RemoteException;

    void visit(ShowOtherShipResponse event) throws RemoteException;

    void visit(WaitOthersBuildingEvent event) throws RemoteException;

    void visit(AlienCabinCoord event) throws RemoteException;

    void visit(FlightStartEvent event) throws RemoteException;

    void visit(EndGameEvent event) throws RemoteException;

    void visit(TruckerWithdrawEvent event) throws RemoteException;

    void visit(GenericErrorInsertedData event) throws RemoteException;

    void visit(NoAvailableSpaceError event) throws RemoteException;

    void visit(ReturnStackEvent event) throws RemoteException;

    void visit(GenericErrorEvent event) throws RemoteException;

    void visit(WaitOthersStartFlightEvent event) throws RemoteException;

    void visit(BooleanDecision event) throws RemoteException;

    void visit(CardFinished event) throws RemoteException;

    void visit(EpidemicEvent event) throws RemoteException;

    void visit(HumanCoordinates event) throws RemoteException;

    void visit(PlanetDecision event) throws RemoteException;

    void visit(PlotGood event) throws RemoteException;

    void visit(RollDiceResult event) throws RemoteException;

    void visit(StarDustEvent event) throws RemoteException;

    void visit(StartCardEvent event) throws RemoteException;

    void visit(RearrangeEvent event) throws RemoteException;

    void visit(GainCosmicCredits event) throws RemoteException;

    void visit(InvalidCoordinatesEvent event) throws RemoteException;

    void visit(NotEnoughBatterieEvent event) throws RemoteException;

    void visit(InvalidComponentsExceptionEvent event) throws RemoteException;

    void visit(InvalidPositionExceptionEvent event) throws RemoteException;

    void visit(PlanetNotFreeExceptionEvent event) throws RemoteException;

    void visit(InvalidGoodChosenEvent event) throws RemoteException;

    void visit(NotEnoughGoodsChosenEvent event) throws RemoteException;

    void visit(WrongNumberOfBatteriesSelectedEvent event) throws RemoteException;

    void visit(RemotionConfirmed event) throws RemoteException;

    void visit(RemoveBatteries event) throws RemoteException;

    void visit(RemoveGoods event) throws RemoteException;

    void visit(StackDropOk event) throws RemoteException;

    void visit(PingEvent event);

    void visit(ResumeGameEvent resumeGameEvent);

    void visit(ChooseResumeEvent chooseResumeEvent);

    void visit(AvailablePlayersEvent availablePlayersEvent);

    void visit(HumanRemovedConfirmed event);

}
