package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private Button PLAY_AGAINST_AI;
  private Button PLAY_WITH_FRIEND;

  final boolean AGAINST_AI = false;



  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    PLAY_AGAINST_AI =  findViewById(R.id.playAgainstAI_button);
    PLAY_WITH_FRIEND = findViewById(R.id.playWithFriend_button);

    PLAY_AGAINST_AI.setOnClickListener(new View.OnClickListener(){

      @Override
      public void onClick(View v) {
        OpenGameActivity(true);
      }
    });
    PLAY_WITH_FRIEND.setOnClickListener(new View.OnClickListener(){

      @Override
      public void onClick(View v) {
        OpenGameActivity(false);
      }
    });


  }

  private void OpenGameActivity(boolean AGAINST_AI) {
    Intent intent = new Intent(this, Game.class);
    intent.putExtra("AGAINST_AI",AGAINST_AI);
    startActivity(intent);
  }

  @Override
  public void onClick(View v) {

  }
}
