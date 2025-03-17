package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * Class that manage the Trucker's SpaceShip, with its components, goods, astronauts
 * */

public class SpaceShip {

    private Component[][] components;
    private Component[] kept;
    private int lost;
    private int redGoods, greenGoods, blueGoods,yellowGoods;
    private int numHuman;
    private int goodsCapacity;
    private ArrayList<Goods> goodsList; //lista di tutte le merci che contiene la nava nello stato corrente e sono ordinante in base al value dal più grande al più piccolo

    /**
     * Spaceship constructor, it inizializes the attributes of this class
     * */
    public SpaceShip(){
        components=new Component[9][10];
        kept=new Component[2];
        for(int i=0;i<2;i++)
            kept[i]=null;
        for(int i=0;i<9;i++){
            for(int j=0;j<10;j++){
                components[i][j]=null;
            }
        }
        lost=0;
        Cabin cabin=new Cabin(3, 3, 3, 3, "cabin","",null, true);
        components[7][7]=cabin;
        redGoods=0;
        greenGoods=0;
        blueGoods=0;
        yellowGoods=0;
        numHuman=0;
        goodsList=new ArrayList<>();
    }

    /**
     *
     * @return goodsCapacity How many goods can be on the spaceship
     * */
    public int getGoodsCapacity(){
        Load load=null;
        for(int i=0;i<9;i++){
            for(int j=0;j<10;j++){
                if(components[i][j]!=null && components[i][j] instanceof Cabin){
                    load=(Load)components[i][j];
                    goodsCapacity+=load.getCapacity();
                }
            }
        }
        return goodsCapacity;
    }

/** numero di astronauti sulla spaceship */
    public int getNumHuman(){
        Cabin cabin=null;
        for(int i=5;i<9;i++) {
            for (int j = 4; j < 10; j++) {
                if(components[i][j]!=null) {
                    if(components[i][j] instanceof Cabin) {
                        cabin=(Cabin)components[i][j];
                        if(cabin.getAlien()==null) numHuman+=2;
                    }
                }
            }
        }
        return numHuman;
    }
    /** rimuove un astronauta dalla spaceship*/
    public void removeHuman(int x, int y){
        if(components[x][y] instanceof Cabin && numHuman>0) {
            numHuman--;
        }

    }
    /** rimuove un numero num di astronauti dalla spaceship*/
    public void removeNumHuman(int x, int y, int num){
        if(components[x][y] instanceof Cabin && numHuman>0) {
            numHuman-=num;
        }

    }
    /**restituisce la matrice dei componenti*/
    public Component[][] getComponents() {
        return components;
    }

    /**aggiunge un componente alla matrice in una certa cella di coordinate (x,y)*/
    public void addComponent(Component component, int x, int y){
        components[x][y]=component;
    }

    /**date le coordineate, rimuove il componente*/
    public Component removeComponent(int x, int y){
        Component component=components[x][y];
        components[x][y]=null;
        return component;
    }

/**potere dei cannoni singoli*/
    public int getSingleCannonPower(){
        int power=0;
        Cannon cannon=null;
        for(int i=5;i<9;i++){
            for(int j=4;j<10;j++){
                if(components[i][j]!=null && components[i][j].getType().equals("cannon")){
                    if(components[i][j] instanceof Cannon){
                        cannon=(Cannon)components[i][j];
                        if(!cannon.getIsDouble()){
                            if(cannon.getOrientation()==Orientation.N) power++;
                            else power+=0.5;
                        }

                    }

                }

            }
        }
        return power;
    }
    /**numero di componenti di tipo cannone doppio*/
    public int getDoubleCannon(){
        int power=0;
        Cannon cannon=null;
        for(int i=5;i<9;i++){
            for(int j=4;j<10;j++){
                if(components[i][j]!=null && components[i][j].getType().equals("cannon")){
                    if(components[i][j] instanceof Cannon){
                        cannon=(Cannon)components[i][j];
                        if(cannon.getIsDouble() && cannon.getIsActive()){

                                if(cannon.getOrientation()==Orientation.N) power+=2;
                                else power++;

                        }

                    }

                }

            }
        }
        return power;
    }


    public int getSingleEnginePower(){
        int power=0;
        Engine engine=null;
        for(int i=5;i<9;i++){
            for(int j=4;j<10;j++){
                if(components[i][j]!=null && components[i][j].getType().equals("engine")){
                    if(components[i][j] instanceof Engine){
                        engine=(Engine)components[i][j];
                        if(!engine.getIsDouble())
                            power++;

                    }

                }

            }
        }
        return power;
    }
    public int getDoubleEngine(){
        int doubleEngine=0;
        Engine engine=null;
        for(int i=5;i<9;i++){
            for(int j=4;j<10;j++){
                if(components[i][j]!=null && components[i][j].getType().equals("engine")){
                    if(components[i][j] instanceof Engine){
                        engine=(Engine)components[i][j];
                        if(engine.getIsDouble() && engine.getIsActive())
                            doubleEngine++;

                    }

                }

            }
        }
        return doubleEngine;
    }

