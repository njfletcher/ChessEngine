package Chess;


public class FenParser {



    public void fenToBitboards(String fen) {

        /*GOAL: take fen string of a starting position and convert it to a full array of bitboards
         will be used for evaluation mainly. http://www.chess-poster.com/english/fen/fen_epd_viewer.htm

        Example: 5r2/2p2rb1/1pNp4/p2Pp1pk/2P1K3/PP3PP1/5R2/5R2 w - - 1 51
         - Divided by file(/), always reads left to right in terms of rank.
        -Last part represents whose turn it is, castling and special moves rights, and timer options.
        - lower case pieces = black, upper case pieces = white.

         */

        String[] parsed = fen.split(" ");

        String[] positionsbyRank = parsed[0].split("/");

        int counter = 63;

        //handles the piece positions

        for (String rank : positionsbyRank) {


            for (int i = rank.length() - 1; i >= 0; i--) {

                char letter = rank.charAt(i);
                switch (letter) {
                    case 'p':
                        GameState.bPawns |= 1L << counter;
                        break;
                    case 'r':
                        GameState.bRooks |= 1L << counter;
                        break;
                    case 'n':
                        GameState.bKnights |= 1L << counter;
                        break;
                    case 'b':
                        GameState.bBishops |= 1L << counter;
                        break;
                    case 'q':
                        GameState.bQueens |= 1L << counter;
                        break;
                    case 'k':
                        GameState.bKing |= 1L << counter;
                        break;
                    case 'P':
                        GameState.wPawns |= 1L << counter;
                        break;
                    case 'R':
                        GameState.wRooks |= 1L << counter;
                        break;
                    case 'N':
                        GameState.wKnights |= 1L << counter;
                        break;
                    case 'B':
                        GameState.wBishops |= 1L << counter;
                        break;
                    case 'Q':
                        GameState.wQueens |= 1L << counter;
                        break;
                    case 'K':
                        GameState.wKing |= 1L << counter;
                        break;
                    case '/':
                        counter++;
                        break;
                    case '1':
                        break;
                    case '2':
                        counter--;
                        break;
                    case '3':
                        counter -= 2;
                        break;
                    case '4':
                        counter -= 3;
                        break;
                    case '5':
                        counter -= 4;
                        break;
                    case '6':
                        counter -= 5;
                        break;
                    case '7':
                        counter -= 6;
                        break;
                    case '8':
                        counter -= 7;
                        break;

                }
                counter--;
            }


        }
        //handles the rest of the fen.

        //side to move
        GameState.sideToMove = parsed[1].equals("w") ? 1 : -1;


        //castle rights
        if(parsed[2].contains("K")){
            GameState.castleRights |= 1L << 3;
        }
        if(parsed[2].contains("Q")){
            GameState.castleRights |= 1L << 2;
        }
        if(parsed[2].contains("k")){
            GameState.castleRights |= 1L << 1;
        }
        if(parsed[2].contains("q")){
            GameState.castleRights |= 1L;
        }



        //enPassant square

        GameState.enPassant = parsed[3].equals("-") ? 64 : Integer.parseInt(parsed[3]);
    }





    public boolean verifyFen(String fen){

        return false;
    }


}
