package Chess;

public class Program {



    public static void main(String[] args) {


        ChessBoard board = new ChessBoard();
        FenParser parser = new FenParser();

        board.gameStart();

        //long bits =board.squareToBitboard("g8");

        //board.representation(bits);
        //board.representation(StartPos.fileTables[0]);



        parser.fenToBitboards("5r2/2p2rb1/1pNp4/p2Pp1pk/2P1K3/PP3PP1/5R2/5R2 w - - 1 51");


    }
}
