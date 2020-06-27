package com.example.chess;

public class Rook extends ChessPiece {

    protected Rook(String chessPieceLocation, String chessPiece) {
        super(chessPieceLocation, chessPiece);
    }

    private Rook(String chessPieceLocation){
        super(chessPieceLocation, "Rook");
    }
}
