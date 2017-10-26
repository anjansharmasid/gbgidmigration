package com.thomascook.crm.gbgidmigration.readfile;
/**
 * Application: TCV to WebRio GBG_ID Migration
 * Version: 1.0.
 * All Rights Reserved 
 * ThomasCook
 * Created by : Anjan
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.thomascook.crm.gbgidmigration.db.ConfigDatabase;
import com.thomascook.crm.gbgidmigration.db.CustomerRecord;
import com.thomascook.crm.gbgidmigration.db.connection.SQLExecutor;

public class ReadFFromFile {
	final static Logger logger = Logger.getLogger("ReadFFromFile");
	final static Logger parserlog = Logger.getLogger("reportsLogger");


	private BufferedReader br = null;
	private String line = "";
	private String cvsSplitBy = ",";
	//public static String inputCSVLocation = "/code/TCV-SAMPLE/tcvCSVdata";
	public static String inputCSVLocation = "/export/webrio/tcvdatamigration/tcvCSVdata"; 
	private static ConfigDatabase configDatabase;
	private static SQLExecutor sQLExecutor;
	
	private String ivertedComa = "\"";

	public static void main(String[] args) {
		try {
			configDatabase = new ConfigDatabase(inputCSVLocation);
			sQLExecutor = new SQLExecutor(configDatabase);
		} catch (Exception e) {
			logger.error("Problem in creating ConfigDatabase. Application terminating.");
			e.printStackTrace();
		}
		ReadFFromFile obj = new ReadFFromFile();
		try{
		obj.searchFiles(inputCSVLocation);
		logger.info(ConfigDatabase.dataSource.toString());
		}catch (Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public void searchFiles(String path) throws Exception {
		File fileLocation = new File(path);
		if (fileLocation.exists()) {
			File[] files = fileLocation.listFiles();
			for (File f : files) {
				if (f.getAbsolutePath().endsWith(".csv")) {
					System.out.println("Processing :" + f.getAbsolutePath());
					if (parse(f.getAbsolutePath())) {
						try {
							br = new BufferedReader(new FileReader(f.getAbsolutePath()));
							int rownum = 1;
							CustomerRecord customerRecord=null;
							while ((line = br.readLine()) != null) {
								// use comma as separator
								String[] customer_gbg = line.split(cvsSplitBy);
								int index_of_sep = customer_gbg[1].indexOf("/");
								if (index_of_sep > 0) {
									String GBG_ID_T = customer_gbg[0].substring(1, customer_gbg[0].length() - 1).trim();
									String GBG_ID = GBG_ID_T.replaceAll(ivertedComa, "").trim();
									String Branch_T = customer_gbg[1].substring(0, index_of_sep).trim();
									String Branch = Branch_T.replaceAll(ivertedComa, "").trim();
									String Consultation_T = customer_gbg[1].substring(index_of_sep + 1,
											customer_gbg[1].length()).trim();
									String Consultation =Consultation_T.replaceAll(ivertedComa, "").trim();
									try {
										customerRecord = new CustomerRecord(GBG_ID, Integer.parseInt(Branch),
												Long.parseLong(Consultation), rownum, f.getAbsolutePath(), sQLExecutor);
									} catch (Exception e) {
										logger.error("Database Error : problem in finding customer reff for Row:" + rownum + "  GBG_ID ="
												+ GBG_ID + "    Branch =" + Branch + "  Consultation ="
												+ Consultation + " error: " + e.getMessage());
									}
								}
								rownum = rownum + 1;
							}
						} catch (FileNotFoundException e) {
							logger.error(e.getMessage());
							e.printStackTrace();
						} catch (IOException e) {
							logger.error(e.getMessage());
							e.printStackTrace();
						} finally {
							if (br != null) {
								try {
									br.close();
								} catch (IOException e) {
									logger.error(e.getMessage());
									e.printStackTrace();
								}
							}
							try {
								 File originFile = new File (f.getAbsolutePath());
								 Path source = originFile.toPath();
							     Files.move(source, source.resolveSibling(f.getAbsolutePath()+".done"));
							     logger.info("Renaming "+ f.getAbsolutePath() + " to "+ f.getAbsolutePath()+".done");
							} catch (IOException e) {
								logger.info("Failed to rename "+ f.getAbsolutePath() + " to "+ f.getAbsolutePath()+".done");
							     e.printStackTrace();
							}
						}
					}
					
				} else {
					logger.info(f.getAbsolutePath()+ " is not a .CVS file." );
				}
			}
		} else {
			logger.error("Error: Input source for gbg_id csv does not existe: " + path);
			System.exit(1);
		}
	}

	private boolean parse(String csvdatafilepath) {
	    ArrayList<String> parse_error = new ArrayList<String>();
	    String current_line = "";
		parserlog.info("Parsing " + csvdatafilepath);
		try {
			br = new BufferedReader(new FileReader(csvdatafilepath));
			int rownum = 1;
			while ((line = br.readLine()) != null) {
				String[] customer_gbg = line.split(cvsSplitBy);
				int index_of_sep = customer_gbg[1].indexOf("/");
				if (index_of_sep > 0) {
					String GBG_ID_T = customer_gbg[0].substring(1, customer_gbg[0].length() - 1).trim();
					String GBG_ID = GBG_ID_T.replaceAll(ivertedComa, "").trim();
					String Branch_T = customer_gbg[1].substring(0, index_of_sep).trim();
					String Branch = Branch_T.replaceAll(ivertedComa, "").trim();
					String Consultation_T = customer_gbg[1].substring(index_of_sep + 1,customer_gbg[1].length()).trim();
					String Consultation =Consultation_T.replaceAll(ivertedComa, "").trim();
					try{
						current_line = "Error in File:"+ csvdatafilepath +" Line: "+ rownum + " GBG_ID:"+GBG_ID + " Branch:"+Branch + " Consultation:"+Consultation;
						if ((GBG_ID.isEmpty()) || Branch.isEmpty() || Consultation.isEmpty()){
							if(parse_error.size()<300){
								parse_error.add(current_line);
							}
						}
						if ((GBG_ID.length()<20 || Branch.length()<3 || Consultation.length()<7)){
							if(parse_error.size()<300){
								parse_error.add(current_line);
							}
						}
						Integer.parseInt(Branch);
						Long.parseLong(Consultation);
					}catch(Exception e){
						if(parse_error.size()<300){
							  parse_error.add(current_line);
						}		
					}
				}
				rownum = rownum + 1;
			}
		} 
		catch (Exception e) {
				if(parse_error.size()<300){
				  parse_error.add("Problem in file" + csvdatafilepath);
				  parserlog.info("Error Parsing " + csvdatafilepath);
				}
		
		 }
		 finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		} 
	    if(parse_error.size()>0){
			for(String s : parse_error){
				parserlog.error(s);
			}
			return false;
		}else{
			parserlog.info("Succefully parsed " + csvdatafilepath);
			return true;
		}
	}

}
