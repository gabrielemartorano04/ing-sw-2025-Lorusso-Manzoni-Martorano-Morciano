package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.events.serverevents.cardevents.StartCardEvent;

import java.util.*;

/**
 * All methods used to print something on the screen, implemented by the TUI or the GUI
 */
public abstract class UI {

    public abstract void showWelcome(EnumSet<PlayerColors> availableColors);

    //SETUP
    public abstract void showWelcome();
    public abstract void showInsertNumPlayers();

    public abstract void showChooseColor(EnumSet<PlayerColors> availableColors);
    public abstract void showColorTaken();
    public abstract void showNicknameTaken();
    public abstract void showPlayerJoined(String nickname);

    public abstract void showWaitingForPlayers(int currentPlayers);
    public abstract void showStartCountdown(int secondsLeft);

    public abstract void showSetupComplete();

    //BUILDING
    public abstract void showPlayers(ArrayList<String> players);
    public abstract void showBuildPhaseStart(ArrayList<Component> availableComponents);

    public abstract void showAvailableComponent(ArrayList<Component> components);
    public abstract void showKeptComponents(ArrayList<Component> kept);
    public abstract void showSelectComponent();
    public abstract void showBuildOptions(); // (link / keep / discard)
    public abstract void showDiscardComponent(Component component);

    public abstract void showComponentOptions();

    public abstract void showComponentPlaced(Component component, int x, int y);
    public abstract void showInvalidPlacement(String reason);

    public abstract void showHourglassTurned();
    public abstract void showTimeExpired();
    public abstract void showWaitForOthers();

    public abstract void showBuildComplete();

    //FLIGTH
    public abstract void showFlightPhaseStart();

    public abstract void showCardDrawn(StartCardEvent event);
    public abstract void showCardEffectStart(Card card);
    public abstract void showCardEffectEnd(Card card);

    public abstract void showTruckerMoved(String nickname, int newPosition);
    public abstract void showTruckerWithdrawn(String nickname);
    public abstract void showTruckerLostAllCrew(String nickname);

    public abstract void showGoodsGained(Goods goods);

    //END OF GAME
    public abstract void showGameEnded();
    public abstract void showScores(Map<String, Integer> finalScores);

    //ERRORS/SYSTEM
    public abstract void showError(String message);
    public abstract void showConnectionLost();
    public abstract void showInvalidCommand(String expectedFormat);

    //INPUT
    public abstract void askIndex();


    public abstract void askCoordinates();
    public abstract void showGoodsToRemove(List<Goods> goods);
    public abstract void askGoodsIndexesToRemove();
    public abstract void askPlacedComponent();

    //PLAYER DATA
    public abstract void showPlayerShipStatus(int position, int batteryCount, int humanCount, int cannonPower, int enginePower, Map<String, Integer> goodsByColor);
    public abstract void showShipLayout(String nickname, Component[][] layout);

    public abstract void showMessage(String message);
    public abstract void showTrack(Map<String, Integer> truckers, int level);

    public abstract void showBooleanOption(String message);

    public abstract void updateHourglass(int secondsLeft);

    public abstract void showStack(Stack stack);

    public abstract void showComponentSelected(Component component);

    public abstract void showCurrCondition(Condition cond);

    public abstract void showInputFormat(ParserState parserState);

}
