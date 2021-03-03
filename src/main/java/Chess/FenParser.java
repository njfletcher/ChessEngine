package Chess;

public class FenParser {



    public void fenToBitboards(String fen){

        /*GOAL: take fen string of a starting position and convert it to a full array of bitboards
         will be used for evaluation mainly. http://www.chess-poster.com/english/fen/fen_epd_viewer.htm

        Example: 5r2/2p2rb1/1pNp4/p2Pp1pk/2P1K3/PP3PP1/5R2/5R2 w - - 1 51
         - Divided by file(/), always reads left to right in terms of rank.
        -Last part represents whose turn it is, castling and special moves rights, and timer options.
        - lower case pieces = black, upper case pieces = white.

         */

        String[] parsed = fen.split("/");

        int counter = 63;

        for(String s: parsed){

            for(int i =s.length()-1; i>=0; i--){

                char letter = s.charAt(i);
                switch(letter){
                    case 'p' : StartPos.bPawns |= 1L<< counter; break;
                    case 'r' : StartPos.bRooks |= 1L<<counter; break;
                    case 'n' : StartPos.bKnights |= 1L<<counter; break;
                    case 'b' : StartPos.bBishops |= 1L<<counter; break;
                    case 'q' : StartPos.bQueens |= 1L<<counter; break;
                    case 'k' : StartPos.bKing |= 1L<<counter; break;
                    case 'P' : StartPos.wPawns |= 1L<<counter; break;
                    case 'R' : StartPos.wRooks |= 1L<<counter; break;
                    case 'N' : StartPos.wKnights |= 1L<<counter; break;
                    case 'B' : StartPos.wBishops |= 1L<<counter; break;
                    case 'Q' : StartPos.wQueens |= 1L<<counter; break;
                    case 'K' : StartPos.wKing |= 1L<<counter; break;
                    //case '/' : counter ++; break;
                    case '1' : break;
                    case '2' : counter--; break;
                    case '3' : counter-=2; break;
                    case '4' : counter -=3; break;
                    case '5' : counter-=4; break;
                    case '6' : counter-=5; break;
                    case '7' : counter-=6; break;
                    case '8' : counter-=7; break;

                }
                counter--;
            }
        }


    }

    public boolean verifyFen(String fen){

        return false;
    }


}
