package com.example.chess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button PLAY_AGAINST_AI = findViewById(R.id.playAgainstAI_button);
    Button PLAY_WITH_FRIEND = findViewById(R.id.playWithFriend_button);

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
