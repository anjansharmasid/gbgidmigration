package com.thomascook.crm.gbgidmigration.db.connection;
/**
 * Application: TCV to WebRio GBG_ID Migration
 * Version: 1.0.
 * All Rights Reserved 
 * ThomasCook
 * Created by : Anjan
 */

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import com.thomascook.crm.gbgidmigration.db.ConfigDatabase;

public class SQLExecutor {

	final static Logger logger = Logger.getLogger("SQLExecutor");

	private String JDBC_DRIVER = null;
	private String JDBC_DB_URL = null;
	private String JDBC_USER = null;
	private String JDBC_PASS = null;
	private static GenericObjectPool gPool = null;
	private ConfigDatabase configDatabase = null;
	private DataSource dataSource = null;

	public SQLExecutor(ConfigDatabase configDatabase) throws Exception {
		this.configDatabase = configDatabase;
		init();
	}

	private void init() throws Exception {
		JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
		JDBC_DB_URL = "jdbc:oracle:thin:@" + this.configDatabase.dataSource.getHostname() + ":"
				+ this.configDatabase.dataSource.getPort() + "/" + this.configDatabase.dataSource.getServicename();
		JDBC_USER = this.configDatabase.dataSource.getUsername();
		JDBC_PASS = this.configDatabase.dataSource.getPassword();
		Class.forName(JDBC_DRIVER);
		gPool = new GenericObjectPool();
		gPool.setMaxActive(this.configDatabase.dataSource.getPoolsize());
		ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_DB_URL, JDBC_USER, JDBC_PASS);
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null, null, false, true);
		dataSource = new PoolingDataSource(gPool);
	}

	public GenericObjectPool getConnectionPool() {
		return gPool;
	}

	public ArrayList<Long> getCust_ref_num(int branch,long consultation_reff) throws Exception{
		String sqlSelect = "select cust.CUST_REF_NUM from consultation con, customer cust  "
				+ "where con.BRANCH_BUDGET_CTR = ? and "
				+ "con.CONSULTATION_REF = ? and "
				+ "con.CUST_REF_NUM = cust.CUST_REF_NUM ORDER BY con.DATE_LU desc";
		ArrayList<Long> ar = new ArrayList<Long>();
		ResultSet rsObj =null;
		Connection connObj = null;
		PreparedStatement pstmtObj = null;
		try {
			connObj = dataSource.getConnection();
			pstmtObj = connObj.prepareStatement(sqlSelect);
			pstmtObj.setInt(1,branch);
			pstmtObj.setLong(2,consultation_reff);
			rsObj = pstmtObj.executeQuery();
			while (rsObj.next()) {
				Long tmp_cust_ref = rsObj.getLong("CUST_REF_NUM");
				if(!ar.contains(tmp_cust_ref)){
				  ar.add(tmp_cust_ref);
				}
			}
		} catch (Exception sqlException) {
			logger.error("sqlException:" + sqlException.getMessage());
			sqlException.printStackTrace();
		} finally {
			try {
				if (rsObj != null) {
					rsObj.close();
				}
				if (pstmtObj != null) {
					pstmtObj.close();
				}
				if (connObj != null) {
					connObj.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
				logger.error("sqlException:" + sqlException.getMessage());
			}
		}
		return ar;
	}

	public void insertRecord(String GBG_ID, int branch, long consultation_reff, String cust_ref_num, long record_number, String file_name, int multiple_record) throws Exception {
		ResultSet rsObj = null;
		Connection connObj = null;
		PreparedStatement pstmtObj = null;
		String sql = "INSERT INTO TCV_CUSTOMER_TEMP(GBG_ID,BRANCH,CONSULTATION_REFF,CUST_REF_NUM,RECORD_NUMBER,FILE_NAME,MULTIPLE_RECORD) values (?,?,?,?,?,?,?)";
		try {
			connObj = dataSource.getConnection();
			pstmtObj = connObj.prepareStatement(sql);
			pstmtObj.setString(1,GBG_ID);
			pstmtObj.setInt(2,branch);
			pstmtObj.setLong(3,consultation_reff);
			pstmtObj.setString(4,cust_ref_num);
			pstmtObj.setLong(5,record_number);
			pstmtObj.setString(6,file_name);
			pstmtObj.setInt(7,multiple_record);
			rsObj = pstmtObj.executeQuery();
		} catch (Exception sqlException) {
			logger.error("sqlException:" + sqlException.getMessage());
			sqlException.printStackTrace();
		} finally {
			try {
				if (rsObj != null) {
					rsObj.close();
				}
				if (pstmtObj != null) {
					pstmtObj.close();
				}
				if (connObj != null) {
					connObj.close();
				}
			} catch (Exception sqlException) {
				sqlException.printStackTrace();
				logger.error("sqlException:" + sqlException.getMessage());
			}
		}
	}

}
