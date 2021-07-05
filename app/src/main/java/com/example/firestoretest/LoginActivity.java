package com.example.firestoretest;
/* Assignment: final
Campus: Ashdod
Author: Matan Tal, ID: 201492881
*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login activity";
    private static final String ACTION_BAR_TITLE = "Login";
    private static final String ERROR_INPUT_EMAIL = "Invalid Email";
    private static final String RECOVER_PASSWORD_DIALOG_TITLE = "Recover Password";
    private static final String HINT_EMAIL = "Email";
    private static final String RECOVER_PASSWORD_DIALOG_POSITIVE_BTN = "Recover";
    private static final String RECOVER_PASSWORD_DIALOG_NEGATIVE_BTN = "Cancel";
    private static final String PROGRESS_DIALOG_SENDING_EMAIL = "Sending email...";
    private static final String PROGRESS_DIALOG_LOGGING_IN = "Logging in...";
    private static final String AUTH_FAILED_TOAST_MESSAGE = "Authentication failed.";
    //views
    TextView mNotHaveAccountTv, mRecoverPasswordTv;
    Button mLoginBtn;
    EditText mEmailEt, mPasswordEt;

    //Declare an instance of FirebaseAuth
    private FirebaseAuth FirebaseAuth;

    //progress dialog
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ActionBar and it's title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(ACTION_BAR_TITLE);
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init
        mEmailEt = findViewById(R.id.login_email_ET);
        mPasswordEt = findViewById(R.id.login_password_Et);
        mLoginBtn = findViewById(R.id.login_loginBtn);
        mNotHaveAccountTv = findViewById(R.id.login_not_have_accountTv);
        mRecoverPasswordTv = findViewById(R.id.login_recover_password_Tv);

        //initialize the FirebaseAuth instance.
        FirebaseAuth = FirebaseAuth.getInstance();

        //login button click
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input data
                String email = mEmailEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                //validate
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //invalid email pattern set error
                    mEmailEt.setError(ERROR_INPUT_EMAIL);
                    mEmailEt.setFocusable(true);
                } else {
                    //valid email
                    LoginActivity.this.loginUser(email, password);
                }
            }
        });

        //not have account click
        mNotHaveAccountTv.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        //recover password textView click
        mRecoverPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });


        //init progress dialog
        progressDialog = new ProgressDialog(this);

    }

    /**
     *
     */
    private void showRecoverPasswordDialog() {
        //AlterDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(RECOVER_PASSWORD_DIALOG_TITLE);
        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views to set in the dialog
        EditText recoverDialogEmailEt = new EditText(this);
        recoverDialogEmailEt.setHint(HINT_EMAIL);
        recoverDialogEmailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        /*
         *   sets the min width of a EditView to fit a text of n 'M' letters
         * regardless of the actual text extension and text size
         */
        recoverDialogEmailEt.setMinEms(16);


        linearLayout.addView(recoverDialogEmailEt);
        linearLayout.setPadding(10, 10, 10, 10);

        builder.setView(linearLayout);

        //button recover
        builder.setPositiveButton(RECOVER_PASSWORD_DIALOG_POSITIVE_BTN, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                String emailAddress = recoverDialogEmailEt.getText().toString().trim();
                Log.d(TAG, "onClick: Recover password send an email to " + emailAddress);
                beginRecovery(emailAddress);
            }
        });
        //button cancel
        builder.setNegativeButton(RECOVER_PASSWORD_DIALOG_NEGATIVE_BTN, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });

        //show dialog
        builder.create().show();
    }

    /**
     *
     * @param emailAddress
     */
    private void beginRecovery(String emailAddress) {
        //show progress dialog
        progressDialog.setMessage(PROGRESS_DIALOG_SENDING_EMAIL);
        progressDialog.show();

        com.google.firebase.auth.FirebaseAuth auth = FirebaseAuth.getInstance();
        Log.d(TAG, "beginRecovery: email address is " + emailAddress);
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    /**
     *
     * @param email
     * @param password
     */
    private void loginUser(String email, String password) {
        //show progress dialog
        progressDialog.setMessage(PROGRESS_DIALOG_LOGGING_IN);
        progressDialog.show();

        FirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //dismiss progress dialog
                        progressDialog.dismiss();
                        // Sign in success, update UI with the signed-in user's information

                        FirebaseUser user = FirebaseAuth.getCurrentUser();
                        //User is sing in, start LoginActivity
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                        finish();
                    } else {
                        //dismiss progress dialog
                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user.

                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(e -> {
            //dismiss progress dialog
            progressDialog.dismiss();
            //error, get and shoe error message
            Toast.makeText(LoginActivity.this, "" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        });
    }

    /**
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();    //go previous Activity
        return super.onSupportNavigateUp();
    }
}