package it.polimi.ingsw;

import java.util.ArrayList;

public class SpaceShip {

    private Component[][] components;
    private Component[] kept;
    private int lost;
    private int redGoods, greenGoods, blueGoods,yellowGoods;
    private int numHuman;

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
        Cabin cabin=new Cabin();
        cabin.setIsMain(true);
        components[7][7]=cabin;
        redGoods=0;
        greenGoods=0;
        blueGoods=0;
        yellowGoods=0;
        numHuman=0;
    }

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
    public void removeHuman(int x, int y){
        if(components[x][y] instanceof Cabin && numHuman>0) {
            numHuman--;
        }

    }

    public Component[][] getComponents() {
        return components;
    }

    public void addComponent(Component component, int x, int y){
        components[x][y]=component;
    }

    public Component removeComponent(int x, int y){
        Component component=components[x][y];
        components[x][y]=null;
        return component;
    }

    public int getSingleCannonPower(){
        int power=0;
        Cannon cannon=null;
        for(int i=5;i<9;i++){
            for(int j=4;j<10;j++){
                if(components[i][j]!=null && components[i][j].getType().equals("cannon")){
                    if(components[i][j] instanceof Cannon){
                        cannon=(Cannon)components[i][j];
                        if(!cannon.getIsDouble())
                            power++;

                    }

                }

            }
        }
        return power;
    }
    public int getDoubleCannon(){
        int doubleCannon=0;
        Cannon cannon=null;
        for(int i=5;i<9;i++){
            for(int j=4;j<10;j++){
                if(components[i][j]!=null && components[i][j].getType().equals("cannon")){
                    if(components[i][j] instanceof Cannon){
                        cannon=(Cannon)components[i][j];
                        if(cannon.getIsDouble())
                            doubleCannon++;

                    }

                }

            }
        }
        return doubleCannon;
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
                        if(engine.getIsDouble())
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
        int active=controller.askActiveComponents(getDoubleCannon());
        Cabin cabin=null;
        int power=getSingleCannonPower();
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
        return power+2*active*getDoubleCannon();
    }

    public int getTotalEnginePower(GameController controller){
        int active=controller.askActiveComponents(getDoubleEngine());
        Cabin cabin=null;
        int power=getSingleEnginePower();
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
        return power+2*active*getDoubleCannon();
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

    public boolean shootMeteor(int direction, int num){
        switch(direction){
            case 0:
                for(int i=5;i<9;i++) {
                    if(components[i][num]!=null){
                        if(components[i][num] instanceof Cannon
                                && ((Cannon) components[i][num]).getOrientation()== Cannon.Orientation.N)
                            return true;
                    }
                }
                break;
            case 1:
                for(int j=9;j>3;j--){
                    if(components[num][j]!=null){
                        if(components[num][j] instanceof Cannon
                                &&((Cannon) components[num][j]).getOrientation()== Cannon.Orientation.E)
                            return true;
                    }
                }
                break;
            case 2:
                for(int i=8;i>4;i--) {
                    if(components[i][num]!=null) {
                        if (components[i][num] instanceof Cannon
                                && ((Cannon) components[i][num]).getOrientation()== Cannon.Orientation.S)
                            return true;
                    }
                }
                break;
            case 3:
                for(int j=4;j<10;j++){
                    if(components[num][j]!=null) {
                        if(components[num][j] instanceof Cannon
                                && ((Cannon) components[num][j]).getOrientation()==Cannon.Orientation.W)
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



    //possiamo riarrangiare le merci quando facciamo una add
    //SISTEMAAAAAAAA
    public void addGoods(ArrayList<Goods> goods){
        Load load=null;
        for(int i=5;i<9;i++) {
            for (int j = 4; j < 10; j++) {
                if(components[i][j]!=null && components[i][j].getType().equals("Load")){
                    if(components[i][j] instanceof Load){
                        load=(Load)components[i][j];
                        for(int k=0;k<goods.size();k++){
                            if(load.getGoods().get(k).getValue()==4) redGoods++;
                            else if(load.getGoods().get(k).getValue()==3) yellowGoods++;
                            else if(load.getGoods().get(k).getValue()==2) greenGoods++;
                            else if(load.getGoods().get(k).getValue()==1) blueGoods++;
                        }
                        /*if(load.getCapacity()-load.getQuantity()<=0)
                            //organizza merci
                        else //inserisci*/

                    }
                }
            }
        }

    }
    //SISTEMAAAAAAAA
    public void removeGoods(){

    }

}
