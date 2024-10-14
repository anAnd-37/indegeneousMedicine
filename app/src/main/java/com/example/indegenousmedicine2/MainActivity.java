package com.example.indegenousmedicine2;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView titleTextView;
    private ImageView logoImageView;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Arogya Mitra Portal");

        titleTextView = findViewById(R.id.titleTextView);
        logoImageView = findViewById(R.id.logoImageView);
        startButton = findViewById(R.id.startButton);

        // Apply language when activity is created
        LanguageManager.applyLanguage(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = mAuth.getCurrentUser();

        // Check if the user is already logged in
        if (currUser != null) {
            // User is signed in, redirect to UserInfoActivity
            Intent userDetailsIntent = new Intent(MainActivity.this, UserInfoActivity.class);
            userDetailsIntent.putExtra("userId", currUser.getUid());
            startActivity(userDetailsIntent);
            finish(); // Close this activity
            return; // Return to avoid executing further code
        }

//        // Set click listener for the "Get Started" button
//        startButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Check if the user is logged in
//                if (currUser == null) {
//                    // User is not logged in, redirect to login activity
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                    finish(); // Close this activity so user can't go back
////            signInWithGoogle();
//                } else {
//                    // User is signed in
//                    // Redirect to UserDetails activity and pass the current user's details
//                    Intent userDetailsIntent = new Intent(MainActivity.this, UserInfoActivity.class);
//                    userDetailsIntent.putExtra("userId", currUser.getUid()); // Pass user ID to UserDetails activity if needed
//                    startActivity(userDetailsIntent);
//                    finish();// Close this activity
//                    return;
//                }
//            }
//        });

        // Set click listener for the "Get Started" button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to LoginActivity
                startActivity(new Intent(MainActivity.this, LoginLogic.class));
                finish(); // Close this activity
            }
        });

        // Apply animations
        applyAnimations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_language, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_english) {
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
        } else {
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
        MainActivity.this.recreate();
    }


    // Apply animations to GUI elements
    private void applyAnimations() {
        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        findViewById(R.id.logoImageView).startAnimation(slideUpAnimation);
        findViewById(R.id.titleTextView).startAnimation(slideUpAnimation);
        // Animation for the title
        Animation titleAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        titleTextView.startAnimation(titleAnimation);

        // Animation for the logo
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        logoImageView.startAnimation(logoAnimation);

        // Animation for the button
        Animation buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        startButton.startAnimation(buttonAnimation);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Apply language when activity is resumed
        LanguageManager.applyLanguage(this);
    }
}
