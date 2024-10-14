package com.example.indegenousmedicine2;

import static android.content.ContentValues.TAG;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class DrugDetails extends AppCompatActivity {
    //421-22, 367

    private String currentUser;
    private EditText mDrugNameEditText;
    private EditText mMessageEditText;
    private EditText mScientificNameEditText;
    private EditText mhowToApplyEditText;
    private EditText mMedicinalPlantsEditText;
    private EditText mModeOfPreparationEditText;
    private RadioGroup viableRadioGroup;
    private RadioButton selectedRadioButton;
    private EditText mYearsUsedEditText;
    private EditText mLivingAreaSinceEditText;
    private Button mSendButton;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private Button mProfileButton;
    private String user;
    private String userEmail;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int MAX_IMAGES = 5;
    private ImageView selectedImageView;
    private Bitmap selectedImageBitmap;
    private int imageCount = 0;
    private ArrayList<Bitmap> selectedImagesList = new ArrayList<>();
    private ArrayList<Uri> selectedImagesUrls = new ArrayList<>();
    private ImageAdapter imageAdapter;
    ArrayList<String> urls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

//        currentUser = getIntent().getStringExtra("NameOfClient");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUser = extras.getString("NameOfClient", "Anonymous");
        } else {
            currentUser = "Anonymous";
        }
        // Check for camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, continue with activity setup
            setupViews();
        }

//        RecyclerView imageRecyclerView = findViewById(R.id.imageRecyclerView);
        // Initialize the list to hold selected images
        selectedImagesList = new ArrayList<>();

        // Initialize the adapter with the selectedImagesList
//        imageAdapter = new ImageAdapter(selectedImagesList);

        // Set the LayoutManager for the RecyclerView
        RecyclerView imageRecyclerView = findViewById(R.id.imageRecyclerView);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Set the adapter for the RecyclerView
        imageRecyclerView.setAdapter(imageAdapter);


        Button imageButton = findViewById(R.id.imageButton);
//        imageButton.setOnClickListener(v -> {
//            // Open the gallery or camera based on user choice
//            selectImage();
//        });
        imageButton.setOnClickListener(v -> selectImage());


        // Apply language when activity is created
        LanguageManager.applyLanguage(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Agroषधि");


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = mAuth.getCurrentUser();
        if (currUser == null) {
            // User is not signed in, start the sign-in flow
            startActivity(new Intent(DrugDetails.this, LoginLogic.class));
            finish(); // Close this activity so user can't go back
        } else {
            // User is signed in
            setupViews();
        }
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(v -> selectImage());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, proceed with activity setup
                setupViews();
            } else {
                // Camera permission denied, show a message or handle it accordingly
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void selectImage() {
        // Check if camera permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, show dialog to request permission
            AlertDialog.Builder builder = new AlertDialog.Builder(DrugDetails.this);
            builder.setMessage("Please grant access to camera to select image")
                    .setPositiveButton("Grant", (dialog, which) -> {
                        // Request camera permission
                        ActivityCompat.requestPermissions(DrugDetails.this,
                                new String[]{Manifest.permission.CAMERA},
                                CAMERA_PERMISSION_REQUEST_CODE);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        // Do nothing if permission is not granted
                        dialog.dismiss();
                    })
                    .show();
        } else {
            // Permission is granted, proceed with image selection
            showImageSelectionOptions();
        }
    }

    private void showImageSelectionOptions() {
        // Show options for selecting image from camera or gallery
        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DrugDetails.this);
        builder.setTitle("Select Option");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                // Open camera intent
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } else if (options[item].equals("Choose from Gallery")) {
                // Open gallery intent
                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void displaySelectedImage(Bitmap bitmap) {
        // Display the selected image in ImageView
        if (selectedImageView != null) {
            selectedImageView.setImageBitmap(bitmap);
            selectedImageView.setVisibility(View.VISIBLE);
        } else {
            Log.e(TAG, "displaySelectedImage: selectedImageView is null");
        }
    }
    private void setupViews() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessageDatabaseReference = mFirebaseDatabase.getReference();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            user = currentUser.getDisplayName();
            userEmail = currentUser.getEmail();
        } else {
            user = "anonymous";
        }

        mMessageEditText = findViewById(R.id.drugNameEditText);
        mScientificNameEditText = findViewById(R.id.ScientificNameEditText);
        mhowToApplyEditText = findViewById(R.id.howToApplyEditText);
        mMedicinalPlantsEditText = findViewById(R.id.medicinalPlantsEditText);
        mModeOfPreparationEditText = findViewById(R.id.modeOfPreparationEditText);
        viableRadioGroup = findViewById(R.id.viableRadioGroup);
        mSendButton = findViewById(R.id.sendButton);
        mYearsUsedEditText = findViewById(R.id.yearsUsedEditText);
        mLivingAreaSinceEditText = findViewById(R.id.livingAreaSinceEditText);

        TextInputLayout yearsUsedTextInputLayout = findViewById(R.id.yearsUsedTextInputLayout);

        viableRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.yesRadioButton) {
                yearsUsedTextInputLayout.setVisibility(View.VISIBLE);
            } else {
                yearsUsedTextInputLayout.setVisibility(View.GONE);
                mYearsUsedEditText.setText(""); // Clear the EditText when "No" is selected
            }
        });

