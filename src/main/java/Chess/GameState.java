package Chess;

public class GameState {

    public static long wPawns;
    public static long wRooks;
    public static long wKnights;
    public static long wKing;
    public static long wBishops;
    public static long wQueens;

    public static long bPawns;
    public static long bKnights;
    public static long bKing;
    public static long bBishops;
    public static long bQueens;
    public static long bRooks;




    public static long[] updatePiecesSum(){
        long bPieces= GameState.bPawns | GameState.bKnights | GameState.bKing | GameState.bBishops | GameState.bQueens| GameState.bRooks;

        long wPieces= GameState.wPawns | GameState.wKnights | GameState.wKing | GameState.wBishops | GameState.wQueens| GameState.wRooks;

        long allPieces= wPieces | bPieces;

        return new long[]{bPieces, wPieces,allPieces};

    }

    public static long[] generatePiecesArray(){
        long[] pieces = {

                bPawns, bRooks, bKnights, bKing, bQueens, bBishops,
                wPawns, wRooks, wKnights, wKing, wQueens, wBishops

        };

        return pieces;

    }


    //1 for white, 0 for black
    //By defualt white moves first, unless the engine is fed a position which takes place during a game.
    long sideToMove =1L;


    /*
   4bit long

   wKingside, wQueenside, bkingside, bQueenside.

    1111 by defualt, unless loading in a midgame position.
    */
    public static long castleRights = 1111;


    //holds square of possible en passant target squares.
    public static long whiteEnPassant;
    public static long blackEnPassant;



}
