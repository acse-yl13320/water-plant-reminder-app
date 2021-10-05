package boundary;

import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;

import control.PlantControl;
import entity.Plant;

import java.awt.event.ActionListener;

/**
 * 
 * @author liyin
 *
 */
public class ReviseGUI implements ActionListener {

	private JPanel cPanel;
	private JPanel sPanel;
	private JTextField nameField;
	private JTextField waterField;
	private JTextField feedField;
	private JTextField waterQField;
	private JTextField feedQField;
	private JTextArea tipsField;
	private JTextField websiteField;
	private JLabel mainHint;
	private JLabel nameLabel;
	private JLabel waterLabel;
	private JLabel feedLabel;
	private JLabel waterQLabel;
	private JLabel feedQLabel;
	private JLabel tipLabel;
	private JLabel websiteLabel;
	private JButton submit;
	private JButton discard;
	private JFrame frame;
	private Plant plant;
	private String name;
	private Double water;
	private Double feed;
	private int waterQ;
	private int feedQ;
	private String tips;
	private String website;
	public static final int Add = 0;
	public static final int Revise = 1;
	private int addorRevise;

	public ReviseGUI(Plant plant, int addorRevise) {
		this.addorRevise = addorRevise;
		this.plant = plant;
		System.out.println("revise GUI");
		startFrame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == submit) {
			try {
				name = nameField.getText();
				Plant duplica = PlantControl.getPlant(name);
				if(duplica == null || duplica.equals(this.plant)) {

				} else {
					nameField.setText("");
					mainHint.setText("duplicate plant name!");
					return ;
				}
				water = Double.parseDouble(waterField.getText());
				feed = Double.parseDouble(feedField.getText());
				waterQ = Integer.parseInt(waterQField.getText());
				feedQ = Integer.parseInt(feedQField.getText());
				tips = tipsField.getText();
				website = websiteField.getText();
			} catch (Exception e1) {
				waterField.setText("");
				feedField.setText("");
				waterQField.setText("");
				feedQField.setText("");
				mainHint.setText("Invalid input!");
				return ;
			}
			plant.setName(name);
			plant.setWaterPerWeek(water);
			plant.setFeedPerWeek(feed);
			plant.setTips(tips);
			plant.setWaterQuantity(waterQ);
			plant.setFeedQuantity(feedQ);
			plant.setWebsite(website);
			if(addorRevise == Add){
				PlantControl.plantList.add(plant);
			}
			PlantControl.savePlant();
			frame.dispose();
		} else if (e.getSource() == discard) {
			frame.dispose();
		}
	}

	public void startFrame() {

		frame = new JFrame();
		frame.setLayout(new BorderLayout());

		cPanel = new JPanel();
		cPanel.setLayout(new GridLayout(0, 2));
		sPanel = new JPanel();
		sPanel.setLayout(new GridLayout(0, 2));

		mainHint = new JLabel("Please input");

		nameField = new JTextField(plant.getName());
		nameLabel = new JLabel("name:");

		waterField = new JTextField("" + plant.getWaterPerWeek());
		waterLabel = new JLabel("water per week:");

		feedField = new JTextField("" + plant.getFeedPerWeek());
		feedLabel = new JLabel("feed per week:");

		waterQField = new JTextField("" + plant.getWaterQuantity());
		waterQLabel = new JLabel("water quantity:");

		feedQField = new JTextField("" + plant.getFeedQuantity());
		feedQLabel = new JLabel("feed quantity:");

		tipsField = new JTextArea(10,3);
		tipsField.append(plant.getTips());
		tipLabel = new JLabel("tips:");

		websiteField = new JTextField(10);
		websiteField.setText(plant.getWebsite());
		websiteLabel = new JLabel("website:");

		submit = new JButton("submit");
		submit.addActionListener(this);
		discard = new JButton("discard");
		discard.addActionListener(this);

		cPanel.add(nameLabel);
		cPanel.add(nameField);
		cPanel.add(waterLabel);
		cPanel.add(waterField);
		cPanel.add(feedLabel);
		cPanel.add(feedField);
		cPanel.add(waterQLabel);
		cPanel.add(waterQField);
		cPanel.add(feedQLabel);
		cPanel.add(feedQField);
		cPanel.add(tipLabel);
		cPanel.add(tipsField);
		cPanel.add(websiteLabel);
		cPanel.add(websiteField);

		sPanel.add(submit);
		sPanel.add(discard);

		frame.add(BorderLayout.NORTH, mainHint);
		frame.add(BorderLayout.CENTER, cPanel);
		frame.add(BorderLayout.SOUTH, sPanel);
		frame.setVisible(true);
		frame.setSize(800, 400);
		
	}
}
