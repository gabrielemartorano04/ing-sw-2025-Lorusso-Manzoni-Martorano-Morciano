package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.model.components.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Class that manage the Trucker's SpaceShip, with its components, goods, astronauts, aliens
 */
public class SpaceShip implements Serializable {

    /**
     * Components in the spaceship as a matrix
     */
    private final Component[][] components;

    /**
     * Number of lost components
     */
    private int lost;

    /**
     * Number of goods for each color
     */
    private int redGoods, greenGoods, blueGoods, yellowGoods;

    /**
     * Number of astronauts in the spaceship
     */
    private int numHuman;

    /**
     * Spaceship goods capacity
     */
    private int goodsCapacity;

    /**
     * List of goods in the spaceship, sort in descending order by their value
     */
    private final ArrayList<Goods> goodsList;

    /**
     * Number of batteries in the spaceship
     */
    private int numOfBattery;

    /**
     * Number of aliens in the spaceship
     */
    private int numAlien;

    /**
     * Number of double cannons and number of double engines
     */
    private int numDoubleCannon, numDoubleEngine;

    /**
     * Serial version
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * It tells if there is an activeShield in the spaceship
     */
    private boolean existsAnActiveShield;

    /**
     * Class constructor, it initializes the attributes of this class.
     */
    public SpaceShip() {
        components = new Component[11][12];
        for(int i=0;i<5;i++){
            for(int j=0;j<11;j++){
                components[i][j]=new EmptySpace();
            }
        }
        int y=0;
        while (y<12){
            components[10][y]=new EmptySpace();
            y++;
        }
        y=0;
        while (y<11){
            components[y][11]=new EmptySpace();
            y++;
        }

        lost = 0;
        redGoods = 0;
        greenGoods = 0;
        blueGoods = 0;
        yellowGoods = 0;
        goodsList = new ArrayList<>();
        numHuman = 0;
        numAlien=0;
        numOfBattery = 0;
        numDoubleCannon=0;
        numDoubleEngine=0;
        this.existsAnActiveShield = false;
    }

    /**
     * This method initializes the matrix. Component are null if that position can be filled,
     * otherwise it's an empty space. It also places the main cabin in the middle of the spaceship
     * @param level game level
     * @param color player color
     */
    public void initSpaceShip(int level, PlayerColors color){
        if(level==0){
            for(int i=5;i<=9;i++){
                for(int j=4;j<=10;j++){
                    if(j==4 || j==10) components[i][j]=new EmptySpace();
                    else if(j==5 || j==9){
                        if(i==5 || i==6)
                            components[i][j]=new EmptySpace();
                        else components[i][j]=null;
                    }
                    else if(j==6 || j==8){
                        if(i==5){
                            components[i][j]=new EmptySpace();
                        }
                        else components[i][j]=null;
                    }
                    else if(j==7){
                        if(i==9) components[i][j]=new EmptySpace();
                        else components[i][j]=null;
                    }
                }
            }
        }
        else{
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
        }
        Cabin cabin=null;
        switch(color){
            case BLUE -> {
                cabin = new Cabin(3, 3, 3, 3, "Cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web33.jpg", PlayerColors.BLUE, true);
            }
            case YELLOW -> {
                cabin = new Cabin(3, 3, 3, 3, "Cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web61.jpg", PlayerColors.YELLOW, true);
            }
            case RED -> {
                cabin = new Cabin(3, 3, 3, 3, "Cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web52.jpg", PlayerColors.RED, true);
            }
            case GREEN -> {
                cabin = new Cabin(3, 3, 3, 3, "Cabin", "/it/polimi/ingsw/model/components/tiles/GT-new_tiles_16_for web34.jpg", PlayerColors.GREEN, true);
            }
        }
        components[7][7] = cabin;
    }

    /**
     * This method increase the number of lost components
     * @param lost number of lost components to add
     */
    public void increaseLost(int lost) {
        this.lost += lost;
    }

