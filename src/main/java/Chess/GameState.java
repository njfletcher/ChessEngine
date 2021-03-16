package Chess;

public class GameState {


    //1 for white, 0 for black
    int sideToMove;

    /*
   4bit long: 1111

   wKingside, wQueenside, bkingside, bQueenside.
    */
    public static long castleRights;


    //holds square of possible en passant target squares.
    public static long whiteEnPassant;
    public static long blackEnPassant;



}
