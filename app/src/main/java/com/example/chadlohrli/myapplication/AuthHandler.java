package com.example.chadlohrli.myapplication;
/**
 * Created by charry on 3/5/18.
 */

import android.content.Context;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class AuthHandler implements Runnable {
    private String code;
    private Context context;
    private HttpTransport httpTransport;
    private JacksonFactory jsonFactory;
    private DatabaseReference ref;
    private ArrayList<String> friendKeys;
    private int queryExecCnt;
    private String currentUserKey;

    public AuthHandler(Context context, String code, DatabaseReference myRef, String currentUserKey) {
        if (code == null || context == null) Log.e("RequestFriendListRunnable()", "param is null");
        this.code = code;
        this.context = context;
        this.currentUserKey = currentUserKey;
        httpTransport = new NetHttpTransport();
        jsonFactory = new JacksonFactory();
        ref = myRef;
        friendKeys = new ArrayList<>();
        queryExecCnt = 0;
    }

    public List<Person> getFriendList(String code) throws IOException {
        // Go to the Google API Console, open your application's
        // credentials page, and copy the client ID and client secret.
        // Then paste them into the following code.
        String clientId = context.getString(R.string.gapi_client_id);
        String clientSecret = context.getString(R.string.gapi_client_secret);

        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        httpTransport, jsonFactory, clientId, clientSecret, code, context.getString(R.string.gapi_redirect_url))
                        .execute();
        // End of Step 2 <--

//        List<String> scopes = new ArrayList<>();
//        scopes.add("https://www.googleapis.com/auth/contacts.readonly");

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
//                .setServiceAccountScopes(scopes)
                .build()
                .setFromTokenResponse(tokenResponse);

        PeopleService peopleService =
                new PeopleService.Builder(httpTransport, jsonFactory, credential).build();

        ListConnectionsResponse response = null;

        // Set request
        try {
            response = peopleService.people().connections().list("people/me")
                    .setPersonFields("names,emailAddresses")
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("request friend response not working", e.getMessage());
        }

        return response == null ? null : response.getConnections();
    }


    public List<String> getFriendEmails(List<Person> friends) {
        List<String> emails = new ArrayList<>();
        for (Person friend : friends) {
            if (friend.getEmailAddresses().size() > 0)
                emails.add(friend.getEmailAddresses().get(0).getValue());
        }

        return emails;
    }

    public void getDBExistEntryFromEmail(List<String> emails) {
        final int terminateSize = emails.size();
        for (final String email : emails) {
            ref.child("users")
                    .orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            queryExecCnt++;

                            for (DataSnapshot entry : dataSnapshot.getChildren()) {
                                friendKeys.add(entry.getKey());

                                // Similar to executing callback when all queries has been processed
                                if (queryExecCnt == terminateSize) {
                                    setFriendListDB(friendKeys);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    public void setFriendListDB(ArrayList<String> keys) {
        for (String key : keys) {
            ref.child("users").child(currentUserKey).child("friends").child(key).setValue(true);
        }
    }

    @Override
    public void run() {
        try {
            List<Person> friendList = getFriendList(code);
            List<String> friendEmails = getFriendEmails(friendList);
            getDBExistEntryFromEmail(friendEmails);

        } catch (Exception e) {
            Log.e("getFriendList", "Exception");
        }
    }
}
