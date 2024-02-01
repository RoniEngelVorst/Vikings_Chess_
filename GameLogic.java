//import org.junit.platform.engine.support.descriptor.FileSystemSource;

import java.util.Collections;
import java.util.LinkedList;

public class GameLogic implements  PlayableLogic{
    private final int boardSide = 11;
    private ConcretePlayer attacker;
    private ConcretePlayer defender;
    private ConcretePiece[][] board;
    private ConcretePlayer currentPlayer;
    private boolean gameFinished = false;
    public LinkedList<ConcretePiece> allPieces = new LinkedList<>();
    public Position[][] boardOfPositions;

    public GameLogic(){
        reset();
    }
    @Override
    public boolean move(Position a, Position b) {
        if(getPieceAtPosition(a).getOwner() != currentPlayer){return false;}
        //checking if we are mooving the king
        boolean king = getPieceAtPosition(a).getType().equals("♔");
        //starting with the not allows moves
        //making sure we are going straight
        if(b.getX() != a.getX() && b.getY() != a.getY()){
            return false;
        }
        if(getPieceAtPosition(a).type == "♙" || getPieceAtPosition(a).type == "♟" ){
            //make sure the pown does not go to the corners
            if(isACorner(b)){
                return false;
            }
        }
        ConcretePiece currentPiece = getPieceAtPosition(a);

        //moving the pieces before killing
        if(b.getX() == a.getX()){
            //down
            if(a.getY() < b.getY()){
                boolean empty = true;
                for(int i = a.getY()+1; i <= b.getY(); i++){
                    if (this.board[a.getX()][i] != null){
                        empty = false;
                        return false;
                    }
                }
                if(empty == true){
                    changePosition(a,b);

                }
                else{
                    return false;
                }
            }
            //up
            else{
                boolean empty = true;
                for(int i = b.getY()+1; i <= a.getY()-1;i++){
                    if (this.board[a.getX()][i] != null){
                        empty = false;
                        return false;
                    }
                }
                if(empty == true){
                    changePosition(a,b);
                }
                else{
                    return false;
                }
            }

        }


        if(b.getY() == a.getY()){
            //right
            if(a.getX() < b.getX()){
                boolean empty = true;
                for(int i = a.getX()+1; i <= b.getX(); i++){
                    if(this.board[i][a.getY()] != null){
                        empty = false;
                        return false;
                    }
                }
                if(empty == true) {
                    changePosition(a, b);
                }
                else{
                    return false;
                }
            }
            //left
            else{
                boolean empty = true;
                for(int i = b.getX(); i<= a.getX()-1; i++){
                    if(this.board[i][b.getY()] != null){
                        empty = false;
                        return false;
                    }
                }
                if(empty == true){
                    changePosition(a,b);
                }
                else {
                    return false;
                }
            }

        }
        //if the king got to the corner he defender wins
        if(king){
            if(isACorner(b)){
                win(this.defender);
                this.gameFinished = true;
            }
        }
        boolean killed = false;
        //checking if we have a pawn to kill
        int[] x = {0,1,0,-1};
        int[] y = {1,0,-1,0};
        for(int i = 0; i < 4; i++){
            if (isValidPosition(b.getX()+x[i], b.getY()+y[i])&& !king) {
                Position temp = new Position(b.getX() + x[i], b.getY() + y[i]);
                //making sure there is a piece there, and it is not our own and not the king
                if (getPieceAtPosition(temp) != null && getPieceAtPosition(temp).getOwner() != getPieceAtPosition(b).getOwner() && getPieceAtPosition(temp).getType() != "♔") {
                    if(isValidPosition(temp.getX() + x[i], temp.getY() + y[i])) {
                        Position next = new Position(temp.getX() + x[i], temp.getY() + y[i]);
                        if (getPieceAtPosition(next) != null && getPieceAtPosition(next).getOwner() == getPieceAtPosition(b).getOwner()) {
                            killPawn(temp);
                            killed = true;
                        }
                    }
                    //also check if we hit a wall
                    else{
                        killPawn(temp);
                        killed = true;
                    }

                }
                //checking if we killed a king
                if(getPieceAtPosition(temp) != null && getPieceAtPosition(b).getOwner() == this.attacker && getPieceAtPosition(temp).getType() == "♔"){
                    int counter = 0;
                    //down
                    if(isValidPosition(temp.getX(), temp.getY()+1)){
                        Position p = new Position(temp.getX(), temp.getY()+1);
                        if(getPieceAtPosition(p) != null && getPieceAtPosition(p).getOwner() == this.attacker){
                            counter++;
                        }
                    }
                    else{counter++;}

                    //up
                    if(isValidPosition(temp.getX(), temp.getY()-1)){
                        Position p = new Position(temp.getX(), temp.getY()-1);
                        if(getPieceAtPosition(p) != null && getPieceAtPosition(p).getOwner() == this.attacker){
                            counter++;
                        }
                    }
                    else{counter++;}

                    //right
                    if(isValidPosition(temp.getX()+1, temp.getY())){
                        Position p = new Position(temp.getX()+1, temp.getY());
                        if(getPieceAtPosition(p) != null && getPieceAtPosition(p).getOwner() == this.attacker){
                            counter++;
                        }
                    }
                    else{counter++;}

                    //left
                    if(isValidPosition(temp.getX()-1, temp.getY())){
                        Position p = new Position(temp.getX()-1, temp.getY());
                        if(getPieceAtPosition(p) != null && getPieceAtPosition(p).getOwner() == this.attacker){
                            counter++;
                        }
                    }
                    else{counter++;}

                    if(counter ==4){
                        killKing(temp);
                        document(currentPiece,a,b,killed);

                        win(this.attacker);
                        this.gameFinished = true;
                    }
                }
            }
        }



        document(currentPiece,a,b,killed);
        changeTurns(this.currentPlayer);
        return true;
    }

