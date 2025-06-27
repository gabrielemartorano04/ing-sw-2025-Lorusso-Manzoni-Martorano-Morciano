package it.polimi.ingsw.net.control;

import it.polimi.ingsw.net.events.serverevents.*;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.setupevents.*;

/**
 * Interface for handling server events on the client side via visitor pattern.
 */
public interface VirtualView {

    /** Handles a PlayerJoinedEvent. */
    void visit(PlayerJoinedEvent event);

    /** Handles a NicknameAlreadyInError event. */
    void visit(NicknameAlreadyInError event);

    /** Handles an AvailableColorsEvent. */
    void visit(AvailableColorsEvent event);

    /** Handles a ColorAlreadyInError event. */
    void visit(ColorAlreadyInError event);

    /** Handles a StartBuildingEvent. */
    void visit(StartBuildingEvent event);

    /** Handles a ComponentLinkedEvent. */
    void visit(ComponentLinkedEvent event);

    /** Handles a ComponentNotAvailableError event. */
    void visit(ComponentNotAvailableError event);

    /** Handles a ComponentSelectedEvent. */
    void visit(ComponentSelectedEvent event);

    /** Handles a DiscardSuccessEvent. */
    void visit(DiscardSuccessEvent event);

    /** Handles an InvalidCoordinatesError event. */
    void visit(InvalidCoordinatesError event);

    /** Handles a TurnHourglassError event. */
    void visit(TurnHourglassError event);

    /** Handles an HourglassTurnEvent. */
    void visit(HourglassTurnEvent event);

    /** Handles a FirstConnectionEvent. */
    void visit(FirstConnectionEvent event);

    /** Handles a BuildCompleteEvent. */
    void visit(BuildCompleteEvent event);

    /** Handles the illegal ship status of the client*/
    void visit(ShipNotLegal event);

    /** Handles a KeptComponentEvent. */
    void visit(KeptComponentEvent event);

    /** Handles a TurnedComponentSuccessEvent. */
    void visit(TurnedComponentSuccessEvent event);

    /** Handles a CurrentCondition event. */
    void visit(CurrentCondition currentCondition);

    /** Handles a SuccessSelectKept event. */
    void visit(SuccessSelectKept event);

    /** Handles a ShowOtherShipResponse event. */
    void visit(ShowOtherShipResponse event);

    /** Handles a WaitOthersBuildingEvent. */
    void visit(WaitOthersBuildingEvent event);

    /** Handles an AlienCabinCoord event. */
    void visit(AlienCabinCoord event);

    /** Handles a FlightStartEvent. */
    void visit(FlightStartEvent event);

    /** Handles an EndGameEvent. */
    void visit(EndGameEvent event);

    /** Handles a TruckerWithdrawEvent. */
    void visit(TruckerWithdrawEvent event);

    /** Handles a NoAvailableSpaceError event. */
    void visit(NoAvailableSpaceError event);

    /** Handles a ReturnStackEvent. */
    void visit(ReturnStackEvent event);

    /** Handles a GenericErrorEvent. */
    void visit(GenericErrorEvent event);

    /** Handles a WaitOthersHourglassEvent. */
    void visit(WaitOthersHourglassEvent event);

    /** Handles a WaitOthersStartFlightEvent. */
    void visit(WaitOthersStartFlightEvent event);

    /** Handles a BooleanDecision event. */
    void visit(BooleanDecision event);

    /** Handles a CardFinished event. */
    void visit(CardFinished event);

    /** Handles an EpidemicEvent. */
    void visit(EpidemicEvent event);

    /** Handles a HumanCoordinates event. */
    void visit(HumanCoordinates event);

    /** Handles a PlanetDecision event. */
    void visit(PlanetDecision event);

    /** Handles a PlotGood event. */
    void visit(PlotGood event);

    /** Handles a RollDiceResult event. */
    void visit(RollDiceResult event);

    /** Handles a StarDustEvent. */
    void visit(StarDustEvent event);

    /** Handles a StartCardEvent. */
    void visit(StartCardEvent event);

    /** Handles a RearrangeEvent. */
    void visit(RearrangeEvent event);

    /** Handles a GainCosmicCredits event. */
    void visit(GainCosmicCredits event);

    /** Handles an InvalidCoordinatesEvent. */
    void visit(InvalidCoordinatesEvent event);

    /** Handles a NotEnoughBatterieEvent. */
    void visit(NotEnoughBatterieEvent event);

    /** Handles an InvalidComponentsExceptionEvent. */
    void visit(InvalidComponentsExceptionEvent event);

    /** Handles an InvalidPositionExceptionEvent. */
    void visit(InvalidPositionExceptionEvent event);

    /** Handles a PlanetNotFreeExceptionEvent. */
    void visit(PlanetNotFreeExceptionEvent event);

    /** Handles an InvalidGoodChosenEvent. */
    void visit(InvalidGoodChosenEvent event);

    /** Handles a NotEnoughGoodsChosenEvent. */
    void visit(NotEnoughGoodsChosenEvent event);

    /** Handles a WrongNumberOfBatteriesSelectedEvent. */
    void visit(WrongNumberOfBatteriesSelectedEvent event);

    /** Handles a RemotionConfirmed event. */
    void visit(RemotionConfirmed event);

    /** Handles a RemoveBatteries event. */
    void visit(RemoveBatteries event);

    /** Handles a RemoveGoods event. */
    void visit(RemoveGoods event);

    /** Handles a StackDropOk event. */
    void visit(StackDropOk event);

    /** Handles a PingEvent. */
    void visit(PingEvent event);

    /** Handles a ResumeGameEvent. */
    void visit(ResumeGameEvent resumeGameEvent);

    /** Handles a ChooseResumeEvent. */
    void visit(ChooseResumeEvent chooseResumeEvent);

    /** Handles an AvailablePlayersEvent. */
    void visit(AvailablePlayersEvent availablePlayersEvent);

    /** Handles a DrawNewCardEvent. */
    void visit(DrawNewCardEvent drawNewCardEvent);

}
