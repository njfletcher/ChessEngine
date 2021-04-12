package Chess;

import java.util.ArrayList;

public class Move {

    public int squareFrom;
    public int squareTo;
    long[] bitboardCopys;

    long castleRightsCopy;

    public int capturedPieceType;
    public int pieceType;
    public int promotedPieceType;

    ArrayList<Move> moves;

    boolean capture;
    boolean castle;
    boolean promotion;
    boolean enPassant;
    boolean check;


    //normal move, no capture
    public Move(int sFro, int s2, long[] copies, int pType,long castleCopy){
        squareFrom =sFro;
        squareTo = s2;
        bitboardCopys = copies;
        pieceType = pType;
    }

    //captures
    public Move(int sFro, int s2, long[] copies, int pType, int capturedPiece,long castleCopy){
        squareFrom =sFro;
        squareTo = s2;
        bitboardCopys = copies;
        pieceType = pType;
        capturedPieceType= capturedPiece;
        capture = true;
    }

    //castles
    public Move(long[] copies,long castleCopy){

        bitboardCopys = copies;
        castle = true;
        pieceType =1;

    }

    //promoted move only
    public Move(int from, int to,int promoteTo, long[] copies, long castleCopy){

        squareFrom= from;
        squareTo= to;

        bitboardCopys = copies;
        promotion = true;
        pieceType =0;
        promotedPieceType = promoteTo;

    }

    //promoted + capture moves
    public Move(int from, int to,int promoteTo,int capturedPiece, long[] copies, long castleCopy){

        squareFrom= from;
        squareTo= to;

        capturedPieceType = capturedPiece;

        bitboardCopys = copies;
        promotion = true;
        capture= true;
        pieceType =0;
        promotedPieceType = promoteTo;

    }

    public String toString(){

        return "From: " + squareFrom + "(" + boardSqs.getboardSq(squareFrom) + ")" + "\n"
                + " To : " + squareTo + "(" + boardSqs.getboardSq(squareTo) + ")" + "\n"
                + "Piece: " + pieceType + "\n" + "Capture/Castle/Promotion/enPassant/Check" + "\n" +
                (capture ? 1:0) + (castle ? 1:0) + (promotion ? 1:0) + (enPassant ? 1:0) + (check ? 1:0);


    }


}
