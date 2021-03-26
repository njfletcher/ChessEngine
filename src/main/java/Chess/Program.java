package Chess;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class Program {



    public static void main(String[] args) throws InterruptedException {

        //Instant starts = Instant.now();


        ChessBoard board = new ChessBoard();
        FenParser parser = new FenParser();

        parser.fenToBitboards("K7/8/8/7k/7r/8/8/7Q w - - 0 1");


        long[] pieces = GameState.generatePiecesArray();


        ArrayList<Move> moves = generateBlackMoves(pieces);

        for(Move m: moves){

            System.out.println(ChessBoard.evaluatePos(m.bitboardCopys,0L));
        }




        //Instant ends = Instant.now();

        //System.out.println(Duration.between(starts, ends).toMillis());




    }
    public enum Pieces {

        //lowercase for black pieces, upper for white.

        p,r,n,k,q,b,P,R,N,K,Q,B
    }
    public static ArrayList<Move> generateBlackMoves(long[] pieces) {
        ArrayList<Move> legalMoves = new ArrayList<Move>();



        ChessBoard board = new ChessBoard();

        long[] sums = GameState.updatePiecesSum();
        long bPieces = sums[0];
        long wPieces = sums[1];
        long allPieces = sums[2];



        for (int i = 0; i < 6; i++) {

            ArrayList<Integer> indices = ChessBoard.indexSetBits(pieces[i]);

            //ChessBoard.printBitBoard(pieces[i]);

            //System.out.println(indices.toString());

            long[] moves = new long[2];

            for (Integer num : indices) {



                    if(i ==0) {
                        moves = board.calculateBlackPawnMoves(num, bPieces, wPieces, allPieces);
                    }

                    if(i ==1) {
                        moves = board.calculateRookMoves(num, bPieces, wPieces, allPieces);

                    }
                    if(i ==2) {
                        moves = board.calculateKnightMoves(num, bPieces, wPieces, allPieces);

                    }
                    if(i ==3) {
                        moves = board.calculateKingMoves(num, bPieces, wPieces, allPieces);
                    }

                    if(i ==4) {
                        moves = board.calculateQueenMoves(num, bPieces, wPieces, allPieces);
                    }

                    if(i ==5) {

                    moves = board.calculateBishopMoves(num, bPieces, wPieces, allPieces);
                    }




                ArrayList<Integer> moveIndices = ChessBoard.indexSetBits(moves[0]);


                ArrayList<Integer> attackIndices = ChessBoard.indexSetBits(moves[1]);

                for (Integer bit : moveIndices) {

                    //make move
                    pieces[i] &= ~(1L << num);
                    pieces[i]|= (1L << bit);

                    long black = 0L;
                    long white = 0L;
                    long all;

                    for(int h = 0; h<6;h++){
                        black |= pieces[h];
                    }
                    for(int f= 6; f<12;f++){
                        white |= pieces[f];
                    }

                    all = white | black;



                    
                    //check for check after making hypothetical move
                    if ((board.checkForCheck(pieces[3], board.generateSideAttackMask(0L, black,white,all)))==false) {
                        legalMoves.add(new Move(num, bit, pieces,i));

                    }
                    
                    //undo move
                    pieces[i] &= ~(1L << bit);
                    pieces[i] |= (1L << num);

                }
                for (Integer bit : attackIndices) {


                    pieces[i] &= ~(1L << num);
                    pieces[i] |= (1L << bit);




                    int index = 0;
                    for (int j = 6; j < 12; j++) {
                        if ((pieces[j] & (1L << bit)) != 0) {
                            pieces[j] &= ~(1L << bit);
                            index = j;

                        }
                    }

                    long black = 0L;
                    long white = 0L;
                    long all;

                    for(int h = 0; h<6;h++){
                        black |= pieces[i];
                    }
                    for(int f= 6; f<12;f++){
                        white |= pieces[i];
                    }

                    all = white | black;
                    if ((board.checkForCheck(GameState.bKing, board.generateSideAttackMask(0L,black,white,all)) == false)) {
                        legalMoves.add(new Move(num, bit, pieces,i));
                    }
                    pieces[i] &= ~(1L << bit);
                    pieces[i]|= (1L << num);

                    pieces[index] |= (1L << bit);


                }
            }
        }
        return legalMoves;
    }
    public static ArrayList<Move> generateWhiteMoves(long[] pieces) {
        ArrayList<Move> legalMoves = new ArrayList<Move>();



        ChessBoard board = new ChessBoard();

        long[] sums = GameState.updatePiecesSum();
        long bPieces = sums[0];
        long wPieces = sums[1];
        long allPieces = sums[2];



        for (int i = 6; i < 12; i++) {




            ArrayList<Integer> indices = ChessBoard.indexSetBits(pieces[i]);



            long[] moves = new long[2];

            for (Integer num : indices) {

                if(i ==0) {
                    moves = board.calculateWhitePawnMoves(num, wPieces, bPieces, allPieces);
                }

                if(i ==1) {
                    moves = board.calculateRookMoves(num, wPieces, bPieces, allPieces);

                }
                if(i ==2) {
                    moves = board.calculateKnightMoves(num, wPieces, bPieces, allPieces);

                }
                if(i ==3) {
                    moves = board.calculateKingMoves(num, wPieces, bPieces, allPieces);
                }

                if(i ==4) {
                    moves = board.calculateQueenMoves(num, wPieces, bPieces, allPieces);
                }

                if(i ==5) {

                    moves = board.calculateBishopMoves(num, wPieces, bPieces, allPieces);
                }




                ArrayList<Integer> moveIndices = ChessBoard.indexSetBits(moves[0]<<0);
                ArrayList<Integer> attackIndices = ChessBoard.indexSetBits(moves[1]<<0);

                for (Integer bit : moveIndices) {

                    //make move
                    pieces[i] &= ~(1L << num);
                    pieces[i]|= (1L << bit);

                    long black = 0L;
                    long white = 0L;
                    long all;

                    for(int h = 0; h<6;h++){
                        black |= pieces[i];
                    }
                    for(int f= 6; f<12;f++){
                        white |= pieces[i];
                    }

                    all = white | black;



                    //check for check after making hypothetical move
                    if (!(board.checkForCheck(pieces[9], board.generateSideAttackMask(1L,black,white,all)))) {
                        legalMoves.add(new Move(num, bit, pieces,i));
                    }

                    //undo move
                    pieces[i] &= ~(1L << bit);
                    pieces[i] |= (1L << num);

                }
                for (Integer bit : attackIndices) {


                    pieces[i] &= ~(1L << num);
                    pieces[i] |= (1L << bit);

                    long[] piecesNew = GameState.generatePiecesArray();


                    int index = 0;
                    for (int j = 6; j < 12; j++) {
                        if ((pieces[j] & (1L << bit)) != 0) {
                            pieces[j] &= ~(1L << bit);
                            index = j;

                        }
                    }

                    long black = 0L;
                    long white = 0L;
                    long all;

                    for(int h = 0; h<6;h++){
                        black |= pieces[i];
                    }
                    for(int f= 6; f<12;f++){
                        white |= pieces[i];
                    }

                    all = white | black;

                    if (!(board.checkForCheck(pieces[9], board.generateSideAttackMask(0L,black,white,all)))) {
                        legalMoves.add(new Move(num, bit, pieces,i));
                    }
                    pieces[i] &= ~(1L << bit);
                    pieces[i]|= (1L << num);

                    pieces[index] |= (1L << bit);


                }
            }
        }
        return legalMoves;
    }

}
