package Chess;

import java.util.ArrayList;

public class Move {

    public int squareFrom;
    public int squareTo;


    //0-63 for actual squares, 64 if enpassant is not possible.
    public int enPassSquare;

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
    public Move(int sFro, int s2, long[] copies, int pType,long castleCopy, int enPassantSquare){
        squareFrom =sFro;
        squareTo = s2;
        bitboardCopys = copies;
        pieceType = pType;
        castleRightsCopy = castleCopy;
        enPassSquare = enPassantSquare;
    }

    //captures
    public Move(int sFro, int s2, long[] copies, int pType, int capturedPiece,long castleCopy, int enPassantSquare){
        squareFrom =sFro;
        squareTo = s2;
        bitboardCopys = copies;
        pieceType = pType;
        capturedPieceType= capturedPiece;
        capture = true;
        castleRightsCopy = castleCopy;
        enPassSquare = enPassantSquare;
    }

    //castles
    public Move(long[] copies,long castleCopy, int enPassantSquare){

        bitboardCopys = copies;
        castle = true;
        pieceType =1;
        castleRightsCopy = castleCopy;
        enPassSquare = enPassantSquare;

    }

    //promoted move only
    public Move(int from, int to,int promoteTo, long[] copies, long castleCopy, int enPassantSquare,int pType){

        squareFrom= from;
        squareTo= to;

        bitboardCopys = copies;
        promotion = true;
        pieceType = pType;
        promotedPieceType = promoteTo;
        castleRightsCopy = castleCopy;
        enPassSquare = enPassantSquare;

    }

    //promoted + capture moves
    public Move(int from, int to,int promoteTo,int capturedPiece, long[] copies, long castleCopy, int enPassantSquare,int pType){

        squareFrom= from;
        squareTo= to;

        capturedPieceType = capturedPiece;

        bitboardCopys = copies;
        promotion = true;
        capture= true;
        pieceType = pType;
        promotedPieceType = promoteTo;
        castleRightsCopy = castleCopy;
        enPassSquare = enPassantSquare;

    }
    //enPassant
    public Move(int from, int to,int capturedPiece, long[] copies,int pType, long castleCopy, int enPassantSquare){

        squareFrom= from;
        squareTo= to;

        capturedPieceType = capturedPiece;

        bitboardCopys = copies;
        pieceType = pType;
        enPassant = true;
        castleRightsCopy = castleCopy;
        enPassSquare = enPassantSquare;

    }

    public String toString(){

        return "From: " + squareFrom + "(" + boardSqs.getboardSq(squareFrom) + ")" + "\n"
                + " To : " + squareTo + "(" + boardSqs.getboardSq(squareTo) + ")" + "\n"
                + "Piece: " + pieceType + "\n" +
                "EnPass Sqaure: " + enPassSquare +
                "\n" + "Capture/Castle/Promotion/enPassant/Check" + "\n" +
                (capture ? 1:0) + (castle ? 1:0) + (promotion ? 1:0) + (enPassant ? 1:0) + (check ? 1:0);


    }


}
