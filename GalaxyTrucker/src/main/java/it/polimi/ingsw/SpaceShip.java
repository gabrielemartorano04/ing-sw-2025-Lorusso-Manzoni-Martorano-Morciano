package it.polimi.ingsw;

import java.util.ArrayList;

public class SpaceShip {

    private Component[][] components;
    private Component[] kept;
    private int lost;

    public SpaceShip(){
        components=new Component[5][7];
        kept=new Component[2];
        for(int i=0;i<2;i++)
            kept[i]=null;
        for(int i=0;i<5;i++){
            for(int j=0;j<7;j++){
                components[i][j]=null;
            }
        }
        lost=0;
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
        for(int i=0;i<5;i++){
            for(int j=0;j<7;j++){
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
        for(int i=0;i<5;i++){
            for(int j=0;j<7;j++){
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
        for(int i=0;i<5;i++){
            for(int j=0;j<7;j++){
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
        for(int i=0;i<5;i++){
            for(int j=0;j<7;j++){
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
        for(int i=0;i<5;i++){
            for(int j=0;j<7;j++){
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
        Component comD=components[x][y+2];
        Component comL=components[x][y-1];

        int[] link=com.getLink();
        int linkU=comU.getLink()[2];
        int linkR=comR.getLink()[3];
        int linkD=comD.getLink()[0];
        int linkL=comL.getLink()[1];
        //CONTROLLO CHE U,R,D,L SIA COLLEGATO BENE

        return true;
    }




}
