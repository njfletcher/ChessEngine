package Chess;

import java.util.Arrays;

public class ChessBoard {

    //IDEA: MAKE EACH METHOD RETURN AN ARRAY. FIRST INDEX IS MOVES, SECOND IS ATTACK MASK.
    //MAYBE JUST FOR PAWNS

    public long squareToBitboard(String uciPosition) {

        //GOAL: take incoming board position and turn it into the required bitboards/update existing bitboards.
        //until done just use static starting positions.


        System.out.println(boardSqs.getBitofSquare(uciPosition));

        long bits = (1L << boardSqs.getBitofSquare(uciPosition) );

        return bits;

    }



    public void calculateKingMoves(long kingBitBoard, long ownSideBitboard, long[] lookupTables){

        /*
        [0, 0, 0, 0, 0, 0, 0, 0]
        [0, 0, 0, 0, 0, 0, 0, 0]
        [0, 0, 0, 0, 0, 0, 0, 0]
        [0, 0, 0, 1, 2, 3, 0, 0]
        [0, 0, 0, 8, x, 4, 0, 0]
        [0, 0, 0, 7, 6, 5, 0, 0]
        [0, 0, 0, 0, 0, 0, 0, 0]
        [0, 0, 0, 0, 0, 0, 0, 0]

         */

        //8 possible moves for a king from a given position, unless on a border, under check, or attempting to move into a piece that is
        // under the watch of another piece.
         long kingAFile = kingBitBoard & lookupTables[0];
         long kingHFile = kingBitBoard & lookupTables[lookupTables.length-2];

         long move1 = kingAFile <<7;
         long move2 = kingBitBoard <<8;
         long move3 = kingHFile <<9;
         long move4 = kingHFile <<1;

         long move5 = kingHFile >> 7;
         long move6 = kingBitBoard >>8;
         long move7  = kingAFile >>9;
         long move8 = kingAFile >>1;

         long kingPsuedos = move1 | move2| move3| move4| move5| move6| move7| move8;

         long kingReals = kingPsuedos & ~ownSideBitboard;

         printBitBoard(kingReals);


    }

    public void calculateKnightMoves(long knightLocation, long ownSideBitboard, long[] lookuptables){

                /*
        [0, 0, 0, 0, 0, 0, 0, 0]
        [0, 0, 0, 0, 0, 0, 0, 0]
        [0, 0, 0, 2, 0, 3, 0, 0]
        [0, 0, 1, 0, 0, 0, 4, 0]
        [0, 0, 0, 0, x, 0, 0, 0]
        [0, 0, 8, 0, 0, 0, 5, 0]
        [0, 0, 0, 7, 0, 6, 0, 0]
        [0, 0, 0, 0, 0, 0, 0, 0]

         */

        //8 possible moves for a knight, depending on which file you are on. Cannot move into a discovered check for your own king.

        //checking to see if knight is on a or b file
        long spot1Clip = lookuptables[0] & lookuptables[1] & knightLocation;
        //checking to see if knight is on a file
        long spot2Clip = lookuptables[0] & knightLocation;
        //checking to see if knight is on h file
        long spot3Clip = lookuptables[lookuptables.length-2] & knightLocation;
        //checking to see if knight is on g or h file
        long spot4Clip = lookuptables[3] & lookuptables[lookuptables.length-2] & knightLocation;

        long spot5Clip = spot4Clip;
        long spot6Clip = spot3Clip;
        long spot7Clip = spot2Clip;
        long spot8Clip = spot1Clip;

        long spot1 = spot1Clip <<6;
        long spot2 = spot2Clip <<15;
        long spot3 = spot3Clip <<17;
        long spot4 = spot4Clip <<10;

        long spot5 = spot5Clip >> 6;
        long spot6 = spot6Clip >> 15;
        long spot7 = spot7Clip >>17;
        long spot8 = spot8Clip >>10;


        long knightPsuedos = spot1 | spot2 | spot3 | spot4 | spot5 | spot6| spot7 | spot8;
        long knightLegals = knightPsuedos & ~ownSideBitboard;

        printBitBoard(knightLegals);


    }


