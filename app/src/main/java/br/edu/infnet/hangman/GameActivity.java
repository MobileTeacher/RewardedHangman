package br.edu.infnet.hangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class GameActivity extends AppCompatActivity
        implements RewardedVideoAdListener {

    private RewardedVideoAd rewardedVideoAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
}
