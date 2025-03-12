package it.polimi.ingsw;
import java.util.ArrayList;

public class Load extends Component{
    private boolean isSpecial;
    private int capacity;
    private ArrayList<Goods> goods;

    public Load() {
        isSpecial = false;
        capacity = 0;
        goods = new ArrayList<>();
    }

    public void setCapacity(int capacity) {
        capacity = goods.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public int getQuantity() {
        return goods.size();
    }
}
