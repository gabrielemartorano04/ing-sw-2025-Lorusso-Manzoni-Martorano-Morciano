package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.exceptions.PlanetNotFreeException;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.visitor.CardVisitor;
import it.polimi.ingsw.model.visitor.ParametricCardVisitor;

import java.util.ArrayList;
import java.util.Comparator;

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

    //variabili per la addGood
    private int currGood;
    private int currPlayerIndex;
    private Trucker currTrucker;
    private Goods currGoodToGain;
    private ArrayList<Goods> currGoodsList;
    private ArrayList<Goods> listToPass;
    private int indexOfListToPass;
    private ArrayList<Goods> helperList;
    // fine variabili per la addGood

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
        this.state = CardState.PLOT_BANK;
        this.planetsOccupied = new ArrayList<>();
        for (int i = 0; i < planets.size(); i++) {
            this.planetsOccupied.add(null);
        }
        this.truckersLanded = new ArrayList<>();
        this.currGood = 0;
        this.numOfPlayersLanded = 0;
        this.currPlayerIndex = 0;
        this.currGoodToGain = null;
        this.currTrucker = null;
        this.currGoodsList = new ArrayList<>();
        this.listToPass = new ArrayList<>();
        this.indexOfListToPass = 0;
        this.helperList = new ArrayList<>();
    }

    @Override
    public Object accept(CardVisitor visitor) {
        return visitor.visitForPlanets(this);
    }

    @Override
    public Object accept(ParametricCardVisitor visitor, Object... params) throws InvalidCoordinates, InvalidPositionException {
        return visitor.visitForPlanets(this, params);
    }

    /**
     * Returns, for each planet on the card, a list of goods to gain
     * @return list of lists of good to gain
     */
    public ArrayList<ArrayList<Goods>> getPlanets() {
        return planets;
    }

    public int[] getBank(Bank bank){
        if(state != CardState.PLOT_BANK){
            throw new IllegalStateException("Can't get bank from state " + state);
        }
        state = CardState.ATTESA_PIANETI;
        return bank.getGoods();
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

        state = CardState.PLOT_BANK;
        // se truckersLandend non contiene null vuol dire che tutti i pianeti sono occupati
        // se numOfCurrPlayer è uguale al numero di giocatori della partita vuol dire che tutti hanno risposto
        if(!planetsOccupied.contains(null) || numOfCurrPlayer == numPlayers){
            for(int i = 0; i < planetsOccupied.size(); i++){
                if(planetsOccupied.get(i) != null){
                    numOfPlayersLanded++;
                }
            }
            state = CardState.INITIALIZE;
        }
        return state;
    }

    public CardState initialize(Bank bank){
        if(state != CardState.INITIALIZE){
            throw new RuntimeException("Something is wrong, you called initialize in the state " + state);
        }
        if(currPlayerIndex >= numOfPlayersLanded) {
            throw new RuntimeException("All players have already added their goods");
        }

        this.currTrucker = truckersLanded.get(currPlayerIndex);
        this.currGoodsList = planets.get(truckersLanded.indexOf(currTrucker));
        this.currGood = 0;

        do {
            if (bank.getGoodsInBank(currGoodsList.get(currGood).getValue()) >= 1) {
                state = CardState.PLOT_GOOD;
                return state;
            } else {
                // nella banca non c'è disponibilità per nessuna delle merci della lista del pianeta scelto dal giocatore corrente
                if (currGood == currGoodsList.size()-1) {
                    // se la lista da passare non è vuota allora puoi scegliere anche se la banca non aveva le tue merci
                    if(!listToPass.isEmpty()){
                        state = CardState.PLOT_GOOD_FROM_LIST;
                        return state;
                    }else{ // se non c'era nulla nella banca e neanche nella lista da passare inizializzo per il prossimo trucker
                        currGood = 0;
                        currPlayerIndex++;
                        if(currPlayerIndex == truckersLanded.size()){
                            state = CardState.ESECUZIONE;
                        }else{
                            currTrucker = truckersLanded.get(currPlayerIndex);
                            currGoodsList = planets.get(truckersLanded.indexOf(currTrucker));
                        }
                    }
                }
                currGood++;
            }
        }while (state == CardState.INITIALIZE);

        currPlayerIndex++;

        // se è stato l'ultimo atterrato a scegliere di non prendersi merci allora setto lo stato a esecuzione
        if(currPlayerIndex == truckersLanded.size()){
            state = CardState.ESECUZIONE;
        }
        return state;
    }

    public Goods plotGoodFromPlanet() throws IllegalStateException {
        if(state!=CardState.PLOT_GOOD){
            throw new IllegalStateException("Not callable in state: " + state);
        }
        this.currGoodToGain = currGoodsList.get(currGood);
        return currGoodToGain;
    }

    public Goods plotGoodFromList() throws IllegalStateException {
        if(state!=CardState.PLOT_GOOD_FROM_LIST){
            throw new IllegalStateException("Not callable in state: " + state);
        }
        this.currGoodToGain = listToPass.get(indexOfListToPass);
        return currGoodToGain;
    }

    public CardState chooseToGain (boolean choice) throws IllegalStateException {
        if(state!=CardState.CHOOSE_TO_GAIN){
            throw new IllegalStateException("Not callable in state: " + state);
        }
        if (choice) {
            state = CardState.GOODSTOADD;
        }else {
            helperList.add(currGoodToGain);
            if(currGood==currGoodsList.size()-1){
                if(listToPass.isEmpty()){
                    state = CardState.REARRANGE;
                }else{
                    state = CardState.PLOT_GOOD_FROM_LIST;
                }
            } else currGood++;
        }
        return state;
    }

    public CardState chooseFromList (boolean choice) throws IllegalStateException {
        if(state!=CardState.CHOOSE_FROM_LIST){
            throw new IllegalStateException("Not callable in state: " + state);
        }
        if (choice) {
            state = CardState.ADDFROMLIST;
        }else {
            if(indexOfListToPass==listToPass.size()-1){
                state = CardState.REARRANGE;
            } else indexOfListToPass++;
        }
        return state;
    }

    /**
     * Sets the coordinates of the loads where the winner trucker wants to add the goods gained
     * @param coordinatesToAddGood list of coordinatest
     */
    public CardState setCoordinatesToAddGoods(int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws IllegalStateException, InvalidCoordinates  {
        if (state != CardState.GOODSTOADD) {
            throw new IllegalStateException("Card state is not GOODSTOADD");
        }
        if (coordinatesToAddGood==null || (coordinatesToAddGood.length == 0) || !currTrucker.getSpaceShip().checkLoad(coordinatesToAddGood)) { //check load deve controllare il singolo componente
            throw new InvalidCoordinates("Not goods deposit");
        }
        try{
            currTrucker.getSpaceShip().addGood(currGoodsList.get(currGood), coordinatesToAddGood, positionInLoad, bank);
            bank.removeGoodsInBank(currGoodsList.get(currGood).getValue());
        }catch (InvalidPositionException e) {
            state = CardState.GOODSTOADD;
            return state;
        }
        if(currGood == currGoodsList.size()-1){
            if(listToPass.isEmpty()){
                state = CardState.REARRANGE;
            }else{
                state = CardState.PLOT_GOOD_FROM_LIST;
            }
        }else{
            currGood++;
            do {
                if (bank.getGoodsInBank(currGoodsList.get(currGood).getValue()) >= 1) {
                    state = CardState.CHOOSE_TO_GAIN;
                } else {
                    currGood++;
                    if(currGood==currGoodsList.size()) {
                        if(listToPass.isEmpty()){
                            state = CardState.REARRANGE;
                        }else{
                            state = CardState.PLOT_GOOD_FROM_LIST;
                        }
                    }
                }
            }while(state==CardState.GOODSTOADD);
        }
        return state;
    }

    /**
     * Sets the coordinates of the loads where the winner trucker wants to add the goods gained
     * @param coordinatesToAddGood list of coordinatest
     */
    public CardState coordinatesToAddFromList(int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws IllegalStateException, InvalidCoordinates  {
        if (state != CardState.ADDFROMLIST) {
            throw new IllegalStateException("Not callable in the state: " + state);
        }
        if (coordinatesToAddGood==null || (coordinatesToAddGood.length == 0) || !currTrucker.getSpaceShip().checkLoad(coordinatesToAddGood)) { //check load deve controllare il singolo componente
            throw new InvalidCoordinates("Not goods deposit");
        }
        try{
            currTrucker.getSpaceShip().addGood(listToPass.get(indexOfListToPass), coordinatesToAddGood, positionInLoad, bank);
            bank.removeGoodsInBank(listToPass.get(indexOfListToPass).getValue());
            listToPass.remove(currGoodToGain);
        }catch (InvalidPositionException e) {
            state = CardState.ADDFROMLIST;
            return state;
        }
        if(indexOfListToPass == listToPass.size()-1){
            state = CardState.REARRANGE;
        }else{
            indexOfListToPass++;
            state = CardState.PLOT_GOOD_FROM_LIST;
        }
        return state;
    }

    // si potrebbe non metter contiune e procedere se tutto e a null
    public void rearrange(int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws IllegalStateException, InvalidCoordinates, InvalidPositionException {
        if (state != CardState.REARRANGE) {
            throw new IllegalStateException("Not callable in the state: " + state);
        }
        if (!contiune) {
            if (coord1 == null && !currTrucker.getSpaceShip().checkLoad(coord1) && coord2 == null && !currTrucker.getSpaceShip().checkLoad(coord2)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                currTrucker.getSpaceShip().swapGoods(coord1, value1, coord2, value2);
            } catch (InvalidPositionException e) {
                state = CardState.REARRANGE;
                throw new InvalidPositionException("invalid positions");
            }
        } else{
            // tutti hanno settato le loro merci
            if(currPlayerIndex == truckersLanded.size()-1){
                state = CardState.ESECUZIONE;
            }else{
                 ArrayList<Goods> mergedList = new ArrayList<>(listToPass);
                 mergedList.addAll(helperList);
                 mergedList.sort(Comparator.comparingInt(g -> -g.getValue()));
                 listToPass = mergedList;
                 helperList.clear();
                 state = CardState.INITIALIZE;
            }
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

        // a questo punto potrei avere che la lista delle merci passate tra i giocatori non è vuota ma non è un problema
        // infatti tolgo dalla banca il conteggio delle merci dentro listToPass solo quando aggiungo alla nave quindi
        // le merci avanzate dentro la lista listToPass sono già contate nella banca e faccio solo la clear di listToPass
        // per completezza anche se sarebbe evitabile tanto l'istanza di una carta si usa una sola volta
        listToPass.clear();

        // muovo in dietro i giocatori ma partendo dal più indietro nella plancia non dal Leader
        for(int i = truckersLanded.size() - 1; i>=0; i--){
            game.moveBackward(truckersLanded.get(i), daysLoss);
        }
        state = CardState.EXIT;
        return state;
    }
}