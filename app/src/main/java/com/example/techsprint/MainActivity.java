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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private  Button logOut  ;
    FirebaseAuth auth ;

    private EditText caseId ;
    private Button caseSearchButton;

    private FirebaseFirestore db;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOut =findViewById(R.id.logoutbtn);

        auth =FirebaseAuth.getInstance();
        logOut.setOnClickListener((view -> logoutfunction()));


        // case search activity

        caseId = findViewById(R.id.caseIdEditText);

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

        void casesearchFunction() {
            String dataString = caseId.getText().toString();

            if (!dataString.isEmpty()) {
                // Use the EditText value as the document name
                documentReference = db.collection("romit").document(dataString);

                // Create a data map
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("data_key", dataString);

                // Upload the data to Firestore
                documentReference
                        .set(dataMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Data uploaded successfully
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(MainActivity.this, "Document snapshot added with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, AddDataToCase.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle errors here
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(MainActivity.this, "Error adding document", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // Handle empty input
                Toast.makeText(this, "Please enter data in the input field", Toast.LENGTH_SHORT).show();
            }
        }



    }

}