    public void document(ConcretePiece currentPiece, Position a,Position b, boolean killed){
        //documenting the steps
        if(currentPiece.movesHistory.isEmpty()){
            currentPiece.movesHistory.add(a);
            currentPiece.movesHistory.add(b);
        }
        else{
            currentPiece.movesHistory.add(b);
        }
        if(killed){
            currentPiece.numOfKills++;
        }
        boardOfPositions[b.getX()][b.getY()].numOfPieces++;

    }
    public void changeTurns(Player p){
        if(p == this.attacker){this.currentPlayer = this.defender;}
        if(p == this.defender){this.currentPlayer = this.attacker;}
    }
    public void changePosition(Position a, Position b){
        this.board[b.getX()][b.getY()] = getPieceAtPosition(a);
        getPieceAtPosition(a).setPosition(b);
        this.board[a.getX()][a.getY()] = null;
    }

    public boolean isValidPosition(int x, int y){
        if((x > 10) || (x < 0) || (y > 10) || (y < 0)){return false;}
        if(isACorner(new Position(x,y))){return false;}
        else{return true;}
    }

    public boolean isACorner(Position b){
        boolean flag = false;
        if(b.getX() == 0 && b.getY() == 0){flag = true;}
        if(b.getX() == 0 && b.getY() == 10){flag = true;}
        if(b.getX() == 10 && b.getY() == 0){flag = true;}
        if(b.getX() == 10 && b.getY() == 10){flag = true;}
        return flag;
    }

    public void killPawn(Position p){
        this.board[p.getX()][p.getY()] = null;
    }
    public void killKing(Position k){
        this.board[k.getX()][k.getY()] = null;
    }

    public void win(ConcretePlayer p){
        sortMoves(p);
        System.out.println("***************************************************************************");
        sortKills(p);
        System.out.println("***************************************************************************");
        sortSquares(p);
        System.out.println("***************************************************************************");
        sortPieces();
        System.out.println("***************************************************************************");


    }

    public void sortMoves(ConcretePlayer winner){
        Collections.sort(this.allPieces, new movesComparator(winner));
        for(ConcretePiece cp: this.allPieces){
            if(cp.movesHistory.size() > 1){
                System.out.println(cp.toString() + cp.printMovesHistory());
            }
        }
    }
    public void sortKills(ConcretePlayer winner){
        Collections.sort(this.allPieces, new killComparator(winner));
        for(ConcretePiece cp: this.allPieces){
            if(cp.numOfKills > 0){
                System.out.println(cp.toString() + cp.numOfKills + " kills");
            }
        }
    }

