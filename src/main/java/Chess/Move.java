package Chess;

public class Move {

    public int squareFrom;
    public int squareTo;
    long[] bitboardCopys;

    public int pieceType;

    boolean capture;
    boolean castle;
    boolean promotion;
    boolean enPassant;
    boolean check;

    public Move(int s2, int sFro, long[] copies, int pType){
        squareFrom =sFro;
        squareTo = s2;
        bitboardCopys = copies;
        pieceType = pType;
    }


}
