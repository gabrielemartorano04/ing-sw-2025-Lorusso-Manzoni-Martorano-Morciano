package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.events.clientevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.*;
import it.polimi.ingsw.view.ParserState;

/**
 * This class call the right card method, based on current card and client events.
 */
public class CardController {
    private Card currCard;

    /**
     * @return current card in the game
     */
    public Card getCurrCard() {
        return currCard;
    }

    /**
     * This method sets the current card
     * @param card the card that becomes the currCard
     */
    public void setCurrCard(Card card){
        this.currCard = card;
    }

    /**
     * This method calls truckerLanding method of the current card.
     * @param event received Client Event
     * @return Server Event to send to client
     */
    public ServerEvent truckerLanding(TruckerLandingEvent event) {
        try{
            return currCard.truckerLanding(event.getLanding());
        } catch(WrongCallerException | GeneralError e){
            return new GenericErrorEvent(e.getMessage(), true);
        }
    }

    /**
     * This method calls setHumansCoordinate method of the current card.
     * @param event received Client Event
     * @return Server Event to send to client
     */
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

    /**
     * This method calls setGoodsCoordinates method of the current card.
     * @param event received Client Event
     * @return Server Event to send to client
     */
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

    /**
     * This method calls rearrange method of the current card.
     * @param event received Client Event
     * @return Server Event to send to client
     */
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

    /**
     * This method calls rollDice method of the current card.
     * @return Server Event to send to client
     */
    public ServerEvent rollDice() {
        try {
            return currCard.rollDice();
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    /**
     * This method calls activateShield method of the current card.
     * @param event received Client Event
     * @return Server Event to send to client
     */
    public ServerEvent activateShield(ActivateShieldEvent event) {
        try {
            return currCard.activateShield(event.getDecision(), event.getCoordinates(), event.getBatteryCoordinates());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        } catch (InvalidCoordinates e) {
            return new InvalidCoordinatesEvent(e.getMessage(), false,ParserState.ACTIVATE_ENGINES,event.getNickname());
        }
    }

    /**
     * This method calls activateEngines method of the current card.
     * @param event received Client Event
     * @return Server Event to send to client
     */
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

    /**
     * This method calls activateCannons method of the current card.
     * @param event received Client Event
     * @return Server Event to send to client
     */
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

    /**
     * This method calls setWinDecision method of the current card.
     * @param event received Client Event
     * @return Server Event to send to client
     */
    public ServerEvent setWinDecision(GainCosmicCreditsResponseEvent event) {
        try {
            return currCard.setWinDecision(event.getAnswer());
        } catch (GeneralError | WrongCallerException e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    /**
     * This method calls playerLandingPlanets method of the current card.
     * @param event received Client Event
     * @return Server Event to send to client
     */
    public ServerEvent playerLandingPlanets(PlanetDecisionEvent event) {
        try {
            return currCard.playerLandingPlanets(event.getIndex());
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        } catch (PlanetNotFreeException e) {
            return new PlanetNotFreeExceptionEvent(e.getMessage(), false, ParserState.PLANET_DECISION, event.getNickname());
        }
    }

    /**
     * This method calls removeGoods method of the current card.
     * @param event received Client Event
     * @return Server Event to send to client
     */
    public ServerEvent removeGoods(RemoveGoodsEvent event) {
        try {
            return currCard.removeGoods();
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    /**
     * This method calls removeAsking method of the current card.
     * @param event received Client Event
     * @return Server Event to send to client
     */
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

    /**
     * This method calls removeBatteries method of the current card.
     * @param event received Client Event
     * @return Server Event to send to client
     */
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

    /**
     * This method calls handleCurrentCondition method of the current card.
     * @return Server Event to send to client
     */
    public ServerEvent handleCurrentCondition() {
        try {
            return currCard.handleCurrentCondition();
        } catch (WrongCallerException | GeneralError e) {
            return new GenericErrorEvent(e.getMessage(), false);
        }
    }

    /**
     * This method calls startCard method of the current card.
     * @return Server Event to send to client
     */
    public ServerEvent startCard(){
        return currCard.startCard();
    }
}