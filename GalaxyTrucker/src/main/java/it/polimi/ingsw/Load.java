package it.polimi.ingsw;
import java.util.ArrayList;

public class Load extends Component{
    private boolean isSpecial;
    private int capacity;
    private ArrayList<Goods> goods;

    public Load(int capacity) {
        isSpecial = false;
        this.capacity = capacity;
        goods = new ArrayList<Goods>();
        super.setType("Load");
    }

    public int getCapacity() {
        return capacity;
    }

    public int getQuantity() {
        return goods.size();
    }

    public void addGood(ArrayList<Goods> newGoods) {
        goods.addAll(newGoods);
    }

    public String getType() {
        return type;
    }
}
