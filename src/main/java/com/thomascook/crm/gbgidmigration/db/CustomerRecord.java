package com.thomascook.crm.gbgidmigration.db;
/**
 * Application: TCV to WebRio GBG_ID Migration
 * Version: 1.0.
 * All Rights Reserved 
 * ThomasCook
 * Created by : Anjan
 */

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.thomascook.crm.gbgidmigration.db.connection.SQLExecutor;

public class CustomerRecord {
	final static Logger logger = Logger.getLogger("CustomerRecord");
	private String GBG_ID;
	private Integer branch;
	private Long consultation_reff;
	private String cust_ref_num;
	private Long record_number;
	private String file_name;
	private Integer multiple_record;
	private SQLExecutor sQLExecutor;

	public CustomerRecord(String gBG_ID, int branch, long consultation_reff, long record_number, String file_name, SQLExecutor sQLExecutor) throws Exception {
		super();
		this.sQLExecutor=sQLExecutor;
		GBG_ID = gBG_ID;
		this.branch = branch;
		this.consultation_reff = consultation_reff;
		this.record_number = record_number;
		this.file_name = file_name;
		ArrayList<Long>  customer_reffList = getCustomers(branch,consultation_reff);
		multiple_record = customer_reffList.size();
		if(!customer_reffList.isEmpty()){
			StringBuffer  cust_ref_num_tmp = new StringBuffer();
	        int coma = customer_reffList.size(); 
	        for(long l: customer_reffList ){
	        	cust_ref_num_tmp.append(l);
	        	if(coma > 1){
	        		cust_ref_num_tmp.append(", ");
	        		coma = coma -1;
	        	}
	        }
	        cust_ref_num = cust_ref_num_tmp.toString();
		}
	
		try{
		  if(!GBG_ID.isEmpty() && branch >0 && consultation_reff > 0 &&  !cust_ref_num.isEmpty() &&  ! file_name.isEmpty()){	
		      sQLExecutor.insertRecord(GBG_ID, branch,consultation_reff, cust_ref_num, record_number,file_name, multiple_record);
		  }else{
			  logger.error("Failed to insert record in the temporary table :" + GBG_ID +","+ branch+","+consultation_reff+","+ cust_ref_num+","+ record_number+","+file_name+","+ multiple_record );
		  }
		  
		}catch(Exception e){
			logger.error("Failed to insert record in the temporary table :" + GBG_ID +","+ branch+","+consultation_reff+","+ cust_ref_num+","+ record_number+","+file_name+","+ multiple_record );
		}
	}

	private ArrayList<Long> getCustomers(int branch, long consultation_reff) throws Exception{
		ArrayList<Long> arl = new ArrayList<Long>();
		arl= sQLExecutor.getCust_ref_num(branch,consultation_reff);
		return  arl;
	}
	
	
	@Override
	public String toString() {
		return "CustomerRecord [GBG_ID=" + GBG_ID + ", branch=" + branch + ", consultation_reff=" + consultation_reff
				+ ", customer_reff=" + cust_ref_num + ", record_number=" + record_number + ", file_name=" + file_name
				+ ", multiple_record=" + multiple_record + "]";
	}
	
	

}
