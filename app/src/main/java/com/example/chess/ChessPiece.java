package com.example.chess;

import java.util.ArrayList;
import java.util.List;

abstract class ChessPiece{

    String
            chessPieceLocation, chessPiece;

    protected ChessPiece(String chessPieceLocation, String chessPiece){
        this.chessPiece =         chessPiece ;
        this.chessPieceLocation = chessPieceLocation;
    }

    public List<String>  chessMove(){
        String locationS = chessPieceLocation.substring(chessPieceLocation.length() - 2);
        String locationXS = chessPieceLocation.substring(chessPieceLocation.length() - 2, chessPieceLocation.length() - 1);
        String locationYS = chessPieceLocation.substring(chessPieceLocation.length() - 1);
        int location = Integer.parseInt(locationS);
        List<String> locations = new ArrayList<>();
        int locationX = Integer.parseInt(locationXS);
        int locationY = Integer.parseInt(locationYS);
        locations.clear();

        switch (chessPiece.substring(8)){
            case "Knight":{

                if(locationX + 2 <= 7 && locationY + 1 <= 7)locations.add("box"+Integer.toString( location + 21));
                if(locationX + 1 <= 7 && locationY + 2 <= 7)locations.add("box"+Integer.toString(location + 12));
                if(locationX - 1 >= 0 && locationY - 2 >= 0){locations.add("box"+Integer.toString(location - 12));}
                if(locationX - 2 >= 0 && locationY - 1 >= 0){locations.add("box"+Integer.toString(location - 21));}


                if(locationX + 2 <= 7 && locationY - 1 >= 0)locations.add("box"+Integer.toString( locationX + 2) +Integer.toString(locationY - 1));
                if(locationX + 1 <= 7 && locationY - 2 >= 0)locations.add("box"+Integer.toString( locationX + 1) +Integer.toString(locationY - 2));
                if(locationX - 1 >= 0 && locationY + 2 <= 7){locations.add("box"+Integer.toString( locationX - 1) +Integer.toString(locationY + 2));}
                if(locationX - 2 >= 0 && locationY + 1 <= 7){locations.add("box"+Integer.toString( locationX - 2) +Integer.toString(locationY + 1));}
            }
            break;

            case "Bishop" : {
                for(int i= 1 ; i <= 7 ; i++){
                    if(locationX + i <=7 && locationY + i <=7)
                    locations.add("box"+Integer.toString( locationX + i)+ Integer.toString( locationY + i));

                    if(locationX - i >= 0 && locationY - i >= 0)
                        locations.add("box"+Integer.toString( locationX - i)+ Integer.toString( locationY - i));

                    if(locationX + i <= 7 && locationY - i >= 0)
                        locations.add("box"+Integer.toString( locationX + i)+ Integer.toString( locationY - i));

                    if(locationX - i >= 0 && locationY + i <= 7)
                        locations.add("box"+Integer.toString( locationX - i)+ Integer.toString( locationY + i));
                }
            }
            break;

            case"Rook":{
                for(int i = 1; i <=7; i++){
                    if(locationX + i <=7)
                        locations.add("box"+Integer.toString( locationX + i)+ Integer.toString( locationY));

                    if(locationY + i <= 7)
                        locations.add("box"+Integer.toString( locationX)+ Integer.toString( locationY + i));

                    if (locationX - i >= 0)
                        locations.add("box"+Integer.toString( locationX - i)+ Integer.toString( locationY));

                    if(locationY - i >= 0)
                    locations.add("box"+Integer.toString( locationX )+ Integer.toString( locationY - i));
                }

            }
            break;
            case"King":{
                if(locationX + 1 <=7)
                    locations.add("box"+Integer.toString( locationX + 1)+ Integer.toString( locationY));

                if(locationY + 1 <= 7)
                    locations.add("box"+Integer.toString( locationX)+ Integer.toString( locationY + 1));

                if (locationX - 1 >= 0)
                    locations.add("box"+Integer.toString( locationX - 1)+ Integer.toString( locationY));

                if(locationY - 1 >= 0)
                    locations.add("box"+Integer.toString( locationX )+ Integer.toString( locationY - 1));

                if(locationX + 1 <=7 && locationY + 1 <=7)
                    locations.add("box"+Integer.toString( locationX + 1)+ Integer.toString( locationY + 1));

                if(locationX - 1 >= 0 && locationY - 1 >= 0)
                    locations.add("box"+Integer.toString( locationX - 1)+ Integer.toString( locationY - 1));

                if(locationX + 1 <= 7 && locationY - 1 >= 0)
                    locations.add("box"+Integer.toString( locationX + 1)+ Integer.toString( locationY - 1));

                if(locationX - 1 >= 0 && locationY + 1 <= 7)
                    locations.add("box"+Integer.toString( locationX - 1)+ Integer.toString( locationY + 1));
            }
            break;

            case "Queen":{
                for(int i = 1; i <=7; i++){
                    if(locationX + i <=7 && locationY + i <=7)
                        locations.add("box"+Integer.toString( locationX + i)+ Integer.toString( locationY + i));

                    if(locationX - i >= 0 && locationY - i >= 0)
                        locations.add("box"+Integer.toString( locationX - i)+ Integer.toString( locationY - i));

                    if(locationX + i <= 7 && locationY - i >= 0)
                        locations.add("box"+Integer.toString( locationX + i)+ Integer.toString( locationY - i));

                    if(locationX - i >= 0 && locationY + i <= 7)
                        locations.add("box"+Integer.toString( locationX - i)+ Integer.toString( locationY + i));

                    if(locationX + i <=7)
                        locations.add("box"+Integer.toString( locationX + i)+ Integer.toString( locationY));

                    if(locationY + i <= 7)
                        locations.add("box"+Integer.toString( locationX)+ Integer.toString( locationY + i));

                    if (locationX - i >= 0)
                        locations.add("box"+Integer.toString( locationX - i)+ Integer.toString( locationY));

                    if(locationY - i >= 0)
                        locations.add("box"+Integer.toString( locationX )+ Integer.toString( locationY - i));
                }
            }
            break;

            default:{
                if(chessPiece.contains("black") && chessPiece.contains("pawn")){
                    if(locationX + 1 <=7)
                        locations.add("box"+ String.valueOf(locationX + 1) + Integer.toString( locationY));

                    if(locationX == 1){
                        locations.add("box"+Integer.toString( locationX + 2)+ Integer.toString( locationY));
                    }
                }
                if(chessPiece.contains("white") && chessPiece.contains("pawn")){
                    if(locationX - 1 >=0)
                        locations.add("box"+ String.valueOf(locationX - 1) + Integer.toString( locationY));

                    if(locationX == 6){
                        locations.add("box"+Integer.toString( locationX - 2)+ Integer.toString( locationY));
                    }
                }
            }
            break;
        }

        return locations;
    }
}