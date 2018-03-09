package com.example.chadlohrli.myapplication;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.provider.Contacts;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int PERMISSION_REQUEST_CONTACT = 98;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String TAG = "Google";

    Button songButton;
    Button albumButton;
    private boolean canSend = false;

    ImageButton flashBackButton;
    String loc_name;
    String time;
    private ArrayList<SongData> completeList = new ArrayList<SongData>();

    private BottomNavigationView bottomNav;

    @Override
    protected void onStart(){
        super.onStart();

        //update user
        //mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            TextView userEmail = (TextView)findViewById(R.id.userEmail);
            userEmail.setText(currentUser.getEmail());
        }

        LocationHelper.getLatLong(getApplicationContext());

        SharedPreferences lp = getSharedPreferences("last song", MODE_PRIVATE);
        Map<String, ?> map = SharedPrefs.getSongData(getApplicationContext(), "last song");
        String title = lp.getString("song", "");
        TextView song = (TextView)findViewById(R.id.location);

        if(title.isEmpty()) {
            song.setText("No song played yet");
            return;
        }

        Object t = map.get("Last played");

        double lt = (double)lp.getFloat("Latitude", 0);
        double lng2 = (double)lp.getFloat("Longitude", 0);

        //Log.i("song last played", title);
        if(t != null) {
            time = t.toString();
        }
        song.setText(title);
        TextView deets = (TextView)findViewById(R.id.place_loc_date) ;

        //get song's location
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation((double)lt,(double) lng2, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                loc_name = "Last Played Location: " + listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        deets.setText(loc_name + " at " + time);
        setLastPlayed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        LocationHelper.getLatLong(getApplicationContext());

        //check permissions
        checkLocationPermission();
        askForContactPermission();

        songButton = (Button) findViewById(R.id.song_button);
        albumButton = (Button) findViewById(R.id.album_button);
        flashBackButton = (ImageButton) findViewById(R.id.flashback_button);
        bottomNav = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.download:
                        if(canSend) {
                            Intent searchIntent = new Intent(MainActivity.this, DownloadActivity.class);
                            MainActivity.this.startActivity(searchIntent);
                        }
                        else {
                            checkLocationPermission();
                        }
                        break;
                }
                return true;
            }
        });

        songButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canSend) {
                    Intent intent = new Intent(MainActivity.this, SongListActivity.class);
                    MainActivity.this.startActivity(intent);
                }else{
                    checkLocationPermission();
                    askForContactPermission();
                }
            }
        });

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canSend) {
                    Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                    MainActivity.this.startActivity(intent);
                }else{
                    checkLocationPermission();
                    askForContactPermission();
                }
            }
        });



        flashBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canSend) {
                    Intent intent = new Intent(MainActivity.this, FlashBackActivity.class);
                    MainActivity.this.startActivity(intent);
                }else{
                    checkLocationPermission();
                    askForContactPermission();
                }
            }
        });


        findViewById(R.id.signout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        //Testing Firebase Code
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        /*
        String id = UUID.randomUUID().toString();
        String email = "test@ucsd.edu";
        String name ="test";

        myRef.child("users").child(id).child("username").setValue(name);
        myRef.child("users").child(id).child("email").setValue(email);


        String path = "android.resource://" + getPackageName() + "/raw/backeast";
        String id = "backeast";

        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();


        Uri file = Uri.parse(path);

        StorageMetadata metadata = new StorageMetadata.Builder().setContentType("audio/mpeg").build();

        StorageReference riversRef = mStorageRef.child("song" + "/" + id);

        riversRef.putFile(file,metadata)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.i("Success URL",downloadUrl.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Log.i("Fail","Failed");
                    }
                });


          */


    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(intent);

                    }
                });
    }

    private void setLastPlayed() {

        LocationHelper.getLatLong(getApplicationContext());

        SharedPreferences lp = getSharedPreferences("last song", MODE_PRIVATE);
        Map<String, ?> map = SharedPrefs.getSongData(getApplicationContext(), "last song");
        String title = lp.getString("song", "");
        TextView song = (TextView)findViewById(R.id.location);

        if(title.isEmpty()) {
            song.setText("No song played yet");
            return;
        }

        Object t = map.get("Last played");

        double lt = (double)lp.getFloat("Latitude", 0);
        double lng2 = (double)lp.getFloat("Longitude", 0);

        //Log.i("song last played", title);
        if(t != null) {
            time = t.toString();
        }
        song.setText(title);
        TextView deets = (TextView)findViewById(R.id.place_loc_date) ;

        //get song's location
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation((double)lt,(double) lng2, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                loc_name = "Last Played Location: " + listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        deets.setText(loc_name + " at " + time);


    }

    private void setUserAccount(){

        //grab all accounts associated with this phone
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] list = manager.getAccounts();
        //Log.i("Accounts",list[0].toString());

        //TODO google+ API to fetch friend list

    }

    private void permissionsValid() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED) {

            canSend = true;
        }
    }



    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.create().show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                setUserAccount();
            }
        }
        else{
            setUserAccount();
        }
    }




    //https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime-on-android-6
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("Can we use your location?")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            canSend = false;
            return false;
        } else {
            canSend = true;
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        canSend = true;
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    canSend = false;

                }
                return;
            }
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    setUserAccount();

                } else {

                    Toast.makeText(this,"No permisson for contacts",5).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


        }
    }
}

