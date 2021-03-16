package Chess;

public class Lookups {



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


    public static long wPieces= wPawns | wKnights | wKing | wBishops | wQueens| wRooks;
    public static long bPieces= bPawns | bKnights | bKing | bBishops | bQueens| bRooks;

    public static long allPieces= wPieces | bPieces;


    public static long[] fileTables = {

            // used mainly for checking if a piece is a on a border, therefore nulling any
            // moves going off the board left or right.

            //a clip
            0b1111111011111110111111101111111011111110111111101111111011111110L,
            //b clip
            0b1111110111111101111111011111110111111101111111011111110111111101L,
            //g clip
            0b1011111110111111101111111011111110111111101111111011111110111111L,
            //h clip
            0b0111111101111111011111110111111101111111011111110111111101111111L,

            //a mask
            // other masks can be computed by shifting this mask left by icrements of 1.
            //Example: bmask = amask <<1;
            0b0000000100000001000000010000000100000001000000010000000100000001L,


    };
    public static long[] rankTables = {

            /*
            - will be used mainly for pawns to check for initial double square move, or promotion.
             - represents masking layer of first rank. To get other rank mask just shift(<<) left by
             intervals of 8.

             */

            //rank1 mask
            0b0000000000000000000000000000000000000000000000000000000011111111L

    };

    public static long[] diagonalTables = {


            //a to h diagonal
            0x8040201008040201L

    };

    //public static long borderClip = 0b000000001111110011111100111111001111110011111100111111000000000L;


    public static long[] rookOccupancies;
    public static long[] bishopOccupancies;
    public static long[] queenOccupancies;





}
