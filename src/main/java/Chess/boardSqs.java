package Chess;

public enum boardSqs {

//used for mapping square coordinates(in UCI) to bitboard indices.
//Example: a1 = 0, b8 =1, h8 = 63
//"other" will be used for special moves: en passant, castling, promotion.
//http://pages.cs.wisc.edu/~psilord/blog/data/chess-pages/rep.html

        a1,b1,c1,d1,e1,f1,g1,h1,
        a2,b2,c2,d2,e2,f2,g2,h2,
        a3,b3,c3,d3,e3,f3,g3,h3,
        a4,b4,c4,d4,e4,f4,g4,h4,
        a5,b5,c5,d5,e5,f5,g5,h5,
        a6,b6,c6,d6,e6,f6,g6,h6,
        a7,b7,c7,d7,e7,f7,g7,h7,
        a8,b8,c8,d8,e8,f8,g8,h8,
        other;

        private static boardSqs[] list = boardSqs.values();

        public static boardSqs getboardSq(int i) {
                return list[i];
        }

        public static int listGetLastIndex() {
                return list.length - 1;
        }

        public static int getBitofSquare(String square){

                return boardSqs.valueOf(square).ordinal();
        }
}