    public void calculateWhitePawnMoves(long pawnLocation, long ownSideBitboard, long blackPieces,long allPieces, long[] lookuptables){

        long oneStep = pawnLocation << 8 & ~allPieces;

        //checking if said pawn is on 2 rank
        long twoSteps = ((oneStep & (Lookups.rankTables[0]<<16))<<8) & ~allPieces;

        long legalMoves = oneStep | twoSteps;

        //checking for if pawn is on right border
        long pawnEastAttack = (pawnLocation & Lookups.fileTables[0])<<7;

        //checking for if pawn is on left border
        long pawnWestAttack = (pawnLocation & Lookups.fileTables[Lookups.fileTables.length-2])<<9;

        long pawnAttacks = pawnEastAttack | pawnWestAttack;
        long pawnLegalAttacks = pawnAttacks & blackPieces;

        long whiteValidGeneral = pawnLegalAttacks | legalMoves;

        //printBitBoard(whiteValidGeneral);



    }
    public void calculateBlackPawnMoves(long pawnLocation, long ownSideBitboard, long WhitePieces,long allPieces, long[] lookupTables){

        long oneStep = pawnLocation >> 8 & ~allPieces;

        //checking for if said pawn is on 7th rank.
        long twoSteps = ((oneStep & (Lookups.rankTables[0]<<40))>>8) & ~allPieces;

        long legalMoves = oneStep | twoSteps;

        //checking for if pawn is on left border
        long pawnEastAttack = (pawnLocation & Lookups.fileTables[0])>>9;
        //checking for if pawn if on right border
        long pawnWestAttack = (pawnLocation & Lookups.fileTables[Lookups.fileTables.length-2])>>7;

        long pawnAttacks = pawnEastAttack | pawnWestAttack;
        long pawnLegalAttacks = pawnAttacks & WhitePieces;

        long blackValidGeneral = pawnLegalAttacks | legalMoves;

        //printBitBoard(blackValidGeneral);


    }

    public long[] calculateRookMoves(int square, long ownSideBitboard, long oppositePieces, long allPieces){

        long moves= calcCross(square, allPieces);

        return new long[]{moves & ~ownSideBitboard,(moves & ~ownSideBitboard) & oppositePieces};
    }

    public long[] calculateBishopMoves(int square, long ownSide, long oppositePieces, long allPieces){


        long moves = calcDiagonal(square,allPieces);

        return new long[]{moves & ~ownSide,(moves & ~ownSide) & oppositePieces};



    }

    public long[] calculateQueenMoves(int square, long ownSideBitBoard, long oppositeSidePieces, long allPieces ){

        long crosses = calcCross(square,allPieces);

        long diagonals = calcDiagonal(square,allPieces);

        long moves = crosses | diagonals;


        return new long[]{moves & ~ownSideBitBoard,(moves & ~ownSideBitBoard) & oppositeSidePieces };

    }

    public long[] calculateWKCastle(int kSquare, long kingOccupancy, long singleRookOccupancy){

        kingOccupancy <<= 2;
        singleRookOccupancy >>= 2;

        return new long[]{kingOccupancy,singleRookOccupancy};
    }
    public long[] calculateWQCastle(int kSquare, long kingOccupancy, long singleRookOccupancy){
        kingOccupancy >>= 2;
        singleRookOccupancy <<=3;

        return new long[]{kingOccupancy,singleRookOccupancy};

    }
    public long[] calculateBKCastle(int kSquare, long kingOccupancy, long singleRookOccupancy){
        kingOccupancy <<= 2;
        singleRookOccupancy >>=2;

        return new long[]{kingOccupancy,singleRookOccupancy};

    }
    public long[] calculateBQCastle(int kSquare, long kingOccupancy, long singleRookOccupancy){
        kingOccupancy >>= 2;
        singleRookOccupancy <<=3;

        return new long[]{kingOccupancy,singleRookOccupancy};

    }



    public long calcDiagonal(int square, long allPieces){
        int r,f;
        int tr = square /8;
        int tf = square %8;

        long attacksB = 0L;

        //System.out.println("Square: " + i + " tr: " + tr + " tf: " + tf + " target Square: " + (((tr+1) * 8) + (tf+1)));

        //northeast ray
        for(r = tr+1, f =tf+1; r<=7 && f<=6;r++, f++){
            attacksB |= 1L << (r*8 +f);
            if(((1L << (r*8 +f)) & allPieces) != 0){
                break;
            }
        }

        for(r = tr+1, f =tf-1; r<=7 && f>=1;r++, f--){
            attacksB |= 1L << (r*8 +f);
            if(((1L << (r*8 +f)) & allPieces) != 0){
                break;
            }
        }

        for(r = tr-1, f =tf+1; r>=0 && f<=6;r--, f++){
            attacksB |= 1L << (r*8 +f);
            if(((1L << (r*8 +f)) & allPieces) != 0){
                break;
            }
        }

        for(r = tr-1, f =tf-1; r>=0 && f>=1;r--, f--) {
            attacksB |= 1L << (r * 8 + f);
            if (((1L << (r * 8 + f)) & allPieces) != 0) {
                break;
            }
        }
            return attacksB;
    }

