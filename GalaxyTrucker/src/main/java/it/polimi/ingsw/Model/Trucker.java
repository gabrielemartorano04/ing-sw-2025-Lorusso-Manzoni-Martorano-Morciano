package it.polimi.ingsw.Model;

public class Trucker {


    private PlayerColors color;
    private String nickname;
    private final SpaceShip spaceShip;
    private int position; //posizione assoluta
    private int cosmicCredit;
    private BuildingState buildingState;


    public Trucker(String nickName, PlayerColors color) {
        this.nickname = nickName;
        this.color = color;
        this.spaceShip = new SpaceShip();
        this.cosmicCredit = 0;
    }

    public BuildingState getBuildingState() {
        return buildingState;
    }

    public void setBuildingState(BuildingState buildingState) {
        this.buildingState = buildingState;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addCosmicCredit(int cosmicCredit) {
        this.cosmicCredit += cosmicCredit;
    }

    public PlayerColors getColor() {
        return color;
    }

    public String getNickname() {return nickname;}

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public int getPosition() {
        return position;
    }

    public int getCosmicCredit() {
        return cosmicCredit;
    }

    //FSM

    public void chooseWhatToDo(String cmd){
        switch(buildingState){
            case START:
                switch (cmd){
                    case "Turn Hourglass":
                        turnHourglass();
                }
            case SELECT_COMPONENT:
            case SHOW_STACK:
            case TURN_HOURGLASS:
            case TURN_HOURGLASS_END:
            case LINK:
            case KEEP:
            case END:
        }

    }

    public void chooseComponent(){

    }

    public void useComponent(){

    }

    public void addComponent(){

    }

    public void turnHourglass(Game game){
        if(game.getTimeTurns()==0 && game.get)
    }

    public void finish(){

    }

}

