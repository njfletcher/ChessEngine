package Chess;

public class GameState {


    //1 for white, -1 for black
    public static int sideToMove =1;

    public static long[] statePieces;

    public static int botSide;

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

    public static void updateIndivBoards(){

        bPawns = statePieces[0];
        bRooks = statePieces[1];
        bKnights = statePieces[2];
        bKing = statePieces[3];
        bQueens = statePieces[4];
        bBishops = statePieces[5];
        wPawns = statePieces[6];
        wRooks = statePieces[7];
        wKnights = statePieces[8];
        wKing = statePieces[9];
        wQueens = statePieces[10];
        wBishops = statePieces[11];

    }



    public static boolean currentPlayerIsWhite(){

        if(sideToMove == 0b1L){
            return true;
        }

        else{
            return false;
        }
    }

    public static long[] generatePiecesArray(){
        long[] pieces = {

                bPawns, bRooks, bKnights, bKing, bQueens, bBishops,
                wPawns, wRooks, wKnights, wKing, wQueens, wBishops

        };

        return pieces;

    }

    public static void updatePiecesArray(){
        statePieces  = new long[]{

                bPawns, bRooks, bKnights, bKing, bQueens, bBishops,
                wPawns, wRooks, wKnights, wKing, wQueens, wBishops

        };



    }


    //1 for white, 0 for black
    //By defualt white moves first, unless the engine is fed a position which takes place during a game.



    /*
   4bit long

   wKingside, wQueenside, bkingside, bQueenside.

    1111 by defualt, unless loading in a midgame position.
    */


    public static long castleRights = 0b0000L;


    //holds square of possible en passant target squares.
    public static int enPassant = 64;




}
