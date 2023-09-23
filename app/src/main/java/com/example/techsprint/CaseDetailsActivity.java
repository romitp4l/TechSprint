package com.example.techsprint;

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
import java.util.List;

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

    private String departmentName = "";
    private String dataString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_details);

        // Retrieve extras from MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            departmentName = extras.getString("departmentName", "");
            dataString = extras.getString("dataString", "");
        }

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

        // Get the case document reference
        caseDocumentRef = db.collection(departmentName).document(dataString);

        // Load existing messages
        loadMessages(departmentName, dataString);

        newMessageEditText = findViewById(R.id.newMessageEditText);
        sendMessageButton = findViewById(R.id.sendMessageButton);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = newMessageEditText.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    addMessageToFirestore(messageText, dataString);
                    newMessageEditText.setText(""); // Clear the input field after sending the message
                }
            }
        });
    }

    private void loadMessages(String department, String caseId) {
        // Reference to the Firestore document for the specific case ID in the department
        DocumentReference caseDocumentRef = db.collection(department)
                .document(caseId);

        // Reference to the "messages" subcollection
        CollectionReference messagesRef = caseDocumentRef.collection("messages");

        // Query Firestore to fetch existing messages and add them to messageList
        messagesRef.orderBy("timestamp", Query.Direction.ASCENDING)
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


    private void addMessageToFirestore(String messageText, String caseId) {
        // Create a new Message object
        Message newMessage = new Message(messageText, userEmail, new Date());

        // Create the Firestore document reference for the specific chat room (case)
        DocumentReference chatRoomRef = db.collection(departmentName).document(caseId);

        // Add the new message to the "messages" subcollection of the chat room
        chatRoomRef.collection("messages")
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
