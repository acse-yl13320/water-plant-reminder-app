package boundary;

import control.PlantControl;
import control.QRControl;
import control.WebServiceControl;
import entity.Plant;


import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.xml.ws.Endpoint;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class MainFrame implements ActionListener {

    private JFrame frame;
    private JLabel label;
    private JTextField number;
    private JButton revise;
    private JButton add;
    private JButton remove;
    private JButton QR;
    private JButton refresh;
    private JPanel selectP;
    private JTable table;
    private JScrollPane scrollPane;
    private FileDialog saveDia;

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == revise) {
            try {
                int i = Integer.parseInt(number.getText());
                if (i >= 0 && i < PlantControl.plantList.size()) {
                    ReviseGUI gui = new ReviseGUI(PlantControl.plantList.get(i), ReviseGUI.Revise);
                } else {
                    number.setText("");
                }
            } catch (Exception e1) {
                number.setText("");
            }
        } else if (e.getSource() == add) {
            System.out.println("add a new plant....");
            Plant plant = new Plant();
            ReviseGUI gui = new ReviseGUI(plant, ReviseGUI.Add);

        } else if (e.getSource() == remove) {
            try {
                int i = Integer.parseInt(number.getText());
                if (i >= 0 && i < PlantControl.plantList.size()) {
                    PlantControl.plantList.remove(i);
                    PlantControl.savePlant();
                    refresh();
                } else {
                    number.setText("");
                }
            } catch (Exception e1) {
                number.setText("");
            }
        } else if (e.getSource() == QR) {
            try {
                int i = Integer.parseInt(number.getText());
                if (i >= 0 && i < PlantControl.plantList.size()) {
                    qrOutput(PlantControl.plantList.get(i).getName());
                } else {
                    number.setText("");
                }
            } catch (Exception e1) {
                number.setText("");
            }
        } else if (e.getSource() == refresh) {
            refresh();
        }
    }


    /**
     * initialize the GUI
     */
    private void startFrame() {

        frame = new JFrame("Plant Manager");
        frame.setLayout(new BorderLayout());

        tableInit();
        frame.add(scrollPane, BorderLayout.CENTER);

        label = new JLabel("Input number:");
        number = new JTextField(4);
        selectP = new JPanel();
        revise = new JButton("revise");
        revise.addActionListener(this);
        add = new JButton("add");
        add.addActionListener(this);
        remove = new JButton("remove");
        remove.addActionListener(this);
        QR = new JButton("QRcode");
        QR.addActionListener(this);
        refresh = new JButton("refresh");
        refresh.addActionListener(this);
        selectP.add(label);
        selectP.add(number);
        selectP.add(revise);
        selectP.add(add);
        selectP.add(remove);
        selectP.add(QR);
        selectP.add(refresh);
        frame.add(selectP, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.setSize(800, 800);

        saveDia = new FileDialog(frame, "save as", FileDialog.SAVE);
    }

    private void tableInit() {
        String[] columnNames = {"no.", "name", "waterPerWeek", "feedPerWeek", "waterQuantity", "feedQuantity", "tips", "website"};
        int size = PlantControl.plantList.size();
        String[][] tableValues = new String[size][8];
        for (int i = 0; i < PlantControl.plantList.size(); i++) {
            tableValues[i][0] = "" + i;
            tableValues[i][1] = PlantControl.plantList.get(i).getName();
            tableValues[i][2] = "" + PlantControl.plantList.get(i).getWaterPerWeek();
            tableValues[i][3] = "" + PlantControl.plantList.get(i).getFeedPerWeek();
            tableValues[i][4] = "" + PlantControl.plantList.get(i).getWaterQuantity();
            tableValues[i][5] = "" + PlantControl.plantList.get(i).getFeedQuantity();
            tableValues[i][6] = PlantControl.plantList.get(i).getTips();
            tableValues[i][7] = PlantControl.plantList.get(i).getWebsite();
        }
        table = new JTable(tableValues, columnNames);
        scrollPane = new JScrollPane(table);
        table.setRowHeight(50);
        table.setDefaultRenderer(Object.class, new TableViewRenderer());

        JTableHeader header = table.getTableHeader();
        int rowCount = table.getRowCount();
        Enumeration columns = table.getColumnModel().getColumns();
        while ((columns).hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, column.getIdentifier()
                    , false, false, -1, col).getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++) {
                int preferedWidth = (int) table.getCellRenderer(row, col).getTableCellRendererComponent(table,
                        table.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column);
            column.setWidth(width + table.getIntercellSpacing().width);
        }
    }

    private void refresh() {
        System.out.println("refreshing...");
        frame.remove(scrollPane);
        tableInit();
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.validate();
    }

    private void qrOutput(String payload) {
        saveDia.setFile("QRcode_of_" + payload + ".png");
        saveDia.setVisible(true);
        String path = saveDia.getDirectory();
        String fileName = saveDia.getFile();
        if (path == null || fileName == null)
            return;
        else {
            if(!fileName.endsWith(".png")){
                fileName += ".png";
            }
            try {
                QRControl.printQR(payload, path, fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class TableViewRenderer extends JTextArea implements TableCellRenderer {

        public TableViewRenderer() {
            setLineWrap(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(obj == null ? "" : obj.toString());
            return this;
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        PlantControl.loadPlant();
        MainFrame m = new MainFrame();
        m.startFrame();
        WebServiceControl webServiceControl = new WebServiceControl();
        Endpoint.publish("http://192.168.0.106:9000/PlantService", webServiceControl);
    }

}
