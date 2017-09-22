package controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFileChooser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReformatPTR {
	static Map < Integer, Object[] > donorInfo = new TreeMap < Integer, Object[] >();

	public static void readInData (File readFile) throws IOException{
		FileInputStream fis = new FileInputStream(readFile);

         XSSFWorkbook workbookImport = new XSSFWorkbook(fis);
         XSSFSheet spreadsheetImport = workbookImport.getSheetAt(0);
         XSSFRow row;
         ArrayList<Object> data= new ArrayList<Object>();
         Iterator < Row > rowIterator = spreadsheetImport.iterator();
         int cellid=0;
         donorInfo.clear();

         while (rowIterator.hasNext())
         {
        	 row = (XSSFRow) rowIterator.next();
        	 Iterator < Cell > cellIterator = row.cellIterator();
        	 while ( cellIterator.hasNext())
        	 {
        		 Cell cell = cellIterator.next();
        		 switch (cell.getCellType())
        		 {
        		 case Cell.CELL_TYPE_NUMERIC:
        			 data.add(cell.getNumericCellValue());
        			 //System.out.println(cell.getNumericCellValue());
        			 break;
        		 case Cell.CELL_TYPE_STRING:
        			 data.add(cell.getStringCellValue());
        			 //System.out.println(cell.getStringCellValue());
        			 break;
        		 }
        	 }
        	 //System.out.print(data.get(i));

        	 donorInfo.put(cellid,data.toArray());
        	 data.clear();
        	 cellid++;
        	 //System.out.println();
         }
         fis.close();
		 workbookImport.close();
	}

	public static void writeData(File writeFile, int start, int end) throws IOException{
		if (start>end) {
			System.err.println("start and end months error");
			return;
		}

		//Create Blank workbook
	      XSSFWorkbook workbook = new XSSFWorkbook();
	      //Create a blank spreadsheet
	      XSSFSheet spreadsheet = workbook.createSheet(" Donor Info ");

  	      //Iterate over data and write to sheet
  	      Set < Integer > keyid = donorInfo.keySet();
  	      int rowid = 0;

  	      // header row
  	      XSSFRow row= spreadsheet.createRow(0);
  	      Cell cell =row.createCell(0);
  	      cell.setCellValue("DonationAccount");
  	      cell =row.createCell(1);
  	      cell.setCellValue("FiscalYear");
  	      cell =row.createCell(2);
  	      cell.setCellValue("DonorID");
  	      cell =row.createCell(3);
  	      cell.setCellValue("Name");
  	      cell =row.createCell(4);
  	      cell.setCellValue("DonationDate");
  	      cell =row.createCell(5);
  	      cell.setCellValue("DonationAmount");
  	      cell =row.createCell(6);
  	      cell.setCellValue("DonationType");
  	      rowid++;

  		  // reorganize data so that each gift on separate row with date

  	      // data rows
  	      // for each donor...

  	      System.out.println("size "+ (double)donorInfo.get(1).length);

  	      for (int key = 1;key<keyid.size()-1;key++){
	  		  //for (int key:keyid){
	  			  double thisFY = (double)donorInfo.get(key)[16];
	  			  double prevFY = donorInfo.get(key).length>=18?(double)donorInfo.get(key)[17]:0;
	  			  boolean recurring = false;

	  			  // check for recurring donor
	  			  int giftCount=0;
	  			  int giftStreak=0;
	  			  int maxGiftStreak=0;
	  			  if (thisFY>0){
	  				  // determine recurring or not using data from whole year
	  				  for(int col=4;col<16;col++){
	  					  if ((double)donorInfo.get(key)[col]>0){
	  						  giftCount++;
	  						  giftStreak++;
	  						  maxGiftStreak=giftStreak>maxGiftStreak?giftStreak:maxGiftStreak;
	  					  }
	  					  else {
	  						  giftStreak=0;
	  					  }
	  				  }
	  			  }
	  			  // consider recurring if more than 3 gifts in a year or gave 3 months in a row
	  			  recurring=(giftCount>=4 || maxGiftStreak>=3);
	  			  //System.out.println("giftStreak "+giftStreak+" giftcount "+giftCount+" max "+maxGiftStreak);

	  			  // if not first or last row and donor gave this FY
	  			  if (thisFY>0){
	  				//
	    	        Object [] objectArr = donorInfo.get(key);
	    	        int column;
	    	       // for (column=4;column<16;column++){
	    	        for (column=start+4;column<=end+4;column++){
	    	        	// if is a number and is greater than 0
	  	        		if (objectArr[column] instanceof Double && (double)objectArr[column]>0){
	  	        			// create a new row
	  	        			row = spreadsheet.createRow(rowid++);
			    	        	for (int cellid=0;cellid<4;cellid++){
			    	        		cell =row.createCell(cellid);
			    	        		String string;
			    	        		if (cellid==0){
			    	        			string = getAcct(objectArr[0].toString());
			    	        			//System.out.println(getAcct(objectArr[0].toString()));
			    	        		}
			    	        		else {
			    	        			string = objectArr[cellid].toString();
			    	        		}
				    	        	cell.setCellValue(string);
			    	        	}

	  	        			cell= row.createCell(4);
			    	        cell.setCellValue(getDate(column,donorInfo.get(1)[1].toString()));
		    	        	cell =row.createCell(5);
	  	        			cell.setCellValue((double)objectArr[column]);
	  	        			cell= row.createCell(6);
	  	        			cell.setCellValue(recurring?"Recurring":"One Time Gift");
	  	        		}
	    	        }
	  			  }
	  		  }
	  	  //String filename = getAcct(donorInfo.get(1)[0].toString())+"-import-" +donorInfo.get(1)[1]+".xlsx";

  	      FileOutputStream out = new FileOutputStream(writeFile);
	      //write operation workbook using file out object
	      workbook.write(out);
	      out.close();
	      System.out.println("file written successfully");
	      workbook.close();
	}

	public static String getSaveFilename(){
		return getAcct(donorInfo.get(1)[0].toString())+"-import-" +donorInfo.get(1)[1]+".xlsx";
	}

	private static String getAcct(String acctName) {
		System.out.println(acctName);
		switch (acctName) {
		case "University of Illinois Legacy Fund":
			return "UILE";
		case "Staff work at U of I - U/C Undergrad":
		case "U of I - U/C Undergrad":
			return "8UNO";
		case "Staff work at U of I Champaign Alum":
		case "U of I Champaign Alum":
			return "8ICH";
		case "Staff work at U of I U/C Alum 50-90s":
		case "U of I U/C Alum 50-90s":
			return "8IUR";
		case "U of I  Urbana/Champaign Area Min":
			return "UIAM";
		case "U of I U/C Undergrad Scholarships":
			return "UISC";
		case "U of I Urbana/Champaign Future Staff":
			return "UIFS";
		case "Trever and Kristin Risinger":
			return "TRIS";
		default:
			return "";
		}
	}

	static private String getDate(int column,String fy){
		String yearFall=fy.substring(0,4);
		String yearSpring=fy.substring(5);

		switch (column){
		case 4:
			return "7/31/"+yearFall;
		case 5:
			return "8/31/"+yearFall;
		case 6:
			return "9/30/"+yearFall;
		case 7:
			return "10/31/"+yearFall;
		case 8:
			return "11/30/"+yearFall;
		case 9:
			return "12/31/"+yearFall;
		case 10:
			return "1/31/"+yearSpring;
		case 11:
			return "2/28/"+yearSpring;
		case 12:
			return "3/31/"+yearSpring;
		case 13:
			return "4/30/"+yearSpring;
		case 14:
			return "5/31/"+yearSpring;
		case 15:
			return "6/30/"+yearSpring;
		default:
			return "";
		}
	}


}


