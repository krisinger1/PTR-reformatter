package ptrGUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.poi.ss.formula.functions.EDate;

import controller.ReformatPTR;


public class MainFrame extends JFrame {
	JPanel panel;
	File ptrFile;
	File[] ptrFiles;
	String[] monthStrings = {"July","August","September","October","November","December","January","February","March","April","May","June"};


	public MainFrame(String title){
		super(title);
		this.setPreferredSize(new Dimension(800,800));
		this.setMinimumSize(new Dimension(800,800));

		JComboBox<String> startMonthComboBox = new JComboBox<>(monthStrings);
		JComboBox<String> endMonthComboBox = new JComboBox<>(monthStrings);

		JTextField startMonth = new JTextField(10);
		JTextField endMonth = new JTextField(10);
		//JButton runButton = new JButton("Run");
		JButton openFileButton = new JButton("Open File");
		JButton convertFileButton = new JButton("Convert File");
		JButton readCsvButton = new JButton("Read csv");
		JTable fileDisplayTable;


		panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

		panel.add(new JLabel("Start Month"));
		panel.add(startMonthComboBox);
		//panel.add(startMonth);
		panel.add(new JLabel("End Month"));
		panel.add(endMonthComboBox);
		//panel.add(endMonth);
		panel.add(openFileButton);
		//panel.add(runButton);
		panel.add(readCsvButton);
		panel.add(convertFileButton);
		
		readCsvButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				System.out.println("csv button clicked");

				try {
					//ReformatPTR.readInCsvFile(ptrFile);
					ReformatPTR.readInCsvFile(ptrFiles);

				}
				catch (IOException ie) {
					ie.printStackTrace();
				}
			}
		});

//		runButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				System.out.println("run button clicked");
//				try {
//					ReformatPTR.readInData(ptrFiles);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		});

		openFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("open file button clicked");
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setCurrentDirectory(new File("c://users/kristin/onedrive/ivcf work stuff/donorsnap import files/"));
				int choice = fileChooser.showOpenDialog(null);
				ptrFiles=fileChooser.getSelectedFiles();
				System.out.println("file " +ptrFiles[0].getName());
			}
		});

		convertFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("conver file button clicked");
				// file chooser goes here?
				try {
					System.out.println("start ="+startMonthComboBox.getSelectedIndex());
					System.out.println("end ="+endMonthComboBox.getSelectedIndex());
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("c://users/kristin/onedrive/ivcf work stuff/donorsnap import files/"));

					fileChooser.setSelectedFile(new File(ReformatPTR.getSaveFilename()));
					fileChooser.showSaveDialog(null);
					File writeFile = fileChooser.getSelectedFile();

					ReformatPTR.writeData(writeFile,startMonthComboBox.getSelectedIndex(),endMonthComboBox.getSelectedIndex());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(panel);
		setVisible(true);

	}

}
