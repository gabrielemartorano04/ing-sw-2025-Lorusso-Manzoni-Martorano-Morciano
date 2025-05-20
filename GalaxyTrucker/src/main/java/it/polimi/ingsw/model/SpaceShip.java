package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidPositionException;
import it.polimi.ingsw.utilities.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.model.components.*;

import java.io.Serializable;
import java.util.*;

/**
 * Class that manage the Trucker's SpaceShip, with its components, goods, astronauts, aliens
 * */


public class SpaceShip implements Serializable {

    private Component[][] components; //components in the matrix
    private int lost; //number of lost components
    private int redGoods, greenGoods, blueGoods, yellowGoods; // number of goods for each type color
    private int numHuman; //number of astronauts on the spaceship
    private int goodsCapacity; //number of goods capacity
    private ArrayList<Goods> goodsList; //list of goods in the spaceship, sort by their value (desc)
    private int numOfBattery; //number of batteries in the spaceship
    private int numAlien;
    private int numDoubleCannon, numDoubleEngine;
    private static final long serialVersionUID = 1L;

    /**
     * Spaceship constructor, it initializes the attributes of this class.
     * If a components[i][j] can be filled it's initialized null,
     * otherwise it's initialized as EmptySpace.
     * The counters are 0 because the spaceship is empty.
     * This constructor also initialize the main cabin in the middle of the spaceship.
     */
    //chiedi per parametro color per inizializzare il colore della main cabin

