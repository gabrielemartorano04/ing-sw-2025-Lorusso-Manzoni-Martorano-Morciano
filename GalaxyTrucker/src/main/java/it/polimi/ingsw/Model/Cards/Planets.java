package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Exceptions.InvalidCoordinates;
import it.polimi.ingsw.Exceptions.InvalidPositionException;
import it.polimi.ingsw.Exceptions.PlanetNotFreeException;
import it.polimi.ingsw.Model.Bank;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;

/**
 * Class that manages the single card planets
 * In order of position, starting from the leader, each trucker decides if he/she wants, losing days of flight, to land on a planet and which.
 * Once everybody has decided, in reverse order of position, the truckers lose their days of flight.
 * Is possible that nobody wants to land on a planet.
 */
public class Planets extends Card {
    private final ArrayList<ArrayList<Goods>> planets;
    private final int daysLoss;

    // planetsOccupied.get(i) è il trucker che ha deciso di atterrare nel pianeta i-esimo, oppure null se nessuno è voluto atterrare su quel pianeta
    private ArrayList<Trucker> planetsOccupied;

    // truckersLanded tiene conto dei trucker che hanno deciso di atterrare in ordine di decisione, quindi il primo trucker in
    // truckersLanded è il primo trucker che è atterrato effettivamente in un pianeta
    private ArrayList<Trucker> truckersLanded;
    private int numOfCurrPlayer = 0;
    private ArrayList<ArrayList<int[]>> truckersCoordinates; // solita convenzione per salvarmi le coordinate di dove aggiungere le merci per ogni trucker
    private CardState state;
    private int numOfPlayersLanded;
    private boolean allValid1 = true; // mi dice se tutte le liste delle coordinate sono state riempite
    private boolean choiceToGain;
    private int currGood;

    /**
     * Planets constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param daysLoss number of days of flight to lose
     * @param planets list of lists of goods to gain
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public Planets(boolean trialFlight, int daysLoss, int level, ArrayList<ArrayList<Goods>> planets, String frontImage, String backImage) {
        super("Planets", "A new planetary system discovered", trialFlight, level, frontImage, backImage);
        this.daysLoss = daysLoss;
        this.planets = planets;
        this.truckersCoordinates = new ArrayList<>();
        this.state = CardState.ATTESA_PIANETI;
        this.planetsOccupied = new ArrayList<>();
        for (int i = 0; i < planets.size(); i++) {
            this.planetsOccupied.add(null);
        }
        this.truckersLanded = new ArrayList<>();
        this.currGood = 0;
        this.choiceToGain = false;
        this.numOfPlayersLanded = 0;
    }

    /**
     * Returns, for each planet on the card, a list of goods to gain
     * @return list of lists of good to gain
     */
    public ArrayList<ArrayList<Goods>> getPlanets() {
        return planets;
    }

    /**
     * Lands the player on the planet he chose and saves that that trucker has landed
     * @param indexOfLanding: index of the planet decided by the trucker, its value is -1 if the trucker does not want to land
     * @param trucker: trucker that is currently deciding
     * @param numPlayers: number of players in the current game
     * @throws PlanetNotFreeException: the trucker decided to land on a planet that is already occupied
     */
    public CardState setPlayerLanding(int indexOfLanding, Trucker trucker, int numPlayers) throws PlanetNotFreeException {
        if(state != CardState.ATTESA_PIANETI){
            throw new IllegalStateException("Can't land in the state: " + this.state);
        }
        if(indexOfLanding < -1 || indexOfLanding >= this.planets.size()){
            throw new RuntimeException("No planets at the index: " + indexOfLanding);
        }
        if(trucker == null){
            throw new RuntimeException("Trucker is null");
        }
        if(planetsOccupied.contains(trucker)){
            throw new RuntimeException("This trucker has already chosen");
        }
        if(planetsOccupied.get(indexOfLanding)!=null){
            throw new PlanetNotFreeException("You choose a planet that was already occupied");
        }
        if(indexOfLanding == -1){
            numOfCurrPlayer++;
        }else {
            planetsOccupied.add(indexOfLanding, trucker);
            truckersLanded.add(trucker);
            numOfCurrPlayer++;
        }

        // se truckersLandend non contiene null vuol dire che tutti i pianeti sono occupati
        // se numOfCurrPlayer è uguale al numero di giocatori della partita vuol dire che tutti hanno risposto
        if(!planetsOccupied.contains(null) || numOfCurrPlayer == numPlayers){
            for(int i = 0; i < planetsOccupied.size(); i++){
                if(planetsOccupied.get(i) != null){
                    numOfPlayersLanded++;
                }
            }
            state = CardState.ATTESA_RISPOSTE;
        }
        return state;
    }

