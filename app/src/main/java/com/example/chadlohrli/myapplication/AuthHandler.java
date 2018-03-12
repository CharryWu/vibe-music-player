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
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AuthHandler implements Runnable {
    private String code;
    private Context context;
    private HttpTransport httpTransport;
    private JacksonFactory jsonFactory;
    private DatabaseReference ref;

    public AuthHandler(Context context, String code, DatabaseReference myRef) {
        if (code == null || context == null) Log.e("RequestFriendListRunnable()", "param is null");
        this.code = code;
        this.context = context;
        httpTransport = new NetHttpTransport();
        jsonFactory = new JacksonFactory();
        ref = myRef;
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

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(tokenResponse);

        PeopleService peopleService =
                new PeopleService.Builder(httpTransport, jsonFactory, credential).build();

        // Set request
        ListConnectionsResponse response = peopleService.people().connections().list("people/me")
                .setPersonFields("names,emailAddresses")
                .execute();

        return response.getConnections();
    }

    public List<String> getFriendEmails(List<Person> friends) {
        List<String> emails = new ArrayList<>();
        for (Person friend : friends) {
            if (friend.getEmailAddresses().size() > 1)
                emails.add(friend.getEmailAddresses().get(0).getValue());
        }

        return emails;
    }

    public void getDBExistEntryFromEmail(List<String> emails){

    }

    public void setFriendListDB(){

    }

    @Override
    public void run() {
        try {
            List<Person> friendList = getFriendList(code);
            List<String> friendEmails = getFriendEmails(friendList);
            for (Person p : friendList) {
            }

        } catch (Exception e) {
            Log.e("RequestFriendListRunnable.run()", "Exception");
        }
    }
}