    public Component chooseComponent(int x, int y){
        return components[x][y];
    }



    public ArrayList<Direction> getProtection(){
        ArrayList<Direction> p=new ArrayList<Direction>();
        Shield shield=null;
        for(int i=5;i<9;i++){
            for(int j=4;j<10;j++){
                if(components[i][j]!=null && components[i][j].getType().equals("Shield")){
                    if(components[i][j] instanceof Shield){
                        shield=(Shield)components[i][j];
                        p.add(shield.getDirection());
                    }
                }
            }
        }
        return p;
    }

    private boolean isValid(int x, int y){

        Cannon cannon=null;
        Component com=components[x][y];
        Component comU=components[x-1][y];
        Component comR=components[x][y+1];
        Component comD=components[x+1][y];
        Component comL=components[x][y-1];

        int[] link=com.getLink();
        int linkU=comU.getLink()[2];
        int linkR=comR.getLink()[3];
        int linkD=comD.getLink()[0];
        int linkL=comL.getLink()[1];
        //CONTROLLO CHE U,R,D,L SIA COLLEGATO BENE
        if(components[x][y] instanceof Cannon){
            cannon=(Cannon)components[x][y];
            switch(cannon.getOrientation()){
                case N:
                    if(components[x-1][y]!=null)return false;
                    break;
                case E:
                    if(components[x][y+1]!=null)return false;
                    break;
                case S:
                    if(components[x+1][y]!=null)return false;
                    break;
                case W:
                    if(components[x][y-1]!=null)return false;
                    break;
            }
        }
        else if (components[x][y] instanceof Engine){
            if(components[x+1][y]!=null) return false;

        }
        if(!compatibleLinks(link[0],linkU)) return false;
        if(!compatibleLinks(link[1],linkR)) return false;
        if(!compatibleLinks(link[2],linkD)) return false;
        if(!compatibleLinks(link[3],linkL)) return false;
        return true;
    }
    private boolean compatibleLinks(int l1,int l2){
        if(l1==l2 || l1==3 || l2==3)
            return true;
        else {
            if(l1==1){
                if (l2==2) return false;
            }
            if(l1==2){
                if(l2==1) return false;
            }
        }
        return true;
    }

    public int getTotalCannonPower(GameController controller){
        controller.askActiveComponents(getDoubleCannon());
        Cabin cabin=null;
        int power=0;
        for(int i=5;i<9;i++) {
            for (int j = 4; j < 10; j++) {
                if(components[i][j]!=null && components[i][j] instanceof Cabin){
                    cabin=(Cabin)components[i][j];
                    if(cabin.getAlien()!=null && cabin.getAlien().getColor()==AlienColor.PURPLE){
                        power+=2;
                    }
                }
            }
        }
        return getSingleCannonPower()+getDoubleCannon()+power;
    }

    public int getTotalEnginePower(GameController controller){
        controller.askActiveComponents(getDoubleEngine());
        Cabin cabin=null;
        int power=0;
        for(int i=5;i<9;i++) {
            for (int j = 4; j < 10; j++) {
                if(components[i][j]!=null && components[i][j] instanceof Cabin){
                    cabin=(Cabin)components[i][j];
                    if(cabin.getAlien()!=null && cabin.getAlien().getColor()==AlienColor.BROWN){
                        power+=2;
                    }
                }
            }
        }
        return power+getSingleEnginePower()+getDoubleEngine();
    }


    public int countExposedConnectors(){
        int cont=0;
        for(int i=5;i<9;i++){
            for(int j=4;j<10;j++){
                if(components[i][j]!=null){
                    Component com=components[i][j];
                    Component comU=components[i-1][j];
                    Component comR=components[i][j+1];
                    Component comD=components[i+1][j];
                    Component comL=components[i][j-1];
                    if(comU==null) cont++;
                    if(comR==null) cont++;
                    if(comD==null) cont++;
                    if(comL==null) cont++;
                }
            }
        }
        return cont;
    }
    public boolean wellDone(){
        for(int i=5;i<9;i++) {
            for (int j = 4; j < 10; j++) {
                if(!isValid(i,j))
                    return false;
            }
        }
        return true;
    }

