package com.example.waterplantreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    public static List<Plant> plantList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PlantAdapter adapter;

    private static final String wsdl_url = "http://192.168.0.106:9000/PlantService?wsdl";
    private static final String name_space = "http://control/";

    public static final int UPDATE_TEXT = 99;

    private int notiHour;
    private int notiMin;
    private Calendar nextNoti;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler(){

            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case UPDATE_TEXT:
                        refreshPlants();
                        break;
                        default:
                            break;
                }
            }
        };


        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                refreshPlants();
            }
        });

        initPlant();

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlantAdapter(plantList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(resultCode, data);
            final String qrContent = scanResult.getContents();
            Toast.makeText(this, "Result: " + qrContent, Toast.LENGTH_SHORT).show();
            requestWebService(qrContent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add_plant_on_toolbar:
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.calendar_on_toolbar:
                Intent intent2 = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent2);
                break;
            case R.id.order_plant_on_toolbar:
                showPopupMenu(toolbar.findViewById(R.id.order_plant_on_toolbar));
                break;
            case R.id.settings_in_menu:
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        Toast.makeText(MainActivity.this, hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
                        notiHour = hourOfDay;
                        notiMin = minute;
                        for(Plant plant : plantList){
                            Calendar nextWater = plant.getNextWater();
                            nextWater.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            nextWater.set(Calendar.MINUTE, minute);
                            nextWater.set(Calendar.SECOND, 0);
                            plant.setNextWater(nextWater);

                            Calendar nextFeed = plant.getNextFeed();
                            nextFeed.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            nextFeed.set(Calendar.MINUTE, minute);
                            nextFeed.set(Calendar.SECOND, 0);
                            plant.setNextFeed(nextFeed);
//                            Log.e("debug", "" + nextWater.getTime());
                            plant.save();
                        }
                        findNext();
                    }
                }, (notiHour >= 0 )? notiHour : 0, (notiMin >= 0)? notiMin : 0, true).show();
                break;
            case R.id.start_service_in_menu:
                if(notiHour >= 0){
                    setReminder(true);
                    Toast.makeText(MainActivity.this, "notification on", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "please set the notification time at first", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.stop_service_in_menu:
                setReminder(false);
                Toast.makeText(MainActivity.this, "notification off ", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return true;
    }

    private void requestWebService(final String plantName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = WebService.questToWebService(wsdl_url, name_space,"plantService", plantName);

                if(!result.equals("")){
                    String item[] = result.split(",");
                    String name = item[0];
                    double water = Double.parseDouble(item[1]);
                    double feed = Double.parseDouble(item[2]);
                    int waterQ = Integer.parseInt(item[3]);
                    int feedQ = Integer.parseInt(item[4]);
                    String tips = "";
                    for(int i = 5; i < item.length - 1; i ++){
                        tips += item[i];
                    }
                    String website = item[item.length - 1];
                    Plant plant = new Plant(name, water, feed, waterQ, feedQ, tips, website);
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.default_plant_image);
                    plant.setImageBitmap(bmp);
                    plant.setPlace("default");
                    Calendar calendar = Calendar.getInstance();
                    if(notiHour >= 0){
                        calendar.set(Calendar.HOUR_OF_DAY, notiHour);
                        calendar.set(Calendar.MINUTE, notiMin);
                    }
                    plant.setNextFeed(calendar);
                    plant.setNextWater(calendar);
                    plant.save();


                    plantList.add(plant);
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, plant.toString(), Toast.LENGTH_SHORT).show();
                    Looper.loop();

                    Message message = new Message();
                    message.what = UPDATE_TEXT;
                    handler.sendMessage(message);

                }
            }
        }).start();
    }


    private void showPopupMenu(View view){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.sort_by_name:
                        sortPlantList("name", 1);
                        break;
                    case R.id.sort_by_name_r:
                        sortPlantList("name", -1);
                        break;
                    case R.id.sort_by_place:
                        sortPlantList("place", 1);
                        break;
                    case R.id.sort_by_place_r:
                        sortPlantList("place", -1);
                        break;
                    case R.id.sort_by_water:
                        sortPlantList("waterTime", 1);
                        break;
                    case R.id.sort_by_feed:
                        sortPlantList("feedTime", 1);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void sortPlantList(String key, final int order){
        if(key.equals("name")){
            plantList.sort(new Comparator<Plant>() {
                @Override
                public int compare(Plant o1, Plant o2) {
                    return o1.getName().compareTo(o2.getName()) * order;
                }
            });
        } else if(key.equals("place")){
            plantList.sort(new Comparator<Plant>() {
                @Override
                public int compare(Plant o1, Plant o2) {
                    return o1.getPlace().compareTo(o2.getPlace()) * order;
                }
            });
        } else if(key.equals("waterTime")){
            plantList.sort(new Comparator<Plant>() {
                @Override
                public int compare(Plant o1, Plant o2) {
                    return (o1.getNextWater().get(Calendar.YEAR) - o2.getNextWater().get(Calendar.YEAR)) * 365
                            + (o1.getNextWater().get(Calendar.DAY_OF_YEAR) - o2.getNextWater().get(Calendar.DAY_OF_YEAR));
                }
            });
        } else if(key.equals("feedTime")){
            plantList.sort(new Comparator<Plant>() {
                @Override
                public int compare(Plant o1, Plant o2) {
                    return (o1.getNextFeed().get(Calendar.YEAR) - o2.getNextFeed().get(Calendar.YEAR)) * 365
                            + (o1.getNextFeed().get(Calendar.DAY_OF_YEAR) - o2.getNextFeed().get(Calendar.DAY_OF_YEAR));
                }
            });
        }
        adapter.notifyDataSetChanged();
        Log.e("thread", "resort.... " );
    }

    private void findNext(){
        nextNoti = Calendar.getInstance();
        nextNoti.set(Calendar.YEAR, nextNoti.get(Calendar.YEAR) + 1);
        for(Plant plant : plantList){
            if(plant.getNextFeed().before(nextNoti)){
                nextNoti = plant.getNextFeed();
            }

            if(plant.getNextWater().before(nextNoti)){
                nextNoti = plant.getNextWater();
            }
        }
    }

    private void initPlant(){
        LitePal.getDatabase();
        plantList = DataSupport.findAll(Plant.class);

        for(Plant plant : plantList){
            setNextTime(plant);

        }

        findNext();

        if(plantList.size() == 0){
            notiHour = -1;
            notiMin = -1;
        } else {
            notiHour = plantList.get(0).getNextWater().get(Calendar.HOUR_OF_DAY);
            notiMin = plantList.get(0).getNextWater().get(Calendar.MINUTE);
        }


    }

    private void refreshPlants(){
//        Log.e("thread", "refreshPlants.... " );
//        Thread refreshThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    Thread.sleep(1000);
//                } catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        initPlant();
//                        adapter.notifyDataSetChanged();
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                });
//            }
//        });
//        refreshThread.start();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }



    private void setNextTime(Plant plant){
        Calendar today = Calendar.getInstance();
//        Log.e("debug", "today" + today.getTime());
//        Log.e("debug", "nextwater" + plant.getNextWater().getTime());
        if(today.after(plant.getNextWater())){
            Calendar nextWater = Calendar.getInstance();
            nextWater.set(Calendar.DAY_OF_YEAR, (int) (today.get(Calendar.DAY_OF_YEAR) + Math.ceil(7 / plant.getWaterPerWeek())));
            plant.setNextWater(nextWater);

        }

        if(today.after(plant.getNextFeed())){
            Calendar nextFeed = Calendar.getInstance();
            nextFeed.set(Calendar.DAY_OF_YEAR, (int) (today.get(Calendar.DAY_OF_YEAR) + Math.ceil(7 / plant.getFeedPerWeek())));
            plant.setNextFeed(nextFeed);
        }

    }

    private void setReminder(boolean b) {

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(this,AlarmReceiver.class), 0);
        if(b){
            am.set(AlarmManager.RTC_WAKEUP, nextNoti.getTimeInMillis(),pi);
//            Log.e("debug", "nextnoti" + nextNoti.getTime());
        }
        else{
            // cancel current alarm
            am.cancel(pi);
        }

    }

}
