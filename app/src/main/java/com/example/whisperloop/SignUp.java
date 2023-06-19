package com.example.whisperloop;

import static com.example.whisperloop.util.Validator.validate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whisperloop.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 2342;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    Uri imageUri;
    String imageUriString;
    String status = "Hey I'm Using Whisper Loop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView logInButton = findViewById(R.id.loginButtonId1);
        EditText singUpUserName = findViewById(R.id.signupUserNameId1);
        EditText singUpEmailId = findViewById(R.id.signUpEmailId1);
        EditText signUpPassword = findViewById(R.id.passwordId1);
        EditText signUpConfirmPassword = findViewById(R.id.confirmPasswordId1);
        CircleImageView profilePicture = findViewById(R.id.profilePicId1);
        Button signUpButton = findViewById(R.id.signupButtonId1);
        setSignInButtonOnClickListener(logInButton);
        setProfilePictureOnClickListener(profilePicture);
        signUpButton.setOnClickListener(v -> {
            String userNameInput = singUpUserName.getText().toString();
            String emailInput = singUpEmailId.getText().toString();
            String passwordInput = signUpPassword.getText().toString();
            String confirmPasswordInput = signUpConfirmPassword.getText().toString();
            if (passwordInput.equals(confirmPasswordInput)) {
                if (validate(SignUp.this, emailInput, passwordInput)) {
                    try {
                        firebaseAuth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(id);
                                if (imageUri != null) {
                                    StorageReference storageReference = firebaseStorage.getReference().child("upload").child(id);
                                    storageReference.putFile(imageUri).addOnCompleteListener(task13 -> {
                                        if (task13.isSuccessful()) {
                                            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                                imageUriString = imageUri.toString();
                                                User user = new User(id, userNameInput, emailInput, passwordInput, profilePicture.toString(), status);
                                                databaseReference.setValue(user).addOnCompleteListener(task12 -> {
                                                    if (task12.isSuccessful()) {
                                                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(SignUp.this, "Error While Creating User", Toast.LENGTH_SHORT);
                                                    }
                                                });
                                            });

                                        }
                                    });
                                } else {
                                    String status = "Hey I'm Using This Application";
                                    imageUriString = "https://firebasestorage.googleapis.com/v0/b/whisper-650ce.appspot.com/o/ProfilePic.jpeg?alt=media&token=d3de306d-f0ab-4409-a1a2-691a853bb769";
                                    User user = new User(id, userNameInput, emailInput, passwordInput, imageUriString, status);
                                    databaseReference.setValue(user).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(SignUp.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(SignUp.this, "Error...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(SignUp.this, "Password is not matching", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                imageUri = data.getData();
            }
        }
    }


    private void setProfilePictureOnClickListener(CircleImageView profilePicture) {
        profilePicture.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);


//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 11);
        });
    }

    private void setSignInButtonOnClickListener(TextView logInButton) {
        logInButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);
            finish();
        });
    }
}