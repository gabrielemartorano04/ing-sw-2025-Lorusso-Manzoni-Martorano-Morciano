package it.polimi.ingsw;

import java.util.ArrayList;

public class SpaceShip {

    private Component[][] components;
    private Component[] kept;
    private int lost;
    private int redGoods, greenGoods, blueGoods,yellowGoods;

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
        return getSingleCannonPower()+2*active*getDoubleCannon();
    }

    public int getTotalEnginePower(GameController controller){
        int active=controller.askActiveComponents(getDoubleEngine());
        return getSingleEnginePower()+2*active*getDoubleEngine();
    }


    public int exposedConnectors(){
        int cont=0;
        for(int i=5;i<9;i++){
            for(int j=4;j<10;j++){
                if(components[i][j]!=null){
                    Component com=components[i][j];
                    Component comU=components[i-1][j];
                    Component comR=components[i][j+1];
                    Component comD=components[i+1][j];
                    Component comL=components[i][j-1];
                    if(comU==null) cont+=com.getLink()[0];
                    if(comR==null) cont+=com.getLink()[1];
                    if(comD==null) cont+=com.getLink()[2];
                    if(comL==null) cont+=com.getLink()[3];
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

    //possiamo riarrangiare le merci quando facciamo una add
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

    public void removeGoods(){

    }
}
