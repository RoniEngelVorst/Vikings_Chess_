import java.util.Comparator;

public class movesComparator implements Comparator<ConcretePiece> {
     private ConcretePlayer player;
     movesComparator(ConcretePlayer player){
         this.player = player;
     }
    @Override
    public int compare(ConcretePiece p1, ConcretePiece p2) {
        if(p1.getOwner() == p2.getOwner()){
            if(p1.movesHistory.size() == p2.movesHistory.size()){
                return p1.getName() - p2.getName();
            }
            return p1.movesHistory.size() - p2.movesHistory.size();
        }
        if(p1.getOwner() == player){
            return -1;
        }

        return 1;
    }
}
