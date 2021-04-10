package Chess;

import java.util.ArrayList;

public class Move {

    public int squareFrom;
    public int squareTo;
    long[] bitboardCopys;

    long castleRightsCopy;

    public int capturedPieceType;

    ArrayList<Move> moves;

    public int pieceType;

    boolean capture;
    boolean castle;
    boolean promotion;
    boolean enPassant;
    boolean check;


    //normal move, no capture
    public Move(int sFro, int s2, long[] copies, int pType){
        squareFrom =sFro;
        squareTo = s2;
        bitboardCopys = copies;
        pieceType = pType;
    }

    //captures
    public Move(int sFro, int s2, long[] copies, int pType, int capturedPiece){
        squareFrom =sFro;
        squareTo = s2;
        bitboardCopys = copies;
        pieceType = pType;
        capturedPieceType= capturedPiece;
        capture = true;
    }

    //castles
    public Move(long[] copies){

        bitboardCopys = copies;
        castle = true;
        pieceType =1;

    }

    public String toString(){

        return "From: " + squareFrom + "(" + boardSqs.getboardSq(squareFrom) + ")" + "\n"
                + " To : " + squareTo + "(" + boardSqs.getboardSq(squareTo) + ")" + "\n"
                + "Piece: " + pieceType;


    }


}
