//package it.polimi.ingsw.controller;
//
//import it.polimi.ingsw.model.Bank;
//import it.polimi.ingsw.model.Game;
//import it.polimi.ingsw.model.Trucker;
//import it.polimi.ingsw.model.cards.Card;
//import it.polimi.ingsw.model.cards.CardState;
//import it.polimi.ingsw.model.visitor.*;
//import it.polimi.ingsw.utilities.exceptions.*;
//
//import javax.swing.*;
//import java.util.ArrayList;
//
///**
// *
// */
//public class CardController {
//
//    public String getCardName(Card card) {
//        return card.getName();
//    }
//
//    public String getCardDescription(Card card) {
//        return card.getDescription();
//    }
//
//    public int getCardLevel(Card card) {
//        return card.getLevel();
//    }
//
//    public boolean isTrialFlight(Card card) {
//        return card.isTrialFlight();
//    }
//
//    public ImageIcon getCardFrontImage(Card card) {
//        return card.getFrontImage();
//    }
//
//    public ImageIcon getCardBackImage(Card card) {
//        return card.getBackImage();
//    }
//
//    public CardState applyEffects(Card card, Game game) {
//        return card.cardEffect(game);
//    }
//
//    public Object getDays(Card card) {
//        CardVisitor visitor = new GetDaysVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getCredits(Card card) {
//        CardVisitor visitor = new GetCreditsVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getCrewMembersToLose(Card card) {
//        CardVisitor visitor = new GetElementsToLoseVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getCrewMembersRequired(Card card) {
//        CardVisitor visitor = new GetElementsRequired();
//        return card.accept(visitor);
//    }
//
//    public Object getGoods(Card card) {
//        CardVisitor visitor = new GetGoodsVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getDirections(Card card) {
//        CardVisitor visitor = new GetDirectionVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getCurrentMeteor(Card card) {
//        CardVisitor visitor = new GetCurrentAttackVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getCurrentShoot(Card card) {
//        CardVisitor visitor = new GetCurrentAttackVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getSlaversPower(Card card) {
//        CardVisitor visitor = new GetEnemyPowerVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getSmugglersPower(Card card) {
//        CardVisitor visitor = new GetEnemyPowerVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getPiratesPower(Card card) {
//        CardVisitor visitor = new GetEnemyPowerVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getShoots(Card card) {
//        CardVisitor visitor = new GetShootsVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getPlanets(Card card) {
//        CardVisitor visitor = new GetPlanetsVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getGoodsFromPlanet(Card card) {
//        CardVisitor visitor = new GetGoodsFromPlanetsVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getGoodsFromList(Card card) {
//        CardVisitor visitor = new GetGoodsVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getGoodsToLose(Card card) {
//        CardVisitor visitor = new GetElementsToLoseVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getGoodsToGain(Card card) {
//        CardVisitor visitor = new GetElementsToGainVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getCurrentGood(Card card) {
//        CardVisitor visitor = new GetCurrentGoodVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getBatteriesToRemove(Card card) {
//        CardVisitor visitor = new GetElementsToRemove();
//        return card.accept(visitor);
//    }
//
//    public Object getConditionsOrder(Card card) {
//        CardVisitor visitor = new GetConditionsOrderVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object getPenalties(Card card) {
//        CardVisitor visitor = new GetPenaltiesVisitor();
//        return card.accept(visitor);
//    }
//
//    public Object setTruckerLandingOnAbandonedShip(Card card, Trucker trucker, boolean landing, int numPlayer) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetTruckerLandingVisitor();
//            return card.accept(visitor, trucker, landing, numPlayer);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setTruckerLandingOnAbandonedStation(Card card, Trucker trucker, boolean landing, int numPlayer, Bank bank) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetTruckerLandingVisitor();
//            return card.accept(visitor, trucker, landing, numPlayer, bank);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setTruckerLandingOnPlanets(Card card, int indexOfLanding, Trucker trucker, int numPlayers) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetTruckerLandingVisitor();
//            return card.accept(visitor, indexOfLanding, trucker, numPlayers);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setCoordinatesToRemoveCrew(Card card, ArrayList<int[]> coordinates) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetCoordinatesToRemoveElementsVisitor();
//            return card.accept(visitor, coordinates);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setCoordinatesToRemoveCrewForSlavers(Card card, ArrayList<int[]> truckersCoordinatesToRemoveHumans, int numPlayer, Trucker trucker) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetCoordinatesToRemoveElementsVisitor();
//            return card.accept(visitor, truckersCoordinatesToRemoveHumans, numPlayer, trucker);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setCoordinatesToRemoveBatteries(Card card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetCoordinatesToRemoveBatteriesVisitor();
//            return card.accept(visitor, coordinatesToRemoveBatteries, trucker);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setCoordinatesToAddGoods(Card card, int[] coordinatesToAddGood, int value, Trucker trucker, Bank bank) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetCoordinatesToAddElementsVisitor();
//            return card.accept(visitor, coordinatesToAddGood, value, trucker, bank);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setCoordinatesToAddGoodsFromPlanet(Card card, int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetCoordinatesToAddElementsVisitor();
//            return card.accept(visitor, coordinatesToAddGood, positionInLoad, bank);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setCoordinatesToAddGoodsFromList(Card card, int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetCoordinatesToAddElementsFromListVisitor();
//            return card.accept(visitor, coordinatesToAddGood, positionInLoad, bank);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setChoiceToGain(Card card, boolean choice) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetChoiceToGainVisitor();
//            return card.accept(visitor, choice);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setChoiceToGainFromList(Card card, boolean choice) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetChoiceToGainFromListVisitor();
//            return card.accept(visitor, choice);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object rearrange(Card card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new RearrangeVisitor();
//            return card.accept(visitor, trucker, coord1, coord2, value1, value2, contiune);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object rearrangeForPlanet(Card card, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new RearrangeVisitor();
//            return card.accept(visitor, coord1, coord2, value1, value2, contiune);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object rollDice(Card card, Game game) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new RollDiceVisitor();
//            return card.accept(visitor, game);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setAnswear(Card card, boolean answear) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetAnswersVisitor();
//            return card.accept(visitor, answear);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setTruckersAnswear(Card card, boolean answear, Trucker trucker) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetAnswersVisitor();
//            return card.accept(visitor, answear, trucker);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setDoubleEnginesCoordinates(Card card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetDoubleEnginesCoordinatesVisitor();
//            return card.accept(visitor, trucker, truckerCoordinate, numTruckers);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setDoubleEnginesCoordinatesforWarZone(Card card, Game game, Trucker trucker, ArrayList<int[]> truckerCannonsCoordinates) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetDoubleEnginesCoordinatesVisitor();
//            return card.accept(visitor, game, trucker, truckerCannonsCoordinates);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setDoubleCannonsCoordinates(Card card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetDoubleCannonsCoordinatesVisitor();
//            return card.accept(visitor, trucker, truckerCoordinate, numTruckers);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setDoubleCannonsCoordinatesForSmugglers(Card card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers, Bank bank) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetDoubleCannonsCoordinatesVisitor();
//            return card.accept(visitor, trucker, truckerCoordinate, numTruckers, bank);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setDoubleCannonsCoordinatesForWarZone(Card card, Game game, Trucker trucker, ArrayList<int[]> truckerCannonsCoordinates) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetDoubleCannonsCoordinatesVisitor();
//            return card.accept(visitor, game, trucker, truckerCannonsCoordinates);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setDefeatedTruckers(Card card, ArrayList<Trucker> defeatedTruckers) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetDefeatedTruckersVisitor();
//            return card.accept(visitor, defeatedTruckers);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object setWinnerTrucker(Card card, Trucker winnerTrucker) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new SetWinnerTruckerVisitor();
//            return card.accept(visitor, winnerTrucker);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object getBank(Card card, Bank bank) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new GetBankVisitor();
//            return card.accept(visitor, bank);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object initialize(Card card, Bank bank) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new InitializeVisitor();
//            return card.accept(visitor, bank);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object manageRemoveGoods(Card card, Trucker trucker, Bank bank) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new ManageRemoveGoodsVisitor();
//            return card.accept(visitor, trucker, bank);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object plotGoods(Card card, Trucker trucker) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new GetGoodsVisitor();
//            return card.accept(visitor, trucker);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object removeAsking(Card card, ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new RemoveAskingVisitor();
//            return card.accept(visitor, indexes, trucker, bank);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object combactZone(Card card, Game game) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new CombatZoneVisitor();
//            return card.accept(visitor, game);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//    }
//
//    public Object findWeakestTrucker(Card card, Game game, ArrayList<ArrayList<int[]>> truckersCoordinates) throws InvalidCoordinates, InvalidPositionException, InvalidGoodChosen, NotEnoughGoodsChosen, WrongNumberOfBatteriesSelected, InvalidComponentsException, NotEnoughBatteries {
//        try {
//            ParametricCardVisitor visitor = new FindWeakestTruckerVisitor();
//            return card.accept(visitor, game, truckersCoordinates);
//        } catch (InvalidCoordinates e) {
//            throw new InvalidCoordinates(e.getMessage());
//        } catch (InvalidPositionException e) {
//            throw new InvalidPositionException(e.getMessage());
//        } catch (InvalidGoodChosen e) {
//            throw new InvalidGoodChosen(e.getMessage());
//        } catch (NotEnoughGoodsChosen e) {
//            throw new NotEnoughGoodsChosen(e.getMessage());
//        } catch (WrongNumberOfBatteriesSelected e) {
//            throw new WrongNumberOfBatteriesSelected(e.getMessage());
//        } catch (InvalidComponentsException e) {
//            throw new InvalidComponentsException(e.getMessage());
//        } catch (NotEnoughBatteries e) {
//            throw new NotEnoughBatteries(e.getMessage());
//        }
//
//    }
//}