    public SpaceShip(PlayerColors color) {
        components = new Component[10][11];
        for(int i=0;i<5;i++){
            for(int j=0;j<11;j++){
                components[i][j]=new EmptySpace();
            }
        }
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(i==5){
                    if(j==6 || j==8) components[i][j]=null;
                    else components[i][j]=new EmptySpace();
                }
                else if(i==6){
                    if(j==4 || j==10)components[i][j]=new EmptySpace();
                    else components[i][j]=null;
                }
                else if(i==7 || i==8) components[i][j]=null;
                else if(i==9){
                    if(j==7) components[i][j]=new EmptySpace();
                    else components[i][j]=null;
                }
            }
        }

        lost = 0;
        Cabin cabin=null;
        switch(color){
            case BLUE -> {
                cabin = new Cabin(3, 3, 3, 3, "cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web33.jpg", PlayerColors.BLUE, true);
            }
            case YELLOW -> {
                cabin = new Cabin(3, 3, 3, 3, "cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web61.jpg", PlayerColors.YELLOW, true);
            }
            case RED -> {
                cabin = new Cabin(3, 3, 3, 3, "cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web52.jpg", PlayerColors.RED, true);
            }
            case GREEN -> {
                cabin = new Cabin(3, 3, 3, 3, "cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web34.jpg", PlayerColors.GREEN, true);
            }
        }
        components[7][7] = cabin;
        redGoods = 0;
        greenGoods = 0;
        blueGoods = 0;
        yellowGoods = 0;
        goodsList = new ArrayList<>();
        //ogni cabin se non ha un alieno ha due umani
        numHuman = 0;
        numAlien=0;
        //ogni battery storage è pieno di batterie
        numOfBattery = 0;
        numDoubleCannon=0;
        numDoubleEngine=0;
    }

    public void increaseLost(int lost) {
        this.lost += lost;
    }

    /**
     * This method set the full number of batteries in each battery storage and it updates numOfBattery counter to its
     * maximum
     */
    public void setBattery(){
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j] != null){
                    switch(components[i][j]){
                        case BatteryStorage c -> {
                            numOfBattery= c.getCapacity();
                            c.setQuantity(numOfBattery);
                        }
                        default -> {
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * This method calculates the goods capacity of the entire spaceship
     * @return goodsCapacity the goods capacity of the entire spaceship
     */
    public int getGoodsCapacity() {
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j] != null){
                    switch(components[i][j]){
                        case Load c -> {
                            goodsCapacity += c.getCapacity();
                        }
                        default -> {
                            break;
                        }
                    }
                }
            }
        }
        return goodsCapacity;
    }

    /**
     * This method has to be called after setNumAlien, if the player didn't put an alien into the cabin we fill
     * the cabin with 2 astronauts
     * @return number of astronauts in the spaceship
     */
    public void setNumHuman() {

        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    switch (components[i][j]) {
                        case Cabin c -> {
                            if (c.getAlien() == null){
                                numHuman += 2;
                                c.setQuantity(2);
                            }

                        }
                        default -> {
                            break;
                        }
                    }
                }

            }
        }
    }

    /**
     * This method
     * @return number of astronauts and aliens in the spaceship
     */
    public int getNumHuman(){
        return numHuman+numAlien;
    }

    /**
     * If the cabin is linked with a lifesupport and the player select the component this method puts the alien into
     * the cabin
     * @param coord coordinate of the components in which the player would like to put aliens
     */
    public void setNumAlien(ArrayList<int[]> coord){

        Alien alien=null;
        ArrayList<LifeSupport> ls=new ArrayList<>();
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j]!=null){
                    switch(components[i][j]){
                        case Cabin c -> {
                            if(!c.getIsMain()){
                                if(components[i+1][j]!=null){
                                    switch(components[i+1][j]){
                                        case LifeSupport c1->{
                                            int link1=components[i][j].getLink()[2];
                                            int link2=components[i+1][j].getLink()[0];
                                            if(compatibleLinks(link1,link2))  ls.add(c1);
                                        }
                                        default -> {
                                            break;
                                        }
                                    }
                                }
                                if(components[i-1][j]!=null){
                                    switch(components[i-1][j]){
                                        case LifeSupport c2->{
                                            int link1=components[i][j].getLink()[0];
                                            int link2=components[i-1][j].getLink()[2];
                                            if(compatibleLinks(link1,link2))  ls.add(c2);
                                        }
                                        default -> {
                                            break;
                                        }
                                    }
                                }
                                if(components[i][j+1]!=null){
                                    switch(components[i][j+1]){
                                        case LifeSupport c3->{
                                            int link1=components[i][j].getLink()[1];
                                            int link2=components[i-1][j].getLink()[3];
                                            if(compatibleLinks(link1,link2))  ls.add(c3);
                                        }
                                        default -> {
                                            break;
                                        }
                                    }
                                }
                                if(components[i][j-1]!=null){
                                    switch(components[i][j-1]){
                                        case LifeSupport c4->{
                                            int link1=components[i][j].getLink()[3];
                                            int link2=components[i-1][j].getLink()[1];
                                            if(compatibleLinks(link1,link2))  ls.add(c4);
                                        }
                                        default -> {
                                            break;
                                        }
                                    }
                                }
                            }
                            int[] co={i,j};
                            for(LifeSupport l:ls){
                                if(l!=null && coord.contains(c)){
                                    alien=new Alien(l.getColor());
                                    c.setAlien(alien);
                                    numAlien++;
                                }
                            }
                        }
                        default -> {
                        }
                    }
                }
            }
        }
    }

    /**
     * This method supports epidemic
     * @param x coordinate x of the component
     * @param y coordinate x of the component
     */

    public void removeHuman(int x, int y){

        switch(components[x][y]){
            case Cabin c->{
                if(numHuman>0){
                    if(c.getQuantity()>0){
                        c.removeHumans(1);
                        numHuman--;
                    }
                }
            }
            default -> {}
        }
    }

    /**
     *
     * @param coord where coord[0] coord[1] are the x coordinate of the component from which the player
     *              wants to remove a number of astronauts cointained in coord[2]
     */
    public void removeNumHuman(ArrayList<int[]> coord){

        for(int[] c:coord){
            switch (components[c[0]][c[1]]){
                case Cabin cabin -> {

                    cabin.removeHumans(c[2]);
                    numHuman -= c[2];

                }
                default -> { }
            }
        }
    }

    /**
     * @return matrix of components and empty spaces
     */
    public Component[][] getComponents() {
        return components;
    }

    public int getNumDoubleCannon(){
        return numDoubleCannon;
    }
    public int getNumDoubleEngine(){
        return numDoubleEngine;
    }

    /**
     * This method adds a component to the matrix
     * @param component component the player wants to add
     * @param x x coordinate of the cell in which the player wants to add the component
     * @param y y coordinate of the cell in which the player wants to add the component
     */
    public void addComponent(Component component, int x, int y) throws InvalidCoordinates{
        if(components[x][y]==null ) {
            if(
                    (components[x+1][y]!=null && !components[x+1][y].getType().equals("Empty Space") && compatibleLinks(component.getLink()[2],components[x+1][y].getLink()[0]))
                    || (components[x-1][y]!=null && !components[x-1][y].getType().equals("Empty Space") && compatibleLinks(component.getLink()[0],components[x-1][y].getLink()[2]))
                    || (components[x][y+1]!=null && !components[x][y+1].getType().equals("Empty Space") && compatibleLinks(component.getLink()[1],components[x][y+1].getLink()[3]))
                    || (components[x][y-1]!=null && !components[x][y-1].getType().equals("Empty Space") && compatibleLinks(component.getLink()[3],components[x][y-1].getLink()[1]))
            ){
                if(components[x+1][y]!=null)
                System.out.println("link compatibili: "+component.getLink()[2] +" e "+ components[x+1][y].getLink()[0]);
                if(components[x-1][y]!=null)
                System.out.println("link compatibili: "+component.getLink()[0] +" e "+ components[x-1][y].getLink()[2]);
                if(components[x][y+1]!=null)
                System.out.println("link compatibili: "+component.getLink()[1]+" e "+ components[x][y+1].getLink()[3]);
                if(components[x][y-1]!=null)
                System.out.println("link compatibili: "+component.getLink()[3] +" e "+ components[x][y-1].getLink()[1]);

                System.out.println("Ho controllato e sto linkando");
                components[x][y] = component;
            }
            else{
                throw new InvalidCoordinates("The component is not linked in this space");
            }
            switch (component){
                case Cannon cannon->{
                    if(cannon.getIsDouble())
                        numDoubleCannon++;
                }
                case Engine engine->{
                    if(engine.getIsDouble())
                        numDoubleEngine++;
                }
                default -> {}
            }
        }
        else throw new InvalidCoordinates("You can't place a component here");
    }

    /**
     * This method remove a component from the matrix
     * @param x x coordinate of the component to remove
     * @param y y coordinate of the component to remove
     * @return the removed component
     */
    public Component removeComponent(int x, int y) throws InvalidCoordinates {

        if(components[x][y]==null){
            throw new InvalidCoordinates("No component to be removed in this position");
        }
        Component com=components[x][y];
        switch (components[x][y]) {
            case EmptySpace c -> {}
            case Cannon cannon->{
                if(cannon.getIsDouble()) numDoubleCannon--;
                com=components[x][y];
                components[x][y] = null;
            }
            case Engine engine->{
                if(engine.getIsDouble()) numDoubleEngine--;
                com=components[x][y];
                components[x][y] = null;
            }
            default -> {
                com=components[x][y];
                components[x][y] = null;
            }
        }
        return com;
    }

    /**
     * This method calculate the single cannons power
     * @return the single cannons power
     */
    public int getSingleCannonPower() {
        int power = 0;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null ) {
                    switch(components[i][j]){
                        case Cannon c->{
                            if (!c.getIsDouble()) {
                                if (c.getOrientation() == Orientation.N) power++;
                                else power += 0.5;
                            }
                        }
                        default -> {}
                    }
                }
            }
        }
        return power;
    }

    /**
     * This method is called after the method that activates the double cannon components
     * @return the power of activated double cannons
     */
    public int getDoubleCannon() {
        int power = 0;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null ) {
                    switch(components[i][j]){
                        case Cannon c->{
                            if (c.getIsDouble() && c.getIsActive()) {

                                if (c.getOrientation() == Orientation.N) power += 2;
                                else power++;

                            }
                        }
                        default -> {}
                    }
                }
            }
        }
        return power;
    }

    /**
     * This method calculate the single engines power
     * @return the single engines power
     */
    public int getSingleEnginePower() {
        int power = 0;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null ) {
                    switch(components[i][j]){
                        case Engine e->{
                            if (!e.getIsDouble())
                                power++;
                        }
                        default -> {}
                    }
                }
            }
        }
        return power;
    }

    /**
     * This method is called after the method that activates the double engines components
     * @return the power of activated double engines
     */

    public int getDoubleEngine() {
        int doubleEngine = 0;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    switch(components[i][j]){
                        case Engine e->{
                            if (e.getIsDouble() && e.getIsActive() &&e.getOrientation() == Orientation.S);
                                doubleEngine++;
                        }
                        default -> {}
                    }
                }

            }
        }
        return doubleEngine;
    }

    /**
     * This method return the component in position (x,y)
     * @param x x coordinate of the choosen component
     * @param y coordinate of the choosen component
     * @return the component with coordinates (x,y)
     */
    public Component chooseComponent(int x, int y) {
        return components[x][y];
    }

    /**
     * This method find all the shield e calculate the protection
     * @return spaceship directions protected by the shields
     */
    public ArrayList<Direction> getProtection() {
        ArrayList<Direction> p = new ArrayList<Direction>();

        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    switch(components[i][j]){
                        case Shield s->{
                            p.add(s.getDirection());
                        }
                        default -> {}
                    }
                }
            }
        }
        return p;
    }

    /**
     * This method takes two coordinates in input e return if the component is correctly linked with the others components
     * @param x x coordinate of the component
     * @param y y coordinate of the component
     * @return boolean that says if the component in position (x,y) is correctly linked with the others components
     */
    public boolean isValid(int x, int y) {

        Cannon cannon = null;
        Component com = components[x][y];
        Component comU = null;
        Component comR = null;
        Component comD = null;
        Component comL = null;
        comU = components[x - 1][y];
        if(y+1<11) comR = components[x][y + 1];
        if(x+1<10) comD = components[x + 1][y];
        comL = components[x][y - 1];

        int linkU=0,linkR=0,linkD=0,linkL=0;
        Integer[] link = com.getLink();
        if(comU!=null){
             linkU= comU.getLink()[2];
        }
        if(comR!=null){
            linkR = comR.getLink()[3];
        }
        if(comD!=null){
            linkD = comD.getLink()[0];
        }
        if(comL!=null){
            linkL = comL.getLink()[1];
        }
        //CONTROLLO CHE U,R,D,L SIA COLLEGATO BENE
        switch (components[x][y]) {
            case Cannon c->{
                switch (c.getOrientation()) {
                    case N:
                        if (components[x - 1][y] != null) return false;
                        break;
                    case E:
                        if (components[x][y + 1] != null) return false;
                        break;
                    case S:
                        if (components[x + 1][y] != null) return false;
                        break;
                    case W:
                        if (components[x][y - 1] != null) return false;
                        break;
                }
            }
            case Engine e->{
                switch (e.getOrientation()) {
                    case N:
                        if (components[x - 1][y] != null) return false;
                        break;
                    case E:
                        if (components[x][y + 1] != null) return false;
                        break;
                    case S:
                        if (components[x + 1][y] != null) return false;
                        break;
                    case W:
                        if (components[x][y - 1] != null) return false;
                        break;
                }
            }
            default -> {}


        }

        if (!compatibleLinks(link[0], linkU)) return false;
        if (!compatibleLinks(link[1], linkR)) return false;
        if (!compatibleLinks(link[2], linkD)) return false;
        if (!compatibleLinks(link[3], linkL)) return false;
        return true;
    }

    private boolean compatibleLinks(int l1, int l2) {
        if(l1==3)
            if(l2!=0) return true;
        if(l2==3)
            if(l1!=0) return true;
        return l1 == l2 && l1!=0;
    }
