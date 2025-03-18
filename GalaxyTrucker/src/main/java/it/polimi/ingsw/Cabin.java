package it.polimi.ingsw;

public class Cabin extends Component{
    private boolean isMain;
    private int quantity;
    private final int capacity;
    private Alien alien;
    private LifeSupport lifeSupport;
    private Color color;

    public Cabin(int linkU, int linkR, int linkD, int linkL, String type, String imagePath, Color color, boolean isMain) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.isMain = isMain;
        this.quantity = 0;
        this.capacity = 2;
        this.alien = null;
        this.lifeSupport = null;
        this.color = color;
    }

    public Alien getAlien(){
        return alien;
    }
    public void setAlien(Alien alien){
        this.alien = alien;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean getIsMain() {
        return isMain;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCapacity() {
        return capacity;
    }

    public Color getColor() {
        return color;
    }

    public void removeHumans(int num) {
        quantity -= num;
    }

    public String getType() {
        return type;
    }
}
