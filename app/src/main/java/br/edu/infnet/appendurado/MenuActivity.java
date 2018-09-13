package br.edu.infnet.appendurado;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static com.google.android.gms.ads.AdRequest.ERROR_CODE_INTERNAL_ERROR;
import static com.google.android.gms.ads.AdRequest.ERROR_CODE_INVALID_REQUEST;
import static com.google.android.gms.ads.AdRequest.ERROR_CODE_NETWORK_ERROR;
import static com.google.android.gms.ads.AdRequest.ERROR_CODE_NO_FILL;

public class MenuActivity extends AppCompatActivity {


    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        MobileAds.initialize(this,
                "ca-app-pub-5837960030724452~1935876669");

        mAdView = findViewById(R.id.adView);

        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                TextView textView = findViewById(R.id.debug_text);
                switch (errorCode){
                    case ERROR_CODE_INTERNAL_ERROR:
                        textView.setText("ERROR_CODE_INTERNAL_ERROR");
                        break;
                    case ERROR_CODE_INVALID_REQUEST:
                        textView.setText("ERROR_CODE_INVALID_REQUEST");
                        break;
                    case ERROR_CODE_NETWORK_ERROR:
                        textView.setText("ERROR_CODE_NETWORK_ERROR");
                        break;
                    case ERROR_CODE_NO_FILL:
                        textView.setText("ERROR_CODE_NO_FILL");
                        break;
                }
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void startGame(View botao){
        EditText wordField = findViewById(R.id.palavra_escolhida);
        String word = wordField.getText().toString().toUpperCase();

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("palavra", word);
        startActivity(intent);
    }

    public void showProductsList(View view){
        startActivity(new Intent(this, ProductListActivity.class));
    }
}
