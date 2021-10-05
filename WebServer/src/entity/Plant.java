package entity;

/**
 * @author liyin
 *
 */
import javax.xml.bind.annotation.XmlRootElement; 

public class Plant {
	
	private String name;
	private double waterPerWeek;
	private double feedPerWeek;
	private String tips;
	private String website;
	private int waterQuantity;
	private int feedQuantity;
	
	public Plant(String name, double waterPerWeek, double feedPerWeek, int waterQuantity, int feedQuantity, String tips, String website) {
		this.name = name;
		this.waterPerWeek = waterPerWeek;
		this.feedPerWeek = feedPerWeek;
		this.tips = tips;
		this.website = website;
		this.waterQuantity = waterQuantity;
		this.feedQuantity = feedQuantity;
	}

	public Plant() {
		
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the waterPerWeek
	 */
	public double getWaterPerWeek() {
		return waterPerWeek;
	}

	/**
	 * @return the feedPerWeek
	 */
	public double getFeedPerWeek() {
		return feedPerWeek;
	}

	/**
	 * @return the tips
	 */
	public String getTips() {
		return tips;
	}

	public String getWebsite() {
		return website;
	}

	public int getWaterQuantity() {
		return waterQuantity;
	}

	public int getFeedQuantity() {
		return feedQuantity;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param waterPerWeek the waterPerWeek to set
	 */
	public void setWaterPerWeek(double waterPerWeek) {
		this.waterPerWeek = waterPerWeek;
	}

	/**
	 * @param feedPerWeek the feedPerWeek to set
	 */
	public void setFeedPerWeek(double feedPerWeek) {
		this.feedPerWeek = feedPerWeek;
	}

	/**
	 * @param tips the tips to set
	 */
	public void setTips(String tips) {
		this.tips = tips;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public void setWaterQuantity(int waterQuantity) {
		this.waterQuantity = waterQuantity;
	}

	public void setFeedQuantity(int feedQuantity) {
		this.feedQuantity = feedQuantity;
	}

	public String toString() {
		return name + "," + waterPerWeek + "," + feedPerWeek + "," + waterQuantity + "," + feedQuantity+ "," + tips + "," + website;
	}
}