//        mProfileButton.setOnClickListener(v -> redirection());

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSendButton.setOnClickListener(v -> saveDataToFirebase());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Apply language when activity is resumed
        LanguageManager.applyLanguage(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Check if the back button is pressed
            // Redirect to UserInfoActivity
            Log.d(TAG, "onOptionsItemSelected: userInfo");






//--------->>>>>            //URGENT
            //It should redirect to the users list(for Aarogya Mitra portal)
            startActivity(new Intent(DrugDetails.this, HomeDummy.class));
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.action_change_language) {
            // Implement logic to change language
            // For example, you can show a dialog to let the user select the language
            showLanguageSelectionDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    private void showLanguageSelectionDialog() {
        // Implement code to show a dialog for language selection
        // You can use AlertDialog or create a custom dialog
        // When the user selects a language, update the locale configuration and refresh UI
        // Example:
        new AlertDialog.Builder(this)
                .setTitle("Select Language")
                .setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                updateLocale("en"); // English
                                break;
                            case 1:
                                updateLocale("as"); // Assamese
                                break;
                            // Add cases for other languages if needed
                        }
                        refreshUI();
                    }
                })
                .show();
    }

    private void updateLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void refreshUI() {
        // You may need to recreate the activity for changes to take effect
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    private void redirection() {
        Log.d("MainActivity", "onClick: mProfileButton");
//        Intent intent = new Intent(UserDetails.this, ProfileActivity.class);
//        intent.putExtra("name", user); // Pass user name retrieved from Google auth
//        intent.putExtra("email", userEmail); // Pass user email retrieved from Google auth
        Log.d(TAG, "redirection: Profile");
//        startActivity(intent);
    }

    private void saveDataToFirebase() {
        String message = mMessageEditText.getText().toString().trim();
        String scientificName = mScientificNameEditText.getText().toString().trim();
        String howToApply = mhowToApplyEditText.getText().toString().trim();
        String medicinalPlants = mMedicinalPlantsEditText.getText().toString().trim();
        String modeOfPreparation = mModeOfPreparationEditText.getText().toString().trim();
        boolean isViable;
        int checkedRadioButtonId = viableRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.yesRadioButton) {
            isViable = true;
        } else if (checkedRadioButtonId == R.id.noRadioButton) {
            isViable = false;
        } else {
            // Neither Yes nor No radio button is checked
            Toast.makeText(this, "Please select whether the drug is viable or not", Toast.LENGTH_SHORT).show();
            return;
        }
        String yearsUsedSinceText = mYearsUsedEditText.getText().toString().trim();
        int yearsUsedSince = 0;

        if (!yearsUsedSinceText.isEmpty()) {
            yearsUsedSince = Integer.parseInt(yearsUsedSinceText);
        }

        String livingAreaSinceText = mLivingAreaSinceEditText.getText().toString().trim();
        int livingAreaSince = 0;


        if (!livingAreaSinceText.isEmpty()) {
            livingAreaSince = Integer.parseInt(livingAreaSinceText);
        }

        if (message.isEmpty() || scientificName.isEmpty() || howToApply.isEmpty() || medicinalPlants.isEmpty() || modeOfPreparation.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        final String scientificNameLowerCase = scientificName.toLowerCase(); // Convert to lowercase for case-insensitive comparison
        String new_scientificNameLowerCase = capitalizeFirstLetter(scientificNameLowerCase);

        int finalYearsUsedSince = yearsUsedSince;
        int finalLivingAreaSince = livingAreaSince;

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading data, please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mMessageDatabaseReference.child("drugs").orderByChild("Scientific name").equalTo(new_scientificNameLowerCase)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Drug with the same scientific name already exists
                            progressDialog.dismiss(); // Dismiss progress dialog
                            Toast.makeText(DrugDetails.this, "Drug with scientific name already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            // Drug doesn't exist, proceed with adding it
                            // Upload images to Firebase Storage and get the download URLs
                            uploadImagesAndAddDrug(message, new_scientificNameLowerCase, howToApply, medicinalPlants, modeOfPreparation, isViable, finalYearsUsedSince, finalLivingAreaSince, progressDialog);
//                            clearFieldsAndRecyclerView();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss(); // Dismiss progress dialog
                        Toast.makeText(DrugDetails.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input; // Return input as is if it's null or empty
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private void uploadImagesAndAddDrug(String message, String scientificNameLowerCase, String howToApply, String medicinalPlants, String modeOfPreparation, boolean isViable, int yearsUsedSince, int livingAreaSince, ProgressDialog progressDialog) {
        ArrayList<String> imageUrls = new ArrayList<>();
//        int totalSelectedImages = selectedImagesList.size(); // Store the total number of selected images
//        int successfulUploads = 0;

        // Check if any images are selected
        if (!selectedImagesList.isEmpty()) {
            Log.d(TAG, "uploadImagesAndAddDrug: enterd the upload images and add drug method");
            int[] successfulUploads = {0}; // Declare as an array to make it effectively final

            // Iterate over each selected image
            for (Bitmap bitmap : selectedImagesList) {
                Log.d(TAG, "uploadImagesAndAddDrug: enterd the loop and iterating for every image");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child("images").child(System.currentTimeMillis() + ".jpg");

                // Convert bitmap to byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                // Upload byte array to Firebase Storage
                UploadTask uploadTask = storageRef.putBytes(data);
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    Log.d(TAG, "uploadImagesAndAddDrug: Now uploading the image and getting the url");
                    // Image uploaded successfully
                    // Get the download URL of the uploaded image
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Add the URL to the list
                        imageUrls.add(uri.toString());
                        successfulUploads[0]++; // Increment counter

                        // If all images are uploaded, call addDrugToDatabase
                        if (successfulUploads[0] == selectedImagesList.size()) {
                            Log.d(TAG, "uploadImagesAndAddDrug: now all the images are uploaded, adding the object to realtime database");
                            addDrugToDatabase(message, scientificNameLowerCase, howToApply, medicinalPlants, modeOfPreparation, isViable, yearsUsedSince, livingAreaSince, imageUrls, progressDialog);
                            Log.d(TAG, "uploadImagesAndAddDrug: successfully uploaded the data to realtime database");
                        }
                    });
                }).addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                    progressDialog.dismiss(); // Dismiss progress dialog
                    Toast.makeText(DrugDetails.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
            }
        } else {
            // If no images selected, directly add drug to database
            Log.d(TAG, "uploadImagesAndAddDrug: No images, directly addding data to realtime database");
            addDrugToDatabase(message, scientificNameLowerCase, howToApply, medicinalPlants, modeOfPreparation, isViable, yearsUsedSince, livingAreaSince, new ArrayList<>(), progressDialog);
        }
    }

private void addDrugToDatabase(String message, String scientificName, String howToApply, String medicinalPlants, String modeOfPreparation, boolean isViable, int yearsUsedSince, int livingAreaSince, ArrayList<String> imageUrls, ProgressDialog progressDialog) {
    Log.d(TAG, "addDrugToDatabase: inside add drug to database");
    String key = mMessageDatabaseReference.child("drug_to_be_validated").push().getKey();
    if (key != null) {
        // Save user details first
        mMessageDatabaseReference.child("drug_to_be_validated").child(key).child("Aarogya Mitra").setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // User details saved successfully, now save other drug details
                    mMessageDatabaseReference.child("drug_to_be_validated").child(key).child("Drug Name").setValue(message)
                            .addOnSuccessListener(aVoid1 -> {
                                mMessageDatabaseReference.child("drug_to_be_validated").child(key).child("scientificName").setValue(scientificName)
                                        .addOnSuccessListener(aVoid2 -> {
                                            mMessageDatabaseReference.child("drug_to_be_validated").child(key).child("howToApply").setValue(howToApply)
                                                    .addOnSuccessListener(aVoid3 -> {
                                                        mMessageDatabaseReference.child("drug_to_be_validated").child(key).child("medicinalPlants").setValue(medicinalPlants)
                                                                .addOnSuccessListener(aVoid4 -> {
                                                                    mMessageDatabaseReference.child("drug_to_be_validated").child(key).child("modeOfPreparation").setValue(modeOfPreparation)
                                                                            .addOnSuccessListener(aVoid5 -> {
                                                                                mMessageDatabaseReference.child("drug_to_be_validated").child(key).child("isViable").setValue(isViable)
                                                                                        .addOnSuccessListener(aVoid6 -> {
                                                                                            if (isViable) {
                                                                                                mMessageDatabaseReference.child("drug_to_be_validated").child(key).child("yearsUsedSince").setValue(yearsUsedSince)
                                                                                                        .addOnSuccessListener(aVoid7 -> {
                                                                                                            mMessageDatabaseReference.child("drug_to_be_validated").child(key).child("livingAreaSince").setValue(livingAreaSince)
                                                                                                                    .addOnSuccessListener(aVoid8 -> {
                                                                                                                        // Add the Client field with the value from currentUser
                                                                                                                        mMessageDatabaseReference.child("drug_to_be_validated").child(key).child("Client").setValue(currentUser)
                                                                                                                                .addOnSuccessListener(aVoid9 -> {
                                                                                                                                    // All data saved successfully, now save image URLs
                                                                                                                                    Log.d(TAG, "addDrugToDatabase: adding images and urls to database->line 518");
                                                                                                                                    saveImageUrlsToDatabase(key, imageUrls, progressDialog);
                                                                                                                                    // Clear all fields and the RecyclerView
                                                                                                                                    // clearFieldsAndRecyclerView();
                                                                                                                                    progressDialog.dismiss(); // Dismiss progress dialog
                                                                                                                                    Log.d(TAG, "addDrugToDatabase: successfully added data to database");
                                                                                                                                    Toast.makeText(DrugDetails.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                                                                                                                                })
                                                                                                                                .addOnFailureListener(e -> {
                                                                                                                                    progressDialog.dismiss(); // Dismiss progress dialog
                                                                                                                                    Toast.makeText(DrugDetails.this, "Failed to add Client: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                                                });
                                                                                                                    })
                                                                                                                    .addOnFailureListener(e -> {
                                                                                                                        progressDialog.dismiss(); // Dismiss progress dialog
                                                                                                                        Toast.makeText(DrugDetails.this, "Failed to add livingAreaSince: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                                    });
                                                                                                        })
                                                                                                        .addOnFailureListener(e -> {
                                                                                                            progressDialog.dismiss(); // Dismiss progress dialog
                                                                                                            Toast.makeText(DrugDetails.this, "Failed to add yearsUsedSince: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                        });
                                                                                            } else {
                                                                                                // Add the Client field with the value from currentUser
                                                                                                mMessageDatabaseReference.child("drug_to_be_validated").child(key).child("Client").setValue(currentUser)
                                                                                                        .addOnSuccessListener(aVoid9 -> {
                                                                                                            // All data saved successfully, now save image URLs
                                                                                                            saveImageUrlsToDatabase(key, imageUrls, progressDialog);
                                                                                                            progressDialog.dismiss(); // Dismiss progress dialog
                                                                                                        })
                                                                                                        .addOnFailureListener(e -> {
                                                                                                            progressDialog.dismiss(); // Dismiss progress dialog
                                                                                                            Toast.makeText(DrugDetails.this, "Failed to add Client: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                        });
                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(e -> {
                                                                                            progressDialog.dismiss(); // Dismiss progress dialog
                                                                                            Toast.makeText(DrugDetails.this, "Failed to add isViable: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                        });
                                                                            })
                                                                            .addOnFailureListener(e -> {
                                                                                progressDialog.dismiss(); // Dismiss progress dialog
                                                                                Toast.makeText(DrugDetails.this, "Failed to add modeOfPreparation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                            });
                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    progressDialog.dismiss(); // Dismiss progress dialog
                                                                    Toast.makeText(DrugDetails.this, "Failed to add medicinalPlants: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                });
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        progressDialog.dismiss(); // Dismiss progress dialog
                                                        Toast.makeText(DrugDetails.this, "Failed to add howToApply: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    });
                                        })
                                        .addOnFailureListener(e -> {
                                            progressDialog.dismiss(); // Dismiss progress dialog
                                            Toast.makeText(DrugDetails.this, "Failed to add scientificName: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss(); // Dismiss progress dialog
                                Toast.makeText(DrugDetails.this, "Failed to add message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss(); // Dismiss progress dialog
                    Toast.makeText(DrugDetails.this, "Failed to add user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

private void saveImageUrlsToDatabase(String drugKey, ArrayList<String> imageUrls, ProgressDialog progressDialog) {
    // Save image URLs to the database under the specific drug key
    Log.d(TAG, "saveImageUrlsToDatabase: inside save image urls to database");
    for (String url : imageUrls) {
        mMessageDatabaseReference.child("drug_to_be_validated").child(drugKey).child("imageUrls").push().setValue(url)
                .addOnSuccessListener(aVoid -> {
                    // Image URL saved successfully
                    Log.d(TAG, "saveImageUrlsToDatabase: added one image successfully");
                    Toast.makeText(DrugDetails.this, "Image URL added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to save image URL
                    Toast.makeText(DrugDetails.this, "Failed to add image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        progressDialog.dismiss(); // Dismiss progress dialog
    }
    clearFieldsAndRecyclerView();
    Toast.makeText(DrugDetails.this, "Drug added successfully ;)", Toast.LENGTH_SHORT).show();

    startActivity(new Intent(DrugDetails.this, UserInfoActivity.class));
}


    private void clearFieldsAndRecyclerView() {
        Log.d(TAG, "clearFieldsAndRecyclerView: clearing all the fields");
        // Clear all EditText fields
        mMessageEditText.setText("");
        mScientificNameEditText.setText("");
        mhowToApplyEditText.setText("");
        mMedicinalPlantsEditText.setText("");
        mModeOfPreparationEditText.setText("");
        mYearsUsedEditText.setText("");
        mLivingAreaSinceEditText.setText("");

        // Clear radio button selection
        viableRadioGroup.clearCheck();

        // Clear selected images list and notify adapter
        selectedImagesList.clear();
        imageAdapter.notifyDataSetChanged();
        Log.d(TAG, "clearFieldsAndRecyclerView: successfully cleared all the fields");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Image captured from camera
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    if (imageBitmap != null) {
                        addImageToList(imageBitmap);
                        // Set the RecyclerView visibility to visible
                        RecyclerView imageRecyclerView = findViewById(R.id.imageRecyclerView);
                        imageRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                // Image selected from gallery
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        addImageToList(imageBitmap);
                        // Set the RecyclerView visibility to visible
                        RecyclerView imageRecyclerView = findViewById(R.id.imageRecyclerView);
                        imageRecyclerView.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void addImageToList(Bitmap imageBitmap) {
        if (selectedImagesList.size() < 5) { // Limiting the number of images to 5
            selectedImagesList.add(imageBitmap);
            int size = selectedImagesList.size();
            Log.d(TAG, "addImageToList: " + size);
            imageAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
        } else {
            Toast.makeText(this, "You can select up to 5 images", Toast.LENGTH_SHORT).show();
        }
    }


}