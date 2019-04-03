package com.example.spiceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

//TODO: POPULATE VIEWS IF USER IS EDITING

public class AdditionalDetails extends AppCompatActivity {

    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtional_details);

        FirebaseUser user = FirebaseManager.getCurrentUser();
        database = FirebaseManager.getDatabaseReference();

        CardView finish = (CardView) findViewById(R.id.cardFinish);
        TextView name = (TextView) findViewById(R.id.edtName);
        TextView phoneNumber = (TextView) findViewById(R.id.edtPhone);
        TextView lName = (TextView) findViewById(R.id.edtLName);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUserName = name.getText().toString();
                String userPhoneNumber = phoneNumber.getText().toString();
                String newUserLName = lName.getText().toString();
                if(!userPhoneNumber.isEmpty())
                addDetails(newUserName, userPhoneNumber, newUserLName, user);
                startActivityForResult(new Intent(AdditionalDetails.this, HomePage.HomePageActivity.class), 0);
            }
        });

    }

    protected void addDetails(String fName, String phone, String lName, FirebaseUser user) {
        if(!fName.isEmpty())
            database.child("users").child(user.getUid()).child("fName").setValue(fName);
        if(!lName.isEmpty())
            database.child("users").child(user.getUid()).child("lName").setValue(lName);
        if(!phone.isEmpty())
            database.child("users").child(user.getUid()).child("phoneNumber").setValue(phone);
    }

//    private Uri imgFilePath;
//    private Button btnChooseImg, btnUpload;
//    FirebaseUser user = FirebaseManager.getCurrentUser();
//    private static final int GALLERY_INTENT = 2;
//    protected void addUserPhoto(){
//        if(imgFilePath != null){
//
//        }
//    }
//
//    private void selectImage(){
//        Intent imgIntent = new Intent();
//        imgIntent.setType("image/*");
//        imgIntent.setAction(Intent.ACTION_PICK);
//        startActivityForResult(Intent.createChooser(imgIntent, "Choose Photo"), GALLERY_INTENT);
//
//    }
//    private void uploadImage(){
//        database = FirebaseManager.getDatabaseReference();
//        database.child("users").child(user.getUid()).child("details").child("profilePicture").setValue(imgFilePath);
//    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent image) {
//        super.onActivityResult(requestCode, resultCode, image);
//        if ((requestCode == GALLERY_INTENT) && (resultCode == RESULT_OK) && (image != null) && (image.getData() != null)) {
//            imgFilePath = image.getData();
//        }
//        btnChooseImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImage();
//            }
//        });
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadImage();
//            }
//        });
//
//    }
}