    public void sortSquares(ConcretePlayer winner){
        Collections.sort(this.allPieces, new SquaresComparator(winner));
        for(ConcretePiece cp: this.allPieces){
            if(cp.calcSquares() > 0){
                System.out.println(cp.toString() + cp.calcSquares() + " squares");
            }
        }
    }

    public void sortPieces(){
        LinkedList<Position> positionLinkedList = new LinkedList<>();
        for(int i=0; i<boardSide; i++){
            for(int j=0; j<boardSide; j++){
                if(boardOfPositions[i][j].numOfPieces > 1){
                    positionLinkedList.add(boardOfPositions[i][j]);
                }
            }
        }
        Collections.sort(positionLinkedList, new piecesComparator());
        for(Position p: positionLinkedList){
            System.out.println(p.toString() + p.numOfPieces + " pieces");
        }
    }

    @Override
    public ConcretePiece getPieceAtPosition(Position position) {
        return this.board[position.getX()][position.getY()];
    }

    @Override
    public ConcretePlayer getFirstPlayer() {
        return this.defender;
    }

    @Override
    public ConcretePlayer getSecondPlayer() {
        return this.attacker;
    }

    @Override
    public boolean isGameFinished() {
        return gameFinished;
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return (currentPlayer == this.attacker);
    }

