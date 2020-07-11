package com.example.chess;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public class ChessLogic {

    public boolean WHITE_TURN;
    private Hashtable<String, String> LOCATION_TABLE = new Hashtable<>();

    public ChessLogic(Hashtable<String, String> locationTable) {
        this.WHITE_TURN = true;
        this.LOCATION_TABLE.putAll(locationTable);
    }

    private List<String> chessMove(String chessPiece) {
        List<String> locations = new ArrayList<>();
        String pieceLocation = getPieceLocation(chessPiece);
        if (pieceLocation.equals("")) return locations;
        String locationXS = pieceLocation.substring(pieceLocation.length() - 2, pieceLocation.length() - 1);
        String locationYS = pieceLocation.substring(pieceLocation.length() - 1);

        int locationX = Integer.parseInt(locationXS);
        int locationY = Integer.parseInt(locationYS);
        locations.clear();
        String checkLocation;
        boolean flag1, flag2, flag3, flag4, flag5, flag6, flag7, flag8;
        flag1 = flag2 = flag3 = flag4 = flag5 = flag6 = flag7 = flag8 = false;

        switch (chessPiece.substring(8)) {
            case "knight": {

                if (locationX + 2 <= 7 && locationY + 1 <= 7)
                    locations.add("box" + (locationX + 2) + (locationY + 1));
                if (locationX + 1 <= 7 && locationY + 2 <= 7)
                    locations.add("box" + (locationX + 1) + (locationY + 2));
                if (locationX - 1 >= 0 && locationY - 2 >= 0) {
                    locations.add("box" + (locationX - 1) + (locationY - 2));
                }
                if (locationX - 2 >= 0 && locationY - 1 >= 0) {
                    locations.add("box" + (locationX - 2) + (locationY - 1));
                }


                if (locationX + 2 <= 7 && locationY - 1 >= 0)
                    locations.add("box" + (locationX + 2) + (locationY - 1));
                if (locationX + 1 <= 7 && locationY - 2 >= 0)
                    locations.add("box" + (locationX + 1) + (locationY - 2));
                if (locationX - 1 >= 0 && locationY + 2 <= 7) {
                    locations.add("box" + (locationX - 1) + (locationY + 2));
                }
                if (locationX - 2 >= 0 && locationY + 1 <= 7) {
                    locations.add("box" + (locationX - 2) + (locationY + 1));
                }
            }
            break;

            case "bishop": {
                for (int i = 1; i <= 7; i++) {
                    if (locationX + i <= 7 && locationY + i <= 7)
                        if (!flag1) {
                            checkLocation = "box" + (locationX + i) + (locationY + i);
                            if (LOCATION_TABLE.contains(checkLocation)) flag1 = true;
                            locations.add(checkLocation);
                        }
                    if (locationX - i >= 0 && locationY - i >= 0)
                        if (!flag2) {
                            checkLocation = "box" + (locationX - i) + (locationY - i);
                            if (LOCATION_TABLE.contains(checkLocation)) flag2 = true;
                            locations.add(checkLocation);
                        }
                    if (locationX + i <= 7 && locationY - i >= 0)
                        if (!flag3) {
                            checkLocation = "box" + (locationX + i) + (locationY - i);
                            if (LOCATION_TABLE.contains(checkLocation)) flag3 = true;
                            locations.add(checkLocation);
                        }
                    if (locationX - i >= 0 && locationY + i <= 7)
                        if (!flag4) {
                            checkLocation = "box" + (locationX - i) + (locationY + i);
                            if (LOCATION_TABLE.contains(checkLocation)) flag4 = true;
                            locations.add(checkLocation);
                        }
                }
            }
            break;

            case "rook": {

                for (int i = 1; i <= 7; i++) {
                    if (locationX + i <= 7) {
                        if (!flag1) {
                            checkLocation = "box" + (locationX + i) + locationY;
                            if (LOCATION_TABLE.contains(checkLocation)) flag1 = true;
                            locations.add(checkLocation);
                        }
                    }
                    if (locationY + i <= 7) {
                        if (!flag2) {
                            checkLocation = "box" + locationX + (locationY + i);
                            if (LOCATION_TABLE.contains(checkLocation)) flag2 = true;
                            locations.add(checkLocation);
                        }
                    }
                    if (locationX - i >= 0) {
                        if (!flag3) {
                            checkLocation = "box" + (locationX - i) + locationY;
                            if (LOCATION_TABLE.contains(checkLocation)) flag3 = true;
                            locations.add(checkLocation);
                        }
                    }
                    if (locationY - i >= 0) {
                        if (!flag4) {
                            checkLocation = "box" + (locationX) + (locationY - i);
                            if (LOCATION_TABLE.contains(checkLocation)) flag4 = true;
                            locations.add(checkLocation);
                        }
                    }
                }
            }
            break;
            case "king": {
                if (locationX + 1 <= 7)
                    locations.add("box" + (locationX + 1) + locationY);

                if (locationY + 1 <= 7)
                    locations.add("box" + locationX + (locationY + 1));

                if (locationX - 1 >= 0)
                    locations.add("box" + (locationX - 1) + locationY);

                if (locationY - 1 >= 0)
                    locations.add("box" + locationX + (locationY - 1));

                if (locationX + 1 <= 7 && locationY + 1 <= 7)
                    locations.add("box" + (locationX + 1) + (locationY + 1));

                if (locationX - 1 >= 0 && locationY - 1 >= 0)
                    locations.add("box" + (locationX - 1) + (locationY - 1));

                if (locationX + 1 <= 7 && locationY - 1 >= 0)
                    locations.add("box" + (locationX + 1) + (locationY - 1));

                if (locationX - 1 >= 0 && locationY + 1 <= 7)
                    locations.add("box" + (locationX - 1) + (locationY + 1));
            }
            break;

            case "queen": {
                for (int i = 1; i <= 7; i++) {
                    if (locationX + i <= 7 && locationY + i <= 7)
                        if (!flag1) {
                            checkLocation = "box" + (locationX + i) + (locationY + i);
                            if (LOCATION_TABLE.contains(checkLocation)) flag1 = true;
                            locations.add(checkLocation);
                        }

                    if (locationX - i >= 0 && locationY - i >= 0)
                        if (!flag2) {
                            checkLocation = "box" + (locationX - i) + (locationY - i);
                            if (LOCATION_TABLE.contains(checkLocation)) flag2 = true;
                            locations.add(checkLocation);
                        }
                    if (locationX + i <= 7 && locationY - i >= 0)
                        if (!flag3) {
                            checkLocation = "box" + (locationX + i) + (locationY - i);
                            if (LOCATION_TABLE.contains(checkLocation)) flag3 = true;
                            locations.add(checkLocation);
                        }

                    if (locationX - i >= 0 && locationY + i <= 7)
                        if (!flag4) {
                            checkLocation = "box" + (locationX - i) + (locationY + i);
                            if (LOCATION_TABLE.contains(checkLocation)) flag4 = true;
                            locations.add(checkLocation);
                        }

                    if (locationX + i <= 7)
                        if (!flag5) {
                            checkLocation = "box" + (locationX + i) + (locationY);
                            if (LOCATION_TABLE.contains(checkLocation)) flag5 = true;
                            locations.add(checkLocation);
                        }

                    if (locationY + i <= 7)
                        if (!flag6) {
                            checkLocation = "box" + (locationX) + (locationY + i);
                            if (LOCATION_TABLE.contains(checkLocation)) flag6 = true;
                            locations.add(checkLocation);
                        }

                    if (locationX - i >= 0)
                        if (!flag7) {
                            checkLocation = "box" + (locationX - i) + (locationY);
                            if (LOCATION_TABLE.contains(checkLocation)) flag7 = true;
                            locations.add(checkLocation);
                        }

                    if (locationY - i >= 0)
                        if (!flag8) {
                            checkLocation = "box" + (locationX) + (locationY - i);
                            if (LOCATION_TABLE.contains(checkLocation)) flag8 = true;
                            locations.add(checkLocation);
                        }
                }
            }
            break;

            case "pawn": {
                String move;
                if (chessPiece.contains("black")) {
                    if (locationX + 1 <= 7) {
                        move = "box" + (locationX + 1) + locationY;
                        if (!LOCATION_TABLE.contains(move)) {
                            locations.add(move);
                            move = "box" + (locationX + 2) + locationY;
                            if (locationX == 1 && !LOCATION_TABLE.contains(move)) {
                                locations.add(move);
                            }
                        }

                        move = "box" + (locationX + 1) + (locationY + 1);
                        if (locationY + 1 <= 7) {
                            if (LOCATION_TABLE.contains(move)) {
                                locations.add(move);
                            }
                        }
                        move = "box" + (locationX + 1) + (locationY - 1);
                        if (locationY - 1 >= 0) {
                            if (LOCATION_TABLE.contains(move)) {
                                locations.add(move);
                            }
                        }
                    }
                }

                if (chessPiece.contains("white")) {
                    if (locationX - 1 >= 0) {
                        move = "box" + (locationX - 1) + locationY;
                        if (!LOCATION_TABLE.contains(move)) {
                            locations.add(move);
                            move = "box" + (locationX - 2) + locationY;
                            if (locationX == 6 && !LOCATION_TABLE.contains(move)) {
                                locations.add(move);
                            }
                        }

                        move = "box" + (locationX - 1) + (locationY + 1);
                        if (locationY + 1 <= 7) {
                            if (LOCATION_TABLE.contains(move)) {
                                locations.add(move);
                            }
                        }

                        move = "box" + (locationX - 1) + (locationY - 1);
                        if (locationY - 1 >= 0) {
                            if (LOCATION_TABLE.contains(move))
                                locations.add(move);
                        }
                    }
                }
            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + chessPiece.substring(8));
        }
        return locations;
    }

    public List<String> possibleMoves(String chessPiece, boolean virtualCall) {
        List<String> ret = new ArrayList<>();
        List<String> finalRet = new ArrayList<>();

        if (!LOCATION_TABLE.containsKey(chessPiece)) return ret;
        String pieceLocation = getPieceLocation(chessPiece);

        if (chessPiece.contains("white") && WHITE_TURN) {
            for (String move : chessMove(chessPiece)) {
                if (!LOCATION_TABLE.contains(move)) {
                    ret.add(move);
                } else {
                    for (String piece : LOCATION_TABLE.keySet()) {
                        if (Objects.equals(LOCATION_TABLE.get(piece), move) && piece.contains("black"))
                            ret.add(move);
                    }
                }
            }
        } else if (chessPiece.contains("black") && !WHITE_TURN) {
            for (String move : chessMove(chessPiece)) {
                if (!LOCATION_TABLE.contains(move)) {
                    ret.add(move);
                } else {
                    for (String piece : LOCATION_TABLE.keySet()) {
                        if (Objects.equals(LOCATION_TABLE.get(piece), move) && piece.contains("white"))
                            ret.add(move);
                    }
                }
            }
        }
        if (virtualCall) return ret;
        for (String dest : ret) {
            if (!isMoveCreatingCheckForSelf(chessPiece, pieceLocation, dest))
                finalRet.add(dest);
        }
        return finalRet;
    }

    // This method returns the name of killedPiece if killing happens while moving given chessPiece to dest.
    public String movePieceFromSrcToDest(String chessPiece, String dest) {

        String KilledPiece = getPieceAtLocation(dest);
        if (KilledPiece != null && KilledPiece.equals(chessPiece))
            return null;
        System.out.println(chessPiece + "********" + KilledPiece);
        if (KilledPiece != null) LOCATION_TABLE.put(KilledPiece, "");
        LOCATION_TABLE.put(chessPiece, dest);

        return KilledPiece;
    }


    private boolean isMoveCreatingCheckForSelf(String chessPiece, String src, String dest) {

        // temporarily move the chessPiece from src to dest
        String killedPieceIfAny = movePieceFromSrcToDest(chessPiece, dest);

        // check if current chessPiece turn king can be killed by opponent
        boolean ret = isCheck();

        // move back chessPiece from dest to src
        movePieceFromSrcToDest(chessPiece, src);
        if (killedPieceIfAny != null) {
            movePieceFromSrcToDest(killedPieceIfAny, dest);
        }

        return ret;
    }

    public boolean isCheck() {
        String kingChessPiece, oppositePiecesColor;

        //loop through all opponent pieces and return true if they can kill current turn's king

        if (WHITE_TURN) {
            kingChessPiece = "white_0_king";
            oppositePiecesColor = "black";
        } else {
            kingChessPiece = "black_0_king";
            oppositePiecesColor = "white";
        }

        String kingLocation = getPieceLocation(kingChessPiece);

        for (String piece : LOCATION_TABLE.keySet()) {
            if (piece.contains(oppositePiecesColor)) {
                WHITE_TURN = !WHITE_TURN;
                if (possibleMoves(piece, true).contains(kingLocation)) {
                    WHITE_TURN = !WHITE_TURN;
                    return true;
                }
                WHITE_TURN = !WHITE_TURN;
            }
        }
        return false;
    }

    public boolean isCheckmate() {
        if(!isCheck()) return false;
        String pieceColor = WHITE_TURN ? "white" : "black";
        for (String piece : LOCATION_TABLE.keySet()) {
            if (piece.contains(pieceColor)) {
                if (possibleMoves(piece, false).size() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isDraw() {
        if (isCheck()) return false;
        String piecesColor = WHITE_TURN ? "white" : "black";
        for (String piece : LOCATION_TABLE.keySet()) {
            if (piece.contains(piecesColor)) {
                if (possibleMoves(piece, false).size() > 0) {
                    return false;
                }
            }
        }
        return true;

    }

    public String getPieceLocation(String chessPiece) {
        return LOCATION_TABLE.get(chessPiece);
    }

    private String getPieceAtLocation(String location) {
        for (String piece : LOCATION_TABLE.keySet()) {
            if (Objects.equals(LOCATION_TABLE.get(piece), location))
                return piece;
        }
        return null;
    }

    public void resetGame(Hashtable<String, String> LOCATION_TABLE) {
        this.WHITE_TURN = true;
        this.LOCATION_TABLE.clear();
        this.LOCATION_TABLE.putAll(LOCATION_TABLE);
    }
}
