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
        super.setType("Load");
    }

    public void setCapacity(int capacity) {
        //come si fa?
    }

    public void setQuantity(int quantity) {
        //come si fa?
    }

    public int getCapacity() {
        return capacity;
    }

    public int getQuantity() {
        return goods.size();
    }

    public String getType() {
        return type;
    }
}
