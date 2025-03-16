package it.polimi.ingsw;
import java.util.ArrayList;

public class Load extends Component{
    private boolean isSpecial;
    private int capacity;
    private ArrayList<Goods> goods;

    public Load(int linkU, int linkR, int linkD, int linkL, String type, int capacity, boolean isSpecial) {
        super(linkU, linkR, linkD, linkL, type);
        this.isSpecial = isSpecial;
        this.capacity = capacity;
        goods = new ArrayList<Goods>();
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

    public ArrayList<Goods> getGoods() {
        return goods;
    }
}
