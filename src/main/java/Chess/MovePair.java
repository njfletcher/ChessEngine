package Chess;

public class MovePair {

   private  Move m;
   private int evalScore;

   public MovePair(Move move, int score){

       m = move;
       evalScore= score;
   }

   public Move first(){
       return m;
   }

    public int second(){
        return evalScore;
    }

}
