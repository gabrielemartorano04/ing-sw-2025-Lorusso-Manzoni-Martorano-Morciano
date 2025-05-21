package it.polimi.ingsw.controller;

import com.sun.javafx.iio.jpeg.JPEGImageLoader;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.view.ParserState;

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
            return new InvalidComponentsExceptionEvent(e.getMessage(), false, ParserState.HUMAN_COORD,event.getNickname() );
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), false,ParserState.HUMAN_COORD,event.getNickname());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    public ServerEvent setGoodsCoordinates(PlotGoodEvent event) {
        try {
            return currCard.setGoodsCoordinates(event.getDecision(), event.getCoordinates(), event.getPositionInLoad());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), false, ParserState.PLOT_GOOD, event.getNickname());
        } catch (InvalidPositionException e) {
            return new InvalidPositionExceptionEvent(e.getMessage(), false, ParserState.PLOT_GOOD, event.getNickname());
        }
    }

    public ServerEvent rearrange(RearrangeEvent event) {
        try {
            return currCard.rearrange(event.getCoord1(), event.getCoord2(), event.getValue1(), event.getValue2(), event.getStop());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), false, ParserState.REARRANGE, event.getNickname());
        } catch (InvalidPositionException e) {
            return new InvalidPositionExceptionEvent(e.getMessage(), false,ParserState.REARRANGE, event.getNickname());
        }
    }

    public ServerEvent rollDice() {
        try {
            return currCard.rollDice();
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    public ServerEvent activateShield(ActivateShieldEvent event) {
        try {
            return currCard.activateShield(event.getDecision(), event.getCoordinates(), event.getBatteryCoordinates());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), false,ParserState.ACTIVATE_ENGINES,event.getNickname());
        }
    }

    public ServerEvent activateEngines(ActivateEnginesEvent event) {
        try {
            return currCard.activateEngines(event.getDecision(), event.getCoordinates(), event.getBatteryCoordinates());
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), false,ParserState.ACTIVATE_ENGINES,event.getNickname());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        } catch (NotEnoughBatteries e) {
            return new NotEnoughBatterieEvent(e.getMessage(), false,ParserState.ACTIVATE_ENGINES,event.getNickname());
        }
    }

    public ServerEvent activateCannons(ActivateCannonsEvent event) {
        try {
            return currCard.activateCannons(event.getDecision(), event.getCoordinates(), event.getBatteryCoordinates());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        } catch (InvalidComponentsException e) {
            return new InvalidComponentsExceptionEvent(e.getMessage(), false,ParserState.ACTIVATE_CANNONS,event.getNickname());
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), false,ParserState.ACTIVATE_CANNONS,event.getNickname());
        } catch (NotEnoughBatteries e) {
            return new NotEnoughBatterieEvent(e.getMessage(), false,ParserState.ACTIVATE_CANNONS,event.getNickname());
        }

    }

    public ServerEvent setWinDecision(SetWinDecision event) {
        try {
            return currCard.setWinDecision(event.getWin());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    public ServerEvent playerLandingPlanets(PlanetDecisionEvent event) {
        try {
            return currCard.playerLandingPlanets(event.getIndex());
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        } catch (PlanetNotFreeException e) {
            return new PlanetNotFreeExceptionEvent(e.getMessage(), false, ParserState.PLANET_DECISION, event.getNickname());
        }
    }

    public ServerEvent removeGoods(RemoveGoodsEvent event) {
        try {
            return currCard.removeGoods();
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    public ServerEvent removeAsking(RemoveAskingEvent event) {
        try {
            return currCard.removeAsking(event.getIndexes());
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        } catch (InvalidGoodChosen e) {
            return new InvalidGoodChosenEvent(e.getMessage(), false, ParserState.REMOVE_GOODS, event.getNickname() );
        } catch (NotEnoughGoodsChosen e) {
            return new NotEnoughGoodsChosenEvent(e.getMessage(), false, ParserState.REMOVE_GOODS, event.getNickname());
        }
    }

    public ServerEvent removeBatteries(RemoveBatteriesEvent event) {
        try {
            return currCard.removeBatteries(event.getCoordinatesToRemoveBatteries());
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), false,ParserState.REMOVE_BATTERIES,event.getNickname());
        } catch (WrongNumberOfBatteriesSelected e) {
            return new WrongNumberOfBatteriesSelectedEvent(e.getMessage(), false, ParserState.REMOVE_BATTERIES, event.getNickname());
        } catch (NotEnoughBatteries e) {
            return new NotEnoughBatterieEvent(e.getMessage(), false,ParserState.REMOVE_BATTERIES,event.getNickname());
        }
    }

    public ServerEvent handleCurrentCondition() {
        try {
            return currCard.handleCurrentCondition();
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }


    public ServerEvent startCard(){
        return currCard.startCard();
    }
}