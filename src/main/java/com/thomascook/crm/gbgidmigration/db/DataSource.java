package com.thomascook.crm.gbgidmigration.db;
/**
 * Application: TCV to WebRio GBG_ID Migration
 * Version: 1.0.
 * All Rights Reserved 
 * ThomasCook
 * Created by : Anjan
 */

public class DataSource {
	
	private String hostname;
    private int port;
    private String sID;
    private String servicename;
    private String username;
    private String password;
    private int poolsize;
    private String poolname;
    private long timeout;
    private boolean autocommit;
   

    
    public DataSource(String hostname, String port, String sID, String servicename, String username,
			String password, String poolsize, String poolname, String timeout, String autocommit) {
		this.hostname=hostname;
		this.port=Integer.parseInt(port);
		this.sID=sID;
		this.servicename=servicename;
		this.username=username;
		this.password=password;
		this.poolsize=Integer.parseInt(poolsize);
		this.poolname=poolname;
		this.timeout=Long.parseLong(timeout);
		this.autocommit=Boolean.parseBoolean(autocommit);
		
		
		
		
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getsID() {
		return sID;
	}
	public void setsID(String sID) {
		this.sID = sID;
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPoolsize() {
		return poolsize;
	}
	public void setPoolsize(int poolsize) {
		this.poolsize = poolsize;
	}
	public String getPoolname() {
		return poolname;
	}
	public void setPoolname(String poolname) {
		this.poolname = poolname;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public boolean isAutocommit() {
		return autocommit;
	}
	public void setAutocommit(boolean autocommit) {
		this.autocommit = autocommit;
	}
	
	@Override
	public String toString() {
		return "DataSource [hostname=" + hostname + ", port=" + port + ", sID=" + sID + ", servicename=" + servicename
				+ ", password=" + "XXXX" + ", poolsize=" + poolsize + ", poolname=" + poolname + ", timeout="
				+ timeout + ", autocommit=" + autocommit + "]";
	}
	
	
}
