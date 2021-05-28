package Chess;



import java.util.ArrayList;
import java.util.Arrays;

public class ChessBoard {


    public static long squareToBitboard(String uciPosition) {

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
         long kingPsuedos = Lookups.kingLookups[square];

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



        long knightPsuedos = Lookups.knightLookups[square];
        long knightLegals = knightPsuedos & ~allPieces;
        long knightAttacks = knightPsuedos & enemySide;




        return new long[]{knightLegals,knightAttacks};


    }

    public void initKnightLookups(){


        for(int i =0; i<64;i++){

            long knightLocation = 1L<<i;

            //checking to see if knight is on a or b file
            long spot1Clip = Lookups.fileTables[0] & Lookups.fileTables[1] & knightLocation;
            //checking to see if knight is on a file
            long spot2Clip = Lookups.fileTables[0] & knightLocation;
            //checking to see if knight is on h file
            long spot3Clip = Lookups.fileTables[3] & knightLocation;
            //checking to see if knight is on g or h file
            long spot4Clip = Lookups.fileTables[3] & Lookups.fileTables[2] & knightLocation;


            long spot5Clip = spot4Clip;
            long spot6Clip = spot3Clip;
            long spot7Clip = spot2Clip;
            long spot8Clip = spot1Clip;


            long spot1 = spot1Clip <<6;
            long spot2 = spot2Clip <<15;
            long spot3 = spot3Clip <<17;
            long spot4 = spot4Clip <<10;

            long spot5 = spot5Clip >>> 6;
            long spot6 = spot6Clip >>> 15;
            long spot7 = spot7Clip >>>17;
            long spot8 = spot8Clip >>>10;




            long knightPsuedos = spot1 | spot2 | spot3 | spot4 | spot5 | spot6| spot7 | spot8;

            Lookups.knightLookups[i] = knightPsuedos;

        }
    }


    public long[] calculateWhitePawnMoves(int square, long ownSideBitboard, long blackPieces,long allPieces){


        long legalMoves = Lookups.wPawnLookups[square][0] & ~allPieces;

        if((legalMoves & (1L<< square +8)) ==0){

            legalMoves &= ~(1L<<square + 16);
        }




        long pawnLegalAttacks = Lookups.wPawnLookups[square][1] & blackPieces;

        long whiteValidGeneral = pawnLegalAttacks | legalMoves;

        return new long[]{legalMoves,pawnLegalAttacks};





    }




    public long[] calculateBlackPawnMoves(int square, long ownSideBitboard, long WhitePieces,long allPieces){



        long legalMoves = Lookups.bPawnLookups[square][0] & ~allPieces;


        if((legalMoves & (1L<< square -8)) ==0){

            legalMoves &= ~(1L<<square - 16);
        }

        long pawnLegalAttacks =  Lookups.bPawnLookups[square][1] & WhitePieces;

        long blackValidGeneral = pawnLegalAttacks | legalMoves;

        return new long[]{legalMoves,pawnLegalAttacks};




    }

    public void initPawnLookups(boolean white){

        for(int i =0; i<64;i++) {
            if (white) {

                long pawnLocation = 1L << i;

                long oneStep = pawnLocation << 8;

                //checking if said pawn is on 2 rank
                long twoSteps = ((oneStep & (Lookups.rankTables[0]<<16))<<8);

                long legalMoves = oneStep | twoSteps;

                //checking for if pawn is on left border
                long pawnEastAttack = (pawnLocation & Lookups.fileTables[0])<<7;

                //checking for if pawn is on right border
                long pawnWestAttack = (pawnLocation & Lookups.fileTables[3])<<9;

                long pawnAttacks = pawnEastAttack | pawnWestAttack;

                Lookups.wPawnLookups[i] = new long[]{ legalMoves,pawnAttacks};




            }
            else {
                long pawnLocation = 1L <<i;

                long oneStep = pawnLocation >>> 8;

                //checking for if said pawn is on 7th rank.
                long twoSteps = ((oneStep & (Lookups.rankTables[0]<<40))>>>8);

                long legalMoves = oneStep | twoSteps;

                //checking for if pawn is on left border
                long pawnEastAttack = (pawnLocation & Lookups.fileTables[0])>>>9;
                //checking for if pawn if on right border
                long pawnWestAttack = (pawnLocation & Lookups.fileTables[3])>>>7;

                long pawnAttacks = pawnEastAttack | pawnWestAttack;


                Lookups.bPawnLookups[i] = new long[]{ legalMoves,pawnAttacks};



            }
        }
    }


