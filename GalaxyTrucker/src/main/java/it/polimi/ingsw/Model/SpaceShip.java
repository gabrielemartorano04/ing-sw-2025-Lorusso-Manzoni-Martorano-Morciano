package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Components.*;

import java.util.*;

/**
 * Class that manage the Trucker's SpaceShip, with its components, goods, astronauts
 * */

public class SpaceShip {

    private Component[][] components; //components in the matrix
    private Component[] kept; //2 components the trucker decided to keep during the spaceship construction
    private int lost; //number of lost components
    private int redGoods, greenGoods, blueGoods, yellowGoods; // number of goods for each type color
    private int numHuman; //number of astronauts on the spaceship
    private int goodsCapacity; //number of goods capacity
    private ArrayList<Goods> goodsList; //list of goods in the spaceship, sort by their value (desc)
    private int numOfBattery; //number of batteries in the spaceship
    private int numAlien;

    /**
     * Spaceship constructor, it initializes the attributes of this class
     * if a components[i][j] can be filled it's initialized null
     * otherwise it's initialized as EmptySpace
     * the counters are 0 because the spaceship is empty
     * This constructor also initialize the main cabin in the middle of the spaceship
     */
    //chiedi per parametro color per inizializzare il colore della main cabin
    public SpaceShip() {
        components = new Component[10][11];
        kept = new Component[2];
        for (int i = 0; i < 2; i++)
            kept[i] = null;
        //empty space dove non puoi aggiungere mai
        //null dove ancora non c'è un componente
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

        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 10; j++) {
                components[i][j] = null;
            }
        }
        lost = 0;
        Cabin cabin = new Cabin(3, 3, 3, 3, "cabin", "", null, true);
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
    }

    /**
     * This method set the full number of batteries in each battery storage and it updates numOfBattery counter to its
     * maximum
     */
    public void setBattery(){
        BatteryStorage bs=null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j] != null && components[i][j].getType().equals("Battery Storage")){
                    numOfBattery= bs.getCapacity();
                    bs.setQuantity(numOfBattery);
                }
            }
        }
    }

    /**
     * This method calculates the goods capacity of the entire spaceship
     * @return goodsCapacity the goods capacity of the entire spaceship
     */
    public int getGoodsCapacity() {
        Load load = null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null && components[i][j].getType().equals("Load")) {
                    load = (Load) components[i][j];
                    goodsCapacity += load.getCapacity();
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
        Cabin cabin = null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    if (components[i][j].getType().equals("Cabin")) {
                        cabin = (Cabin) components[i][j];
                        if (cabin.getAlien() == null){
                            numHuman += 2;
                            cabin.setQuantity(2);
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
        Cabin cabin=null;
        Alien alien=null;
        LifeSupport ls=null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j]!=null && components[i][j].getType().equals("Cabin")){
                    cabin=(Cabin)components[i][j];
                    if(!cabin.getIsMain()){
                        if(components[i+1][j].getType().equals("Life Support"))
                        {
                            int link1=components[i][j].getLink()[2];
                            int link2=components[i+1][j].getLink()[0];
                            if(link1==link2 || link1==3 || link2==3) ls=(LifeSupport) components[i+1][j];
                        }
                        else if(components[i-1][j].getType().equals("Life Support")){
                            int link1=components[i][j].getLink()[0];
                            int link2=components[i+1][j].getLink()[2];
                            if(link1==link2 || link1==3 || link2==3) ls=(LifeSupport) components[i+1][j];
                            ls=(LifeSupport) components[i-1][j];
                        }
                        else if(components[i][j+1].getType().equals("Life Support")){
                            int link1=components[i][j].getLink()[1];
                            int link2=components[i+1][j].getLink()[3];
                            if(link1==link2 || link1==3 || link2==3) ls=(LifeSupport) components[i+1][j];
                            ls=(LifeSupport) components[i][j+1];
                        }
                        else if (components[i][j-1].getType().equals("Life Support")){
                            int link1=components[i][j].getLink()[3];
                            int link2=components[i+1][j].getLink()[1];
                            if(link1==link2 || link1==3 || link2==3) ls=(LifeSupport) components[i+1][j];
                            ls=(LifeSupport) components[i][j-1];
                        }
                        int[] c = {i, j};
                        if(ls!=null && coord.contains(c)){
                            alien=new Alien(ls.getColor());
                            cabin.setAlien(alien);
                            numAlien++;
                        }
                    }

                }
            }
        }
    }

    /**
     * The player select the coordinates of the component from which he would like to remove an astronaut
     * @param x coordinate x of the component
     * @param y coordinate x of the component
     */
    public void removeHuman(int x, int y) {
        Cabin cabin=null;
        if (components[x][y].getType().equals("Cabin") && numHuman > 0) {
            cabin=(Cabin) components[x][y];
            if(cabin.getQuantity()>0){
                cabin.removeHumans(1);
                numHuman--;
            }
            else{
                //exception, qui non ci sono astronauti!!
            }
        }

    }

    /**
     *
     * @param coord where coord[0] coord[1] are the x coordinate of the component from which the player
     *              wants to remove a number of astronauts cointained in coord[2]
     */
    public void removeNumHuman(ArrayList<int[]> coord) {
        Cabin cabin=null;
        for(int[] c:coord){
            if (components[c[0]][c[1]].getType().equals("Cabin")){
                cabin=(Cabin)components[c[0]][c[1]];
                if(cabin.getQuantity() >= c[2]) {
                    cabin.removeHumans(c[2]);
                    numHuman -= c[2];
                }
                else {
                    //eccezione non hai abbastanza umani da togliere
                }
            }

        }
    }

    /**
     * @return matrix of components and empty spaces
     */
    public Component[][] getComponents() {
        return components;
    }

    /**
     * This method adds a component to the matrix
     * @param component component the player wants to add
     * @param x x coordinate of the cell in which the player wants to add the component
     * @param y y coordinate of the cell in which the player wants to add the component
     */
    public void addComponent(Component component, int x, int y) {
        if(components[x][y]==null ) components[x][y] = component;
        //else eccezione non puoi riempire un emptyspace
    }

    /**
     * This method remove a component from the matrix
     * @param x x coordinate of the component to remove
     * @param y y coordinate of the component to remove
     * @return the removed component
     */
    public Component removeComponent(int x, int y) {
        Component component=null;
        //se components[x][y] già null eccezione non c'è niente qua
        if(!components[x][y].getType().equals("Empty Space")) {
            component = components[x][y];
            components[x][y] = null;
        }
        return component;
    }

    /**
     * This method calculate the single cannons power
     * @return the single cannons power
     */
    public int getSingleCannonPower() {
        int power = 0;
        Cannon cannon = null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null ) {
                    if (components[i][j].getType().equals("Cannon")) {
                        cannon = (Cannon) components[i][j];
                        if (!cannon.getIsDouble()) {
                            if (cannon.getOrientation() == Orientation.N) power++;
                            else power += 0.5;
                        }

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
        Cannon cannon = null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null ) {
                    if (components[i][j].getType().equals("Cannon")) {
                        cannon = (Cannon) components[i][j];
                        if (cannon.getIsDouble() && cannon.getIsActive()) {

                            if (cannon.getOrientation() == Orientation.N) power += 2;
                            else power++;

                        }

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
        Engine engine = null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null ) {
                    if (components[i][j].getType().equals("Engine")) {
                        engine = (Engine) components[i][j];
                        if (!engine.getIsDouble())
                            power++;

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
        Engine engine = null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null && components[i][j].getType().equals("Engine")) {

                        engine = (Engine) components[i][j];
                        if (engine.getIsDouble() && engine.getIsActive())
                            doubleEngine++;// non c'è bisogno di controllare l'orientamento perchè



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
        Shield shield = null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null && components[i][j].getType().equals("Shield")) {

                        shield = (Shield) components[i][j];
                        p.add(shield.getDirection());

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
    private boolean isValid(int x, int y) {

        Cannon cannon = null;
        Component com = components[x][y];
        Component comU = components[x - 1][y];
        Component comR = components[x][y + 1];
        Component comD = components[x + 1][y];
        Component comL = components[x][y - 1];

        int[] link = com.getLink();
        int linkU = comU.getLink()[2];
        int linkR = comR.getLink()[3];
        int linkD = comD.getLink()[0];
        int linkL = comL.getLink()[1];
        //CONTROLLO CHE U,R,D,L SIA COLLEGATO BENE
        if (components[x][y].getType().equals("Cannon")) {
            cannon = (Cannon) components[x][y];
            switch (cannon.getOrientation()) {
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
        } else if (components[x][y].getType().equals("Engine")) {
            if (components[x + 1][y] != null) return false;

        }
        if (!compatibleLinks(link[0], linkU)) return false;
        if (!compatibleLinks(link[1], linkR)) return false;
        if (!compatibleLinks(link[2], linkD)) return false;
        if (!compatibleLinks(link[3], linkL)) return false;
        return true;
    }

    private boolean compatibleLinks(int l1, int l2) {
        if (l1 == l2 || l1 == 3 || l2 == 3)
            return true;
        else {
            if (l1 == 1) {
                if (l2 == 2) return false;
            }
            if (l1 == 2) {
                if (l2 == 1) return false;
            }
        }
        return true;
    }
//in ingresso coordinate dei cannoni doppi che vogliamo attivare (da controller)

    /**
     * This method calculate the sum of single cannon power, double activated cannon and power given by aliens in the cabins
     * Before calling this method setting the aliens is needed
     * @param coord coordinates of the components the player wants to activate
     * @return power of cannons of the spaceship
     */
    public int getTotalCannonPower(ArrayList<int[]> coord) {
        Cannon cannon = null;
        for(int[] c:coord){
            cannon = (Cannon) components[c[0]][c[1]];
            cannon.setIsActive(true);
        }
        Cabin cabin = null;
        int power = 0;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null && components[i][j].getType().equals("Cabin")) {
                    cabin = (Cabin) components[i][j];
                    if (cabin.getAlien() != null && cabin.getAlien().getColor() == AlienColor.PURPLE) {
                        power += 2;
                    }
                }
            }
        }
        return getSingleCannonPower() + getDoubleCannon() + power;
    }

    /**
     * This method calculate the sum of single engine power, double activated engine and power given by aliens in the cabins
     * Before calling this method setting the aliens is needed
     * @param coord coordinates of the components the player wants to activate
     * @return power of engine of the spaceship
     */
    public int getTotalEnginePower(ArrayList<int[]> coord) {
        Engine engine = null;
        for(int[] c:coord){
            engine = (Engine) components[c[0]][c[1]];
            engine.setIsActive(true);
        }
        Cabin cabin = null;
        int power = 0;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null && components[i][j].getType().equals("Cabin")) {
                    cabin = (Cabin) components[i][j];
                    if (cabin.getAlien() != null && cabin.getAlien().getColor() == AlienColor.BROWN) {
                        power += 2;
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
                if (components[i][j] != null && !components[i][j].getType().equals("Empty Space")) {
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
        return cont;
    }

    /**
     * @return true if all the components are correctly linked
     */
    public boolean wellDone() {
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (!isValid(i, j))
                    return false;
            }
        }
        return true;
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
                    if (components[i][num] != null) return true;
                }
                break;
            case 1:
                for (int j = 10; j > 3; j--) {
                    if (components[num][j] != null) return true;
                }
                break;
            case 2:
                for (int i = 9; i > 4; i--) {
                    if (components[i][num] != null) return true;
                }
                break;
            case 3:
                for (int j = 4; j <= 10; j++) {
                    if (components[num][j] != null) return true;
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
    public void destroyComponent(int num, int direction) {
        switch (direction) {
            case 0:
                for (int i = 5; i <= 9; i++) {
                    if (components[i][num] != null) removeComponent(i, num);
                }
                break;
            case 1:
                for (int j = 10; j > 3; j--) {
                    if (components[num][j] != null) removeComponent(num, j);
                }
                break;
            case 2:
                for (int i = 9; i > 4; i--) {
                    if (components[i][num] != null) removeComponent(i, num);
                }
                break;
            case 3:
                for (int j = 4; j <= 10; j++) {
                    if (components[num][j] != null) removeComponent(num, j);
                }
                break;
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
                        if (components[i][num].getType().equals("Cannon")
                                && ((Cannon) components[i][num]).getOrientation() == Orientation.N)
                            return true;
                    }
                }
                break;
            case 1:
                for (int j = 10; j > 3; j--) {
                    if (components[num][j] != null) {
                        if (components[num][j].getType().equals("Cannon")
                                && ((Cannon) components[num][j]).getOrientation() == Orientation.E)
                            return true;
                    }
                }
                break;
            case 2:
                for (int i = 9; i > 4; i--) {
                    if (components[i][num] != null) {
                        if (components[i][num].getType().equals("Cannon")
                                && ((Cannon) components[i][num]).getOrientation() == Orientation.S)
                            return true;
                    }
                }
                break;
            case 3:
                for (int j = 4; j <= 10; j++) {
                    if (components[num][j] != null) {
                        if (components[num][j].getType().equals("Cannon")
                                && ((Cannon) components[num][j]).getOrientation() == Orientation.W)
                            return true;
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
                        if (components[i][num] != null && !components[i][num].getType().equals("Empty Space")) {
                            if (components[i][num].getLink()[0] == 0)
                                return true;
                        }
                    }
                    break;
                case 1:
                    for (int j = 10; j > 3; j--) {
                        if (components[num][j] != null && !components[num][j].getType().equals("Empty Space")) {
                            if (components[num][j].getLink()[1] == 0)
                                return true;
                        }
                    }
                    break;
                case 2:
                    for (int i = 9; i > 4; i--) {
                        if (components[i][num] != null && !components[i][num].getType().equals("Empty Space")) {
                            if (components[i][num].getLink()[2] == 0)
                                return true;
                        }
                    }
                    break;
                case 3:
                    for (int j = 4; j <= 10; j++) {
                        if (components[num][j] != null && !components[num][j].getType().equals("Empty Space")) {
                            if (components[num][j].getLink()[3] == 0)
                                return true;
                        }
                    }
                    break;
            }
        Shield shield=null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null && components[i][j].getType().equals("Shield")) {
                    shield=(Shield)components[i][j];
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
        Shield shield=null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null && components[i][j].getType().equals("Shield")) {
                    shield=(Shield)components[i][j];
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
                if (components[i][j] != null && components[i][j].getType().equals("Cabin") && ((Cabin) components[i][j]).getQuantity() > 0) {
                    if ((components[i + 1][j].getType().equals("Cabin") && ((Cabin) components[i + 1][j]).getQuantity() > 0 && compatibleLinks(components[i][j].getLink()[2], components[i+1][j].getLink()[0]))
                            || (components[i][j + 1].getType().equals("Cabin") && ((Cabin) components[i][j + 1]).getQuantity() > 0 && compatibleLinks(components[i][j].getLink()[1], components[i][j+1].getLink()[3]))
                            || (components[i - 1][j].getType().equals("Cabin") && ((Cabin) components[i - 1][j]).getQuantity() > 0 && compatibleLinks(components[i][j].getLink()[0], components[i-1][j].getLink()[2]))
                            || (components[i][j - 1].getType().equals("Cabin") && ((Cabin) components[i][j - 1]).getQuantity() > 0) && compatibleLinks(components[i][j].getLink()[3], components[i][j-1].getLink()[1])) {
                        removeHuman(i, j);
                    }
                }
            }
        }
    }

    /**
     * This method remove a number of goods from the spaceship, through goodsList it's easy to access
     * the component that contains a certain good, so we remove it from the component and from goodsList.
     * If the player loses all his goods and he has to lose more, he will lose the rest in batteries,
     * at the beginning the player choose battery storages from which he can remove batteries
     * @param numOfLostGoods number of gooods the player has to remove
     * @param coord coordinates of battery storages from which he will lose batteries eventually
     */
    public void removeGoods(int numOfLostGoods, ArrayList<int[]> coord) {
        int x = 0;
        int y = 0;
        Load load = null;
        BatteryStorage batteryStorage = null;
        int batteryQuantity = 0;
        if (goodsList.size() > numOfLostGoods) {
            //possiamo rimuovere merci
            for (int i = 0; i < numOfLostGoods; i++) {
                goodsList.remove(i);
            }
        } else {
            for (int i = 0; i < goodsList.size(); i++) {
                x = goodsList.get(0).getX();
                y = goodsList.get(0).getY();
                load = (Load) components[x][y];
                load.removeGoodFromHead();
                goodsList.remove(0);
                numOfLostGoods--;

            }
            while (numOfLostGoods > 0) {
                //mi restituisce  le coordinate dei componenti da cui vuole togliere batterie
                for (int[] c : coord) {
                    batteryStorage = (BatteryStorage) components[c[0]][c[1]];
                    batteryQuantity = batteryStorage.getQuantity();
                    if (batteryQuantity <= numOfLostGoods) {
                        batteryStorage.setQuantity(0);
                        numOfLostGoods = numOfLostGoods - batteryQuantity;
                    } else {
                        batteryQuantity = batteryQuantity - numOfLostGoods;
                        batteryStorage.setQuantity(batteryQuantity);
                        numOfLostGoods = 0;
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
    public void addGoods(List <Goods> goods,ArrayList<int[]> coord){
        int index=0;
        Load load=null;
        for(int[] c : coord){
            int i=c[0];
            int j=c[1];
            if (components[i][j] != null && components[i][j].getType().equals("Load")) {
                load = (Load) components[i][j];

                while(load.getCapacity()-load.getQuantity()>0 && index<goods.size()){
                    if (load.getIsSpecial() && goods.get(index).getValue() == 4) {
                        load.addGood(goods.get(index));
                        addGoodsList(goods.get(index));
                        index++;
                    }
                    else if (goods.get(index).getValue() != 4) {
                        load.addGood(goods.get(index));
                        addGoodsList(goods.get(index));
                        index++;
                    }
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
                                Load l = (Load) components[g.getX()][g.getY()];
                                if(l.getIsSpecial()){
                                    l.removeGood(g);
                                    goodsList.remove(i);
                                    l.addGood(goods.get(j));
                                    addGoodsList(goods.get(j));
                                }
                            }
                        }
                    }
                    else{
                            //inserisci in qualsiasi spazio vuoto
                        goods.get(j).setX(goodsList.get(i).getX());
                        goods.get(j).setY(goodsList.get(i).getY());
                        Load loadToRemove=(Load)components[goodsList.get(i).getX()][goodsList.get(i).getY()];
                        loadToRemove.removeGood(goodsList.get(i));
                        loadToRemove.addGood(goods.get(j));
                        goodsList.remove(i);
                        addGoodsList(goods.get(j));
                    }
                }
            }
        }


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
    public void removeBatteries(ArrayList<int[]> batteryCoordinates){
        BatteryStorage bs=null;
        for(int[] c:batteryCoordinates){
            if (components[c[0]][c[1]].getType().equals("Battery Storage")){
                bs=(BatteryStorage) components[c[0]][c[1]];
                if(bs.getQuantity() >= c[2]) {
                    bs.setQuantity(bs.getQuantity() - c[2]);
                    numHuman -= c[2];
                }
                else {
                    //eccezione non hai abbastanza umani in c[0]c[1]
                }
            }

        }
    }
}
