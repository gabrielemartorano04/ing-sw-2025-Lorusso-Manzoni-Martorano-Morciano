package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.events.serverevents.AlienCabinCoord;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.flightevents.FlightStartEvent;
import it.polimi.ingsw.net.events.serverevents.flightevents.InvalidPositionError;
import it.polimi.ingsw.net.events.serverevents.setupevents.AvailableColorsEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.ColorAlreadyInError;
import it.polimi.ingsw.net.events.serverevents.setupevents.NicknameAlreadyInError;

import java.util.*;

/**
 * All methods used to print something on the screen, implemented by the TUI or the GUI
 */
public interface UI {

    // omologazione GUI TUI

    void welcomeOldScene(ArrayList<PlayerColors> colors, ArrayList<String> nicknames);


    // WELCOME + CREATION

    void showInsertNumPlayers();

    void welcomeScene(AvailableColorsEvent event);

    void nickNameAlreadyTaken(NicknameAlreadyInError event);

    void colorAlreadyTaken(ColorAlreadyInError event);

    // BUILDING

    void startBuildingScene(StartBuildingEvent event);

    void componentLinked(ComponentLinkedEvent event);

    void componentNotAvailable(ComponentNotAvailableError event);

    void componentSelected(ComponentSelectedEvent event);

    void invalidCoordinates(InvalidCoordinatesError event);

    void invalidPosition(InvalidPositionError event);

    void turnHourGlassError(TurnHourglassError event);

    void turnHourGlass(HourglassTurnEvent event);

    void buildingComplete(BuildCompleteEvent event);

    void keptComponent(KeptComponentEvent event);

    void componentTurned(TurnedComponentSuccessEvent event);

    void keptSelected(SuccessSelectKept event);

    void askAlienCabin(AlienCabinCoord event);

    void showStack(Stack stack);

    void dropStack(StackDropOk event);

    void refreshAvailableComponents();

    // FLIGHT

    void flightScene(FlightStartEvent event);

    void showCurrCondition(CurrentCondition event);

    void cardFinished(CardFinished event);

    void epidemic(EpidemicEvent event);

    void showInputFormat(ParserState parserState);

    void stardust(StarDustEvent event);

    void showError(String message);

    void showTruckerWithdrawn(String nickname);

    void showResult(RollDiceResult event);

    void showDead();

    void updatePlanetsOccupied(PlanetDecision event);

    void showTrack(Map<String, Integer> truckers, int level);


    // Usati in piu scene

    void showMessage(String message);

    // fine metodi usati in piu scene

    //END GAME

    void showGameEnded();

    void showScores(Map<String, Integer> finalScores);





    // fine omologazione GUI TUI
    // (penso che tutti gli altri metodi che seguono possonano diventare privati di GUI o TUI
    // e non dover essere ereditati da questa interface)


    //SETUP
    void showPlayerJoined(String nickname);


    //BUILDING
    void showAvailableComponent(ArrayList<Component> components);
    void showKeptComponents(ArrayList<Component> kept);
    void showBuildOptions(); // (link / keep / discard)
    void showDiscardComponent(Component component);

    void showInvalidPlacement(String reason);

    // strano che non venga usato time expired
    void showTimeExpired();

    void showWaitForOthers();

    void showBuildComplete();

    void showShipLayout(String nickname, Component[][] layout);

    void showContinue();

    //FLIGTH
    void showCardDrawn(StartCardEvent event);

    void showStardustCard(StarDustEvent event);
    void updateCurrGood(Goods good);
    void showEpidemicCard(EpidemicEvent event);
    void showGoodsGained(Goods goods);
    //INPUT


    //PLAYER DATA
    void showPlayerShipStatus(int position, int batteryCount, int humanCount, float cannonPower, int enginePower, Map<String, Integer> goodsByColor, int credits);

    //NON ELIMINARE
    void showotherShipLayout(String nickname, Component[][] layout);



}
