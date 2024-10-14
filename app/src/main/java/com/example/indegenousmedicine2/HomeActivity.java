package com.example.indegenousmedicine2;
import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Check if user is logged in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // User is not logged in, display toast and redirect to LoginActivity
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish(); // Finish the current activity to prevent the user from going back to HomeActivity
            return; // Return to prevent executing the rest of the code in onCreate
        }

        // Find views
        Button userTile = findViewById(R.id.userTile);
        Button drugTile = findViewById(R.id.drugTile);
        Button statusTile = findViewById(R.id.statusTile);
        Button newUserTile = findViewById(R.id.newUserTile);
        Button newDrugTile = findViewById(R.id.newDrugTile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Agroषधि");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        toolbar.setLogo(R.drawable.logo_final);
        ViewGroup.LayoutParams lp = toolbar.getLayoutParams();
        if (lp instanceof Toolbar.LayoutParams) {
            ((Toolbar.LayoutParams) lp).gravity = android.view.Gravity.START;
        }
        toolbar.setLayoutParams(lp);
//        // Set click listener for profile icon
//        ImageView profileIcon = toolbar.findViewById(R.id.logo_final);
//        profileIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle profile icon click
//                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
//            }
//        });



        // Set click listeners for tiles
        userTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, UsersActivity.class));
            }
        });

        drugTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DrugActivity.class));
            }
        });

        statusTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this, StatusActivity.class));
            }
        });

        newUserTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, UserInfoActivity.class));
            }
        });

        newDrugTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DrugDetails.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_profile:
////                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
//                return true;
//            case R.id.menu_languages:
////                startActivity(new Intent(HomeActivity.this, LanguagesActivity.class));
//                return true;
//            case R.id.menu_lists:
////                startActivity(new Intent(HomeActivity.this, ListsActivity.class));
//                return true;
//            case R.id.menu_users:
////                startActivity(new Intent(HomeActivity.this, UsersActivity.class));
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
        if (item.getItemId() == R.id.menu_profile) {
            // Handle profile menu item click
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
//            Log.d(TAG, package com.example.indegenousmedicine2;);
            return true;
        }else if (item.getItemId() == R.id.menu_english) {
            setLocale("en");
            recreateActivity();
//            LanguageManager.setLanguage(this, "en");
//            LanguageManager.applyLanguage(this);
            return true;
        } else if (item.getItemId() == R.id.menu_assamese) {
            setLocale("as");
            recreateActivity();
//            LanguageManager.setLanguage(this, "as");
//            LanguageManager.applyLanguage(this);
            return true;
        } else if (item.getItemId() == R.id.menu_users) {
            // Handle profile menu item click
//                startActivity(new Intent(UserInfoActivity.this, DrugActivity.class));
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            Intent intent = new Intent(HomeActivity.this, UsersActivity.class);
            intent.putExtra("userName", currentUser);
            startActivity(intent);
//            Log.d(TAG, package com.example.indegenousmedicine2;);
            return true;
        }else if (item.getItemId() == R.id.menu_drug) {
            // Handle profile menu item click
//                startActivity(new Intent(UserInfoActivity.this, DrugActivity.class));
            Log.d(TAG, "onOptionsItemSelected: Drug option selected");
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void setLocale(String languageCode) {
//        Locale locale = new Locale(languageCode);
//        Locale.setDefault(locale);
//        Resources resources = getResources();
//        Configuration configuration = resources.getConfiguration();
//        configuration.setLocale(locale);
//        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        // Store the selected language preference in SharedPreferences
        LanguageManager.setLanguage(this, languageCode);

        // Apply the new language
        LanguageManager.applyLanguage(this);


    }

    private void recreateActivity() {
        // Recreate the activity to apply the new language
        HomeActivity.this.recreate();
    }
}
