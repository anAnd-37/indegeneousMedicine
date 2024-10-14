package com.example.indegenousmedicine2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DrugDetailActivity extends AppCompatActivity {

    private RecyclerView drugRecyclerView;
    private DrugAdapterForPlantsDetails drugAdapter;
    private List<DrugDetail> drugList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_detail);

        // Set up the toolbar with a back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            // Navigate to HomeDummy when the back button is clicked
            Intent intent = new Intent(DrugDetailActivity.this, HomeDummy.class);
            startActivity(intent);
            finish();
        });

        // Initialize RecyclerView and other variables
        drugRecyclerView = findViewById(R.id.drugRecyclerView);
        drugRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        drugList = new ArrayList<>();
        drugAdapter = new DrugAdapterForPlantsDetails(this, drugList);
        drugRecyclerView.setAdapter(drugAdapter);

        // Fetch data from Firebase
        fetchDrugDetails();
    }

    private void fetchDrugDetails() {
        // Reference to the "ListOfValues" node in the database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ListOfValues");

        // Add a ValueEventListener to listen for data changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the list before adding new data
                drugList.clear();

                // Iterate through each child node under "ListOfValues"
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convert each child into a DrugDetail object
                    DrugDetail drugDetail = snapshot.getValue(DrugDetail.class);

                    // Add the object to the list if it is not null
                    if (drugDetail != null) {
                        drugList.add(drugDetail);
                    }
                }

                // Notify the adapter about the updated data
                drugAdapter.notifyDataSetChanged();

                // Log the details of each drug
//                for (DrugDetail drug : drugList) {
//                    Log.d("DrugDetailActivity", "Drug Detail: " +
//                            "Vernacular Name: " + drug.getVernacularName() +
//                            ", Scientific Name: " + drug.getScientificName() +
//                            ", Photograph: " + drug.getPhotograph() +
//                            ", Medicinal Use: " + drug.getMedicinalUse());
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DrugDetailActivity", "Database error: " + databaseError.getMessage());
            }
        });
    }
}
