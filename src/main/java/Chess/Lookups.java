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
            0b0000000000000000000000000111111001111110000000000000000000000000L


    };

    public static long[] castleTables = {


            //squares of concern for white castling, left side
            0b0000000000000000000000000000000000000000000000000000000000001110L,

            //squares of concern for white castling, right side
            0b0000000000000000000000000000000000000000000000000000000001100000L,




            //squares of concern for black castling, right side
            0b0110000000000000000000000000000000000000000000000000000000000000L,

            //squares of concern for black castling, left side
            0b0000111000000000000000000000000000000000000000000000000000000000L


    };





        public static int getIndexPromotion(char p){

            if(GameState.sideToMove ==1){
                if(p == 'q'){
                    return 10;
                }
                if(p == 'r'){
                    return 7;
                }
                if(p == 'b'){
                    return 11;
                }
                if(p == 'n') {
                    return 8;
                }
                else return -1;
            }
            else{
                if(p == 'q'){
                    return 4;
                }
                if(p == 'r'){
                    return 1;
                }
                if(p == 'b'){
                    return 5;
                }
                if(p == 'n'){
                    return 2;
                }
                else return -1;
            }


        }



          public static long[] rookMagics = {
                  0xa8002c000108020L,
                  0x6c00049b0002001L,
                  0x100200010090040L,
                  0x2480041000800801L,
                  0x280028004000800L,
                  0x900410008040022L,
                  0x280020001001080L,
                  0x2880002041000080L,
                  0xa000800080400034L,
                  0x4808020004000L,
                  0x2290802004801000L,
                  0x411000d00100020L,
                  0x402800800040080L,
                  0xb000401004208L,
                  0x2409000100040200L,
                  0x1002100004082L,
                  0x22878001e24000L,
                  0x1090810021004010L,
                  0x801030040200012L,
                  0x500808008001000L,
                  0xa08018014000880L,
                  0x8000808004000200L,
                  0x201008080010200L,
                  0x801020000441091L,
                  0x800080204005L,
                  0x1040200040100048L,
                  0x120200402082L,
                  0xd14880480100080L,
                  0x12040280080080L,
                  0x100040080020080L,
                  0x9020010080800200L,
                  0x813241200148449L,
                  0x491604001800080L,
                  0x100401000402001L,
                  0x4820010021001040L,
                  0x400402202000812L,
                  0x209009005000802L,
                  0x810800601800400L,
                  0x4301083214000150L,
                  0x204026458e001401L,
                  0x40204000808000L,
                  0x8001008040010020L,
                  0x8410820820420010L,
                  0x1003001000090020L,
                  0x804040008008080L,
                  0x12000810020004L,
                  0x1000100200040208L,
                  0x430000a044020001L,
                  0x280009023410300L,
                  0xe0100040002240L,
                  0x200100401700L,
                  0x2244100408008080L,
                  0x8000400801980L,
                  0x2000810040200L,
                  0x8010100228810400L,
                  0x2000009044210200L,
                  0x4080008040102101L,
                  0x40002080411d01L,
                  0x2005524060000901L,
                  0x502001008400422L,
                  0x489a000810200402L,
                  0x1004400080a13L,
                  0x4000011008020084L,
                  0x26002114058042L,
        };
        public static long[] bishopMagics = {
                0x89a1121896040240L,
                0x2004844802002010L,
                0x2068080051921000L,
                0x62880a0220200808L,
                0x4042004000000L,
                0x100822020200011L,
                0xc00444222012000aL,
                0x28808801216001L,
                0x400492088408100L,
                0x201c401040c0084L,
                0x840800910a0010L,
                0x82080240060L,
                0x2000840504006000L,
                0x30010c4108405004L,
                0x1008005410080802L,
                0x8144042209100900L,
                0x208081020014400L,
                0x4800201208ca00L,
                0xf18140408012008L,
                0x1004002802102001L,
                0x841000820080811L,
                0x40200200a42008L,
                0x800054042000L,
                0x88010400410c9000L,
                0x520040470104290L,
                0x1004040051500081L,
                0x2002081833080021L,
                0x400c00c010142L,
                0x941408200c002000L,
                0x658810000806011L,
                0x188071040440a00L,
                0x4800404002011c00L,
                0x104442040404200L,
                0x511080202091021L,
                0x4022401120400L,
                0x80c0040400080120L,
                0x8040010040820802L,
                0x480810700020090L,
                0x102008e00040242L,
                0x809005202050100L,
                0x8002024220104080L,
                0x431008804142000L,
                0x19001802081400L,
                0x200014208040080L,
                0x3308082008200100L,
                0x41010500040c020L,
                0x4012020c04210308L,
                0x208220a202004080L,
                0x111040120082000L,
                0x6803040141280a00L,
                0x2101004202410000L,
                0x8200000041108022L,
                0x21082088000L,
                0x2410204010040L,
                0x40100400809000L,
                0x822088220820214L,
                0x40808090012004L,
                0x910224040218c9L,
                0x402814422015008L,
                0x90014004842410L,
                0x1000042304105L,
                0x10008830412a00L,
                0x2520081090008908L,
                0x40102000a0a60140L,
        };





         public static int[] rookIndexBits = {

                12, 11, 11, 11, 11, 11, 11, 12,
                11, 10, 10, 10, 10, 10, 10, 11,
                11, 10, 10, 10, 10, 10, 10, 11,
                11, 10, 10, 10, 10, 10, 10, 11,
                11, 10, 10, 10, 10, 10, 10, 11,
                11, 10, 10, 10, 10, 10, 10, 11,
                11, 10, 10, 10, 10, 10, 10, 11,
                12, 11, 11, 11, 11, 11, 11, 12
        };


        public static int[] bishopIndexBits = {
                6, 5, 5, 5, 5, 5, 5, 6,
                5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 7, 7, 7, 7, 5, 5,
                5, 5, 7, 9, 9, 7, 5, 5,
                5, 5, 7, 9, 9, 7, 5, 5,
                5, 5, 7, 7, 7, 7, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5,
                6, 5, 5, 5, 5, 5, 5, 6
        };

        public static long[] bishopMasks = {

                0b0000000001000000001000000001000000001000000001000000001000000000L,
                0b0000000000000000010000000010000000010000000010000000010000000000L,
                0b0000000000000000000000000100000000100000000100000000101000000000L,
                0b0000000000000000000000000000000001000000001000100001010000000000L,
                0b0000000000000000000000000000000000000010010001000010100000000000L,
                0b0000000000000000000000000000001000000100000010000101000000000000L,
                0b0000000000000000000000100000010000001000000100000010000000000000L,
                0b0000000000000010000001000000100000010000001000000100000000000000L,
                0b0000000000100000000100000000100000000100000000100000000000000000L,
                0b0000000001000000001000000001000000001000000001000000000000000000L,
                0b0000000000000000010000000010000000010000000010100000000000000000L,
                0b0000000000000000000000000100000000100010000101000000000000000000L,
                0b0000000000000000000000000000001001000100001010000000000000000000L,
                0b0000000000000000000000100000010000001000010100000000000000000000L,
                0b0000000000000010000001000000100000010000001000000000000000000000L,
                0b0000000000000100000010000001000000100000010000000000000000000000L,
                0b0000000000010000000010000000010000000010000000000000001000000000L,
                0b0000000000100000000100000000100000000100000000000000010000000000L,
                0b0000000001000000001000000001000000001010000000000000101000000000L,
                0b0000000000000000010000000010001000010100000000000001010000000000L,
                0b0000000000000000000000100100010000101000000000000010100000000000L,
                0b0000000000000010000001000000100001010000000000000101000000000000L,
                0b0000000000000100000010000001000000100000000000000010000000000000L,
                0b0000000000001000000100000010000001000000000000000100000000000000L,
                0b0000000000001000000001000000001000000000000000100000010000000000L,
                0b0000000000010000000010000000010000000000000001000000100000000000L,
                0b0000000000100000000100000000101000000000000010100001000000000000L,
                0b0000000001000000001000100001010000000000000101000010001000000000L,
                0b0000000000000010010001000010100000000000001010000100010000000000L,
                0b0000000000000100000010000101000000000000010100000000100000000000L,
                0b0000000000001000000100000010000000000000001000000001000000000000L,
                0b0000000000010000001000000100000000000000010000000010000000000000L,
                0b0000000000000100000000100000000000000010000001000000100000000000L,
                0b0000000000001000000001000000000000000100000010000001000000000000L,
                0b0000000000010000000010100000000000001010000100000010000000000000L,
                0b0000000000100010000101000000000000010100001000100100000000000000L,
                0b0000000001000100001010000000000000101000010001000000001000000000L,
                0b0000000000001000010100000000000001010000000010000000010000000000L,
                0b0000000000010000001000000000000000100000000100000000100000000000L,
                0b0000000000100000010000000000000001000000001000000001000000000000L,
                0b0000000000000010000000000000001000000100000010000001000000000000L,
                0b0000000000000100000000000000010000001000000100000010000000000000L,
                0b0000000000001010000000000000101000010000001000000100000000000000L,
                0b0000000000010100000000000001010000100010010000000000000000000000L,
                0b0000000000101000000000000010100001000100000000100000000000000000L,
                0b0000000001010000000000000101000000001000000001000000001000000000L,
                0b0000000000100000000000000010000000010000000010000000010000000000L,
                0b0000000001000000000000000100000000100000000100000000100000000000L,
                0b0000000000000000000000100000010000001000000100000010000000000000L,
                0b0000000000000000000001000000100000010000001000000100000000000000L,
                0b0000000000000000000010100001000000100000010000000000000000000000L,
                0b0000000000000000000101000010001001000000000000000000000000000000L,
                0b0000000000000000001010000100010000000010000000000000000000000000L,
                0b0000000000000000010100000000100000000100000000100000000000000000L,
                0b0000000000000000001000000001000000001000000001000000001000000000L,
                0b0000000000000000010000000010000000010000000010000000010000000000L,
                0b0000000000000010000001000000100000010000001000000100000000000000L,
                0b0000000000000100000010000001000000100000010000000000000000000000L,
                0b0000000000001010000100000010000001000000000000000000000000000000L,
                0b0000000000010100001000100100000000000000000000000000000000000000L,
                0b0000000000101000010001000000001000000000000000000000000000000000L,
                0b0000000001010000000010000000010000000010000000000000000000000000L,
                0b0000000000100000000100000000100000000100000000100000000000000000L,
                0b0000000001000000001000000001000000001000000001000000001000000000L,
        };

        public static long[] rookMasks = {

                0b0000000000000001000000010000000100000001000000010000000101111110L,
                0b0000000000000010000000100000001000000010000000100000001001111100L,
                0b0000000000000100000001000000010000000100000001000000010001111010L,
                0b0000000000001000000010000000100000001000000010000000100001110110L,
                0b0000000000010000000100000001000000010000000100000001000001101110L,
                0b0000000000100000001000000010000000100000001000000010000001011110L,
                0b0000000001000000010000000100000001000000010000000100000000111110L,
                0b0000000010000000100000001000000010000000100000001000000001111110L,
                0b0000000000000001000000010000000100000001000000010111111000000000L,
                0b0000000000000010000000100000001000000010000000100111110000000000L,
                0b0000000000000100000001000000010000000100000001000111101000000000L,
                0b0000000000001000000010000000100000001000000010000111011000000000L,
                0b0000000000010000000100000001000000010000000100000110111000000000L,
                0b0000000000100000001000000010000000100000001000000101111000000000L,
                0b0000000001000000010000000100000001000000010000000011111000000000L,
                0b0000000010000000100000001000000010000000100000000111111000000000L,
                0b0000000000000001000000010000000100000001011111100000000100000000L,
                0b0000000000000010000000100000001000000010011111000000001000000000L,
                0b0000000000000100000001000000010000000100011110100000010000000000L,
                0b0000000000001000000010000000100000001000011101100000100000000000L,
                0b0000000000010000000100000001000000010000011011100001000000000000L,
                0b0000000000100000001000000010000000100000010111100010000000000000L,
                0b0000000001000000010000000100000001000000001111100100000000000000L,
                0b0000000010000000100000001000000010000000011111101000000000000000L,
                0b0000000000000001000000010000000101111110000000010000000100000000L,
                0b0000000000000010000000100000001001111100000000100000001000000000L,
                0b0000000000000100000001000000010001111010000001000000010000000000L,
                0b0000000000001000000010000000100001110110000010000000100000000000L,
                0b0000000000010000000100000001000001101110000100000001000000000000L,
                0b0000000000100000001000000010000001011110001000000010000000000000L,
                0b0000000001000000010000000100000000111110010000000100000000000000L,
                0b0000000010000000100000001000000001111110100000001000000000000000L,
                0b0000000000000001000000010111111000000001000000010000000100000000L,
                0b0000000000000010000000100111110000000010000000100000001000000000L,
                0b0000000000000100000001000111101000000100000001000000010000000000L,
                0b0000000000001000000010000111011000001000000010000000100000000000L,
                0b0000000000010000000100000110111000010000000100000001000000000000L,
                0b0000000000100000001000000101111000100000001000000010000000000000L,
                0b0000000001000000010000000011111001000000010000000100000000000000L,
                0b0000000010000000100000000111111010000000100000001000000000000000L,
                0b0000000000000001011111100000000100000001000000010000000100000000L,
                0b0000000000000010011111000000001000000010000000100000001000000000L,
                0b0000000000000100011110100000010000000100000001000000010000000000L,
                0b0000000000001000011101100000100000001000000010000000100000000000L,
                0b0000000000010000011011100001000000010000000100000001000000000000L,
                0b0000000000100000010111100010000000100000001000000010000000000000L,
                0b0000000001000000001111100100000001000000010000000100000000000000L,
                0b0000000010000000011111101000000010000000100000001000000000000000L,
                0b0000000001111110000000010000000100000001000000010000000100000000L,
                0b0000000001111100000000100000001000000010000000100000001000000000L,
                0b0000000001111010000001000000010000000100000001000000010000000000L,
                0b0000000001110110000010000000100000001000000010000000100000000000L,
                0b0000000001101110000100000001000000010000000100000001000000000000L,
                0b0000000001011110001000000010000000100000001000000010000000000000L,
                0b0000000000111110010000000100000001000000010000000100000000000000L,
                0b0000000001111110100000001000000010000000100000001000000000000000L,
                0b0111111000000001000000010000000100000001000000010000000100000000L,
                0b0111110000000010000000100000001000000010000000100000001000000000L,
                0b0111101000000100000001000000010000000100000001000000010000000000L,
                0b0111011000001000000010000000100000001000000010000000100000000000L,
                0b0110111000010000000100000001000000010000000100000001000000000000L,
                0b0101111000100000001000000010000000100000001000000010000000000000L,
                0b0011111001000000010000000100000001000000010000000100000000000000L,
                0b0111111010000000100000001000000010000000100000001000000000000000L,

        };

        static long[][] bishopMagicAttacks = new long[64][512];

        static long[][] rookMagicAttacks = new long[64][4096];

        static long[] kingLookups = new long[64];

        static long[] knightLookups = new long[64];


        static long[][] wPawnLookups = new long[64][2];
        static long[][] bPawnLookups = new long[64][2];




    }




