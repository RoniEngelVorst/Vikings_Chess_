import java.util.Comparator;

public class SquaresComparator implements Comparator<ConcretePiece> {
    private ConcretePlayer player;
    SquaresComparator(ConcretePlayer player){
        this.player = player;
    }
    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {
        if(o1.calcSquares() == o2.calcSquares()) {
            if (o1.getName() == o2.getName()) {
                if (o1.getOwner() == this.player) {
                    return -1;
                } else {
                    return 1;
                }
            }
            else {
                return o1.getName() - o2.getName();
            }
        }
        return o2.calcSquares() - o1.calcSquares();
    }
}
