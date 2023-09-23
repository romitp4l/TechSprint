package com.example.techsprint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button logOut;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userEmail;
    private EditText caseId, department;
    private Button caseSearchButton;
    private FirebaseFirestore db;

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOut = findViewById(R.id.logoutbtn);
        auth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        }

        logOut.setOnClickListener(view -> logoutFunction());

        caseId = findViewById(R.id.caseIdEditText);
        department = findViewById(R.id.departmentEditText);
        db = FirebaseFirestore.getInstance();

        caseSearchButton = findViewById(R.id.caseSearchButton);
        caseSearchButton.setOnClickListener(view -> caseSearchFunction());
    }

    void logoutFunction() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, SplashActivity.class));
    }

    void caseSearchFunction() {
        String dataString = caseId.getText().toString().trim();
        String departmentName = department.getText().toString().trim();

        if (!dataString.isEmpty()) {

            // passing extras for CaseDetailActivity
            Intent intent = new Intent(MainActivity.this, CaseDetailsActivity.class);
            intent.putExtra("departmentName", departmentName);
            intent.putExtra("dataString", dataString);
            startActivity(intent);
            // registering new case to the database


        } else {
            // Handle empty input
            Toast.makeText(this, "Please enter data in the input field", Toast.LENGTH_SHORT).show();
        }
    }
}
