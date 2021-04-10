package Chess;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class Program {


    //check generating and being used from before causing problems?????
    //white makes move, black then makes moves off of those moves. It seems black is still obeying white's original attack mask when making the next move.
    //examine ordering of generating check.

    public static void main(String[] args) throws InterruptedException {

        Instant starts = Instant.now();


        ChessBoard board = new ChessBoard();
        FenParser parser = new FenParser();

        parser.fenToBitboards("1B6/8/1K6/2Qr2k1/8/5b2/8/8 w - - 0 1");


        //1B6/8/1K6/2Qr2k1/8/8/8/8 b - - 0 1

        //K7/1q6/8/1q3k2/8/8/8/8 w - - 0 1

        int sideCopy = GameState.sideToMove;
        long castleCopy = GameState.castleRights;

        long[] pieces = GameState.generatePiecesArray();



        System.out.println(miniMax(pieces,castleCopy,4,sideCopy,true).first());

        //black is minimizing player, white is maximizing





        Instant ends = Instant.now();

        System.out.println("TIME TAKEN FOR DEPTH OF 4: " + Duration.between(starts, ends).toMillis() / 1000);



    }

    public static MovePair miniMax(long[] pieces,long castleRights, int depth, int side, boolean isMaxPlayer) {

        if (depth == 0) {
            return new MovePair(null,ChessBoard.evaluatePos(pieces));
        }

        Move bestMove = null;
        ArrayList<Move> possibleMoves = getCurrentPlayerMoves(pieces, side,castleRights);

        int bestVal = 0;
        if (isMaxPlayer) {
            bestVal = Integer.MIN_VALUE;

            for (Move m : possibleMoves) {

                MovePair pair = miniMax(m.bitboardCopys, castleRights,depth - 1, -1 * side, !isMaxPlayer);
                int currVal = pair.second();

                if(currVal>bestVal){
                    bestVal = currVal;
                    bestMove =m;
                }

            }
        }


        if (!isMaxPlayer) {
                bestVal = Integer.MAX_VALUE;

            for (Move m : possibleMoves) {

                MovePair pair = miniMax(m.bitboardCopys, castleRights,depth - 1, -1 * side, !isMaxPlayer);
                int currVal = pair.second();

                if(currVal<bestVal){
                    bestVal = currVal;
                    bestMove =m;
                }

            }

        }





        return new MovePair(bestMove,bestVal);


    }





    public static ArrayList<Move> getCurrentPlayerMoves(long[] pieces,long sideMove,long castleRights){

        if(sideMove == 1){
            return generateWhiteMoves(pieces,castleRights);
        }
        else{
            return generateBlackMoves(pieces,castleRights);
        }
    }


    //TO DO: MAKE CODE CLEANER(ESP for loops)
    //Do array copy process with white move gen


    public static ArrayList<Move> generateBlackMoves(long[] pieces,long castleRights) {
        ArrayList<Move> legalMoves = new ArrayList<Move>();



        ChessBoard board = new ChessBoard();


        //look for errors in this part



        long bPieces= 0L;
        long wPieces = 0L;
        long allPieces;

        for(int h = 0; h<6;h++){
            bPieces |= pieces[h];
        }
        for(int f= 6; f<12;f++){
            wPieces |= pieces[f];
        }

        allPieces = wPieces | bPieces;



        for (int i = 0; i < 6; i++) {

            ArrayList<Integer> indices = ChessBoard.indexSetBits(pieces[i]);

            //ChessBoard.printBitBoard(pieces[i]);

            //System.out.println(indices.toString());

            long[] moves = new long[2];


            //num = each position on board for ith piece type
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

                    long[] copy = new long[12];

                    for(int l = 0; l < 12; l++){
                        copy[l] = pieces[l];
                    }



                    //check for check after making hypothetical move
                    if ((board.checkForCheck(pieces[3], board.generateSideAttackMask(pieces,-1, black, white, all))) ==false) {
                        legalMoves.add(new Move(num, bit, copy,i));

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
                        black |= pieces[h];
                    }
                    for(int f= 6; f<12;f++){
                        white |= pieces[f];
                    }

                    long[] copy = new long[12];

                    for(int l = 0; l < 12; l++){
                        copy[l] = pieces[l];
                    }

                    all = white | black;
                    if ((board.checkForCheck(pieces[3], board.generateSideAttackMask(pieces,-1,black,white,all))) == false) {
                        legalMoves.add(new Move(num, bit, copy,i));
                    }

                    pieces[i] &= ~(1L << bit);
                    pieces[i]|= (1L << num);

                    pieces[index] |= (1L << bit);


                }

                if(i==1){
                    //castle black checks.
                }
            }
        }
        return legalMoves;
    }

    public static ArrayList<Move> generateWhiteMoves(long[] pieces, long castleRights) {
        ArrayList<Move> legalMoves = new ArrayList<Move>();



        ChessBoard board = new ChessBoard();

        long bPieces= 0L;
        long wPieces = 0L;
        long allPieces;

        for(int h = 0; h<6;h++){
            bPieces |= pieces[h];
        }
        for(int f= 6; f<12;f++){
            wPieces |= pieces[f];
        }

        allPieces = wPieces | bPieces;



        for (int i = 6; i < 12; i++) {

            ArrayList<Integer> indices = ChessBoard.indexSetBits(pieces[i]);

            long[] moves = new long[2];

            for (Integer num : indices) {

                if(i ==6) {
                    moves = board.calculateWhitePawnMoves(num, wPieces, bPieces, allPieces);
                }

                if(i ==7) {
                    moves = board.calculateRookMoves(num, wPieces, bPieces, allPieces);

                }
                if(i ==8) {
                    moves = board.calculateKnightMoves(num, wPieces, bPieces, allPieces);

                }
                if(i ==9) {
                    moves = board.calculateKingMoves(num, wPieces, bPieces, allPieces);
                }

                if(i ==10) {
                    moves = board.calculateQueenMoves(num, wPieces, bPieces, allPieces);
                }

                if(i ==11) {

                    moves = board.calculateBishopMoves(num, wPieces, bPieces, allPieces);
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

                    long[] copy = new long[12];

                    for(int l = 0; l < 12; l++){
                        copy[l] = pieces[l];
                    }





                    //check for check after making hypothetical move
                    if (!(board.checkForCheck(pieces[9], board.generateSideAttackMask(pieces,1,black,white,all)))) {
                        legalMoves.add(new Move(num, bit, copy,i));
                    }

                    //undo move
                    pieces[i] &= ~(1L << bit);
                    pieces[i] |= (1L << num);

                }
                for (Integer bit : attackIndices) {


                    pieces[i] &= ~(1L << num);
                    pieces[i] |= (1L << bit);




                    int index = 0;
                    for (int j = 0; j < 6; j++) {
                        if ((pieces[j] & (1L << bit)) != 0) {
                            pieces[j] &= ~(1L << bit);
                            index = j;

                        }
                    }

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

                    long[] copy = new long[12];

                    for(int l = 0; l < 12; l++){
                        copy[l] = pieces[l];
                    }

                    if (!(board.checkForCheck(pieces[9], board.generateSideAttackMask(pieces,1,black,white,all)))) {
                        legalMoves.add(new Move(num, bit, copy,i));
                    }
                    pieces[i] &= ~(1L << bit);
                    pieces[i]|= (1L << num);

                    pieces[index] |= (1L << bit);


                }
                if(i==7){
                    //castle white checks.
                }
            }
        }
        return legalMoves;
    }



    public static long[] generateTeamLongs(long[] pieces){
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

        return new long[] { black, white, all};
    }

   

}
