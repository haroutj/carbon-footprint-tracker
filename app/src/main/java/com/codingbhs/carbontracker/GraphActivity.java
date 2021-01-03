package com.codingbhs.carbontracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_graph);

        FloatingActionButton prevBtn = findViewById(R.id.prev_btn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        List<EntryClass> entries = new TableControllerEntries(this).read();

        GraphView graph = findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();

        if (entries.size() > 0) {
            for (int i = entries.size() - 1; i >= 0; i--) {

                EntryClass obj = entries.get(i);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, 2000 + Integer.parseInt((obj.date).substring(6)));
                cal.set(Calendar.MONTH, Integer.parseInt((obj.date).substring(0, 2)) - 1);
                cal.set(Calendar.DATE, Integer.parseInt((obj.date).substring(3, 5)));
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                double lbCO2 = obj.lbCO2;

                series.appendData(new DataPoint(cal.getTimeInMillis(), lbCO2), true, entries.size());
            }
        }

        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));

        TextView tipText = findViewById(R.id.tip_text);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            graph.getViewport().setScalable(true);
            graph.getViewport().setScrollable(true);
            graph.getViewport().setScalableY(true);
            graph.getViewport().setScrollableY(true);

            tipText.setVisibility(View.GONE);

            graph.getGridLabelRenderer().setNumHorizontalLabels(5);
        }
        else {
            graph.getViewport().setScalable(false);
            graph.getViewport().setScrollable(false);
            graph.getViewport().setScalableY(false);
            graph.getViewport().setScrollableY(false);

            tipText.setVisibility(View.VISIBLE);

            graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        }
/*
        if (entries.size() > 0) {
            EntryClass obj = entries.get(entries.size() - 1);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2000 + Integer.parseInt((obj.date).substring(6)));
            cal.set(Calendar.MONTH, Integer.parseInt((obj.date).substring(0, 2)) - 1);
            cal.set(Calendar.DATE, Integer.parseInt((obj.date).substring(3, 5)));
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            EntryClass obj2 = entries.get(0);

            Calendar cal2 = Calendar.getInstance();
            cal2.set(Calendar.YEAR, 2000 + Integer.parseInt((obj2.date).substring(6)));
            cal2.set(Calendar.MONTH, Integer.parseInt((obj2.date).substring(0, 2)) - 1);
            cal2.set(Calendar.DATE, Integer.parseInt((obj2.date).substring(3, 5)));
            cal2.set(Calendar.HOUR_OF_DAY, 0);
            cal2.set(Calendar.MINUTE, 0);
            cal2.set(Calendar.SECOND, 0);
            cal2.set(Calendar.MILLISECOND, 0);

            graph.getViewport().setMinX(cal.getTimeInMillis());
            graph.getViewport().setMaxX(cal2.getTimeInMillis());

            graph.getViewport().setXAxisBoundsManual(true);
        }
 */
        graph.getViewport().setMinY(0);
        graph.getViewport().setXAxisBoundsManual(false);
        graph.getViewport().setYAxisBoundsManual(true);

    }
}
