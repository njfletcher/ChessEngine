package Chess;



import java.util.ArrayList;
import java.util.Arrays;

public class ChessBoard {


    public long squareToBitboard(String uciPosition) {

        //GOAL: take incoming board position and turn it into the required bitboards/update existing bitboards.


        System.out.println(boardSqs.getBitofSquare(uciPosition));

        long bits = (1L << boardSqs.getBitofSquare(uciPosition) );

        return bits;

    }



    public long[] calculateKingMoves(int square, long ownSideBitboard,long enemySide, long allPieces){

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
         long kingBitBoard =1L<< square;
         long kingAFile = kingBitBoard & Lookups.fileTables[0];
         long kingHFile = kingBitBoard & Lookups.fileTables[Lookups.fileTables.length-2];

         long move1 = kingAFile <<7;
         long move2 = kingBitBoard <<8;
         long move3 = kingHFile <<9;
         long move4 = kingHFile <<1;

         long move5 = kingHFile >> 7;
         long move6 = kingBitBoard >>8;
         long move7  = kingAFile >>9;
         long move8 = kingAFile >>1;

         long kingPsuedos = move1 | move2| move3| move4| move5| move6| move7| move8;

         long kingMoves = kingPsuedos & ~allPieces;
         long kingAttacks = kingPsuedos & enemySide;

        return new long[]{kingMoves,kingAttacks};




    }

    public long[] calculateKnightMoves(int square, long ownSideBitboard,long enemySide, long allPieces){

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

        long knightLocation = 1L<<square;

        //checking to see if knight is on a or b file
        long spot1Clip = Lookups.fileTables[0] & Lookups.fileTables[1] & knightLocation;
        //checking to see if knight is on a file
        long spot2Clip = Lookups.fileTables[0] & knightLocation;
        //checking to see if knight is on h file
        long spot3Clip = Lookups.fileTables[Lookups.fileTables.length-2] & knightLocation;
        //checking to see if knight is on g or h file
        long spot4Clip = Lookups.fileTables[3] & Lookups.fileTables[Lookups.fileTables.length-2] & knightLocation;

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
        long knightLegals = knightPsuedos & ~allPieces;
        long knightAttacks = knightPsuedos & enemySide;
        String s = Long.toString(knightLegals);
        String s1 = Long.toString(knightAttacks);



        return new long[]{knightLegals,knightAttacks};


    }


    public long[] calculateWhitePawnMoves(int square, long ownSideBitboard, long blackPieces,long allPieces){

        long pawnLocation = 1L << square;

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

        return new long[]{legalMoves,pawnLegalAttacks};





    }
    public long[] calculateBlackPawnMoves(int square, long ownSideBitboard, long WhitePieces,long allPieces){

        long pawnLocation = 1L <<square;

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

        return new long[]{legalMoves,pawnLegalAttacks};




    }

    public long[] calculateRookMoves(int square, long ownSideBitboard, long oppositePieces, long allPieces){

        long moves= calcCross(square, allPieces);

        return new long[]{moves & ~allPieces,(moves & ~ownSideBitboard) & oppositePieces};
    }

    public long[] calculateBishopMoves(int square, long ownSide, long oppositePieces, long allPieces){


        long moves = calcDiagonal(square,allPieces);

        return new long[]{moves & ~allPieces,(moves & ~ownSide) & oppositePieces};



    }

