package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import entity.*;

import javax.jws.WebMethod;
import javax.jws.WebService;

public class PlantControl {

	public static ArrayList<Plant> plantList;


	public static void loadPlant() {
		plantList = new ArrayList<Plant>();
		try {
			System.out.println("loading plants...");
			BufferedReader reader = new BufferedReader(new FileReader("src/plants.csv"));
			reader.readLine();
			String line = null;
			while ((line = reader.readLine()) != null) {
				String item[] = line.split(",");
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
				plantList.add(plant);
				System.out.println(plantList.indexOf(plant) + "," + plant);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void savePlant() {
		try {
			File csv = new File("src/plants.csv");
			BufferedWriter bw = new BufferedWriter(new FileWriter(csv, false));
			bw.write("name,waterPerWeek,feedPerWeek,waterQuantity,feedQuantity,tips");
			bw.newLine();
			for (int i = 0; i < plantList.size(); i++) {
				bw.write(plantList.get(i).toString());
				bw.newLine();
			}
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setPlant(int i, String name, double waterPerWeek, double feedPerWeek, int waterQ, int feedQ, String tips, String website) {
		Plant plant = plantList.get(i);
		plant.setName(name);
		plant.setWaterPerWeek(waterPerWeek);
		plant.setFeedPerWeek(feedPerWeek);
		plant.setWaterQuantity(waterQ);
		plant.setFeedQuantity(feedQ);
		plant.setTips(tips);
		plant.setWebsite(website);
	}

	public static Plant getPlant(String name) {
		for (int i = 0; i < plantList.size(); i++) {
			if (plantList.get(i).getName().equals(name)) {
				return plantList.get(i);
			}
		}
		return null;
	}
	
}
