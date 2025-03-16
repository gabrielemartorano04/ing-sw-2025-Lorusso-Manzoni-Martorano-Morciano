package it.polimi.ingsw;

public class BatteryStorage extends Component{
    private int capacity;
    private int quantity;

    public BatteryStorage(int linkU, int linkR, int linkD, int linkL, String type, String imagePath, int capacity) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.capacity = capacity;
        this.quantity = capacity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }
}
