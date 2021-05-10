package Chess;



import java.io.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;



public class Program {

    public static void main(String[] args) throws Exception {


        ChessBoard board = new ChessBoard();
        FenParser parser = new FenParser();


        InputStream is =  Lichess.sendRequest("GET","/api/stream/event");

        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(is));
        String line = null;
        while ((line = reader.readLine()) != null) {


            if(line.length() != 0) {

                System.out.println(line);

                Gson gson = new Gson();

                JsonObject coderollsJsonObject = gson.fromJson(line, JsonObject.class);

               //get type of event
                String s = coderollsJsonObject.get("type").toString();





                if(s.equals("\"challenge\"")){


                    //get challenge(game) id
                    String id = coderollsJsonObject.get("challenge").getAsJsonObject().get("id").getAsString();

                    //get name of opponent
                    String challengerID = coderollsJsonObject.get("challenge").getAsJsonObject().get("challenger").getAsJsonObject().get("id").toString();

                    //accept
                    Lichess.sendPOSTRequest("/api/challenge/" + id + "/accept","",null);

                    //start game
                    playGame(challengerID,id);

                }





            }

        }
        reader.close();















    }

    public static void playGame(String oppID, String challengeID) throws IOException {

        FenParser parser = new FenParser();
        Gson gson = new Gson();

        InputStream is = Lichess.sendRequest("GET", "/api/bot/game/stream/" + challengeID);


        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(is));
        String line = reader.readLine();

        System.out.println(line);

        JsonObject coderollsJsonObject = gson.fromJson(line, JsonObject.class);

        String whiteId = coderollsJsonObject.get("white").getAsJsonObject().get("id").toString();

        String fen = coderollsJsonObject.get("initialFen").toString().replaceAll("\"","");


        if(fen.equals("startpos")){
            parser.fenToBitboards("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        }
        else{
            parser.fenToBitboards(fen);
        }

        //checks what color the bot is playing for this game
        GameState.botSide = whiteId.equals("\"goosefish\"") ? 1 : -1;


        int count =0;
        String line1 = null;

        boolean firstMove = true;

        //even describes what moves the bot should translate.
        //if bot is first move, it should translate every second(aka even)move
        boolean even = false;
        if(GameState.botSide == GameState.sideToMove){
            even = true;

        }

        System.out.println(even + " ");

        while ((line1 = reader.readLine())!= null) {

            if(line1.length() != 0) {

                JsonObject json = gson.fromJson(line1, JsonObject.class);

                if (json.get("type").toString().equals("\"gameState\"")) {

                    JsonObject jsonRead = gson.fromJson(line1, JsonObject.class);

                    String gameLine = jsonRead.get("moves").toString().replaceAll("\"", "");

                    String[] moveArr = gameLine.split(" ");

                    if ((moveArr.length % 2 == 0) == even) {




                        System.out.print("Your Turn");


                        //making sure that the incoming events are moves not chats.


                        System.out.println(line1);

                        String[] moves = json.get("moves").toString().split(" ");

                        System.out.println(moves[moves.length - 1].replaceAll("\"", ""));

                        long[] movePieces1 = GameState.generatePiecesArray();

                        for(int i =0; i<12;i++){
                            System.out.println(i);

                            ChessBoard.printBitBoard(movePieces1[i]);
                        }


                        Lichess.recieveIncoming(moves[moves.length - 1].replaceAll("\"", ""));

                        long[] movePieces2 = GameState.generatePiecesArray();

                        for(int i =0; i<12;i++){
                            System.out.println(i);

                            ChessBoard.printBitBoard(movePieces2[i]);
                        }


                        GameState.sideToMove *= -1;

                    }

                }
            }
            if(GameState.botSide== GameState.sideToMove){


                System.out.print("My Turn");
                long[] movePieces = GameState.generatePiecesArray();


                for(int i =0; i<12;i++){
                    System.out.println(i);

                    ChessBoard.printBitBoard(movePieces[i]);
                }


                long castle = GameState.castleRights;
                int enPassant = GameState.enPassant;

                int sideTurn = GameState.sideToMove;



                boolean isMaximizer = (GameState.botSide == 1) ? true : false;





                Move move = miniMax(movePieces, castle, enPassant, 4, sideTurn, isMaximizer, Integer.MIN_VALUE, Integer.MAX_VALUE).first();



                System.out.println(move);

                GameState.statePieces = move.bitboardCopys;

                GameState.castleRights = move.castleRightsCopy;

                GameState.enPassant = move.enPassSquare;

                GameState.updateIndivBoards();

                String finalMove = Lichess.translateMove(move);

                System.out.println(finalMove);

                Map<String, String> dic = new HashMap<String, String>();

                dic.put("offeringDraw", "false");

                Lichess.sendPOSTRequest("/api/bot/game/" + challengeID + "/move/" + finalMove + "", "", dic);

                GameState.sideToMove *= -1;


                long[] movePieces1 = GameState.generatePiecesArray();

                for(int i =0; i<12;i++){
                    System.out.println(i);

                    ChessBoard.printBitBoard(movePieces1[i]);
                }

            }




        }

    }















    public static MovePair miniMax(long[] pieces,long castleRights,int enPassSquare, int depth, int side, boolean isMaxPlayer, int alpha, int beta) {

        if (depth == 0 || (ChessBoard.checkForCheckmate(pieces,castleRights,enPassSquare,side)== true)|| (ChessBoard.checkForStalemate(pieces,castleRights,enPassSquare,side)== true)) {
            //| (ChessBoard.checkForCheckmate(pieces,castleRights,enPassSquare,side)== true)| (ChessBoard.checkForStalemate(pieces,castleRights,enPassSquare,side)== true)
            return new MovePair(null,ChessBoard.evaluatePos(pieces,castleRights,enPassSquare,depth));
        }

        Move bestMove= null;
        ArrayList<Move> possibleMoves = getCurrentPlayerMoves(pieces, side,castleRights,enPassSquare);

        sortMoves(possibleMoves,depth);

        int bestVal = isMaxPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for(Move m: possibleMoves){

            long[] copy = new long[12];




            for(int l = 0; l < 12; l++){
                copy[l] = m.bitboardCopys[l];
            }



            int currValue = miniMax(copy,m.castleRightsCopy,m.enPassSquare,depth-1,-1* side,!isMaxPlayer,alpha,beta).second();

            if(isMaxPlayer){

                if(currValue>bestVal){
                    bestVal = currValue;
                    bestMove = m;
                }
                alpha = Math.max(alpha, currValue);
            }
            else{
                if(currValue<bestVal){
                    bestVal = currValue;
                    bestMove = m;
                }
                beta = Math.min(beta, currValue);
            }

            if(beta <= alpha) {

                break;
            }

        }




        return new MovePair(bestMove,bestVal);


   }

   public static void  sortMoves(ArrayList<Move> moves, int depth){


       /* for(Move m : moves){

            m.evalScore = ChessBoard.evaluatePos(m.bitboardCopys,m.castleRightsCopy,m.enPassSquare,depth);
        }

        */

        moves.sort(new MoveSorter());



   }

   public static ArrayList<Move> getCurrentPlayerMoves(long[] pieces,long sideMove,long castleRights,int enPassSquare){

        if(sideMove == 1){
            return generateWhiteMoves(pieces,castleRights,enPassSquare);
        }
        else{
            return generateBlackMoves(pieces,castleRights,enPassSquare);
        }
    }


    //TO DO: MAKE CODE CLEANER(ESP for loops)
    //Do array copy process with white move gen


    public static ArrayList<Move> generateBlackMoves(long[] pieces,long castleRights,int enPassSquare) {
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


                //loops through each standard move square
                for (Integer bit : moveIndices) {



                    //make move
                    pieces[i] &= ~(1L << num);
                    pieces[i]|= (1L << bit);

                    long[] teamCopies = generateTeamLongs(pieces);

                    long[] copy = new long[12];

                    int enPassTarget = enPassSquare;

                    long castleRightsCopy = castleRights;

                    for(int l = 0; l < 12; l++){
                        copy[l] = pieces[l];
                    }

                    //Change castle rights if the rook is being moved, depending on what rook is being moved.
                    if(i==1){
                        if(num == 7){
                            castleRightsCopy &= ~(1L<<1);
                        }
                        if(num ==0){
                            castleRightsCopy &= ~(1L);
                        }
                    }

                    //nulls both castle rights of a side if king moves
                    if(i==3){
                        castleRightsCopy &= ~(1L);

                        castleRightsCopy &= ~(1L<<1);
                    }


                    if(i ==0 & (bit == num-16)){

                        enPassTarget = num -8;

                    }
                    else{
                        enPassTarget = 64;
                    }

                    //promotion check
                    if(i==0 & bit<8){

                        pieces[i] &= ~(1L << bit);

                        //1,2,4,5
                        for(int promotionPieceType: new int[] {1,2,4,5}){

                            pieces[promotionPieceType] |= (1L << bit);

                            long[] copyPromotion = new long[12];

                            enPassTarget = 64;

                            long[] teamCopiesPromotion = generateTeamLongs(pieces);
                            for(int l = 0; l < 12; l++){
                                copyPromotion[l] = pieces[l];
                            }

                            if ((board.checkForCheck(pieces[3], board.generateSideAttackMask(pieces,-1, teamCopiesPromotion[0], teamCopiesPromotion[1], teamCopiesPromotion[2]))) ==false) {
                                legalMoves.add(new Move(num, bit,promotionPieceType, copyPromotion,castleRightsCopy,enPassTarget,i));

                            }

                            pieces[promotionPieceType] &= ~(1L << bit);

                        }

                        pieces[i] |= (1L << bit);

                        pieces[i] &= ~(1L << bit);
                        pieces[i] |= (1L << num);

                        break;
                    }



                    //check for check after making hypothetical move
                    if ((board.checkForCheck(pieces[3], board.generateSideAttackMask(pieces,-1, teamCopies[0], teamCopies[1], teamCopies[2]))) ==false) {
                        legalMoves.add(new Move(num, bit, copy,i,castleRightsCopy,enPassTarget));

                    }

                    
                    //undo move
                    pieces[i] &= ~(1L << bit);
                    pieces[i] |= (1L << num);

                }

                //loops through each attack target square.
                for (Integer bit : attackIndices) {


                    pieces[i] &= ~(1L << num);
                    pieces[i] |= (1L << bit);

                    int enPassTarget = enPassSquare;




                    int index = 0;
                    for (int j = 6; j < 12; j++) {
                        if ((pieces[j] & (1L << bit)) != 0) {
                            pieces[j] &= ~(1L << bit);
                            index = j;

                        }
                    }

                    long[] teamCopies = generateTeamLongs(pieces);


                    long[] copy = new long[12];

                    enPassTarget = 64;

                    long castleRightsCopy = castleRights;

                    //if the white rook is being attacked, white's castle rights.
                    if(index == 7 & bit == 63){
                        castleRightsCopy &= ~(1L<<3);
                    }
                    if(index == 7 & bit == 56){
                        castleRightsCopy &= ~(1L<<2);
                    }



                    for(int l = 0; l < 12; l++){
                        copy[l] = pieces[l];
                    }

                    //promotion + attack check
                    if(i==0 & bit<8){

                        pieces[i] &= ~(1L << bit);

                        //1,2,4,5
                        for(int promotionPieceType: new int[] {1,2,4,5}){

                            pieces[promotionPieceType] |= (1L << bit);

                            long[] copyPromotion = new long[12];



                            long[] teamCopiesPromotion = generateTeamLongs(pieces);
                            for(int l = 0; l < 12; l++){
                                copyPromotion[l] = pieces[l];
                            }

                            if ((board.checkForCheck(pieces[3], board.generateSideAttackMask(pieces,-1, teamCopiesPromotion[0], teamCopiesPromotion[1], teamCopiesPromotion[2]))) ==false) {
                                legalMoves.add(new Move(num, bit,index,promotionPieceType, copyPromotion,castleRightsCopy,enPassTarget,i));

                            }

                            pieces[promotionPieceType] &= ~(1L << bit);

                        }

                        pieces[i] |= (1L << bit);

                        pieces[i] &= ~(1L << bit);
                        pieces[i] |= (1L << num);
                        pieces[index] |= (1L << bit);

                        break;
                    }


                    if ((board.checkForCheck(pieces[3], board.generateSideAttackMask(pieces,-1,teamCopies[0],teamCopies[1],teamCopies[2]))) == false) {
                        legalMoves.add(new Move(num, bit, copy,i,index,castleRightsCopy,enPassTarget));
                    }

                    pieces[i] &= ~(1L << bit);
                    pieces[i]|= (1L << num);

                    pieces[index] |= (1L << bit);


                }

                if(i==1){
                    //castle black checks.
                    int enPassTarget = enPassSquare;
                    enPassTarget = 64;

                    long[] teamCopies = generateTeamLongs(pieces);

                    //attack map check should be per side, not one thing.
                    if(!(((board.generateSideAttackMask(pieces,-1,teamCopies[0],teamCopies[1],teamCopies[2]) & Lookups.castleTables[3]) != 0) | ((teamCopies[2] & Lookups.castleTables[3])) != 0 )) {
                        //queenside black castle
                        if ((castleRights & 0b1L) != 0) {

                            pieces[3] &= ~(1L << 60);
                            pieces[3] |= (1L << 58);

                            pieces[1] &= ~(1L << 56);
                            pieces[1] |= (1L << 59);


                            long[] teamCopiesCastle = generateTeamLongs(pieces);

                            long castleRightsCopy = castleRights;



                            castleRightsCopy &= ~(1L);

                            castleRightsCopy &= ~(1L<<1);

                            long[] copy1 = new long[12];

                            for (int l = 0; l < 12; l++) {
                                copy1[l] = pieces[l];
                            }

                            if ((board.checkForCheck(pieces[3], board.generateSideAttackMask(pieces, -1, teamCopiesCastle[0], teamCopiesCastle[1], teamCopiesCastle[2]))) == false) {
                                legalMoves.add(new Move(copy1, castleRightsCopy,enPassTarget));
                            }

                            pieces[3] &= ~(1L << 58);
                            pieces[3] |= (1L << 60);

                            pieces[1] &= ~(1L << 59);
                            pieces[1] |= (1L << 56);

                        }
                    }
                    if(!(((board.generateSideAttackMask(pieces,-1,teamCopies[0],teamCopies[1],teamCopies[2]) & Lookups.castleTables[2]) != 0) | ((teamCopies[2] & Lookups.castleTables[2])) != 0 )){
                        //kingside black castle
                        if((castleRights & 0b10L) != 0){

                            pieces[3] &= ~(1L << 60);
                            pieces[3]|= (1L <<62);

                            pieces[1] &= ~(1L << 63);
                            pieces[1]|= (1L <<61);


                            long[] teamCopiesCastle = generateTeamLongs(pieces);

                            long castleRightsCopy = castleRights;

                            castleRightsCopy &= ~(1L<<1);
                            castleRightsCopy &= ~(1L);

                            long[] copy1 = new long[12];

                            for(int l = 0; l < 12; l++){
                                copy1[l] = pieces[l];
                            }

                            if ((board.checkForCheck(pieces[3], board.generateSideAttackMask(pieces,-1,teamCopiesCastle[0],teamCopiesCastle[1],teamCopiesCastle[2]))) == false) {
                                legalMoves.add(new Move(copy1,castleRightsCopy,enPassTarget));
                            }

                            pieces[3] &= ~(1L <<62);
                            pieces[3]|= (1L << 60);

                            pieces[1] &= ~(1L << 61);
                            pieces[1]|= (1L <<63);
                        }

                    }

                }

                //black  enPassant move check
                if(i==0 &(enPassSquare != 64)){

                    for(Integer possEnPass: indices){
                        if((possEnPass == enPassSquare+ 7) |(possEnPass == enPassSquare+ 9) ){

                            pieces[0] &= ~(1L << possEnPass);
                            pieces[0] |= (1L << enPassSquare);

                            pieces[6] &= ~(1L << enPassSquare+8);

                            long[] teamCopiesPass = generateTeamLongs(pieces);

                            long[] copy1 = new long[12];

                            for(int l = 0; l < 12; l++){
                                copy1[l] = pieces[l];
                            }

                            int enPassTarget = enPassSquare;
                            enPassTarget = 64;

                            if ((board.checkForCheck(pieces[3], board.generateSideAttackMask(pieces,-1,teamCopiesPass[0],teamCopiesPass[1],teamCopiesPass[2]))) == false) {
                                legalMoves.add(new Move(possEnPass,enPassSquare,6,copy1,0,castleRights,enPassTarget));
                            }

                            pieces[0] |= (1L << possEnPass);
                            pieces[0] &= ~(1L << enPassSquare);

                            pieces[6] |= (1L << enPassSquare+8);

                        }
                    }


                }
            }
        }
        return legalMoves;
    }

    public static ArrayList<Move> generateWhiteMoves(long[] pieces, long castleRights,int enPassSquare) {
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


                //loop through each standard move target square.
                for (Integer bit : moveIndices) {



                    //make move
                    pieces[i] &= ~(1L << num);
                    pieces[i]|= (1L << bit);

                    long[] teamCopies = generateTeamLongs(pieces);
                    int enPassTarget = enPassSquare;

                    long[] copy = new long[12];


                    long castleRightsCopy = castleRights;

                    for(int l = 0; l < 12; l++){
                        copy[l] = pieces[l];
                    }


                    //Change castle rights if the rook is being moved
                    if(i==7){
                        if(num == 63){
                            castleRightsCopy &= ~(1L<<3);
                        }
                        if(num ==56){
                            castleRightsCopy &= ~(1L<<2);
                        }
                    }


                    //nulls both castle rights of a side if king moves
                    if(i==9){
                        castleRights &= ~(1L<<3);

                        castleRights &= ~(1L<<2);
                    }

                    if(i ==6 & (bit == num+16)){

                        enPassTarget = num +8;

                    }
                    else{
                        enPassTarget = 64;
                    }

                    //promotion check
                    if(i==6 & bit>56){

                        pieces[i] &= ~(1L << bit);

                        for(int promotionPieceType: new int[] {7,8,10,11}){

                            pieces[promotionPieceType] |= (1L << bit);

                            long[] copyPromotion = new long[12];

                            enPassTarget = 64;

                            long[] teamCopiesPromotion = generateTeamLongs(pieces);
                            for(int l = 0; l < 12; l++){
                                copyPromotion[l] = pieces[l];
                            }

                            if ((board.checkForCheck(pieces[9], board.generateSideAttackMask(pieces,1, teamCopiesPromotion[0], teamCopiesPromotion[1], teamCopiesPromotion[2]))) ==false) {
                                legalMoves.add(new Move(num, bit,promotionPieceType, copyPromotion,castleRightsCopy,enPassTarget,i));

                            }

                            pieces[promotionPieceType] &= ~(1L << bit);

                        }

                        pieces[i] |= (1L << bit);

                        pieces[i] &= ~(1L << bit);
                        pieces[i] |= (1L << num);

                        break;
                    }


                    //check for check after making hypothetical move
                    if (!(board.checkForCheck(pieces[9], board.generateSideAttackMask(pieces,1,teamCopies[0],teamCopies[1],teamCopies[2])))) {
                        legalMoves.add(new Move(num, bit, copy,i,castleRightsCopy,enPassTarget));
                    }

                    //undo move
                    pieces[i] &= ~(1L << bit);
                    pieces[i] |= (1L << num);

                }

                //loops through each attack target square
                for (Integer bit : attackIndices) {


                    pieces[i] &= ~(1L << num);
                    pieces[i] |= (1L << bit);


                    int enPassTarget = enPassSquare;
                    enPassTarget = 64;


                    int index = 0;
                    for (int j = 0; j < 6; j++) {
                        if ((pieces[j] & (1L << bit)) != 0) {
                            pieces[j] &= ~(1L << bit);
                            index = j;

                        }
                    }

                    long[] teamCopies = generateTeamLongs(pieces);

                    long castleRightsCopy = castleRights;

                    if(index == 1 & bit == 7){
                        castleRightsCopy &= ~(1L<<1);
                    }
                    if(index == 1 & bit == 0){
                        castleRightsCopy &= ~(1L);
                    }



                    long[] copy = new long[12];

                    for(int l = 0; l < 12; l++){
                        copy[l] = pieces[l];
                    }

                    //promotion + attack check
                    if(i==6 & bit>56){

                        pieces[i] &= ~(1L << bit);


                        for(int promotionPieceType: new int[] {7,8,10,11}){

                            pieces[promotionPieceType] |= (1L << bit);

                            long[] copyPromotion = new long[12];

                            long[] teamCopiesPromotion = generateTeamLongs(pieces);
                            for(int l = 0; l < 12; l++){
                                copyPromotion[l] = pieces[l];
                            }

                            if ((board.checkForCheck(pieces[3], board.generateSideAttackMask(pieces,1, teamCopiesPromotion[0], teamCopiesPromotion[1], teamCopiesPromotion[2]))) ==false) {
                                legalMoves.add(new Move(num, bit,index,promotionPieceType, copyPromotion,castleRightsCopy,enPassTarget,i));

                            }

                            pieces[promotionPieceType] &= ~(1L << bit);

                        }

                        pieces[i] |= (1L << bit);

                        pieces[i] &= ~(1L << bit);
                        pieces[i] |= (1L << num);
                        pieces[index] |= (1L << bit);

                        break;
                    }


                    if (!(board.checkForCheck(pieces[9], board.generateSideAttackMask(pieces,1,teamCopies[0],teamCopies[1],teamCopies[2])))) {
                        legalMoves.add(new Move(num, bit, copy,i,index,castleRightsCopy,enPassTarget));
                    }
                    pieces[i] &= ~(1L << bit);
                    pieces[i]|= (1L << num);

                    pieces[index] |= (1L << bit);


                }
                if(i==7){


                    //castle white checks.

                    int enPassTarget = enPassSquare;
                    enPassTarget = 64;
                    long[] teamCopies = generateTeamLongs(pieces);

                    //attack map check should be per side, not one thing.
                    if(!(((board.generateSideAttackMask(pieces,1,teamCopies[0],teamCopies[1],teamCopies[2]) & Lookups.castleTables[0]) != 0) | ((teamCopies[2] & Lookups.castleTables[0])) != 0 )) {
                        //queenside white castle
                        if ((castleRights & 0b100L) != 0) {

                            pieces[9] &= ~(1L << 4);
                            pieces[9] |= (1L << 2);

                            pieces[7] &= ~(1L);
                            pieces[7] |= (1L << 3);


                            long[] teamCopiesCastle = generateTeamLongs(pieces);

                            long castleRightsCopy = castleRights;

                            castleRightsCopy &= ~(1L<<2);
                            castleRightsCopy &= ~(1L<<3);

                            long[] copy1 = new long[12];

                            for (int l = 0; l < 12; l++) {
                                copy1[l] = pieces[l];
                            }

                            if ((board.checkForCheck(pieces[9], board.generateSideAttackMask(pieces, 1, teamCopiesCastle[0], teamCopiesCastle[1], teamCopiesCastle[2]))) == false) {
                                legalMoves.add(new Move(copy1, castleRightsCopy,enPassTarget));
                            }

                            pieces[9] &= ~(1L << 2);
                            pieces[9] |= (1L << 4);

                            pieces[7] &= ~(1L << 3);
                            pieces[7] |= (1L);

                        }
                    }
                    if(!(((board.generateSideAttackMask(pieces,1,teamCopies[0],teamCopies[1],teamCopies[2]) & Lookups.castleTables[1]) != 0) | ((teamCopies[2] & Lookups.castleTables[1])) != 0 )) {
                        //kingside white castle
                        if ((castleRights & 0b1000L) != 0) {

                            pieces[9] &= ~(1L << 4);
                            pieces[9] |= (1L << 6);

                            pieces[7] &= ~(1L << 7);
                            pieces[7] |= (1L << 5);


                            long[] teamCopiesCastle = generateTeamLongs(pieces);

                            long castleRightsCopy = castleRights;

                            castleRightsCopy &= ~(1L<<3);
                            castleRightsCopy &= ~(1L<<2);

                            long[] copy1 = new long[12];

                            for (int l = 0; l < 12; l++) {
                                copy1[l] = pieces[l];
                            }

                            if ((board.checkForCheck(pieces[9], board.generateSideAttackMask(pieces, 1, teamCopiesCastle[0], teamCopiesCastle[1], teamCopiesCastle[2]))) == false) {
                                legalMoves.add(new Move(copy1, castleRightsCopy,enPassTarget));
                            }

                            pieces[9] &= ~(1L << 6);
                            pieces[9] |= (1L << 4);

                            pieces[7] &= ~(1L << 5);
                            pieces[7] |= (1L << 7);
                        }


                    }


                }

                //black  enPassant move check
                if(i==6 &(enPassSquare != 64)){

                    for(Integer possEnPass: indices){
                        if((possEnPass == enPassSquare- 7) |(possEnPass == enPassSquare- 9) ){

                            pieces[6] &= ~(1L << possEnPass);
                            pieces[6] |= (1L << enPassSquare);

                            pieces[0] &= ~(1L << enPassSquare-8);

                            long[] teamCopiesPass = generateTeamLongs(pieces);

                            long[] copy1 = new long[12];

                            for(int l = 0; l < 12; l++){
                                copy1[l] = pieces[l];
                            }

                            int enPassTarget = enPassSquare;
                            enPassTarget = 64;

                            if ((board.checkForCheck(pieces[9], board.generateSideAttackMask(pieces,-1,teamCopiesPass[0],teamCopiesPass[1],teamCopiesPass[2]))) == false) {
                                legalMoves.add(new Move(possEnPass,enPassSquare,0,copy1,6,castleRights,enPassTarget));
                            }

                            pieces[6] |= (1L << possEnPass);
                            pieces[6] &= ~(1L << enPassSquare);

                            pieces[0] |= (1L << enPassSquare-8);

                        }
                    }


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
