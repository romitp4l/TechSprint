package com.example.techsprint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddDataToCase extends AppCompatActivity {

    private ImageButton  imageButton;
    private Button sendMessage
            ;
    private RecyclerView recyclerView ;
    private EditText  writeMessage ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_to_case);
        imageButton =findViewById(R.id.imageButton);
        sendMessage =findViewById(R.id.addMessageButton);
        recyclerView =findViewById(R.id.recycleView);
        writeMessage =findViewById(R.id.typeMessageEditText);


        imageButton.setOnClickListener((view -> addImageFunction()));
        sendMessage.setOnClickListener((view -> sendMessageFunction()));


        // fetching data from firebase to show in the recycler view
        //recyclerView

        String message = writeMessage.getText().toString();



    }
    void addImageFunction(){
        //adding image to fire base
    }
    void sendMessageFunction(){
        // sending message to fire base

    }
}