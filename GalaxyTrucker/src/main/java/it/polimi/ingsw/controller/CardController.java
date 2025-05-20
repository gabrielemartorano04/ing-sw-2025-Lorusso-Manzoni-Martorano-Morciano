package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.utilities.exceptions.*;

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

    public ServerEvent setHumansCoordinates(HumanCoordEvent event) {
        try {
            return currCard.setHumansCoordinates(event.getHumanCoord());
        } catch (InvalidComponentsException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), true);
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    public ServerEvent setGoodsCoordinates(PlotGoodEvent event) {
        try {
            return currCard.setGoodsCoordinates(event.getDecision(), event.getCoordinates(), event.getPositionInLoad());
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), true);
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidPositionException e) {
            return new InvalidPositionExceptionEvent(e.getMessage(), true);
        }
    }

    public ServerEvent rearrange(RearrangeEvent event) {
        try {
            return currCard.rearrange(event.getCoord1(), event.getCoord2(), event.getValue1(), event.getValue2(), event.getStop());
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), true);
        } catch (InvalidPositionException e) {
            return new InvalidPositionExceptionEvent(e.getMessage(), true);
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

    public ServerEvent activateShield(ActivateShieldEvent event) {
        try {
            return currCard.activateShield(event.getDecision(), event.getCoordinates(), event.getBatteryCoordinates());
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), true);
        }
    }

    public ServerEvent activateEngines(ActivateEnginesEvent event) {
        try {
            return currCard.activateEngines(event.getDecision(), event.getCoordinates(), event.getBatteryCoordinates());
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), true);
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (NotEnoughBatteries e) {
            return new NotEnoughBatterieEvent(e.getMessage(), true);
        }
    }

    public ServerEvent activateCannons(ActivateCannonsEvent event) {
        try {
            return currCard.activateCannons(event.getDecision(), event.getCoordinates(), event.getBatteryCoordinates());
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidComponentsException e) {
            return new InvalidComponentsExceptionEvent(e.getMessage(), true);
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), true);
        } catch (NotEnoughBatteries e) {
            return new NotEnoughBatterieEvent(e.getMessage(), true);
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

    public ServerEvent playerLandingPlanets(PlanetDecisionEvent event) {
        try {
            return currCard.playerLandingPlanets(event.getIndex());
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (PlanetNotFreeException e) {
            return new PlanetNotFreeExceptionEvent(e.getMessage(), true);
        }
    }


    public ServerEvent startCard(){
        return currCard.startCard();
    }
}