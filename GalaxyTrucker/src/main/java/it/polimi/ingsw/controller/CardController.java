package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.GenericErrorEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.ClientHandlerSocket;
import it.polimi.ingsw.utilities.exceptions.*;

import java.util.ArrayList;

public class CardController {
    private Card currCard;
    private GameController gameController;

    public CardController() {
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public Card getCurrCard() {
        return currCard;
    }

    public void setCurrCard(Card card){
        this.currCard = card;
    }

    public ServerEvent truckerLanding(TruckerLandingEvent event) {
        try{
            return currCard.truckerLanding(event.getLanding());
        } catch(WrongCallerException e){
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    public ServerEvent setHumansCoordinates(SendHumanCoordEvent event) {
        try {
            return currCard.setHumansCoordinates(event.getHumanCoord());
        } catch (InvalidComponentsException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    //public ServerEvent setGoodsCoordinates()

    public ServerEvent rearrange(RearrangeEvent event) {
        try {
            return currCard.rearrange(event.getCoord1(), event.getCoord2(), event.getValue1(), event.getValue2(), event.getStop());
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidPositionException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    public ServerEvent rollDice() {
        try {
            return currCard.rollDice();
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    public ServerEvent activateShield(SendActivateShieldEvent event) {
        try {
            return currCard.activateShield(event.getDecision(), event.getCoordShield(), event.getCoordBS());
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    public ServerEvent activateEngines(SendActivateEnginesEvent event) {
        try {
            return currCard.activateEngines(event.getDecision(), event.getCoordinates(), event.getBatteryCoordinates());
        } catch (InvalidCoordinates e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (NotEnoughBatteries e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    public ServerEvent activateCannons(SendActivateCannonsEvent event) {
        try {
            return currCard.activateCannons(event.getDecision(), event.getCoordinates(), event.getBatteryCoordinates());
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidComponentsException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (NotEnoughBatteries e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }

    }

    public ServerEvent setWinDecision(SetWinDecision event) {
        try {
            return currCard.setWinDecision(event.getWin());
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }
}