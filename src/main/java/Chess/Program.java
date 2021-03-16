package Chess;

public class Program {



    public static void main(String[] args) {


        ChessBoard board = new ChessBoard();
        FenParser parser = new FenParser();


        parser.fenToBitboards("5r2/2p2rb1/1pNp4/p2Pp1pk/2P1K3/PP3PP1/5R2/5R2 w - - 1 51");

        long wPieces= Lookups.wPawns | Lookups.wKnights | Lookups.wKing | Lookups.wBishops | Lookups.wQueens| Lookups.wRooks;
        long bPieces= Lookups.bPawns | Lookups.bKnights | Lookups.bKing | Lookups.bBishops | Lookups.bQueens| Lookups.bRooks;

        long allPieces= wPieces | bPieces;




        ChessBoard.printBitBoard(allPieces);

        ChessBoard.printBitBoard(Lookups.bKing);
        ChessBoard.printBitBoard(board.calculateQueenMoves(55,Lookups.wPieces,bPieces,allPieces)[0]);
        ChessBoard.printBitBoard(board.calculateQueenMoves(55,Lookups.wPieces,bPieces,allPieces)[1]);



        board.checkForCheck(Lookups.bKing, board.calculateQueenMoves(55,Lookups.wPieces,bPieces,allPieces)[1]);

        ChessBoard.printBitBoard(board.squareToBitboard("e1"));
        ChessBoard.printBitBoard(board.squareToBitboard("h1"));


        board.calculateWKCastle(14,board.squareToBitboard("e1"),board.squareToBitboard("h1"));


        ChessBoard.printBitBoard(board.calculateWKCastle(14,board.squareToBitboard("e1"),board.squareToBitboard("h1"))[0]);
        ChessBoard.printBitBoard(board.calculateWKCastle(14,board.squareToBitboard("e1"),board.squareToBitboard("h1"))[1]);




        //board.printBitBoard(Lookups.borderClip);

        //board.printBitBoard(Lookups.rookAttacks[12]);
        //board.printBitBoard(Lookups.bishopAttacks[12]);
        //board.printBitBoard(Lookups.queenAttacks[12]);


    }
    public enum Pieces {

        //lowercase for black pieces, upper for white.

        p,r,n,k,q,b,P,R,N,K,Q,B
    }
}
