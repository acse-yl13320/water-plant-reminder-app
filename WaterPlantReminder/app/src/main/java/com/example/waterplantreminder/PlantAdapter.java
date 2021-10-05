package com.example.waterplantreminder;

import android.content.Intent;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;


public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder>{

    static List<Plant> plantList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView plantImage;
        TextView plantName;
        TextView plantPlaceText;
        TextView plantWaterText;
        TextView plantFeedText;
        View plantView;

        public ViewHolder(View view){
            super(view);
            plantView = view;
            plantImage = view.findViewById(R.id.plant_image);
            plantName = view.findViewById(R.id.plant_name);
            plantPlaceText = view.findViewById(R.id.plant_place_text_in_item);
            plantWaterText = view.findViewById(R.id.plant_water_text_in_item);
            plantFeedText = view.findViewById(R.id.plant_feed_text_in_item);
        }
    }

    public PlantAdapter(List<Plant> plantList){
        this.plantList = plantList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.plantView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Plant plant = plantList.get(position);
                Toast.makeText(v.getContext(), plant.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(),PlantEditActivity.class);
                intent.putExtra("position", position);
                //intent.putExtra("plant", plant);
                v.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        Plant plant = plantList.get(position);
        viewHolder.plantImage.setImageBitmap(plant.getImageBitmap());
        viewHolder.plantName.setText(plant.getName());

        viewHolder.plantPlaceText.setText(plant.getPlace());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        Date nextWater = plant.getNextWater().getTime();
        Date nextFeed = plant.getNextFeed().getTime();

        viewHolder.plantWaterText.setText(sdf.format(nextWater));
        viewHolder.plantFeedText.setText(sdf.format(nextFeed));
    }

    @Override
    public int getItemCount(){
        return plantList.size();
    }
}
