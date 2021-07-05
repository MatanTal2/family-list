package com.example.firestoretest;
/* Assignment: final
Campus: Ashdod
Author: Matan Tal, ID: 201492881
*/
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {


    private static final int password_length = 6;
    private final static String ACTION_BAR_TITLE = "Create Account";
    private final static String  PROGRESS_BAR_REGISTER_USER = "Registering User...";
    //views
    EditText mEmailET, mPasswordET;
    Button mRegisterBtn;
    TextView mHaveAccount;

    //progressbar to disply while registering user
    ProgressDialog progressDialog;

    // Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ActionBar and it's title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(ACTION_BAR_TITLE);
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init
        mEmailET = findViewById(R.id.register_emailEt);
        mPasswordET = findViewById(R.id.register_passwordEt);
        mRegisterBtn = findViewById(R.id.register_registerBtn);
        mHaveAccount = findViewById(R.id.register_have_accountTv);


        // initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(PROGRESS_BAR_REGISTER_USER);

        //handle register btn click
        mRegisterBtn.setOnClickListener(view -> {
            // input email, password
            String email = mEmailET.getText().toString().trim();
            String password = mPasswordET.getText().toString().trim();
            //validate
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // set error and focus to email editText
                mEmailET.setError("Invalid Email");
                mEmailET.setFocusable(true);
            } else if (password.length() < 6) {
                // TODO make the password more secure.(letters uppercase smaller case digit and signs)
                // set error and focus to password editText
                mPasswordET.setError(String.format("Password length at least %d characters", password_length));
            } else {
                registerUser(email, password);  // register the user
            }

        });

        //handle login textView click listener
        mHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void registerUser(String email, String password) {
        // email and password pattern is valid, show progress dialog  and start registering user
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, dismiss dialog and start Register Activity
                        progressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();

                        assert user != null;
                        Toast.makeText(RegisterActivity.this, "Register...\n" + user.getEmail(), Toast.LENGTH_SHORT);
                        startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
            //error, dismiss progress dialog and get and show the error message
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();    //go previous Activity
        return super.onSupportNavigateUp();
    }
}