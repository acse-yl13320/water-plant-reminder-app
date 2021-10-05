package com.example.waterplantreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PlantEditActivity extends AppCompatActivity {

    private ImageView plantImage;
    public static final int TAKE_PHOTO = 1;

    private TextView plantName;
    private TextView plantTips;

    private EditText editPlace;

    private Button savePlant;
    private Button moreInfo;
    private Button deletePlant;

    private Plant plant;

    private TextView waterText;
    private TextView feedText;
    private TextView placeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_edit);

        plant = PlantAdapter.plantList.get(getIntent().getIntExtra("position", 1));
        //plant = getIntent().getParcelableExtra("Plant");

        plantImage = findViewById(R.id.plant_image_edit);
        plantImage.setImageBitmap(plant.getImageBitmap());

        plantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        plantName = findViewById(R.id.plant_name_edit);
        plantName.setText(plant.getName());

        plantTips = findViewById(R.id.tips_text);
        plantTips.setText("Tips: " + plant.getTips());

        waterText = findViewById(R.id.water_frequency_text);
        waterText.setText(String.format("%.1f",plant.getWaterPerWeek()) + " time per Week, quantity: " + plant.getWaterQuantity() + "mL");

        feedText = findViewById(R.id.feed_frequency_text);
        feedText.setText(String.format("%.1f",plant.getFeedPerWeek()) + " time per Week, quantity: " + plant.getFeedQuantity() + "g");

        placeText = findViewById(R.id.plant_place_text);
        placeText.setText(plant.getPlace());

        editPlace = findViewById(R.id.edit_plant_place);
        editPlace.setHint("Enter the place of this plant");


        savePlant = findViewById(R.id.save_button_in_edit);
        savePlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = editPlace.getText().toString();
                if(!place.equals("")){
                    plant.setPlace(place);
                }
                plant.save();
                finish();
            }
        });

        moreInfo = findViewById(R.id.information_button_in_edit);
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(plant.getWebsite() == ""){
                    Toast.makeText(PlantEditActivity.this, "no information anymore", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(plant.getWebsite()));
                    startActivity(intent);
                }
            }
        });

        deletePlant = findViewById(R.id.delete_button_in_edit);
        deletePlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plant.delete();
                finish();
                PlantAdapter.plantList.remove(plant);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_PHOTO && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int side = Math.min(width, height);
            int x = (width - side) / 2;
            int y = (height - side ) / 2;
            Bitmap rectangle = Bitmap.createBitmap(bitmap, x, y, side, side);
            plantImage.setImageBitmap(rectangle);
            plant.setImageBitmap(rectangle);
        }
    }
}
