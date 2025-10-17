package com.example.caclulator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public class MainActivity extends AppCompatActivity {

    EditText studentDiscounts, travelExpenses, ticketExpenses, numberOfStudents, numberOfGrownUps, houseExpenses, budget, time;
    TextView textView, textView2, textView3, textView4, textView5;

    Button button, button2;


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
        textView4 = (TextView)findViewById(R.id.textView4);
        textView5 = (TextView)findViewById(R.id.textView5);
        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        budget = (EditText)findViewById(R.id.budget);
        time = (EditText)findViewById(R.id.time);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView4.setText("Eredmények");
                numberOfStudents.setText("diákok száma");
                numberOfGrownUps.setText("tanár/kisérők száma");
                textView5.setText("Zsebpénz állapota");
                studentDiscounts.setText("kedvezmény");
                travelExpenses.setText("utazási kölcség");
                ticketExpenses.setText("belépői költség");
                houseExpenses.setText("szállási költség");
                time.setText("hátra lévő idő(hónap)");
                budget.setText("zsebpénz");
            }
        });

    }

    public void onClickButton(){
        //textView4.setText("Felőnttek száma: " + numberOfGrownUps.getText().toString());

        int num= Integer.parseInt(numberOfGrownUps.getText().toString());
        int num2= Integer.parseInt(numberOfStudents.getText().toString());
        int num3= Integer.parseInt(travelExpenses.getText().toString());
        int num4= Integer.parseInt(ticketExpenses.getText().toString());
        int num5= Integer.parseInt(houseExpenses.getText().toString());
        int num7= Integer.parseInt(budget.getText().toString());
        int num8= Integer.parseInt(time.getText().toString());
        double num6= Double.parseDouble(studentDiscounts.getText().toString());

        int count = num + num2;
        int expences = (num3 * count) + (num4 * count) + (num5 * count);
        double finalprice = expences - (expences * (num6 / 100));
        double timeRemain = Math.round(finalprice / num8);

        if(num7 < finalprice){
           textView5.setText("Nincs elég zsebpénz!");
       }
       else{
           textView5.setText("Minden rendben!");
       }

        textView4.setText(String.format(
                "\tLétszám"
                +"\nTanár: " + num
                +"\nDiák: " + num2
                +"\nÖsszlétszám: " + count
                +"\n\tKöltségvetés"
                +"\nUtazási költség: " + (num3 * count)
                +"\nBelépő költség: " + (num4 * count)
                +"\nSzállási költség: " + (num5 * count)
                + "\nKedvezmény nélkül: " + expences
                +"\nKedvezmény: " + num6
                +"\nÖsszesen: " + finalprice
                +"\nHónapokra osztva: " + timeRemain
        ));

    }
}

