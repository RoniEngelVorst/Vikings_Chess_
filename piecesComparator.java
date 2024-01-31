import java.util.Comparator;

public class piecesComparator implements Comparator<Position>{

    @Override
    public int compare(Position o1, Position o2) {
        if(o1.numOfPieces == o2.numOfPieces){
            if(o1.getX() == o2.getX()){
                return o1.getY() - o2.getY();
            }
            return o1.getX() - o2.getY();
        }
        return  o2.numOfPieces - o1.numOfPieces;
    }
}
