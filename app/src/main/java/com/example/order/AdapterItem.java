package com.example.order;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.MyHolder> {

    Context context;
    List<ModelItem> itemList;
    final FirebaseUser fItem = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Deliver");

    //constructor
    public AdapterItem(Context context, List<ModelItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout (row_user.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_item, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {
        //get data
        final String cName = itemList.get(i).getName();
        final String price = itemList.get(i).getPrice();
        final String status = itemList.get(i).getStatus();
        final String location = itemList.get(i).getAddress();
        final String date = itemList.get(i).getDate();
        final String contact = itemList.get(i).getContactNo();

        //set data
        myHolder.cName.setText(cName);
        myHolder.price.setText(price);
        myHolder.address.setText(location);
        myHolder.contactNo.setText(contact);
        myHolder.date.setText(date);
        myHolder.status.setText(status);

        //handle item click
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Odered By: " +cName, Toast.LENGTH_SHORT).show();
            }
        });

        //click to show delete dialog box
        myHolder.mDeleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Delete this order will delete permanently!!");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reff.child(fItem.getUid()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Order delete",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //failed, dismiss progress

                                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                    }
                });
                //cancel delete button
                builder.setNegativeButton("No", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                //create and show dialog
                builder.create().show();
            }
        });

        myHolder.mUpdate.setOnClickListener(new View.OnClickListener() {
//
            @Override
            public void onClick(View view) {
                final ModelItem modelItem = new ModelItem();

                //custom dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                 builder.setTitle("Update States"); //Update name or Update phone
                //set layout of dialog
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10, 10, 10, 10);
                //add Edit text
                final EditText editText = new EditText(context);
//                editText.setHint("Enter "+key);
                linearLayout.addView(editText);

                builder.setView(linearLayout);

                //add buttons in dialog
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //input text from edit text
                        String value = editText.getText().toString().trim();
                        Intent intent = new Intent(context,MainActivity.class);

                        intent.putExtra("States",value);


                        //validate
                        if (!TextUtils.isEmpty(value)){

                            HashMap<String, Object> result = new HashMap<>();
                            result.put("States", value);

                            reff.child("Delivery").updateChildren(result)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //updated, dissmiss progress

                                            Toast.makeText(context, "Updated...",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //failed, dismiss progress

                                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(context, "Please enter "+"States",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                //add button in dialog to cancel
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //create and show dialog
                builder.create().show();


            }
//
//
        });

        myHolder.mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reff = FirebaseDatabase.getInstance().getReference().child("Deliver");
                ModelItem modelItem = new ModelItem();

                HashMap<Object, String> hashMap = new HashMap<>();
                //put info in hashmap

                    hashMap.put("name", cName);
                    hashMap.put("address", location);
                    hashMap.put("phone", contact);
                    hashMap.put("price", price);
                    hashMap.put("state", "Delivered");
                    hashMap.put("date", date);

                    //Insert order to Deliver Table
                    reff.push().setValue(hashMap);
                    Toast.makeText(context, "Insert to Deliver Table",Toast.LENGTH_LONG).show();





            }
        });

    }




    @Override
    public int getItemCount() {
        return itemList.size();
    }




    //view holder class
    class MyHolder extends RecyclerView.ViewHolder {

        ImageView mDeleteIv;
        ImageView mUpdate, mDoneBtn;

        TextView cName, price, status, contactNo, address, date;


        public MyHolder(View itemView) {
            super(itemView);

            //init views
            mDeleteIv = itemView.findViewById(R.id.deleteIv);
            cName = itemView.findViewById(R.id.cName);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            contactNo = itemView.findViewById(R.id.contactNo);
            address = itemView.findViewById(R.id.locationTo);
            mUpdate = itemView.findViewById(R.id.updateBtn);
            mDoneBtn = itemView.findViewById(R.id.doneBtn);


        }
    }

}