    public long[] calculateBishopMoves(int square, long ownSide, long oppositeSide, long allPieces){

        long copy = allPieces;
        allPieces &= Lookups.bishopMasks[square];

        allPieces *= Lookups.bishopMagics[square];

        allPieces >>>= 64 - Lookups.bishopIndexBits[square];

        long moves = Lookups.bishopMagicAttacks[square][(int)allPieces];

        return new long[]{moves & ~copy,moves  & oppositeSide};


    }

      /*public long[] calculateBishopMoves(int square, long ownSide, long oppositePieces, long allPieces){


        long moves = calcDiagonal(square,allPieces);

        return new long[]{moves & ~allPieces,(moves & ~ownSide) & oppositePieces};



    }

     */

    public long[] calculateRookMoves(int square, long ownSide, long oppoSide, long allPieces){

        long copy = allPieces;
        allPieces &= Lookups.rookMasks[square];

        allPieces *= Lookups.rookMagics[square];

        allPieces >>>= 64 - Lookups.rookIndexBits[square];

        long moves= Lookups.rookMagicAttacks[square][(int)allPieces];






        return new long[]{moves & ~copy,moves  & oppoSide};
    }

      /*public long[] calculateRookMoves(int square, long ownSideBitboard, long oppositePieces, long allPieces){

        long moves= calcCross(square, allPieces);

        return new long[]{moves & ~allPieces,(moves & ~ownSideBitboard) & oppositePieces};
    }

     */

    public long[] calculateQueenMoves(int square, long ownSide, long oppoSide, long allPieces){

        long[] diags = calculateBishopMoves(square,ownSide, oppoSide,allPieces);

        long[] cross = calculateRookMoves(square,ownSide, oppoSide,allPieces);


        return new long[]{diags[0] | cross[0],diags[1] | cross[1]};


    }
    /* public long[] calculateQueenMoves(int square, long ownSideBitBoard, long oppositeSidePieces, long allPieces ){

        long crosses = calcCross(square,allPieces);

        long diagonals = calcDiagonal(square,allPieces);

        long moves = crosses | diagonals;




        return new long[]{moves & ~allPieces,(moves & oppositeSidePieces )};

    }

    */



    public void initMagicAttackTables(boolean isBishop){


        for(int square = 0; square < 64; square++){

            long bMask = Lookups.bishopMasks[square];
            long rMask = Lookups.rookMasks[square];


            long usedMask = isBishop ? bMask : rMask;

            int bitNum = indexSetBits(usedMask).size();

            int occVariations = 1<< bitNum;

            for(int block = 0; block < occVariations; block++){

                if(isBishop){

                    long occupancy = getBlockers(block,bitNum,usedMask);

                    int magicKey= (int) ((occupancy * Lookups.bishopMagics[square]) >>> (64 - Lookups.bishopIndexBits[square]));

                    Lookups.bishopMagicAttacks[square][magicKey] = calcDiagonal(square, occupancy);

                }
                else{

                    long occupancy = getBlockers(block,bitNum,usedMask);

                    int magicKey= (int) ((occupancy * Lookups.rookMagics[square]) >>> (64 - Lookups.rookIndexBits[square]));

                    Lookups.rookMagicAttacks[square][magicKey] = calcCross(square, occupancy);

                }
            }


        }

    }

