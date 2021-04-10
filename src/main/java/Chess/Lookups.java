package Chess;

public class Lookups {


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

    public static long[] evalTables = {


            //Middle of the board, used mostly for checking for centralized pawns.
            0b0000000000000000011111100111111001111110011111100000000000000000L


    };

    public static long[] castleTables = {


            //squares of concern for white castling
            0b0000000000000000000000000000000000000000000000000000000001101110L,

            //squares of concern for black castling
            0b0111011000000000000000000000000000000000000000000000000000000000L


    };


    //public static long borderClip = 0b000000001111110011111100111111001111110011111100111111000000000L;








}
