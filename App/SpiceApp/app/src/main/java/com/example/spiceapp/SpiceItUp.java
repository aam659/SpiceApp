package com.example.spiceapp;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class SpiceItUp extends AppCompatActivity {
    private TextView txtName;
    private ImageView imgRestuarant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spice_it_up);

        initializeToolbar();

        initializeViews();

        // Set listeners for programmatic spiceItUp()
        // findViewById(R.id.btnSIU).setOnClickListener(view -> hereAPI());
    }

    private void initializeToolbar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Spice It Up");
    }

    private void initializeViews(){
        txtName = findViewById(R.id.txtName);
        imgRestuarant = findViewById(R.id.imgRestuarant);
    }

    private void hereAPI(){
        //https://developer.here.com/documentation/android-starter/dev_guide/topics/places.html
    }

    private void updateImage(/*I think parameter will be PhotoMetadata*/){
        //update image view with new restaurant result
    }

}


//    private void spiceItUp(){
//        // Define a Place ID.
//        String placeId = "ChIJN1t_tDeuEmsRUsoyG83frY4";
//
//        // Specify the fields to return (in this example all fields are returned).
//        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);
//
//
//        // Construct a request object, passing the place ID and fields array.
//        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
//
//        Task<FetchPlaceResponse> placeTask = placesClient.fetchPlace(request);
//
//        placeTask.addOnSuccessListener(
//                (response) -> {
//                    Place place = response.getPlace();
//                    txtName.setText(place.getName());
//                });
//
//        placeTask.addOnFailureListener(
//                (exception) -> {
//                    exception.printStackTrace();
//                    txtName.setText("Failed to get the Place object");
//                    Toast.makeText(this, "Not Spicy", Toast.LENGTH_LONG).show();
//                });
//    }

//    private void spiceItUp1(){
//        // Define a Place ID.
//        String placeId = "ChIJN1t_tDeuEmsRUsoyG83frY4";
//
//        // Specify the fields to return (in this example all fields are returned).
//        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);
//
//        // Construct a request object, passing the place ID and fields array.
//        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
//
//        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
//            Place place = response.getPlace();
//            txtName.setText(place.getName());
//        }).addOnFailureListener((exception) -> {
//            if (exception instanceof ApiException) {
//                ApiException apiException = (ApiException) exception;
//                int statusCode = apiException.getStatusCode();
//                // Handle error with given status code.
//                txtName.setText("Failed to get the Place object");
//                Toast.makeText(this, "Not Spicy", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
