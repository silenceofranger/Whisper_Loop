package com.example.whisperloop;

import static com.example.whisperloop.util.Validator.validate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whisperloop.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    Uri imageUri;
    String imageUriString;
    String status = "Hey I'm Using This Application";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView logInButton = findViewById(R.id.loginbut);
        EditText singUpUserName = findViewById(R.id.rgusername);
        EditText singUpEmailId = findViewById(R.id.signUpEmailId1);
        EditText signUpPassword = findViewById(R.id.rgpassword);
        EditText signUpConfirmPassword = findViewById(R.id.rgrepassword);
        CircleImageView profilePicture = findViewById(R.id.profilerg0);
        Button signUpButton = findViewById(R.id.signupbutton);

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
                            System.out.println("CK1");
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                System.out.println("CK2");
                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(id);
                                StorageReference storageReference = firebaseStorage.getReference().child("upload").child(id);
                                System.out.println("CK3");

                                if (imageUri != null) {
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
                                        System.out.println("CK5");
                                        if (task1.isSuccessful()) {
//                                            progressDialog.show();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        CircleImageView profilePicture = findViewById(R.id.profilerg0);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                imageUri = data.getData();
                profilePicture.setImageURI(imageUri);
            }
        }
    }

    private void setProfilePictureOnClickListener(CircleImageView profilePicture) {
        profilePicture.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 11);
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