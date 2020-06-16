package com.example.bloodbankmanagementsystem.Activities;

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
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodbankmanagementsystem.HelperClass.UserHelperClass;
import com.example.bloodbankmanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText nameEt, cityEt, emailEt, numberEt, bloodGroupET, passwordEt;
    Button submitButton;
    TextView sign_up_text;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEt = findViewById(R.id.name);
        cityEt = findViewById(R.id.city);
        emailEt = findViewById(R.id.email);
        bloodGroupET = findViewById(R.id.blood_group);
        passwordEt = findViewById(R.id.password);
        numberEt = findViewById(R.id.number);
        submitButton = findViewById(R.id.submit_button);

        mAuth = FirebaseAuth.getInstance();//current instance of database

        sign_up_text = findViewById(R.id.sign_up_text);
        progressBar = findViewById(R.id.progressBar);

//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString().trim();
                String city = cityEt.getText().toString().trim();
                String email = emailEt.getText().toString().trim();
                String blood_group = bloodGroupET.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String number = numberEt.getText().toString().trim();

                List<String> valid_blood_groups = new ArrayList<>();
                valid_blood_groups.add("A+");
                valid_blood_groups.add("A-");
                valid_blood_groups.add("B+");
                valid_blood_groups.add("B-");
                valid_blood_groups.add("AB+");
                valid_blood_groups.add("AB-");
                valid_blood_groups.add("O+");
                valid_blood_groups.add("O-");//bloodgroup validation

                if (TextUtils.isEmpty(name)) {
                    emailEt.setError("Name is empty");
                    return;
                }
                if (TextUtils.isEmpty(city)) {
                    cityEt.setError("City name is required");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    emailEt.setError("Email address is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    emailEt.setError("Password is required");
                    return;
                }
                if (!valid_blood_groups.contains(blood_group)) {
                    bloodGroupET.setError("Blood group invalid choose from " + valid_blood_groups);
                    return;
                }
                if (number.length() != 10) {
                    numberEt.setError("Invalid mobile number, number should be of 10 digits");
                    return;
                }
                if (password.length() < 6) {
                    passwordEt.setError("Password must be greater than six digit chracters");
                    return;
                }

                //save data to firebase
                database = FirebaseDatabase.getInstance();
                ref = database.getReference("users");
                UserHelperClass helperClass = new UserHelperClass(name, city, email, password, number, blood_group);
                ref.child(name).setValue(helperClass);
                //store the information in the database

                progressBar.setVisibility(View.VISIBLE); //progress bar visibility

                //firebase: register the user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //finish();
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(RegisterActivity.this, "You are registered Succsessfully !!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    RegisterActivity.this.finish();
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "Error !" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE); //progress bar visibility
                                    // Toast.makeText(EmailPasswordActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            });

        sign_up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }
}
