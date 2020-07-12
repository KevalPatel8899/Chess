package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
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

public class MainActivity extends AppCompatActivity
        implements View.OnLongClickListener, View.OnClickListener, View.OnDragListener {

    final boolean AGAINST_AI = false;

    //    private static final String TAG = MainActivity.class.getSimpleName();
    List<ImageView> IMAGE_VIEW_LIST = new ArrayList<>();
    List<ImageView> IMAGE_VIEW_WITH_KILL_EVENTS = new ArrayList<>();

    MediaPlayer MOVE_SOUND = new MediaPlayer(), KILLED_SOUND = new MediaPlayer();

    public static final String IMAGE_VIEW_TAG = "LAUNCHER LOGO";
    public boolean WHITE_TURN = true;
    public List<LinearLayout> PIECE_POSSIBLE_MOVES = new ArrayList<>();
    public List<String> PIECE_POSSIBLE_MOVES_S = new ArrayList<>();
    ImageView PIECE;
    public final String[] CHESS_PIECE_LIST = {
            "black_1_knight",
            "black_0_knight",
            "black_1_rook",
            "black_0_rook",
            "black_1_bishop",
            "black_0_bishop",
            "black_0_king",
            "black_0_queen",
            "black_0_pawn",
            "black_1_pawn",
            "black_2_pawn",
            "black_3_pawn",
            "black_4_pawn",
            "black_5_pawn",
            "black_6_pawn",
            "black_7_pawn",
            "white_1_knight",
            "white_0_knight",
            "white_1_rook",
            "white_0_rook",
            "white_1_bishop",
            "white_0_bishop",
            "white_0_king",
            "white_0_queen",
            "white_0_pawn",
            "white_1_pawn",
            "white_2_pawn",
            "white_3_pawn",
            "white_4_pawn",
            "white_5_pawn",
            "white_6_pawn",
            "white_7_pawn"
    };

    String[] ORIGINAL_LOCATION = new String[32];
    LinearLayout KILLED_WHITE_PIECE_CONTAINER, KILLED_BLACK_PIECE_CONTAINER;

    LinearLayout[] LAST_MOVE = new LinearLayout[2];
    public Hashtable<String, String> LOCATION_TABLE = new Hashtable<>();

    ChessLogic Game1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int index = 0;
        for (String chessPiece : CHESS_PIECE_LIST) {
            ImageView pieceIv = (ImageView) getViewByName(chessPiece);
            IMAGE_VIEW_LIST.add(pieceIv);
            pieceIv.setOnClickListener(this);
            LinearLayout l = (LinearLayout) pieceIv.getParent();
            ORIGINAL_LOCATION[index] = getBoxNameOFLayout(l);
            LOCATION_TABLE.put(chessPiece, ORIGINAL_LOCATION[index]);
            index++;
        }

        for (ImageView imageview : IMAGE_VIEW_LIST) {
            imageview.setTag(IMAGE_VIEW_TAG);
            implementEvents();
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resetGame();
                    }
                });

        KILLED_WHITE_PIECE_CONTAINER = findViewById(R.id.killedWhitePiece);
        KILLED_BLACK_PIECE_CONTAINER = findViewById(R.id.killedBlackPiece);

        Game1 = new ChessLogic(LOCATION_TABLE);
        MOVE_SOUND = MediaPlayer.create(this, R.raw.chess_move_audio);
        KILLED_SOUND = MediaPlayer.create(this, R.raw.chess_killed_piece);

    }

    // Implement long click and drag listener
    public void implementEvents() {
        // add or remove any view that you don't want to be dragged
        for (ImageView imageview : IMAGE_VIEW_LIST) {
            imageview.setOnLongClickListener(this);
        }
        // add or remove any layout view that you don't want to accept dragged view
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

        String chessPiece =
                view.getResources()
                        .getResourceName(view.getId())
                        .substring(view.getResources().getResourceName(view.getId()).indexOf('/') + 1);

        if ((WHITE_TURN && chessPiece.contains("black"))
                || (!WHITE_TURN && chessPiece.contains("white"))) return false;

        PIECE_POSSIBLE_MOVES_S = Game1.possibleMoves(chessPiece, false);
        PIECE_POSSIBLE_MOVES.clear();
        for (String move : PIECE_POSSIBLE_MOVES_S) {
            PIECE_POSSIBLE_MOVES.add(
                    (LinearLayout) findViewById(getResources().getIdentifier(move, "id", getPackageName())));
        }
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
        view.startDrag(
                data // data to be dragged
                ,
                shadowBuilder // drag shadow
                ,
                view // local data about the drag and drop operation
                ,
                0 // no needed flags
        );

        // Set view visibility to INVISIBLE as we are going to drag the view
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
                //  view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);//set background
                // color to your view
                // Invalidate the view to force a redraw in the new tint
                //  view.invalidate();
                // returns true to indicate that the View can accept the dragged data.
                return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

            // Returns false. During the current drag and drop operation, this View will
            // not receive events again until ACTION_DRAG_ENDED is sent.

            case DragEvent.ACTION_DRAG_ENTERED:
                // Applies a YELLOW or any color tint to the View, when the dragged view entered into drag
                // acceptable view
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
                // Re-sets the color tint to blue, if you had set the BLUE color or any color in
                // ACTION_DRAG_STARTED. Returns true; the return value is ignored.

                //  view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

                // If u had not provided any color in ACTION_DRAG_STARTED then clear color filter.
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
                //                Toast.makeText(this, "Dragged data is " + dragData,
                // Toast.LENGTH_SHORT).show();

                // Turns off any color tints
                view.getBackground().clearColorFilter();

                // Invalidates the view to force a redraw
                view.invalidate();

                View v = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) v.getParent();
                owner.removeView(v); // remove the dragged view
                LinearLayout container =
                        (LinearLayout)
                                view; // caste the view into LinearLayout as our drag acceptable layout is
                // LinearLayout
                container.addView(v); // Add the dragged view
                v.setVisibility(View.VISIBLE); // finally set Visibility to VISIBLE

                // if dropped at illegal position then replace the piece to its original position
                if (!PIECE_POSSIBLE_MOVES.contains(container)) {
                    container.removeView(v);
                    owner.addView(v);
                    v.setVisibility(View.VISIBLE);
                } else {
                    // if dropped at different location then change turn
                    if (!owner.equals(container)) {
                        String chessPiece = getChessPieceName(v);
                        String ownerLocation = getBoxNameOFLayout((LinearLayout) owner);
                        String destLocation = getBoxNameOFLayout(container);

                        movePieceFromSrcToDest((ImageView) v, destLocation);
                        postMoveSteps(chessPiece, ownerLocation, destLocation);
                    }
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
                //                    Toast.makeText(this, imageViewID + " " + imageViewLocation,
                // Toast.LENGTH_SHORT).show();
                //                else
                //                    Toast.makeText(this, "The drop didn't work.",
                // Toast.LENGTH_SHORT).show();

                // returns true; the value is ignored.

                if (AGAINST_AI) playerAI();

                return true;

            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }

    private String getChessPieceName(View v) {
        String temp = v.getResources().getResourceName(v.getId());
        return temp.substring(temp.indexOf('/') + 1);
    }

    // On click event for chess piece
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        resetBackground();
        removeOnClickEvent();

        String chessPiece = getChessPieceName(v);

        PIECE_POSSIBLE_MOVES_S = Game1.possibleMoves(chessPiece, false);
        PIECE_POSSIBLE_MOVES.clear();
        for (String move : PIECE_POSSIBLE_MOVES_S)
            PIECE_POSSIBLE_MOVES.add(
                    (LinearLayout) findViewById(getResources().getIdentifier(move, "id", getPackageName())));

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

    View.OnClickListener onClickListenerForTiles =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetBackground();
                    if (PIECE_POSSIBLE_MOVES.size() > 0) {
                        if (PIECE_POSSIBLE_MOVES.contains((LinearLayout) v)) {
                            if (PIECE != null) {
                                MOVE_SOUND.start();
                                LinearLayout owner = (LinearLayout) PIECE.getParent();
                                owner.removeView(PIECE);
                                LinearLayout container = (LinearLayout) v;
                                container.addView(PIECE);
                                String chessPiece = getChessPieceName(PIECE);
                                String ownerLocation = getBoxNameOFLayout(owner);
                                String destLocation = getBoxNameOFLayout(container);

                                postMoveSteps(chessPiece, ownerLocation, destLocation);

                                PIECE_POSSIBLE_MOVES.clear();
                                PIECE = null;
                                removeOnClickEvent();
                                if (AGAINST_AI) playerAI();
                            }
                        }
                    }
                }
            };

    View.OnClickListener onClickListenerToKillChessPiece =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetBackground();
                    LinearLayout parentLayout = (LinearLayout) v.getParent();
                    if (PIECE != null) {
                        KILLED_SOUND.start();
                        LinearLayout chessPieceParentLayout = (LinearLayout) PIECE.getParent();
                        parentLayout.removeView(v);
                        chessPieceParentLayout.removeView(PIECE);
                        parentLayout.addView(PIECE);

                        Game1.movePieceFromSrcToDest(
                                getChessPieceName(PIECE), getBoxNameOFLayout(parentLayout), false);

                        PIECE = null;
                        PIECE_POSSIBLE_MOVES.clear();
                        addPieceToKillContainer((ImageView) v);
                        WHITE_TURN = !WHITE_TURN;
                        Game1.WHITE_TURN = !Game1.WHITE_TURN;
                        removeOnClickEvent();
                        showCheckmateOrCheckOrDraw();
                        if (AGAINST_AI) playerAI();
                    }
                }
            };

    public void postMoveSteps(String chessPiece, String ownerLocation, String destLocation) {

        Game1.movePieceFromSrcToDest(chessPiece, destLocation, false);

        // check if the current move is implementation of enPassant
        if (!Game1.enPassant.equals("") && chessPiece.contains("pawn")) {
            checkIfEnPassantHappen(chessPiece, ownerLocation, destLocation);
        }

        // castle if needed
        if (chessPiece.contains("king")) moveRookIfCastle(chessPiece, ownerLocation, destLocation);

            // inform ChessLogic about pawn taking two steps
        else if (chessPiece.contains("pawn"))
            enableEnPassantIfPawn2Steps(chessPiece, ownerLocation, destLocation);
        else Game1.enPassant = "";

        WHITE_TURN = !WHITE_TURN;
        Game1.WHITE_TURN = !Game1.WHITE_TURN;

        // This has to be called after changing the turn
        showCheckmateOrCheckOrDraw();
    }

    private boolean showCheckmateOrCheckOrDraw() {
        if (Game1.isCheckmate()) {
            String winner = !WHITE_TURN ? "White" : "Black";
            gameEnds();
            Toast.makeText(this, "CHECKMATE!!!  " + winner + " Wins !!!", Toast.LENGTH_LONG).show();
        } else if (Game1.isCheck()) Toast.makeText(this, "CHECK !!!", Toast.LENGTH_SHORT).show();
        else if (Game1.isDraw()) Toast.makeText(this, "DRAW !!!", Toast.LENGTH_LONG).show();

        return false;
    }

    private void gameEnds() {
        for (int i = 0; i < 32; i++) {
            ImageView iv = IMAGE_VIEW_LIST.get(i);
            iv.setOnClickListener(null);
            iv.setOnLongClickListener(null);
        }
    }

    public String getBoxNameOFLayout(LinearLayout l) {
        String temp = l.getResources().getResourceName(l.getId());
        return temp.substring(temp.indexOf('/') + 1);
    }

    public void resetGame() {
        WHITE_TURN = true;
        Game1.resetGame(LOCATION_TABLE);

        for (int i = 0; i < 32; i++) {
            ImageView iv = IMAGE_VIEW_LIST.get(i);
            iv.setOnClickListener(this);
            iv.setOnLongClickListener(this);
            movePieceFromSrcToDest(iv, ORIGINAL_LOCATION[i]);
            iv.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
        resetBackground();
    }

    public void playerAI() {
        if (!WHITE_TURN) {
            if (Game1.isCheckmate() || Game1.isDraw()) {
                gameEnds();
                return;
            }

            Random rand = new Random();
            String randomPiece;
            String destLocation;
            ImageView iv;
            int lengthOfPossibleMoves;

            while (true) {
                randomPiece = CHESS_PIECE_LIST[rand.nextInt(32)];
                if (!randomPiece.contains("black")) continue;
                iv = (ImageView) getViewByName(randomPiece);

                PIECE_POSSIBLE_MOVES_S = Game1.possibleMoves(randomPiece, false);
                PIECE_POSSIBLE_MOVES.clear();
                for (String move : PIECE_POSSIBLE_MOVES_S) {
                    PIECE_POSSIBLE_MOVES.add(
                            (LinearLayout)
                                    findViewById(getResources().getIdentifier(move, "id", getPackageName())));
                }

                lengthOfPossibleMoves = PIECE_POSSIBLE_MOVES.size();
                if (lengthOfPossibleMoves != 0) break;
            }
            LinearLayout l = PIECE_POSSIBLE_MOVES.get(rand.nextInt(lengthOfPossibleMoves));
            LAST_MOVE[0] = (LinearLayout) iv.getParent();
            LAST_MOVE[1] = l;
            destLocation = getBoxNameOFLayout(l);
            movePieceFromSrcToDest(iv, destLocation);

            setBackgroundOfLastMove(LAST_MOVE[0], "#FFFF99");
            setBackgroundOfLastMove(LAST_MOVE[1], "Yellow");
            String ownerLocation = getBoxNameOFLayout(LAST_MOVE[0]);
            postMoveSteps(randomPiece, ownerLocation, destLocation);
        }
    }

    public void setBackgroundOfLastMove(LinearLayout l, String color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{12, 12, 12, 12, 12, 12, 12, 12});
        shape.setColor(Color.parseColor(color));
        shape.setStroke(6, Color.parseColor("Black"));
        l.setBackground(shape);
    }

    public boolean isPiecePresent(String boxXX) {
        LinearLayout linearLayout =
                findViewById(getResources().getIdentifier(boxXX, "id", getPackageName()));
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

    public void removeOnClickEvent() {
        for (ImageView iv : IMAGE_VIEW_WITH_KILL_EVENTS) iv.setOnClickListener(this);

        IMAGE_VIEW_WITH_KILL_EVENTS.clear();
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
                    linearLayout =
                            findViewById(getResources().getIdentifier("box" + x + y, "id", getPackageName()));
                    linearLayout.setBackgroundResource(R.color.colorBoardLight);
                } else {
                    linearLayout =
                            findViewById(getResources().getIdentifier("box" + x + y, "id", getPackageName()));
                    linearLayout.setBackgroundResource(R.color.colorBoardDark);
                }
            }
        }
    }

    public ImageView removeKilledPiece(LinearLayout linearLayout) {
        if (linearLayout.getChildCount() > 1) {
            KILLED_SOUND.start();

            ImageView iv = (ImageView) linearLayout.getChildAt(0);
            linearLayout.removeView(linearLayout.getChildAt(0));
            addPieceToKillContainer(iv);
            return iv;
        }

        MOVE_SOUND.start();
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

        return removeKilledPiece(destLinearLayout);
    }

    public void moveRookIfCastle(
            String kingChessPiece, String kingPrevLocation, String kingDestLocation) {

        if (Math.abs(
                Integer.parseInt(kingPrevLocation.substring(4))
                        - Integer.parseInt(kingDestLocation.substring(4)))
                == 2) {
            if (kingChessPiece.contains("white")) {
                if (kingDestLocation.equals("box72")) {
                    Game1.movePieceFromSrcToDest("white_0_rook", "box73", false);
                    movePieceFromSrcToDest((ImageView) getViewByName("white_0_rook"), "box73");
                } else {
                    Game1.movePieceFromSrcToDest("white_1_rook", "box75", false);
                    movePieceFromSrcToDest((ImageView) getViewByName("white_1_rook"), "box75");
                }
            } else {
                if (kingDestLocation.equals("box02")) {
                    Game1.movePieceFromSrcToDest("black_0_rook", "box03", false);
                    movePieceFromSrcToDest((ImageView) getViewByName("black_0_rook"), "box03");
                } else {
                    Game1.movePieceFromSrcToDest("black_1_rook", "box05", false);
                    movePieceFromSrcToDest((ImageView) getViewByName("black_1_rook"), "box05");
                }
            }
        }
    }

    public void enableEnPassantIfPawn2Steps(
            String pawnChessPiece, String pawnPrevLocation, String pawnDestLocation) {
        if (Math.abs(
                Integer.parseInt(pawnPrevLocation.substring(3, 4))
                        - Integer.parseInt(pawnDestLocation.substring(3, 4)))
                == 2) {
            Game1.enPassant = pawnDestLocation;
        } else Game1.enPassant = "";
    }

    public void checkIfEnPassantHappen(
            String pawnChessPiece, String pawnPrevLocation, String pawnDestLocation) {

        int enPassant_x = Integer.parseInt(Game1.enPassant.substring(3, 4));
        int enPassant_y = Integer.parseInt(Game1.enPassant.substring(4));
        // if pawn goes diagonal to the enPassant value
        if (enPassant_x == Integer.parseInt(pawnPrevLocation.substring(3, 4))
                && enPassant_y == Integer.parseInt(pawnDestLocation.substring(4))) {
            int resID = getResources().getIdentifier(Game1.enPassant, "id", getPackageName());
            LinearLayout l = (LinearLayout) findViewById(resID);
            ImageView iv = (ImageView) (l.getChildAt(0));

            String pawnToRemove = getChessPieceName(iv);
            l.removeView(iv);
            addPieceToKillContainer(iv);
            Game1.movePieceFromSrcToDest(pawnToRemove, "", false);
        }
        Game1.enPassant = "";
    }

    public void addPieceToKillContainer(ImageView iv) {
        iv.getLayoutParams().height = 70; // OR
        iv.getLayoutParams().width = 70;

        if (WHITE_TURN)
            KILLED_BLACK_PIECE_CONTAINER.addView(iv);
        else KILLED_WHITE_PIECE_CONTAINER.addView(iv);
    }


}
