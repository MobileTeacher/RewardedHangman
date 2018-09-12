package br.edu.infnet.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;

public class MenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void startGame(View botao){
        android.widget.EditText wordField = findViewById(R.id.palavra_escolhida);
        String word = wordField.getText().toString().toUpperCase();

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("palavra", word);
        startActivity(intent);
    }
}
