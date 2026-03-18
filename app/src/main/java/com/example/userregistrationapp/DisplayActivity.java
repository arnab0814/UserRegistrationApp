package com.example.userregistrationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class DisplayActivity extends AppCompatActivity {

    private TextView tvName, tvDob, tvEmail, tvCounter;
    private Button btnBack, btnNext, btnPrevious;
    private ArrayList<HashMap<String, String>> userList;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // Initialize views
        tvName = findViewById(R.id.tvName);
        tvDob = findViewById(R.id.tvDob);
        tvEmail = findViewById(R.id.tvEmail);
        tvCounter = findViewById(R.id.tvCounter);
        btnBack = findViewById(R.id.btn_back);
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);

        // Get data from database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        userList = dbHelper.getAllUsers();

        // If no users in database, get from Intent (for latest entry)
        if (userList.isEmpty()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("name", getIntent().getStringExtra("name"));
            user.put("dob", getIntent().getStringExtra("dob"));
            user.put("email", getIntent().getStringExtra("email"));
            userList.add(user);
        }


        // Display first user
        displayUser(currentIndex);

        // Next button - show next user
        btnNext.setOnClickListener(v -> {
            if (currentIndex < userList.size() - 1) {
                currentIndex++;
                displayUser(currentIndex);
            } else {
                Toast.makeText(DisplayActivity.this, "No more users", Toast.LENGTH_SHORT).show();
            }
        });

        // Previous button - show previous user
        btnPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                displayUser(currentIndex);
            } else {
                Toast.makeText(DisplayActivity.this, "No previous users", Toast.LENGTH_SHORT).show();
            }
        });

        // Back button - go to blank registration page
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
    public DisplayActivity() {
        super();
    }

    private void displayUser(int index) {
        if (index >= 0 && index < userList.size()) {
            HashMap<String, String> user = userList.get(index);

            tvName.setText(user.get("name"));
            tvDob.setText(user.get("dob"));
            tvEmail.setText(user.get("email"));

            // Update counter
            tvCounter.setText("User " + (index + 1) + " of " + userList.size());

            // Enable/Disable buttons
            btnPrevious.setEnabled(index > 0);
            btnNext.setEnabled(index < userList.size() - 1);
        }
    }
}