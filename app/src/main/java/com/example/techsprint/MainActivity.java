package com.example.techsprint;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private  Button logOut  ;
    FirebaseAuth auth ;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userEmail = user.getEmail();

    private EditText caseId ,department ;
    private Button caseSearchButton;

    private FirebaseFirestore db;
    private DocumentReference documentReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOut =findViewById(R.id.logoutbtn);

        auth =FirebaseAuth.getInstance();
        logOut.setOnClickListener((view -> logoutfunction()));


        // case search activity

        caseId = findViewById(R.id.caseIdEditText);

        department =findViewById(R.id.departmentEditText);

        db = FirebaseFirestore.getInstance();

        // Reference to your Firestore document
       // documentReference = db.collection("romit").document("naya document");

        caseSearchButton = findViewById(R.id.caseSearchButton);

        caseSearchButton.setOnClickListener((view -> casesearchFunction()));





    }
    void logoutfunction(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this,     "Logout successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this,SplashActivity.class));
    }
    void caseSearchingFunction(){
        startActivity(new Intent(MainActivity.this,AddDataToCase.class));
    }



    void casesearchFunction() {
        String dataString = caseId.getText().toString();
        String departmentName = department.getText().toString();

        if (!dataString.isEmpty()) {
            // Reference to the Firestore document
            DocumentReference documentReference = db.collection(departmentName).document(dataString);

            // Create a data map for the new entry
            Map<String, Object> newDataMap = new HashMap<>();
            newDataMap.put("data_key", dataString);
            newDataMap.put("department", departmentName);
            newDataMap.put("user_email", userEmail);
            newDataMap.put("timestamp", FieldValue.serverTimestamp());

            // Add the new entry to a subcollection named "entries"
            documentReference.collection("entries")
                    .add(newDataMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Data added successfully
                            Log.d(TAG, "New entry added with ID: " + documentReference.getId());
                            Toast.makeText(MainActivity.this, "New entry added with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, AddDataToCase.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle errors here
                            Log.w(TAG, "Error adding new entry", e);
                            Toast.makeText(MainActivity.this, "Error adding new entry", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Handle empty input
            Toast.makeText(this, "Please enter data in the input field", Toast.LENGTH_SHORT).show();
        }
    }


}