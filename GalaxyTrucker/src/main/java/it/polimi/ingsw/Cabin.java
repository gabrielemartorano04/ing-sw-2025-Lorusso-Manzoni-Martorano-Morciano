package it.polimi.ingsw;

public class Cabin extends Component{
    private boolean isMain;
    private int quantity;
    private int capacity;
    private Alien alien;
    private LifeSupport lifeSupport;

    public Cabin() {
        this.isMain = false;
        this.quantity = 0;
        this.capacity = 0;
        this.alien = null;
        this.lifeSupport = null;
        super.setType("Cabin");
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

    public boolean isMain() {
        return isMain;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getType() {
        return type;
    }
}
