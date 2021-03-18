package Chess;

public class GameState {

    public static long wPawns = 0L;
    public static long wRooks = 0L;
    public static long wKnights = 0L;
    public static long wKing =  0L;
    public static long wBishops = 0L;
    public static long wQueens = 0L;

    public static long bPawns = 0L;
    public static long bKnights = 0L;
    public static long bKing = 0L;
    public static long bBishops= 0L;
    public static long bQueens = 0L;
    public static long bRooks = 0L;


    public static long[] pieces = {

            bPawns, bRooks, bKnights, bKing, bQueens, bBishops,
            wPawns, wRooks, wKnights, wKing, wQueens, wBishops

    };

    public static long[] updatePiecesSum(){
        long bPieces= GameState.bPawns | GameState.bKnights | GameState.bKing | GameState.bBishops | GameState.bQueens| GameState.bRooks;

        long wPieces= GameState.wPawns | GameState.wKnights | GameState.wKing | GameState.wBishops | GameState.wQueens| GameState.wRooks;

        long allPieces= wPieces | bPieces;

        return new long[]{bPieces, wPieces,allPieces};

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
