package Chess;

public class MovePair {

   private  Move m;
   private double evalScore;

   public MovePair(Move move, double score){

       m = move;
       evalScore= score;
   }

   public Move first(){
       return m;
   }

    public double second(){
        return evalScore;
    }

}
