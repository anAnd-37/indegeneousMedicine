package com.example.indegenousmedicine2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.google.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DrugListActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private RecyclerView recyclerViewDrugs;
    private ArrayList<String> drugNames;
    private DrugAdapter adapter;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_list);
        // Apply language when activity is created
        LanguageManager.applyLanguage(this);

//        recyclerViewDrugs = findViewById(R.id.recyclerViewDrugs);
        recyclerViewDrugs.setLayoutManager(new LinearLayoutManager(this));

        Log.d("DrugListActivity", "line52");

        drugNames = new ArrayList<>();
//        adapter = new DrugAdapter(DrugListActivity.this, drugNames);
        recyclerViewDrugs.setAdapter(adapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // If user is null, start Google sign-in flow
            signInWithGoogle();
        } else {
            // If user is not null, retrieve drugs for current user
            String user = currentUser.getDisplayName();
            Log.d("DrugListActivity", "onCreate: "+user);
            retrieveDrugsForCurrentUser(currentUser);
        }
//        // Retrieve drug names from Firebase
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                drugNames.clear(); // Clear the list before adding new items
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String drugName = snapshot.getKey(); // Get the drug name from the snapshot key
//                    drugNames.add(drugName); // Add the drug name to the list
//                }
//                adapter.notifyDataSetChanged(); // Notify the adapter of changes in the data
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle database error
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Apply language when activity is resumed
        LanguageManager.applyLanguage(this);
    }

    private void signInWithGoogle() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    private void retrieveDrugsForCurrentUser(FirebaseUser user) {
        // Retrieve current user's ID
        String userId = user.getUid();
        String userName = user.getDisplayName();

        // Query "drugs" collection based on current user's ID
        databaseReference = FirebaseDatabase.getInstance().getReference().child("drugs");
        Query query = databaseReference.orderByChild("user").equalTo(userName);

        // Retrieve drug names from Firebase
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drugNames.clear(); // Clear the list before adding new items
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String drugName = snapshot.child("message").getValue(String.class); // Get the drug name from the snapshot key
                    drugNames.add(drugName); // Add the drug name to the list
                }
                adapter.notifyDataSetChanged(); // Notify the adapter of changes in the data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DrugListActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // If user is not null, retrieve drugs for current user
                    retrieveDrugsForCurrentUser(currentUser);
                } else {
                    // Handle scenario where user is still null after sign-in
                    Toast.makeText(this, "Failed to sign in.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                // Sign in failed
                if (response != null) {
                    Toast.makeText(this, "Sign in failed: " + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Sign in failed.", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
    }
}
