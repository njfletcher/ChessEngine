package Chess;

public class Program {



    public static void main(String[] args) {


        ChessBoard board = new ChessBoard();
        FenParser parser = new FenParser();

        board.gameStart();

        //long bits =board.squareToBitboard("g8");

        //board.representation(bits);
        //board.representation(StartPos.fileTables[0]);



        parser.fenToBitboards("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");




        board.initSlidingAttacks();


        //board.printBitBoard(Lookups.borderClip);


    }
}
