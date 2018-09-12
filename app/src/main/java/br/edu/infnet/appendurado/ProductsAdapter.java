package br.edu.infnet.appendurado;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>{

    List<SkuDetails> productsSkuDetailsList;
    OnBuyButtonClickListener buyListener;


    public ProductsAdapter(List<SkuDetails> productsSkuDetailsList, OnBuyButtonClickListener listener) {
        this.productsSkuDetailsList = productsSkuDetailsList;
        buyListener = listener;
    }

    @Override
    public ProductsAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_card_layout, parent, false);
        return new ProductViewHolder(card);
    }

    @Override
    public void onBindViewHolder(ProductsAdapter.ProductViewHolder holder, int position) {
        final SkuDetails skuDetails = productsSkuDetailsList.get(position);
        holder.productPrice.setText(skuDetails.getPrice());
        holder.productName.setText(skuDetails.getTitle());
        holder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buyListener != null){
                    buyListener.onBuyClick(skuDetails);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsSkuDetailsList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        public TextView productName;
        public TextView productPrice;
        public ImageView productImage;
        public Button buyButton;
        public ProductViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            buyButton = itemView.findViewById(R.id.buy_product_button);

        }
    }
}
