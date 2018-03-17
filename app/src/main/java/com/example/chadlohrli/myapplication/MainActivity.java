package com.example.chadlohrli.myapplication;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.Context;
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
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;

import android.provider.Contacts;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    public static final int PERMISSION_REQUEST_READ = 97;
    public static final int PERMISSION_REQUEST_WRITE = 96;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String TAG = "Google";
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    Button songButton;
    Button albumButton;
    Button refreshButton;
    private boolean canSend = false;
    private boolean canDownload = false;

    ImageButton flashBackButton;
    String loc_name;
    String time;
    private ArrayList<SongData> completeList = new ArrayList<SongData>();

    private BottomNavigationView bottomNav;
    private static String date;
    private static String timeStamp;

    private String serverCode = "";

    @Override
    protected void onStart() {
        super.onStart();


        //update user
        //mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            TextView userEmail = (TextView) findViewById(R.id.userEmail);
            userEmail.setText(currentUser.getEmail());
        }

        LocationHelper.getLatLong(getApplicationContext());

        SharedPreferences lp = getSharedPreferences("last song", MODE_PRIVATE);
        Map<String, ?> map = SharedPrefs.getSongData(getApplicationContext(), "last song");
        String title = lp.getString("song", "");
        TextView song = (TextView) findViewById(R.id.location);

        if (title.isEmpty()) {
            song.setText("No song played yet");
            return;
        }

        Object t = map.get("Last played");

        double lt = (double) lp.getFloat("Latitude", 0);
        double lng2 = (double) lp.getFloat("Longitude", 0);

        //Log.i("song last played", title);
        if (t != null) {
            time = t.toString();
        }
        song.setText(title);
        TextView deets = (TextView) findViewById(R.id.place_loc_date);

        //get song's location
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation((double) lt, (double) lng2, 1);
            if (null != listAddresses && listAddresses.size() > 0) {
                loc_name = "Last Played Location: " + listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        deets.setText(loc_name + " at " + time);
        setLastPlayed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.date:
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(getSupportFragmentManager(), "date picker");
                return true;
            case R.id.time:
                DialogFragment timeFragment = new TimePickerFragment();
                timeFragment.show(getSupportFragmentManager(), "time picker");
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void setDate(int day, int month, int year) {
        int currDay = day;
        int currMonth = month+1;
        int currYear = year;
        String zero = Integer.toString(0);
        date = Integer.toString(currYear) + "." + zero + Integer.toString(currMonth) + "." + Integer.toString(currDay);
        Log.d("DATE IS ", date);

    }

    public void setTime(int hour, int minute) {
        int currHour = hour;
        int currMinute = minute;
        int currSec = 00;
        timeStamp = Integer.toString(currHour) + "." + Integer.toString(currMinute) + "." + Integer.toString(currSec);
        Log.d("TIME IS ", timeStamp);

    }

    public static String getDate() {
        return date;
    }

    public static String getTime() {
        return timeStamp;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Context context = this.getApplicationContext();
        //Testing Firebase Code
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.gapi_client_id))
                .requestEmail()
                .requestServerAuthCode(getResources().getString(R.string.gapi_client_id), false)
                .requestScopes(new Scope("https://www.googleapis.com/auth/contacts.readonly"))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();


        LocationHelper.getLatLong(context);

        askForContactPermission();
        askForReadPermission();

        //check permissions
        checkLocationPermission();


        songButton = (Button) findViewById(R.id.song_button);
        albumButton = (Button) findViewById(R.id.album_button);
        flashBackButton = (ImageButton) findViewById(R.id.flashback_button);
        bottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        refreshButton = (Button) findViewById(R.id.refresh);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.download:
                        if (canSend && canDownload) {
                            Intent searchIntent = new Intent(MainActivity.this, DownloadActivity.class);
                            MainActivity.this.startActivity(searchIntent);
                        } else {
                            checkLocationPermission();
                            askForContactPermission();
                            askForReadPermission();
                        }
                        break;
                }
                return true;
            }
        });

        songButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canSend && canDownload) {
                    Intent intent = new Intent(MainActivity.this, SongListActivity.class);
                    MainActivity.this.startActivity(intent);
                } else {
                    checkLocationPermission();
                    askForContactPermission();
                    askForReadPermission();
                }
            }
        });

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canSend && canDownload) {
                    Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                    MainActivity.this.startActivity(intent);
                } else {
                    checkLocationPermission();
                    askForContactPermission();
                    askForReadPermission();
                }
            }
        });


        flashBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canSend && canDownload) {
                    Intent intent = new Intent(MainActivity.this, VibeActivity.class);
                    MainActivity.this.startActivity(intent);
                } else {
                    checkLocationPermission();
                    askForContactPermission();
                    askForReadPermission();
                }
            }
        });

        findViewById(R.id.signout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Google Sign in", "here");
                serverCode = account.getServerAuthCode();
                if (!serverCode.equals(""))
                    new Thread(new AuthHandler(this.getApplicationContext(), serverCode, myRef,currentUser.getUid())).start();


            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("Failed", e.getMessage());
                findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
            } catch (Exception e) {
                Log.e("Login:", "Exception");
            }
        }
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
        TextView song = (TextView) findViewById(R.id.location);

        if (title.isEmpty()) {
            song.setText("No song played yet");
            return;
        }

        Object t = map.get("Last played");

        double lt = (double) lp.getFloat("Latitude", 0);
        double lng2 = (double) lp.getFloat("Longitude", 0);

        //Log.i("song last played", title);
        if (t != null) {
            time = t.toString();
        }
        song.setText(title);
        TextView deets = (TextView) findViewById(R.id.place_loc_date);

        //get song's location
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation((double) lt, (double) lng2, 1);
            if (null != listAddresses && listAddresses.size() > 0) {
                loc_name = "Last Played Location: " + listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        deets.setText(loc_name + " at " + time);


    }

    private void setUserAccount() {

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
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED) {

            canSend = true;
        }
    }


    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

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
            } else {
                setUserAccount();
            }
        } else {
            setUserAccount();
        }
    }

    public boolean askForReadPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                                        {Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , PERMISSION_REQUEST_READ);
                    }
                });
                builder.create().show();
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_READ);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            canDownload = false;
            return false;
        } else {
            canDownload = true;
            return true;
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

                    Toast.makeText(this, "No permisson for contacts", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case PERMISSION_REQUEST_READ: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        canDownload = true;
                    } else {
                        canDownload = false;
                    }
                    return;
                }


            }
        }
    }

}

