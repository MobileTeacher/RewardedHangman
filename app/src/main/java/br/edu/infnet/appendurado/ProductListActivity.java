package br.edu.infnet.appendurado;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductListActivity extends AppCompatActivity
        implements PurchasesUpdatedListener, OnBuyButtonClickListener{

    RecyclerView recyclerView;
    BillingClient billingClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.products_list);

        billingClient = BillingClient.newBuilder(this)
                            .setListener(this)
                            .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK){
                    Toast.makeText(ProductListActivity.this, "Conectou ao PLAY", Toast.LENGTH_SHORT).show();

                    List<String> skuList = new ArrayList<>();
                    skuList.add("dica01");
                    skuList.add("words_package01");
                    skuList.add("life_update01");

                    SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder()
                                                            .setSkusList(skuList)
                                                            .setType(BillingClient.SkuType.INAPP)
                                                            .build();

                    billingClient.querySkuDetailsAsync(skuDetailsParams, new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                            if (responseCode == BillingClient.BillingResponse.OK
                                    && skuDetailsList != null){
                                Toast.makeText(ProductListActivity.this,
                                                        "Lista: " + skuDetailsList.size(),
                                                        Toast.LENGTH_SHORT).show();
                                recyclerView.setLayoutManager(new LinearLayoutManager(ProductListActivity.this));
                                recyclerView.addItemDecoration(new DividerItemDecoration(
                                                                ProductListActivity.this,
                                                                    DividerItemDecoration.VERTICAL));
                                recyclerView.setAdapter(new ProductsAdapter(skuDetailsList, ProductListActivity.this));
                            } else {
                                Toast.makeText(ProductListActivity.this, "FALHOU LISTA", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(ProductListActivity.this, "DESCONECTOU do PLAY", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        TextView debugText = findViewById(R.id.purchases_debug);
        if (responseCode == BillingClient.BillingResponse.OK){
            String debug = "";
            for (Purchase purchase: purchases){
                debug += purchase.getSku() + " " + new Date(purchase.getPurchaseTime());
            }

            debugText.setText(debug);
        }
        else {
            debugText.setText("Xabu na compra");
        }
    }


    @Override
    public void onBuyClick(SkuDetails skuDetails) {
        BillingFlowParams params = BillingFlowParams.newBuilder()
                .setSku(skuDetails.getSku())
                .setType(skuDetails.getType())
                .build();
        billingClient.launchBillingFlow(this, params);
    }
}
