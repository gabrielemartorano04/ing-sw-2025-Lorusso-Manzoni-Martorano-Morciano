package it.polimi.ingsw;

public class Cabin extends Component{
    private boolean isMain;
    private int quantity;
    private int capacity;
    private Alien alien;
    private LifeSupport lifeSupport;
    private Color color;

    public Cabin(int linkU, int linkR, int linkD, int linkL, String type, String imagePath, Color color, boolean isMain) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.isMain = isMain;
        this.quantity = 0;
        this.capacity = 0;
        this.alien = null;
        this.lifeSupport = null;
        this.color = color;
    }

    public Alien getAlien(){
        return alien;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public String getType() {
        return type;
    }
}
