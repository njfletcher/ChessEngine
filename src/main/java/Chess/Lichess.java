package Chess;

//import sun.jvm.hotspot.debugger.ProcessInfo;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;

public class Lichess {

    final static String baseUrl = "https://lichess.org";
    private static final String token = System.getenv("API_TOKEN").replace("\"","");

    public static InputStream sendRequest(String method, String endpoint) throws IOException
    {
        HttpsURLConnection con = (HttpsURLConnection) new URL(baseUrl + endpoint).openConnection();
        con.setRequestMethod(method);
        if(token != null)
        {
            con.setRequestProperty("Authorization", "Bearer " + token);
        }
        con.setRequestProperty("Accept", "*/*");
        if(method.equals("POST"))
        {
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Length", "0");
        }
        con.setRequestProperty("User-Agent", "CompactChess (+https://hell.sh/CompactChess)");
        con.setDoInput(true);
        con.setDoOutput(method.equals("POST"));
        con.setUseCaches(false);
        try
        {
            return con.getInputStream();
        }
        catch(ConnectException e)
        {
            System.out.println("[NETW] Retrying " + method + " " + endpoint);
            return sendRequest(method, endpoint);
        }
        catch(IOException e)
        {
            if(e.getMessage().contains("Server returned HTTP response code: 504 for URL"))
            {
                System.out.println("[NETW] Retrying " + method + " " + endpoint);
                return sendRequest(method, endpoint);
            }
            else
            {
                throw e;
            }
        }
    }



    public static InputStream sendPOSTRequest(String endpoint, String data, Map<String, String> headers) throws IOException
    {


            HttpsURLConnection con = (HttpsURLConnection) new URL(baseUrl + endpoint).openConnection();
            con.setRequestMethod("POST");
            if (token != null) {
                con.setRequestProperty("Authorization", "Bearer " + token);
            }
            con.setRequestProperty("Accept", "*/*");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Length", String.valueOf(data.length()));
            con.setRequestProperty("User-Agent", "CompactChess (+https://hell.sh/CompactChess)");
            if(headers != null){
                for(Map.Entry<String,String> header : headers.entrySet()){

                    con.setRequestProperty(header.getKey().toString(), header.getValue().toString());

                }
            }
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            try {
                con.connect();
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(data);
                writer.flush();
                return con.getInputStream();
            } catch (ConnectException e) {
                System.out.println("[NETW] Retrying POST " + endpoint);
                return sendPOSTRequest(endpoint, data, headers);
            } catch (IOException e) {
                if (e.getMessage().contains("Server returned HTTP response code: 504 for URL")) {
                    System.out.println("[NETW] Retrying POST " + endpoint);
                    return sendPOSTRequest(endpoint, data, headers);
                } else {
                    throw e;
                }
            }
    }








