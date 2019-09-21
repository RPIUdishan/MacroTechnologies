package com.example.macro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



    public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

        private Context mCtx;
        private List<CartItem> cartItemList;

        public CartItemAdapter(Context mCtx, List<CartItem> cartItemList) {
            this.mCtx = mCtx;
            this.cartItemList = cartItemList;
        }


        @Override
        public CartItemAdapter.CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(mCtx);
            View view=inflater.inflate(R.layout.cart_items_layout,null);
            CartItemAdapter.CartItemViewHolder holder=new CartItemAdapter.CartItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
            CartItem cartItem=cartItemList.get(position);
            holder.textViewName.setText("item name : "+ cartItem.getName());
            holder.textViewQuantity.setText(String.valueOf("quantities : "+cartItem.getQuantity()));
            holder.textViewPrice.setText(String.valueOf("price : "+cartItem.getPrice()));
            holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(cartItem.getImage()));

        }



        @Override
        public int getItemCount() {
            return cartItemList.size();
        }

        class CartItemViewHolder extends RecyclerView.ViewHolder{

            ImageView imageView;
            TextView textViewName,textViewQuantity,textViewPrice;
            public CartItemViewHolder (View itemView){
                super(itemView);

                imageView=itemView.findViewById(R.id.cart_product_imageView);
                textViewQuantity=itemView.findViewById(R.id.cart_product_quantity);
                textViewName=itemView.findViewById(R.id.cart_product_name);
                textViewPrice=itemView.findViewById(R.id.cart_product_price);




            }


        }


    }
