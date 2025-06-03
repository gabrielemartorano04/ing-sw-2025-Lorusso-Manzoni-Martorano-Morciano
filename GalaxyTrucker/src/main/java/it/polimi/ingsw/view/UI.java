package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.StartCardEvent;
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

    void showInsertNumPlayers();

    void welcomeScene(AvailableColorsEvent event);

    void nickNameAlreadyTaken(NicknameAlreadyInError event);

    void colorAlreadyTaken(ColorAlreadyInError event);

    void startBuildingScene(StartBuildingEvent event);

    void componentLinked(ComponentLinkedEvent event);

    void componentNotAvailable(ComponentNotAvailableError event, ViewFlow viewFlow);

    void componentSelected(ComponentSelectedEvent event, ViewFlow viewFlow);

    void invalidCoordinates(InvalidCoordinatesError event);

    void invalidPosition(InvalidPositionError event);

    void turnHourGlassError(TurnHourglassError event);


    // fine omologazione GUI TUI
    // (penso che tutti gli altri metodi che seguono possonano diventare privati di GUI o TUI
    // e non dover essere ereditati da questa interface)



    void showWelcome(EnumSet<PlayerColors> availableColors);

    //SETUP
    void showWelcome();

    void showChooseColor(EnumSet<PlayerColors> availableColors);
    void showColorTaken();
    void showNicknameTaken();
    void showPlayerJoined(String nickname);

    void showWaitingForPlayers(int currentPlayers);
    void showStartCountdown(int secondsLeft);

    void showSetupComplete();

    //BUILDING
    void showPlayers(ArrayList<String> players);
    void showBuildPhaseStart(ArrayList<Component> availableComponents);

    void showAvailableComponent(ArrayList<Component> components);
    void showKeptComponents(ArrayList<Component> kept);
    void showSelectComponent();
    void showBuildOptions(); // (link / keep / discard)
    void showDiscardComponent(Component component);

    void showComponentOptions();

    void showComponentPlaced(Component component, int x, int y);
    void showInvalidPlacement(String reason);

    void showHourglassTurned();
    void showTimeExpired();
    void showWaitForOthers();

    void showBuildComplete();

    //FLIGTH
    void showFlightPhaseStart();

    void showCardDrawn(StartCardEvent event);
    void showCardEffectStart(Card card);
    void showCardEffectEnd(Card card);

    void showTruckerMoved(String nickname, int newPosition);
    void showTruckerWithdrawn(String nickname);
    void showTruckerLostAllCrew(String nickname);

    void showGoodsGained(Goods goods);

    //END OF GAME
    void showGameEnded();
    void showScores(Map<String, Integer> finalScores);

    //ERRORS/SYSTEM
    void showError(String message);
    void showConnectionLost();
    void showInvalidCommand(String expectedFormat);

    //INPUT
    void askIndex();


    void askCoordinates();
    void showGoodsToRemove(List<Goods> goods);
    void askGoodsIndexesToRemove();
    void askPlacedComponent();

    //PLAYER DATA
    void showPlayerShipStatus(int position, int batteryCount, int humanCount, float cannonPower, int enginePower, Map<String, Integer> goodsByColor);
    void showShipLayout(String nickname, Component[][] layout);

    void showMessage(String message);
    void showTrack(Map<String, Integer> truckers, int level);

    void showBooleanOption(String message);

    void updateHourglass(int secondsLeft);

    void showStack(Stack stack);

    void showComponentSelected(Component component);

    void showCurrCondition(Condition cond);

    void showInputFormat(ParserState parserState);

}
