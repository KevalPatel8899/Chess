package com.example.chess;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public class ChessLogic {

  public boolean WHITE_TURN;
  public String enPassant = "";
  private Hashtable<String, String> LOCATION_TABLE = new Hashtable<>();
  private boolean WHITE_K_SIDE_CASTLE_RIGHTS = true;
  private boolean BLACK_K_SIDE_CASTLE_RIGHTS = true;
  private boolean WHITE_Q_SIDE_CASTLE_RIGHTS = true;
  private boolean BLACK_Q_SIDE_CASTLE_RIGHTS = true;

  public ChessLogic(Hashtable<String, String> locationTable) {
    this.WHITE_TURN = true;
    this.LOCATION_TABLE.putAll(locationTable);
  }

  private List<String> chessMove(String chessPiece) {
    List<String> locations = new ArrayList<>();
    String pieceLocation = getPieceLocation(chessPiece);

    if (pieceLocation.equals("")) return locations;

    int x = Integer.parseInt(pieceLocation.substring(3, 4));
    int y = Integer.parseInt(pieceLocation.substring(4));
    String checkLocation;
    boolean flag1, flag2, flag3, flag4, flag5, flag6, flag7, flag8;
    flag1 = flag2 = flag3 = flag4 = flag5 = flag6 = flag7 = flag8 = false;

    switch (chessPiece.substring(8)) {
      case "knight":
        {
          if (x + 2 <= 7 && y + 1 <= 7) locations.add("box" + (x + 2) + (y + 1));
          if (x + 1 <= 7 && y + 2 <= 7) locations.add("box" + (x + 1) + (y + 2));
          if (x - 1 >= 0 && y - 2 >= 0) locations.add("box" + (x - 1) + (y - 2));
          if (x - 2 >= 0 && y - 1 >= 0) locations.add("box" + (x - 2) + (y - 1));

          if (x + 2 <= 7 && y - 1 >= 0) locations.add("box" + (x + 2) + (y - 1));
          if (x + 1 <= 7 && y - 2 >= 0) locations.add("box" + (x + 1) + (y - 2));
          if (x - 1 >= 0 && y + 2 <= 7) locations.add("box" + (x - 1) + (y + 2));
          if (x - 2 >= 0 && y + 1 <= 7) locations.add("box" + (x - 2) + (y + 1));
        }
        break;

      case "bishop":
        {
          for (int i = 1; i <= 7; i++) {
            if (x + i <= 7 && y + i <= 7)
              if (!flag1) {
                checkLocation = "box" + (x + i) + (y + i);
                if (LOCATION_TABLE.contains(checkLocation)) flag1 = true;
                locations.add(checkLocation);
              }
            if (x - i >= 0 && y - i >= 0)
              if (!flag2) {
                checkLocation = "box" + (x - i) + (y - i);
                if (LOCATION_TABLE.contains(checkLocation)) flag2 = true;
                locations.add(checkLocation);
              }
            if (x + i <= 7 && y - i >= 0)
              if (!flag3) {
                checkLocation = "box" + (x + i) + (y - i);
                if (LOCATION_TABLE.contains(checkLocation)) flag3 = true;
                locations.add(checkLocation);
              }
            if (x - i >= 0 && y + i <= 7)
              if (!flag4) {
                checkLocation = "box" + (x - i) + (y + i);
                if (LOCATION_TABLE.contains(checkLocation)) flag4 = true;
                locations.add(checkLocation);
              }
          }
        }
        break;

      case "rook":
        {
          for (int i = 1; i <= 7; i++) {
            if (x + i <= 7)
              if (!flag1) {
                checkLocation = "box" + (x + i) + y;
                if (LOCATION_TABLE.contains(checkLocation)) flag1 = true;
                locations.add(checkLocation);
              }
            if (y + i <= 7)
              if (!flag2) {
                checkLocation = "box" + x + (y + i);
                if (LOCATION_TABLE.contains(checkLocation)) flag2 = true;
                locations.add(checkLocation);
              }
            if (x - i >= 0)
              if (!flag3) {
                checkLocation = "box" + (x - i) + y;
                if (LOCATION_TABLE.contains(checkLocation)) flag3 = true;
                locations.add(checkLocation);
              }
            if (y - i >= 0)
              if (!flag4) {
                checkLocation = "box" + (x) + (y - i);
                if (LOCATION_TABLE.contains(checkLocation)) flag4 = true;
                locations.add(checkLocation);
              }
          }
        }
        break;
      case "king":
        {
          if (x + 1 <= 7) locations.add("box" + (x + 1) + (y));
          if (y + 1 <= 7) locations.add("box" + (x) + (y + 1));
          if (x - 1 >= 0) locations.add("box" + (x - 1) + (y));
          if (y - 1 >= 0) locations.add("box" + (x) + (y - 1));
          if (x + 1 <= 7 && y + 1 <= 7) locations.add("box" + (x + 1) + (y + 1));
          if (x - 1 >= 0 && y - 1 >= 0) locations.add("box" + (x - 1) + (y - 1));
          if (x + 1 <= 7 && y - 1 >= 0) locations.add("box" + (x + 1) + (y - 1));
          if (x - 1 >= 0 && y + 1 <= 7) locations.add("box" + (x - 1) + (y + 1));

          // checking for castle moves
          if (WHITE_TURN) {
            if (WHITE_Q_SIDE_CASTLE_RIGHTS
                && !LOCATION_TABLE.contains("box72")
                && !LOCATION_TABLE.contains("box73")) {
              locations.add("box72");
            }
            if (WHITE_K_SIDE_CASTLE_RIGHTS
                && !LOCATION_TABLE.contains("box75")
                && !LOCATION_TABLE.contains("box76")) {
              locations.add("box76");
            }
          } else {
            if (BLACK_Q_SIDE_CASTLE_RIGHTS
                && !LOCATION_TABLE.contains("box02")
                && !LOCATION_TABLE.contains("box03")) {
              locations.add("box02");
            }
            if (BLACK_K_SIDE_CASTLE_RIGHTS
                && !LOCATION_TABLE.contains("box05")
                && !LOCATION_TABLE.contains("box06")) {
              locations.add("box06");
            }
          }
        }
        break;

      case "queen":
        {
          for (int i = 1; i <= 7; i++) {
            if (x + i <= 7 && y + i <= 7)
              if (!flag1) {
                checkLocation = "box" + (x + i) + (y + i);
                if (LOCATION_TABLE.contains(checkLocation)) flag1 = true;
                locations.add(checkLocation);
              }
            if (x - i >= 0 && y - i >= 0)
              if (!flag2) {
                checkLocation = "box" + (x - i) + (y - i);
                if (LOCATION_TABLE.contains(checkLocation)) flag2 = true;
                locations.add(checkLocation);
              }
            if (x + i <= 7 && y - i >= 0)
              if (!flag3) {
                checkLocation = "box" + (x + i) + (y - i);
                if (LOCATION_TABLE.contains(checkLocation)) flag3 = true;
                locations.add(checkLocation);
              }
            if (x - i >= 0 && y + i <= 7)
              if (!flag4) {
                checkLocation = "box" + (x - i) + (y + i);
                if (LOCATION_TABLE.contains(checkLocation)) flag4 = true;
                locations.add(checkLocation);
              }
            if (x + i <= 7)
              if (!flag5) {
                checkLocation = "box" + (x + i) + (y);
                if (LOCATION_TABLE.contains(checkLocation)) flag5 = true;
                locations.add(checkLocation);
              }
            if (y + i <= 7)
              if (!flag6) {
                checkLocation = "box" + (x) + (y + i);
                if (LOCATION_TABLE.contains(checkLocation)) flag6 = true;
                locations.add(checkLocation);
              }
            if (x - i >= 0)
              if (!flag7) {
                checkLocation = "box" + (x - i) + (y);
                if (LOCATION_TABLE.contains(checkLocation)) flag7 = true;
                locations.add(checkLocation);
              }
            if (y - i >= 0)
              if (!flag8) {
                checkLocation = "box" + (x) + (y - i);
                if (LOCATION_TABLE.contains(checkLocation)) flag8 = true;
                locations.add(checkLocation);
              }
          }
        }
        break;

      case "pawn":
        {
          String move;
          if (chessPiece.contains("black")) {
            if (x + 1 <= 7) {
              // check single step
              move = "box" + (x + 1) + y;
              if (!LOCATION_TABLE.contains(move)) {
                locations.add(move);
                // check double steps
                move = "box" + (x + 2) + y;
                if (x == 1 && !LOCATION_TABLE.contains(move)) locations.add(move);
              }
              // check for the kill
              move = "box" + (x + 1) + (y + 1);
              if (y + 1 <= 7) if (LOCATION_TABLE.contains(move)) locations.add(move);
              move = "box" + (x + 1) + (y - 1);
              if (y - 1 >= 0) if (LOCATION_TABLE.contains(move)) locations.add(move);

              // check for enPassant
              if (!enPassant.equals("") && x == 4) {
                int enPassant_y = Integer.parseInt(enPassant.substring(4));
                if (Math.abs(enPassant_y - y) == 1) locations.add("box" + (x + 1) + (enPassant_y));
              }
            }
          } else {
            if (x - 1 >= 0) {
              // check single step
              move = "box" + (x - 1) + y;
              if (!LOCATION_TABLE.contains(move)) {
                locations.add(move);
                // check double steps
                move = "box" + (x - 2) + y;
                if (x == 6 && !LOCATION_TABLE.contains(move)) locations.add(move);
              }
              // check for the kill
              move = "box" + (x - 1) + (y + 1);
              if (y + 1 <= 7) if (LOCATION_TABLE.contains(move)) locations.add(move);
              move = "box" + (x - 1) + (y - 1);
              if (y - 1 >= 0) if (LOCATION_TABLE.contains(move)) locations.add(move);

              // check for enPassant
              if (!enPassant.equals("") && x == 3) {
                int enPassant_y = Integer.parseInt(enPassant.substring(4));
                if (Math.abs(enPassant_y - y) == 1) locations.add("box" + (x - 1) + (enPassant_y));
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

    String opponentPieceColor = chessPiece.contains("white") ? "black" : "white";

    if ((WHITE_TURN && opponentPieceColor.equals("white"))
        || (!WHITE_TURN && opponentPieceColor.equals("black"))) return ret;

    for (String move : chessMove(chessPiece))
      if (!LOCATION_TABLE.contains(move)) ret.add(move);
      else
        for (String piece : LOCATION_TABLE.keySet())
          if (Objects.equals(LOCATION_TABLE.get(piece), move) && piece.contains(opponentPieceColor))
            ret.add(move);

    if (virtualCall) return ret;

    for (String dest : ret)
      if (!isMoveCreatingCheckForSelf(chessPiece, pieceLocation, dest)) finalRet.add(dest);
    if (chessPiece.contains("king")) {
      if (WHITE_TURN) {
        if (WHITE_Q_SIDE_CASTLE_RIGHTS && !finalRet.contains("box73")) finalRet.remove("box72");
        else if (WHITE_K_SIDE_CASTLE_RIGHTS && !finalRet.contains("box75"))
          finalRet.remove("box76");
      } else {
        if (BLACK_Q_SIDE_CASTLE_RIGHTS && !finalRet.contains("box03")) finalRet.remove("box02");
        else if (BLACK_K_SIDE_CASTLE_RIGHTS && !finalRet.contains("box05"))
          finalRet.remove("box06");
      }
    }
    return finalRet;
  }

  // This method returns the name of killedPiece if killing happens while moving given chessPiece to
  // dest.
  public String movePieceFromSrcToDest(String chessPiece, String dest, boolean virtualCall) {

    // Take away castle rights if king or rook moves.
    if (!virtualCall && (chessPiece.contains("king") || chessPiece.contains("rook"))) {
      if (chessPiece.contains("white")) {
        if (chessPiece.contains("king"))
          WHITE_K_SIDE_CASTLE_RIGHTS = WHITE_Q_SIDE_CASTLE_RIGHTS = false;
        else if (chessPiece.contains("1_rook")) WHITE_K_SIDE_CASTLE_RIGHTS = false;
        else if (chessPiece.contains("0_rook")) WHITE_Q_SIDE_CASTLE_RIGHTS = false;
      } else {
        if (chessPiece.contains("king"))
          BLACK_K_SIDE_CASTLE_RIGHTS = BLACK_Q_SIDE_CASTLE_RIGHTS = false;
        else if (chessPiece.contains("1_rook")) BLACK_K_SIDE_CASTLE_RIGHTS = false;
        else if (chessPiece.contains("0_rook")) BLACK_Q_SIDE_CASTLE_RIGHTS = false;
      }
    }

    String KilledPiece = getPieceAtLocation(dest);

    if (KilledPiece != null) {
      if (KilledPiece.equals(chessPiece)) return null;
      else LOCATION_TABLE.put(KilledPiece, "");
    }

    LOCATION_TABLE.put(chessPiece, dest);

    return KilledPiece;
  }

  private boolean isMoveCreatingCheckForSelf(String chessPiece, String src, String dest) {

    // temporarily move the chessPiece from src to dest
    String killedPieceIfAny = movePieceFromSrcToDest(chessPiece, dest, true);

    // check if current chessPiece turn king can be killed by opponent
    boolean ret = isCheck();

    // move back chessPiece from dest to src
    movePieceFromSrcToDest(chessPiece, src, true);

    if (killedPieceIfAny != null) movePieceFromSrcToDest(killedPieceIfAny, dest, true);

    return ret;
  }

  public boolean isCheck() {
    String kingChessPiece, oppositePiecesColor;

    // loop through all opponent pieces and return true if they can kill current turn's king

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
    if (!isCheck()) return false;

    String pieceColor = WHITE_TURN ? "white" : "black";

    for (String piece : LOCATION_TABLE.keySet())
      if (piece.contains(pieceColor) && possibleMoves(piece, false).size() > 0) return false;

    return true;
  }

  public boolean isDraw() {
    if (isCheck()) return false;

    String piecesColor = WHITE_TURN ? "white" : "black";

    for (String piece : LOCATION_TABLE.keySet())
      if (piece.contains(piecesColor) && possibleMoves(piece, false).size() > 0) return false;

    return true;
  }

  public String getPieceLocation(String chessPiece) {
    return LOCATION_TABLE.get(chessPiece);
  }

  private String getPieceAtLocation(String location) {
    for (String piece : LOCATION_TABLE.keySet()) {
      if (Objects.equals(LOCATION_TABLE.get(piece), location)) return piece;
    }
    return null;
  }


  public void resetGame(Hashtable<String, String> LOCATION_TABLE) {
    this.WHITE_TURN = true;
    this.enPassant = "";
    this.LOCATION_TABLE.clear();
    this.LOCATION_TABLE.putAll(LOCATION_TABLE);
    this.WHITE_Q_SIDE_CASTLE_RIGHTS =
        this.WHITE_K_SIDE_CASTLE_RIGHTS =
            this.BLACK_K_SIDE_CASTLE_RIGHTS = this.BLACK_Q_SIDE_CASTLE_RIGHTS = true;
  }
}
