package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.LinkAddress;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener, View.OnDragListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    List<ImageView> imageViewList = new ArrayList<>();
    List<String> chessMovesList, chessPiecePossibleMoves = new ArrayList<>();

    private static final String IMAGE_VIEW_TAG = "LAUNCHER LOGO";
    private LinearLayout linearLayout;
    private String imageViewID, imageViewLocation;
    private boolean whiteTurn = true;
    private List<LinearLayout> previousPossibleMoves = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] chessPieceList = {"black_1_knight", "black_0_knight",
                "black_1_rook", "black_0_rook",
                "black_1_bishop", "black_0_bishop",
                "black_0_king", "black_0_queen",
                "black_0_pawn", "black_1_pawn", "black_2_pawn", "black_3_pawn", "black_4_pawn", "black_5_pawn", "black_6_pawn", "black_7_pawn",
                "white_1_knight", "white_0_knight",
                "white_1_rook", "white_0_rook",
                "white_1_bishop", "white_0_bishop",
                "white_0_king", "white_0_queen",
                "white_0_pawn", "white_1_pawn", "white_2_pawn", "white_3_pawn", "white_4_pawn", "white_5_pawn", "white_6_pawn", "white_7_pawn"
        };
        for (int i = 0; i < chessPieceList.length; i++) {
            int resID = getResources().getIdentifier(chessPieceList[i], "id", getPackageName());
            imageViewList.add((ImageView) findViewById(resID));
            findViewById(resID).setOnClickListener(this);
        }

        for (ImageView imageview : imageViewList) {
            imageview.setTag(IMAGE_VIEW_TAG);
            implementEvents(imageview);
        }
    }


    //Implement long click and drag listener
    private void implementEvents(ImageView imageView) {
        //add or remove any view that you don't want to be dragged
        for (ImageView imageview : imageViewList) {
            imageview.setOnLongClickListener(this);
        }
        //add or remove any layout view that you don't want to accept dragged view
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String box = "box" + i + j;
                int resID = getResources().getIdentifier(box, "id", getPackageName());
                findViewById(resID).setOnDragListener(this);
            }
        }
    }


    @Override
    public boolean onLongClick(View view) {
        // Create a new ClipData.
        // This is done in two steps to provide clarity. The convenience method
        // ClipData.newPlainText() can create a plain text ClipData in one step.

        previousPossibleMoves = possibleMoves(view);

        imageViewID = view.getResources().getResourceName(view.getId()).substring(view.getResources().getResourceName(view.getId()).indexOf('/') + 1);

        if ((whiteTurn && imageViewID.contains("black")) || (!whiteTurn && imageViewID.contains("white")))
            return false;

        // Create a new ClipData.Item from the ImageView object's tag
        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);

        // Instantiates the drag shadow builder.
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

        // Starts the drag
        view.startDrag(data//data to be dragged
                , shadowBuilder //drag shadow
                , view//local data about the drag and drop operation
                , 0//no needed flags
        );

        //Set view visibility to INVISIBLE as we are going to drag the view
