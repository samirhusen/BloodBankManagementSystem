package com.example.bloodbankmanagementsystem.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodbankmanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    Button submit_button;
    ProgressBar progressBar;
    Button register;
    FirebaseAuth mAuth;
    TextView mforgot_password;

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (mAuth.getCurrentUser() != null) {
//            finish();
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.email);
        passwordEt = findViewById(R.id.password);
        submit_button = findViewById(R.id.submit_button);
        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mforgot_password = findViewById(R.id.forgot_password);

        mforgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter your valid Email To get Reset link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and send reset link to rye provided email

                        String mail = resetMail.getText().toString();

                        if (TextUtils.isEmpty(mail)) {
                            Toast.makeText(LoginActivity.this, "Email address is required to reset password", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset link is sent to your Email address.", Toast.LENGTH_SHORT).show();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error ! Reset link is not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });

//        if (FirebaseAuth.getInstance().getCurrentUser() != null ) {
//            startActivity (new Intent(this, MainActivity.class));
//            this.finish();
//        }

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                emailEt.setError(null);
//                passwordEt.setError(null);
                final String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailEt.setError("Email address is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordEt.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    passwordEt.setError("Password must be greater than six digit chracters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE); //progress bar visibility

                //firebase user authentication
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(LoginActivity.this, "You are Logged in Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    //PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("email",email).apply();
                                    //LoginActivity.this.finish();
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE); //progress bar visibility
                                }
                                // ...
                            }
                        });
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

    }
}


//    private boolean isValid(String number, String password) {
//        if (number.isEmpty()) {
//            showMessage("Empty Email Address");
//            emailEt.setError("Empty Email Address");
//            return false;
//        } else if (password.isEmpty()) {
//            showMessage("Empty Password");
//            passwordEt.setError("Empty Password");
//            return false;
//        }
//        return true;
//    }


//    private void showMessage(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }



