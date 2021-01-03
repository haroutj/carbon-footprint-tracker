package com.codingbhs.carbontracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_database);

        FloatingActionButton prevBtn = findViewById(R.id.prev_btn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FloatingActionButton deleteAllBtn = findViewById(R.id.delete_all_btn);
        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askOption().show();
            }
        });

        FloatingActionButton graphBtn = findViewById(R.id.graph_btn);
        graphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DatabaseActivity.this, GraphActivity.class));
            }
        });

        readRecords();
    }

    public void readRecords() {

        LinearLayout linearLayoutRecords = findViewById(R.id.ll_entries);
        linearLayoutRecords.removeAllViews();

        List<EntryClass> entries = new TableControllerEntries(this).read();

        if (entries.size() > 0) {

            for (EntryClass obj : entries) {

                final int id = obj.id;
                String formattedDate = obj.date;
                double lbCO2 = obj.lbCO2;
                double miles = obj.miles;
                double mpg = obj.mpg;
                String radio = obj.radio;

                TextView dateText = new TextView(this);
                dateText.setPadding(25, 0, 0, 5);
                dateText.setText(formattedDate);
                dateText.setTextColor(Color.parseColor("#808080"));
                dateText.setTag(Integer.toString(id));
                dateText.setTextSize(12);

                linearLayoutRecords.addView(dateText);

                TextView lbCO2Text = new TextView(this);
                lbCO2Text.setPadding(0, 0, 0, 10);
                lbCO2Text.setText(lbCO2 + " pounds of CO2 produced");
                lbCO2Text.setTextColor(Color.parseColor("#FF000000"));
                lbCO2Text.setTag(Integer.toString(id));
                lbCO2Text.setTextSize(18);

                linearLayoutRecords.addView(lbCO2Text);

                final TextView textViewItem = new TextView(this);
                textViewItem.setPadding(0, 0, 0, 20);
                textViewItem.setText(miles + " miles driven in " + mpg + " MPG " + radio + " vehicle");
                textViewItem.setTextColor(Color.parseColor("#808080"));
                textViewItem.setTextSize(14);
                textViewItem.setTag(Integer.toString(id));

                linearLayoutRecords.addView(textViewItem);

                Button delete = new Button(this);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Context context = view.getRootView().getContext();
                        boolean deleteSuccessful = new TableControllerEntries(context).delete(Integer.parseInt(textViewItem.getTag().toString()));

                        if (deleteSuccessful) {
                            Toast.makeText(context, "Entry deleted.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Unable to delete entry!", Toast.LENGTH_SHORT).show();
                        }

                        readRecords();
                    }
                });
                delete.setPadding(40, 0, 0, 70);
                delete.setText("DELETE");
                delete.setBackground(null);
                delete.setGravity(Gravity.NO_GRAVITY);
                delete.setTag(Integer.toString(id));

                linearLayoutRecords.addView(delete);
            }

        } else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(0, 0, 0, 0);
            locationItem.setGravity(Gravity.CENTER_HORIZONTAL);
            locationItem.setText("No entries yet.");

            linearLayoutRecords.addView(locationItem);
        }

    }

    private AlertDialog askOption()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete All")
                .setMessage("Do you want to delete all entries?")
                .setIcon(null)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteAll();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }

    private void deleteAll() {
        LinearLayout linearLayoutRecords = findViewById(R.id.ll_entries);
        linearLayoutRecords.removeAllViews();

        List<EntryClass> entries = new TableControllerEntries(this).read();

        for (EntryClass obj : entries) {
            Context context = getApplicationContext();
            new TableControllerEntries(context).delete(Integer.parseInt((Integer.toString(obj.id))));
        }

        readRecords();
    }
}