//        view.setVisibility(View.INVISIBLE);
        return true;
    }

    // This is the method that the system calls when it dispatches a drag event to the
    // listener.
    @Override
    public boolean onDrag(View view, DragEvent event) {
        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();
        // Handles each of the expected events
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    // if you want to apply color when drag started to your view you can uncomment below lines
                    // to give any color tint to the View to indicate that it can accept
                    // data.

                    //  view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);//set background color to your view

                    // Invalidate the view to force a redraw in the new tint
                    //  view.invalidate();

                    // returns true to indicate that the View can accept the dragged data.
                    return true;
                }

                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                // Applies a YELLOW or any color tint to the View, when the dragged view entered into drag acceptable view
                // Return true; the return value is ignored.
                view.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);

                // Invalidate the view to force a redraw in the new tint
                view.invalidate();

                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                // Re-sets the color tint to blue, if you had set the BLUE color or any color in ACTION_DRAG_STARTED. Returns true; the return value is ignored.

                //  view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

                //If u had not provided any color in ACTION_DRAG_STARTED then clear color filter.
                view.getBackground().clearColorFilter();
                // Invalidate the view to force a redraw in the new tint
                view.invalidate();

                return true;
            case DragEvent.ACTION_DROP:
                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);

                // Gets the text data from the item.
                String dragData = item.getText().toString();

                // Displays a message containing the dragged data.
                Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                // Turns off any color tints
                view.getBackground().clearColorFilter();

                // Invalidates the view to force a redraw
                view.invalidate();

                View v = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) v.getParent();
                owner.removeView(v);//remove the dragged view
                LinearLayout container = (LinearLayout) view;//caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                container.addView(v);//Add the dragged view
                v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE

                // if dropped at illegal position then replace the piece to its original position
                if(!previousPossibleMoves.contains(container)){
                    container.removeView(v);
                    owner.addView(v);
                    v.setVisibility(View.VISIBLE);
                }else{
                    // if dropped at different location then change turn
                    if (!owner.equals(container))
                        whiteTurn = !whiteTurn;
                }


                // Returns true. DragEvent.getResult() will return true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                // Turns off any color tinting
                view.getBackground().clearColorFilter();

                // Invalidates the view to force a redraw
                view.invalidate();

                // Does a getResult(), and displays what happened.
