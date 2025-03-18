package it.polimi.ingsw;
import java.util.ArrayList;

public class Load extends Component{
    private boolean isSpecial;
    private int capacity;
    private ArrayList<Goods> goods; //lista delle merci contenute in quel Load ordinate dalla rarità maggiore a quella minore

    public Load(int linkU, int linkR, int linkD, int linkL, String type, String imagePath, int capacity, boolean isSpecial) {
        super(linkU, linkR, linkD, linkL, type, imagePath);
        this.isSpecial = isSpecial;
        this.capacity = capacity;
        goods = new ArrayList<Goods>();
        for(Goods g : goods){
            g=null;
        }
    }

    public boolean getIsSpecial() {
        return isSpecial;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getQuantity() {
        return goods.size();
    }

    public void addGood(Goods good) {
        int i = 0;
        while (i < goods.size() && goods.get(i).getValue() > good.getValue()) {
            i++;
        }
        goods.add(i, good);
    }

    public void removeGoodFromHead() {
        goods.remove(0);
    }

    public void removeGood(Goods good){
        goods.remove(good);
    }

    public String getType() {
        return type;
    }

    public ArrayList<Goods> getGoods() {
        return goods;
    }
}
