package com.example.userregistrationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etDob, etEmail;
    private Button btnSubmit;

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.et_name);
        etDob = findViewById(R.id.et_dob);  // ✅ Properly assign etDob
        etEmail = findViewById(R.id.et_email);
        btnSubmit = findViewById(R.id.btn_submit);

        // Clear fields when activity is created
        clearFields();

        // Opens date picker when clicking on DOB field
        etDob.setOnClickListener(v -> showDatePickerDialog(etDob));

        btnSubmit.setOnClickListener(v -> submitForm());
    }

    private void clearFields() {
        etName.setText("");
        etDob.setText("");
        etEmail.setText("");
    }

    private void showDatePickerDialog(EditText dobEditText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    // Formats selected date as DD/MM/YYYY
                    String dob = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                    dobEditText.setText(dob);
                },
                year, month, day
        );

        datePickerDialog.show(); // Shows the dialog
    }

    private void submitForm() {
        String name = etName.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        // Validation
        if (name.isEmpty() || dob.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!email.contains("@")) {
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.insertUser(name, dob, email);
        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();

        // Pass data to Activity 2
        Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("dob", dob);
        intent.putExtra("email", email);
        startActivity(intent);

        // Clear fields for next entry
        clearFields();
    }
}