    public void initKingTables(){

        for(int i =0; i <64; i++){

            long kingBitBoard = 0b1L<< i;
            long kingAFile = kingBitBoard & Lookups.fileTables[0];
            long kingHFile = kingBitBoard & Lookups.fileTables[3];

            long move1 = kingAFile <<7;
            long move2 = kingBitBoard <<8;
            long move3 = kingHFile <<9;
            long move4 = kingHFile <<1;

            long move5 = kingHFile >>> 7;


            long move6 = kingBitBoard >>>8;
            long move7  = kingAFile >>>9;
            long move8 = kingAFile >>>1;

            long kingPsuedos = move1 | move2| move3| move4| move5| move6| move7| move8;

            Lookups.kingLookups[i] = kingPsuedos;
        }
    }

    long getBlockers(int index, int bitNum, long mask)
    {
        // occupancy map
        long occupancy = 0L;

        // loop over the range of bits within attack mask
        for (int count = 0; count < bitNum; count++)
        {
            // get LS1B index of attacks mask
            int square = getLSB(mask);

            // pop LS1B in attack map
            mask &= mask -1;

            // make sure occupancy is on board
            if ((index & (1 << count))!=0)
                // populate occupancy map
                occupancy |= (1L << square);
        }

        // return occupancy map
        return occupancy;
    }



    public static int getLSB(long bitboard){


        String bits = padZeros(bitboard);
        int count =0;

        for(int i = bits.length()-1; i>=0;i--){
            if(bits.charAt(i) == '1'){
                break;
            }
            count++;
        }


        return count;



    }



