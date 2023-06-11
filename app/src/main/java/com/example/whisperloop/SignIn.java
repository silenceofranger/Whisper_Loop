package com.example.whisperloop;

import static com.example.whisperloop.util.Validator.validate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    Button signInSubmitButton;
    EditText emailId;

    EditText password;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebaseAuth = FirebaseAuth.getInstance();
        signInSubmitButton = findViewById(R.id.logbutton);
        emailId = findViewById(R.id.editTexLogEmail);
        password = findViewById(R.id.editTextLogPassword);
        Button signUpButton = findViewById(R.id.signUpButtonLogInScreen);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });


        signInSubmitButton.setOnClickListener(v -> {
            String emailInput = emailId.getText().toString();
            String passwordInput = password.getText().toString();

            if (validate(SignIn.this, emailInput, passwordInput)) {
                firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (Exception exception) {
                            Toast.makeText(SignIn.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}