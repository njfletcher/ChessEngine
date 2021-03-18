package Chess;

public class Move {

    public int squareFrom;
    public int squareTo;

    public int pieceType;

    boolean capture;
    boolean castle;
    boolean promotion;
    boolean enPassant;
    boolean check;

    public Move(int s2, int sFro){
        squareFrom =sFro;
        squareTo = s2;
    }


}