    /**
     * This method set the full number of batteries in each battery storage and
     * updates numOfBattery counter to its maximum
     */
    public void setBattery(){
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j] != null){
                    switch(components[i][j]){
                        case BatteryStorage c -> {
                            numOfBattery += c.getCapacity();
                            c.setQuantity(c.getCapacity());
                        }
                        default -> {
                        }
                    }
                }
            }
        }
    }

    /**
     * This method has to be called after setNumAlien, if the player didn't put an alien into the cabin we fill
     * the cabin with 2 astronauts
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
                        default -> {}
                    }
                }

            }
        }
    }

    /**
     * This method returns goods in spaceship
     * @return number of goods for each color
     */
    public Map<String, Integer> getGoodsByColor() {
        Map<String, Integer> goodsByColor = new HashMap<>();

        for (Goods goods : goodsList) {
            String color = goods.getColor();
            goodsByColor.put(color, goodsByColor.getOrDefault(color, 0) + 1);
        }

        return goodsByColor;
    }


    /**
     * This method returns the number of crew members in the spaceship
     * @return number of astronauts and aliens in the spaceship
     */
    public int getNumCrew(){
        return numHuman+numAlien;
    }

    /**
     * If the cabin is linked with a life support and the player select the component this method puts the alien into
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
                                        default -> {}
                                    }
                                }
                                if(components[i-1][j]!=null){
                                    switch(components[i-1][j]){
                                        case LifeSupport c2->{
                                            int link1=components[i][j].getLink()[0];
                                            int link2=components[i-1][j].getLink()[2];
                                            if(compatibleLinks(link1,link2))  ls.add(c2);
                                        }
                                        default -> {}
                                    }
                                }
                                if(components[i][j+1]!=null){
                                    switch(components[i][j+1]){
                                        case LifeSupport c3->{
                                            int link1=components[i][j].getLink()[1];
                                            int link2=components[i][j+1].getLink()[3];
                                            if(compatibleLinks(link1,link2))  ls.add(c3);
                                        }
                                        default -> { }
                                    }
                                }
                                if(components[i][j-1]!=null){
                                    switch(components[i][j-1]){
                                        case LifeSupport c4->{
                                            int link1=components[i][j].getLink()[3];
                                            int link2=components[i][j-1].getLink()[1];
                                            if(compatibleLinks(link1,link2))  ls.add(c4);
                                        }
                                        default -> {}
                                    }
                                }
                            }
                            for(LifeSupport l:ls){
                                int[] currentCoord = {i, j};
                                for (int[] selectedCoord : coord) {
                                    if (selectedCoord[0] == currentCoord[0] && selectedCoord[1] == currentCoord[1]) {
                                        if(selectedCoord[2]==0 && l.getColor()==AlienColor.PURPLE){
                                            alien = new Alien(AlienColor.PURPLE);
                                            c.setAlien(alien);
                                            numAlien++;
                                        }
                                        else if(selectedCoord[2]==1 && l.getColor()==AlienColor.BROWN){
                                            alien =new Alien(AlienColor.BROWN);
                                            c.setAlien(alien);
                                            numAlien++;
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
    }

    /**
     * This method remove a human from cabin in position (x,y)
     * @param x coordinate x of the cabin
     * @param y coordinate x of the cabin
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
     * This method remove a number of human from a list of cabins.
     * @param coord list of coordinates, for each coordinate the first number is the x,
     *              the second is the y and the third is the number of humans to remove.
     * @param numMax maximum number of astronauts to lose
     */
    public void removeNumHuman(ArrayList<int[]> coord,int numMax){

        for(int[] c:coord){
            if(numMax>0){
                switch (components[c[0]][c[1]]){
                    case Cabin cabin -> {
                            if(c[2]==1){
                                if(cabin.getAlien()!=null){
                                    cabin.setAlien(null);
                                    numAlien--;
                                }else{
                                    cabin.removeHumans(1);
                                    numHuman --;
                                }
                                numMax--;
                            }
                             else {
                                cabin.removeHumans(2);
                                numHuman -=2;
                                numMax-=2;
                            }
                        }

                    default -> { }
                }
            }else return;
        }
    }

    /**
     * @return matrix of components and empty spaces
     */
    public Component[][] getComponents() {
        return components;
    }

    /**
     * @return the number of double cannons in the spaceship
     */
    public int getNumDoubleCannon(){
        return numDoubleCannon;
    }

    /**
     * @return the number of double engines in the spaceship
     */
    public int getNumDoubleEngine(){
        return numDoubleEngine;
    }

    /**
     * This method adds a component to the matrix
     * @param component component the player wants to add
     * @param x x coordinate of the cell in which the player wants to add the component
     * @param y y coordinate of the cell in which the player wants to add the component
     * @throws InvalidCoordinates if the player can't place a component in the given position
     */
    public void addComponent(Component component, int x, int y) throws InvalidCoordinates{
        if(components[x][y]==null ) {
            if(
                    (components[x+1][y]!=null && !components[x+1][y].getType().equals("Empty Space") && compatibleLinks(component.getLink()[2],components[x+1][y].getLink()[0]))
                    || (components[x-1][y]!=null && !components[x-1][y].getType().equals("Empty Space") && compatibleLinks(component.getLink()[0],components[x-1][y].getLink()[2]))
                    || (components[x][y+1]!=null && !components[x][y+1].getType().equals("Empty Space") && compatibleLinks(component.getLink()[1],components[x][y+1].getLink()[3]))
                    || (components[x][y-1]!=null && !components[x][y-1].getType().equals("Empty Space") && compatibleLinks(component.getLink()[3],components[x][y-1].getLink()[1]))
            ){
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
     * @throws InvalidCoordinates if there isn't any component to remove in that position
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
    public float getSingleCannonPower() {
        float power = 0;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null ) {
                    switch(components[i][j]){
                        case Cannon c->{
                            if (!c.getIsDouble()) {
                                if (c.getOrientation() == Orientation.N) {
                                    power++;
                                }
                                else{
                                    power += (float) 1/2;
                                }
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
    public float getDoubleCannon() {
        float power = 0;
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
                            if (e.getIsDouble() && e.getIsActive() && e.getOrientation() == Orientation.S)
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
     * This method checks if the component is correctly linked with the others components
     * @param x x coordinate of the component
     * @param y y coordinate of the component
     * @return boolean that says if the component in position (x,y) is correctly linked with the others components
     */
    private boolean isValid(int x, int y) {
        Component com = components[x][y];
        switch (com) {
            case Engine e->{
                if(e.getOrientation()!=Orientation.S) return false;
            }
            default -> {}
        }
        Component comU = null;
        Component comR = null;
        Component comD = null;
        Component comL = null;
        comU = components[x - 1][y];
        if(y+1<11) comR = components[x][y + 1];
        if(x+1<10) comD = components[x + 1][y];
        comL = components[x][y - 1];

        int linkU=-1,linkR=-1,linkD=-1,linkL=-1;
        Integer[] link = com.getLink();
        if(comU!=null){
            switch(comU){
                case EmptySpace e->{}
                case Cannon c->{
                    if(c.getOrientation()==Orientation.S) return false;
                    linkU= comU.getLink()[2];
                }
                case Engine e->{return false;}
                default -> {
                    linkU= comU.getLink()[2];
                }
            }
        }
        if(comR!=null){
            switch(comR){
                case EmptySpace e->{}
                case Cannon c->{
                    if(c.getOrientation()==Orientation.W) return false;
                    linkR = comR.getLink()[3];
                }
                default -> {
                    linkR = comR.getLink()[3];
                }
            }
        }
        if(comD!=null){
            switch (comD){
                case EmptySpace e->{}
                case Cannon c->{
                    if(c.getOrientation()==Orientation.N) return false;
                    linkD = comD.getLink()[0];
                }
                default -> {
                    linkD = comD.getLink()[0];
                }
            }
        }
        if(comL!=null){
            switch (comL){
                case EmptySpace e->{}
                case Cannon c->{
                    if(c.getOrientation()==Orientation.E) return false;
                    linkL = comL.getLink()[1];
                }
                default -> {
                    linkL = comL.getLink()[1];
                }
            }
        }

        if (linkU!=-1 && !compatibleLinks(link[0], linkU) && !(linkU==0 && link[0]==0)){
            return false;
        }
        if (linkR!=-1 && !compatibleLinks(link[1], linkR) && !(linkR==0 && link[1]==0)){
            return false;
        }
        if (linkD!=-1 && !compatibleLinks(link[2], linkD) && !(linkD==0 && link[2]==0)){
            return false;
        }
        if (linkL!=-1 && !compatibleLinks(link[3], linkL) && !(linkL==0 && link[3]==0)){
            return false;
        }
        return true;
    }

    /**
     * This method checks if two links are compatible
     * @param l1 first link
     * @param l2 second link
     * @return if the two links are compatible
     */
    private boolean compatibleLinks(int l1, int l2) {
        if(l1==3)
            if(l2!=0) return true;
        if(l2==3)
            if(l1!=0) return true;
        return l1 == l2 && l1!=0;
    }

    /**
     * This method calculates the total cannon power
     * @return total cannon power
     */
    public float getTotalCannonPower() {

        float power = 0;
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

    /**
     * This method sets all double cannons as unactive
     */
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

    /**
     * This method sets all double engines as unactive
     */
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

    /**
     * This method sets some double engines as active
     * @param coord engines coordinates to activate
     * @param batteriesCoord battery storages coordinates from which the player wants to lose batteries,
     *                       for each coordinate in coordinates the first integer is x, the second is the y
     *                       and the third is the number of batteries to remove from that battery storage
     * @throws NotEnoughBatteries if the player doesn't have enough batteries
     */
    public void activateDoubleEngines(ArrayList<int[]> coord, ArrayList<int[]> batteriesCoord) throws NotEnoughBatteries {
        if(coord.size()>numOfBattery)
            throw new NotEnoughBatteries("You don't have enough batteries to activate all that engines!");
        for(int[] c:coord){
            switch(components[c[0]][c[1]]){
                case Engine engine->{
                    for(int[] bat:batteriesCoord){
                        if(components[bat[0]][bat[1]]!=null){
                            switch(components[bat[0]][bat[1]]){
                                case BatteryStorage bs->{
                                    if(bs.getQuantity()>0){
                                        engine.setIsActive(true);
                                        numOfBattery--;
                                        bs.removeNumOfBatteries(1);
                                    }
                                }
                                default -> {}
                            }
                        }
                    }
                }default -> {}
            }

        }
    }


     /**
     * This method sets some double cannons as active
     * @param coord cannons coordinates to activate
     * @param batteriesCoord battery storages coordinates from which the player wants to lose batteries,
     *                       for each coordinate in coordinates the first integer is x, the second is the y
     *                       and the third is the number of batteries to remove from that battery storage
     * @throws NotEnoughBatteries if the player doesn't have enough batteries
     */
    public void activateDoubleCannons(ArrayList<int[]> coord, ArrayList<int[]> batteriesCoord) throws NotEnoughBatteries {
        if(coord.size()>numOfBattery)
            throw new NotEnoughBatteries("You don't have enough batteries to activate all that cannons!");
        for(int[] c:coord){
            switch(components[c[0]][c[1]]){
                case Cannon cannon->{
                    for(int[] bat:batteriesCoord){
                        if(components[bat[0]][bat[1]]!=null){
                            switch(components[bat[0]][bat[1]]){
                                case BatteryStorage bs->{
                                    if(bs.getQuantity()>0){
                                        cannon.setIsActive(true);
                                        numOfBattery--;
                                        bs.removeNumOfBatteries(1);
                                    }
                                }
                                default -> {}
                            }
                        }
                    }
                }default -> {}
            }

        }
    }

    /**
     * This method sets a shield as active
     * @param shieldCoord shield coordinate to activate
     * @param batteryCoord battery storages coordinate from which the player wants to lose batteries,
     *                     the first integer is x, the second is the y
     * @throws NotEnoughBatteries if the player doesn't have enough batteries
     */
    public void activateShield(int[] shieldCoord, int[] batteryCoord) throws NotEnoughBatteries {

        switch(components[shieldCoord[0]][shieldCoord[1]]){
            case Shield shield->{
                shield.setIsActive(true);
                this.existsAnActiveShield = true;
            }
            default -> {}
        }
        switch(components[batteryCoord[0]][batteryCoord[1]]){
            case BatteryStorage bs->{
                if(bs.getQuantity()>0)
                    bs.removeNumOfBatteries(1);
                else throw new NotEnoughBatteries("You don't have enough batteries to activate all that shield!");

            }
            default -> {}
        }
    }

    /**
     * This method deactivates all active shields in the spaceship
     */
    public void deActivateShields(){
        this.existsAnActiveShield = false;
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
        this.existsAnActiveShield=false;
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
        return power + getSingleEnginePower() + 2*getDoubleEngine();
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
                            if(comU!=null) {
                                switch (comU) {
                                    case EmptySpace emptySpace -> {
                                        if (com.getLink()[0] > 0) cont++;
                                    }
                                    default -> {
                                    }
                                }
                            }
                            if (comU == null && com.getLink()[0]>0) cont++;

                            if(comR!=null) {
                                switch (comR) {
                                    case EmptySpace emptySpace -> {
                                        if (com.getLink()[1] > 0) cont++;
                                    }
                                    default -> {
                                    }
                                }
                            }
                            if (comR == null && com.getLink()[1]>0) cont++;

                            if(comD!=null) {
                                switch (comD) {
                                    case EmptySpace emptySpace -> {
                                        if (com.getLink()[2] > 0) cont++;
                                    }
                                    default -> {
                                    }
                                }
                            }
                            if (comD == null && com.getLink()[2]>0) cont++;

                            if(comL!=null) {
                                switch (comL) {
                                    case EmptySpace emptySpace -> {
                                        if (com.getLink()[3] > 0) cont++;
                                    }
                                    default -> {
                                    }
                                }
                            }
                            if (comL == null && com.getLink()[3]>0) cont++;
                        }
                    }

                }
            }
        }
        return cont;
    }

    /**
     * This method checks and corrects the spaceship for level 2
     * @return true if all the components are correctly linked
     */
    @Deprecated
    public boolean wellDone() {
        boolean wellDone=true;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j]!=null){
                    switch(components[i][j]){
                        case EmptySpace emptySpace->{}
                        default -> {
                            if(!isValid(i, j)){
                                try{
                                    removeComponent(i,j);
                                    lost++;
                                } catch (InvalidCoordinates e) {
                                    throw new RuntimeException(e);
                                }
                                if(wellDone) wellDone=false;
                            }
                        }
                    }
                }

            }
        }
        if(!wellDone){
            componentsVisited(7,7);
            lost+=removeUnlinkedComponents(Game.getIstance().getBank());
        }
        return wellDone;
    }


    /**
     * This method checks and corrects the spaceship for level 2
     * @return true if all the components are correctly linked
     */
    @Deprecated
    public boolean wellDoneForTrialFlight() {
        boolean wellDone=true;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j]!=null){
                    switch(components[i][j]){
                        case EmptySpace emptySpace->{}
                        default -> {
                            if(!isValid(i, j)){
                                try{
                                    removeComponent(i,j);
                                } catch (InvalidCoordinates e) {
                                    throw new RuntimeException(e);
                                }
                                if(wellDone) wellDone=false;
                            }
                        }
                    }
                }

            }
        }
        if(!wellDone){
            componentsVisited(7,7);
            removeUnlinkedComponents(Game.getIstance().getBank());
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
            if(direction==0 || direction==2){
                if(num<4 || num>10){
                    return false;
                }
            }
            if(direction==1 || direction==3){
                if(num<5 || num>9){

                    return false;
                }
            }
            switch (direction) {
                case 0:
                    for (int i = 5; i <= 9; i++) {
                        if (components[i][num] != null){
                            switch(components[i][num]){
                                case EmptySpace emptySpace->{
                                }
                                default -> {return true;}
                            }
                        }
                    }
                    break;
                case 1:

                    for (int j = 10; j > 3; j--) {
                        if (components[num][j] != null) {
                            switch(components[num][j]){
                                case EmptySpace emptySpace->{
                                }
                                default -> {return true;}
                            }
                        }
                    }
                    break;
                case 2:

                    for (int i = 9; i > 4; i--) {
                        if (components[i][num] != null) {
                            switch(components[i][num]){
                                case EmptySpace emptySpace->{
                                }
                                default -> {return true;}
                            }
                        }
                    }
                    break;
                case 3:
                    for (int j = 4; j <= 10; j++) {
                        if (components[num][j] != null) {
                            switch(components[num][j]){
                                case EmptySpace emptySpace->{
                                }
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
        boolean removed=false;
        switch (direction) {
            case 0:
                for (int i = 5; i <= 9; i++) {
                    if (components[i][num] != null) {
                        if(!removed){
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
                                    removed=true;
                                }
                            }
                        }
                    }
                }
                break;
            case 1:
                for (int j = 10; j > 3; j--) {
                    if (components[num][j] != null) {
                        if(!removed){
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
                                    removed=true;
                                }
                            }
                        }
                    }
                }
                break;
            case 2:
                for (int i = 9; i > 4; i--) {
                    if (components[i][num] != null) {
                        if(!removed){
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
                                    removed=true;
                                }
                            }
                        }
                    }
                }
                break;
            case 3:
                for (int j = 4; j <= 10; j++) {
                    if (components[num][j] != null) {
                        if(!removed) {
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
                                    removed=true;
                                }
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
                        Iterator<Goods> iterator = goodsList.iterator();
                        while (iterator.hasNext()) {
                            Goods g = iterator.next();
                            if (g.getX() == x && g.getY() == y) {
                                if (g.getValue() == 4) redGoods--;
                                else if (g.getValue() == 3) yellowGoods--;
                                else if (g.getValue() == 2) greenGoods--;
                                else if (g.getValue() == 1) blueGoods--;

                                iterator.remove();
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
                    if(components[x+1][y]!=null){
                        switch(components[x+1][y]){
                            case Cabin cabin->{
                                if(cabin.getAlien() != null) {
                                    cabin.setAlien(null);
                                    numAlien--;
                                }
                            }default -> {}
                        }
                    }
                    if(components[x-1][y]!=null){
                        switch(components[x-1][y]){
                            case Cabin cabin->{
                                if(cabin.getAlien() != null) {
                                    cabin.setAlien(null);
                                    numAlien--;
                                }
                            }default -> {}
                        }
                    }
                    if(components[x][y+1]!=null){
                        switch(components[x][y+1]){
                            case Cabin cabin->{
                                if(cabin.getAlien() != null) {
                                    cabin.setAlien(null);
                                    numAlien--;
                                }
                            }default -> {}
                        }
                    }
                    if(components[x][y-1]!=null){
                        switch(components[x][y-1]){
                            case Cabin cabin->{
                                if(cabin.getAlien() != null) {
                                    cabin.setAlien(null);
                                    numAlien--;
                                }
                            }default -> {}
                        }
                    }

                }default -> {}
            }
        }
        componentsVisited(7,7);
        removeUnlinkedComponents(bank);
    }

    /**
     * Starting from the main cabin we marke as visited every component that is reachable through a path that starts
     * in the main cabin, every component that isn't reachable from the main cabin will be left as not Visited
     * @param x: coordinate x of the main cabin
     * @param y: coordinate y of the main cabin
     */
    private void componentsVisited(int x, int y) {
        if (x < 0 || x >= components.length || y < 0 || y >= components[0].length) return;

        Component current = components[x][y];
        if (current == null || current.isVisited()) return;

        switch (current) {
            case EmptySpace ignored -> { return; }
            default -> {
                current.setVisited(true);
                Integer[] links = current.getLink();

                if (x + 1 < components.length) {
                    Component down = components[x + 1][y];
                    if (down != null) {
                        switch (down) {
                            case EmptySpace ignored -> {}
                            default -> {
                                Integer[] downLinks = down.getLink();
                                if (compatibleLinks(links[2], downLinks[0])) {
                                    componentsVisited(x + 1, y);
                                }
                            }
                        }
                    }
                }

                if (y + 1 < components[0].length) {
                    Component right = components[x][y + 1];
                    if (right != null) {
                        switch (right) {
                            case EmptySpace ignored -> {}
                            default -> {
                                Integer[] rightLinks = right.getLink();
                                if (compatibleLinks(links[1], rightLinks[3])) {
                                    componentsVisited(x, y + 1);
                                }
                            }
                        }
                    }
                }

                if (x - 1 >= 0) {
                    Component up = components[x - 1][y];
                    if (up != null) {
                        switch (up) {
                            case EmptySpace ignored -> {}
                            default -> {
                                Integer[] upLinks = up.getLink();
                                if (compatibleLinks(links[0], upLinks[2])) {
                                    componentsVisited(x - 1, y);
                                }
                            }
                        }
                    }
                }

                if (y - 1 >= 0) {
                    Component left = components[x][y - 1];
                    if (left != null) {
                        switch (left) {
                            case EmptySpace ignored -> {}
                            default -> {
                                Integer[] leftLinks = left.getLink();
                                if (compatibleLinks(links[3], leftLinks[1])) {
                                    componentsVisited(x, y - 1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * This method remove an unlinked component which is not reachable from the main cabin, then it sets all the
     * component as unvisited for the next check. It's usually called when a component is destroyed by a cannon shot
     * or a meteorite
     */
    private int removeUnlinkedComponents(Bank bank){
        int cont=0;
        int x=0,y=0;
        Component c=null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j] != null ) {
                    switch(components[i][j]){
                        case EmptySpace emptySpace->{}
                        default -> {
                            if(!components[i][j].isVisited()) {
                                try {
                                    c=removeComponent(i, j);
                                    x=i;
                                    y=j;
                                } catch (InvalidCoordinates e) {
                                    throw new RuntimeException(e);
                                }
                                cont++;
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
                        Iterator<Goods> iterator = goodsList.iterator();
                        while (iterator.hasNext()) {
                            Goods g = iterator.next();
                            if (g.getX() == x && g.getY() == y) {
                                if (g.getValue() == 4) redGoods--;
                                else if (g.getValue() == 3) yellowGoods--;
                                else if (g.getValue() == 2) greenGoods--;
                                else if (g.getValue() == 1) blueGoods--;

                                iterator.remove();
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
                            if(cabin.getAlien() != null) {
                                cabin.setAlien(null);
                                numAlien--;}
                        }default -> {}
                    }
                    switch(components[x-1][y]){
                        case Cabin cabin->{
                            if(cabin.getAlien() != null) {
                                cabin.setAlien(null);
                                numAlien--;}
                        }default -> {}
                    }
                    switch(components[x][y+1]){
                        case Cabin cabin->{
                            if(cabin.getAlien() != null) {
                                cabin.setAlien(null);
                                numAlien--;
                            }
                        }default -> {}
                    }
                    switch(components[x][y-1]){
                        case Cabin cabin->{
                            if(cabin.getAlien() != null) {
                                cabin.setAlien(null);
                                numAlien--;}
                        }default -> {}
                    }

                }default -> {}
            }
        }
        return cont;
    }

    /**
     * This method says if there is a cannon which can shoot the arriving meteorite
     * @param direction Direction 0-->N, 1-->E, 2-->S, 3-->W from which the meteorite arrives
     * @param num number of row or colon from which the meteorite arrives
     * @return true if there is a cannon which can shoot the arriving meteorite, false otherwise
     */
    public boolean blockBigMeteor(int direction, int num) {
        switch (direction) {
            case 0:
                for (int i = 5; i <= 9; i++) {
                    if (components[i][num] != null) {
                        switch(components[i][num]){
                            case Cannon cannon->{
                                if (cannon.getOrientation() == Orientation.N){
                                    if(cannon.getIsDouble() && cannon.getIsActive()) return true;
                                    else if(!cannon.getIsDouble()) return true;
                                }
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
                                if(Game.getIstance().getGameLevel()==2){
                                    if(cannon.getIsDouble() && cannon.getIsActive()) return true;
                                    else if(!cannon.getIsDouble()) return true;
                                }
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
                                if(Game.getIstance().getGameLevel()==2){
                                    if(cannon.getIsDouble() && cannon.getIsActive()) return true;
                                    else if(!cannon.getIsDouble()) return true;
                                }
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
                                if(Game.getIstance().getGameLevel()==2){
                                    if(cannon.getIsDouble() && cannon.getIsActive()) return true;
                                    else if(!cannon.getIsDouble()) return true;
                                }
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
    public boolean blockSmallMeteor(int direction, int num) {


            switch (direction) {
                case 0:
                    for (int i = 5; i <= 9; i++) {
                        if (components[i][num] != null) {
                            switch(components[i][num]){
                                case EmptySpace emptySpace->{}
                                default -> {
                                    if (components[i][num].getLink()[0] == 0){
                                        return true;
                                    }
                                    else {
                                        if(numOfBattery>0 && existsAnActiveShield){
                                            for (int w = 5; w <= 9; w++) {
                                                for (int z = 4; z <= 10; z++) {
                                                    if (components[w][z] != null) {
                                                        switch(components[w][z]) {
                                                            case Shield shield->{
                                                                if(shield.getIsActive()){
                                                                    if(shield.getDirection().equals(Direction.NE) || shield.getDirection().equals(Direction.NW)){
                                                                        return true;
                                                                    }
                                                                }
                                                            }
                                                            default -> {}
                                                        }

                                                    }
                                                }
                                            }
                                        }else return false;
                                    }
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
                                    if (components[num][j].getLink()[1] == 0){
                                        return true;
                                    }
                                    else{
                                        if(numOfBattery>0 && existsAnActiveShield){
                                                for (int w = 5; w <= 9; w++) {
                                                    for (int z = 4; z <= 10; z++) {
                                                        if (components[w][z] != null) {
                                                            switch(components[w][z]) {
                                                                case Shield shield->{
                                                                    if(shield.getIsActive()){
                                                                        if (shield.getDirection().equals(Direction.NE) || shield.getDirection().equals(Direction.SE)){
                                                                            return true;
                                                                        }
                                                                    }
                                                                }
                                                                default -> {}
                                                            }

                                                        }
                                                    }
                                                }

                                        }else return false;
                                    }
                                }
                            }

                        }
                    }
                    break;
                //if the meteor comes from sud
                case 2:
                    for (int i = 9; i > 4; i--) {
                        if (components[i][num] != null) {
                            switch(components[i][num]){
                                case EmptySpace emptySpace->{}
                                default -> {
                                    if (components[i][num].getLink()[2] == 0){
                                        return true;
                                    }
                                    else{
                                        if(numOfBattery>0 && existsAnActiveShield){

                                                for (int w = 5; w <= 9; w++) {
                                                    for (int z = 4; z <= 10; z++) {
                                                        if (components[w][z] != null) {
                                                            switch(components[w][z]) {
                                                                case Shield shield->{
                                                                    if(shield.getIsActive()){
                                                                        if(shield.getDirection().equals(Direction.SE) || shield.getDirection().equals(Direction.SW)){
                                                                            return true;
                                                                        }
                                                                    }
                                                                }
                                                                default -> {}
                                                            }

                                                        }
                                                    }
                                                }

                                        }else return false;
                                    }
                                }
                            }

                        }
                    }
                    break;
                //if the meteor comes from ovest
                case 3:
                    for (int j = 4; j <= 10; j++) {
                        if (components[num][j] != null) {
                            switch(components[num][j]){
                                case EmptySpace emptySpace->{}
                                default -> {
                                    if (components[num][j].getLink()[3] == 0){
                                        return true;
                                    }
                                    else{
                                        if(numOfBattery>0 && existsAnActiveShield){
                                                for (int w = 5; w <= 9; w++) {
                                                    for (int z = 4; z <= 10; z++) {
                                                        if (components[w][z] != null) {
                                                            switch(components[w][z]) {
                                                                case Shield shield->{
                                                                    if(shield.getIsActive()){
                                                                        if(shield.getDirection().equals(Direction.SW) || shield.getDirection().equals(Direction.NW)){
                                                                            return true;
                                                                        }
                                                                    }
                                                                }
                                                                default -> {}
                                                            }

                                                        }
                                                    }
                                                }

                                        } else return false;
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        return false;
    }

    /**
     * This method is used if it's already known that the spaceship will be hit by the small meteorite,
     * if the spaceship has protection in the critical direction, the player can choose to use or not the
     * shield. This is possible only if there is enough batteries to use the shield.
     * @param direction Direction 0-->N, 1-->E, 2-->S, 3-->W from which the meteorite arrives
     * @return true if the spaceship is protected
     */
    public boolean blockLightCannonShot(int direction){

        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    switch(components[i][j]){
                        case Shield shield->{
                            if(shield.getIsActive()){
                                if(
                                        (direction == 0 && (shield.getDirection().equals(Direction.NE) || shield.getDirection().equals(Direction.NW)))
                                                || (direction == 1 && (shield.getDirection().equals(Direction.NE) || shield.getDirection().equals(Direction.SE)))
                                                || (direction == 2 && (shield.getDirection().equals(Direction.SE) || shield.getDirection().equals(Direction.SW)))
                                                || (direction == 3 && (shield.getDirection().equals(Direction.SW) || shield.getDirection().equals(Direction.NW)))
                                ){
                                    if(numOfBattery>0){
                                        return true;
                                    }
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
     * This method remove an astronauts from each not empty cabin linked to another not empty cabin
     */
    public void epidemic() {
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j] != null) {
                    switch (components[i][j]){
                        case Cabin cabin->{
                            if(cabin.getQuantity()>0){
                                if(components[i + 1][j]!=null) {
                                    switch (components[i + 1][j]) {
                                        case Cabin cabin1 -> {
                                            if (cabin1.getQuantity() > 0 && compatibleLinks(cabin.getLink()[2], cabin1.getLink()[0])) {
                                                removeHuman(i, j);
                                                continue;
                                            }
                                        }
                                        default -> {
                                        }
                                    }
                                }
                                if(components[i][j + 1]!=null) {
                                    switch (components[i][j + 1]) {
                                        case Cabin cabin1 -> {
                                            if (cabin1.getQuantity() > 0 && compatibleLinks(cabin.getLink()[1], cabin1.getLink()[3])) {
                                                removeHuman(i, j);
                                                continue;
                                            }
                                        }
                                        default -> {
                                        }
                                    }
                                }
                                if(components[i - 1][j]!=null) {
                                    switch (components[i - 1][j]) {
                                        case Cabin cabin1 -> {
                                            if (cabin1.getQuantity() > 0 && compatibleLinks(cabin.getLink()[0], cabin1.getLink()[2])) {
                                                removeHuman(i, j);
                                                continue;
                                            }
                                        }
                                        default -> {
                                        }
                                    }
                                }
                                if(components[i][j-1]!=null) {
                                    switch (components[i][j - 1]) {
                                        case Cabin cabin1 -> {
                                            if (cabin1.getQuantity() > 0 && compatibleLinks(cabin.getLink()[3], cabin1.getLink()[1])) {
                                                removeHuman(i, j);
                                            }
                                        }
                                        default -> {
                                        }
                                    }
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
     * This method adds the specified good in the specified component, also the position is given.
     * If there's space in the load it just add the good, otherwise we have to remove the good that occupies the gived position
     * @param good good the user wants to add
     * @param coord coordinate of the load
     * @param positionInLoad position in the load
     * @throws InvalidPositionException if the good is special but not the choosen load
     */
    public Goods addGood(Goods good,int[] coord,int positionInLoad ,Bank bank) throws InvalidPositionException{
        if(components[coord[0]][coord[1]]!=null){
            switch(components[coord[0]][coord[1]]){
                case Load load->{

                    if(!load.getIsSpecial() && good.getValue() == 4){
                        throw new InvalidPositionException("Special load expected");
                    }
                    Goods g=load.getGood(positionInLoad);
                    if(g.getValue()==-1){
                        load.addGood(positionInLoad,good);
                        switch(good.getValue()) {
                            case 1 -> blueGoods++;
                            case 2 -> greenGoods++;
                            case 3 -> yellowGoods++;
                            case 4 -> redGoods++;
                        }
                        bank.removeGoodsInBank(good.getValue());
                        addGoodsList(good);
                    }
                    else{
                        bank.addGoodsInBank(g.getValue()); // il valore di g non può essere -1 perché il load è pieno
                        load.addGood(positionInLoad,good);
                        switch(good.getValue()) {
                            case 1 -> blueGoods++;
                            case 2 -> greenGoods++;
                            case 3 -> yellowGoods++;
                            case 4 -> redGoods++;
                        }
                        bank.removeGoodsInBank(good.getValue());
                        goodsList.remove(g);
                        addGoodsList(good);
                        return g;
                    }
                }
                default -> {
                }
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
     * This method returns the number of batteries in the spaceship
     * @return number of batteries in the spaceship
     */
    public int getNumBatteries(){
        return numOfBattery;
    }

    /**
     * This method checks if in the given list of coordinates there are only loads
     * @param c list of coordinates to check
     * @return if in the given positions there are only loads
     */
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
     * @param positionInLoad1 position of the first load il the first load
     * @param coord2 coordinate of the second load
     * @param positionInLoad2 position of the second load il the second load
     * @throws InvalidPositionException if in that coordinates there is no load
     */
    public void swapGoods(int[] coord1, int positionInLoad1, int[] coord2, int positionInLoad2) throws InvalidPositionException {
        Load load1 = getLoadAt(coord1);
        Load load2 = getLoadAt(coord2);

        Goods g1 = load1.getGood(positionInLoad1);
        Goods g2 = load2.getGood(positionInLoad2);

        load1.addGood(positionInLoad1, g2);
        load2.addGood(positionInLoad2, g1);
    }


    /**
     * This method returns the load in the given position
     * @param coord position coordinates
     * @return load in the given position
     * @throws InvalidPositionException if there isn't a load in that position
     */
    private Load getLoadAt(int[] coord) throws InvalidPositionException {
        switch (components[coord[0]][coord[1]]) {
            case Load l -> { return l; }
            default -> throw new InvalidPositionException("Load expected at position");
        }
    }


    /**
     * @return number of red goods in the spaceship
     */
    public int getRedGoods(){
        return redGoods;
    }

    /**
     * @return number of green goods in the spaceship
     */
    public int getGreenGoods(){
        return greenGoods;
    }

    /**
     * @return number of blue goods in the spaceship
     */
    public int getBlueGoods(){
        return blueGoods;
    }

    /**
     * @return number of yellow goods in the spaceship
     */
    public int getYellowGoods() {
        return yellowGoods;
    }

    /**
     * This method removes a good from spaceship's goods list
     * @param good good to remove
     */
    private void removeFromGoodsList(Goods good) {
        for (int i = 0; i < goodsList.size(); i++) {
            Goods g = goodsList.get(i);
            if (g.getX() == good.getX() &&
                    g.getY() == good.getY() &&
                    g.getValue() == good.getValue()) {
                goodsList.remove(i);
                return;
            }
        }
    }

    /**
     * This method removes a list of the goods from the spaceship
     * @param list list of goods to remove
     */
    public void removeGoods(ArrayList<Goods> list) {
        for (Goods g : list) {
            switch (components[g.getX()][g.getY()]) {
                case Load load -> {
                    List<Goods> goodsInLoad = load.getGoods();
                    for (int i = 0; i < goodsInLoad.size(); i++) {
                        if (goodsInLoad.get(i).getValue() == g.getValue()) {
                            load.removeGood(i); // "svuota" la merce trovata
                            removeFromGoodsList(goodsInLoad.get(i));
                            break;
                        }
                    }
                }
                default -> {}
            }
        }
    }


    /**
     * This method checks every cabin in the spaceship. If it's next to a life support the method adds its coordinate
     * and the color in the list. (0: purple, 1:brown)
     * @return list of coordinates of cabin where the player cn place aliens and color of the alien
     */
    public ArrayList<int[]> getAlienCabin() {
        ArrayList<int[]> alienCabin = new ArrayList<>();
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++){
                if(components[i][j]!=null){
                    switch(components[i][j]){
                        case Cabin cabin->{
                                if (!(i==7 && j==7)) {
                                    if (components[i + 1][j] != null) {
                                        switch(components[i+1][j]){
                                            case LifeSupport lifeSupport->{
                                                if (components[i + 1][j].getType().equalsIgnoreCase("Life Support")) {
                                                    int link1 = components[i][j].getLink()[2];
                                                    int link2 = components[i + 1][j].getLink()[0];
                                                    if (compatibleLinks(link1, link2)) {
                                                        if(lifeSupport.getColor()==AlienColor.PURPLE) alienCabin.add(new int[]{i,j,0});
                                                        else alienCabin.add(new int[]{i,j,1});
                                                    }
                                                }
                                            }
                                            default -> {}
                                        }
                                    }
                                    if (components[i - 1][j] != null) {
                                        switch(components[i-1][j]){
                                            case LifeSupport lifeSupport->{
                                                int link1 = components[i][j].getLink()[0];
                                                int link2 = components[i - 1][j].getLink()[2];
                                                if (compatibleLinks(link1, link2)) {
                                                    if(lifeSupport.getColor()==AlienColor.PURPLE) alienCabin.add(new int[]{i,j,0});
                                                    else alienCabin.add(new int[]{i,j,1});
                                                }                                            }
                                            default -> {}
                                        }
                                    }
                                    if (components[i][j + 1] != null) {
                                        switch (components[i][j + 1]) {
                                            case LifeSupport lifeSupport->{
                                                int link1 = components[i][j].getLink()[1];
                                                int link2 = components[i][j + 1].getLink()[3];
                                                if (compatibleLinks(link1, link2)) {
                                                    if(lifeSupport.getColor()==AlienColor.PURPLE) alienCabin.add(new int[]{i,j,0});
                                                    else alienCabin.add(new int[]{i,j,1});
                                                }
                                            }
                                            default -> {}
                                        }
                                    }
                                    if (components[i][j - 1] != null) {
                                        switch(components[i][j-1]){
                                            case LifeSupport lifeSupport->{
                                                int link1 = components[i][j].getLink()[3];
                                                int link2 = components[i][j - 1].getLink()[1];
                                                if (compatibleLinks(link1, link2)) {
                                                    if(lifeSupport.getColor()==AlienColor.PURPLE) alienCabin.add(new int[]{i,j,0});
                                                    else alienCabin.add(new int[]{i,j,1});
                                                }
                                            }
                                            default -> {}
                                        }
                                    }
                                }

                        }
                        default -> {}
                    }
                }
            }
        }
        return alienCabin;
    }

    /**
     * This method removes all the crew members from the spaceship
     */
    public void removeAllHumans() {
        for(int i=0; i<components.length; i++){
            for(int j=0; j<components[i].length; j++){
                if(components[i][j]!=null){
                    switch (components[i][j]){
                        case Cabin cabin->{
                            if(cabin.getAlien()!=null){
                                cabin.setAlien(null);
                            }else {
                                cabin.removeHumans(cabin.getQuantity());
                                numHuman=0;
                            }
                        }
                        default -> {}
                    }
                }
            }
        }
    }

    /**
     * This method removes all the batteries from the spaceship
     */
    public void removeAllBatteries(){
        numOfBattery=0;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++){
                if(components[i][j]!=null){
                    switch(components[i][j]){
                        case BatteryStorage c->{
                            int quantity=c.getQuantity();
                            c.removeNumOfBatteries(quantity);
                        }
                        default -> {}
                    }
                }
            }
        }
    }

    /**
     * Compares the current spaceship components with the components of another spaceship to determine if any
     * changes have been made. The method evaluates differences.
     *
     * @param oldShip the spaceship against which the current spaceship's components are compared
     * @return true if there is any change in the components between the two spaceships, otherwise false
     */
    public boolean isChanged(SpaceShip oldShip){
        for(int i=0; i<components.length; i++){
            for(int j=0; j<components[i].length; j++){
                if(components[i][j]!=null){
                    switch (components[i][j]){

                        case Cabin cabin->{
                            switch (oldShip.getComponents()[i][j]){
                                case Cabin oldCabin->{
                                    if (cabin.getAlien() != null) {
                                        if (oldCabin.getAlien()==null || !cabin.getAlien().getColor().equals(oldCabin.getAlien().getColor())) {
                                            return true;
                                        }
                                    } else {
                                        if (cabin.getQuantity() != oldCabin.getQuantity()) {
                                            return true;
                                        }
                                    }
                                }
                                default -> {return true;}
                            }
                        }
                        case Cannon cannon->{
                            switch (oldShip.getComponents()[i][j]){
                                case Cannon oldCannon->{
                                    if (cannon.getIsDouble() != oldCannon.getIsDouble()) return true;
                                }
                                default -> {return true;}
                            }
                        }

                        case LifeSupport lifeSupport->{
                            switch (oldShip.getComponents()[i][j]){
                                case LifeSupport oldLifeSupport->{
                                    if(!lifeSupport.getColor().equals(oldLifeSupport.getColor())) return true;
                                }
                                default -> {return true;}
                            }
                        }

                        case Shield shield->{
                            switch (oldShip.getComponents()[i][j]){
                                case Shield oldShield->{
                                    if(!shield.getDirection().equals(oldShield.getDirection())) return true;
                                }
                                default -> {return true;}
                            }
                        }

                        case Engine engine -> {
                            switch (oldShip.getComponents()[i][j]){
                                case Engine oldEngine->{
                                    if(engine.getIsDouble() != oldEngine.getIsDouble()) return true;
                                }
                                default -> {return true;}
                            }
                        }

                        case Load load->{
                            switch (oldShip.getComponents()[i][j]){
                                case Load oldLoad->{
                                    for(int x=0; x<load.getGoods().size(); x++){
                                        if(load.getGoods().get(x).getValue() != oldLoad.getGoods().get(x).getValue()) return true;
                                    }
                                }
                                default -> {return true;}
                            }
                        }

                        case EmptySpace emptySpace->{
                            switch (oldShip.getComponents()[i][j]){
                                case EmptySpace oldEmptySpace->{}
                                default -> {return true;}
                            }
                        }

                        case Pipes pipes -> {
                            switch (oldShip.getComponents()[i][j]){
                                case Pipes oldPipes->{}
                                default -> {return true;}
                            }
                        }

                        case BatteryStorage batteryStorage -> {
                            switch (oldShip.getComponents()[i][j]){
                                case BatteryStorage oldBatteryStorage->{
                                    if(batteryStorage.getQuantity()!=oldBatteryStorage.getQuantity()) return true;
                                }
                                default -> {return true;}
                            }
                        }

                    }
                }else{
                    if(oldShip.getComponents()[i][j]!=null){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the components within the specified range of indices pass the validation criteria.
     *
     * @return true if all components within the specified range are either null, an instance
     *         of EmptySpace, or satisfy the validation criteria; false otherwise.
     */
    public boolean isShipLegal() {
        boolean wellDone=true;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j]!=null){
                    switch(components[i][j]){
                        case EmptySpace emptySpace->{}
                        default -> {
                            if(!isValid(i, j)){
                                if(wellDone) wellDone=false;
                            }
                        }
                    }
                }

            }
        }
        return wellDone;
    }

    /**
     * Determines whether the ship is legal after removing a component
     * at the specified coordinates. If the game level is 2, the number of lost components increases.
     *
     * @param x the x-coordinate of the component to remove
     * @param y the y-coordinate of the component to remove
     * @return true if the game state is in a valid condition after the removal; false otherwise
     */
    public boolean isLegalAfterRemove( int x, int y){
        try {
            removeComponent(x, y);
            if(Game.getIstance().getGameLevel()==2) {
                lost++;
            }
        } catch (InvalidCoordinates e) {
            throw new RuntimeException(e);
        }
        return isShipLegal();
    }


}
