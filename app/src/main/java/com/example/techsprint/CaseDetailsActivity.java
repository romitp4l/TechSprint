package com.example.techsprint;

import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaseDetailsActivity extends AppCompatActivity {

    private static final String TAG = "CaseDetailsActivity";

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private DocumentReference caseDocumentRef;
    private String userEmail;

    private RecyclerView recyclerView;
    private EditText newMessageEditText;
    private Button sendMessageButton;

    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_details);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Get user email
        if (user != null) {
            userEmail = user.getEmail();
        }

        // Initialize RecyclerView and set its layout manager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);

        // Get the case document reference (Replace "departmentName" and "dataString" with actual values)
        String departmentName = "your_department_name";
        String dataString = "your_data_string";
        caseDocumentRef = db.collection(departmentName).document(dataString);

        // Load existing messages
        loadMessages();

        newMessageEditText = findViewById(R.id.newMessageEditText);
        sendMessageButton = findViewById(R.id.sendMessageButton);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = newMessageEditText.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    addMessageToFirestore(messageText);
                    newMessageEditText.setText(""); // Clear the input field after sending the message
                }
            }
        });
    }

    private void loadMessages() {
        // Query Firestore to fetch existing messages and add them to messageList
        caseDocumentRef.collection("entries")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Handle the error
                            Log.e(TAG, "Error loading messages", e);
                            return;
                        }

                        messageList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Message message = document.toObject(Message.class);
                            messageList.add(message);
                        }
                        messageAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void addMessageToFirestore(String messageText) {
        // Create a new Message object
        Message newMessage = new Message(messageText, userEmail, new Date());

        // Add the new message to Firestore under the "entries" subcollection
        caseDocumentRef.collection("entries")
                .add(newMessage)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Data added successfully
                        Log.d(TAG, "New message added with ID: " + documentReference.getId());
                        Toast.makeText(CaseDetailsActivity.this, "New message sent!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors here
                        Log.e(TAG, "Error adding new message", e);
                        Toast.makeText(CaseDetailsActivity.this, "Error sending message", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
