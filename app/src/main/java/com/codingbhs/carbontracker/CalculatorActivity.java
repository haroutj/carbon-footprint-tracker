package com.codingbhs.carbontracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_calculator);

        FloatingActionButton prevBtn = findViewById(R.id.prev_btn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Button saveBtn = findViewById(R.id.save_btn);

        final int[] selRadioId = {((RadioGroup)findViewById(R.id.radio_group)).getCheckedRadioButtonId()};
        final double[] miles = {-1};
        final double[] mpg = {-1};
        final double[] lbCO2 = {-1};
        final double[] monthsCO2 = {-1};

        Button calcBtn = findViewById(R.id.calc_btn);
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miles[0] = -1;
                mpg[0] = -1;

                try {
                    miles[0] = Double.parseDouble(((EditText)findViewById(R.id.miles_in)).getText().toString());} catch (NumberFormatException e) {
                    miles[0] = -1;}
                try {
                    mpg[0] = Double.parseDouble(((EditText)findViewById(R.id.mpg_in)).getText().toString());} catch (NumberFormatException e) {
                    mpg[0] = -1;}
                selRadioId[0] = ((RadioGroup)findViewById(R.id.radio_group)).getCheckedRadioButtonId();
                lbCO2[0] = (int)(1000 * CalculatorActivity.this.calcCO2(miles[0], mpg[0], selRadioId[0])) / (double)1000;
                monthsCO2[0] = (int)(1000 * CalculatorActivity.this.calcMonths(lbCO2[0])) / (double)1000;

                TextView calcText = findViewById(R.id.calc_text);
                if (miles[0] != -1 && mpg[0] != -1) {
                    if (saveBtn.getVisibility() == View.GONE) {
                        saveBtn.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.riseout));
                    }
                    saveBtn.setVisibility(View.VISIBLE);
                    calcText.setTextColor(Color.parseColor("#8a000000"));
                    calcText.setText("You have contributed about " + lbCO2[0] + " pounds of CO2 with this entry. It would take a single mature tree about " + monthsCO2[0] + " months to absorb this amount of CO2 from the atmosphere.");
                }
                else {
                    if (saveBtn.getVisibility() == View.VISIBLE) {
                        saveBtn.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sinkin));
                    }
                    saveBtn.setVisibility(View.GONE);
                    calcText.setText("Please input entry values to receive a calculation.");
                    calcText.setTextColor(Color.parseColor("#B00020"));
                }
            }
        });

        final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miles[0] = -1;
                mpg[0] = -1;

                try {
                    miles[0] = Double.parseDouble(((EditText)findViewById(R.id.miles_in)).getText().toString());} catch (NumberFormatException e) {
                    miles[0] = -1;}
                try {
                    mpg[0] = Double.parseDouble(((EditText)findViewById(R.id.mpg_in)).getText().toString());} catch (NumberFormatException e) {
                    mpg[0] = -1;}
                selRadioId[0] = ((RadioGroup)findViewById(R.id.radio_group)).getCheckedRadioButtonId();
                lbCO2[0] = (int)(1000 * CalculatorActivity.this.calcCO2(miles[0], mpg[0], selRadioId[0])) / (double)1000;
                monthsCO2[0] = (int)(1000 * CalculatorActivity.this.calcMonths(lbCO2[0])) / (double)1000;

                TextView calcText = findViewById(R.id.calc_text);
                if (miles[0] != -1 && mpg[0] != -1) {
                    if (saveBtn.getVisibility() == View.GONE) {
                        saveBtn.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.riseout));
                    }
                    saveBtn.setVisibility(View.VISIBLE);
                    calcText.setTextColor(Color.parseColor("#8a000000"));
                    calcText.setText("You have contributed about " + lbCO2[0] + " pounds of CO2 with this entry. It would take a single mature tree about " + monthsCO2[0] + " months to absorb this amount of CO2 from the atmosphere.");
                    EntryClass obj = new EntryClass();
                    obj.date = formatter.format(Calendar.getInstance().getTime());
                    obj.lbCO2= lbCO2[0];
                    obj.miles= miles[0];
                    obj.mpg = mpg[0];
                    if (selRadioId[0] == (findViewById(R.id.gas_rbtn)).getId()) {
                        obj.radio = "Gasoline";
                    }
                    else if (selRadioId[0] == (findViewById(R.id.diesel_rbtn)).getId()) {
                        obj.radio = "Diesel";
                    }
                    else {
                        obj.radio = "Autogas";
                    }

                    Context context = view.getRootView().getContext();
                    boolean createSuccessful = new TableControllerEntries(context).create(obj);

                    if (createSuccessful)
                        Toast.makeText(context, "Entry saved.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Entry error!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (saveBtn.getVisibility() == View.VISIBLE) {
                        saveBtn.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sinkin));
                    }
                    saveBtn.setVisibility(View.GONE);
                    calcText.setText("Please input entry values to receive a calculation.");
                    calcText.setTextColor(Color.parseColor("#B00020"));
                }
            }
        });

        Button viewDbBtn = findViewById(R.id.view_db_btn);

        viewDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalculatorActivity.this, DatabaseActivity.class));
            }
        });
    }

    private double calcCO2(double miles, double mpg, int selRadioId) {
        if (selRadioId == (findViewById(R.id.gas_rbtn)).getId()) {
            return miles/mpg * 18.95;
        }
        else if (selRadioId == (findViewById(R.id.diesel_rbtn)).getId()) {
            return miles/mpg * 22.38;
        }
        else {
            return miles/mpg * 12.72;
        }
    }

    private double calcMonths(double lbCO2) {
        return lbCO2 / 48 * 12;
    }
}
