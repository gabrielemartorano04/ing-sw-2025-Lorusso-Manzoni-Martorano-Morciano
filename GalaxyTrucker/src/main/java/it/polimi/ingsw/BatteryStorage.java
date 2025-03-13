package it.polimi.ingsw;

public class BatteryStorage extends Component{
    private int capacity;
    private int quantity;

    public BatteryStorage() {
        this.capacity = 0;
        this.quantity = 0;
        super.setType("Battery Storage");
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
