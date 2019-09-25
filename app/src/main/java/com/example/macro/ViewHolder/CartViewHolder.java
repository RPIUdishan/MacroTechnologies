package com.example.macro.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macro.Interface.ItemClickListner;
import com.example.macro.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.product_name_cart);
        txtProductPrice = itemView.findViewById(R.id.product_price_cart);
        txtProductQuantity = itemView.findViewById(R.id.product_quantity_cart);
    }

    @Override
    public void onClick(View view) {

        itemClickListner.onClick(view, getAdapterPosition(), false);

    }


    public void setItemClickListner(ItemClickListner itemClickListner) {


        this.itemClickListner = itemClickListner;
    }
}