    public long calcDiagonal(int square, long allPieces){
        int r,f;
        int tr = square /8 ;
        int tf = square %8 ;

        long attacksB = 0L;

        //System.out.println("Square: " + i + " tr: " + tr + " tf: " + tf + " target Square: " + (((tr+1) * 8) + (tf+1)));

        //northeast ray
        for(r = tr+1, f =tf+1; r<=7 && f<=7;r++, f++){
            attacksB |= 1L << (r*8 +f);
            if(((1L << (r*8 +f)) & allPieces) != 0){
                break;
            }
        }

        for(r = tr+1, f =tf-1; r<=7 && f>=0;r++, f--){
            attacksB |= 1L << (r*8 +f);
            if(((1L << (r*8 +f)) & allPieces) != 0){
                break;
            }
        }

        for(r = tr-1, f =tf+1; r>=0 && f<=7;r--, f++){
            attacksB |= 1L << (r*8 +f);
            if(((1L << (r*8 +f)) & allPieces) != 0){
                break;
            }
        }

        for(r = tr-1, f =tf-1; r>=0 && f>=0;r--, f--) {
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



    public long generateSideAttackMask(long[] pieces, long sideToMove, long black, long white, long all){

        if(sideToMove ==1){

            long attackMap =0L;

            ArrayList<Integer> indices = indexSetBits(pieces[0]);


            for(Integer num: indices){
                attackMap |= calculateBlackPawnMoves(num,black,white,all)[1];

            }



            indices = indexSetBits(pieces[1]);
            for(Integer num: indices){
                attackMap |=  calculateRookMoves(num,black,white,all)[1];

            }


            indices = indexSetBits(pieces[2]);
            for(Integer num: indices){
                attackMap |=  calculateKnightMoves(num,black,white,all)[1];

            }


            indices = indexSetBits(pieces[3]);
            for(Integer num: indices){
                attackMap |= calculateKingMoves(num,black,white,all)[1];

            }

            indices = indexSetBits(pieces[4]);

            for(Integer num: indices){
                attackMap |= calculateQueenMoves(num,black,white,all)[1];

            }

            indices = indexSetBits(pieces[5]);
            for(Integer num: indices){
                attackMap |= calculateBishopMoves(num,black,white,all)[1];

            }



            return attackMap;


        }
        else{
            long attackMap =0L;

            ArrayList<Integer> indices = indexSetBits(pieces[6]);

            for(Integer num: indices){
                attackMap |=calculateWhitePawnMoves(num,white,black,all)[1];

            }

            indices = indexSetBits(pieces[7]);
            for(Integer num: indices){
                attackMap |= calculateRookMoves(num,white,black,all)[1];

            }


            indices = indexSetBits(pieces[8]);
            for(Integer num: indices){
                attackMap |= calculateKnightMoves(num,white,black,all)[1];

            }


            indices = indexSetBits(pieces[9]);
            for(Integer num: indices){
                attackMap |= calculateKingMoves(num,white,black,all)[1];

            }

            indices = indexSetBits(pieces[10]);

            for(Integer num: indices){
                attackMap |= calculateQueenMoves(num,white,black,all)[1];

            }

            indices = indexSetBits(pieces[11]);
            for(Integer num: indices){
                attackMap |= calculateBishopMoves(num,white,black,all)[1];

            }

            return attackMap;

        }

    }

    public static int evaluatePos(Move move,long castleRights, int enPass, int depth){
        //, long black, long white, long all

        long[] bitboards = Program.copyArray(move.bitboardCopys);
        ChessBoard board = new ChessBoard();

        long[] teamLongs = Program.generateTeamLongs(bitboards);



        ArrayList<Integer> blackPindices = indexSetBits(bitboards[0]);

        ArrayList<Integer> whitePindices = indexSetBits(bitboards[6]);


        ArrayList<Integer> blackBindices = indexSetBits(bitboards[5]);

        ArrayList<Integer> whiteBindices = indexSetBits(bitboards[11]);





        //pawns worth 1, knights/bishops worth 3, rook worth 5, queen worth 9, checkmate worth 10000


        int specialWhite =0;

        int specialBlack =0;



        int blackMoveSize= Program.generateBlackMoves(bitboards,castleRights,enPass).size();
        int whiteMoveSize= Program.generateWhiteMoves(bitboards,castleRights,enPass).size();


        long blackAttack = board.generateSideAttackMask(bitboards,1,teamLongs[0],teamLongs[1],teamLongs[2]);
        long whiteAttack = board.generateSideAttackMask(bitboards,-1,teamLongs[0],teamLongs[1],teamLongs[2]);

        boolean blackCheck = board.checkForCheck(bitboards[3],whiteAttack);

        boolean whiteCheck = board.checkForCheck(bitboards[9],blackAttack);

        int simpleMatScore = (9 * (indexSetBits(bitboards[10]).size()- indexSetBits(bitboards[4]).size())) +
                (5 * (indexSetBits(bitboards[7]).size()- indexSetBits(bitboards[1]).size())) +
                (3 * (indexSetBits(bitboards[8]).size()- indexSetBits(bitboards[2]).size())) +
                (3 * (whiteBindices.size()- blackBindices.size())) +
                (1 * (whitePindices.size()- blackPindices.size()));



        int mobilityScore = 0;

        if(GameState.moveCount <=5){
            if(move.pieceType == 4){
                specialBlack-=1;
            }
            if(move.pieceType == 10){
                specialWhite-=1;
            }
        }



        if(move.castle){

            if(GameState.botSide == 1) specialWhite+=2;
            if(GameState.botSide == -1)specialBlack+=2;
        }


        if((1l<<3 & GameState.castleRights)!= 0 | (1l<<2 & GameState.castleRights)!= 0){

            specialWhite+=2;
        }

        if((1l<<1 & GameState.castleRights)!= 0 | (1l & GameState.castleRights)!= 0){

            specialBlack+=2;
        }

        //three fold repition on bot's end check
        if(GameState.moveHistory != null){

            int count =0;
            for(long[] longs: GameState.moveHistory){

                if(Arrays.equals(longs,bitboards)){
                    count++;
                }

            }
            if(count>=2){
                if(GameState.botSide ==1){
                    specialWhite =-500;
                }
                else{
                    specialBlack =-500;
                }
            }
        }

        //checkmates
        if(blackMoveSize==0 & (blackCheck == true )){

            if(depth>0){
                specialWhite =1000 * depth;

            }
            else {
                specialWhite = 1000;
            }

        }

        if(whiteMoveSize==0 & (whiteCheck == true )){
            if(depth>0){
                specialBlack =1000 * depth;

            }
            else {
                specialBlack = 1000;
            }
        }



        //stalemates
        if(blackMoveSize==0 & (blackCheck == false )){

            specialWhite = -500;

        }
        if(whiteMoveSize==0 & (whiteCheck== false )){

                specialBlack = -500;

        }







        return (specialWhite -specialBlack)+ (simpleMatScore +mobilityScore);

    }

    public static long[] initBishopMasks() {


        long[] masks = new long[64];

        for (int i = 0; i < 64; i++) {

            int r, f;
            int tr = i / 8;
            int tf = i % 8;

            long attacksB = 0L;

            //System.out.println("Square: " + i + " tr: " + tr + " tf: " + tf + " target Square: " + (((tr+1) * 8) + (tf+1)));

            //northeast ray
            for (r = tr + 1, f = tf + 1; r <= 6 && f <= 6; r++, f++) {
                attacksB |= 1L << (r * 8 + f);
            }

            for (r = tr + 1, f = tf - 1; r <= 6 && f >= 1; r++, f--) {
                attacksB |= 1L << (r * 8 + f);

            }

            for (r = tr - 1, f = tf + 1; r >= 1 && f <= 6; r--, f++) {
                attacksB |= 1L << (r * 8 + f);

            }

            for (r = tr - 1, f = tf - 1; r >= 1 && f >= 1; r--, f--) {
                attacksB |= 1L << (r * 8 + f);

            }

            masks[i] = attacksB;

        }
        return masks;

    }



    public static long[] initRookMasks(){

        long[] masks = new long[64];

        for (int i = 0; i < 64; i++) {


            int r,f;
            int tr = i /8;
            int tf = i %8;

            long attacksR = 0L;

            //System.out.println("Square: " + i + " tr: " + tr + " tf: " + tf + " target Square: " + (((tr+1) * 8) + (tf+1)));

            //northeast ray
            for(r = tr+1; r<=6 ;r++){
                attacksR |= 1L << (r*8 +tf);

            }

            for(r = tr-1; r>=1;r--){
                attacksR |= 1L << (r*8 +tf);

            }

            for(f = tf+1; f<=6;f++){
                attacksR |= 1L << (tr*8 +f);

            }
            for(f = tf-1; f>=1;f--){
                attacksR |= 1L << (tr*8 +f);

            }

            masks[i] = attacksR;
        }
        return masks;





    }


    public static ArrayList<Integer> indexSetBits(long number){

        final  ArrayList<Integer> positions = new ArrayList<>();
        int position = 0;
        while (number != 0) {
            if ((number & 1L) != 0) {
                positions.add(position);
            }
            position++;
            number = number >>> 1;
        }
        return positions;
    }


    /*public static ArrayList<Integer> indexSetBits(Long bitboard){

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


     */


    public static boolean checkForCheckmate(long[] pieces, long castles,int enPass, int side){

        long[] teamLongs = Program.generateTeamLongs(pieces);
        ChessBoard board = new ChessBoard();

        if(Program.generateBlackMoves(pieces,castles,enPass).size()==0 & (board.checkForCheck(pieces[3],board.generateSideAttackMask(pieces,-1,teamLongs[0],teamLongs[1],teamLongs[2]))== true )){
            return true;
        }
        if(Program.generateWhiteMoves(pieces,castles,enPass).size()==0 & (board.checkForCheck(pieces[9],board.generateSideAttackMask(pieces,1,teamLongs[0],teamLongs[1],teamLongs[2]))== true )){
            return true;
        }

        else return false;
    }
    public static boolean checkForStalemate(long[] pieces, long castles,int enPass, int side){

        long[] teamLongs = Program.generateTeamLongs(pieces);
        ChessBoard board = new ChessBoard();

        if(Program.generateBlackMoves(pieces,castles,enPass).size()==0 & (board.checkForCheck(pieces[3],board.generateSideAttackMask(pieces,-1,teamLongs[0],teamLongs[1],teamLongs[2]))== false )){
            return true;
        }
        if(Program.generateWhiteMoves(pieces,castles,enPass).size()==0 & (board.checkForCheck(pieces[9],board.generateSideAttackMask(pieces,1,teamLongs[0],teamLongs[1],teamLongs[2]))== false )){
            return true;
        }

        else return false;
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