    public boolean isHit(int direction, int num){
        switch(direction){
            case 0:
                for(int i=5;i<9;i++) {
                    if(components[i][num]!=null) return true;
                }
                break;
            case 1:
                for(int j=9;j>3;j--){
                    if(components[num][j]!=null) return true;
                }
                break;
            case 2:
                for(int i=8;i>4;i--) {
                    if(components[i][num]!=null) return true;
                }
                break;
            case 3:
                for(int j=4;j<10;j++){
                    if(components[num][j]!=null) return true;
                }
                break;
        }
        return false;
    }

    public void destroyComponent( int num,int direction){
        switch(direction){
            case 0:
                for(int i=5;i<9;i++) {
                    if(components[i][num]!=null) removeComponent(i,num);
                }
                break;
            case 1:
                for(int j=9;j>3;j--){
                    if(components[num][j]!=null) removeComponent(num,j);
                }
                break;
            case 2:
                for(int i=8;i>4;i--) {
                    if(components[i][num]!=null) removeComponent(i,num);
                }
                break;
            case 3:
                for(int j=4;j<10;j++){
                    if(components[num][j]!=null) removeComponent(num,j);
                }
                break;
        }
    }

    public boolean shootMeteor(int direction, int num){
        switch(direction){
            case 0:
                for(int i=5;i<9;i++) {
                    if(components[i][num]!=null){
                        if(components[i][num] instanceof Cannon
                                && ((Cannon) components[i][num]).getOrientation()== Orientation.N)
                            return true;
                    }
                }
                break;
            case 1:
                for(int j=9;j>3;j--){
                    if(components[num][j]!=null){
                        if(components[num][j] instanceof Cannon
                                &&((Cannon) components[num][j]).getOrientation()== Orientation.E)
                            return true;
                    }
                }
                break;
            case 2:
                for(int i=8;i>4;i--) {
                    if(components[i][num]!=null) {
                        if (components[i][num] instanceof Cannon
                                && ((Cannon) components[i][num]).getOrientation()== Orientation.S)
                            return true;
                    }
                }
                break;
            case 3:
                for(int j=4;j<10;j++){
                    if(components[num][j]!=null) {
                        if(components[num][j] instanceof Cannon
                                && ((Cannon) components[num][j]).getOrientation()==Orientation.W)
                            return true;
                    }
                }
                break;
        }
        return false;
    }

    public boolean blockSmallMeteor(int direction, int num){
        if(isHit(direction,num)){
            if(shootMeteor(direction,num)) return true;

            switch(direction){
                case 0:
                    for(int i=5;i<9;i++) {
                        if(components[i][num]!=null){
                            if(components[i][num].getLink()[0]==0)
                                return true;
                        }
                    }
                    break;
                case 1:
                    for(int j=9;j>3;j--){
                        if(components[num][j]!=null){
                            if(components[num][j].getLink()[1]==0)
                                return true;
                        }
                    }
                    break;
                case 2:
                    for(int i=8;i>4;i--) {
                        if(components[i][num]!=null) {
                            if (components[i][num].getLink()[2]==0)
                                return true;
                        }
                    }
                    break;
                case 3:
                    for(int j=4;j<10;j++){
                        if(components[num][j]!=null) {
                            if(components[num][j].getLink()[3]==0)
                                return true;
                        }
                    }
                    break;
            }
        }
        return false;
    }

    public void epidemic(){
        for(int i=5;i<9;i++) {
            for (int j = 4; j < 10; j++){
                if(components[i][j]!=null && components[i][j] instanceof Cabin && ((Cabin)components[i][j]).getQuantity()>0 ) {
                    if((components[i+1][j] instanceof Cabin && ((Cabin)components[i+1][j]).getQuantity()>0)
                    || (components[i][j+1] instanceof Cabin && ((Cabin)components[i][j+1]).getQuantity()>0)
                    || (components[i-1][j] instanceof Cabin && ((Cabin)components[i-1][j]).getQuantity()>0)
                    || (components[i][j-1] instanceof Cabin && ((Cabin)components[i][j-1]).getQuantity()>0)){
                        removeHuman(i,j);
                    }
                }
            }
        }
    }




    //SISTEMAAAAAAAA
    public void removeGoods(int numOfLostGoods){
        int x=0;
        int y=0;
        if(goodsList.size()>numOfLostGoods){
            //possiamo rimuovere merci
            for(int i=0;i<numOfLostGoods;i++){
                goodsList.remove(i);
            }
        }
        else{
            for(int i=0;i<goodsList.size();i++){
                x=goodsList.get(0).getX();
                y=goodsList.get(0).getY();
                (Load)components[x][y].removeGood();
                goodsList.remove(0);

            }
            /*for(int j=;j<numOfLostGoods;i++){
                goodsList.remove(i);
            }*/
        }
    }


}
