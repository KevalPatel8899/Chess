package com.example.chess;

import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ChessPiece{

    String chessPieceLocation, chessPiece;

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
            case "knight":{

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

            case "bishop" : {
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

            case"rook":{
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
            case"king":{
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

            case "queen":{
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
                String move = "";
//                if(chessPiece.contains("black") && chessPiece.contains("pawn")){
//                    MainActivity mainActivity = new MainActivity();
//                    if(locationX + 1 <=7){
//                        locations.add("box"+ String.valueOf(locationX + 1) + Integer.toString( locationY));
//                        move = "box"+ String.valueOf(locationX + 1) + Integer.toString( locationY+1);
//                        if(locationY+1 <=7) {
//                            if(mainActivity.moveChecker(move)){
//                                locations.add(move);
//                            }
//                        }
//                        move = "box"+ (locationX + 1) + Integer.toString( locationY-1);
//                        if(locationY - 1 >=0){
//                                if(mainActivity.moveChecker(move)){
//                                    locations.add(move);
//                                }
//                        }
//                    }
//                    if(locationX == 1){
//                        locations.add("box"+Integer.toString( locationX + 2)+ Integer.toString( locationY));
//                    }
//
//                }
//                if(chessPiece.contains("white") && chessPiece.contains("pawn")){
//                    MainActivity mainActivity = new MainActivity();
//                    if(locationX - 1 >=0)
//                        locations.add("box"+ String.valueOf(locationX - 1) + Integer.toString( locationY));
//
//                    if(locationX == 6){
//                        locations.add("box"+Integer.toString( locationX - 2)+ Integer.toString( locationY));
//                    }
//                }
            }
            break;
        }
        return locations;
    }
}