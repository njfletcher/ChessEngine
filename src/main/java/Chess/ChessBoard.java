package Chess;

import java.util.Arrays;

public class ChessBoard {

    public ChessBoard() {


    }

    public void gameStart() {
        //representation(StartPos.bKing);
    }


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

         representation(kingReals);


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

        long spot1Clip = lookuptables[0] & lookuptables[1] & knightLocation;
        long spot2Clip = lookuptables[0] & knightLocation;
        long spot3Clip = lookuptables[lookuptables.length-2] & knightLocation;
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

        representation(knightLegals);


    }


    public void calculateWhitePawnMoves(long pawnLocation, long ownSideBitboard, long blackPieces,long allPieces, long[] lookuptables){

            long oneStep = pawnLocation << 8 & ~allPieces;
            long twoSteps = ((oneStep & (StartPos.rankTables[0]<<16))<<8) & ~allPieces;

            long legalMoves = oneStep | twoSteps;

            long pawnEastAttack = (pawnLocation & StartPos.fileTables[0])<<7;
            long pawnWestAttack = (pawnLocation & StartPos.fileTables[StartPos.fileTables.length-2])<<9;

            long pawnAttacks = pawnEastAttack | pawnWestAttack;
            long pawnLegalAttacks = pawnAttacks & blackPieces;

            long whiteValidGeneral = pawnLegalAttacks | legalMoves;

            representation(whiteValidGeneral);



    }
    public void calculateBlackPawnMoves(long pawnLocation, long ownSideBitboard, long WhitePieces,long allPieces, long[] lookuptables){

        long oneStep = pawnLocation >> 8 & ~allPieces;
        long twoSteps = ((oneStep & (StartPos.rankTables[0]<<40))>>8) & ~allPieces;

        long legalMoves = oneStep | twoSteps;

        long pawnEastAttack = (pawnLocation & StartPos.fileTables[0])>>9;
        long pawnWestAttack = (pawnLocation & StartPos.fileTables[StartPos.fileTables.length-2])>>7;

        long pawnAttacks = pawnEastAttack | pawnWestAttack;
        long pawnLegalAttacks = pawnAttacks & WhitePieces;

        long blackValidGeneral = pawnLegalAttacks | legalMoves;

        representation(blackValidGeneral);


    }


    public void representation(Long pieceBoard) {

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