    public long calcCross(int square, long allPieces){

        int r,f;
        int tr = square /8;
        int tf = square %8;

        long attacksR = 0L;

        //System.out.println("Square: " + i + " tr: " + tr + " tf: " + tf + " target Square: " + (((tr+1) * 8) + (tf+1)));

        //northeast ray
        for(r = tr+1; r<=7 ;r++){
            attacksR |= 1L << (r*8 +tf);
            if(((1L << (r*8 +tf)) & allPieces) != 0){
                break;
            }

        }

        for(r = tr-1; r>=0;r--){
            attacksR |= 1L << (r*8 +tf);
            if(((1L << (r*8 +tf)) & allPieces) != 0){
                break;
            }
        }

        for(f = tf+1; f<=7;f++){
            attacksR |= 1L << (tr*8 +f);
            if(((1L << (tr*8 +f)) & allPieces) != 0){
                break;
            }
        }
        for(f = tf-1; f>=0;f--){
            attacksR |= 1L << (tr*8 +f);
            if(((1L << (tr*8 +f)) & allPieces) != 0){
                break;
            }
        }

        return attacksR;

    }



    public void findSignificantSetBits(long bitboard){

        int leastSig = -1;
        int mostSig= -1;

        


    }
    public void initSlidingOMasks(){

        //Goal: loop through each square and initialize three arrays: queenAttacks, rook attacks, bishop attack, for each square.
        //will be used to initialize arrays used with magic bitboards.

        Lookups.rookOccupancies = new long[64];
        Lookups.bishopOccupancies = new long[64];
        Lookups.queenOccupancies = new long[64];


        for(int i =0;i<64;i++){

            int r,f;
            int tr = i /8;
            int tf = i %8;



            //initializes rook attack masks------------------------

            long attacksR = 0L;

            //System.out.println("Square: " + i + " tr: " + tr + " tf: " + tf + " target Square: " + (((tr+1) * 8) + (tf+1)));

            //northeast ray
            for(r = tr+1; r<=7 ;r++){
                attacksR |= 1L << (r*8 +tf);
            }

            for(r = tr-1; r>=0;r--){
                attacksR |= 1L << (r*8 +tf);
            }

            for(f = tf+1; f<=7;f++){
                attacksR |= 1L << (tr*8 +f);
            }
            for(f = tf-1; f>=0;f--){
                attacksR |= 1L << (tr*8 +f);
            }

            Lookups.rookOccupancies[i] = (attacksR);

            //printBitBoard(attacksR);


            //initializes bishop attack masks-----------------------

            long attacksB = 0L;

            //System.out.println("Square: " + i + " tr: " + tr + " tf: " + tf + " target Square: " + (((tr+1) * 8) + (tf+1)));

            //northeast ray
            for(r = tr+1, f =tf+1; r<=7 && f<=6;r++, f++){
                attacksB |= 1L << (r*8 +f);
            }

            for(r = tr+1, f =tf-1; r<=7 && f>=1;r++, f--){
                attacksB |= 1L << (r*8 +f);
            }

            for(r = tr-1, f =tf+1; r>=0 && f<=6;r--, f++){
                attacksB |= 1L << (r*8 +f);
            }

            for(r = tr-1, f =tf-1; r>=0 && f>=1;r--, f--){
                attacksB |= 1L << (r*8 +f);
            }


            Lookups.bishopOccupancies[i] = (attacksB);

            //printBitBoard(attacksB);


            //initializes queen masks by combining other attacks----

            long attacksQ = attacksB | attacksR;

            Lookups.queenOccupancies[i] = (attacksQ);

            //printBitBoard(attacksQ);

        }

    }

    public boolean checkForCheck(long kingBit, long attackMask){


        if((kingBit & attackMask) !=0){
            System.out.println("Check!");
            return true;
        }
        else{
            System.out.println("No Check!");
            return false;
        }
    }




    public static void printBitBoard(Long pieceBoard) {

        String full = "";
        //String square = rank *8 + file;
        String s = Long.toBinaryString(pieceBoard);

        //System.out.println(64 - Long.toBinaryString(pieceBoard).length());

        if (Long.toBinaryString(pieceBoard).length() == 64) {
            full = Long.toBinaryString(pieceBoard);
            s = full;
        } else {

            full = String.format("%0" + (64 - Long.toBinaryString(pieceBoard).length()) + 'd', 0);
            s = full + "" + s;

        }

        System.out.println(s);


        int[][] board = new int[8][8];
        int p = 0;

        for (int rank = 0; rank < 8; rank++)
            for (int file = 7; file >=0; file--) {
                board[rank][file] = Integer.parseInt(s.substring(p, p + 1));
                p++;
            }


        //prints 2d array representation of the bitboard, making it look like a chessboard.
        for (int[] array : board) {

            System.out.println(Arrays.toString(array));

        }
    }



}