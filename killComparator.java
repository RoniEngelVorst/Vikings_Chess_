import java.util.Comparator;

public class killComparator implements Comparator<ConcretePiece> {
    private ConcretePlayer player;
    killComparator(ConcretePlayer player){
        this.player = player;
    }
    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {
        if (o1.numOfKills == o2.numOfKills) {
            if (o1.getName() == o2.getName()) {
                if (o1.getOwner() == this.player) {
                    return -1;
                } else {
                    return 1;
                }
            }
            return o1.getName() - o2.getName();
        }
        return o2.numOfKills - o1.numOfKills;
    }

}
