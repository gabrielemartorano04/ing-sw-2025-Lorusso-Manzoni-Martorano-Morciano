package it.polimi.ingsw.Model;

public class Trucker {


    private PlayerColors color;
    private String nickName;
    private final SpaceShip spaceShip;
    private int position; //posizione assoluta
    private int cosmicCredit;

    public Trucker(String nickName, PlayerColors color) {
        this.nickName = nickName;
        this.color = color;
        this.spaceShip = new SpaceShip();
        this.cosmicCredit = 0;
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

    public String getNickName() {return nickName;}

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public int getPosition() {
        return position;
    }

    public int getCosmicCredit() {
        return cosmicCredit;
    }
}

