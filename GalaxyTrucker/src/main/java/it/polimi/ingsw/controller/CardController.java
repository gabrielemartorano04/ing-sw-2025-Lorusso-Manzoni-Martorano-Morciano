package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.*;

public class CardController {
    private Card currCard;

    public Card getCurrCard() {
        return currCard;
    }

    public void setCurrCard(Card card){
        this.currCard = card;
    }

    public ServerEvent truckerLanding(TruckerLandingEvent event) {
        try{
            return currCard.truckerLanding(event.getLanding());
        } catch(WrongCallerException | GeneralError e){
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    public ServerEvent setHumansCoordinates(HumanCoordEvent event) {
        try {
            return currCard.setHumansCoordinates(event.getHumanCoord());
        } catch (InvalidComponentsException e) {
            return new InvalidComponentsExceptionEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), true);
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    public ServerEvent setGoodsCoordinates(PlotGoodEvent event) {
        try {
            return currCard.setGoodsCoordinates(event.getDecision(), event.getCoordinates(), event.getPositionInLoad());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), true);
        } catch (InvalidPositionException e) {
            return new InvalidPositionExceptionEvent(e.getMessage(), true);
        }
    }

    public ServerEvent rearrange(RearrangeEvent event) {
        try {
            return currCard.rearrange(event.getCoord1(), event.getCoord2(), event.getValue1(), event.getValue2(), event.getStop());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), true);
        } catch (InvalidPositionException e) {
            return new InvalidPositionExceptionEvent(e.getMessage(), true);
        }
    }

    public ServerEvent rollDice() {
        try {
            return currCard.rollDice();
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    public ServerEvent activateShield(ActivateShieldEvent event) {
        try {
            return currCard.activateShield(event.getDecision(), event.getCoordinates(), event.getBatteryCoordinates());
        } catch (GeneralError | WrongCallerException e) {
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
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (NotEnoughBatteries e) {
            return new NotEnoughBatterieEvent(e.getMessage(), true);
        }
    }

    public ServerEvent activateCannons(ActivateCannonsEvent event) {
        try {
            return currCard.activateCannons(event.getDecision(), event.getCoordinates(), event.getBatteryCoordinates());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidComponentsException e) {
            return new InvalidComponentsExceptionEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), true);
        } catch (NotEnoughBatteries e) {
            return new NotEnoughBatterieEvent(e.getMessage(), true);
        }

    }

    public ServerEvent setWinDecision(SetWinDecision event) {
        try {
            return currCard.setWinDecision(event.getWin());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    public ServerEvent playerLandingPlanets(PlanetDecisionEvent event) {
        try {
            return currCard.playerLandingPlanets(event.getIndex());
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (PlanetNotFreeException e) {
            return new PlanetNotFreeExceptionEvent(e.getMessage(), true);
        }
    }

    public ServerEvent removeGoods(RemoveGoodsEvent event) {
        try {
            return currCard.removeGoods();
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    public ServerEvent removeAsking(RemoveAskingEvent event) {
        try {
            return currCard.removeAsking(event.getIndexes());
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidGoodChosen e) {
            return new InvalidGoodChosenEvent(e.getMessage(), true);
        } catch (NotEnoughGoodsChosen e) {
            return new NotEnoughGoodsChosenEvent(e.getMessage(), true);
        }
    }

    public ServerEvent removeBatteries(RemoveBatteriesEvent event) {
        try {
            return currCard.removeBatteries(event.getCoordinatesToRemoveBatteries());
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), true);
        } catch (WrongNumberOfBatteriesSelected e) {
            return new WrongNumberOfBatteriesSelectedEvent(e.getMessage(), true);
        } catch (NotEnoughBatteries e) {
            return new NotEnoughBatterieEvent(e.getMessage(), true);
        }
    }

    public ServerEvent combatZone(CombactZoneEvent event) {
        try {
            return currCard.combatZone();
        } catch (WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), true);
        } catch (GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    public ServerEvent startCard(){
        return currCard.startCard();
    }
}