//                if (event.getResult())
//                    Toast.makeText(this, imageViewID + " " + imageViewLocation, Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();

                // returns true; the value is ignored.
                return true;

            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }

    public boolean isPiecePresent(String move) {
        LinearLayout linearLayout = findViewById(getResources().getIdentifier(move, "id", getPackageName()));
        if (linearLayout.getChildCount() > 0) {
            return true;
        }
        return false;
    }

    public List<String> chessMove(String chessPieceLocation, String chessPiece) {
        String locationS = chessPieceLocation.substring(chessPieceLocation.length() - 2);
        String locationXS = chessPieceLocation.substring(chessPieceLocation.length() - 2, chessPieceLocation.length() - 1);
        String locationYS = chessPieceLocation.substring(chessPieceLocation.length() - 1);
        int location = Integer.parseInt(locationS);
        List<String> locations = new ArrayList<>();
        int locationX = Integer.parseInt(locationXS);
        int locationY = Integer.parseInt(locationYS);
        locations.clear();
        String checkLocation = "";
        boolean flag1, flag2, flag3, flag4, flag5, flag6, flag7, flag8;
        flag1 = flag2 = flag3 = flag4 = flag5 = flag6 = flag7 = flag8 = false;

        switch (chessPiece.substring(8)) {
            case "knight": {

                if (locationX + 2 <= 7 && locationY + 1 <= 7)
                    locations.add("box" + Integer.toString(location + 21));
                if (locationX + 1 <= 7 && locationY + 2 <= 7)
                    locations.add("box" + Integer.toString(location + 12));
                if (locationX - 1 >= 0 && locationY - 2 >= 0) {
                    locations.add("box" + Integer.toString(location - 12));
                }
                if (locationX - 2 >= 0 && locationY - 1 >= 0) {
                    locations.add("box" + Integer.toString(location - 21));
                }


                if (locationX + 2 <= 7 && locationY - 1 >= 0)
                    locations.add("box" + Integer.toString(locationX + 2) + Integer.toString(locationY - 1));
                if (locationX + 1 <= 7 && locationY - 2 >= 0)
                    locations.add("box" + Integer.toString(locationX + 1) + Integer.toString(locationY - 2));
                if (locationX - 1 >= 0 && locationY + 2 <= 7) {
                    locations.add("box" + Integer.toString(locationX - 1) + Integer.toString(locationY + 2));
                }
                if (locationX - 2 >= 0 && locationY + 1 <= 7) {
                    locations.add("box" + Integer.toString(locationX - 2) + Integer.toString(locationY + 1));
                }
            }
            break;

            case "bishop": {
                for (int i = 1; i <= 7; i++) {
                    if (locationX + i <= 7 && locationY + i <= 7)
                        if (!flag1) {
                            checkLocation = "box" + Integer.toString(locationX + i) + Integer.toString(locationY + i);
                            if (isPiecePresent(checkLocation)) flag1 = true;
                            locations.add(checkLocation);
                        }
                    if (locationX - i >= 0 && locationY - i >= 0)
                        if (!flag2) {
                            checkLocation = "box" + Integer.toString(locationX - i) + Integer.toString(locationY - i);
                            if (isPiecePresent(checkLocation)) flag2 = true;
                            locations.add(checkLocation);
                        }
                    if (locationX + i <= 7 && locationY - i >= 0)
                        if (!flag3) {
                            checkLocation = "box" + Integer.toString(locationX + i) + Integer.toString(locationY - i);
                            if (isPiecePresent(checkLocation)) flag3 = true;
                            locations.add(checkLocation);
                        }
                    if (locationX - i >= 0 && locationY + i <= 7)
                        if (!flag4) {
                            checkLocation = "box" + Integer.toString(locationX - i) + Integer.toString(locationY + i);
                            if (isPiecePresent(checkLocation)) flag4 = true;
                            locations.add(checkLocation);
                        }
                }
            }
            break;

            case "rook": {

                for (int i = 1; i <= 7; i++) {
                    if (locationX + i <= 7) {
                        if (!flag1) {
                            checkLocation = "box" + Integer.toString(locationX + i) + locationY;
                            if (isPiecePresent(checkLocation)) flag1 = true;
                            locations.add(checkLocation);
                        }
                    }
                    if (locationY + i <= 7) {
                        if (!flag2) {
                            checkLocation = "box" + Integer.toString(locationX) + Integer.toString(locationY + i);
                            if (isPiecePresent(checkLocation)) flag2 = true;
                            locations.add(checkLocation);
                        }
                    }
                    if (locationX - i >= 0) {
                        if (!flag3) {
                            checkLocation = "box" + Integer.toString(locationX - i) + Integer.toString(locationY);
                            if (isPiecePresent(checkLocation)) flag3 = true;
                            locations.add(checkLocation);
                        }
                    }
                    if (locationY - i >= 0) {
                        if (!flag4) {
                            checkLocation = "box" + Integer.toString(locationX) + Integer.toString(locationY - i);
                            if (isPiecePresent(checkLocation)) flag4 = true;
                            locations.add(checkLocation);
                        }
                    }
                }
            }
            break;
            case "king": {
                if (locationX + 1 <= 7)
                    locations.add("box" + Integer.toString(locationX + 1) + Integer.toString(locationY));

                if (locationY + 1 <= 7)
                    locations.add("box" + Integer.toString(locationX) + Integer.toString(locationY + 1));

                if (locationX - 1 >= 0)
                    locations.add("box" + Integer.toString(locationX - 1) + Integer.toString(locationY));

                if (locationY - 1 >= 0)
                    locations.add("box" + Integer.toString(locationX) + Integer.toString(locationY - 1));

                if (locationX + 1 <= 7 && locationY + 1 <= 7)
                    locations.add("box" + Integer.toString(locationX + 1) + Integer.toString(locationY + 1));

                if (locationX - 1 >= 0 && locationY - 1 >= 0)
                    locations.add("box" + Integer.toString(locationX - 1) + Integer.toString(locationY - 1));

                if (locationX + 1 <= 7 && locationY - 1 >= 0)
                    locations.add("box" + Integer.toString(locationX + 1) + Integer.toString(locationY - 1));

                if (locationX - 1 >= 0 && locationY + 1 <= 7)
                    locations.add("box" + Integer.toString(locationX - 1) + Integer.toString(locationY + 1));
            }
            break;

            case "queen": {
                for (int i = 1; i <= 7; i++) {
                    if (locationX + i <= 7 && locationY + i <= 7)
                        if (!flag1) {
                            checkLocation = "box" + Integer.toString(locationX + i) + Integer.toString(locationY + i);
                            if (isPiecePresent(checkLocation)) flag1 = true;
                            locations.add(checkLocation);
                        }

                    if (locationX - i >= 0 && locationY - i >= 0)
                        if (!flag2) {
                            checkLocation = "box" + Integer.toString(locationX - i) + Integer.toString(locationY - i);
                            if (isPiecePresent(checkLocation)) flag2 = true;
                            locations.add(checkLocation);
                        }
                    if (locationX + i <= 7 && locationY - i >= 0)
                        if (!flag3) {
                            checkLocation = "box" + Integer.toString(locationX + i) + Integer.toString(locationY - i);
                            if (isPiecePresent(checkLocation)) flag3 = true;
                            locations.add(checkLocation);
                        }

                    if (locationX - i >= 0 && locationY + i <= 7)
                        if (!flag4) {
                            checkLocation = "box" + Integer.toString(locationX - i) + Integer.toString(locationY + i);
                            if (isPiecePresent(checkLocation)) flag4 = true;
                            locations.add(checkLocation);
                        }

                    if (locationX + i <= 7)
                        if (!flag5) {
                            checkLocation = "box" + Integer.toString(locationX + i) + Integer.toString(locationY);
                            if (isPiecePresent(checkLocation)) flag5 = true;
                            locations.add(checkLocation);
                        }

                    if (locationY + i <= 7)
                        if (!flag6) {
                            checkLocation = "box" + Integer.toString(locationX) + Integer.toString(locationY + i);
                            if (isPiecePresent(checkLocation)) flag6 = true;
                            locations.add(checkLocation);
                        }

                    if (locationX - i >= 0)
                        if (!flag7) {
                            checkLocation = "box" + Integer.toString(locationX - i) + Integer.toString(locationY);
                            if (isPiecePresent(checkLocation)) flag7 = true;
                            locations.add(checkLocation);
                        }

                    if (locationY - i >= 0)
                        if (!flag8) {
                            checkLocation = "box" + Integer.toString(locationX) + Integer.toString(locationY - i);
                            if (isPiecePresent(checkLocation)) flag8 = true;
                            locations.add(checkLocation);
                        }
                }
            }
            break;

            case "pawn": {
                String move = "";
                if (chessPiece.contains("black")) {
                    if (locationX + 1 <= 7) {
                        move = "box" + String.valueOf(locationX + 1) + Integer.toString(locationY);
                        if (!isPiecePresent(move)) {
                            locations.add(move);
                            if (locationX == 1) {
                                locations.add("box" + Integer.toString(locationX + 2) + Integer.toString(locationY));
                            }
                        }

                        move = "box" + String.valueOf(locationX + 1) + Integer.toString(locationY + 1);
                        if (locationY + 1 <= 7) {
                            if (isPiecePresent(move)) {
                                locations.add(move);
                            }
                        }
                        move = "box" + (locationX + 1) + Integer.toString(locationY - 1);
                        if (locationY - 1 >= 0) {
                            if (isPiecePresent(move)) {
                                locations.add(move);
                            }
                        }
                    }
                }

                if (chessPiece.contains("white")) {
                    if (locationX - 1 >= 0) {
                        move = "box" + String.valueOf(locationX - 1) + Integer.toString(locationY);
                        if (!isPiecePresent(move)) {
                            locations.add(move);
                            if (locationX == 6) {
                                locations.add("box" + Integer.toString(locationX - 2) + Integer.toString(locationY));
                            }
                        }

                        move = "box" + String.valueOf(locationX - 1) + Integer.toString(locationY + 1);
                        if (locationY + 1 <= 7) {
                            if (isPiecePresent(move)) {
                                locations.add(move);
                            }
                        }

                        move = "box" + String.valueOf(locationX - 1) + Integer.toString(locationY - 1);
                        if (locationY - 1 >= 0) {
                            if (isPiecePresent(move))
                                locations.add(move);
                        }
                    }
                }
            }
            break;
        }
        return locations;
    }

    //On click event for chess piece
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        for (LinearLayout linearLayout : possibleMoves(v)){
            customView(linearLayout);
        }

    }

    public static void customView(View v) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{12, 12, 12, 12, 12, 12, 12, 12});
        shape.setColor(Color.parseColor("Green"));
        shape.setStroke(6, Color.parseColor("Black"));
        v.setBackground(shape);
    }

    public List<LinearLayout> possibleMoves(View v){
        List<LinearLayout> ret = new ArrayList<>();
        String imageViewID = v.getResources().getResourceName(v.getId()).substring(v.getResources().getResourceName(v.getId()).indexOf('/') + 1);
        LinearLayout linearLayout = (LinearLayout) v.getParent();
        String imageViewLocation = linearLayout.getResources().getResourceName(linearLayout.getId()).substring(v.getResources().getResourceName(linearLayout.getId()).indexOf('/') + 1);

        if (chessPiecePossibleMoves.size() != 0) {
            for (int i = 0; i < chessPiecePossibleMoves.size(); i++) {
                String x = chessPiecePossibleMoves.get(i).substring(3, 4);
                String y = chessPiecePossibleMoves.get(i).substring(4);
                if ((Integer.parseInt(x) % 2 == 0 && Integer.parseInt(y) % 2 == 0) || (Integer.parseInt(x) % 2 != 0 && Integer.parseInt(y) % 2 != 0)) {
                    linearLayout = findViewById(getResources().getIdentifier(chessPiecePossibleMoves.get(i), "id", getPackageName()));
                    linearLayout.setBackgroundResource(R.color.colorBoardDark);
                } else {
                    linearLayout = findViewById(getResources().getIdentifier(chessPiecePossibleMoves.get(i), "id", getPackageName()));
                    linearLayout.setBackgroundResource(R.color.colorBoardLight);
                }
            }

            chessPiecePossibleMoves.clear();
        }

        ImageView imageView;
        if (imageViewID.contains("white") && whiteTurn) {
            for (String move : chessMove(imageViewLocation, imageViewID)) {
                linearLayout = findViewById(getResources().getIdentifier(move, "id", getPackageName()));
                if (!isPiecePresent(move)) {
                    chessPiecePossibleMoves.add(move);

                    //                linearLayout.setBackgroundResource(R.color.colorChessPieceMoves);
                    ret.add(linearLayout);
                    //              How to remove view from the layout
                    //              linearLayout.setVisibility(linearLayout.GONE);
                } else {
                    imageView = (ImageView) linearLayout.getChildAt(0);
                    String childName = imageView.getResources().getResourceName(imageView.getId());

                    if (childName.contains("black")) {
                        chessPiecePossibleMoves.add(move);
                        //                linearLayout.setBackgroundResource(R.color.colorChessPieceMoves);
                        ret.add(linearLayout);
                    }
                }
            }
        } else if (imageViewID.contains("black") && !whiteTurn) {
            for (String move : chessMove(imageViewLocation, imageViewID)) {
                linearLayout = findViewById(getResources().getIdentifier(move, "id", getPackageName()));
                if (!isPiecePresent(move)) {
                    chessPiecePossibleMoves.add(move);

                    //                linearLayout.setBackgroundResource(R.color.colorChessPieceMoves);
                    ret.add(linearLayout);
                } else {
                    imageView = (ImageView) linearLayout.getChildAt(0);
                    String childName = imageView.getResources().getResourceName(imageView.getId());
                    if (childName.contains("white")) {
                        chessPiecePossibleMoves.add(move);
                        // linearLayout.setBackgroundResource(R.color.colorChessPieceMoves);
                        ret.add(linearLayout);
                    }
                }
            }
        }
        return ret;
    }
}