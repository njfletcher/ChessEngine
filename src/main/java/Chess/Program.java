package Chess;

import java.util.ArrayList;

public class Program {



    public static void main(String[] args) {


        ChessBoard board = new ChessBoard();
        FenParser parser = new FenParser();


        parser.fenToBitboards("5r2/2p2rb1/1pNp4/p2Pp1pk/2P1K3/PP3PP1/5R2/5R2 w - - 1 51");

        long wPieces= GameState.wPawns | GameState.wKnights | GameState.wKing | GameState.wBishops | GameState.wQueens| GameState.wRooks;
        long bPieces= GameState.bPawns | GameState.bKnights | GameState.bKing | GameState.bBishops | GameState.bQueens| GameState.bRooks;

        long allPieces= wPieces | bPieces;




        ArrayList<Move> legalMoves = new ArrayList<Move>();
       ArrayList<Integer> indices = ChessBoard.indexSetBits(GameState.bPawns);

        for(Integer num : indices){

            long[] moves =board.calculateBlackPawnMoves(num,bPieces,wPieces,allPieces);
            ArrayList<Integer> moveIndices = ChessBoard.indexSetBits(moves[0]);
            ArrayList<Integer> attackIndices = ChessBoard.indexSetBits(moves[1]);

            for(Integer bit: moveIndices){

                long bRooksCopy = GameState.bPawns;
                //System.out.println("Before: ");
                //ChessBoard.printBitBoard(bRooksCopy);

                 bRooksCopy &= ~(1L<<num);
                 bRooksCopy |= (1L<<bit);



                 //System.out.println("after: ");
                 //ChessBoard.printBitBoard(bRooksCopy);

                 if(!(board.checkForCheck(GameState.bKing, board.generateSideAttackMask(0L)))){
                     legalMoves.add(new Move(num, bit));
                 }
            }
            for(Integer bit: attackIndices){

                long bRooksCopy = GameState.bPawns;
                System.out.println("Before: ");
                ChessBoard.printBitBoard(bRooksCopy);

                bRooksCopy &= ~(1L<<num);
                bRooksCopy |= (1L<<bit);

                System.out.println("after: ");
                ChessBoard.printBitBoard(bRooksCopy);

                for(int i =6; i<12;i++){
                    if((GameState.pieces[i] & (1L<<bit)) !=0){
                        long bitboardCopy = GameState.pieces[i];
                        bitboardCopy &= ~(1L<<num);

                    }
                }
            }
        }




       for(Move m: legalMoves){
          System.out.println(" from: " + m.squareFrom);
           System.out.println("-----------------------------");
           System.out.println(" to: " + m.squareTo);

        }






    }
    public enum Pieces {

        //lowercase for black pieces, upper for white.

        p,r,n,k,q,b,P,R,N,K,Q,B
    }

}
