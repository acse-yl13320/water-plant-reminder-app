package com.example.waterplantreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private List<Plant> plantList;
    private TextView textView;
    private HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        textView = findViewById(R.id.to_do_list);

        plantList = MainActivity.plantList;

        map = listToMap(plantList);

        //Toast.makeText(CalendarActivity.this, map.toString(), Toast.LENGTH_LONG).show();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView,
                                            int year, int month, int dayOfMonth) {
                Toast.makeText(CalendarActivity.this, "" + year + "." + (month + 1) + "." + dayOfMonth, Toast.LENGTH_SHORT).show();
                //Toast.makeText(CalendarActivity.this, map.toString(), Toast.LENGTH_LONG).show();
                String key = "" + year + month + dayOfMonth;
                textView.setText(map.get(key));
                //Toast.makeText(CalendarActivity.this, map.get(key), Toast.LENGTH_LONG).show();
            }
        });
    }

    private HashMap<String, String> listToMap(List<Plant> plantList){
        HashMap<String, String> map = new HashMap<>();

        for(Plant plant : plantList){
            Calendar calendar = plant.getNextFeed();
            String feedKey = "" + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH);
            calendar = plant.getNextWater();
            String waterKey = "" + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH);

            if(map.get(waterKey) == null){
                String toDo = "Water " + plant.getName() + " on this day in " + plant.getPlace() + ", quantity: " + plant.getWaterQuantity() + "mL\n";
                map.put(waterKey, toDo);
            } else {
                String toDo = map.get(waterKey);
                toDo += "Water " + plant.getName() + " on this day in " + plant.getPlace() + ", quantity: " + plant.getWaterQuantity() + "mL\n";
                map.put(waterKey, toDo);
            }

            if(map.get(feedKey) == null){
                String toDo = "Feed " + plant.getName() + " on this day in " + plant.getPlace() + ", quantity: " + plant.getFeedQuantity() + "g\n";
                map.put(feedKey, toDo);
            } else {
                String toDo = map.get(feedKey);
                toDo += "Feed " + plant.getName() + " on this day in " + plant.getPlace() + ", quantity: " + plant.getFeedQuantity() + "g\n";
                map.put(feedKey, toDo);
            }

        }
        return map;
    }

}
