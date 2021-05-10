package Chess;


import java.util.Comparator;



public class MoveSorter implements Comparator<Move>{


    @Override
    public int compare(Move o1, Move o2) {

        return Integer.compare(o2.evalScore,o1.evalScore);
    }
}
