package Chess;

public class Program {



    public static void main(String[] args) {


        ChessBoard board = new ChessBoard();
        FenParser parser = new FenParser();


        parser.fenToBitboards("5r2/2p2rb1/1pNp4/p2Pp1pk/2P1K3/PP3PP1/5R2/5R2 w - - 1 51");

        long wPieces= Lookups.wPawns | Lookups.wKnights | Lookups.wKing | Lookups.wBishops | Lookups.wQueens| Lookups.wRooks;
        long bPieces= Lookups.bPawns | Lookups.bKnights | Lookups.bKing | Lookups.bBishops | Lookups.bQueens| Lookups.bRooks;

        long allPieces= wPieces | bPieces;



        board.printBitBoard(Lookups.bQueens);
        board.printBitBoard(allPieces);


        board.printBitBoard(board.calculateQueenMoves(61,Lookups.wPieces,Lookups.bPieces,allPieces));






        //board.printBitBoard(Lookups.borderClip);

        //board.printBitBoard(Lookups.rookAttacks[12]);
        //board.printBitBoard(Lookups.bishopAttacks[12]);
        //board.printBitBoard(Lookups.queenAttacks[12]);


    }
}
