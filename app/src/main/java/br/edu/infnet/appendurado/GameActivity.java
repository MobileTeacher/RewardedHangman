package br.edu.infnet.appendurado;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity
        implements RewardedVideoAdListener {

    private RewardedVideoAd rewardedVideoAd;
    private String secretWord = "Activity";
    private char[] secretArray;
    private TextView secretTextView;
    private char[] hiddenText;
    private int chances = 7;
    private TextView counterTextView;
    private String letrasJaEscolhidas = "";

    private Button buyButton;

    private final String TAG = "DEBUG";
    private BillingClient billingClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        secretTextView = findViewById(R.id.secret_word);
        counterTextView = findViewById(R.id.counter);
        buyButton = findViewById(R.id.buy_hint_btn);

        // como calcular a hiddenText?
        Intent intent = getIntent();
        secretWord = intent.getStringExtra("palavra");
        // útil para as comparações
        secretArray = secretWord.toCharArray();

        Log.d(TAG, secretWord);

        int wordSize = secretWord.length();

        hiddenText = new char[wordSize];
        for (int i = 0; i < wordSize; i++) {
            //hiddenText = hiddenText + "_ ";
            hiddenText[i] = '_';
        }

        updateUI();

        // passo 2 inicialização do MobileAds SDK
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");

        // passo 3: inicialização de uma instância de RewardedVideoAd
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        // passo 4: configuração de um RewardedVideoAdLister
        rewardedVideoAd.setRewardedVideoAdListener(this);

        // passo 5: requisição de anúncios + configuração de adUnitId
        AdRequest adRequest = new AdRequest.Builder().build();
        rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917" , adRequest);


        billingClient = BillingClient.newBuilder(this)
                                    .setListener(new PurchasesUpdatedListener() {
                                        @Override
                                        public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

                                        }
                                    }).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                Toast.makeText(GameActivity.this, "VAMOS VER", Toast.LENGTH_SHORT).show();
                if (responseCode == BillingClient.BillingResponse.OK) {
                    buyButton.setEnabled(true);
                    List skuList = new ArrayList<>();
                    skuList.add("premium_upgrade");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(int responseCode, List skuDetailsList) {
                                    // Process the result.

                                }
                            });
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(GameActivity.this, "DISCONNECTED PLAY", Toast.LENGTH_SHORT).show();
            }



        });
    }

    public void showRewardedVideo(View view){
        // passo 6: exibe o anúncio
        rewardedVideoAd.show();
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "CARREGOU!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "Vídeo recompensa fechado", Toast.LENGTH_LONG).show();
        AdRequest adRequest = new AdRequest.Builder().build();
        rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917" , adRequest);
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this, "Recompensa: "
                + rewardItem.getAmount()
                + " " + rewardItem.getType(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(this, "COMPLETOU!", Toast.LENGTH_LONG).show();
    }

    public void chutarLetra(View view){
        EditText campoLetra = findViewById(R.id.campo_letra);
        char letra = campoLetra.getText().toString().toUpperCase().charAt(0);

        boolean acertou = false;
        for (int i = 0; i < secretArray.length; i++){
            if (letra == secretArray[i]){
                hiddenText[i] = letra;
                acertou = true;
            }
        }
        if (!acertou){
            chances = chances - 1;
        }

        campoLetra.setText("");
        letrasJaEscolhidas += letra + " ";
        updateUI();
    }

    public void buyHint(View view){
        BillingFlowParams params = BillingFlowParams.newBuilder()
                                    .setSku("dica01")
                                    .setType(BillingClient.SkuType.INAPP).build();
        int responseCode = billingClient.launchBillingFlow(this, params);
    }


    public void updateUI(){
        String withSpace = "";
        for (int i= 0; i < hiddenText.length; i++){
            withSpace += hiddenText[i] + " ";
        }
        secretTextView.setText(withSpace);
        counterTextView.setText(String.valueOf(chances));

        TextView textView = findViewById(R.id.ja_escolhidas);
        textView.setText(letrasJaEscolhidas);


        Intent intent = new Intent(this, ResultActivity.class);
        if (!(String.valueOf(hiddenText).contains("_"))){
            intent.putExtra("result", "Você venceu!");
            startActivity(intent);
        }

        if (chances == 0){
            intent.putExtra("result", "Você PERDEU!!!!");
            startActivity(intent);
        }
    }

}