    public long[] calculateQueenMoves(int square, long ownSideBitBoard, long oppositeSidePieces, long allPieces ){

        long crosses = calcCross(square,allPieces);

        long diagonals = calcDiagonal(square,allPieces);

        long moves = crosses | diagonals;


        return new long[]{moves & ~allPieces,(moves & ~ownSideBitBoard) & oppositeSidePieces };

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


    public boolean checkForCheck(long kingBit, long attackMask){


        if((kingBit & attackMask) !=0){

            return true;
        }
        else{

            return false;
        }
    }



    public long generateSideAttackMask(long sideToMove, long black, long white, long all){

        if(sideToMove ==1L){

            long attackMap =0L;

            ArrayList<Integer> indices = indexSetBits(GameState.bPawns);


            for(Integer num: indices){
                long[] pawnAttacks = calculateBlackPawnMoves(num,black,white,all);
                attackMap |= pawnAttacks[1];
                

            }



            indices = indexSetBits(GameState.bRooks);
            for(Integer num: indices){
                long[] rookAttacks = calculateRookMoves(num,black,white,all);
                attackMap |= rookAttacks[1];


            }


            indices = indexSetBits(GameState.bKnights);
            for(Integer num: indices){
                long[] knightAttacks = calculateKnightMoves(num,black,white,all);
                attackMap |= knightAttacks[1];
                ChessBoard.printBitBoard(attackMap);

            }


            indices = indexSetBits(GameState.bKing);
            for(Integer num: indices){
                long[] kingAttacks = calculateKingMoves(num,black,white,all);
                attackMap |= kingAttacks[1];
                ChessBoard.printBitBoard(attackMap);

            }

            indices = indexSetBits(GameState.bQueens);

            for(Integer num: indices){
                long[] queenAttacks = calculateQueenMoves(num,black,white,all);
                attackMap |= queenAttacks[1];
                ChessBoard.printBitBoard(attackMap);

            }

            indices = indexSetBits(GameState.bBishops);
            for(Integer num: indices){
                long[] bishopAttacks = calculateBishopMoves(num,black,white,all);
                attackMap |= bishopAttacks[1];
                ChessBoard.printBitBoard(attackMap);

            }



            return attackMap;


        }
        else{
            long attackMap =0L;

            ArrayList<Integer> indices = indexSetBits(GameState.wPawns);

            for(Integer num: indices){
                long[] pawnAttacks = calculateWhitePawnMoves(num,white,black,all);
                attackMap |= pawnAttacks[1];
            }

            indices = indexSetBits(GameState.wRooks);
            for(Integer num: indices){
                long[] rookAttacks = calculateRookMoves(num,white,black,all);
                attackMap |= rookAttacks[1];
            }


            indices = indexSetBits(GameState.wKnights);
            for(Integer num: indices){
                long[] knightAttacks = calculateKnightMoves(num,white,black,all);
                attackMap |= knightAttacks[1];
            }


            indices = indexSetBits(GameState.wKing);
            for(Integer num: indices){
                long[] kingAttacks = calculateKingMoves(num,white,black,all);
                attackMap |= kingAttacks[1];
            }

            indices = indexSetBits(GameState.wQueens);

            for(Integer num: indices){
                long[] queenAttacks = calculateQueenMoves(num,white,black,all);

                attackMap |= queenAttacks[1];


            }

            indices = indexSetBits(GameState.wBishops);
            for(Integer num: indices){
                long[] bishopAttacks = calculateBishopMoves(num,white,black,all);


                attackMap |= bishopAttacks[1];
            }

            return attackMap;

        }

    }

    public static int evaluatePos(long[] bitboards, long sideToMove){

        //pawns worth 1, knights/bishops worth 3, rook worth 5, queen worth 9, checkmate worth 10000


        int evaluationWhite = 0;
        int evaluationBlack = 0;



        //simple counter based only on assigned value for each piece.

        //black pieces
        for(int i = 0; i<6;i++){
            int numP = indexSetBits(bitboards[i]).size();



            switch(i){
                case(0): evaluationBlack += numP;
                break;

                case(1): evaluationBlack += numP * 5;
                break;

                case(2): evaluationBlack += numP * 3;
                break;

                case(3): evaluationBlack += numP;
                break;

                case(4): evaluationBlack += numP * 9;
                break;

                case(5): evaluationBlack += numP * 3;
                break;
            }

            //System.out.println(evaluationBlack);
        }

        //white pieces
        for(int j = 6; j<12;j++){

            int numP = indexSetBits(bitboards[j]).size();

            switch(j){
                case(6): evaluationWhite += numP;
                break;

                case(7): evaluationWhite += numP * 5;
                break;

                case(8): evaluationWhite += numP * 3;
                break;

                case(9): evaluationWhite += numP;
                break;

                case(10): evaluationWhite += numP * 9;
                break;

                case(11): evaluationWhite += numP * 3;
                break;
            }



        }



        //check to see if knights are on a or h files, which is punished.

        evaluationWhite -= indexSetBits(bitboards[8] & (Lookups.fileTables[Lookups.fileTables.length-1] & (Lookups.fileTables[Lookups.fileTables.length-1]<<7))).size();
        evaluationBlack -= indexSetBits(bitboards[2] & (Lookups.fileTables[Lookups.fileTables.length-1] & (Lookups.fileTables[Lookups.fileTables.length-1]<<7))).size();



        //check for centralized/ developed pawns.
        evaluationWhite += indexSetBits(bitboards[6] & Lookups.evalTables[0]).size();
        evaluationBlack += indexSetBits(bitboards[0] & Lookups.evalTables[0]).size();







        return evaluationBlack -evaluationWhite;
    }



    public static ArrayList<Integer> indexSetBits(Long bitboard){

        ArrayList<Integer> indices = new ArrayList<>();

        String bits = padZeros(bitboard);

        int count =63;

        for(int i = 0; i<bits.length();i++){
            if(bits.charAt(i) == '1'){
                indices.add(count);
            }
            count--;
        }


        return indices;
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
    public  static String padZeros(long bitBoard){

        String full = "";
        //String square = rank *8 + file;
        String s = Long.toBinaryString(bitBoard);

        //System.out.println(64 - Long.toBinaryString(pieceBoard).length());

        if (Long.toBinaryString(bitBoard).length() == 64) {
            full = Long.toBinaryString(bitBoard);
            s = full;
        } else {

            full = String.format("%0" + (64 - Long.toBinaryString(bitBoard).length()) + 'd', 0);
            s = full + "" + s;

        }
        return s;
    }



}