    public Goods setChoiceToGain(boolean choice) throws IllegalStateException {
        if(state!=CardState.CHOOSE_TO_GAIN){
            throw new IllegalStateException("Card state is not CHOOSE_TO_GAIN");
        }
        if (choice) {
            this.choiceToGain = choice;
            state = CardState.GOODSTOADD;
            return goodsGained[currGood];
        }else {
            state = CardState.ESECUZIONE;
            return null;
        }
    }

    /**
     * Sets the coordinates of the loads where the winner trucker wants to add the goods gained
     * @param winnerCoordinatesToAddGood list of coordinatest
     */
    public void setWinnerCoordinatesToAddGoods(int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws IllegalStateException, InvalidPositionException, InvalidCoordinates  {
        if (state != CardState.GOODSTOADD) {
            throw new IllegalStateException("Card state is not GOODSTOADD");
        }
        if (winnerCoordinatesToAddGood==null || !trucker.getSpaceShip().checkLoad(winnerCoordinatesToAddGood)) { //check load deve controllare il singolo componente
            throw new InvalidCoordinates("Not goods deposit");
        }
        try{
            trucker.getSpaceShip().addGood(goodsGained[currGood], winnerCoordinatesToAddGood, value);
            bank.removeGoodsInBank(goodsGained[currGood].getValue());
        }catch (InvalidPositionException e) {
            state = CardState.GOODSTOADD;
        }
        if(currGood == goodsGained.length-1){
            state = CardState.REARRANGE;
        }
        else{
            currGood++;
            do {
                if (bank.getGoodsInBank(goodsGained[currGood].getValue()) >= 1) {
                    state = CardState.CHOOSE_TO_GAIN;
                } else {
                    currGood++;
                    if(currGood>=goodsGained.length) {
                        state = CardState.ESECUZIONE;
                    }
                }
            }while(currGood<goodsGained.length && state!=CardState.CHOOSE_TO_GAIN);
        }

    }

    // si potrebbe non metter contiune e procedere se tutto e a null
    public void rearrange(Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws IllegalStateException, InvalidCoordinates, InvalidPositionException {
        if (state != CardState.REARRANGE) {
            throw new IllegalStateException("Card state is not REARRANGE");
        }
        if (!contiune) {
            if (coord1 == null && !trucker.getSpaceShip().checkLoad(coord1) && coord2 == null && !trucker.getSpaceShip().checkLoad(coord2)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                trucker.getSpaceShip().swapGoods(coord1, value1, coord2, value2);
            } catch (InvalidPositionException e) {
                state = CardState.REARRANGE;
                throw new InvalidPositionException("invalid positions");
            }
        } else{
            state = CardState.ESECUZIONE;
        }

    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public CardState cardEffect(Game game) {
        if (this.state != CardState.ESECUZIONE) {
            throw new IllegalStateException("CardEffect not callable in the state: " + this.state);
        }
        int i = 0;

        // riempo le merci a tutti tanto chi non è atterrato su un pianeta o non ha voluto caricare nessuna merce
        // avrà truckersCoordinates.get(i) come lista vuota e addGoods non fa nulla
        for(Trucker trucker : truckersLanded){
            trucker.getSpaceShip().addGoods(planets.get(i), truckersCoordinates.get(i));
            i++;
        }

        // muovo in dietro i giocatori ma partendo dal più indietro nella plancia non dal Leader
        for(i = truckersLanded.size() - 1; i>=0; i--){
            game.moveBackward(truckersLanded.get(i), daysLoss);
        }
        state = CardState.EXIT;
        return state;
    }
}