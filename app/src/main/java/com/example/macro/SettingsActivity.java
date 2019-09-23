package com.example.macro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macro.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText nameEditText, addressEditText,phoneEditText;
    private TextView profileChangeTextButton, closeTextButton, saveTextButton;


    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureReference;
    private String checker = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureReference = FirebaseStorage.getInstance().getReference().child("Profile picturesProfile pictures");

        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        nameEditText = (EditText)findViewById(R.id.settngs_user_name);
        addressEditText = (EditText) findViewById(R.id.settngs_address);
        phoneEditText=(EditText)findViewById(R.id.settngs_user_phone);

        profileChangeTextButton = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextButton = (TextView) findViewById(R.id.close_settings_btn);
        saveTextButton = (TextView) findViewById(R.id.update_settings_btn);


        customerInfoDisplay(profileImageView, nameEditText,  phoneEditText,addressEditText);

        closeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker == "clicked"){

                    userInfoSaved();

                }else{

                    updateOnlyUserInfo();

                }
            }
        });


        profileChangeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checker = "clicked";

                CropImage.activity(imageUri).setAspectRatio(1,1).start(SettingsActivity.this);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
        }else{

            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));

            finish();
        }

    }

    private void updateOnlyUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Customers");

        HashMap<String, Object> customerMap = new HashMap<>();

        customerMap.put("userName", nameEditText.getText().toString());
        customerMap.put("address", addressEditText.getText().toString());
        customerMap.put("phone",phoneEditText.getText().toString());

        ref.child(Prevalent.currentCustomer.getEmail()).updateChildren(customerMap);
        Prevalent.currentCustomer.setUserName(nameEditText.getText().toString());
        Prevalent.currentCustomer.setImage(myUrl);
        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Information Updated", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void userInfoSaved() {

        if (TextUtils.isEmpty(nameEditText.getText().toString())){

            Toast.makeText(this, "You should enter the name...", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(phoneEditText.getText().toString())){

            Toast.makeText(this, "You should enter the phone...", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(addressEditText.getText().toString())){

            Toast.makeText(this, "You should enter the address...", Toast.LENGTH_SHORT).show();

        }else if (checker == "clicked"){

            uploadImage();

        }

    }

    private void uploadImage() {

        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please waiting, Updating Information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        if(imageUri != null){

            final StorageReference fileRef = storageProfilePictureReference.child(Prevalent.currentCustomer.getEmail() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()){

                        Uri downloadUrl = task.getResult();

                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Customers");

                        HashMap<String, Object> customerMap = new HashMap<>();

                        customerMap.put("phone",phoneEditText.getText().toString());
                        customerMap.put("userName", nameEditText.getText().toString());
                        customerMap.put("address", addressEditText.getText().toString());
                        customerMap.put("image", myUrl);

                        ref.child(Prevalent.currentCustomer.getEmail()).updateChildren(customerMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile Information Updated", Toast.LENGTH_SHORT).show();
                        finish();

                    }

                    else{

                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Cannot be Updated..", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        else{
            Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void customerInfoDisplay(final CircleImageView profileImageView, final EditText nameEditText, final EditText phoneEditText, final EditText addressEditText) {

        DatabaseReference CustomerRef = FirebaseDatabase.getInstance().getReference().child("Customers").child(Prevalent.currentCustomer.getEmail());

        CustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("userName").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        nameEditText.setText(name);
                        phoneEditText.setText(phone);
                        addressEditText.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}