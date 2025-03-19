package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Components.*;

import java.util.*;

/**
 * Class that manage the Trucker's SpaceShip, with its components, goods, astronauts
 * */

public class SpaceShip {

    private Component[][] components;
    private Component[] kept;
    private int lost;
    private int redGoods, greenGoods, blueGoods, yellowGoods;
    private int numHuman;
    private int goodsCapacity;
    private ArrayList<Goods> goodsList; //lista di tutte le merci che contiene la nava nello stato corrente e sono ordinante in base al value dal più grande al più piccolo
    private int numOfBatterys;
    /**
     * Spaceship constructor, it inizializes the attributes of this class
     */
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
        //ogni battery storage è pieno di batterie
        numOfBatterys = 0;
    }

    public void setBattery(){
        BatteryStorage bs=null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j] != null && components[i][j].getType().equals("Battery Storage")){
                    numOfBatterys= bs.getCapacity();
                    bs.setQuantity(numOfBatterys);
                }
            }
        }
    }

    /**
     * @return goodsCapacity How many goods can be on the spaceship
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
     * numero di astronauti sulla spaceship
     */
    public int getNumHuman() {
        Cabin cabin = null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    if (components[i][j].getType().equals("Cabin")) {
                        cabin = (Cabin) components[i][j];
                        if (cabin.getAlien() == null) numHuman += 2;
                    }
                }
            }
        }
        return numHuman;
    }

    /**
     * rimuove un astronauta dalla spaceship
     */
    public void removeHuman(int x, int y) {
        Cabin cabin=null;
        if (components[x][y].getType().equals("Cabin") && numHuman > 0) {
            cabin.removeHumans(1);
            numHuman--;
        }

    }

    /**
     * rimuove un numero num di astronauti dalla spaceship
     */
    //i parametri sono il numero di umani che vogliamo rimuovere e le coord(da controller)dei componenti da cui togliere gli umani
    public void removeNumHuman(int num, ArrayList<int[]> coord) {
        Cabin cabin=null;
        for(int[] c:coord){
            if (components[c[0]][c[1]].getType().equals("Cabin")){
                cabin=(Cabin)components[c[0]][c[1]];
                if(cabin.getQuantity() >= num) {
                    cabin.removeHumans(num);
                    numHuman -= num;
                    num=0;
                }
                else {
                    num-=cabin.getQuantity();
                    cabin.setQuantity(0);
                }
            }

        }


    }

    /**
     * restituisce la matrice dei componenti
     */
    public Component[][] getComponents() {
        return components;
    }

    /**
     * aggiunge un componente alla matrice in una certa cella di coordinate (x,y)
     */
    public void addComponent(Component component, int x, int y) {
        if(components[x][y]==null ) components[x][y] = component;
        //else eccezione non puoi riempire un emptyspace
    }

    /**
     * date le coordineate, rimuove il componente
     */
    public Component removeComponent(int x, int y) {
        Component component=null;
        if(!components[x][y].getType().equals("Empty Space")) {
            component = components[x][y];
            components[x][y] = null;
        }
        return component;
    }

    /**
     * potere dei cannoni singoli
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
     * potenza dei cannoni doppi
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
     * potere dei motori singoli
     * */
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

    /**potere dei motori doppi
     * @return
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

    public Component chooseComponent(int x, int y) {
        return components[x][y];
    }


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

    public boolean wellDone() {
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (!isValid(i, j))
                    return false;
            }
        }
        return true;
    }

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

    public boolean blockSmallMeteor(int direction, int num) {

            if (shootMeteor(direction, num)) return true;

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

        return false;
    }
    //considerando che la nave verrà sicuramente colpita controllo se la nave ha uno scudo che protegge quella direzione, se ha almeno una batteria chiedo se vuole attivare quello scudo
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
                                        if(numOfBatterys>0 && use){
                                            return true;
                                        }
                    }
                }
            }
        }
        return false;
    }

    public void epidemic() {
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null && components[i][j].getType().equals("Cabin") && ((Cabin) components[i][j]).getQuantity() > 0) {
                    if ((components[i + 1][j].getType().equals("Cabin") && ((Cabin) components[i + 1][j]).getQuantity() > 0)
                            || (components[i][j + 1].getType().equals("Cabin") && ((Cabin) components[i][j + 1]).getQuantity() > 0)
                            || (components[i - 1][j].getType().equals("Cabin") && ((Cabin) components[i - 1][j]).getQuantity() > 0)
                            || (components[i][j - 1].getType().equals("Cabin") && ((Cabin) components[i][j - 1]).getQuantity() > 0)) {
                        removeHuman(i, j);
                    }
                }
            }
        }
    }


    //coord coordinate dei componenti da cui togliere batterie
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

    public void addGoodsList(Goods good){
        int i = 0;
        while (i < goodsList.size() && goodsList.get(i).getValue() > good.getValue()) {
            i++;
        }
        goodsList.add(i, good);
    }
    //dove vuole mettere le merci
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
    public ArrayList<Goods> getGoodsList(){
        return goodsList;
    }
    public int getLost(){
        return lost;
    }

    public void setHuman(){
        Cabin cabin=null;
        Alien alien=null;
        LifeSupport ls=null;
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if(components[i][j]!=null && components[i][j].getType().equals("Cabin")){
                    cabin=(Cabin)components[i][j];
                    if(!cabin.getIsMain()){
                        if(components[i+1][j].getType().equals("Life Support"))
                            ls=(LifeSupport) components[i+1][j];
                        else if(components[i-1][j].getType().equals("Life Support"))
                            ls=(LifeSupport) components[i-1][j];
                        else if(components[i][j+1].getType().equals("Life Support"))
                            ls=(LifeSupport) components[i][j+1];
                        else if (components[i][j-1].getType().equals("Life Support"))
                            ls=(LifeSupport) components[i][j-1];
                        if(ls!=null){
                            alien=new Alien(ls.getColor());
                            cabin.setAlien(alien);
                        }
                    }
                    if(cabin.getAlien()==null){
                        cabin.setQuantity(2);
                        numHuman+=2;
                    }

                }
            }
        }
    }

}
