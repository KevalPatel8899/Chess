package com.example.chess;

import java.util.Hashtable;
import java.util.List;

public class ChessLogic {

    private boolean WHITE_TURN;
    private Hashtable<String, String> ORIGINAL_LOCATION = new Hashtable<>();
    private Hashtable<String, String> locationTable = new Hashtable<>();
    private boolean IS_CHECK;
    private boolean IS_CHECKMATE;

    public ChessLogic(Hashtable<String, String> locationTable){
        this.WHITE_TURN = true;
        this.IS_CHECK = false;
        this.IS_CHECKMATE = false;
        this.locationTable = locationTable;
        this.ORIGINAL_LOCATION = locationTable;
    }

    public List<String> chessMove(String chessPiece) {
        return null;
    }
}
