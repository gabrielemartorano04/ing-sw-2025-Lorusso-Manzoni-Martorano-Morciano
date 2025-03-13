package it.polimi.ingsw;

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

    public int getEnginePower(){
        int power=0;
        int doubleEngine=0;
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

    }
}
