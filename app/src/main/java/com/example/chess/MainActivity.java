package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener, View.OnDragListener {

    //    private static final String TAG = MainActivity.class.getSimpleName();
    List<ImageView> IMAGE_VIEW_LIST = new ArrayList<>();
    List<ImageView> IMAGE_VIEW_WITH_KILL_EVENTS = new ArrayList<>();

    public static final String IMAGE_VIEW_TAG = "LAUNCHER LOGO";
    public boolean WHITE_TURN = true;
    public List<LinearLayout> PIECE_POSSIBLE_MOVES = new ArrayList<>();
    ImageView PIECE;

    public final String[] CHESS_PIECE_LIST = {"black_1_knight", "black_0_knight",
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
    String[] ORIGINAL_LOCATION = new String[32];
    LinearLayout KILLED_WHITE_PIECE , KILLED_BLACK_PIECE;

    LinearLayout[] LAST_MOVE = new LinearLayout[2];
    public Hashtable<String, String> locationTable = new Hashtable<>();

    @SuppressLint("CutPasteId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int index = 0;
        for (String chessPiece : CHESS_PIECE_LIST) {
            int resID = getResources().getIdentifier(chessPiece, "id", getPackageName());
            ImageView pieceIv = (ImageView) findViewById(resID);
            IMAGE_VIEW_LIST.add(pieceIv);

            findViewById(resID).setOnClickListener(this);
            LinearLayout l = (LinearLayout) pieceIv.getParent();
            ORIGINAL_LOCATION[index] = getBoxNameOFLayout(l);
            locationTable.put(chessPiece,ORIGINAL_LOCATION[index]);
            index++;

        }

        for (ImageView imageview : IMAGE_VIEW_LIST) {
            imageview.setTag(IMAGE_VIEW_TAG);
            implementEvents();
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });

        KILLED_WHITE_PIECE = findViewById(R.id.killedWhitePiece);
        KILLED_BLACK_PIECE = findViewById(R.id.killedBlackPiece);
    }

    public String getBoxNameOFLayout(LinearLayout l){
        String temp = l.getResources().getResourceName(l.getId());
        return temp.substring(temp.indexOf('/') + 1);
    }

    public void resetGame() {
        WHITE_TURN = true;
        for (int i = 0; i < 32; i++) {
            ImageView iv = IMAGE_VIEW_LIST.get(i);

            movePieceFromSrcToDest(iv, ORIGINAL_LOCATION[i]);
        }

        for(ImageView imageView : IMAGE_VIEW_LIST){
            imageView.setOnClickListener(this);
        }

        resetBackground();
    }

    //Implement long click and drag listener
    public void implementEvents() {
        //add or remove any view that you don't want to be dragged
        for (ImageView imageview : IMAGE_VIEW_LIST) {
            imageview.setOnLongClickListener(this);
        }
        //add or remove any layout view that you don't want to accept dragged view
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String box = "box" + i + j;
                int resID = getResources().getIdentifier(box, "id", getPackageName());
                findViewById(resID).setOnDragListener(this);
                findViewById(resID).setOnClickListener(onClickListenerForTiles);
            }
        }
    }


    @Override
    public boolean onLongClick(View view) {
        // Create a new ClipData.
        // This is done in two steps to provide clarity. The convenience method
        // ClipData.newPlainText() can create a plain text ClipData in one step.
        resetBackground();
        PIECE_POSSIBLE_MOVES = possibleMoves(view, false);


        String imageViewID = view.getResources().getResourceName(view.getId()).substring(view.getResources().getResourceName(view.getId()).indexOf('/') + 1);

        if ((WHITE_TURN && imageViewID.contains("black")) || (!WHITE_TURN && imageViewID.contains("white")))
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
                // if you want to apply color when drag started to your view you can uncomment below lines
                // to give any color tint to the View to indicate that it can accept
                // data.
                //  view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);//set background color to your view
                // Invalidate the view to force a redraw in the new tint
                //  view.invalidate();
                // returns true to indicate that the View can accept the dragged data.
                return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

            // Returns false. During the current drag and drop operation, this View will
            // not receive events again until ACTION_DRAG_ENDED is sent.

            case DragEvent.ACTION_DRAG_ENTERED:
                // Applies a YELLOW or any color tint to the View, when the dragged view entered into drag acceptable view
                // Return true; the return value is ignored.
                if (PIECE_POSSIBLE_MOVES.contains((LinearLayout) view)) {
                    view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                }

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
//                ClipData.Item item = event.getClipData().getItemAt(0);

                // Gets the text data from the item.
//                String dragData = item.getText().toString();

                // Displays a message containing the dragged data.
//                Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

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
                if (!PIECE_POSSIBLE_MOVES.contains(container)) {
                    container.removeView(v);
                    owner.addView(v);
                    v.setVisibility(View.VISIBLE);
                } else {
                    // if dropped at different location then change turn
                    if (!owner.equals(container))
                        WHITE_TURN = !WHITE_TURN;
                }
                PIECE_POSSIBLE_MOVES.clear();
                removeKilledPiece(container);

                check();

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
                playerAI();
                return true;

            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }

    public void playerAI() {
        if (!WHITE_TURN) {
            Random rand = new Random();
            String randomPiece;
            while (true) {
                randomPiece = CHESS_PIECE_LIST[rand.nextInt(32)];
                if (!randomPiece.contains("black")) continue;
                ImageView iv = (ImageView) getViewByName(randomPiece);
                PIECE_POSSIBLE_MOVES = possibleMoves(iv, false);
                int lengthOfPossibleMoves = PIECE_POSSIBLE_MOVES.size();
                if (lengthOfPossibleMoves == 0) continue;
                LinearLayout l = PIECE_POSSIBLE_MOVES.get(rand.nextInt(lengthOfPossibleMoves));
                LAST_MOVE[0] = (LinearLayout) iv.getParent();
                LAST_MOVE[1] = l;
                String dest = getBoxNameOFLayout(l);
                movePieceFromSrcToDest(iv, dest);


                setBackgroundOfLastMove(LAST_MOVE[0],"#FFFF99");
                setBackgroundOfLastMove(LAST_MOVE[1],"Yellow");
                break;
            }
            WHITE_TURN = true;
            check();
        }
    }

    public void setBackgroundOfLastMove(LinearLayout l,String color){
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{12, 12, 12, 12, 12, 12, 12, 12});
        shape.setColor(Color.parseColor(color));
        shape.setStroke(6, Color.parseColor("Black"));
        l.setBackground(shape);
    }

    public boolean isPiecePresent(String boxXX) {
        LinearLayout linearLayout = findViewById(getResources().getIdentifier(boxXX, "id", getPackageName()));
        return linearLayout.getChildCount() > 0;
    }

    public View getViewByName(String name) {
        int id = getResources().getIdentifier(name, "id", getPackageName());
        return findViewById(id);
    }

    public String getChessPieceLocation(String chessPiece) {
        View pieceView = getViewByName(chessPiece);
        LinearLayout l = (LinearLayout) pieceView.getParent();
        return getBoxNameOFLayout(l);
    }

    //Returns a list of possible moves of a given piece type at location
    public List<String> chessMove(String chessPieceLocation, String chessPiece) {
        String locationXS = chessPieceLocation.substring(chessPieceLocation.length() - 2, chessPieceLocation.length() - 1);
        String locationYS = chessPieceLocation.substring(chessPieceLocation.length() - 1);
        List<String> locations = new ArrayList<>();
        int locationX  , locationY ;
        try {
            locationX = Integer.parseInt(locationXS);
            locationY = Integer.parseInt(locationYS);
        }catch (Exception e){
            System.err.println("Location is not an integer value");
            return locations;
        }
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
                                if (isPiecePresent(checkLocation)) flag1 = true;
                                locations.add(checkLocation);
                            }
                        if (locationX - i >= 0 && locationY - i >= 0)
                            if (!flag2) {
                                checkLocation = "box" + (locationX - i) + (locationY - i);
                                if (isPiecePresent(checkLocation)) flag2 = true;
                                locations.add(checkLocation);
                            }
                        if (locationX + i <= 7 && locationY - i >= 0)
                            if (!flag3) {
                                checkLocation = "box" + (locationX + i) + (locationY - i);
                                if (isPiecePresent(checkLocation)) flag3 = true;
                                locations.add(checkLocation);
                            }
                        if (locationX - i >= 0 && locationY + i <= 7)
                            if (!flag4) {
                                checkLocation = "box" + (locationX - i) + (locationY + i);
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
                                checkLocation = "box" + (locationX + i) + locationY;
                                if (isPiecePresent(checkLocation)) flag1 = true;
                                locations.add(checkLocation);
                            }
                        }
                        if (locationY + i <= 7) {
                            if (!flag2) {
                                checkLocation = "box" + locationX + (locationY + i);
                                if (isPiecePresent(checkLocation)) flag2 = true;
                                locations.add(checkLocation);
                            }
                        }
                        if (locationX - i >= 0) {
                            if (!flag3) {
                                checkLocation = "box" + (locationX - i) + locationY;
                                if (isPiecePresent(checkLocation)) flag3 = true;
                                locations.add(checkLocation);
                            }
                        }
                        if (locationY - i >= 0) {
                            if (!flag4) {
                                checkLocation = "box" + (locationX) + (locationY - i);
                                if (isPiecePresent(checkLocation)) flag4 = true;
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
                                if (isPiecePresent(checkLocation)) flag1 = true;
                                locations.add(checkLocation);
                            }

                        if (locationX - i >= 0 && locationY - i >= 0)
                            if (!flag2) {
                                checkLocation = "box" + (locationX - i) + (locationY - i);
                                if (isPiecePresent(checkLocation)) flag2 = true;
                                locations.add(checkLocation);
                            }
                        if (locationX + i <= 7 && locationY - i >= 0)
                            if (!flag3) {
                                checkLocation = "box" + (locationX + i) + (locationY - i);
                                if (isPiecePresent(checkLocation)) flag3 = true;
                                locations.add(checkLocation);
                            }

                        if (locationX - i >= 0 && locationY + i <= 7)
                            if (!flag4) {
                                checkLocation = "box" + (locationX - i) + (locationY + i);
                                if (isPiecePresent(checkLocation)) flag4 = true;
                                locations.add(checkLocation);
                            }

                        if (locationX + i <= 7)
                            if (!flag5) {
                                checkLocation = "box" + (locationX + i) + (locationY);
                                if (isPiecePresent(checkLocation)) flag5 = true;
                                locations.add(checkLocation);
                            }

                        if (locationY + i <= 7)
                            if (!flag6) {
                                checkLocation = "box" + (locationX) + (locationY + i);
                                if (isPiecePresent(checkLocation)) flag6 = true;
                                locations.add(checkLocation);
                            }

                        if (locationX - i >= 0)
                            if (!flag7) {
                                checkLocation = "box" + (locationX - i) + (locationY);
                                if (isPiecePresent(checkLocation)) flag7 = true;
                                locations.add(checkLocation);
                            }

                        if (locationY - i >= 0)
                            if (!flag8) {
                                checkLocation = "box" + (locationX) + (locationY - i);
                                if (isPiecePresent(checkLocation)) flag8 = true;
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
                            if (!isPiecePresent(move)) {
                                locations.add(move);
                                move = "box" + (locationX + 2) + locationY;
                                if (locationX == 1 && !isPiecePresent(move)) {
                                    locations.add(move);
                                }
                            }

                            move = "box" + (locationX + 1) + (locationY + 1);
                            if (locationY + 1 <= 7) {
                                if (isPiecePresent(move)) {
                                    locations.add(move);
                                }
                            }
                            move = "box" + (locationX + 1) + (locationY - 1);
                            if (locationY - 1 >= 0) {
                                if (isPiecePresent(move)) {
                                    locations.add(move);
                                }
                            }
                        }
                    }

                    if (chessPiece.contains("white")) {
                        if (locationX - 1 >= 0) {
                            move = "box" + (locationX - 1) + locationY;
                            if (!isPiecePresent(move)) {
                                locations.add(move);
                                move = "box" + (locationX - 2) + locationY;
                                if (locationX == 6 && !isPiecePresent(move)) {
                                    locations.add(move);
                                }
                            }

                            move = "box" + (locationX - 1) + (locationY + 1);
                            if (locationY + 1 <= 7) {
                                if (isPiecePresent(move)) {
                                    locations.add(move);
                                }
                            }

                            move = "box" + (locationX - 1) + (locationY - 1);
                            if (locationY - 1 >= 0) {
                                if (isPiecePresent(move))
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

    public void removeOnClickEvent() {
        for (ImageView iv : IMAGE_VIEW_WITH_KILL_EVENTS) {
            iv.setOnClickListener(this);
        }
        IMAGE_VIEW_WITH_KILL_EVENTS.clear();
    }

    //On click event for chess piece
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        resetBackground();
        removeOnClickEvent();
        PIECE_POSSIBLE_MOVES = possibleMoves(v, false);
        PIECE = (ImageView) v;
        for (LinearLayout linearLayout : PIECE_POSSIBLE_MOVES) {
            customView(linearLayout);
            if (linearLayout.getChildCount() > 0) {
                ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                imageView.setOnClickListener(onClickListenerToKillChessPiece);
                IMAGE_VIEW_WITH_KILL_EVENTS.add(imageView);
            }
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

    public void resetBackground() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                View linearLayout;
                if ((x % 2 == 0 && y % 2 == 0) || (x % 2 != 0 && y % 2 != 0)) {
                    linearLayout = findViewById(getResources().getIdentifier("box" + x + y, "id", getPackageName()));
                    linearLayout.setBackgroundResource(R.color.colorBoardDark);
                } else {
                    linearLayout = findViewById(getResources().getIdentifier("box" + x + y, "id", getPackageName()));
                    linearLayout.setBackgroundResource(R.color.colorBoardLight);
                }
            }
        }
    }

    View.OnClickListener onClickListenerForTiles = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetBackground();
            if (PIECE_POSSIBLE_MOVES.size() > 0) {
                if (PIECE_POSSIBLE_MOVES.contains((LinearLayout) v)) {
                    if (PIECE != null) {
                        LinearLayout parentLayout = (LinearLayout) PIECE.getParent();
                        parentLayout.removeView(PIECE);
                        LinearLayout linearLayout = (LinearLayout) v;
                        linearLayout.addView(PIECE);
                        PIECE_POSSIBLE_MOVES.clear();
                        PIECE = null;
                        WHITE_TURN = !WHITE_TURN;
                        removeOnClickEvent();
                        check();
                        playerAI();
                    }
                }
            }
        }
    };

    View.OnClickListener onClickListenerToKillChessPiece = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetBackground();
            LinearLayout parentLayout = (LinearLayout) v.getParent();
            if (PIECE != null) {
                LinearLayout chessPieceParentLayout = (LinearLayout) PIECE.getParent();
                parentLayout.removeView(v);
                chessPieceParentLayout.removeView(PIECE);
                parentLayout.addView(PIECE);
                PIECE = null;
                PIECE_POSSIBLE_MOVES.clear();
                killedPieceRemoval((ImageView) v);
                WHITE_TURN = !WHITE_TURN;
                removeOnClickEvent();
                check();
                playerAI();
            }
        }
    };

    ///Returns List of linearLayouts that are valid for the given view.
    public List<LinearLayout> possibleMoves(View v, boolean virtualCall) {
        List<LinearLayout> ret = new ArrayList<>();
        List<LinearLayout> finalRet = new ArrayList<>();
        if (v == null) return ret;
        String imageViewID = v.getResources().getResourceName(v.getId()).substring(v.getResources().getResourceName(v.getId()).indexOf('/') + 1);
        LinearLayout linearLayout = (LinearLayout) v.getParent();
        String imageViewLocation = getBoxNameOFLayout(linearLayout);

        ImageView imageView;
        if (imageViewID.contains("white") && WHITE_TURN) {
            for (String move : chessMove(imageViewLocation, imageViewID)) {
                linearLayout = findViewById(getResources().getIdentifier(move, "id", getPackageName()));
                if (!isPiecePresent(move)) {
                    ret.add(linearLayout);
                } else {
                    imageView = (ImageView) linearLayout.getChildAt(0);
                    String childName = imageView.getResources().getResourceName(imageView.getId());

                    if (childName.contains("black")) {
                        ret.add(linearLayout);
                    }
                }
            }
        } else if (imageViewID.contains("black") && !WHITE_TURN) {
            for (String move : chessMove(imageViewLocation, imageViewID)) {
                linearLayout = findViewById(getResources().getIdentifier(move, "id", getPackageName()));
                if (!isPiecePresent(move)) {
                    ret.add(linearLayout);
                } else {
                    imageView = (ImageView) linearLayout.getChildAt(0);
                    String childName = imageView.getResources().getResourceName(imageView.getId());
                    if (childName.contains("white")) {
                        ret.add(linearLayout);
                    }
                }
            }
        }
        if (virtualCall) return ret;
        for (LinearLayout destLl : ret) {
            String dest = getBoxNameOFLayout(destLl);
            if (!isMoveCreatingCheckForSelf(imageViewID, imageViewLocation, dest))
                finalRet.add(destLl);
        }
        return finalRet;
    }

    public ImageView removeKilledPiece(LinearLayout linearLayout) {
        if (linearLayout.getChildCount() > 1) {
            ImageView iv = (ImageView) linearLayout.getChildAt(0);
            linearLayout.removeView(linearLayout.getChildAt(0));
            killedPieceRemoval(iv);
            return iv;
        }
        return null;
    }


    public ImageView movePieceFromSrcToDest(ImageView pieceIv, String dest) {

        int destResId = getResources().getIdentifier(dest, "id", getPackageName());
        LinearLayout destLinearLayout = findViewById(destResId);

        LinearLayout l = (LinearLayout) pieceIv.getParent();
        if (l != null) {
            l.removeView(pieceIv);
        }
        destLinearLayout.addView(pieceIv);
        pieceIv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        return removeKilledPiece(destLinearLayout);
    }

    public boolean isMoveCreatingCheckForSelf(String piece, String src, String dest) {
        ImageView pieceIv = (ImageView) getViewByName(piece);
        // temporarily move the piece from src to dest
        ImageView killedPieceIfAny = movePieceFromSrcToDest(pieceIv, dest);

        // check if current piece turn king can be killed by opponent
        boolean ret = IsCurrentKingInPositionOfOpponentAnyPiecePossibleMove();

        // move back piece from dest to src
        movePieceFromSrcToDest(pieceIv, src);
        if (killedPieceIfAny != null) {
            movePieceFromSrcToDest(killedPieceIfAny, dest);
        }

        return ret;
    }

    public boolean IsCurrentKingInPositionOfOpponentAnyPiecePossibleMove() {
        ImageView x;

        if (WHITE_TURN)
            x = findViewById(R.id.white_0_king);
        else
            x = findViewById(R.id.black_0_king);

        // Linear Layout of the king
        LinearLayout l = (LinearLayout) x.getParent();

        //loop through all opponent pieces and return true if they can kill current turn's king
        String piecesColor = WHITE_TURN ? "black" : "white";

        for (String piece : CHESS_PIECE_LIST) {
            if (piece.contains(piecesColor)) {
                WHITE_TURN = !WHITE_TURN;
                if (possibleMoves(getViewByName(piece), true).contains(l)) {
                    WHITE_TURN = !WHITE_TURN;
                    return true;
                }
                WHITE_TURN = !WHITE_TURN;
            }
        }
        return false;
    }

    public void check() {
        if (IsCurrentKingInPositionOfOpponentAnyPiecePossibleMove()) {
            if (!isCheckmate())
                Toast.makeText(this, "Check !!!", Toast.LENGTH_SHORT).show();
        }else{
            draw();
        }

    }

    public boolean isCheckmate() {
        String piecesColor = WHITE_TURN ? "white" : "black";
        for (String piece : CHESS_PIECE_LIST) {
            if (piece.contains(piecesColor)) {
                if (possibleMoves(getViewByName(piece), false).size() > 0) {
                    return false;
                }
            }
        }
        String winner = (WHITE_TURN) ? "Black" : "White";

        for(ImageView imageView : IMAGE_VIEW_LIST){
            imageView.setOnClickListener(null);
        }

        Toast.makeText(this, "CHECKMATE !!! " + winner + "  Wins !!!", Toast.LENGTH_SHORT).show();
        WHITE_TURN = true;
        return true;
    }

    public void killedPieceRemoval(ImageView imageView){
        imageView.getLayoutParams().height = 70; // OR
        imageView.getLayoutParams().width = 70;
        if(!WHITE_TURN){
            KILLED_BLACK_PIECE.addView(imageView);
        }else{
            KILLED_WHITE_PIECE.addView(imageView);
        }
    }

    @SuppressLint("ShowToast")
    public void draw(){

        String side = "black";
        int imageViewID;
        ImageView v;

        if(WHITE_TURN){
            side = "white" ;
        }

        for(String piece: CHESS_PIECE_LIST){
            if(piece.contains(side)){
                imageViewID = getResources().getIdentifier(piece,"id",getPackageName());
                v = findViewById(imageViewID);

                if(possibleMoves(v, false).size() != 0){
                    return;
                }
            }
        }

        for(ImageView imageView : IMAGE_VIEW_LIST){
            imageView.setOnClickListener(null);
        }


        Toast.makeText(this, "Draw", Toast.LENGTH_SHORT);

    }


}
