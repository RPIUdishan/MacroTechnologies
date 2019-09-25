package com.example.macro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class SM_Add_Items extends AppCompatActivity  {

    EditText editTextPname, editTextPrice, editTextDescription, editTextCategory;
    Button  buttonSubmit;
    ImageView imageUpload;
    private String pName, price, description, category, saveCurrentDate, saveCurrentTime;
    private String productRandomKey, downloadImageUrl;
    private DatabaseReference productRef;
    private ProgressDialog loadingBar;

    private static final int GALLARY_PICK=1;
    private Uri ImageUri;
    private StorageReference productImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sm__add__items);

        loadingBar = new ProgressDialog(this);


        productImageRef = FirebaseStorage.getInstance().getReference().child("{ProductImages");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products_n");

        editTextPname = findViewById(R.id.editTxt_pName);
        editTextPrice = findViewById(R.id.editTxt_price);
        editTextDescription = findViewById(R.id.editTxt_description);
        editTextCategory = findViewById(R.id.editText_category);

        buttonSubmit = findViewById(R.id.btn_submit);
        imageUpload = findViewById(R.id.image_Upload);


        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallary();
            }
        });
        
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateItems();
            }
        });
    }

    private void validateItems() {
        category = editTextCategory.getText().toString();
        description = editTextDescription.getText().toString();
        price = editTextPrice.getText().toString();
        pName = editTextPname.getText().toString();

        if (TextUtils.isEmpty(category)){
            Toast.makeText(this,"Please write product category..",Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(pName)){
            Toast.makeText(this,"Please write product Name..",Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(price)){
            Toast.makeText(this,"Please enter price..",Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(description)){
            Toast.makeText(this,"Please write product description..",Toast.LENGTH_SHORT).show();
        }


        else if (imageUpload == null){
            Toast.makeText(this,"Product Image is mandatory..",Toast.LENGTH_SHORT).show();
        }


        else {
            storeProductInformation();
        }
    }

    private void storeProductInformation() {

        loadingBar.setTitle("Adding new products");
        loadingBar.setMessage("Please wait. while we are adding new products");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImageRef.child(ImageUri.getLastPathSegment() + productRandomKey+ ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg = e.toString();
                Toast.makeText(SM_Add_Items.this, "Error "+msg, Toast.LENGTH_LONG).show();

                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SM_Add_Items.this, "Product Image upload successful..",Toast.LENGTH_SHORT).show();

                Task<Uri> UrlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();

                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(SM_Add_Items.this,"Got the Product Image URL successfully..",Toast.LENGTH_SHORT).show();

                            saveProductInfoToDB();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDB() {

        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",category);
        productMap.put("price",price);
        productMap.put("pname",pName);

        productRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent1 = new Intent(SM_Add_Items.this, Store_Management.class);
                            startActivity(intent1);

                            loadingBar.dismiss();
                            Toast.makeText(SM_Add_Items.this,"Product added successfully..", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            loadingBar.dismiss();
                            String msg = task.getException().toString();
                            Toast.makeText(SM_Add_Items.this,"Error"+msg,Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void openGallary() {
        Intent gallaryIntent = new Intent();
        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
        gallaryIntent.setType("image/*");
        startActivityForResult(gallaryIntent, GALLARY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLARY_PICK && resultCode==RESULT_OK  &&  data!=null){
            ImageUri = data.getData();
            imageUpload.setImageURI(ImageUri);
        }

    }








}