    //gets incoming move from lichess opponent and updates gamestate bitboard.
    public static void recieveIncoming(String move) {

        GameState.updatePiecesArray();

        int movePieceType = -1;
        int attackedPieceType = -1;
        GameState.enPassant =64;

        //Q,N,B,R q,n,b,r

        //promotions are the only moves which have extra letter on end.
        if (move.length() == 5) {

            int moveFrom = boardSqs.getBitofSquare(move.substring(0, 2));

            int moveTo = boardSqs.getBitofSquare(move.substring(2,4));

            char promoteTo = move.charAt(move.length() - 1);


            for (int i = 0; i < 12; i++) {

                if ((GameState.statePieces[i] & (1L << moveFrom)) != 0) {
                    movePieceType = i;
                }
                if ((GameState.statePieces[i] & (1L << moveTo)) != 0) {
                    attackedPieceType = i;


                }
            }

            if(attackedPieceType != -1) {
                GameState.statePieces[attackedPieceType] &= ~(1L << moveTo);
            }

            GameState.statePieces[movePieceType] &= ~(1L << moveFrom);
            GameState.statePieces[Lookups.getIndexPromotion(promoteTo)] |= (1L << moveTo);
        }


        //any other move besides promotions, bc all have the same length of notation
        else {

            int moveFrom = boardSqs.getBitofSquare(move.substring(0, 2));

            int moveTo = boardSqs.getBitofSquare(move.substring(2));

            //find which piece is being moved
            for (int i = 0; i < 12; i++) {

                if ((GameState.statePieces[i] & (1L << moveFrom)) != 0) {
                    movePieceType = i;

                }
                if ((GameState.statePieces[i] & (1L << moveTo)) != 0) {
                    attackedPieceType = i;
                }
            }
            //-------------------------------------------------------------------


            //find if a piece is being directly attacked(different for enPass)
            if(attackedPieceType != -1) {
                GameState.statePieces[attackedPieceType] &= ~(1L << moveTo);
            }
            //-------------------------------------------------------------------


            //now check for arbitrary moves that are hard to translate from the move string: castles enpessant, etc.
            //also update gamestate castle rights, enpass sq, etc based on which piece is being moved


            //black and white kings, check for castles. Regardless null castle rights for side bc king is moving
            if (movePieceType == 9) {

                if (move.equals("e1g1")) {

                    GameState.statePieces[7] &= ~(1L << 7);
                    GameState.statePieces[7] |= (1L << 5);


                }

                if (move.equals("e1c1")) {

                    GameState.statePieces[7] &= ~(1L);
                    GameState.statePieces[7] |= (1L << 3);

                }
                GameState.castleRights &= ~(1L << 2);
                GameState.castleRights &= ~(1L <<3);
            }

            if (movePieceType == 3) {

                if (move.equals("e8g8")) {

                    GameState.statePieces[1] &= ~(1L << 63);
                    GameState.statePieces[1] |= (1L << 61);

                }

                if (move.equals("e8c8")) {


                    GameState.statePieces[1] &= ~(1L << 56);
                    GameState.statePieces[1] |= (1L << 59);

                }

                GameState.castleRights &= ~(1L);
                GameState.castleRights &= ~(1L <<1);
            }
            //-------------------------------------------------------------------


            //now rook moves, which null castle rights depending on which rook side is moving
            if (movePieceType == 7) {

                if(moveFrom == 0){
                    GameState.castleRights &= ~(1L<<2);
                }

                if(moveFrom == 7){
                    GameState.castleRights &= ~(1L<<3);
                }

            }

            if (movePieceType == 1) {

                if(moveFrom == 63){
                    GameState.castleRights &= ~(1L<<1);
                }

                if(moveFrom == 56){
                    GameState.castleRights &= ~(1L);
                }
            }
            //-------------------------------------------------------------------


            //pawn moves
            // special move could either be enPassant move, or a double push which generates an enPassant square.
            if (movePieceType == 0) {

                //double push
                if(moveFrom>moveTo+9){

                    GameState.enPassant = moveTo +8;
                }

                //enPass
                if((attackedPieceType == -1) && (moveTo == moveFrom -9 || moveTo == moveFrom -7)){

                    GameState.statePieces[6] &= ~(1L << (moveTo +8));
                }
            }

            if (movePieceType == 6) {

                //double push
                if(moveTo>moveFrom+9){
                    GameState.enPassant = moveTo -8;
                }

                //enPass
                if((attackedPieceType == -1) && (moveTo == moveFrom +9 || moveTo == moveFrom +7)){

                    GameState.statePieces[0] &= ~(1L << (moveTo -8));

                }

            }
            //-------------------------------------------------------------------






            GameState.statePieces[movePieceType] &= ~(1L << moveFrom);
            GameState.statePieces[movePieceType] |= (1L << moveTo);
            GameState.updateIndivBoards();
        }
    }

    public static String translateMove(Move m){


        if(m ==null){
            return "";
        }
        String move = boardSqs.getboardSq(m.squareFrom).toString() + boardSqs.getboardSq(m.squareTo).toString();

        return move;
    }


}
