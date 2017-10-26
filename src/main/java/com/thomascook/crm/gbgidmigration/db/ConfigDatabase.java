package com.thomascook.crm.gbgidmigration.db;

/**
 * Application: TCV to WebRio GBG_ID Migration
 * Version: 1.0.
 * All Rights Reserved 
 * ThomasCook
 * Created by : Anjan
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigDatabase {

	final static Logger logger = Logger.getLogger("ConfigDatabase");

	private String databaseConfigFilePath;
	public static DataSource dataSource;

	public ConfigDatabase(String databaseConfigFilePath) throws Exception {
		// /code/TCV-SAMPLE/msd_sample
		int indexOf = databaseConfigFilePath.lastIndexOf("/");
		databaseConfigFilePath = databaseConfigFilePath.substring(0, indexOf);
		this.databaseConfigFilePath = databaseConfigFilePath;
		File file = new File(databaseConfigFilePath);
		if (!file.exists()) {
			logger.error(" Error: DatabaseConfigFile does not exists:" + file.getAbsolutePath());
			System.out.println(" Error: DatabaseConfigFile does not exists:" + file.getAbsolutePath());
		}
		if (dataSource == null) {
			dataSource = makeDataSource();
		}
	}

	private DataSource makeDataSource() throws Exception {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(databaseConfigFilePath + File.separator + "dataSource.properties");
			prop.load(input);
			dataSource = new DataSource(prop.getProperty("hostname"), prop.getProperty("port"), prop.getProperty("sID"),
					prop.getProperty("servicename"), prop.getProperty("username"), prop.getProperty("password"),
					prop.getProperty("poolsize"), prop.getProperty("poolname"), prop.getProperty("timeout"),
					prop.getProperty("autocommit"));
		} catch (IOException ex) {
			logger.error("Error: Could not read database dataSource.properties at location " + databaseConfigFilePath
					+ File.separator + "dataSource.properties");
			System.out.println("Error: Could not read database dataSource.properties at location "
					+ databaseConfigFilePath + File.separator + "dataSource.properties");
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error("Error: Could not close inputstream" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return dataSource;
	}
}
