package com.example.techsprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private  Button logOut ,caseSearchButton ;
    FirebaseAuth auth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOut =findViewById(R.id.logoutbtn);
        caseSearchButton =findViewById(R.id.searchBtn);
        auth =FirebaseAuth.getInstance();
        logOut.setOnClickListener((view -> logoutfunction()));

        caseSearchButton.setOnClickListener((view -> caseSearchingFunction()));
    }
    void logoutfunction(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this,     "Logout successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this,SplashActivity.class));
    }
    void caseSearchingFunction(){
        startActivity(new Intent(MainActivity.this,AddDataToCase.class));
    }
}