//in ingresso coordinate dei cannoni doppi che vogliamo attivare (da controller)


    public void activeDoubleCannons(ArrayList<int[]> coord) throws NotEnoughBatteries {
        if(coord.size()>numOfBattery)
            throw new NotEnoughBatteries("You have not enough batteries to active all that cannons!");
        for(int[] c:coord){
            switch(components[c[0]][c[1]]){
                case Cannon cannon->{

                    if (numOfBattery>0){
                        cannon.setIsActive(true);
                        numOfBattery--;
                    }

                }default -> {}
            }
        }
    }


    /**
     * This method calculate the sum of single cannon power, double activated cannon and power given by aliens in the cabins
     * Before calling this method setting the aliens is needed
     * @return power of cannons of the spaceship
     */
    public int getTotalCannonPower() {

        int power = 0;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    switch(components[i][j]){
                        case Cabin cabin->{
                            if (cabin.getAlien() != null) {
                                if( cabin.getAlien().getColor() == AlienColor.PURPLE) {
                                    power += 2;
                                }
                            }
                        }
                        default -> {}
                    }
                }
            }
        }
        return getSingleCannonPower() + getDoubleCannon() + power;
    }

    public void deactivateDoubleCannons() {
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null ) {
                    switch(components[i][j]){
                        case Cannon c->{
                            if (c.getIsDouble() && c.getIsActive()) {
                                c.setIsActive(false);
                            }
                        }
                        default -> {}
                    }
                }
            }
        }
    }

    public void deactivateDoubleEngines() {
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null ) {
                    switch(components[i][j]){
                        case Engine e->{
                            if (e.getIsDouble() && e.getIsActive()) {
                                e.setIsActive(false);
                            }
                        }
                        default -> {}
                    }
                }
            }
        }
    }

    public void activateDoubleEngines(ArrayList<int[]> coord, ArrayList<int[]> batteriesCoord) throws NotEnoughBatteries {
        if(coord.size()>numOfBattery)
            throw new NotEnoughBatteries("You don't have enough batteries to activate all that engines!");
        for(int[] c:coord){
            switch(components[c[0]][c[1]]){
                case Engine engine->{
                    engine.setIsActive(true);
                    numOfBattery--;
                    for(int[] bat:batteriesCoord){
                        if(components[bat[0]][bat[1]]!=null){
                            switch(components[bat[0]][bat[1]]){
                                case BatteryStorage bs->{
                                    if(bs.getQuantity()>0) bs.removeNumOfBatteries(1);
                                }
                                default -> {}
                            }
                        }
                    }
                }default -> {}
            }

        }
    }

    public void activateDoubleCannons(ArrayList<int[]> coord, ArrayList<int[]> batteriesCoord) throws NotEnoughBatteries {
        if(coord.size()>numOfBattery)
            throw new NotEnoughBatteries("You don't have enough batteries to activate all that engines!");
        for(int[] c:coord){
            switch(components[c[0]][c[1]]){
                case Cannon cannon->{
                    cannon.setIsActive(true);
                    numOfBattery--;
                    for(int[] bat:batteriesCoord){
                        if(components[bat[0]][bat[1]]!=null){
                            switch(components[bat[0]][bat[1]]){
                                case BatteryStorage bs->{
                                    if(bs.getQuantity()>0) bs.removeNumOfBatteries(1);
                                }
                                default -> {}
                            }
                        }
                    }
                }default -> {}
            }

        }
    }

    public void activateShield(int[] shieldCoord, int[] batteryCoord){
        switch(components[shieldCoord[0]][shieldCoord[1]]){
            case Shield shield->{
                shield.setIsActive(true);
            }
            default -> {}
        }
        switch(components[batteryCoord[0]][batteryCoord[1]]){
            case BatteryStorage bs->{
                bs.removeNumOfBatteries(1);
            }
            default -> {}
        }
    }

    public void deActivateShields(){
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null ) {
                    switch(components[i][j]){
                        case Shield shield->{
                            if (shield.getIsActive()) {
                                shield.setIsActive(false);
                            }
                        }
                        default -> {}
                    }
                }
            }
        }
    }




    /**
     * This method calculate the sum of single engine power, double activated engine and power given by aliens in the cabins
     * Before calling this method setting the aliens is needed
     * @return power of engine of the spaceship
     */
    public int getTotalEnginePower() {

        int power = 0;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    switch(components[i][j]){
                        case Cabin cabin->{
                            if (cabin.getAlien() != null && cabin.getAlien().getColor() == AlienColor.BROWN) {
                                power += 2;
                            }
                        }
                        default -> {}
                    }

                }
            }
        }
        return power + getSingleEnginePower() + getDoubleEngine();
    }



    /**
     * @return number of exposed connectors in the entire spaceship
     */
    public int countExposedConnectors() {
        int cont = 0;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    switch(components[i][j]){
                        case EmptySpace emptySpace->{}
                        default -> {
                            Component com = components[i][j];
                            Component comU = components[i - 1][j];
                            Component comR = components[i][j + 1];
                            Component comD = components[i + 1][j];
                            Component comL = components[i][j - 1];
                            if (comU == null) cont++;
                            if (comR == null) cont++;
                            if (comD == null) cont++;
                            if (comL == null) cont++;
                        }
                    }

                }
            }
        }
        return cont;
    }

    /**
     * @return true if all the components are correctly linked
     */
    public boolean wellDone() {
        boolean wellDone=true;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j]!=null && !isValid(i, j))
                    try{
                        removeComponent(i,j);
                    } catch (InvalidCoordinates e) {
                        throw new RuntimeException(e);
                    }
                    wellDone=false;
            }
        }
        if(!wellDone){
            componentsVisited(7,7);
            removeUnlinkedComponents();
        }
        return wellDone;
    }

    /**
     * This method controll if the spaceship is hit
     * @param direction direction of the attack
     * @param num row or colon of the attack
     * @return true if the spaceship is hit else false
     */
    public boolean isHit(int direction, int num) {
        switch (direction) {
            case 0:
                for (int i = 5; i <= 9; i++) {
                    if (components[i][num] != null){
                        switch(components[i][num]){
                            case EmptySpace emptySpace->{}
                            default -> {return true;}
                        }
                    }
                }
                break;
            case 1:
                for (int j = 10; j > 3; j--) {
                    if (components[num][j] != null) {
                        switch(components[num][j]){
                            case EmptySpace emptySpace->{}
                            default -> {return true;}
                        }
                    }
                }
                break;
            case 2:
                for (int i = 9; i > 4; i--) {
                    if (components[i][num] != null) {
                        switch(components[i][num]){
                            case EmptySpace emptySpace->{}
                            default -> {return true;}
                        }
                    }
                }
                break;
            case 3:
                for (int j = 4; j <= 10; j++) {
                    if (components[num][j] != null) {
                        switch(components[num][j]){
                            case EmptySpace emptySpace->{}
                            default -> {return true;}
                        }
                    }
                }
                break;
        }
        return false;
    }

    /**
     * This method remove a component destroyed by a big meteorite
     * @param num number of row or colon from which the meteorite arrives
     * @param direction Direction 0-->N, 1-->E, 2-->S, 3-->W from which the meteorite arrives
     */
    public void destroyComponent(int num, int direction, Bank bank) {
        Component c=null;
        int x=0; int y=0;
        switch (direction) {
            case 0:
                for (int i = 5; i <= 9; i++) {
                    if (components[i][num] != null) {

                        switch(components[i][num]){
                            case EmptySpace emptySpace->{}
                            default -> {
                                c=components[i][num];
                                x=i;
                                y=num;
                                try{
                                    removeComponent(i, num);
                                } catch (InvalidCoordinates e) {
                                    throw new RuntimeException(e);
                                }
                                lost++;
                            }
                        }


                    }
                }
                break;
            case 1:
                for (int j = 10; j > 3; j--) {
                    if (components[num][j] != null) {
                        switch(components[num][j]){
                            case EmptySpace emptySpace->{}
                            default -> {
                                c=components[num][j];
                                x=num;
                                y=j;
                                try{
                                    removeComponent(num, j);
                                } catch (InvalidCoordinates e) {
                                    throw new RuntimeException(e);
                                }
                                lost++;
                            }
                        }

                    }
                }
                break;
            case 2:
                for (int i = 9; i > 4; i--) {
                    if (components[i][num] != null) {
                        switch(components[i][num]){
                            case EmptySpace emptySpace->{}
                            default -> {
                                c=components[i][num];
                                x=i;
                                y=num;
                                try{
                                    removeComponent(i, num);
                                } catch (InvalidCoordinates e) {
                                    throw new RuntimeException(e);
                                }
                                lost++;
                            }
                        }

                    }
                }
                break;
            case 3:
                for (int j = 4; j <= 10; j++) {
                    if (components[num][j] != null) {
                        switch(components[num][j]){
                            case EmptySpace emptySpace->{}
                            default -> {
                                c=components[num][j];
                                x=num;
                                y=j;
                                try{
                                    removeComponent(num, j);
                                } catch (InvalidCoordinates e) {
                                    throw new RuntimeException(e);
                                }
                                lost++;

                            }
                        }

                    }
                }
                break;
        }
        if(c!=null) {
            switch(c){
                case Cabin cabin->{
                    if(cabin.getAlien() != null) {
                        numAlien--;
                    }
                    else if(cabin.getQuantity()>0){
                        numHuman-= cabin.getQuantity();
                    }
                }
                case Load load->{
                    goodsCapacity-= load.getCapacity();
                    if(load.getQuantity()>0){

                        for(Goods g:goodsList){
                            if(g.getX()==x && g.getY()==y){
                                if(g.getValue()==4) redGoods--;
                                else if(g.getValue()==3) yellowGoods--;
                                else if(g.getValue()==2) greenGoods--;
                                else if(g.getValue()==1) blueGoods--;
                                goodsList.remove(g);
                                bank.addGoodsInBank(g.getValue());
                            }
                        }
                    }
                }
                case BatteryStorage batteryStorage->{
                    if(batteryStorage.getQuantity()>0){
                        numOfBattery-=batteryStorage.getQuantity();
                    }
                }
                case LifeSupport lifeSupport->{
                    switch(components[x+1][y]){
                        case Cabin cabin->{
                            if(cabin.getAlien() != null) { numAlien--;}
                        }default -> {}
                    }
                    switch(components[x-1][y]){
                        case Cabin cabin->{
                            if(cabin.getAlien() != null) { numAlien--;}
                        }default -> {}
                    }
                    switch(components[x][y+1]){
                        case Cabin cabin->{
                            if(cabin.getAlien() != null) { numAlien--;}
                        }default -> {}
                    }
                    switch(components[x][y-1]){
                        case Cabin cabin->{
                            if(cabin.getAlien() != null) { numAlien--;}
                        }default -> {}
                    }

                }default -> {}
            }
        }
        componentsVisited(7,7);
        removeUnlinkedComponents();
    }

    /**
     * Starting from the main cabin we marke as visited every component that is reachable through a path that starts
     * in the main cabin, every component that isn't reachable from the main cabin will be left as not Visited
     * @param x: coordinate x of the main cabin
     * @param y: coordinate y of the main cabin
     */
    private void componentsVisited(int x, int y){
        // caso base della funzione che è ricorsiva
        // 1. sto uscendo dai bordi della matrice
        // 2. sono in un component null o vuoto o è un componente già visitato che è diverso dalla cabina principale
        // il fatto della cabina principale è dovuto al fatto che non è un albero ma un grafo e se setto la cabina principale
        // a visited allora non riesco a esplorare tutto perchè appena ritorno alla main cabin è visited e quindi finisco
        // per settare la cabina principale a visited uso il terzo if

        if (x < 0 || x > components.length || y < 0 || y > components[0].length) {
            return;
        }
        if(components[x][y] == null  || (components[x][y].isVisited() && (x!=7 && y!=7))) {
            return;
        }
        switch(components[x][y]){
            case EmptySpace emptySpace->{
                return;
            }default -> {}
        }
        if (components[6][7] != null && components[7][6] != null && components[8][7] != null && components[7][8] != null){
            if(components[6][7].isVisited() && components[7][6].isVisited() && components[8][7].isVisited() && components[7][8].isVisited())
                components[7][7].setVisited(true);
        }

        components[x][y].setVisited(true);
        componentsVisited(x+1, y);
        componentsVisited(x, y+1);
        componentsVisited(x, y-1);
        componentsVisited(x-1, y);
    }

    /**
     * This method remove an unlinked component which is not reachable from the main cabin, then it sets all the
     * component as unvisited for the next check. It's usually called when a component is destroyed by a cannon shot
     * or a meteorite
     */
    private void removeUnlinkedComponents(){
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j] != null ) {
                    switch(components[i][j]){
                        case EmptySpace emptySpace->{}
                        default -> {
                            if(!components[i][j].isVisited()) {
                                try {
                                    removeComponent(i, j);
                                } catch (InvalidCoordinates e) {
                                    throw new RuntimeException(e);
                                }
                                lost++;
                            }
                        }
                    }

                }
            }
        }
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j] != null) {
                    switch(components[i][j]){
                        case EmptySpace emptySpace->{}
                        default -> {
                            components[i][j].setVisited(false);
                        }
                    }
                }
            }
        }
    }

    /**
     * This method says if there is a cannon which can shoot the arriving meteorite
     * @param direction Direction 0-->N, 1-->E, 2-->S, 3-->W from which the meteorite arrives
     * @param num number of row or colon from which the meteorite arrives
     * @return true if there is a cannon which can shoot the arriving meteorite, false otherwise
     */
    public boolean shootMeteor(int direction, int num) {
        switch (direction) {
            case 0:
                for (int i = 5; i <= 9; i++) {
                    if (components[i][num] != null) {
                        switch(components[i][num]){
                            case Cannon cannon->{
                                if (cannon.getOrientation() == Orientation.N)
                                    return true;
                            }default->{}
                        }
                    }
                }
                break;
            case 1:
                for (int j = 10; j > 3; j--) {
                    if (components[num][j] != null) {
                        switch(components[num][j]){
                            case Cannon cannon->{
                                if (cannon.getOrientation() == Orientation.E)
                                    return true;
                            } default->{}
                        }
                    }
                }
                break;
            case 2:
                for (int i = 9; i > 4; i--) {
                    if (components[i][num] != null) {
                        switch(components[i][num]){
                            case Cannon cannon->{
                                if (cannon.getOrientation() == Orientation.S)
                                    return true;
                            } default -> {}
                        }
                    }
                }
                break;
            case 3:
                for (int j = 4; j <= 10; j++) {
                    if (components[num][j] != null) {
                        switch(components[num][j]){
                            case Cannon cannon->{
                                if (cannon.getOrientation() == Orientation.W)
                                    return true;
                            } default -> {}
                        }
                    }
                }
                break;
        }
        return false;
    }

    /**
     * This method returns if the spaceship can block a small meteorite, with a cannon or if it doesn't
     * have exposed links
     * @param direction Direction 0-->N, 1-->E, 2-->S, 3-->W from which the meteorite arrives
     * @param num number of row or colon from which the meteorite arrives
     * @return true if the spaceshio doesn't have exposed links or has a cannon where is hit by the small meteorite
     */
    public boolean blockSmallMeteor(int direction, int num, boolean use) {


            switch (direction) {
                case 0:
                    for (int i = 5; i <= 9; i++) {
                        if (components[i][num] != null) {
                            switch(components[i][num]){
                                case EmptySpace emptySpace->{}
                                default -> {
                                    if (components[i][num].getLink()[0] == 0)
                                        return true;
                                }
                            }
                        }
                    }
                    break;
                case 1:
                    for (int j = 10; j > 3; j--) {
                        if (components[num][j] != null) {
                            switch(components[num][j]){
                                case EmptySpace emptySpace->{}
                                default -> {
                                    if (components[num][j].getLink()[1] == 0)
                                        return true;
                                }
                            }

                        }
                    }
                    break;
                case 2:
                    for (int i = 9; i > 4; i--) {
                        if (components[i][num] != null) {
                            switch(components[i][num]){
                                case EmptySpace emptySpace->{}
                                default -> {
                                    if (components[i][num].getLink()[2] == 0)
                                        return true;
                                }
                            }

                        }
                    }
                    break;
                case 3:
                    for (int j = 4; j <= 10; j++) {
                        if (components[num][j] != null) {
                            switch(components[num][j]){
                                case EmptySpace emptySpace->{}
                                default -> {
                                    if (components[num][j].getLink()[3] == 0)
                                        return true;
                                }
                            }
                        }
                    }
                    break;
            }

        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    switch(components[i][j]) {
                        case Shield shield->{
                            if(
                                    (direction == 0 && (shield.getDirection().equals(Direction.NE) || shield.getDirection().equals(Direction.NW)))
                                            || (direction == 1 && (shield.getDirection().equals(Direction.NE) || shield.getDirection().equals(Direction.SE)))
                                            || (direction == 2 && (shield.getDirection().equals(Direction.SE) || shield.getDirection().equals(Direction.SW)))
                                            || (direction == 3 && (shield.getDirection().equals(Direction.SW) || shield.getDirection().equals(Direction.NW)))
                            ){
                                if(numOfBattery>0 && use){
                                    return true;
                                }
                            }
                        }
                        default -> {}
                    }

                }
            }
        }


        return false;
    }

    /**
     * This method is used if it's already known that the spaceship will be hit by the small meteorite,
     * if the spaceship has protection in the critical direction, the player can choose to use or not the
     * shield. This is possible only if there is enough batteries to use the shield.
     * @param direction Direction 0-->N, 1-->E, 2-->S, 3-->W from which the meteorite arrives
     * @param num number of row or colon from which the meteorite arrives
     * @param use true if the player wants to use the shield, false otherwise
     * @return true if the spaceship is protected
     */
    public boolean blockLightCannonShot(int direction,int num, boolean use){

        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    switch(components[i][j]){
                        case Shield shield->{
                            if(
                                    (direction == 0 && (shield.getDirection().equals(Direction.NE) || shield.getDirection().equals(Direction.NW)))
                                            || (direction == 1 && (shield.getDirection().equals(Direction.NE) || shield.getDirection().equals(Direction.SE)))
                                            || (direction == 2 && (shield.getDirection().equals(Direction.SE) || shield.getDirection().equals(Direction.SW)))
                                            || (direction == 3 && (shield.getDirection().equals(Direction.SW) || shield.getDirection().equals(Direction.NW)))
                            ){
                                if(numOfBattery>0 && use){
                                    return true;
                                }
                            }
                        }
                        default -> {}
                    }

                }
            }
        }
        return false;
    }


    /**
     * This method remove an astronauts from each non empty cabin linked to an other non empty cabin
     */
    public void epidemic() {
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j] != null) {
                    switch (components[i][j]){
                        case Cabin cabin->{
                            if(cabin.getQuantity()>0){
                                switch(components[i+1][j]){
                                    case Cabin cabin1->{
                                        if(cabin1.getQuantity()>0 && compatibleLinks(cabin.getLink()[2], cabin.getLink()[0])){
                                            removeHuman(i,j);
                                            return;
                                        }
                                    }default -> {}
                                }
                                switch(components[i][j+1]){
                                    case Cabin cabin1->{
                                        if(cabin1.getQuantity()>0 && compatibleLinks(cabin.getLink()[1], cabin.getLink()[3])){
                                            removeHuman(i,j);
                                            return;
                                        }
                                    }default -> {}
                                }
                                switch(components[i-1][j]){
                                    case Cabin cabin1->{
                                        if(cabin1.getQuantity()>0 && compatibleLinks(cabin.getLink()[0], cabin.getLink()[2])){
                                            removeHuman(i,j);
                                            return;
                                        }
                                    }default -> {}
                                }
                                switch(components[i][j-1]){
                                    case Cabin cabin1->{
                                        if(cabin1.getQuantity()>0 && compatibleLinks(cabin.getLink()[3], cabin.getLink()[1])){
                                            removeHuman(i,j);
                                            return;
                                        }
                                    }default -> {}
                                }
                            }
                        }default -> {}
                    }
                }
            }
        }
    }



    /**
     * This method adds a good in the goodsList in a way that keeps it sorted in descending order
     * based on goods value
     * @param good the good that needs to be added to goodsList
     */
    public void addGoodsList(Goods good){
        int i = 0;
        while (i < goodsList.size() && goodsList.get(i).getValue() > good.getValue()) {
            i++;
        }
        goodsList.add(i, good);
    }

    /**
     * This method insert a list of goods in the Load components choosen by the player
     * @param goods list of goods the player wants to add
     * @param coord coordinates of Load components
     */
    public void addGoods(List <Goods> goods,ArrayList<int[]> coord,Bank bank){
        int index=0;
        for(int[] c : coord){
            int i=c[0];
            int j=c[1];
            if (components[i][j] != null) {
                switch(components[i][j]){
                    case Load load->{
                        while(load.getCapacity()-load.getQuantity()>0 && index<goods.size()){
                            if (load.getIsSpecial() && goods.get(index).getValue() == 4) {
                                load.addGood(goods.get(index));
                                addGoodsList(goods.get(index));
                                bank.removeGoodsInBank(goods.get(index).getValue());
                                index++;
                            }
                            else if (goods.get(index).getValue() != 4) {
                                load.addGood(goods.get(index));
                                addGoodsList(goods.get(index));
                                bank.removeGoodsInBank(goods.get(index).getValue());
                                index++;
                            }
                        }
                    }default -> {}
                }
            }
        }

        for(int j = index; j<goods.size(); j++){
            for(int i=goodsList.size()-1;i>=0;i--){
                if(goods.get(j).getValue() >goodsList.get(i).getValue()){
                    if(goods.get(j).getValue() == 4){
                            // vedi se c'è spazio per un rosso e poi inserisci
                        for(Goods g : goodsList){
                            if(g.getValue() < 4){
                                switch(components[g.getX()][g.getY()]){
                                    case Load l->{
                                        if(l.getIsSpecial()){
                                            l.removeGood(g);
                                            goodsList.remove(i);
                                            bank.addGoodsInBank(g.getValue());
                                            l.addGood(goods.get(j));
                                            addGoodsList(goods.get(j));
                                            bank.removeGoodsInBank(goods.get(j).getValue());
                                        }
                                    }
                                    default -> {}
                                }


                            }
                        }
                    }
                    else{
                            //inserisci in qualsiasi spazio vuoto
                        goods.get(j).setX(goodsList.get(i).getX());
                        goods.get(j).setY(goodsList.get(i).getY());
                        switch(components[goodsList.get(i).getX()][goodsList.get(i).getY()]){
                            case Load loadToRemove->{
                                bank.addGoodsInBank(goodsList.get(i).getValue());
                                loadToRemove.removeGood(goodsList.get(i));
                                bank.removeGoodsInBank(goods.get(j).getValue());
                                loadToRemove.addGood(goods.get(j));
                                goodsList.remove(i);
                                addGoodsList(goods.get(j));
                            }default -> {}
                        }
                    }
                }
            }
        }


    }

    /**
     * This method adds the specified good in the specified component, also the position is given.
     * If there's space in the load it just add the good, otherwise we have to remove the good that occupies the gived position
     * @param good good the user wants to add
     * @param coord coordinate of the load
     * @param value position in the load
     * @throws InvalidPositionException if the good is special but not the choosen load
     */
    public Goods addGood(Goods good,int[] coord,int value ,Bank bank) throws InvalidPositionException{
        if(components[coord[0]][coord[1]]!=null){
            switch(components[coord[0]][coord[1]]){
                case Load load->{
                    if(!load.getIsSpecial() && good.getValue() == 4){
                        throw new InvalidPositionException("Special load expected");
                    }
                    if(load.getCapacity()-load.getQuantity()>0){
                        load.getGoods().add(value,good);
                        bank.removeGoodsInBank(good.getValue());
                        addGoodsList(good);
                    }
                    else{
                        Goods g=load.getGoods().get(value);
                        load.getGoods().remove(g);
                        bank.addGoodsInBank(g.getValue());
                        load.getGoods().add(value,good);
                        bank.removeGoodsInBank(good.getValue());
                        goodsList.remove(g);
                        addGoodsList(good);
                        return g;
                    }
                }
                default -> {}
            }
        }
        return null;
    }

    /**
     * This method
     * @return the list of goods in the spaceship
     */
    public ArrayList<Goods> getGoodsList(){
        return goodsList;
    }

    /**
     * This method
     * @return the number of lost components
     */
    public int getLost(){
        return lost;
    }

    /**
     * This method remove a number of batteries from some Battery Storage component, the number and the
     * components are choosen by the player
     * @param batteryCoordinates where batteryCoordinates[0] and batteryCoordinates[1] are the x coordinate
     *                           of the component from which the player wants to remove a number of batteries
     *                           cointained in batteryCoordinates[2]
     */
    public void removeBatteries(ArrayList<int[]> batteryCoordinates)throws NotEnoughBatteries {

        for(int[] c:batteryCoordinates){
            switch (components[c[0]][c[1]]){
                case BatteryStorage bs->{
                    if(bs.getQuantity() >= c[2]) {
                        bs.setQuantity(bs.getQuantity() - c[2]);
                        numOfBattery -= c[2];
                    }
                    else {
                        throw new NotEnoughBatteries("There are no enought batteries in the selected storage ( "+c[0]+" , "+c[1]+" )");
                    }
                }default -> {}
            }
        }
    }

    /**
     * This method remove all the goods from the spaceship
     */
    public void removeAllGoods(){
        ArrayList<Goods> c=new ArrayList<>();
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j] != null){
                    switch(components[i][j]){
                        case Load load->{
                            for(Goods g: load.getGoods()){
                                c.add(g);
                            }
                        }
                        default -> {}
                    }
                }
            }
        }
        removeGoods(c);
    }

    /**
     * This method returns the number of batteries in the spaceship
     * @return number of batteries in the spaceship
     */
    public int getNumBatteries(){
        return numOfBattery;
    }

    /**
     * This method checks that components with given coordinates are Loads
     * @param coord coordinates to check
     * @return true if every component with given coordinates is a Loads, false otherwise
     */
    //TODO CAMBIA OGNI CHECK
    public boolean checkLoad(ArrayList<int[]> coord){

        for (int[] c : coord) {
            if (components[c[0]][c[1]] != null) {
                switch (components[c[0]][c[1]]){
                    case Load load->{}
                    default -> { return false; }
                }
            }
            else return false;
        }
        return true;
    }

    public boolean checkLoad(int[] c){

        if (components[c[0]][c[1]] != null) {
            switch (components[c[0]][c[1]]){
                case Load load->{}
                default -> { return false; }
            }
        }else return false;
        return true;
    }

    /**
     * This method checks that components with given coordinates are Engines
     * @param coord coordinates to check
     * @return true if every component with given coordinates is a Engines, false otherwise
     */
    public boolean checkDoubleEngine(ArrayList<int[]> coord){

        for (int[] c : coord) {
            if (components[c[0]][c[1]] != null) {
                switch (components[c[0]][c[1]]){
                    case Engine engine->{
                        if(!engine.getIsDouble()) return false;
                    }
                    default -> { return false; }
                }
            }else return false;
        }
        return true;
    }

    /**
     * This method checks that components with given coordinates are Cannons
     * @param coord coordinates to check
     * @return true if every component with given coordinates is a Cannons, false otherwise
     */
    public boolean checkDoubleCannon(ArrayList<int[]> coord){

        for (int[] c : coord) {
            if (components[c[0]][c[1]] != null) {
                switch (components[c[0]][c[1]]){
                    case Cannon cannon->{
                        if(!cannon.getIsDouble()) return false;
                    }
                    default -> { return false; }
                }
            }else return false;
        }
        return true;
    }

    /**
     * This method checks that components with given coordinates are Shields
     * @param c coordinates to check
     * @return true if the component with given coordinates is a Shields, false otherwise
     */
    public boolean checkShield(int[] c){
        if (components[c[0]][c[1]] != null) {
            switch (components[c[0]][c[1]]){
                case Shield shield->{}
                default -> { return false; }
            }
        }else return false;
        return true;
    }

    /**
     * This method checks that components with given coordinates are Battery Storages
     * @param coord coordinates to check
     * @return true if every component with given coordinates is a Battery Storages, false otherwise
     */
    public boolean checkBatteryStorage(ArrayList<int[]> coord){
        for (int[] c : coord) {
            if (components[c[0]][c[1]] != null) {
                switch (components[c[0]][c[1]]){
                    case BatteryStorage bs->{}
                    default -> { return false; }
                }
            }else return false;
        }
        return true;
    }

    /**
     * This method checks that components with given coordinates are Cabins
     * @param coord coordinates to check
     * @return true if every component with given coordinates is a Cabins, false otherwise
     */
    public boolean checkCabin(ArrayList<int[]> coord, int sum) throws InvalidCoordinates{
        int s=0;
        for (int[] c:coord){
            s+=c[2];
        }
        if (s<sum) throw new InvalidCoordinates("No enough human selected");

        for (int[] c : coord) {
            if (components[c[0]][c[1]] != null) {
                switch (components[c[0]][c[1]]){
                    case Cabin cabin->{
                        if(cabin.getQuantity() < c[2]) {
                            throw new InvalidCoordinates("You haven't enough astronauts in this cabin");
                        }
                    }
                    default -> {
                        throw new InvalidCoordinates("No cabin here");
                    }
                }
            }else return false;
        }
        return true;
    }

    /**
     * This method swap two goods from two different loads
     * @param coord1 coordinate of the first load
     * @param value1 position of the first load il the first load
     * @param coord2 coordinate of the second load
     * @param value2 position of the second load il the second load
     * @throws InvalidPositionException if in that coordinates there is no load
     */
    public void swapGoods(int[] coord1, int value1, int[] coord2, int value2) throws InvalidPositionException{
        Goods g1=null;
        Goods g2=null;
        if(components[coord1[0]][coord1[1]] != null){
            switch(components[coord1[0]][coord1[1]]){
                case Load load->{
                    g1=load.getGoods().get(value1);
                    load.getGoods().remove(g1);
                    if(components[coord2[0]][coord2[1]] != null){
                        switch(components[coord2[0]][coord2[1]]){
                            case Load load1->{
                                g2=load1.getGoods().get(value2);
                                load1.getGoods().remove(g2);
                                load1.getGoods().add(value2,g1);
                                load.getGoods().add(value1,g2);
                            }
                            default -> {
                                throw new InvalidPositionException("Load expected in this position");
                            }
                        }
                    }
                }
                default -> {
                    throw new InvalidPositionException("Load expected in this position");
                }
            }
        }

    }

    public int getRedGoods(){
        return redGoods;
    }
    public int getGreenGoods(){
        return greenGoods;
    }
    public int getBlueGoods(){
        return blueGoods;
    }
    public int getYellowGoods() {
        return yellowGoods;
    }

    public void removeGoods(ArrayList<Goods> list){
        for(Goods g: list){
            switch(components[g.getX()][g.getY()]){
                case Load load->{
                    load.removeGood(g);
                }
                default -> {}
            }
        }
    }

    public ArrayList<int[]> getAlienCabin() {
        ArrayList<int[]> alienCabin = new ArrayList<>();
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++){
                if(components[i][j]!=null){
                    switch(components[i][j]){
                        case Cabin c -> {
                            if(!c.getIsMain()){
                                if(components[i+1][j]!=null){
                                    switch(components[i+1][j]){
                                        case LifeSupport c1->{
                                            int link1=components[i][j].getLink()[2];
                                            int link2=components[i+1][j].getLink()[0];
                                            if(compatibleLinks(link1,link2)) alienCabin.add(new int[]{i,j});
                                        }
                                        default -> {
                                            break;
                                        }
                                    }
                                }
                                if(components[i-1][j]!=null){
                                    switch(components[i-1][j]){
                                        case LifeSupport c2->{
                                            int link1=components[i][j].getLink()[0];
                                            int link2=components[i-1][j].getLink()[2];
                                            if(compatibleLinks(link1,link2))  alienCabin.add(new int[]{i,j});
                                        }
                                        default -> {
                                            break;
                                        }
                                    }
                                }
                                if(components[i][j+1]!=null){
                                    switch(components[i][j+1]){
                                        case LifeSupport c3->{
                                            int link1=components[i][j].getLink()[1];
                                            int link2=components[i-1][j].getLink()[3];
                                            if(compatibleLinks(link1,link2))  alienCabin.add(new int[]{i,j});
                                        }
                                        default -> {
                                            break;
                                        }
                                    }
                                }
                                if(components[i][j-1]!=null){
                                    switch(components[i][j-1]){
                                        case LifeSupport c4->{
                                            int link1=components[i][j].getLink()[3];
                                            int link2=components[i-1][j].getLink()[1];
                                            if(compatibleLinks(link1,link2))  alienCabin.add(new int[]{i,j});
                                        }
                                        default -> {
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        default -> {
                        }
                    }
                }
            }
        }
        return alienCabin;
    }
}
