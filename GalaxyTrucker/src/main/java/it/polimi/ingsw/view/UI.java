package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Goods;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * All methods used to print something on the screen, implemented by the TUI or the GUI
 */
public abstract class UI {

    //SETUP
    public abstract void showWelcome();

    public abstract void showInsertNickname();
    public abstract void showChooseColor(List<String> availableColors);
    public abstract void showColorTaken();
    public abstract void showNicknameTaken();
    public abstract void showPlayerJoined(String nickname);

    public abstract void showWaitingForPlayers(int currentPlayers);
    public abstract void showStartCountdown(int secondsLeft);

    public abstract void showSetupComplete();

    //BUILDING
    public abstract void showBuildPhaseStart();

    public abstract void showAvailableComponents(List<Component> stack);
    public abstract void showKeptComponents(List<Component> kept);
    public abstract void showSelectComponent();
    public abstract void showBuildOptions(); // (link / keep / discard)

    public abstract void showComponentPlaced(Component component, int x, int y);
    public abstract void showInvalidPlacement(String reason);

    public abstract void showHourglassTurned();
    public abstract void showTimeExpired();
    public abstract void showWaitForOthers();

    public abstract void showBuildComplete();

    //FLIGTH
    public abstract void showFlightPhaseStart();

    public abstract void showCardDrawn(Card card);
    public abstract void showCardEffectStart(Card card);
    public abstract void showCardEffectEnd(Card card);

    public abstract void showTruckerMoved(String nickname, int newPosition);
    public abstract void showTruckerWithdrawn(String nickname);
    public abstract void showTruckerLostAllCrew(String nickname);

    public abstract void showGoodsGained(List<Goods> goods);

    //END OF GAME
    public abstract void showGameEnded();
    public abstract void showScores(Map<String, Integer> finalScores);
    public abstract void showWinner(String nickname);

    //ERRORS/SYSTEM
    public abstract void showError(String message);
    public abstract void showConnectionLost();
    public abstract void showInvalidCommand(String expectedFormat);

    //INPUT
    public abstract String askNickname();
    public abstract PlayerColors askColor();
    public abstract int askStackIndex();
    public abstract int[] askCoordinates();
    public abstract void showGoodsToRemove(List<Goods> goods);
    public abstract List<Integer> askGoodsIndexesToRemove(int numToRemove);
    public abstract Component askComponentFromStack(List<Component> stack);
    public abstract int[] askPlacedComponent(Component[][] layout);

    //PLAYER DATA
    public abstract void showPlayerShipStatus(String nickname, int position, int batteryCount, int humanCount, int alienCount, int cannonPower, int shieldPower, int enginePower, Map<String, Integer> goodsByColor);
    public abstract void showShipLayout(String nickname, Component[][] layout);


}