    @Override
    public void reset() {
     this.board = new ConcretePiece[boardSide][boardSide];
     this.boardOfPositions = new Position[boardSide][boardSide];
     this.attacker = new ConcretePlayer(false);
     this.defender = new ConcretePlayer(true);
     this.currentPlayer = this.attacker;
     this.gameFinished = false;
    for (int i = 0; i < boardSide; i++) {
        for (int b = 0; b < boardSide; b++) {
            this.boardOfPositions[i][b] = new Position(i, b);
        }
    }



     this.board[3][0] = new Pawn(this.attacker, 1);
     allPieces.add(this.board[3][0]);
     this.boardOfPositions[3][0].numOfPieces++;
     this.board[4][0] = new Pawn(this.attacker, 2);
     allPieces.add(this.board[4][0]);
     this.boardOfPositions[4][0].numOfPieces++;
     this.board[5][0] = new Pawn(this.attacker, 3);
     allPieces.add(this.board[5][0]);
     this.boardOfPositions[5][0].numOfPieces++;
     this.board[6][0] = new Pawn(this.attacker, 4);
     allPieces.add(this.board[6][0]);
     this.boardOfPositions[6][0].numOfPieces++;
     this.board[7][0] = new Pawn(this.attacker, 5);
     allPieces.add(this.board[7][0]);
     this.boardOfPositions[7][0].numOfPieces++;
     this.board[5][1] = new Pawn(this.attacker, 6);
     allPieces.add(this.board[5][1]);
     this.boardOfPositions[5][1].numOfPieces++;
     this.board[0][3] = new Pawn(this.attacker, 7);
     allPieces.add(this.board[0][3]);
     this.boardOfPositions[0][3].numOfPieces++;
     this.board[10][3] = new Pawn(this.attacker, 8);
     allPieces.add(this.board[10][3]);
     this.boardOfPositions[10][3].numOfPieces++;
     this.board[0][4] = new Pawn(this.attacker, 9);
     allPieces.add(this.board[0][4]);
     this.boardOfPositions[0][4].numOfPieces++;
     this.board[10][4] = new Pawn(this.attacker, 10);
     allPieces.add(this.board[10][4]);
     this.boardOfPositions[10][4].numOfPieces++;
     this.board[0][5] = new Pawn(this.attacker, 11);
     allPieces.add(this.board[0][5]);
     this.boardOfPositions[0][5].numOfPieces++;
     this.board[1][5] = new Pawn(this.attacker, 12);
     allPieces.add(this.board[1][5]);
     this.boardOfPositions[1][5].numOfPieces++;
     this.board[9][5] = new Pawn(this.attacker, 13);
     allPieces.add(this.board[9][5]);
     this.boardOfPositions[9][5].numOfPieces++;
     this.board[10][5] = new Pawn(this.attacker, 14);
     allPieces.add(this.board[10][5]);
     this.boardOfPositions[10][5].numOfPieces++;
     this.board[0][6] = new Pawn(this.attacker, 15);
     allPieces.add(this.board[0][6]);
     this.boardOfPositions[0][6].numOfPieces++;
     this.board[10][6] = new Pawn(this.attacker, 16);
     allPieces.add(this.board[10][6]);
     this.boardOfPositions[10][6].numOfPieces++;
     this.board[0][7] = new Pawn(this.attacker, 17);
     allPieces.add(this.board[0][7]);
     this.boardOfPositions[0][7].numOfPieces++;
     this.board[10][7] = new Pawn(this.attacker, 18);
     allPieces.add(this.board[10][7]);
     this.boardOfPositions[10][7].numOfPieces++;
     this.board[5][9] = new Pawn(this.attacker, 19);
     allPieces.add(this.board[5][9]);
     this.boardOfPositions[5][9].numOfPieces++;
     this.board[3][10] = new Pawn(this.attacker, 20);
     allPieces.add(this.board[3][10]);
     this.boardOfPositions[3][10].numOfPieces++;
     this.board[4][10] = new Pawn(this.attacker, 21);
     allPieces.add(this.board[4][10]);
     this.boardOfPositions[4][10].numOfPieces++;
     this.board[5][10] = new Pawn(this.attacker, 22);
     allPieces.add(this.board[5][10]);
     this.boardOfPositions[5][10].numOfPieces++;
     this.board[6][10] = new Pawn(this.attacker, 23);
     allPieces.add(this.board[6][10]);
     this.boardOfPositions[6][10].numOfPieces++;
     this.board[7][10] = new Pawn(this.attacker, 24);
     allPieces.add(this.board[7][10]);
     this.boardOfPositions[7][10].numOfPieces++;

     this.board[5][3] = new Pawn(this.defender, 1);
     allPieces.add(this.board[5][3]);
     this.boardOfPositions[5][3].numOfPieces++;
     this.board[4][4] = new Pawn(this.defender, 2);
     allPieces.add(this.board[4][4]);
     this.boardOfPositions[4][4].numOfPieces++;
     this.board[5][4] = new Pawn(this.defender, 3);
     allPieces.add(this.board[5][4]);
     this.boardOfPositions[5][4].numOfPieces++;
     this.board[6][4] = new Pawn(this.defender, 4);
     allPieces.add(this.board[6][4]);
     this.boardOfPositions[6][4].numOfPieces++;
     this.board[3][5] = new Pawn(this.defender, 5);
     allPieces.add(this.board[3][5]);
     this.boardOfPositions[3][5].numOfPieces++;
     this.board[4][5] = new Pawn(this.defender, 6);
     allPieces.add(this.board[4][5]);
     this.boardOfPositions[4][5].numOfPieces++;
     this.board[5][5] = new King(this.defender, 7);
     allPieces.add(this.board[5][5]);
     this.boardOfPositions[5][5].numOfPieces++;
     this.board[6][5] = new Pawn(this.defender, 8);
     allPieces.add(this.board[6][5]);
     this.boardOfPositions[6][5].numOfPieces++;
     this.board[7][5] = new Pawn(this.defender, 9);
     allPieces.add(this.board[7][5]);
     this.boardOfPositions[7][5].numOfPieces++;
     this.board[4][6] = new Pawn(this.defender, 10);
     allPieces.add(this.board[4][6]);
     this.boardOfPositions[4][6].numOfPieces++;
     this.board[5][6] = new Pawn(this.defender, 11);
     allPieces.add(this.board[5][6]);
     this.boardOfPositions[5][6].numOfPieces++;
     this.board[6][6] = new Pawn(this.defender, 12);
     allPieces.add(this.board[6][6]);
     this.boardOfPositions[6][6].numOfPieces++;
     this.board[5][7] = new Pawn(this.defender, 13);
     allPieces.add(this.board[5][7]);
     this.boardOfPositions[5][7].numOfPieces++;
    }

    @Override
    public void undoLastMove() {

    }

    @Override
    public int getBoardSize() {
        return this.boardSide;
    }
}
