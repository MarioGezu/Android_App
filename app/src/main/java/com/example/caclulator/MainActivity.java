package com.example.caclulator;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText studentDiscounts, travelExpenses, ticketExpenses, numberOfStudents, numberOfGrownUps, houseExpenses;
    TextView textView, textView2, textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }
    public void initialize(){
        studentDiscounts = (EditText)findViewById(R.id.studentDiscounts);
        travelExpenses = (EditText)findViewById(R.id.travelExpenses);
        ticketExpenses = (EditText)findViewById(R.id.ticketExpenses);
        numberOfStudents = (EditText)findViewById(R.id.numberOfStudents);
        numberOfGrownUps = (EditText)findViewById(R.id.numberOfGrownUps);
        houseExpenses = (EditText)findViewById(R.id.houseExpenses);
        textView = (TextView)findViewById(R.id.textView);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
    }
}

