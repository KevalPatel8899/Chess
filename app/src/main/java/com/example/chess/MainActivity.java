package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnDragListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView black_1_knight, black_2_knight, black_1_rook, black_2_rook, black_1_bishop, black_2_bishop, black_0_king, black_0_queen, black_1_pawn, black_2_pawn, black_3_pawn, black_4_pawn, black_5_pawn, black_6_pawn, black_7_pawn, black_0_pawn;

    private static final String IMAGE_VIEW_TAG = "LAUNCHER LOGO";
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        implementEvents(black_0_king);
        implementEvents(black_0_queen);
        implementEvents(black_1_bishop);
        implementEvents(black_2_bishop);
        implementEvents(black_1_knight);
        implementEvents(black_2_knight);
        implementEvents(black_1_rook);
        implementEvents(black_2_rook);

        implementEvents(black_0_pawn);
        implementEvents(black_1_pawn);
        implementEvents(black_2_pawn);
        implementEvents(black_3_pawn);
        implementEvents(black_4_pawn);
        implementEvents(black_5_pawn);
        implementEvents(black_6_pawn);
        implementEvents(black_7_pawn);


    }

    //Find all views and set Tag to all draggable views
    private void findViews() {
        black_1_knight = (ImageView) findViewById(R.id.Black_1_Knight);
        black_2_knight = (ImageView) findViewById(R.id.Black_2_Knight);
        black_1_rook = (ImageView) findViewById(R.id.Black_1_Rook);
        black_2_rook = (ImageView) findViewById(R.id.Black_2_Rook);
        black_1_bishop = (ImageView) findViewById(R.id.Black_1_Bishop);
        black_2_bishop = (ImageView) findViewById(R.id.Black_2_Bishop);
        black_0_king = (ImageView) findViewById(R.id.Black_0_King);
        black_0_queen = (ImageView) findViewById(R.id.Black_0_Queen);
        black_1_pawn = (ImageView) findViewById(R.id.black_1_pawn);
        black_2_pawn = (ImageView) findViewById(R.id.black_2_pawn);
        black_3_pawn = (ImageView) findViewById(R.id.black_3_pawn);
        black_4_pawn = (ImageView) findViewById(R.id.black_4_pawn);
        black_5_pawn = (ImageView) findViewById(R.id.black_5_pawn);
        black_6_pawn = (ImageView) findViewById(R.id.black_6_pawn);
        black_7_pawn = (ImageView) findViewById(R.id.black_7_pawn);
        black_0_pawn = (ImageView) findViewById(R.id.black_0_pawn);

        black_2_rook.setTag(IMAGE_VIEW_TAG);
        black_2_bishop.setTag(IMAGE_VIEW_TAG);
        black_2_knight.setTag(IMAGE_VIEW_TAG);
        black_0_queen.setTag(IMAGE_VIEW_TAG);
        black_0_king.setTag(IMAGE_VIEW_TAG);
        black_1_bishop.setTag(IMAGE_VIEW_TAG);
        black_1_rook.setTag(IMAGE_VIEW_TAG);
        black_1_knight.setTag(IMAGE_VIEW_TAG);

        black_0_pawn.setTag(IMAGE_VIEW_TAG);
        black_1_pawn.setTag(IMAGE_VIEW_TAG);
        black_2_pawn.setTag(IMAGE_VIEW_TAG);
        black_3_pawn.setTag(IMAGE_VIEW_TAG);
        black_3_pawn.setTag(IMAGE_VIEW_TAG);
        black_4_pawn.setTag(IMAGE_VIEW_TAG);
        black_5_pawn.setTag(IMAGE_VIEW_TAG);
        black_6_pawn.setTag(IMAGE_VIEW_TAG);
        black_7_pawn.setTag(IMAGE_VIEW_TAG);

    }

    //Implement long click and drag listener
    private void implementEvents(ImageView imageView ) {
        //add or remove any view that you don't want to be dragged
          black_0_king.setOnLongClickListener(this);
          black_0_queen.setOnLongClickListener(this);
          black_1_knight.setOnLongClickListener(this);
          black_1_bishop.setOnLongClickListener(this);
          black_1_rook.setOnLongClickListener(this);
          black_2_knight.setOnLongClickListener(this);
          black_2_bishop.setOnLongClickListener(this);
          black_2_rook.setOnLongClickListener(this);
          black_0_pawn.setOnLongClickListener(this);
          black_1_pawn.setOnLongClickListener(this);
          black_2_pawn.setOnLongClickListener(this);
          black_3_pawn.setOnLongClickListener(this);
          black_4_pawn.setOnLongClickListener(this);
          black_5_pawn.setOnLongClickListener(this);
          black_6_pawn.setOnLongClickListener(this);
          black_7_pawn.setOnLongClickListener(this);


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

                // Returns true. DragEvent.getResult() will return true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                // Turns off any color tinting
                view.getBackground().clearColorFilter();

                // Invalidates the view to force a redraw
                view.invalidate();

                linearLayout = (LinearLayout) black_1_knight.getParent();
                String db = linearLayout.getResources().getResourceName(linearLayout.getId());
                String newDB = db.substring(db.indexOf('/') + 1);

                // Does a getResult(), and displays what happened.
                if (event.getResult())
                    Toast.makeText(this, "The drop was handled. " + newDB, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();

                // returns true; the value is ignored.
                return true;

            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }


}
